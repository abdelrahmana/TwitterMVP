package com.example.twitter.ui.twitter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twitter.data.Constants
import com.example.twitter.domain.LogicClass
import com.example.twitter.ui.twitterauth.TwitterAuthGetCodeFragment
import com.example.weatherforcasting.R
import com.example.weatherforcasting.databinding.TwitterFragmentBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class TwitterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Inject
    lateinit var logicClass: LogicClass
    var disposables: CompositeDisposable? = null
    val viewModel: TwitterViewModel by viewModels()
    private var binding: TwitterFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = TwitterFragmentBinding.inflate(layoutInflater, container, false)
        disposables = CompositeDisposable()
        disposables?.add(
            RxTextView.textChanges(binding?.editTextInput!!)
                .skip(1)
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe { query ->
                    val usedCharacters =
                        logicClass.calculateTwitterCharacters(binding?.editTextInput?.text.toString())
                    binding?.remainingText?.text =
                        maxOf(maxTweetLength - usedCharacters, 0).toString()
                    binding?.writtenCharcters?.text = getString(
                        R.string.current_edit_text, usedCharacters,
                        maxTweetLength
                    )
                    binding?.editTextInput?.filters = arrayOf(InputFilter.LengthFilter(if (usedCharacters == maxTweetLength) binding?.remainingText?.text.toString().toInt()
                    else maxTweetLength))
                }
        )
        binding?.clearButton?.setOnClickListener {
            binding?.editTextInput?.text?.clear()
        }
        binding?.postTweet?.setOnClickListener {
            if (binding?.editTextInput?.text?.isNotEmpty() == true) // if not empty
                findNavController().navigate(R.id.action_twitter_get_code)
            //    viewModel.postTweetSdk(binding?.editTextInput?.text.toString())
            //    viewModel.postTweetApi(binding?.editTextInput?.text.toString())

        }
        binding?.copyText?.setOnClickListener {
            copyTextToClipboard(binding?.editTextInput?.text.toString())
        }
        setViewObserver()
        return binding?.root
    }

    private fun copyTextToClipboard(textToCopy: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // Create ClipData
        val clip = ClipData.newPlainText("Copied Text", textToCopy)
        // Set clipboard's primary clip
        clipboard.setPrimaryClip(clip)
        // Show a confirmation
        Toast.makeText(requireContext(), getString(R.string.text_copied), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        disposables?.clear()
        viewModel.clearEmpty()
        super.onDestroyView()
    }

    private fun setViewObserver() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.tweetDataStateFlow.collectLatest {
                it?.let {
                    Toast.makeText(requireContext(), getString(R.string.tweet_posted_successfully), Toast.LENGTH_LONG).show()

                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.authTokenState.collect {
                it?.let {
                    // access token is here with us
//                    Toast.makeText(requireContext(), "access token is "+ it.accessToken, Toast.LENGTH_LONG).show()
                    Constants.VALID_ACCESS_TOKEN = it.accessToken?:""
                    viewModel.postTweetApi(binding?.editTextInput?.text.toString())
                }
            }
        }
        viewModel.networkLoader.observe(viewLifecycleOwner, Observer {
            it?.let { progress ->
                binding?.progressLoader?.visibility = it
            }
        })
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.errorStateFlow.collectLatest {
                it?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()

                }
            }
        }
        setFragmentResultListener(
            TwitterAuthGetCodeFragment.CALLBACK_RESPONSE,

            ) { _, bundle ->

            bundle.getString(TwitterAuthGetCodeFragment.CODE_VALUE).let { code ->
                code?.let { codeValue ->
                   // should call api to get access token from here
                   viewModel.postGetAccessTokenResponse(HashMap<String,Any>().also { it.put(Constants.CODE_KEY,codeValue)
                        it.put(Constants.GRANT_TYPE_KEY,AUTH_CODE)
                        it.put(Constants.CLIENT_ID_KEY,Constants.clientId)
                        it.put(Constants.REDIRECTURL_KEY,REDIRECT_URL)
                        it.put(Constants.CODEVERIFIER_KEY,CODEVERIFIER)
                    })
                }
            }
        }
    }


    companion object {
        val maxTweetLength = 280
        const val AUTH_CODE = "authorization_code"
        const val REDIRECT_URL = "http://127.0.0.1"
        const val CODEVERIFIER = "challenge"

    }
}