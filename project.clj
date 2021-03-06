(defproject app "0.1.0-SNAPSHOT"
	:description "news app"
	:url "http://example.com/FIXME"
	:license
	{:name "Eclipse Public License"
	 :url  "http://www.eclipse.org/legal/epl-v10.html"}

	:dependencies [[org.clojure/clojure "1.9.0"]
				   [ring-server "0.5.0"]
				   [buddy/buddy-auth "2.1.0"]
				   [reagent "0.7.0"]
				   [reagent-utils "0.2.1"]
				   [ring "1.6.3"]
				   [ring/ring-defaults "0.3.1"]
				   [compojure "1.6.0"]
				   [re-frame "0.10.1"]
				   [cljs-ajax "0.7.3"]
				   [hiccup "1.0.5"]
				   [garden "1.3.2"]
				   [ring/ring-json "0.4.0"]
				   [cljsjs/moment "2.17.1-1"]
				   [yogthos/config "0.9"]
				   [org.clojure/clojurescript "1.9.946"
					:scope
					"provided"]
				   [secretary "1.2.3"]
				   [venantius/accountant
					"0.2.3"
					:exclusions
					[org.clojure/tools.reader]]]

	:plugins
	[[lein-environ "1.1.0"]
	 [lein-cljsbuild "1.1.7"]
	 [lein-asset-minifier "0.2.7" :exclusions [org.clojure/clojure]]]

	:ring
	{:handler      app.handler/app
	 :uberwar-name ".war"}

	:min-lein-version "2.5.0"

	:uberjar-name "app.jar"

	:main app.server

	:clean-targets
	^{:protect false}
	[:target-path
	 [:cljsbuild :builds :app :compiler :output-dir]
	 [:cljsbuild :builds :app :compiler :output-to]]

	:source-paths ["src/clj" "src/cljc"]
	:resource-paths ["resources" "target/cljsbuild"]

	:minify-assets
	{:assets
	 {"resources/public/css/style.min.css" "resources/public/css/style.css"}}

	:cljsbuild
	{:builds {:min
			  {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
			   :compiler
			   {:output-to     "target/cljsbuild/public/js/app.js"
				:output-dir    "target/cljsbuild/public/js"
				:source-map    "target/cljsbuild/public/js/app.js.map"
				:optimizations :advanced
				:pretty-print  false}}
			  :app
			  {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
			   :figwheel     {:on-jsload "app.core/mount-root"}
			   :compiler
			   {:main          "app.dev"
				:asset-path    "/js/out"
				:output-to     "target/cljsbuild/public/js/app.js"
				:output-dir    "target/cljsbuild/public/js/out"
				:source-map    true
				:preloads [devtools.preload]
				:optimizations :none
				:pretty-print  true}}}}

	:figwheel
	{:http-server-root "public"
	 :server-port      8080
	 :nrepl-port       7002
	 :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]

	 :css-dirs         ["resources/public/css"]
	 :ring-handler     app.handler/app}


	:profiles
	{:dev     {:repl-options {:init-ns          app.repl
							  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

			   :dependencies [[binaryage/devtools "0.9.8"]
							  [ring/ring-mock "0.3.2"]
							  [ring/ring-devel "1.6.3"]
							  [prone "1.1.4"]
							  [figwheel-sidecar "0.5.14"]
							  [org.clojure/tools.nrepl "0.2.13"]
							  [com.cemerick/piggieback "0.2.2"]
							  [pjstadig/humane-test-output "0.8.3"]]


			   :source-paths ["env/dev/clj"]
			   :plugins      [[lein-figwheel "0.5.14"]]


			   :injections   [(require
							   'pjstadig.humane-test-output)
							  (pjstadig.humane-test-output/activate!)]

			   :env          {:dev  true
							  :port "3000"}}

	 :uberjar {:hooks        [minify-assets.plugin/hooks]
			   :source-paths ["env/prod/clj"]
			   :prep-tasks   ["compile" ["cljsbuild" "once" "min"]]
			   :env          {:production true}
			   :aot          :all
			   :language-out :ecmascript5
			   :omit-source  true}})
