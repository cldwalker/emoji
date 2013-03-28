## Description

This library provides middleware/interceptorware to replace a response
containing emoji names with bundled emoji images.

## Install

Add to your project.clj:

    [emoji "0.1.0"]

## Usage

The below usages are taken from the full pedestal service and
compojure app examples in [examples](examples/).

To use as an interceptor for a pedestal service:

```clojure
(require '[io.pedestal.service.interceptor :refer [defon-response]])
(require '[emoji.core :refer [emoji-response]])
(defon-response emoji-interceptor
  [response]
  (emoji-response response))

;; add emoji-interceptor to a route
["/" {:get some-endpoint ^:interceptors [emoji-interceptor]]
```

With emoji in place, a response body such as `This page is on :fire:`
becomes `This page is on <img height='20' src='/images/emoji/fire.png' style='vertical-align:middle' width='20' />`.

To use as middleware for a ring app:

```clojure
(require '[emoji.core :refer [emoji-response]])
(require '[ring.middleware.resource :refer [wrap-resource]])

;; Assuming a compojure routes table called app-routes
;; resource middleware is needed to serve up bundled emojis
(-> app-routes
    wrap-emoji
    (wrap-resource "/public"))
```

Options `emoji-response` and `wrap-emoji` can take:

* :wild - Converts every word that is a valid emoji name, no
  colon-delimitation necessary. For example "This page is on fire" would yield
  two emojis since on and fire are emoji names.
* :replace-fn - Customize the replacement text for an emoji. Useful
  for customizing the emoji image tag.
* :images-dir - Specify a local directory for emoji images.

To copy the bundled emojis: `lein trampoline run -m emoji.core [DIRECTORY]`

## Bugs/Issues

Please report them
[on github](http://github.com/cldwalker/emoji/issues).

## Credits
* This project would not be possible without @github providing [their emojis](https://github.com/github/gemoji/).

## License

For this library's license see LICENSE.TXT. For license of emoji
images see [gemoji's license](https://github.com/github/gemoji/blob/master/LICENSE).

## Links
* [demo app](https://github.com/cldwalker/emojinator)
