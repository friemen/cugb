(defproject rdbms "0.1.0-SNAPSHOT"
  :description "Demo of relational database access"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.stuartsierra/component "0.3.1"]

                 ; webapp related
                 [http-kit "2.1.19"]
                 [ring "1.4.0"]
                 [compojure "1.4.0"]

                 ; database related
                 [com.h2database/h2 "1.4.190"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [java-jdbc/dsl "0.1.3"]
                 [com.mchange/c3p0 "0.9.5"]
                 [yesql "0.5.1"]
                 [honeysql "0.6.2"]
                 [aggregate "1.0.1"]]
  :profiles {:dev {:source-paths ["src" "dev"]}})
