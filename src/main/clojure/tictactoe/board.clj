(ns tictactoe.board
  (:require [tictactoe.setup :as setup]))

(def player-one setup/player-one)

(def player-two setup/player-two)

(def board-size 9)

(def side-length 3)

(def middle-spot 4)

(defn make-board []
	(vec (range board-size)))

(defn- space-is-empty? [position board]
	(number? (nth board position)))

(defn- filled? [board]
	(not-any? number? board))

(defn available-spaces [board]
  (filter #(space-is-empty? % board) (range (count board))))

(defn active-player [board]
  (let [x-count (get (frequencies board) player-one)
        o-count (get (frequencies board) player-two)]
    (cond (= x-count nil) player-one
          (= o-count nil) player-two
          (<= x-count o-count) player-one
          :else player-two)))

(defn- horizontal-slices [board]
  	(partition side-length board))

(defn- vertical-slices [board]
  	(apply map list (horizontal-slices board)))

(defn- diagonal-one [board]
  	(list (first board) (nth board middle-spot) (last board)))

(defn- diagonal-two [board]
 	(list (nth board (dec side-length)) (nth board middle-spot) (nth board (- board-size side-length))))

(defn- horizontal-winner [board]
  	(let [horizontals (horizontal-slices board)]
    	(cond
        (and (not-any? number? (first horizontals)) (apply = (first horizontals)))
            (first (first horizontals))
        (and (not-any? number? (second horizontals)) (apply = (second horizontals)))
            (first (second horizontals))
        (and (not-any? number? (nth horizontals (dec side-length))) (apply = (nth horizontals (dec side-length))))
            (first (nth horizontals (dec side-length))))))

(defn- vertical-winner [board]
  (let [verticals (vertical-slices board)]
    (cond (and (not-any? number? (first verticals)) (apply = (first verticals)))
              (first (first verticals))
          (and (not-any? number? (second verticals)) (apply = (second verticals)))
              (first (second verticals))
          (and (not-any? number? (nth verticals (dec side-length))) (apply = (nth verticals (dec side-length))))
              (first (nth verticals (dec side-length))))))

(defn- diagonal-winner [board]
  (let [diagonals ((fn [x] (list (diagonal-one x) (diagonal-two x))) board)]
      (cond (and (not-any? number? (first diagonals)) (apply = (first diagonals)))
                (first (first diagonals))
            (and (not-any? number? (second diagonals)) (apply = (second diagonals)))
                (first (second diagonals)))))

(defn winner [board]
  (or (horizontal-winner board) (vertical-winner board) (diagonal-winner board)))

(defn cats-game? [board]
  (and (not (winner board)) (filled? board)))

(defn game-over? [board]
  (cond (winner board) true
        (cats-game? board) true
        :else false))

(defn valid-spot-choice? [choice board]
	(and (some #{choice} (range board-size))
	     (some #{choice} (available-spaces board))))

(defn fill-space
	[choice marker board]
		(if (valid-spot-choice? choice board)
			(assoc board choice marker)
		  (throw (Exception. "Invalid Spot Choice. Please enter the number of an open spot on the board."))))

