(ns gtk-grid
  (:require ["gjs.gi.Gtk" :as Gtk]
            ["gjs.system" :as system]))

(defn ^:export main []
  (Gtk/init nil)
  (let [win (Gtk/Window. #js {:title "Gtk Grid Example"
                              :border_width 10})

        ;; We are going to use a grid as layout.
        grid (Gtk/Grid. #js {:row_spacing    20
                             :column_spacing 20})

        ;; Widgets to show a greeting.
        greeting       (str "Hello, Gtk + Clojurescript!\n Gjs version is: "
                            (.-version system))
        label-greeting (Gtk/Label. #js {:label greeting})

        ;; Here, we are going to use clojure's atom watcher to dynamically
        ;; update a related label.
        label-click-count (Gtk/Label.)
        click-count       (-> (atom nil)
                              (add-watch :label-updater
                                         (fn [_ _ _ new-count]
                                           (.set_label label-click-count
                                                       (str "You have clicked " new-count " times!")))))
        button            (doto (Gtk/Button. #js {:label "Click me!"})
                            (.connect "clicked"
                                      (fn []
                                        (swap! click-count inc))))]
    ;; Set initial value of click count.
    (reset! click-count 0)

    ;; Attach widgets to grid.
    (doto grid
      (.attach label-greeting 0 0 5 2)
      (.attach label-click-count 0 3 3 1)
      (.attach button 3 3 2 1))

    ;; Setup window.
    (doto win
      (.add grid)
      (.connect "delete-event"
                (fn []
                  (println "Quiting ...")
                  (Gtk/main_quit)))
      (.show_all))

    ;; Start main loop.
    (Gtk/main)))
