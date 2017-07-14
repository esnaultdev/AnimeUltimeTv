package blue.aodev.animeultimetv.data.converters

import blue.aodev.animeultimetv.data.annotations.All
import blue.aodev.animeultimetv.data.annotations.History
import blue.aodev.animeultimetv.domain.model.AnimeSummary
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object AnimeSummaryConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                       retrofit: Retrofit): Converter<ResponseBody, *>? {
        if (type is ParameterizedType
                && getRawType(type) === List::class.java
                && getParameterUpperBound(0, type) === AnimeSummary::class.java) {
            for (annotation in annotations) {
                when (annotation) {
                    is All -> return AnimeSummaryAllAdapter()
                    is History -> return AnimeSummaryHistoryAdapter()
                }
            }
        }

        return null
    }

}