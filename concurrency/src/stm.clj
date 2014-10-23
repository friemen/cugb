(ns stm)

(defn transfer
  [account1 account2 amount]
  (dosync (alter account1 - amount)
          (alter account2 + amount))
  amount)

(def a1 (ref 100))
(def a2 (ref 100))

(defn sum?
  [s]
  (dosync (= (+ @a1 @a2) s)))

(defn broken-sum?
  [s]
  (= (+ @a1 @a2) s))

(defn rand-transfer
  []
  (transfer a1 a2 (- 50 (rand-int 101))))


#_(do (def f1 (future (dotimes [_ 1e7] (rand-transfer))))
      (def f2 (future (dotimes [_ 1e7] (rand-transfer)))))

