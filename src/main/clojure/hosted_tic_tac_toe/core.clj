(ns hosted-tic-tac-toe.core
  (:require [tictactoe.console :as console])
  (:gen-class))

(import [javaserver App ClientWorker Reader RequestParser
                    Routes SocketWriter])

(defn -main
  [& args]
  (do
    (console/welcome-player)
    (App/main (into-array String args))))
    ;(let [server App/server]
      ;(let [clientSocket (.accept (.serverSocket server))]
        ;(let [clientWorker (ClientWorker. clientSocket)]
          ;(let [reader (Reader.)]
            ;(.openReader reader clientSocket)
            ;(let [rawRequest (.getRequest clientWorker reader)]
              ;(let [request (RequestParser/createRequest rawRequest)]
                ;(let [responder (Routes/getResponder (.getURIWithoutParams request))]
                  ;(let [response (.getResponse responder request)]
                    ;(let [writer (SocketWriter.)]
                      ;(.openWriter writer clientSocket)
                      ;(.respond writer (.formatResponse response)))))))))))))
