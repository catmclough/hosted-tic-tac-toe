(ns hosted-tic-tac-toe.end-game-html)

(import '(http_messages HTMLContent))

(def page-title "Game Over")

(def new-game-button
  "<form action=\"/\" method=\"/get\">
    <input type=\"submit\" value=\"New Game\" />
   </form>")

(defn get-page [] (str (HTMLContent/openHTMLAndBody page-title) new-game-button (HTMLContent/closeBodyAndHTML)))
