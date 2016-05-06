(ns hosted-tic-tac-toe.board-data-parser
  (require [clojure.string :as string]))

(import '(decoders UTF8Decoder))

(defn get-vars [request-line]
  (string/split (UTF8Decoder/decode request-line) #"&"))

(defn get-data [var]
  (get (string/split var #"=") 1))

(defn get-board [request-data]
  (let [raw-board (get-data (get (get-vars request-data) 0))]
    (let [symbolic-board (map read-string (string/split raw-board #""))]
      (vec (map #(if (not (number? %)) (name %) %) symbolic-board)))))

(defn get-spot-choice [request-data]
  (read-string (get-data (get (get-vars request-data) 1))))

(defn get-player-marker [request-data]
  (get-data (get (get-vars request-data) 2)))
