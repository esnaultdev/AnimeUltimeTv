package blue.aodev.animeultimetv.data.converters

import blue.aodev.animeultimetv.domain.model.AnimeSummary
import okhttp3.ResponseBody
import retrofit2.Converter

internal class AnimeSummaryHistoryAdapter : Converter<ResponseBody, List<AnimeSummary>> {

    override fun convert(responseBody: ResponseBody): List<AnimeSummary> {
        // TODO parse the response
        return emptyList()
    }
}