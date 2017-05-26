(ns contact-sync.core
  (:require [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [contact-sync.db :as db]))

(defn- send-text-response [body]
  (-> (response/ok body)
      (response/header "Content-Type" "text/plain; charset=utf-8")))

(defn- send-json-response [body]
  (-> (response/ok body)
      (response/header "Content-Type" "application/json; charset=utf-8")))

(defroutes contact-routes
  (POST "/upload-contacts" {{email :email
                             contacts :contactList} :params}
        (db/save-contacts {:email email :contacts contacts})
        (send-json-response {:msg "Contacts Updated"}))
  (GET "/contacts" []
       (send-json-response (db/get-contacts))))
