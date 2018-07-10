(ns froscon-18.hangman.frontend.state
  "App state"
  (:require
   [reagent.ratom :as ratom]))


(defonce !state
  (ratom/atom {}))
