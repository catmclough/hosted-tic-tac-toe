(ns hosted-tic-tac-toe.core
  (:require [tictactoe.console :as console]
            [hosted-tic-tac-toe.ttt-routes :as routes])
  (:gen-class))

(import '(javaserver Router)
        '(text_parsers ArgParser)
        '(factories ServerCreator))

(defn getPortChoice [args]
  (ArgParser/getPortChoice (into-array String args) 5000))

(defn -main
  [& args]
  (do
    (console/welcome-player)
    (let [choice (getPortChoice args)]
      (println (str "Server running on port " choice))
      (let [router (Router. (routes/getRoutes))]
        (let [server (.createServer (ServerCreator.) choice router)]
          (.run server))))))
