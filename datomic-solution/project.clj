(defproject datomic-intro "0.1.0-SNAPSHOT"
  :description "Solution project for Datomic based webapp"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main webapp
  :dependencies [[org.clojure/clojure "1.6.0"]
                 ; webapp related
                 [http-kit "2.1.19"]
                 [ring "1.3.2"]
                 [compojure "1.3.4"]
                 ; database related
                 [com.datomic/datomic-free "0.9.5173"]
                 ; logging support
                 [org.clojure/tools.logging "0.3.1"]
                 [log4j "1.2.17"
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jdmk/jmxtools
                               com.sun.jmx/jmxri]]])
