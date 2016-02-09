(ns transducers-intro.prerequisites.ref)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Refs

(def !a
  (atom {:foo 42
         :bar {:baz 99}}))

(def !v
  (volatile! {:foo 42
              :bar {:baz 99}}))


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Deref

@!a
(deref !a)

@!v
(deref !v)


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Transact

(swap! !a update-in [:bar :baz] inc)
@!a

(vswap! !v update-in [:bar :baz] inc)
@!v


;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Set

(reset! !a "hello")
@!a

(vreset! !v "world")
@!v
