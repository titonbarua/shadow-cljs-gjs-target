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
      (let [cleanup (fn [] (sh "rm" "-r" "./simple.js" "./.shadow-cljs/" "./.cpcache/"))]
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
                  (->> (str/split-lines err)
                       (filter #(str/includes? % "JS LOG:"))
                       (map #(str/replace % #".*JS LOG:\s+" "")))]
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
                  (->> (str/split-lines err)
                       (filter #(str/includes? % "JS LOG:"))
                       (map #(str/replace % #".*JS LOG:\s+" "")))]
              (is (= exit 42)
                  "Program exit code should be 42.")

              (is (= output-lines
                     ["Hello, Gjs!"
                      "Command line args are: foo, bar"])
                  "Program output should match."))))
        (cleanup)))))
