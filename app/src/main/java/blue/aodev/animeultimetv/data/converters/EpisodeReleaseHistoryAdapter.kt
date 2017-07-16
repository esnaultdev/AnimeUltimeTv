package blue.aodev.animeultimetv.data.converters

import blue.aodev.animeultimetv.data.model.EpisodeReleaseId
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class EpisodeReleaseHistoryAdapter : Converter<ResponseBody, List<EpisodeReleaseId>> {

    companion object {
        val FACTORY: Converter.Factory = object : Converter.Factory() {
            override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                               retrofit: Retrofit): Converter<ResponseBody, *>? {
                if (type is ParameterizedType
                        && getRawType(type) === List::class.java
                        && getParameterUpperBound(0, type) === EpisodeReleaseId::class.java) {
                    return EpisodeReleaseHistoryAdapter()
                }

                return null
            }
        }
    }

    override fun convert(responseBody: ResponseBody): List<EpisodeReleaseId> {
        return Jsoup.parse(responseBody.string())
                .select(".history tr")
                .map { convertEpisodeElement(it) }
                .filterNotNull()
    }
    private fun convertEpisodeElement(episodeElement: Element): EpisodeReleaseId? {
        val tds = episodeElement.select("td")
        if (tds.size < 2) return null

        val id = tds[0].child(0).attr("href").split("/")[1].toInt()
        val numbers = tds[1].text()

        return EpisodeReleaseId(id, numbers)
    }
}