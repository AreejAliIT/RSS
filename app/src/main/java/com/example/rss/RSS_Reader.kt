package com.example.rss

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class RSS_Reader {

    private val ns: String? = null

    fun parse(inputStream: InputStream): List<StackoverflowData> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readSongsRssFeed(parser)
        }
    }

    private fun readSongsRssFeed(parser: XmlPullParser): List<StackoverflowData> {

        val questions = mutableListOf<StackoverflowData>()
        parser.require(XmlPullParser.START_TAG, ns, "feed")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "entry") {
                parser.require(XmlPullParser.START_TAG, ns, "entry")
                var title: String? = null
                var author: String? = null
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "title" -> title = readTitle(parser)
                        "author" -> author = readName(parser)
                        else -> skip(parser)
                    }
                }
                questions.add(StackoverflowData(title,author))
            } else {
                skip(parser)
            }
        }
        return questions
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readName(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "author")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "author")
        return summary
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}