(ns hosted-tic-tac-toe.cookie-manager)

(import '(http_messages Header$HeaderBuilder))

(def id (atom (rand-int 10000)))

(defn generate-new-session-id [] (swap! id (fn [current-id] (+ current-id (rand-int 10000)))))

(defn get-session-id [] (str "sessionId=" @id))

(defn get-set-cookie-header [] (.build (Header$HeaderBuilder. (str "Set-Cookie: " (get-session-id)))))

(def remove-cookies-header (.build (Header$HeaderBuilder. "Set-Cookie: sessionId=deleted; expires=Thu, 01 Jan 1970 00:00:00 GMT")))
