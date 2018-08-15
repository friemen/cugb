(ns froscon-18.contrib.hangman.backend.wordaxis.parser
  "Contrib: Parse html"
  (:require
   [hickory.core :as h]
   [hickory.select :as hs]
   [hickory.convert :as hc]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Private

(def ^:private html->tree
  (comp h/as-hickory h/parse))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn element-by-id
  "Extracts element with `id` from `html` and
   returns that element in Hiccup representation."
  [id html]
  (when-let [element
             (->> html
                  (html->tree)
                  (hs/select (hs/child (hs/id id)))
                  (first))]

    (hc/hickory-to-hiccup element)))
