(defproject poker "0.1.0-SNAPSHOT"
  :description "A macro-based refactoring library for Clojure."
  :url "http://github.com/ctford/poker"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"
            :distribution :repo}
    :dependencies [[org.clojure/clojure "1.4.0"]]
    :profiles {:dev
               {:plugins [[lein-midje "3.0.0"]]
                :dependencies [[midje "1.5.1"]]}})
