# POST-Redirect-GET demo


Please
see [Wikipedia page](https://en.wikipedia.org/wiki/Post/Redirect/Get)
for an introduction to the idea.



The core ideas of the implementation are here:


Your form routes to an action:

```clojure
[:div
 (form/form-to
  [:post "/actions/save-issue"]
     (form/hidden-field :id id)

  ;; ... more form fields ...

 )]
```


Yo have to configure GET and POST routes in handler.clj namespace that
somehow dispatch to a renderer or an action method, resp.


```clojure
(defn- app-routes
  [config]
  (cp/routes
   (GET "/meta" []
        meta-inf)

   (GET "/views/:view-id" request
        (views/render {:config config}
                      (:params request)))

   (POST "/actions/:action-id" request
         (handle-action! {:config config}
                         (:params request)))

   (GET "/" []
        (response/redirect "/views/index" 303))

   (route/resources "/")
   (route/not-found {:status  404
                     :headers {"Content-Type" "text/html"}
                     :body    "Not found"})))
```

The `handle-action!` implementation is the key:

```clojure
(defn- handle-action!
  "Processes the action and sends a redirect response."
  [context action-params]
  (let [{:keys [view-id view-params]}
        (actions/process! context action-params)]
    (response/redirect (u/to-view view-id view-params)
                       303)))
```

Each action function returns data to describe where the browser
should be redirected to:

```clojure
(defmethod process! "save-issue"
  [context action-params]
  (let [{:keys [id journal-id year number]}
        action-params

        issue
        (db/update-issue {:id         (u/str->int id)
                          :journal-id journal-id
                          :year       year
                          :number     number})]
    {:view-id "issue-details"
     :view-params {:id (:id issue)}}))
```


That's it, basically ;-)
