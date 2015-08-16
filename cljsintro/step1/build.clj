(require '[clojure.java.io :as io])
(require '[cljs.build.api :as cljs])

(-> "target" io/file .mkdir)

(cljs/build "src" {:output-dir "target"
                   :output-to "target/main.js"
                   :asset-path "."
                   :main 'helloworld})

(io/copy (-> "public/index.html" io/resource io/file)
         (io/file "target/index.html"))


;; execute this file with
;; java -cp lib/cljs.jar:src:resources clojure.main build.clj
