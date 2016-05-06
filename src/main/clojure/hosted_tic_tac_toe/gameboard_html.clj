(ns hosted-tic-tac-toe.gameboard-html
  (require [tictactoe.board :as board]))

(import '(http_messages HTMLContent))

(def page-name "Game Board")

(defn get-button [board space marker]
  (str "<form action=\"/gameboard\" method=\"post\">
          <input type=\"hidden\" name=\"board\" value=\"" board "\"/>
          <input type=\"hidden\" name=\"choice\" value=\"" space "\"/>
          <input type=\"hidden\" name=\"marker\" value=\"" marker "\"/>
          <input type=\"submit\" value=\"-\" />
        </form>"))

(defn get-filled-space [gameboard space]
  (str "<font size=\"25\">" (get gameboard space) "</font>"))

(defn- get-board-space [gameboard space]
  (if (some #{space} (board/available-spaces gameboard))
    (get-button (apply str gameboard) space (board/active-player gameboard))
    (get-filled-space gameboard space)))

(defn- get-ttt-board [board]
    (str "<table style=\"width:50%\">
    <caption>TicTacToe Board</caption>
    <col width=\"33%\">
    <col width=\"33%\">
    <col width=\"33%\">
    <tr>
      <td height=\"100\">" (get-board-space board 0) "</td>
      <td height=\"100\">" (get-board-space board 1) "</td>
      <td height=\"100\">" (get-board-space board 2) "</td>
    </tr>
    <tr>
      <td height=\"100\">" (get-board-space board 3) "</td>
      <td height=\"100\">" (get-board-space board 4) "</td>
      <td height=\"100\">" (get-board-space board 5) "</td>
    </tr>
    <tr>
      <td height=\"100\">" (get-board-space board 6) "</td>
      <td height=\"100\">" (get-board-space board 7) "</td>
      <td height=\"100\">" (get-board-space board 8) "</td>
    </tr>
  </table>"))

(defn get-page [board]
  (str
    (HTMLContent/openHTMLAndBody page-name)
    (get-ttt-board board)
    (HTMLContent/closeBodyAndHTML)))
