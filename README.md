# Battleship-game
Personal project, practicing computer networks by making a simple game of Battleship with a Server and two Clients.

Open server.bat on a computer designated as a server.
On the computers intended for playing make sure you have Java 23 installed.
After that you only need client.bat and Warship.jar files in the same folder.
Run a terminal in that folder and type in "client.bat (your IP Address)" or alternatively edit the client.bat file and replace the "%1" with your IP Adress.
Repeat the process on a 2nd computer (it can also be done on the computer running the server) to join as the second player.

Placing ships is done by typing cordinates with this format:
"A5 H", the first character is the row, and the second is the column. the third is either "H" for Horizonal or "V" for vertical.
        inputs must be in this format.

After placing the ships, shooting is done by using cordinates in this format:
"B7", where the first character is the row, and the second is the column.
