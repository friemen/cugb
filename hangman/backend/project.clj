(defproject backend "0.1.0-SNAPSHOT"
  :description
  "hangman backend"

  :dependencies
  [[org.clojure/clojure "1.8.0"]
   [com.cognitect/transit-clj "0.8.281"]
   [compojure "1.5.2"]
   [ring "1.4.0"]
   [ring/ring-defaults "0.1.5"]
   [ring-transit-middleware "0.1.2"]

   [http-kit "2.2.0"]
   [com.cemerick/url "0.1.1" :exclusions [org.clojure/clojure]]
   [hickory "0.7.1"]]

  :profiles
  {:uberjar
   {:aot :all}

   :dev
   {:dependencies
    [[org.clojure/tools.namespace "0.2.11"]
     [stubadub "2.0.0"]]}}
  ,,,)
