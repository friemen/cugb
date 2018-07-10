(ns froscon-18.hangman.frontend.util
  "Misc utilities.")

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Keyboard Events

(defn kb-event-is-letter?
  "Predicate whether the event's character code represents
   either a lower-case or upper-case letter."
  [e]
  (let [char-code (.. e -charCode)]
    (or (<= 65 char-code 90)
        (<= 97 char-code 122))))


(defn kb-event->letter
  "Transform a keyboard-event into a lower-case or upper-case letter -
   return a blank string otherwise."
  [e]
  (if (kb-event-is-letter? e)
    (.. e -key)
    ""))
