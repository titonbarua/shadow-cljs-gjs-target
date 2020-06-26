# shadow-cljs-gjs-target


## Usage

FIXME


__ NOTES:

- This is a modification of the existing node-script target.
- Both :dev and :release mode works.
- Implements a rudimentary `console` analog which proxies appropriate
  functions in gjs.
- Gjs builtin modules are importable with special string syntax.

__ SPECIAL SYNTAX FOR IMPORTING BUILTIN MODULES:

    * js:
        const Gtk = imports.gi.Gtk;
        const ByteArray = imports.ByteArray;
        const { Gtk, GLib } = imports.gi;

    * cljs:
        ["gjs.gi.Gtk" :as Gtk]
        ["gjs.byteArray" :as ByteArray]
        ["gjs.gi" :refer [Gtk GLib]]

__ SIMPLE DEMO:

```clojure
(ns my.app
    (:require [clojure.string :as str]
              ["gjs.gi.Gtk" :as Gtk]
              ["gjs.system" :as system]))

(defn ^:export main []
    (println "Hello from console!")
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

Build the above code with a build spec:

```
{:target :gjs
 :output-to "./my_app.js"
 :main my.app/main}
```

* TODO:

- Source maps don't work.
- Repl does not work.
- There needs to be a way to explicitly set gi versions before import, like:
      imports.gi.versions.Gtk = "3.0";
- Needs to add demo/tests.
- Take care of undefined property errors.

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
