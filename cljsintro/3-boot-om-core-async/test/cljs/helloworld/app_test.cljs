(ns helloworld.app-test
  (:require [helloworld.app :as app]
            [cljs.test :refer-macros [deftest is testing run-tests]]))


;; Enable test when delete-todo is implemented in app ns
#_(deftest new-todo-test
  (let [state {:todos [{:id 1 :desc "Foo"}
                       {:id 2 :desc "Bar"}]}]
    (is (= {:todos [{:id 1 :desc "Foo"}]}
           (app/delete-todo state 2)))))
