(ns component-demo.file-logger
  (:require [com.stuartsierra.component :as c]
            [component-demo.logger :as logger]))


(defrecord FileLogger [filename]
  c/Lifecycle
  (start [component]
    (println ";; [FileLogger] logs to" filename)
    (spit filename "")
    component)

  (stop [component]
    (println ";; [FileLogger] stopped")
    component)

  logger/ILogger

  (log [_ message]
    (let [line (str message "\n")]
      (spit filename line :append true))))


(defn new-logger
  [filename]
  (map->FileLogger {:filename filename}))
