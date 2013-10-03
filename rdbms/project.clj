(defproject rdbms "0.1.0-SNAPSHOT"
  :description "Accessing relational databases"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.h2database/h2 "1.3.173"]
                 [org.clojure/java.jdbc "0.3.0-alpha5"]
                 [com.mchange/c3p0 "0.9.2.1"]
                 [korma "0.3.0-RC5"]
                 [org.clojure/tools.logging "0.2.6"]
                 [log4j "1.2.16"
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jdmk/jmxtools
                               com.sun.jmx/jmxri]]])
