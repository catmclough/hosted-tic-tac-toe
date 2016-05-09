(ns hosted-tic-tac-toe.core
  (:require [tictactoe.console :as console]
            [hosted-tic-tac-toe.ttt-routes :as routes])
  (:gen-class))

(import '(javaserver Router Server)
        '(text_parsers ArgParser)
        '(factories ServerCreator))

(def default-port 5000)

(defn- get-port-choice [args]
  (ArgParser/getPortChoice (into-array String args) default-port))

(defn get-router [routes]
  (Router. routes))

(defn get-server [port router]
  (.createServer (ServerCreator.) port router))

(defn run-server [server]
  (.run server))

(defn -main
  [& args]
  (let [port-choice (get-port-choice args)]
    (println (str "Server running on port " port-choice))
    (let [router (get-router (routes/get-routes))]
      (let [server (get-server port-choice router)]
        (try
          (run-server server)
          (catch Exception e (str "Caught exception while running server: " (.getMessage e)))
          (finally (if (= (type server) Server) (.shutDown server)))))))) ;test this by mocking server creator and returning bad server and no exceptions are thrown
