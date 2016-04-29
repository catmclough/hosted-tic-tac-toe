(ns hosted-tic-tac-toe.gameboard-responder
  (:require [tictactoe.board :as board]))

(import '(responders Responder)
        '(http_messages HTTPStatus HTMLContent Response$ResponseBuilder Header$HeaderBuilder Header ResponseHeader))

(defn getHeaders []
  (let [contentType (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE))
                                                        (HTMLContent/contentType)
                                                        ";")))]
    (into-array Header [contentType])))

(defn get-board-response []
    (let [statusLine (.getStatusLine (HTTPStatus/OK))]
      (let [headers (getHeaders)]
        (let [body (str (HTMLContent/openHTMLAndBody "Game") (board/make-board) (HTMLContent/closeBodyAndHTML))]
          (.build (.body (.headers (.statusLine (Response$ResponseBuilder.) statusLine) headers) body))))))

(defn new-gameboard-responder [supported-methods]
  (reify
    Responder
      (getResponse [this request] (get-board-response))))

