(ns froscon-18.hangman.frontend.handler.pure
  "Pure event handlers"
  (:require
   [clojure.string :as str]))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helper functions

(defn string->letter
  "Try to turn `value` into a upper-case letter.
   Return `nil` otherwise.
   eg
   ```
    (string->letter \"c\")
    ;; => \"C\"

    (string->letter \"abc\")
    ;; => \"C\"

    (string->letter nil)
    ;; => \"\"
   ```"
  [value]
  (let [last-letter (last value)]
    (when-not (str/blank? last-letter)
      (str/upper-case last-letter))))


(defn slot-update
  "Update the vector of slots at index `idx` with a given value"
  [slots idx value]
  (assoc slots idx (string->letter value)))


(defn slots->pattern
  "Turn the vector of slots into a pattern.
   eg
   ```
    (slots->pattern [nil \"A\" nil \"B\"])
    ;; => \"?A?B\"
   ```"
  [slots]
  (str/join (map #(or % "?") slots)))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pure event handlers

(defmulti handle-event
  (fn [event & _]
    event))


(defmethod handle-event :initialize
  ;; "Initialize app state"
  [_ state seed]
  seed)


(defmethod handle-event :set-loading
  ;; "Set loading state"
  [_ state loading?]
  (assoc state :game/loading loading?))


(defmethod handle-event :words-update
  ;; "Update the list of words"
  [_ state words]
  (assoc state :game/words words))


(defmethod handle-event :slot-update
  ;; "Set the value of slot at `idx` to `value`"
  [_ state idx value]
  (-> state
      (assoc :game/words [])
      (update :game/slots slot-update idx value)))
