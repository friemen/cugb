(ns transducers-intro.prerequisites.fn-comp)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Example 1

(vector (str 42))

((comp vector str) 42)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Example 2

(sort-by (comp :age :details)
         [{:name "Peter"  :details {:age 39}}
          {:name "John"   :details {:age 42}}
          {:name "Daniel" :details {:age 41}}])
