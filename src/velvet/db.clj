(ns velvet.db
  (:refer-clojure :exclude [get])
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]))

;Connect to Database
(def mysql-db {:subprotocol "mysql"
               :subname "//localhost:3306/velvet"
               :user "root"
               :password ""
               :zeroDateTimeBehavior "convertToNull"})

;Define what "now" means in terms of timestamps
(def now
  (str (java.sql.Timestamp. (System/currentTimeMillis))))

;Query for all entries
(defn all []
  (j/query mysql-db
    (s/select * :inventory)))

;Query to get a specific entry from the database
(defn get [id]
  (first (j/query mysql-db
    (s/select * :inventory (s/where {:id id})))))

;Query to add new entry
(defn create [params]
  (j/insert! mysql-db :inventory (merge params {:created_at now :updated_at now})))

;Query to save and updated entry
(defn save [id params]
  (j/update! mysql-db :inventory params (s/where {:id id})))

;Query to delete an entry
(defn delete [id]
  (j/delete! mysql-db :inventory (s/where {:id id})))