package blue.aodev.animeultimetv.data.converters

import blue.aodev.animeultimetv.data.model.TopAnimeId
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class TopAnimeAdapter : Converter<ResponseBody, List<TopAnimeId>> {

    companion object {
        val FACTORY: Converter.Factory = object : Converter.Factory() {
            override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                               retrofit: Retrofit): Converter<ResponseBody, *>? {
                if (type is ParameterizedType
                        && getRawType(type) === List::class.java
                        && getParameterUpperBound(0, type) === TopAnimeId::class.java) {
                    return TopAnimeAdapter()
                }

                return null
            }
        }
    }

    override fun convert(responseBody: ResponseBody): List<TopAnimeId> {
        return Jsoup.parse(responseBody.string())
                .select(".main_box")[1]
                .select("a")
                .map { it.attr("href").split("/")[1].toInt() }
                .map { TopAnimeId(it) }
    }
}