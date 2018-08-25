(ns user
  (:require
   [clojure.tools.namespace.repl :as tools]
   [schema.core :as s]

   [com.stuartsierra.component :as c]
   [figwheel-sidecar.repl-api :as ra]))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Component: Figwheel

(def ^:private figwheel-config
  {:figwheel-options
   {:server-port
    3649

    :css-dirs
    ["resources/public/css"]}

   :build-ids
   ["dev" "test"]

   :all-builds
   [{:id
     "dev"

     :source-paths
     ["src/cljs"]

     :figwheel
     {:on-jsload
      "froscon-18.hangman.frontend.core/on-jsload"}

     :compiler
     {:main
      "froscon-18.hangman.frontend.core"

      :asset-path
      "js/compiled/out"

      :output-to
      "resources/public/js/compiled/app.js"

      :output-dir
      "resources/public/js/compiled/out"

      :source-map-timestamp
      true}}]})

(def sass-config
  {:executable-path "sass"
   :input-dir "src/sass"
   :output-dir "resources/public/css"})

(defrecord Figwheel []

  c/Lifecycle
  (start [component]
    (println ";; [Figwheel] Started.")
    (ra/start-figwheel! component)
    component)

  (stop [component]
    (println ";; [Figwheel] Stopped.")
    component))

(defrecord SassWatcher [executable-path input-dir output-dir]
  c/Lifecycle
  (start [config]
    (if (not (:sass-watcher-process config))
      (do
        (println "Figwheel: Starting SASS watch process")
        (assoc config :sass-watcher-process
               (.exec (Runtime/getRuntime)
                      (str executable-path " --watch " input-dir ":" output-dir))))
      config))
  (stop [config]
    (when-let [process (:sass-watcher-process config)]
      (println "Figwheel: Stopping SASS watch process")
      (.destroy process))
    config))

(defn new-figwheel
  [config]
  (map->Figwheel config))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CLJS Helper

(def fig-system
  (atom
   (c/system-map
     :figwheel (map->Figwheel figwheel-config)
     :sass (map->SassWatcher sass-config))))

(defn fig-start []
  (swap! fig-system c/start))

(defn fig-stop []
  (swap! fig-system c/stop))

(defn reload []
  (fig-stop)
  (fig-start))

(defn fig-repl []
  (ra/cljs-repl))

(defn fig-init
  []
  (fig-start)
  (fig-repl))
