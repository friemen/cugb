(ns froscon-18.hangman.frontend.core
  "Main namespace and entry point."
  (:require
   [froscon-18.hangman.frontend.handler :as handler]
   [froscon-18.hangman.frontend.main :as main]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Figwheel

(defn on-jsload
  []
  (js/console.clear)
  (main/mount!))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Init

(def ^:private seed
  {:game/slots
   [nil "E" "A" nil nil nil "F" nil nil]

   :game/words
   []})


(defonce init!
  (do
    (handler/install-watcher!)
    (handler/handle-event! :initialize seed)
    (main/mount!)))
