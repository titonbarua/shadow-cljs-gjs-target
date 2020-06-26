# shadow-cljs-gjs-target

This project implements a build target for
[shadow-cljs](https://github.com/thheller/shadow-cljs) so that
[Clojurescript](https://clojurescript.org/) can be compiled to Javascript in a
form suitable for Gnome Javascript Bindings -
[Gjs](https://gitlab.gnome.org/GNOME/gjs/). 


## Notes
- This is a modification of the `:node-script` target from original shadow-cljs
  source code.
- Only development mode compilation works.
- Gjs does not have a `console` object. As such, a rudimentary `console` analog
  is implemented which proxies appropriate functions in Gjs. This makes Clojurescript
  `println` and related functions working as is.
- Gjs builtin modules(i.e. `gi.Gtk`, `system`) are importable as Clojurescript
  namespaces using a special syntax.
  
## Special syntax for importing builtin modules

Gjs builtin modules can be accessed using a string based namespace syntax -
`"gjs.<builtin-module-name>"`. For example, if you want to import the `gi.Gtk`
module, the mapped namespace name should be `"gjs.gi.Gtk"`.

### Import syntax mapping

| Javascript | Clojurescript |
| ---------- | ------------- |
| `const Gtk = imports.gi.Gtk;` | `(require '["gjs.gi.Gtk" :as Gtk])` |
| `const ByteArray = imports.ByteArray;` | `(require '["gjs.byteArray" :as ByteArray])` |
| `const { Gtk, GLib } = imports.gi;` | `(require '["gjs.gi" :refer [Gtk GLib]])` |

        
## Tutorial

Here's a short tutorial on how to get a simple Gtk app and running with
Clojurescript. Note that I am detailing the lowest friction way of installing
and using shadow-cljs. Feel free to use `lein`, `boot` or `deps.edn`, if you
want so. Look at shadow-cljs documentation to know how to do that.

- Install `npm` with your distribution's package manager. Also, don't forget to
  install `gjs` and gobject introspection bindings, which is packaged under name
  `gir1.2-javascriptcoregtk-4.0` in Ubuntu 20.04.
- Create a demo shadow cljs project with `npx create-cljs-project gjs-demo`.
  This should create a project directory named `gjs-demo`. Change current directory
  to it.
- Create a file named `gjs_demo.cljs` under directory `src/main/` with the following
  content -

```clojure
(ns gjs-demo
    (:require [clojure.string :as str]
              ["gjs.gi.Gtk" :as Gtk]
              ["gjs.system" :as system]))

(defn ^:export main [& args]
  (println "Hello from console!")
  (println "Command line arguments are: " (str/join ", " args))
  (let [msg (str "Hello, Gtk + Clojurescript!\ngjs version = "
                 (.-version system))]
        (Gtk/init nil)
        (let [win   (Gtk/Window.)
              label (Gtk/Label. #js {:label msg})]
            (doto win
                (.add label)
                (.show_all))
            (Gtk/main))))
```

- Edit the `shadow-cljs.edn` file. Change it's content to -

```clojure
;; shadow-cljs configuration
{:source-paths
 ["src/dev"
  "src/main"
  "src/test"]

 :dependencies
 [[titonbarua/shadow-cljs-gjs-target "0.1.0"]]

 :builds
 {:app {:target shadow-cljs-gjs-target.core
        :output-to "./app.js"
        :main gjs-demo/main}}}
```

- Run `npx shadow-cljs compile app`. This should emit a development javascript file
  named `app.js` at project root. Run it with `gjs app.js`. If everything is ok, you
  should see a tiny Gtk window with some text.
  
- To produce a release build, run `npx shadow-cljs release`.

## Related resources

- [Gjs guides](http://gjs.guide/guides/)
- [Gjs Javascript examples](https://github.com/GNOME/gjs/tree/mainline/examples)
- [Gjs project documentation](https://github.com/GNOME/gjs/tree/mainline/doc)
- [Shadow-cljs User Manual](https://shadow-cljs.github.io/docs/UsersGuide.html)

## Things to TODO:

- Fix release mode compilation.
- Source maps don't work.
- REPL does not work.
- There needs to be a way to explicitly set gi versions before import, like:
  `imports.gi.versions.Gtk = "3.0";`
- Needs to add tests.
- Take care of undefined property errors.

## License

Copyright © 2020 Titon Barua

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
