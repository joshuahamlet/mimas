(ns layouts.default
  "Default page layout."
  (:require [hiccup.page :as page]))

(defn render
  "Render the default layout."
  [{:keys [title content]}]
  (page/html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:title title]
     [:link {:rel "stylesheet" :href "/css/style.css"}]]
    [:body
     [:header.site-header
      [:nav
       [:a {:href "/"} "Home"]
       [:a {:href "/blog"} "Blog"]]]
     [:main.container
      [:div {:dangerouslySetInnerHTML {:__html content}}]]
     [:footer.site-footer
      [:p "Built with Mimas"]]]))
