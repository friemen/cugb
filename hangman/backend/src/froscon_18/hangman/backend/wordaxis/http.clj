(ns froscon-18.hangman.backend.wordaxis.http
  "Fetch html from wordaxis.com"
  (:require
   [clojure.string :as str]
   [org.httpkit.client :as http]
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

(def fetch!
  "Given a `pattern` return the corresponding html from wordaxis.com.
   The pattern string consists of alphabetic characters and question-marks."
  (memoize
   (fn [pattern]
     (:body @(http/get (pattern->url pattern))))))





(comment
  (fetch! (pattern->url "h?t??g"))
  ,,,)
