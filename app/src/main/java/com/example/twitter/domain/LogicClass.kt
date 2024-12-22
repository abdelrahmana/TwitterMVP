package com.example.twitter.domain

import java.util.regex.Pattern

class LogicClass {
    fun calculateTwitterCharacters(tweet: String): Int {
        // Regular expression to identify URLs
        val urlPattern = Pattern.compile("https?://\\S+|www\\.\\S+")
        val matcher = urlPattern.matcher(tweet)

        // Replace URLs with a placeholder that counts as 23 characters
        val modifiedTweet = matcher.replaceAll(" ".repeat(23))

        var charCount = 0

        for (char in modifiedTweet) {
            when {
                isCJK(char) || isEmoji(char) -> charCount += 2 // CJK characters and emojis count as 2
                else -> charCount += 1 // Standard characters count as 1
            }
        }

        return charCount
    }

    private fun isCJK(char: Char): Boolean {
        // Check if the character is a CJK character
        return char in '\u4E00'..'\u9FFF' // CJK Unified Ideographs
    }

    private fun isEmoji(char: Char): Boolean {
        // Check if the character is an emoji
        return char.code in 0x1F600..0x1F64F || // Emoticons
                char.code in 0x1F300..0x1F5FF || // Misc Symbols and Pictographs
                char.code in 0x1F680..0x1F6FF || // Transport and Map Symbols
                char.code in 0x1F700..0x1F77F || // Alchemical Symbols
                char.code in 0x1F780..0x1F7FF || // Geometric Shapes Extended
                char.code in 0x1F800..0x1F8FF || // Supplemental Arrows-C
                char.code in 0x1F900..0x1F9FF || // Supplemental Symbols and Pictographs
                char.code in 0x1FA00..0x1FA6F || // Chess Symbols
                char.code in 0x1FA70..0x1FAFF // Symbols and Pictographs Extended-A
    }
}