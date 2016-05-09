(ns hosted-tic-tac-toe.end-game-responder
  (require [hosted-tic-tac-toe.end-game-html :as end-game-html]))

(import '(responders Responder)
        '(http_messages Response$ResponseBuilder Header$HeaderBuilder
                        ResponseHeader HTMLContent Header HTTPStatus)
        '(text_parsers ParameterParser))

(def supported-methods ["GET"])

(defn- request-is-supported [request]
  (not (nil? (some #{(.getMethod request)} supported-methods))))

(defn- get-response-headers []
  (let [content-type (.build (Header$HeaderBuilder. (str (.getKeyword (ResponseHeader/CONTENT_TYPE)) (HTMLContent/contentType) ";")))]
    (into-array Header [content-type])))

(defn- get-winner-from-params [request]
  (let [winner ((clojure.string/split (get (ParameterParser/getDecodedParams (str (.getMethod request) " " (.getURI request))) 0) #"= ") 1)]
      winner))

(defn- get-response-body [request]
  (end-game-html/get-page (get-winner-from-params request)))

(defn- get-game-over-response [request]
  (try
    (if (not (request-is-supported request))
      (.build (.statusLine (Response$ResponseBuilder.) (.getStatusLine HTTPStatus/METHOD_NOT_ALLOWED)))
      (.build (.body (.headers (.statusLine (Response$ResponseBuilder.)
          (.getStatusLine HTTPStatus/OK)) (get-response-headers)) (get-response-body request))))
    (catch Exception e
      (.build (.statusLine (Response$ResponseBuilder.) (.getStatusLine HTTPStatus/NOT_FOUND))))))

(defn new-end-game-responder []
  (reify
    Responder
      (getResponse [this request] (get-game-over-response request))))
