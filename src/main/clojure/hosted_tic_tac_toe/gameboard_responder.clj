(ns hosted-tic-tac-toe.gameboard-responder
  (:require [tictactoe.board :as board]
            [tictactoe.ai :as ai]
			[hosted-tic-tac-toe.gameboard-html :as gameboard-html]
            [hosted-tic-tac-toe.board-data-parser :as data-parser]))

(import '(responders Responder)
        '(http_messages HTTPStatus HTMLContent Request
                        Response$ResponseBuilder Header$HeaderBuilder
                        Header ResponseHeader)
        '(java.net URLEncoder))

(def supported-methods ["GET" "POST"])

(def ai-marker "O")

(defn- request-is-supported [request]
  (not (nil? (some #{(.getMethod request)} supported-methods))))

(defn- get-status-line [gameboard]
  (if (board/game-over? gameboard)
    (.getStatusLine (HTTPStatus/FOUND))
    (.getStatusLine (HTTPStatus/OK))))

(defn game-over-route [gameboard]
  (let [winner (board/winner gameboard)]
    (if (nil? winner)
      (str "/game-over" (URLEncoder/encode "?winner=nil" "UTF-8"))
      (str "/game-over" (URLEncoder/encode (str "?winner=" (board/winner gameboard)) "UTF-8")))))

(defn- get-headers [gameboard]
  (if (board/game-over? gameboard)
    (let [redirect-header (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/REDIRECT)) (game-over-route gameboard))))]
        (into-array Header [redirect-header]))
    (let [content-type (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE)) (HTMLContent/contentType) ";")))]
      (into-array Header [content-type]))))

(defn- get-board-data [request]
  (let [board (.getData request)]
      board))

(defn- update-board [request-data]
  (let [choice (data-parser/get-spot-choice request-data)]
    (let [marker (data-parser/get-player-marker request-data)]
      (let [current-board (data-parser/get-board request-data)]
        (board/fill-space choice marker current-board)))))

(defn- get-board [request]
  (cond
    (= (.getMethod request) "GET")
      (board/make-board)
    (= (.getMethod request) "POST")
      (let [player-choice-board (update-board (.getData request))]
        (if (not (board/game-over? player-choice-board))
          (let [ai-choice (ai/choose-move player-choice-board ai-marker)]
            (board/fill-space ai-choice ai-marker player-choice-board))
        player-choice-board))))

(defn- get-board-response [request]
  (try
    (if (not (request-is-supported request))
      (.build (.statusLine (Response$ResponseBuilder.) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED)))
      (let [gameboard (get-board request)]
        (.build (.body (.headers (.statusLine (Response$ResponseBuilder.)
          (get-status-line gameboard)) (get-headers gameboard)) (gameboard-html/get-page gameboard)))))
    (catch Exception e
      (.build (.statusLine (Response$ResponseBuilder.) (.getStatusLine (HTTPStatus/NOT_FOUND)))))))

(defn new-gameboard-responder []
  (reify
    Responder
      (getResponse [this request] (get-board-response request))))
