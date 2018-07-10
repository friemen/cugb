(ns froscon-18.hangman.frontend.transformations
  "Transform app-state")


(defn state->slots
  [state]
  (:game/slots state))


(defn state->words
  [state]
  (->> (:game/words state)
       (take 10)
       (vec)))

(defn state->loading?
  [state]
  (:game/loading state))
