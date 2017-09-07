(defproject de.doctronic/prgdemo-magman "1.0.0-SNAPSHOT"

  :description
  "Demo of Post Redirect Get pattern"

  :url
  "https://github.com/friemen/cugb/webprgdemo"

  :dependencies
  [[org.clojure/clojure "1.8.0"]

   ;; commons
   [com.stuartsierra/component "0.3.2"]
   [prismatic/schema "1.1.5"]
   [prismatic/plumbing  "0.5.4"]
   [com.taoensso/timbre "4.10.0"]

   ;; web related
   [ring "1.6.2"]
   [ring/ring-defaults "0.3.0"]
   [compojure "1.6.0"]
   [hiccup "1.0.5"]
   [http-kit "2.2.0"]

   ,,,]


  :plugins
  [[lein-ring "0.9.3"]
   [vita-io/uberjar-deploy "1.0.2"]]

  :main de.doctronic.prgdemo.magman.main
  :uberjar-name "prgdemo-magman.jar"

  :repl-options
  {:init-ns user}

  :profiles
  {:dev     {:dependencies []
             :source-paths ["dev"]}
   :uberjar {:aot :all}})
