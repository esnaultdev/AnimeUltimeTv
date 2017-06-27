package blue.aodev.animeultimetv.data

import android.util.Xml
import okhttp3.ResponseBody
import org.xmlpull.v1.XmlPullParser
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class EpisodeInfoAdapter : Converter<ResponseBody, List<EpisodeInfo>> {

    companion object {
        val FACTORY: Converter.Factory = object : Converter.Factory() {
            override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                               retrofit: Retrofit): Converter<ResponseBody, *>? {
                if (type is ParameterizedType
                        && getRawType(type) === List::class.java
                        && getParameterUpperBound(0, type) === EpisodeInfo::class.java) {
                    return EpisodeInfoAdapter()
                }

                return null
            }
        }

        private val mediaIdRegex = Regex("""mediaid>\d*</jwplayer:file>""")
    }

    override fun convert(responseBody: ResponseBody): List<EpisodeInfo> {
        // The XML file returned by the server is invalid and we have to fix it
        val validResponse = responseBody.string().replace(mediaIdRegex,
                { match -> match.value.replace("file", "mediaid")})

        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(validResponse.byteInputStream(), null)
        parser.nextTag()
        return readEpisodes(parser)
    }

    private fun readEpisodes(parser: XmlPullParser): List<EpisodeInfo> {
        val episodes = arrayListOf<EpisodeInfo>()

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
        return episodes
    }

    private fun readEpisode(parser: XmlPullParser): EpisodeInfo? {
        var title: String? = null
        var rawImageUrl: String? = null
        var videoUrl: String? = null
        var hdVideoUrl: String? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            when (name) {
                "title" -> title = readTitle(parser)
                "media:thumbnail" -> rawImageUrl = readThumbnail(parser)
                "jwplayer:file" -> videoUrl = readVideoUrl(parser)
                "jwplayer:hd.file" -> hdVideoUrl = readHdVideoUrl(parser)
                else -> skip(parser)
            }
        }

        if (title == null || rawImageUrl == null || videoUrl == null || hdVideoUrl == null) {
            return null
        }

        // TODO process image url to remove resize
        // http://www.anime-ultime.net/img_resize.php?img=images/img12459.png
        val imageUrl = rawImageUrl

        return EpisodeInfo(title, imageUrl, videoUrl, hdVideoUrl)
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, "title")
        return title
    }

    private fun readThumbnail(parser: XmlPullParser): String {
        var thumbnailUrl = ""
        parser.require(XmlPullParser.START_TAG, null, "media:thumbnail")
        thumbnailUrl = parser.getAttributeValue(null, "url")
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

/*

<rss version="2.0" xmlns:media="http://search.yahoo.com/mrss/" xmlns:jwplayer="http://developer.longtailvideo.com/">
  <channel>
    <title>Playlist</title>
    <item>
      <title>Terra E... 01</title>
      <description>Fansub Par Chikyuu-Fansub</description>
      <link>http://www.anime-ultime.net</link>
      <pubDate>2010-08-29T18:45:50+01:00</pubDate>
      <media:content url="http://www.anime-ultime.net/stream-7073.mp4" duration="1444" />
      <media:thumbnail url="http://www.anime-ultime.net/img_resize.php?img=images/img12459.png" />
	    <jwplayer:mediaid>8817</jwplayer:file>
	    <jwplayer:file>http://www.anime-ultime.net/stream-26464.mp4</jwplayer:file>
	    <jwplayer:hd.file>http://www.anime-ultime.net/stream-7073.mp4</jwplayer:hd.file>
      <jwplayer:provider>http</jwplayer:provider>
      <jwplayer:http.startparam>start</jwplayer:http.startparam>
    </item>

*/
