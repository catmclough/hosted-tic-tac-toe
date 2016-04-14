(ns hosted-tic-tac-toe.core
  (:require [tictactoe.console :as console])
  (:gen-class))

(import [javaserver App])

(defn -main
  [& args]
  (do
    (console/welcome-player)
    (App/main (into-array [""]))))
