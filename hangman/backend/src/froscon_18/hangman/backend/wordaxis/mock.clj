(ns froscon-18.hangman.backend.wordaxis.mock
  "Mock data"
  (:require
   [clojure.string :as str]
   [froscon-18.hangman.backend.wordaxis.helper :as helper]
   [froscon-18.hangman.backend.wordaxis.http :as http]
   [froscon-18.hangman.backend.wordaxis.parser :as parser]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn query!
  [pattern]
  (letfn [(rand-char [& _]
            (str (char (rand-nth (range 65 91)))))

          (word-fn []
            (str/replace pattern #"\?" rand-char))]

    (or (helper/compute pattern)
        (let [n
              (rand-nth (range 2 8))]

          (Thread/sleep 250)
          (vec (repeatedly n word-fn))))))




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
