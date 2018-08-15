(ns froscon-18.hangman.backend.wordaxis.parser
  "Parse html from wordaxis.com"
  (:require
   [froscon-18.contrib.hangman.backend.wordaxis.parser :as contrib]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Private

(defn- anchor?
  [element]
  (and (vector? element)
       (= :a (first element))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn parse
  "Extract word results from wordaxis html"
  [html]
  (->> html
       (contrib/element-by-id "result")
       (filter anchor?)
       (mapv (comp last flatten))))
