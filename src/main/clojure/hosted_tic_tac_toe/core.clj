(ns hosted-tic-tac-toe.core
  (:require [tictactoe.console :as console]
            [hosted-tic-tac-toe.ttt-router :as router])
  (:gen-class))

(import '(routers Router)
        '(javaserver Server)
        '(text_parsers ArgParser)
        '(factories ServerCreator))

(def default-port 5000)

(defn- get-port-choice [args]
  (ArgParser/getPortChoice (into-array String args) default-port))

(defn get-server [port router]
  (.createServer (ServerCreator.) port router))

(defn get-ttt-router []
  (router/new-ttt-router))

(defn run-server [server]
  (.run server))

(defn -main
  [& args]
  (let [port-choice (get-port-choice args)]
    (println (str "Server running on port " port-choice))
    (let [router (get-ttt-router)]
      (let [server (get-server port-choice router)]
        (run-server server)))))
