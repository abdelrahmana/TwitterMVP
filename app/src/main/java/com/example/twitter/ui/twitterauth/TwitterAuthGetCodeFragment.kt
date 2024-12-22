package com.example.twitter.ui.twitterauth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.twitter.data.Constants
import com.example.weatherforcasting.databinding.FragmentTwitterAuthGetCodeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TwitterAuthGetCodeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentTwitterAuthGetCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTwitterAuthGetCodeBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        setWebViewInfo(binding, URL_INFO)
        return binding.root
    }

    private fun setWebViewInfo(binding: FragmentTwitterAuthGetCodeBinding, it: String) {
        binding.webView.setWebViewClient(WebViewClient())
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(it)
        binding.webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // Here put your code
                if (url?.contains("code=") == true) {
                    findNavController().popBackStack()
                    setFragmentResult(
                        CALLBACK_RESPONSE,
                        bundleOf(CODE_VALUE to extractCodeSafely(url))
                    )
                }
                return true //Allow WebView to load url
            }
        }
    }

    fun extractCodeSafely(url: String, delimeter: String = "code="): String? {
        val codePart = url.substringAfter(delimeter, missingDelimiterValue = "")
        return if (codePart.isNotEmpty()) codePart.substringBefore("&") else null
    }

    companion object {
        const val CALLBACK_RESPONSE = "CALLBACK_RESPONSE"
        const val CODE_VALUE = "CODE_VALUE"
        const val URL_INFO =
            "https://twitter.com/i/oauth2/authorize?response_type=code&client_id=${Constants.clientId}&redirect_uri=http://127.0.0.1&scope=tweet.read%20tweet.write%20users.read%20follows.read%20offline.access&state=state&code_challenge=challenge&code_challenge_method=plain"
    }
}