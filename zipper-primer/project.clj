(defproject zipper-primer "0.1.0-SNAPSHOT"
  :description "CUGB: Zipper Primer"
  :url "https://groups.google.com/forum/#!forum/clojure-user-group-bonn"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :main ^:skip-aot zipper-primer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
