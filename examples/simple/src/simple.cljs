(ns simple
  (:require ["gjs.system" :as system]
            [clojure.string :as str]))

(defn ^:export main [& args]
  (println "Hello, Gjs!")
  (println (str "Command line args are: " (str/join ", " args)))
  (system/exit 42))
