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
        val document = Jsoup.parse(responseBody.string())
        val metaDescriptionElement = document.select("meta[name=description]")
        val description = metaDescriptionElement.attr("content")
        return parseMetaDescription(description)
    }

    private fun parseMetaDescription(description: String): AnimeDetails {
        val synopsis = description
                .split("vostfr").drop(1).joinToString("vostfr")
                .split("TITRE ORIGINAL").first().trim()
        return AnimeDetails(synopsis)
    }
}

/*
<meta name="description" content="Tamako Market vostfr Synopsis:
Nous suivons l&amp;#039;histoire d&amp;#039;une jeune coll&eacute;gienne, Tamako Kitashirakawa, habitant une rue commer&ccedil;ante tr&egrave;s conviviale. Son p&egrave;re y tient un magasin familiale de mochi ( pr&eacute;paration &agrave; base de riz gluant ).
Un jour, elle fait la rencontre d&amp;#039;un oiseau qui a la capacit&eacute; de parler. Cette rencontre va bousculer la vie de la jeune fille ainsi que celle du quartier et de ses amis, car cet oiseau doit mener &agrave; bout une mission.

-Fre1-

TITRE ORIGINAL : Tamako Market
ANN&Eacute;E DE PRODUCTION : 2013
STUDIO : [KYOTO ANIMATION]
GENRE : [COM&eacute;DIE]
AUTEUR : [KYOTO ANIMATION]
VOLUMES, TYPE &amp; DUR&Eacute;E : 12 EPS 24 mins

Source: Animeka" />
*/