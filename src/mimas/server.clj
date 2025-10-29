(ns mimas.server
  "Development server with Ring and Jetty."
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [mimas.core :as core]
            [mimas.router :as router]
            [mimas.markdown :as markdown]
            [mimas.templating :as templating]
            [reitit.ring :as ring]
            [hiccup.util :refer [raw-string]]))

(defn render-page
  "Render a page from a markdown file."
  [file-path]
  (let [{:keys [frontmatter content]} (markdown/process-markdown-file file-path)]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (templating/render-html
             (templating/html5-page
               {:title (:title frontmatter "Untitled")
                :css ["/css/style.css"]}
               [:div.content
                (raw-string content)]))}))

(defn handler
  "Main request handler."
  [config]
  (let [routes (router/build-route-tree (:content-dir config))
        router (ring/router
                 (into []
                       (map (fn [[path {:keys [file]}]]
                              [path {:get (fn [_] (render-page file))}]))
                       routes))
        app (ring/ring-handler router
                                (ring/create-default-handler))
        public-dir (:public-dir config "public")]
    (-> app
        (wrap-file public-dir)
        wrap-content-type
        wrap-not-modified)))

(defn start-server
  "Start the development server."
  [config]
  (let [port (get-in config [:dev-server :port] 3000)
        host (get-in config [:dev-server :host] "localhost")]
    (println (str "Starting development server on http://" host ":" port))
    (jetty/run-jetty (handler config)
                     {:port port
                      :host host
                      :join? true})))

(defn -main
  [& args]
  (let [config (core/load-config)]
    (start-server config)))
