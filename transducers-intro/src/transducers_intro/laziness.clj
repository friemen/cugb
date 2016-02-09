(ns transducers-intro.laziness)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helper

(defn dbg
  [x]
  (println x) x)



(comment
  ;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Pull
  (->> (range)
       (mapcat (partial repeat 100))
       (map dbg)
       (first))


  ;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Push
  (let [xform (comp (mapcat (partial repeat 100))
                    (map dbg))]

    (first (sequence xform (range))))



  ;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;; Push (cont'd)
  (let [xform (comp (mapcat (partial repeat 100))
                    (partition-by identity)
                    (map dbg))]

    (first (sequence xform (range))))
  ,,,)
