(ns mimas.router
  "File-based routing using Reitit."
  (:require [reitit.core :as r]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn file->route
  "Convert a file path to a route path.
   Example: content/blog/post.md -> /blog/post"
  [file-path content-dir]
  (let [relative-path (str/replace file-path (str content-dir "/") "")
        without-ext (str/replace relative-path #"\.(md|html)$" "")
        route-path (if (= without-ext "index")
                     "/"
                     (str "/" without-ext))]
    route-path))

(defn collect-content-files
  "Recursively collect all markdown files from content directory."
  [content-dir]
  (let [dir (io/file content-dir)]
    (when (.exists dir)
      (->> (file-seq dir)
           (filter #(.isFile %))
           (filter #(str/ends-with? (.getName %) ".md"))
           (map #(.getPath %))))))

(defn build-route-tree
  "Build a route tree from content files."
  [content-dir]
  (let [files (collect-content-files content-dir)]
    (into []
          (map (fn [file-path]
                 [(file->route file-path content-dir)
                  {:file file-path}]))
          files)))

(defn create-router
  "Create a Reitit router from route definitions."
  [routes]
  (r/router routes))

(comment
  ;; Example usage
  (file->route "content/index.md" "content")
  ;; => "/"

  (file->route "content/blog/my-post.md" "content")
  ;; => "/blog/my-post"

  (build-route-tree "content")
  )
