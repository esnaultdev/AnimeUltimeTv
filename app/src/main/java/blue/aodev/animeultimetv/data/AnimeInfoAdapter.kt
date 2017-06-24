package blue.aodev.animeultimetv.data

import blue.aodev.animeultimetv.domain.AnimeInfo
import blue.aodev.animeultimetv.domain.AnimeInfoType
import org.jsoup.Jsoup
import okhttp3.ResponseBody
import org.jsoup.nodes.Element
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class AnimeInfoAdapter : Converter<ResponseBody, List<AnimeInfo>> {

    companion object {
        val FACTORY: Converter.Factory = object : Converter.Factory() {
            override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                               retrofit: Retrofit): Converter<ResponseBody, *>? {
                if (type is ParameterizedType
                        && getRawType(type) === List::class.java
                        && getParameterUpperBound(0, type) === AnimeInfo::class.java) {
                    return AnimeInfoAdapter()
                }

                return null
            }
        }

        private val idRegex = Regex("""(?<=/)(.+?)(?=-)""")
        private val imageRegex = Regex("""[^=]+$""")
    }

    override fun convert(responseBody: ResponseBody): List<AnimeInfo> {
        val result = Jsoup.parse(responseBody.string())
                .select(".jtable tr:not(:first-child)")
                .map { convertAnimeElement(it) }
        return result
    }

    private fun convertAnimeElement(animeElement: Element): AnimeInfo {
        val aImageElement = animeElement.child(0).child(0)
        val rawId = aImageElement.attr("href")
        val id = idRegex.find(rawId)?.value?.toInt() ?: -1

        val imageElement = aImageElement.child(0)
        val title = imageElement.attr("title")
        val rawImageUrl = imageElement.attr("data-href")
        val imageUrl = imageRegex.find(rawImageUrl)?.value
                ?.let { "http://www.anime-ultime.net/" + it }

        val rawType = animeElement.child(2).text()
        val type = when (rawType) {
            "Episode" -> AnimeInfoType.ANIME
            "OAV" -> AnimeInfoType.OAV
            "Film" -> AnimeInfoType.MOVIE
            else -> AnimeInfoType.ANIME
        }

        val rawCount = animeElement.child(3).text()
        val splitCount = rawCount.split("/")
        val availableCount = splitCount.first().toDoubleOrNull()?.toInt() ?: 0
        val totalCount = splitCount.last().toDoubleOrNull()?.toInt() ?: 0

        val rawRating = animeElement.child(4).text()
        val rating = rawRating.split("/").firstOrNull()?.toDoubleOrNull() ?: 0.0

        return AnimeInfo(id, title, imageUrl, type, availableCount, totalCount, rating)
    }
}