(ns mimas.templating
  "Hiccup-based templating with slot support."
  (:require [hiccup.page :as page]
            [hiccup.core :as hiccup]
            [hiccup.util :refer [raw-string]]))

(defn render-html
  "Render hiccup data structure to HTML string."
  [hiccup-data]
  (hiccup/html hiccup-data))

(defn html5-page
  "Create a basic HTML5 page structure."
  [metadata & body]
  (page/html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:title (:title metadata "Untitled")]
     (when-let [css (:css metadata)]
       (for [href css]
         [:link {:rel "stylesheet" :href href}]))]
    [:body body]))

(defn default-layout
  "Default page layout wrapper."
  [{:keys [title content]}]
  (html5-page
    {:title title}
    [:div.container
     [:main (raw-string content)]]))

(comment
  ;; Example usage
  (render-html
    (html5-page
      {:title "Test Page"}
      [:h1 "Hello World"]
      [:p "This is a test."]))
  )
