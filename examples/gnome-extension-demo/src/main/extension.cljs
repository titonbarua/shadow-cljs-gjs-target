(ns extension
  ;;  npx shadow-cljs release app => --- shadow/cljs/module_vars/main.js:1 \n Parse error. Semi-colon expected
  ;; (:require ["gjs.misc.extensionUtils" :as ExtensionUtils])
  (:require [goog.object :as g]))

;; Objective: 
;; 
;; https://gjs.guide/extensions/overview/anatomy.html
;; 
;; const ExtensionUtils = imports.misc.extensionUtils;
;; const Me = ExtensionUtils.getCurrentExtension();
;; 
;; function init() {
;;     log(`initializing ${Me.metadata.name}`);
;; }
;; 
;; function enable() {
;;     log(`enabling ${Me.metadata.name}`);
;; }
;; 
;; function disable() {
;;     log(`disabling ${Me.metadata.name}`);
;; }

(def ExtensionUtils js/imports.misc.extensionUtils)
(def Me (.getCurrentExtension ExtensionUtils))


(defn init []
  (js/log "clojurescript-demo initialized")
  (js/log (.. Me -metadata -name))
  nil)

(defn enable []
  (js/log "Enable clojurescript-demo extension")
  (js/log (.. Me -metadata -name))
  nil)

(defn disable []
  (js/log "Disable clojurescript-demo extension")
  (js/log (.. Me -metadata -name))
  nil)

(g/set js/global "init" init)
(g/set js/global "enable" enable)
(g/set js/global "disable" disable)
