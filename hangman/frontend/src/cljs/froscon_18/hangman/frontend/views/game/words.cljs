(ns froscon-18.hangman.frontend.views.game.words
  "Game subview for words"
  (:require
   [clojure.string :as str]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Private

(defn- spinner-view
  []
  [:span.fa.fa-spin.fa-spinner.words__spinner])


(defn- wiki-href
  [word]
  (->> (str/lower-case word)
       (str "https://en.wikipedia.org/w/index.php?search=")))


(defn- word-view
  [word]
  [:div.word
   [:a {:href (wiki-href word) :target "_blank"}
    word]])


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn view
  [words loading?]
  [:div.words
   (cond
     loading?
     [spinner-view]

     (empty? words)
     "No results"

     :else
     (for [word words]
       ^{:key word}
       [word-view word]))])
