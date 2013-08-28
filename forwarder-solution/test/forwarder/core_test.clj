(ns forwarder.core-test
  (:use clojure.test
        forwarder.core))

(deftest forward-test
  (let [dest (atom [])
        n (atom 0)
        fwd (create-forwarder (fn [msgs]
                                (swap! dest #(concat % msgs))
                                (swap! n inc))
                              1)]
    (testing "Immediate forwarding"
      (forward fwd "Foo")
      (Thread/sleep 500)
      (is (= ["Foo"] @dest))
      (is (= 1 @n)))
    (testing "Forwarding after 1 second"
      (doseq [msg (range 4)]
        (forward fwd msg))
      (is (= [0 1 2 3] (waiting fwd)))
      (Thread/sleep 1000)
      (is (= [] (waiting fwd)))
      (is (= ["Foo" 0 1 2 3] @dest))
      (is (= 2 @n)))
    (testing "Truly parallel enqueuing/dequeuing"
      (reset! dest [])
      (reset! n 0)
      (doseq [msg (range 1000)]
        (forward fwd msg)
        (Thread/sleep 10))
      (Thread/sleep 1000)
      (is (= (vec (range 1000)) @dest))
      (is (= 11 @n))
      (Thread/sleep 1000)
      (is (= [] (waiting fwd)))
      (is (stopped? (:timer fwd))))
    (stop-forwarder fwd)))


