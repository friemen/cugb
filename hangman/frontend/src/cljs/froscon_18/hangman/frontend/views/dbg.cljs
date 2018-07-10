(ns froscon-18.hangman.frontend.views.dbg
  "Display app data for debugging.")


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn view
  [state]
  [:div.dbg
   (for [k
         [:game/slots
          :game/words]]

     ^{:key (name k)}
     [:div.dbg__entry (pr-str (select-keys state [k]))])])
