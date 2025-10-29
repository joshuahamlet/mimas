(ns mimas.build
  "Static site build pipeline."
  (:require [clojure.java.io :as io]
            [mimas.core :as core]
            [mimas.router :as router]
            [mimas.markdown :as markdown]
            [mimas.templating :as templating]
            [hiccup.util :refer [raw-string]])
  (:import (java.nio.file Files)
           (java.nio.file StandardCopyOption)))

(defn ensure-dir
  "Ensure a directory exists."
  [dir-path]
  (let [dir (io/file dir-path)]
    (when-not (.exists dir)
      (.mkdirs dir))))

(defn copy-static-assets
  "Copy static assets from public directory to output."
  [public-dir output-dir]
  (let [public (io/file public-dir)
        output (io/file output-dir)]
    (when (.exists public)
      (doseq [file (file-seq public)
              :when (.isFile file)]
        (let [relative-path (str (.relativize (.toPath public) (.toPath file)))
              dest-file (io/file output relative-path)]
          (when-let [parent (.getParent dest-file)]
            (ensure-dir parent))
          (Files/copy (.toPath file)
                     (.toPath dest-file)
                     (into-array [StandardCopyOption/REPLACE_EXISTING])))))))

(defn build-page
  "Build a single page to the output directory."
  [file-path route-path output-dir]
  (let [{:keys [frontmatter content]} (markdown/process-markdown-file file-path)
        html (templating/render-html
               (templating/html5-page
                 {:title (:title frontmatter "Untitled")
                  :css ["/css/style.css"]}
                 [:div.content
                  (raw-string content)]))
        output-path (if (= route-path "/")
                      (io/file output-dir "index.html")
                      (io/file output-dir (str (subs route-path 1) "/index.html")))]
    (when-let [parent (.getParent output-path)]
      (ensure-dir parent))
    (spit output-path html)))

(defn build-site
  "Build the entire static site."
  [config]
  (let [{:keys [content-dir output-dir public-dir]} config
        routes (router/build-route-tree content-dir)]
    (println "Building site...")
    (ensure-dir output-dir)

    ;; Build all pages
    (doseq [[route-path {:keys [file]}] routes]
      (println "Building" route-path "from" file)
      (build-page file route-path output-dir))

    ;; Copy static assets
    (println "Copying static assets...")
    (copy-static-assets public-dir output-dir)

    (println "Build complete! Output in" output-dir)))

(defn -main
  [& args]
  (let [config (core/load-config)]
    (build-site config)))
