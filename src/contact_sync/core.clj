(ns contact-sync.core
  (:require [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [contact-sync.db :refer [save-contacts get-contacts]]))

(defn- send-text-response [body]
  (-> (response/ok body)
      (response/header "Content-Type" "text/plain; charset=utf-8")))

(defn- send-json-response [body]
  (-> (response/ok body)
      (response/header "Content-Type" "application/json; charset=utf-8")))

(defroutes contact-routes
  (POST "/upload-contacts" {params :params}
        (println params)
        (send-text-response "Contacts updated"))
  (GET "/contacts" []
       (send-json-response (get-contacts))))
