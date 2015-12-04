(ns libdemos.hiccup-demo
  (:require [hiccup.page :as page]
            [hiccup.form :as form]))


(def users [{:firstname "John" :lastname "Doe"}
            {:firstname "Alice" :lastname "Miller"}
            {:firstname "Bob" :lastname "TheBuilder"}])


(defn render-users
  [users]
  [:table
   [:tr [:th "First name"] [:th "Last name"]
    (for [{:keys [firstname lastname]} users]
      (if (= firstname "John")
        [:tr [:td firstname] [:td lastname]]))]])

(comment
  (page/html4 [:head]
              [:body
               [:div {:class "mui-container"}
                [:h1 "Hello Hiccup"]
                (render-users users)]])
,,,)

;; see also https://github.com/weavejester/hiccup/wiki/Syntax
