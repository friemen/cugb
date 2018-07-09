(ns froscon-18.hangman.frontend.views.game.slots
  "Game subview for slots"
  (:require
   [froscon-18.hangman.frontend.handler :as handler]
   [froscon-18.hangman.frontend.util :as u]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Private

(defn- on-key-press
  [idx]
  (fn [e]
    (handler/handle-event! :slot-update idx (u/kb-event->letter e))))


(defn- slot-view
  [idx value]
  [:div.slot
   [:input {:type "text"
            :placeholder "?"
            :value (str value)
            :on-change (constantly false)
            :on-key-press (on-key-press idx)}]])


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public

(defn view
  [slots]
  [:div.slots
   (for [[idx value] (map-indexed vector slots)]
     ^{:key idx}
     [slot-view idx value])])
