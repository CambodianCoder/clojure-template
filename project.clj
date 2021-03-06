(defproject ZZZZ "0.1.0-SNAPSHOT"

  :description "Default template for a BYU ODH Clojure Program"
  :url "http://example.com/FIXME"

  :dependencies [[byu-odh/byu-cas "0.1.5"]
                 [camel-snake-kebab "0.4.0"]
                 [cheshire "5.9.0"]
                 [clj-http "3.10.0"]
                 [com.mchange/c3p0 "0.9.5.4"]
                 [cprop "0.1.14"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [garden "1.3.9"]
                 [hiccup "1.0.5"]
                 [hikari-cp "2.9.0"]
                 [honeysql "0.9.8"]
                 [luminus-immutant "0.2.5"]
                 [luminus-migrations "0.6.5"]
                 [metosin/compojure-api "1.1.12"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.16"]
                 [com.taoensso/timbre "4.10.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/tools.logging "0.5.0"]
                 [org.postgresql/postgresql "42.2.8"]
                 [metosin/reitit "0.3.9"]
                 [metosin/reitit-schema "0.3.9"]
                 [metosin/reitit-frontend "0.3.9"]
                 [org.webjars.npm/bulma "0.7.5"]
                 [org.webjars/font-awesome "5.10.1"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [ring-middleware-format "0.7.4"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-defaults "0.3.2"]
                 [tick "0.4.20-alpha"]
                 [reformation "4"]
                 [re-frame "0.10.9"]
                 [byu-odh/retabled "7"]]

  :min-lein-version "2.0.0"
  :eastwood {:linters [:all]
             :exclude-linters [:unused-private-vars :unused-fn-args :duplicate-params :unused-locals :keyword-typos :unused-namespaces]
             :exclude-namespaces [:test-paths]}
  :test-paths ["test/clj/unit" "test/clj/usecase"]
  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj" "src/cljc" "src/cljs"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/"
  :main ZZZZ.core
  :plugins [[lein-cprop "1.0.3"]
            [migratus-lein "0.7.2"]
            [lein-cljsbuild "1.1.7"]
            [lein-immutant "2.1.0"]
            [lein-garden "0.3.0"]]
  :garden {:builds [{:id "ZZZZ-viz"
                     :source-path "src/clj/ZZZZ/styles"
                     ;; The var containing your stylesheet:
                     :stylesheet ZZZZ.styles.style/ZZZZ
                     :compiler {;; Where to save the file:
                                :output-to "resources/public/css/style.css"
                                :pretty-print? true}}]}
  :clean-targets ^{:protect false}
  [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]

  :figwheel {:http-server-root "public"
             :nrepl-port 7002
             :css-dirs ["resources/public/css"]}
  
  :immutant {:war {:context-path "/"
                   :name "ZZZZ%t"}}
  :test-selectors {:default (complement :integration)
                 :integration :integration}

  :profiles
  {:uberjar {:omit-source true
             :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
             :cljsbuild
             {:builds
              {:min
               {:source-paths ["src/cljc" "src/cljs" "env/prod/cljs"]
                :compiler
                {:output-to "target/cljsbuild/public/js/app.js"
                 :externs ["react/externs/react.js"]
                 :optimizations :advanced
                 :pretty-print false
                 :closure-warnings
                 {:externs-validation :off :non-standard-jsdoc :off}}}}}
             
             
             :aot :all
             :uberjar-name "ZZZZ.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]


   :project/dev  {:dependencies [[binaryage/devtools "0.9.10"]
                                 [etaoin "0.3.5"]
                                 [figwheel-sidecar "0.5.19"]
                                 [garden-gnome "0.1.0"]
                                 [pjstadig/humane-test-output "0.9.0"]
                                 [prone "2019-07-08"]
                                 [ring/ring-devel "1.7.1"]
                                 [ring/ring-mock "0.4.0"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.14.0"]
                                 [lein-figwheel "0.5.19"]]
                  :cljsbuild
                  {:builds
                   {:app
                    {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                     :compiler
                     {:main "ZZZZ.app"
                      :asset-path "/js/out"
                      :output-to "target/cljsbuild/public/js/app.js"
                      :output-dir "target/cljsbuild/public/js/out"
                      :source-map true
                      :optimizations :none
                      :pretty-print true}}}}
                  :source-paths ["env/dev/clj" "test/clj/unit" "test/clj/usecase" "test/cljs"]
                  :resource-paths ["env/dev/resources"]
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:resource-paths ["env/test/resources"]}
   :profiles/dev {:repl-options {:init-ns user}}
   :profiles/test {}})
