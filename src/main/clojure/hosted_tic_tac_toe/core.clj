(ns hosted-tic-tac-toe.core
  (:gen-class))
(import [javaserver App])

(defn -main
  [& args]
  (App/main (into-array [""])))
