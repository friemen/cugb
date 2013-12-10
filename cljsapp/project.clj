(defproject cljsapp "0.1.0-SNAPSHOT"
  :description "CloureScript app"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2080"]
                 [prismatic/dommy "0.1.2"]]
  :plugins [[lein-cljsbuild "1.0.1-SNAPSHOT"]]
  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :compiler
                        {:output-to "resources/public/js/app.js"
                         ;; Can be :whitespace, :simple, :advanced
                         :optimizations :whitespace
                         :pretty-print true}}]})
