(ns tictactoe.core
    (:require
              [tictactoe.setup :as setup]
              [tictactoe.console :as console]
              [tictactoe.board :as board]
              [tictactoe.ai :as ai]))

(def player-one board/player-one)

(defn get-spot-choice [player board game-type]
  (if (= player player-one)
    (do
      (if (= game-type setup/player-ai-game-type)
        (do (console/prompt-player-turn player)
            (console/get-int-input))
        (do (console/ai-choosing) (ai/choose-move board "X"))))
    (do (console/ai-choosing) (ai/choose-move board "O"))))

(defn end-game [board]
  (cond (board/winner board) (console/winner-message (board/winner board))
        (board/cats-game? board) (console/cats-game-message)))

(defn play-turn [board game-type]
  (let [active-player (board/active-player board)]
    (loop [choice (get-spot-choice active-player board game-type)]
        (or
          (try
            (board/fill-space choice active-player board)
            (catch Exception e (console/displayln (.getMessage e))))
          (recur (get-spot-choice active-player board game-type))))))

(defn play-game
  ([game-type]
    (play-game (board/make-board) game-type))
  ([board game-type]
    (loop [board board]
      (if (board/game-over? board)
        (end-game board)
        (recur (play-turn board game-type))))))

(defn -main []
  (let [game-type (setup/setup-game)]
    (play-game game-type)))

