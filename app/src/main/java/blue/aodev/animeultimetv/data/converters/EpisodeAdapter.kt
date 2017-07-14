package blue.aodev.animeultimetv.data.converters

import android.util.Xml
import blue.aodev.animeultimetv.domain.model.Episode
import okhttp3.ResponseBody
import org.xmlpull.v1.XmlPullParser
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class EpisodeAdapter : Converter<ResponseBody, List<Episode>> {

    companion object {
        val FACTORY: Converter.Factory = object : Converter.Factory() {
            override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                               retrofit: Retrofit): Converter<ResponseBody, *>? {
                if (type is ParameterizedType
                        && getRawType(type) === List::class.java
                        && getParameterUpperBound(0, type) === Episode::class.java) {
                    return EpisodeAdapter()
                }

                return null
            }
        }

        private val mediaIdRegex = Regex("""mediaid>\d*</jwplayer:file>""")
        private val imageRegex = Regex("""[^=]+$""")
    }

    override fun convert(responseBody: ResponseBody): List<Episode> {
        // The XML file returned by the server is invalid and we have to fix it
        val validResponse = responseBody.string().replace(mediaIdRegex,
                { match -> match.value.replace("file", "mediaid")})

        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(validResponse.byteInputStream(), null)
        parser.nextTag()
        return readEpisodes(parser)
    }

    private fun readEpisodes(parser: XmlPullParser): List<Episode> {
        val episodes = arrayListOf<Episode>()

        parser.require(XmlPullParser.START_TAG, null, "rss")
        parser.nextTag()
        parser.require(XmlPullParser.START_TAG, null, "channel")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "item") {
                readEpisode(parser)?.let { episodes.add(it) }
            } else {
                skip(parser)
            }
        }
        return episodes.sortedBy { it.number }
    }

    private fun readEpisode(parser: XmlPullParser): Episode? {
        var title: String? = null
        var rawImageUrl: String? = null
        var videoUrl: String? = null
        var hdVideoUrl: String? = null
        var duration: Int? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            when (name) {
                "title" -> title = readTitle(parser)
                "media:content" -> duration = readDuration(parser)
                "media:thumbnail" -> rawImageUrl = readThumbnail(parser)
                "jwplayer:file" -> videoUrl = readVideoUrl(parser)
                "jwplayer:hd.file" -> hdVideoUrl = readHdVideoUrl(parser)
                else -> skip(parser)
            }
        }

        if (title == null || rawImageUrl == null || videoUrl == null || hdVideoUrl == null
                || duration == null) {
            return null
        }

        val number = title.split(" ").last().toIntOrNull() ?: 0

        val imageUrl = imageRegex.find(rawImageUrl)?.value
                ?.let { "http://www.anime-ultime.net/" + it }
                ?: rawImageUrl

        return Episode(title, number, imageUrl, videoUrl, hdVideoUrl, duration)
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, "title")
        return title
    }

    private fun readDuration(parser: XmlPullParser): Int {
        parser.require(XmlPullParser.START_TAG, null, "media:content")
        val duration = parser.getAttributeValue(null, "duration").toInt()
        parser.nextTag()
        parser.require(XmlPullParser.END_TAG, null, "media:content")
        return duration
    }

    private fun readThumbnail(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, "media:thumbnail")
        val thumbnailUrl = parser.getAttributeValue(null, "url")
        parser.nextTag()
        parser.require(XmlPullParser.END_TAG, null, "media:thumbnail")
        return thumbnailUrl
    }

    private fun readVideoUrl(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, "jwplayer:file")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, "jwplayer:file")
        return title
    }

    private fun readHdVideoUrl(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, "jwplayer:hd.file")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, "jwplayer:hd.file")
        return title
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

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }
}
