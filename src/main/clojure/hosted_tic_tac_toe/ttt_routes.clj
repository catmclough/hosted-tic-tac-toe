(ns hosted-tic-tac-toe.ttt-routes
  (:require [tictactoe.board :as board]
            [hosted-tic-tac-toe.gameboard-responder :as gameboard-responder])
  (:gen-class))


(import '(responders RedirectResponder))

(defn getRoutes []
    (java.util.HashMap. {"/" (RedirectResponder. (into-array String ["GET"]) "/gameboard")
                         "/gameboard" (gameboard-responder/new-gameboard-responder ["GET"])
						}))

;(Form. (into-array Integer/TYPE (map int (board/make-board)))))}))

