(ns froscon-18.hangman.frontend.views.app
  (:require
   [froscon-18.hangman.frontend.transformations :as t]

   [froscon-18.hangman.frontend.views.dbg :as dbg]
   [froscon-18.hangman.frontend.views.game :as game]))



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn view
  [!state]
  (let [state
        @!state

        slots
        (t/state->slots state)

        words
        (t/state->words state)

        loading?
        (t/state->loading? state)]

    [:div.app
     [game/view slots words loading?]
     [dbg/view state]]))
