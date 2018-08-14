(ns froscon-18.hangman.backend.wordaxis.http
  "Fetch html from wordaxis.com"
  (:require
   [clojure.string :as str]
   [froscon-18.contrib.hangman.backend.wordaxis.http :as contrib]
   [cemerick.url :as url]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Private

(def ^:private base-url
  "http://www.wordaxis.com/pattern")


(defn- pattern->url
  "Turn a pattern into a url"
  [pattern]
  (->> pattern
       (str/upper-case)
       (url/url-encode)
       (url/url base-url)
       (str)))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn fetch!
  "Given a `pattern` return the corresponding html from wordaxis.com.
   The pattern string consists of alphabetic characters and question-marks."
  [pattern]
  (contrib/fetch! (pattern->url pattern)))
