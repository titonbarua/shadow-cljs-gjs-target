(ns gtk-offscreen
  (:require ["gjs.gi.Gtk" :as Gtk]))

(defn ^:export main [& [width height]]
  (let [msg (str "Hello, Gtk + Clojurescript!")]
    (Gtk/init nil)
    (let [win   (Gtk/OffscreenWindow.)
          label (Gtk/Label. #js {:label msg})]
      (doto win
        (.set_default_size (int width) (int height))
        (.add label)
        (.show_all))

      ;; Draw window.
      (doseq [_ (range 60)]
        (Gtk/main_iteration_do false))

      ;; Save window as image.
      (println "Saving window.png ...")
      (let [pixbuf (.get_pixbuf win)]
        (.savev pixbuf "window.png" "png" #js [] #js []))

      ;; Close window.
      (.close win))))
