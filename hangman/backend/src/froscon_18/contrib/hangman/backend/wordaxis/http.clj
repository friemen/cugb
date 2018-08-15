(ns froscon-18.contrib.hangman.backend.wordaxis.http
  "Fetch html over http."
  (:require
   [org.httpkit.client :as http]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(def fetch!
  "Synchronously fetch webpage at `uri` over http and return its html body."
  (memoize
   (fn [uri]
     (:body @(http/get uri)))))
