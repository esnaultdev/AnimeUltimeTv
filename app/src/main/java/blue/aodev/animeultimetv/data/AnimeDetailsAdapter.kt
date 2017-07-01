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
    }

    override fun convert(responseBody: ResponseBody): AnimeDetails {
        val document = Jsoup.parse(responseBody.byteStream(), null, "")
        val metaDescriptionElement = document.select("meta[name=description]")
        val description = metaDescriptionElement.attr("content")
        return parseMetaDescription(description)
    }

    private fun parseMetaDescription(rawDescription: String): AnimeDetails {
        // The description still has escaped characters at this point
        val description = Jsoup.parse(rawDescription).text()

        val synopsis = description
                .split("vostfr").drop(1).joinToString("vostfr")
                .split("TITRE ORIGINAL").first().trim()
        return AnimeDetails(synopsis)
    }
}
