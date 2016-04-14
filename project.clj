(defproject hosted-tic-tac-toe "0.1.0-SNAPSHOT"
  :description "A TicTacToe game written in Clojure and hosted by a Java Server."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/test.check "0.9.0"]]
  :plugins [[lein-junit "1.1.8"] [speclj "3.3.0"]]
  :source-paths ["src/main/clojure"]
	:java-source-paths ["src/main/java" "src/test/java"]
  :junit ["src/test/java"]
  :test-paths ["src/test/clojure"]
  :main ^:skip-aot hosted-tic-tac-toe.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all} :dev {:dependencies [[junit/junit "4.11"] [speclj "3.3.1"]]}})
