(ns hosted-tic-tac-toe.ttt-round
  (:require
    [tictactoe.board :as board]
    [tictactoe.ai :as ai]
    [tictactoe.setup :as ttt-setup]
    [hosted-tic-tac-toe.board-data-parser :as data-parser]))

(def user-marker ttt-setup/player-one)

(def ai-marker ttt-setup/player-two)

(defn- update-board [request-data]
  (let [choice (data-parser/get-spot-choice request-data)]
    (let [marker (data-parser/get-player-marker request-data)]
      (let [current-board (data-parser/get-board request-data)]
        (board/fill-space choice marker current-board)))))

(defn play-round [request]
  (let [player-choice-board (update-board (.getData request))]
    (if (not (board/game-over? player-choice-board))
      (let [ai-choice (ai/choose-move player-choice-board ai-marker)]
        (board/fill-space ai-choice ai-marker player-choice-board))
    player-choice-board)))
