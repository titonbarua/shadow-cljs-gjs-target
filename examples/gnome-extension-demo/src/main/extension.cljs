(ns extension)

;; class Extension {constructor () {}
;;                  enable () {}
;;                  disable () {}}
;;                  
;; function init () {
;;    return new Extension ();
;; }

(defn enable []
  (println "Enable extension"))

(defn disable []
  (println "Disable extension"))

(defn ^:export init 
  []
  {:enable enable
   :disable disable})
