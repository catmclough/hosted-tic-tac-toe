(ns tictactoe.ai
  (:require [tictactoe.board :as board]))

(def space-scoring-value 10)

(defn- hypothetical-boards [board]
  (let [open-spots (board/available-spaces board)]
    (map #(board/fill-space % (board/active-player board) board) open-spots)))

(defn- score-game [board-state player depth]
  (cond (= player (board/winner board-state)) (- space-scoring-value depth)
      (and (board/winner board-state) (not= player (board/winner board-state))) (- depth space-scoring-value)
      :else 0))

(defn- minimax
  ([board player]
      (minimax board player 0))
  ([board player depth]
    (let [score (score-game board player depth)]
      (if (board/game-over? board)
        score
        (do
          (let [depth (inc depth)]
          (if (= (board/active-player board) player)
            (apply max (map #(minimax % player depth) (hypothetical-boards board)))
            (apply min (map #(minimax % player depth) (hypothetical-boards board))))))))))

(defn- get-scores [board player]
  (let [open-spots (board/available-spaces board)
        minimax-scores (map #(minimax % player) (hypothetical-boards board))]
    (map (fn [space score] {:space space :score score}) open-spots minimax-scores)))

(defn choose-move [board player]
  (let [scores (get-scores board player)]
    (:space (apply max-key :score scores))))

