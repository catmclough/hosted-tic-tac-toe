(ns hosted-tic-tac-toe.ttt-router
  (:require [tictactoe.board :as board]
            [hosted-tic-tac-toe.gameboard-responder :as gameboard-responder]
            [hosted-tic-tac-toe.end-game-responder :as end-game-responder]))


(import '(routers Router)
        '(responders RedirectResponder))

(defn get-routes-and-responders []
    (java.util.HashMap. {"/" (RedirectResponder. (into-array String ["GET"]) "/gameboard")
                         "/gameboard" (gameboard-responder/new-gameboard-responder)
                         "/game-over" (end-game-responder/new-end-game-responder)}))

(defn new-ttt-router []
  (reify
    Router
      (getRoutesAndResponders [this] (get-routes-and-responders))))
