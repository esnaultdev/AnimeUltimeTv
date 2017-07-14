package blue.aodev.animeultimetv.data.converters

import blue.aodev.animeultimetv.domain.model.AnimeSummary
import blue.aodev.animeultimetv.domain.model.AnimeType
import org.jsoup.Jsoup
import okhttp3.ResponseBody
import org.jsoup.nodes.Element
import retrofit2.Converter

internal class AnimeSummaryAllAdapter : Converter<ResponseBody, List<AnimeSummary>> {

    companion object {
        private val idRegex = Regex("""(?<=/)(.+?)(?=-)""")
        private val imageRegex = Regex("""[^=]+$""")
    }

    override fun convert(responseBody: ResponseBody): List<AnimeSummary> {
        val result = Jsoup.parse(responseBody.string())
                .select(".jtable tr:not(:first-child)")
                .map { convertAnimeElement(it) }
        return result
    }

    private fun convertAnimeElement(animeElement: Element): AnimeSummary {
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
            "Episode" -> AnimeType.ANIME
            "OAV" -> AnimeType.OAV
            "Film" -> AnimeType.MOVIE
            else -> AnimeType.ANIME
        }

        val rawCount = animeElement.child(3).text()
        val splitCount = rawCount.split("/")
        val availableCount = splitCount.first().toDoubleOrNull()?.toInt() ?: 0
        val totalCount = splitCount.last().toDoubleOrNull()?.toInt() ?: 0

        val rawRating = animeElement.child(4).text()
        val rating = rawRating.split("/").firstOrNull()?.toFloatOrNull() ?: 0f

        return AnimeSummary(id, title, imageUrl, type, availableCount, totalCount, rating)
    }
}