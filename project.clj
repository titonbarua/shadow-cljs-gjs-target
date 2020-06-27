(defproject titonbarua/shadow-cljs-gjs-target "0.1.0-SNAPSHOT"
  :description "A Gjs(Gnome Javascript Bindings) target for shadow-cljs compiler."
  :url "https://github.com/titonbarua/shadow-cljs-gjs-target"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies []
  :test-selectors {:headless :headless}
  :profiles {:provided {:dependencies [[org.clojure/clojure "1.10.1"]
                                       [thheller/shadow-cljs "2.10.13"]]}}
  :repl-options {:init-ns shadow-cljs-gjs-target.core})
