(set-env!
 :source-paths    #{"src/cljs"}
 :resource-paths  #{"resources"}
 :asset-paths     #{"target"}
 :dependencies '[[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.107"]
                 [org.clojure/tools.nrepl "0.2.10"]

                 [hiccups "0.3.0"]

                 ;; boot related
                 [adzerk/boot-cljs "0.0-3308-0" :scope "test"]
                 [adzerk/boot-cljs-repl "0.1.10-SNAPSHOT" :scope "test"]
                 [adzerk/boot-reload "0.3.1" :scope "test"]
                 [pandeiro/boot-http "0.6.3-SNAPSHOT" :scope "test"]
                 [infracanophile/boot-cljs-test "0.3.2" :scope "test"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[infracanophile.boot-cljs-test :refer [cljs-test-runner run-cljs-test]])

(deftask production []
  (task-options! cljs {:optimizations :advanced})
  identity)


(deftask development []
  (task-options! cljs {:optimizations :none
                       :unified-mode true
                       :source-map true}
                 reload {:on-jsload 'helloworld.app/init})
  identity)


(deftask cljs-test []
  (set-env! :source-paths #(conj % "test/cljs"))
  (comp (development)
        (cljs-test-runner)
        (cljs)
        (run-cljs-test :cmd "phantomjs")))


(deftask build []
  (comp (speak)
        (cljs)))


(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (build)))


(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))
