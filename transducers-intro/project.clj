(defproject transducers-intro "0.1.0-SNAPSHOT"
  :description "transducers-intro"
  :url "https://github.com/hemmvm/clj-transducers-intro"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [cheshire "5.5.0"]
                 [io.reactivex/rxjava "1.0.10"]
                 [io.reactivex/rxclojure "1.0.0"]]

  :profiles {:dev
             {:dependencies [[org.clojure/tools.namespace "0.2.10"]]}})
