package network;

import game.Player;
import gameObjects.Board;
import gameObjects.Coordinates;
import gameObjects.Ship;

import java.io.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private Socket player1Socket, player2Socket;
    private BufferedReader in1, in2;
    private PrintWriter out1, out2;

    private Player player1, player2;

    public Server() throws IOException {

        ServerSocket serverSocket = new ServerSocket(2030);
        try{
            System.out.println("Waiting for players...");

            player1Socket = serverSocket.accept();
            System.out.println("Player 1 connected!");
            in1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            out1 = new PrintWriter(new OutputStreamWriter(player1Socket.getOutputStream()), true);
            out1.println("Welcome, You are Player 1. Waiting for Player 2...");

            player2Socket = serverSocket.accept();
            System.out.println("Player 2 connected!");
            in2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            out2 = new PrintWriter(new OutputStreamWriter(player2Socket.getOutputStream()), true);
            out2.println("Welcome, You are Player 2.");
            out1.println("Player 2 connected! Game starting...");

            player1 = new Player("Player 1");
            player2 = new Player("Player 2");

            placeShips(player1, in1, out1);
            out2.println("Player 1 is placing their ships, please wait...");
            placeShips(player2, in2, out2);

            System.out.println("Both players placed ships!");

            while(true) {
                shoot(player1, player2, in1, out1, out2);
                if(isGameOver(player2)){
                    out1.println("You Win!");
                    out2.println("You Lose!");
                    try { Thread.sleep(3000); } catch (InterruptedException e) {}
                    out1.println("GAMEOVER");
                    out2.println("GAMEOVER");
                    break;
                }
                shoot(player2, player1, in2, out2, out1);
                if(isGameOver(player1)){
                    out2.println("You Win!");
                    out1.println("You Lose!");
                    try { Thread.sleep(3000); } catch (InterruptedException e) {}
                    out2.println("GAMEOVER");
                    out1.println("GAMEOVER");
                    break;
                }
            }
        }finally{
            System.out.println("Closing connections...");
            if(out1 != null) out1.close();
            if(out2 != null) out2.close();
            if(in1 != null) in1.close();
            if(in2 != null) in2.close();
            if(player1Socket != null) player1Socket.close();
            if(player2Socket != null) player2Socket.close();
            serverSocket.close();
        }
    }

    private void sendBoard(PrintWriter out, Player player, boolean hidden) {
        String board = player.getBoard().getBoardAsString(hidden);
        for(String line : board.split("\n")) {
            out.println(line);
        }
        out.println("END");
    }

    private void placeShips(Player player, BufferedReader in, PrintWriter out) {
        String[] shipNames = {"Carrier", "Battleship", "Destroyer", "Submarine", "Patrol"};
        int[] shipSizes = {5, 4, 3, 3, 2};

        for(int i = 0; i < 5; i++){
            clearScreen(out);
            out.println("\n" + player.getName() + " - Place your ships!");
            out.println("\nPlacing: " + shipNames[i] + " (size " + shipSizes[i] + ")\n");

            sendBoard(out, player, false);
            out.println("\nChose the cordinates to place the ship: ");

            Ship ship = new Ship(shipNames[i], shipSizes[i]);
            boolean placed = false;

            while(!placed){
                try {
                    out.println("INPUT");
                    String input = in.readLine();
                    String[] cordinates = input.split(" ");
                    int row, col;
                    char c = cordinates[0].charAt(0);
                    row = c - 'A';
                    col = Integer.parseInt(cordinates[0].substring(1)) - 1;

                    String direction = cordinates[1].toUpperCase();
                    if(!direction.equalsIgnoreCase("H") && !direction.equalsIgnoreCase("V")){
                        out.println("Invalid direction! Use H for horizontal or V for vertical directions.");
                        continue;
                    }

                    boolean horizonal = direction.equals("H");

                    placed = player.getBoard().placeShip(ship, row, col, horizonal);

                    if (!placed) {
                        out.println("Invalid Placement, Try Again.");
                    }
                }catch (Exception e){
                    out.println("Wrong Input Format, Try Again.");
                }
            }
            player.getShips().add(ship);
        }
        clearScreen(out);
        out.println("All ships placed! Waiting for opponent...");
    }

    private void shoot(Player attacker, Player defender, BufferedReader in, PrintWriter attackerOut, PrintWriter defenderOut) throws IOException {
        clearScreen(attackerOut);
        attackerOut.println("\n\t   YOUR BOARD\n");
        sendBoard(attackerOut, attacker, false);
        attackerOut.println("\n----------------------------------\n");
        attackerOut.println("\t   ENEMY BOARD\n");
        sendBoard(attackerOut, defender, true);
        attackerOut.println("\n\n");

        boolean validShot = false;
        String hitMessage = "";

        while(!validShot){
            try {
                attackerOut.println("Enter coordinates to attack: ");
                attackerOut.println("INPUT");
                String input = in.readLine();

                char letter = input.toUpperCase().charAt(0);
                int row = letter - 'A';
                int col = Integer.parseInt(input.substring(1)) - 1;

                Board board = defender.getBoard();
                int[][] grid = board.getGrid();

                if(grid[row][col] == Board.EMPTY){
                    grid[row][col] = Board.MISS;
                    hitMessage = "\tMiss!";
                }
                else if(grid[row][col] == Board.SHIP){
                    grid[row][col] = Board.HIT;

                    Coordinates shot = new Coordinates(col, row);
                    for(Ship ship : defender.getShips()){
                        if(ship.isHit(shot)){
                            if(!ship.isAlive()){
                                for(Coordinates c : ship.getCordinates()){
                                    defender.getBoard().getGrid()[c.getY()][c.getX()] = Board.SUNK;
                                }
                                hitMessage = "\tYou sunk a ship!";
                            }else{
                                hitMessage = "\tHit!";
                            }
                            break;
                        }
                    }
                }
                else{
                    attackerOut.println("Already attacked, try again.");
                    continue;
                }

                clearScreen(attackerOut);
                attackerOut.println("\n\t   YOUR BOARD\n");
                sendBoard(attackerOut, attacker, false);
                attackerOut.println("\n----------------------------------\n");
                attackerOut.println("\t   ENEMY BOARD\n");
                sendBoard(attackerOut, defender, true);
                attackerOut.println("\n\n" + hitMessage);
                attackerOut.println("\nShot result shown above. Waiting for opponent's turn...");

                clearScreen(defenderOut);
                defenderOut.println("\t   YOUR BOARD");
                sendBoard(defenderOut, defender, false);
                defenderOut.println("Waiting for opponent's turn...");

                validShot = true;
            }
            catch (Exception e){
                attackerOut.println("Wrong Input Format, Try Again.");
            }
        }
    }

    public boolean isGameOver(Player player){
        for(Ship ship : player.getShips()){
            if(ship.isAlive()) return false;
        }
        return true;
    }

    private void clearScreen(PrintWriter out) {
        out.println("CLEAR");
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
