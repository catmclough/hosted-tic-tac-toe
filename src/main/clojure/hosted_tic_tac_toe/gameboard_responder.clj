(ns hosted-tic-tac-toe.gameboard-responder
  (:require [tictactoe.board :as board]
			[hosted-tic-tac-toe.gameboard-html :as gameboard-html]
            [hosted-tic-tac-toe.board-data-parser :as data-parser]))

(import '(responders Responder)
        '(http_messages HTTPStatus HTMLContent Request
                        Response$ResponseBuilder Header$HeaderBuilder
                        Header ResponseHeader)
        )

(def supported-methods ["GET" "POST"])

(defn- request-is-supported [request]
  (not (nil? (some #{(.getMethod request)} supported-methods))))

(defn- get-headers []
  (let [content-type (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE)) (HTMLContent/contentType) ";")))]
    (into-array Header [content-type])))

(defn- get-board-data [request]
  (let [board (.getData request)]
      board))

(defn- update-board [request-data]
  (let [choice (data-parser/get-spot-choice request-data)]
    (let [marker (data-parser/get-player-marker request-data)]
      (let [current-board (data-parser/get-board request-data)]
        (board/fill-space choice marker current-board)))))

(defn- get-body [request]
  (cond
    (= (.getMethod request) "GET")
      (gameboard-html/get-page (board/make-board))
    (= (.getMethod request) "POST")
      (gameboard-html/get-page (update-board (.getData request)))))

(defn- get-board-response [request]
  (if (not (request-is-supported request))
    (.build (.statusLine (Response$ResponseBuilder.) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED)))
    (.build (.body (.headers (.statusLine (Response$ResponseBuilder.)
      (.getStatusLine HTTPStatus/OK)) (get-headers)) (get-body request)))))

(defn new-gameboard-responder []
  (reify
    Responder
      (getResponse [this request] (get-board-response request))))
