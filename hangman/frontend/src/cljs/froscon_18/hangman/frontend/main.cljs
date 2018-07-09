(ns froscon-18.hangman.frontend.main
  "Mount the app to the DOM"
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent]

   [froscon-18.hangman.frontend.state :as state]
   [froscon-18.hangman.frontend.views.app :as app]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn mount!
  []
  (reagent/render
    [app/view state/!state]
    (gdom/getElement "app")))
