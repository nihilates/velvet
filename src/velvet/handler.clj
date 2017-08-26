(ns velvet.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [velvet.views :as views]
            [velvet.db :as db]
            [ring.util.response :as resp]
            [ring.middleware.basic-authentication :refer :all]))

(defn authenticated? [user pass]
  (and (= user "admin")
       (= pass "primemover")))

(defroutes public-routes
  (GET "/" [] (views/main-page))
  (GET "/add" [] (views/add-item))
  (POST "/create" [& params]
    (do (db/create params)
        (resp/redirect "/")))
  (route/resources "/"))

(defroutes protected-routes
  (GET "/admin" [] (views/admin-page))
  (GET "/admin/add" [] (views/add-item))
  (GET "/admin/:id/edit" [id] (views/edit-item id))
  (POST "/admin/:id/save" [& params]
    (do (db/save (:id params) params)
        (resp/redirect "/admin")))
  (GET "/admin/:id/delete" [id]
    (do (db/delete id)
        (resp/redirect "/admin"))))

(defroutes app-routes
  public-routes
  (wrap-basic-authentication protected-routes authenticated?)
  (route/not-found "Invalid Route"))

(def app
  (handler/site app-routes))