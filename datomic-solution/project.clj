(defproject datomic-intro "0.1.0-SNAPSHOT"
  :description "Solution project for Datomic based webapp"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main webapp
  :dependencies [[org.clojure/clojure "1.5.1"]
                 ; webapp related
                 [http-kit "2.1.6"]
                 [ring "1.2.0"]
                 [compojure "1.1.5"]
                 ; database related
                 [com.datomic/datomic-free "0.8.4260"]
                 ; logging support
                 [org.clojure/tools.logging "0.2.6"]
                 [log4j "1.2.16"
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jdmk/jmxtools
                               com.sun.jmx/jmxri]]])
