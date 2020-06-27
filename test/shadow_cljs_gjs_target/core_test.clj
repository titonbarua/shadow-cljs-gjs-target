(ns shadow-cljs-gjs-target.core-test
  (:require [clojure.test :refer :all]
            [shadow-cljs-gjs-target.core :refer :all]
            [clojure.java.shell :refer [sh with-sh-dir]]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(deftest system-check-test
  (testing "Checking test environment ..."
    (is (= (System/getProperty "os.name") "Linux")
        "Need Linux operating system.")

    (is (zero? (:exit (sh "which" "gjs")))
        "Need to have `gjs` command installed.")))

(deftest simple-demo-test
  (testing "Simple demo dev-build compilation ..."
    (with-sh-dir "examples/simple"
      (let [cleanup (fn [] (sh "rm" "-r"
                               "./simple.js"
                               "./.shadow-cljs/"
                               "./jslibs/"
                               "./.cpcache/"))]
        (cleanup)
        (let [{:keys [exit out err]}
              (sh "clojure" "-m" "shadow.cljs.devtools.cli" "compile" "app")]
          (println out)
          (println err)
          (is (= exit 0)
              "Compiler should report success.")

          (testing "Execution ..."
            (let [{:keys [exit out err]}
                  (sh "gjs" "./simple.js" "foo" "bar")

                  output-lines
                  (str/split-lines out)]
              (println out)
              (println err)
              (is (= exit 42)
                  "Program exit code should be 42.")

              (is (= output-lines
                     ["Hello, Gjs!"
                      "Command line args are: foo, bar"])
                  "Program output should match."))))
        (cleanup))))

  #_(testing "Simple demo release-build compilation ..."
    (with-sh-dir "examples/simple"
      (let [cleanup (fn [] (sh "rm" "-r" "./simple.js" "./.shadow-cljs/" "./.cpcache/"))]
        (cleanup)
        (let [{:keys [exit]}
              (sh "clojure" "-m" "shadow.cljs.devtools.cli" "release" "app")]

          (is (= exit 0)
              "Compiler should report success.")

          (testing "Execution ..."
            (let [{:keys [exit out err]}
                  (sh "gjs" "./simple.js" "foo" "bar")

                  output-lines
                  (str/split-lines out)]
              (is (= exit 42)
                  "Program exit code should be 42.")

              (is (= output-lines
                     ["Hello, Gjs!"
                      "Command line args are: foo, bar"])
                  "Program output should match."))))
        (cleanup)))))

(deftest gtk-offscreen-test
  (testing "gtk-offscreen demo dev-build compilation ..."
    (with-sh-dir "examples/gtk-offscreen"
      (let [cleanup (fn [] (sh "rm" "-r"
                               "./gtk-offscreen.js"
                               "./.shadow-cljs/"
                               "./jslibs/"
                               "./.cpcache/"
                               "window.png"))]
        (cleanup)
        (let [{:keys [exit out err]}
              (sh "clojure" "-m" "shadow.cljs.devtools.cli" "compile" "app")]
          (println out)
          (println err)
          (is (= exit 0)
              "Compiler should report success.")

          (testing "Execution ..."
            (let [{:keys [exit out err]}
                  (sh "gjs" "./gtk-offscreen.js" "200" "50")]
              (println out)
              (println err)
              (is (= exit 0)
                  "Program exit code should be 0.")

              (let [{:keys [out]} (sh "file" "./window.png")]
                (is (= out "./window.png: PNG image data, 200 x 50, 8-bit/color RGBA, non-interlaced\n")
                    "An window.png file should exist with correct dimensions.")))))
        (cleanup)))))
