(ns froscon-18.hangman.backend.wordaxis.parser
  "Parse html from wordaxis.com"
  (:require
   [hickory.core :as h]
   [hickory.select :as hs]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Private

(def ^:private html->tree
  (comp h/as-hickory h/parse))

(def ^:private selector-fn
  (hs/child (hs/id "result") (hs/tag "a")))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn parse
  "Extract word results from wordaxis html"
  [html]
  (->> html
       (html->tree)
       (hs/select selector-fn)
       (mapv (comp first :content))
       (filterv string?)))
