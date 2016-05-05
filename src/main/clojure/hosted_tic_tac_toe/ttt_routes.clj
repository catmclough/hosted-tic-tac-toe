(ns hosted-tic-tac-toe.ttt-routes
  (:require [tictactoe.board :as board]
            [hosted-tic-tac-toe.gameboard-responder :as gameboard-responder]
            [hosted-tic-tac-toe.end-game-responder :as end-game-responder])
  (:gen-class))


(import '(responders RedirectResponder))

(defn get-routes []
    (java.util.HashMap. {"/" (RedirectResponder. (into-array String ["GET"]) "/gameboard")
                         "/gameboard" (gameboard-responder/new-gameboard-responder)
                         "/game-over" (end-game-responder/new-end-game-responder)}))
