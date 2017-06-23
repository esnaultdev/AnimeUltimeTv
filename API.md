Anime Ultime undocumented API
=============================

Even if it's not meant for us to use, let's document what they exposed for AJAX reasons.

Search
------

### V4 search endpoint


Available for all queries.
A query without parameter returns all the medias.

#### Format

```
[
  {
    id: (String) V4 search id. Not linked to the media id.
    value: (String) The title of the media
    image: (String) The relative url of the presentation image of the media
  }
  , ...
]
```

#### Example

```
curl http://www.anime-ultime.net/search/searchSuggest.php
```


### V5 Search endpoint

#### Format

```
[
  {
    "id": (Integer) V5 media id
    "title": (String) The title of the media
    "type": (String) The type of the media ("Drama"|"OST"|"Anime"|"Tokusatsu")
    "format": (String) The format of the media ("Episode"|"OST"|"Film"|"OAV")
    "url": (String) The full url in the v5 of the media details
    "number": (String|Integer) The number of submedias or the number of current submedias over the total number ("x/y")
    "img_url": (String) The full url of the presentation image of the media suffixed by "_thindex.png"
  }
]
```

#### Parameters

search: (String) The query, must be at least two characters long.

#### Example

```
curl -X POST --data 'search=te' -H 'X-Requested-With: XMLHttpRequest' http://v5.anime-ultime.net/MenuSearch.html
```

### V5 Advanced Search endpoint

Advanced search, using filters. Produces a better output to work with for episode numbers.

#### Format

```
[
  {
    "id": (Integer) V5 media id
    "title": (String) Media title
    "total": (Integer) Number of planned submedias
    "current": (Integer) Number of available submedias
    "note": (Float) Rating between 0 and 5
    "img_url": (String) The full url of the presentation image of the media suffixed by "_thindex.png"
    "url": (String) The full url in the v5 of the media details
  }
]
```

#### Parameters

type: ("Anime"|"Drama"|"OST"|"Tokusatsu")
search: (String)

details[genre]: ("ACTION"|...)
details[studio]: ("YAMAHA MOTOR"|...)
details[year]: (Integer)

format[OAV]:true
format[Film]:true
format[Episode]:true

#### Example

Request all animes
```
curl -X POST --data 'search=' --data 'type=Anime' -H 'X-Requested-With: XMLHttpRequest' http://v5.anime-ultime.net/SeriesResults.html
```

------------------------


Playlist
--------

### V4

#### Format

```
<channel>
    <title>Playlist</title>
    <item>
      <title>$TITLE</title>
      <description>$FANSUB_INFO</description>
      <link>http://www.anime-ultime.net</link>
      <pubDate>$PUBLICATION_DATE</pubDate>
      <media:content url="$MEDIA_NON_HD_URL" duration="$DURATION" />
      <media:thumbnail url="$IMAGE_URL" />
            <jwplayer:mediaid>$MEDIA_ID</jwplayer:file>
            <jwplayer:file>$MEDIA_NON_HD_URL</jwplayer:file>
            <jwplayer:hd.file>$MEDIA_HD_URL<jwplayer:hd.file>
      <jwplayer:provider>http</jwplayer:provider>
      <jwplayer:http.startparam>start</jwplayer:http.startparam>
    </item>
    ...
</channel>

$TITLE: The title of the episode
$FANSUB_INFO: The information about the fansub
$PUBLICATION_DATE: The publication date of the item, ISO8601
$MEDIA_NON_HD_URL: Full url of the media, non HD
$MEDIA_HD_URL: Full url of the media, HD
$DURATION: The duration of the media in seconds
$MEDIA_ID: An integer, no logical link with any other data
$IMAGE_URL: Full path of the thumbnail image, resized
```

#### Parameters

The v4 media id, in the url.

#### Example

```
curl http://www.anime-ultime.net/playlist-1116.xml
```


----------------------


Overview
--------

### V5

Get information about the media.

#### Format

HTML *sigh*

#### Parameters

id: (Integer) The V5 media id.

#### Example

```
curl -X POST --data 'id=408' -H 'X-Requested-With: XMLHttpRequest' http://v5.anime-ultime.net/SerieOverview.html
```

