(ns mimas.markdown
  "Markdown processing with frontmatter support."
  (:require [markdown.core :as md]
            [clojure.string :as str]
            [malli.core :as m]))

(def frontmatter-delimiter "---")

(def page-schema
  "Malli schema for page frontmatter"
  [:map
   [:title :string]
   [:layout {:optional true} :string]
   [:date {:optional true} inst?]
   [:draft {:optional true} :boolean]])

(defn parse-frontmatter
  "Extract and parse YAML-style frontmatter from markdown content.
   Returns map with :frontmatter and :content keys."
  [text]
  (let [lines (str/split-lines text)]
    (if (= frontmatter-delimiter (first lines))
      (let [rest-lines (rest lines)
            frontmatter-end (or (first (keep-indexed #(when (= frontmatter-delimiter %2) %1) rest-lines))
                                (count rest-lines))
            frontmatter-lines (take frontmatter-end rest-lines)
            content-lines (drop (inc frontmatter-end) rest-lines)
            frontmatter (into {}
                              (keep (fn [line]
                                      (when-let [[_ k v] (re-matches #"(.+?):\s*(.+)" line)]
                                        [(keyword (str/trim k)) (str/trim v)]))
                                    frontmatter-lines))]
        {:frontmatter frontmatter
         :content (str/join "\n" content-lines)})
      {:frontmatter {}
       :content text})))

(defn markdown->html
  "Convert markdown string to HTML."
  [markdown-str]
  (md/md-to-html-string markdown-str))

(defn process-markdown-file
  "Read and process a markdown file, returning map with frontmatter and HTML content."
  [file-path]
  (let [text (slurp file-path)
        {:keys [frontmatter content]} (parse-frontmatter text)
        html (markdown->html content)]
    {:frontmatter frontmatter
     :content html
     :source-path file-path}))
