package com.example.weatherforcasting.domain

import com.example.weatherforcasting.data.model.NotificationData
import com.example.weatherforcasting.ui.list.LocalNotificationFragment.Companion.ID
import com.example.weatherforcasting.ui.list.LocalNotificationFragment.Companion.NOTIFICATION
import com.example.weatherforcasting.ui.list.LocalNotificationFragment.Companion.TIME
import com.example.weatherforcasting.ui.list.LocalNotificationFragment.Companion.TITLE_VALUE
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class LogicClass {
    fun parseXml(xml: String): ArrayList<NotificationData> {
        val result = ArrayList<NotificationData>()

        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(xml.reader())
        var eventType = parser.eventType
        var currentTag: String? = parser.name
        var notificationData: NotificationData? = null
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    when (currentTag) {
                         NOTIFICATION -> {
                             notificationData = NotificationData()
                        }
                    }
                }
                XmlPullParser.TEXT -> {
                    val text = parser.text.trim()
                    if (text.isNotEmpty() && currentTag != null) {
                        when (currentTag) {
                            TITLE_VALUE -> notificationData?.title = text
                            TIME -> notificationData?.time = text
                            ID -> notificationData?.id = text
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == NOTIFICATION) {
                        notificationData?.let { result.add(it) }
                        notificationData = null
                    }
                    currentTag = null
                }
            }
            eventType = parser.next()
        }
        return result
    }
}