(ns froscon-18.hangman.frontend.handler
  "Event handlers"
  (:require
   [plumbing.core :refer-macros [fnk]]

   [froscon-18.hangman.frontend.state :as state]
   [froscon-18.hangman.frontend.util.transit :as transit]
   [froscon-18.hangman.frontend.handler.pure :as pure]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Event handler

(defn handle-event!
  "Handle events by applying pure handlers to the state atom."
  [event & args]
  (swap! state/!state
         (fn [state]
           (apply pure/handle-event event state args))))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Watcher

(defn- words-fetch!
  "Fetch words for `:slots`."
  [{:keys [slots]}]
  (let [endpoint
        "http://localhost:1337/api/words"

        params
        {:pattern (pure/slots->pattern slots)
         :mock? true}

        callback
        (fnk [[:body {data []}]]
          (handle-event! :words-update data)
          (handle-event! :set-loading false))]

    (handle-event! :set-loading true)
    (transit/post endpoint params callback)))


(defn install-watcher!
  "Install watcher to remotely fetch words upon
   slot change"
  []
  (add-watch state/!state
   :words (fn [_ _ state-prev state]
            (when-not (= (:game/slots state-prev)
                         (:game/slots state))

              (words-fetch! {:slots (:game/slots state)})))))
