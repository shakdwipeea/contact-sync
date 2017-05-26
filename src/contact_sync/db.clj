(ns contact-sync.db
  (:require [korma.core :refer [defentity table has-many
                                insert values exec-raw
                                select with]]
            [korma.db :refer [defdb mysql]]
            [clojure.string :as str]))

(defdb db (mysql {:db "contact"
                  :user "root"
                  :password ""}))

(defentity phone-number
  (table :phone_number))

(defentity contact
  (has-many phone-number))

(defentity user
  (has-many contact))


;; (insert user (values [{:email "akash"}
;;                       {:email "antash"}]))

;; (select user (with phone-number))

(defn- schema-stmts []
  (-> "create-schema.sql"
      clojure.java.io/resource
      slurp
      (str/split #"--;;")))


(defn create-schema []
  (println "Creating schema")
  (doseq [stmt (schema-stmts)]
    (exec-raw stmt)))


(defn- save-phone-numbers [contact-id phones]
  (insert phone-number
            (values (mapv
                     (fn [phone]
                       {:number phone
                        :contact_id contact-id})
                     phones))))

(defn- save-contact-list [user-id contacts]
  (doseq [c contacts]
    (-> (insert contact
                (values {:name (:name c)
                         :user_id user-id}))
        :generated_key
        (save-phone-numbers (:phones c)))))

(defn save-contacts [{email :email contacts :contacts}]
  (-> (insert user (values {:email email}))
      :generated_key
      (save-contact-list contacts)))

(defn get-contacts []
  (select user (with contact
                     (with phone-number))))

;; (save-contacts {:email "akash"
;;                :contacts [{:name "Akash"
;;                             :phones ["7676171398" "9308905293"]
;;                             :email "ashakdwipeea@gmail.com"}]})
