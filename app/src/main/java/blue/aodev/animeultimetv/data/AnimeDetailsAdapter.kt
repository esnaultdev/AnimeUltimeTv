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
        var splitDescription = description.split("\r\n").toMutableList()

        // The first element is the name of the anime, which we already know
        splitDescription.removeAt(0)

        val synopsis = splitDescription
                .takeWhile { !it.startsWith("TITRE ORIGINAL :") }
                .map { Jsoup.parse(it).text() } // Remove escaped characters
                .fold(StringBuilder(), { sb, it -> sb.append("$it\n") })
                .toString()
                .trim(' ', '\n')

        splitDescription = splitDescription
                .dropWhile { !it.contains("ANNÉE DE PRODUCTION : ", true) }
                .toMutableList()

        val year = splitDescription[0].split(" : ")[1].toInt()
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

        return AnimeDetails(synopsis, year, studios, genres, author)
    }

    private fun getValuesInBrackets(input: String): List<String> {
        return bracketRegex.findAll(input)
                .map { it.value }
                .map { it.drop(1).dropLast(1) } // Remove the [] around the names
                .toList()
    }
}
