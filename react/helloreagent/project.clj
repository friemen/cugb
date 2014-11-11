(defproject helloreagent "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [reagent "0.4.3"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :cljsbuild { 
    :builds [{:id "helloreagent"
              :source-paths ["src"]
              :compiler {
                :output-to "helloreagent.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
