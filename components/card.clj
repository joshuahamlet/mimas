(ns components.card
  "Card component with slot support."
  (:require [malli.core :as m]))

(def props-schema
  "Malli schema for card props"
  [:map
   [:title :string]
   [:description {:optional true} :string]])

(defn render
  "Render a card component.
   Props: {:title string :description string (optional)}
   Slots: {:default hiccup-content :footer hiccup-content (optional)}"
  [props slots]
  (let [valid? (m/validate props-schema props)]
    (when-not valid?
      (throw (ex-info "Invalid card props" {:props props}))))

  [:div.card
   [:div.card-header
    [:h3.card-title (:title props)]
    (when-let [desc (:description props)]
      [:p.card-description desc])]
   [:div.card-body
    (:default slots)]
   (when-let [footer (:footer slots)]
     [:div.card-footer footer])])
