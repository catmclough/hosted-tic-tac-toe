(ns hosted-tic-tac-toe.gameboard-responder
  (:require [tictactoe.board :as board]
            [tictactoe.ai :as ai]
            [hosted-tic-tac-toe.ttt-round :as round]
			[hosted-tic-tac-toe.gameboard-html :as gameboard-view]))

(import '(responders Responder)
        '(http_messages HTTPStatus Request
                        Response$ResponseBuilder Header$HeaderBuilder
                        Header ResponseHeader)
        '(java.net URLEncoder))

(def supported-methods ["GET" "POST"])

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
    (let [content-type (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE)) gameboard-view/content-type ";")))]
      (into-array Header [content-type]))))

(defn- get-board [request]
  (cond
    (= (.getMethod request) "GET") (board/make-board)
    (= (.getMethod request) "POST") (round/play-round request)))

(defn- get-board-response [request]
  (try
    (if (not (request-is-supported request))
      (.build (.statusLine (Response$ResponseBuilder.) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED)))
      (let [gameboard (get-board request)]
        (.build (.body (.headers (.statusLine (Response$ResponseBuilder.)
          (get-status-line gameboard)) (get-headers gameboard)) (gameboard-view/get-page gameboard)))))
    (catch Exception e
      (.build (.statusLine (Response$ResponseBuilder.) (.getStatusLine (HTTPStatus/NOT_FOUND)))))))

(defn new-gameboard-responder []
  (reify
    Responder
      (getResponse [this request] (get-board-response request))))
