(ns froscon-18.hangman.backend.wordaxis.helper
  "Helper for fetching data"
  (:require
   [clojure.string :as str]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Private

(defn- literal?
  [s]
  (not (str/includes? s "?")))

(defn- wildcards-only?
  [s]
  (re-matches #"\?+" s))



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn compute
  "Try to compute results without the need to actually fetch data"
  [pattern]
  (cond
    (str/blank? (str pattern))
    []

    (literal? pattern)
    [pattern]

    (wildcards-only? pattern)
    []))
