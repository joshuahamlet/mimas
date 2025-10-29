(ns mimas.core
  "Main entry point for the Mimas static site generator."
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.edn :as edn]
            [clojure.java.io :as io]))

(def cli-options
  [["-h" "--help" "Show help"]])

(defn load-config
  "Load site configuration from site.edn"
  []
  (if-let [config-file (io/file "site.edn")]
    (when (.exists config-file)
      (edn/read-string (slurp config-file)))
    {}))

(defn -main
  [& args]
  (let [{:keys [options arguments summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      (println summary)

      :else
      (println "Mimas static site generator"
               "\nUse clj -M:dev to start dev server"
               "\nUse clj -M:build to build site"))))
