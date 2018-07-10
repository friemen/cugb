(defproject hangman "0.1.0-SNAPSHOT"
  :description
  "Hangman frontend"

  :source-paths
  ["src/cljs"]

  :dependencies
  [[org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.9.14"]
   [com.cognitect/transit-cljs "0.8.256"]

   ;; React
   [reagent "0.7.0"]
   [prismatic/plumbing "0.5.4"]]

  :plugins
  [[lein-cljsbuild "1.1.7"]
   [lein-figwheel "0.5.14"]]

  :clean-targets ^{:protect false}
  ["resources/public/js/compiled" "target"]

  :cljsbuild
  {:builds
   [{:id
     "production"

     :source-paths
     ["src/cljs"]

     :compiler
     {:output-to
      "resources/public/js/compiled/app.js"

      :main
      "froscon-18.hangman.frontend.core"

      :optimizations
      :advanced}}]}

  :profiles
  {:uberjar
   {:aot :all
    :hooks [leiningen.cljsbuild]}

   :repl-options
   {:nrepl-middleware
    [cemerick.piggieback/wrap-cljs-repl]}

   :dev
   {:dependencies
    [[org.clojure/tools.namespace "0.2.11"]
     [com.stuartsierra/component "0.3.2"]
     [figwheel-sidecar "0.5.0-1"]
     [com.cemerick/piggieback "0.2.1"]]

    :source-paths
    ["dev"]}})
