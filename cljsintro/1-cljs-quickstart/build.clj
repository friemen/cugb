(require '[clojure.java.io :as io])
(require '[cljs.build.api :as cljs])

(-> "target" io/file .mkdir)
(io/copy (-> "public/index.html" io/resource io/file)
         (io/file "target/index.html"))


(def compiler-settings
  {:output-dir "target"
   :output-to "target/main.js"
   :asset-path "."
   :main 'helloworld.app
   :optimizations :none})

(defn watch []
  (cljs/watch "src" compiler-settings))


(defn build []
  (cljs/build "src" (assoc compiler-settings
                           :optimizations :advanced)))


;; execute this file with
;; java -cp lib/cljs.jar:src:resources clojure.main -i build.clj -e "(watch)"
;; load in the browser target/index.html
