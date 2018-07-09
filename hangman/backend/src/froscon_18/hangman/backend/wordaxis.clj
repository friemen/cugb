(ns froscon-18.hangman.backend.wordaxis
  (:require
   [clojure.string :as str]
   [froscon-18.hangman.backend.wordaxis.helper :as helper]
   [froscon-18.hangman.backend.wordaxis.http :as http]
   [froscon-18.hangman.backend.wordaxis.parser :as parser]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Private

(def ^:private -query!
  (comp parser/parse http/fetch!))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn query!
  [pattern]
  (or (helper/compute pattern)
      (-query! pattern)))








;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Playground

(comment
  (query! "tam????i??")
  (query! "cac??s")
  (query! "H?T??G")
  (query! "?er??n?t?r")
  (query! "?u??l???r")
  (query! "?l?w?rs")

  (query! "p???ra??ing")
  (query! "??og????ing")
  (query! "sun??t")
  (query! "din?e?")
  (query! "m??ic?n?"))
