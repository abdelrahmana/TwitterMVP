package com.example.twitter

import com.banquemisr.challenge05.data.source.remote.TwitterEndPoint
import com.example.twitter.data.cititesrepo.TwitterRepo
import com.example.twitter.data.cititesrepo.TwitterRepoImplementer
import com.example.twitter.domain.LogicClass
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TwitterFragmentTest {
    @Mock
    private lateinit var weatherService: TwitterEndPoint
    lateinit var weatherRepo: TwitterRepo

    private lateinit var logicClass: LogicClass

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        logicClass = LogicClass()
        weatherRepo = TwitterRepoImplementer(weatherService)

    }

    @Test
    fun `Given Name With Normal Number When User Write Text Then Charcters Should Be Counted With One`() {
        val name = "JohnDoe"
        val count = logicClass.calculateTwitterCharacters("hello")
        assertEquals(5, count)

    }

    @Test
    fun `Given Url When User Write Text Then Charcters Should Be Counted With Twenty Three`() {
        val name = "https://twitter.com"
            val count = logicClass.calculateTwitterCharacters(name)
            assertEquals(23, count)

    }
    @Test
    fun `Given Name With Url When User Write Text Then Charcters Should Be Counted In Write Way`() {
        val name = "Hello! This is a test tweet with a URL: https://example.com and some emojis 😊."
        val count = logicClass.calculateTwitterCharacters(name)
        assertEquals(83, count)

    }
    @Test
    fun `Given Name With Url With Emotion When User Write Text Then Charcters Should Be Counted In Write Way`() {
        val name = "I love Kotlin! 💻❤️ https://twitter.com"
        val count = logicClass.calculateTwitterCharacters(name)
        assertEquals(43, count)

    }

    /* @Test
     fun `testDataBase_of_weather_info`() {
         runBlocking {
             val weather = weatherRepo.getWeatherFromLocal(50)
             /*val city = City("test_cairo", "test_cairo", 1, 1.0, 1.0)
             cityDao.insertCity(city)
             val fetchedCity = cityDao.getCityById(1)*/
             assertNotNull(weather)
             assertNotNull(weather.error)

     }
 }*/

    @After
    fun afterTest() {

    }
}