Anime Ultime undocumented API
=============================

Even if it's not mean for us to use, let's document what they exposed for AJAX reasons.

Search
------

### Old endpoint


Available for all queries.
A query without parameter returns all the medias.

Format

```
[
  {
    id: (String) The search id of the media prefixed by "search-". Not linked to the media id.
    value: (String) The title of the media
    image: (String) The relative url of the presentation image of the media
  }
  , ...
]
```

Example

```
curl http://www.anime-ultime.net/search/searchSuggest.php
```


### New endpoint

Format

```
[
  {
    "id": (Integer) Another search id of the media. Not linked to the media id.
    "title": (String) The title of the media
    "type": (String) The type of the media ("Drama"|"OST"|"Anime"|"Tokusatsu")
    "format": (String) The format of the media ("Episode"|"OST"|"Film"|"OAV")
    "url": (String) The full url in the v5 of the media details
    "number": (String|Integer) The number of submedias or the number of current submedias over the total number ("x/y")
    "img_url": (String) The full url of the presentation image of the media suffixed by "_thindex.png"
  }
]
```

Parameters

- search: (String) The search query, must be at least two characters long.

Example

```
curl -X POST --data 'search=te' -H 'X-Requested-With: XMLHttpRequest' http://v5.anime-ultime.net/MenuSearch.html
```

