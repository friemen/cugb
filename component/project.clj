(defproject component-demo "0.1.0-SNAPSHOT"
  :description "Component Sample"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.stuartsierra/component "0.2.3"]

                 ;; Http
                 [http-kit "2.1.19"]
                 [bidi "1.20.3"]

                 ;; Backend DB
                 [org.clojure/java.jdbc "0.4.1"]
                 [java-jdbc/dsl "0.1.3"] ; for schema generation
                 [com.zaxxer/HikariCP "2.3.9"]
                 [com.h2database/h2 "1.4.178"]]

  :main component-demo.core

  :source-paths ["src"]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]]}}

  ,,,)
