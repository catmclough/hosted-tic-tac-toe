(ns hosted-tic-tac-toe.gameboard-responder
  (:require [tictactoe.board :as board]))

(import '(responders Responder)
        '(http_messages HTTPStatus HTMLContent Response$ResponseBuilder Header$HeaderBuilder Header ResponseHeader))

(def page-name "Game Board")

(def supported-methods ["GET"])

(defn- request-is-supported [request]
  (not (nil? (some #{(.getMethod request)} supported-methods))))

(defn- get-status-line [request]
  (do
    (if (request-is-supported request)
      (.getStatusLine HTTPStatus/OK)
      (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED))))

(defn- get-headers []
  (let [content-type (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE)) (HTMLContent/contentType) ";")))]
    (into-array Header [content-type])))

(defn- get-body [request]
  (if (request-is-supported request)
    (str (HTMLContent/openHTMLAndBody page-name) (board/make-board) (HTMLContent/closeBodyAndHTML))
    nil))

(defn get-board-response [request]
  (let [status-line (get-status-line request)]
    (let [headers (get-headers)]
      (let [body (get-body request)]
        (.build (.body (.headers (.statusLine (Response$ResponseBuilder.) status-line) headers) body))))))

(defn new-gameboard-responder []
  (reify
    Responder
      (getResponse [this request] (get-board-response request))))
