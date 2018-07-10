(ns froscon-18.hangman.frontend.views.game
  "Game view"
  (:require
   [froscon-18.hangman.frontend.views.game.slots :as slots]
   [froscon-18.hangman.frontend.views.game.words :as words]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn view
  [slots words loading?]
  [:div.game
   [slots/view slots]
   [words/view words loading?]])
