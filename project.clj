(defproject hosted-tic-tac-toe "0.1.0-SNAPSHOT"
  :description "A TicTacToe game written in Clojure and hosted by a Java Server."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [speclj_ttt "0.1.0-SNAPSHOT"]
                 [javaserver/javaserver "1.0-SNAPSHOT"]]
  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :main ^:skip-aot hosted-tic-tac-toe.core
  :target-path "target/%s"
  :repositories {"local" ~(str (.toURI (java.io.File. "maven_repository")))})
