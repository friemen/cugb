(defproject rdbms "0.1.0-SNAPSHOT"
  :description "Accessing relational databases"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 ; webapp related
                 [http-kit "2.1.6"]
                 [ring "1.2.0"]
                 [compojure "1.1.5"]
                 ; database related
                 [com.h2database/h2 "1.4.178"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [java-jdbc/dsl "0.1.0"]
                 [com.mchange/c3p0 "0.9.2.1"]
                 [korma "0.3.1"]
                 ; logging support
                 [org.clojure/tools.logging "0.2.6"]
                 [log4j "1.2.16"
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jdmk/jmxtools
                               com.sun.jmx/jmxri]]])
