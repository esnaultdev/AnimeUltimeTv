package blue.aodev.animeultimetv.data

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class AnimeDetailsAdapter : Converter<ResponseBody, AnimeDetails> {

    companion object {
        val FACTORY: Converter.Factory = object : Converter.Factory() {
            override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                               retrofit: Retrofit): Converter<ResponseBody, *>? {
                if (type === AnimeDetails::class.java) {
                    return AnimeDetailsAdapter()
                }

                return null
            }
        }

        val bracketRegex = Regex("""\[(.*?)\]""")
    }

    override fun convert(responseBody: ResponseBody): AnimeDetails {
        val document = Jsoup.parse(responseBody.byteStream(), null, "")
        val metaDescriptionElement = document.select("meta[name=description]")
        val description = metaDescriptionElement.attr("content")
        return parseMetaDescription(description)
    }

    private fun parseMetaDescription(description: String): AnimeDetails {
        val titleLessDescription = description.split("vostfr").drop(1).joinToString("vostfr")
        var splitDescription = titleLessDescription.split("\r\n").toMutableList()

        val synopsis = splitDescription
                .takeWhile { !it.trim().startsWith("TITRE ORIGINAL") }
                .map { Jsoup.parse(it).text() } // Remove escaped characters
                .fold(StringBuilder(), { sb, it -> sb.append("$it\n") })
                .toString()
                .trim(' ', '\n', '\r')

        splitDescription = splitDescription
                .dropWhile { !(it.contains("ANNÉE DE PRODUCTION")
                        || it.contains("ANNÉES DE PRODUCTION")) }
                .toMutableList()

        val years = parseYears(splitDescription[0])
        splitDescription.removeAt(0)

        val studios = getValuesInBrackets(splitDescription.first())
                .map { it.toLowerCase().capitalize() }
        splitDescription.removeAt(0)

        val genres =getValuesInBrackets(splitDescription.first())
                .map { it.toLowerCase().capitalize() }
        splitDescription.removeAt(0)

        val author = getValuesInBrackets(splitDescription.first())
                .map { it.toLowerCase().capitalize() }
                .first()

        return AnimeDetails(synopsis, years, studios, genres, author)
    }

    private fun parseYears(yearString: String): IntRange {
        val rawYears = yearString.split("PRODUCTION").last().trim(' ', ':')
        val yearsSplit = rawYears.split(" - ")
        val years = if (yearsSplit.size == 1) {
            val year = parseYear(yearsSplit.first())
            year..year
        } else {
            parseYear(yearsSplit.first())..parseYear(yearsSplit.last())
        }
        return years
    }

    private fun parseYear(yearString: String): Int {
        return yearString.trim().toInt()
    }

    private fun getValuesInBrackets(input: String): List<String> {
        return bracketRegex.findAll(input)
                .map { it.value }
                .map { it.drop(1).dropLast(1) } // Remove the [] around the names
                .toList()
    }
}
