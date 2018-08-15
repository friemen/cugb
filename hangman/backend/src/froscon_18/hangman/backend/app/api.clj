(ns froscon-18.hangman.backend.app.api
  (:require
   [compojure.core :refer [ANY] :as cp]

   [froscon-18.hangman.backend.app.util.transit :as transit]
   [froscon-18.hangman.backend.wordaxis :as wordaxis]
   [froscon-18.hangman.backend.wordaxis.mock :as wordaxis-mock]))


(def routes
  (cp/routes
    (ANY "/words" {:keys [params]}
      (let [{:keys [pattern mock?]}
            params

            data
            (if mock?
              (wordaxis-mock/query! pattern)
              (wordaxis/query! pattern))]

        (transit/response {:data data})))))
