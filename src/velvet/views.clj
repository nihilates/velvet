(ns velvet.views
  (:require [hiccup.core :refer (html)]
            [hiccup.form :as f]
            [velvet.db :as db]))

(defn layout [title & content]
  (html
    [:head [:title title]]
    [:body content]))

(defn item-summary [entry]
  (let [id (:id entry)
        item (:item entry)
        description (:description entry)
        dicesize (:dicesize entry)
        dicecount (:dicecount entry)
        created_at (:created_at entry)]
    [:div {:id "entry"}
      [:h4 item]
      [:p dicecount "d" dicesize]
      [:section description]
      [:br]]))

(defn item-summary-admin [entry]
  (let [id (:id entry)
        item (:item entry)
        description (:description entry)
        dicesize (:dicesize entry)
        dicecount (:dicecount entry)
        created_at (:created_at entry)]
    [:div {:id "entry"}
      [:h4 item]
      [:p dicecount "d" dicesize]
      [:section description]
      [:br]
      [:section.actions
        [:a {:href (str "/admin/" id "/edit")} "Edit"] " / "
        [:a {:href (str "/admin/" id "/delete")} "Delete"]]]))

 (defn main-page []
  (layout "Handy Haverapp"
    [:h1 "Handy Haverapp"]
    [:a {:href "admin"} "Login to edit/delete"]
    [:h3 "Add an Item"]
    (map #(item-summary %) (db/all))
    [:a {:href "/add"} "Add"]))

(defn admin-page []
  (layout "Handy Haverapp - Admin"
    [:h1 "Handy Haverapp - Admin"]
    [:a {:href "/"} "Log Out"] [:br]
    (map #(item-summary-admin %) (db/all))
    [:a {:href "admin/add"} "Add"]))

(defn add-item []
  (layout "Add Item"
    (list
      [:h2 "Add Item"]
      (f/form-to [:post "/create"]
        (f/label "item" "Item")
        (f/text-field "item") [:br]
        (f/label "description" "Description") [:br]
        (f/text-area {:rows 4} "description") [:br]
        (f/label "dicesize" "Dice Size") [:br]
        (f/text-field "dicesize") [:br]
        (f/label "dicecount" "Dice Count") [:br]
        (f/text-field "dicecount") [:br]
        (f/submit-button "Save")))))

(defn edit-item [id]
  (layout "Edit Item"
  (list
    (let [item (db/get id)]
    [:h2 (str "Edit Item " id)]
    (f/form-to [:post "save"]
      (f/label "item" "Item")
      (f/text-field "item") [:br]
      (f/label "description" "Description") [:br]
      (f/text-area {:rows 4} "description") [:br]
      (f/label "dicesize" "Dice Size") [:br]
      (f/text-field "dicesize") [:br]
      (f/label "dicecount" "Dice Count") [:br]
      (f/text-field "dicecount") [:br]
      (f/submit-button "Save"))))))