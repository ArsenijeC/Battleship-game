package game;

import gameObjects.Board;
import gameObjects.Coordinates;
import gameObjects.Ship;

import java.util.Scanner;

public class Game {

    private Player player1;
    private Player player2;


    public void start(){
        player1 = new Player("game.Player 1");
        player2 = new Player("game.Player 2");

        placeShips(player1);
        placeShips(player2);

        while(true){
            shoot(player1, player2);
            if(isGameOver(player2)){
                System.out.println(player1.getName() + " Wins!");
                break;
            }
            shoot(player2, player1);
            if(isGameOver(player1)){
                System.out.println(player2.getName() + " Wins!");
                break;
            }
        }
    }

    public void placeShips(Player player){
        Scanner input = new Scanner(System.in);

        String[] shipNames = {"Carrier", "Battleship", "Destroyer", "Submarine", "Patrol"};
        int[] shipSizes = {5, 4, 3, 3, 2};

        for(int i = 0; i < 5; i++){
            clearScreen();
            System.out.println("\n" + player.getName() + " - Place your ships!");
            System.out.println("\nPlacing: " + shipNames[i] + " (size " + shipSizes[i] + ")\n");

            player.getBoard().printBoard(false);
            System.out.println("Chose the cordinates to place the ship: ");

            Ship ship = new Ship(shipNames[i], shipSizes[i]);
            boolean placed = false;

            while(!placed){
                try {
                    String in = input.nextLine();
                    String[] cordinates = in.split(" ");
                    int row, col;
                    char c = cordinates[0].charAt(0);
                    row = c - 'A';
                    col = Integer.parseInt(cordinates[0].substring(1)) - 1;

                    String direction = cordinates[1].toUpperCase();
                    if(!direction.equalsIgnoreCase("H") && !direction.equalsIgnoreCase("V")){
                        System.out.println("Invalid direction! Use H for horizontal or V for vertical directions.");
                        continue;
                    }

                    boolean horizonal = direction.equals("H");

                    placed = player.getBoard().placeShip(ship, row, col, horizonal);

                    if (!placed) {
                        System.out.println("Invalid Placement, Try Again.");
                    }
                }catch (Exception e){
                    System.out.println("Wrong Input Format, Try Again.");
                }
            }
            player.getShips().add(ship);
        }
    }

    public void shoot(Player attacker, Player defender){
        clearScreen();
        System.out.println("\nYOUR BOARD\n");
        attacker.getBoard().printBoard(false);
        System.out.println("--------------------------------------\n");
        System.out.println("ENEMY BOARD\n");
        defender.getBoard().printBoard(true);

        Scanner input = new Scanner(System.in);
        boolean validShot = false;

        while(!validShot){
            try {
                System.out.println("Enter The Cordinates To Attack: ");
                String in = input.nextLine();
                int row, col;
                char letter = in.toUpperCase().charAt(0);
                row = letter - 'A';
                col = Integer.parseInt(in.substring(1)) - 1;

                Board board = defender.getBoard();
                int[][] grid = board.getGrid();

                if(grid[row][col] == Board.EMPTY){
                    grid[row][col] = Board.MISS;
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
                                System.out.println("You sunk an enemy ship!");
                            }else{
                                System.out.println("Hit!");
                            }
                            break;
                        }
                    }
                }
                else{
                    System.out.println("You already attacked that space, try again.");
                    continue;
                }

                validShot = true;
            }
            catch (Exception e){
                System.out.println("Wrong Input Format, Try Again.");
            }
        }
    }

    public boolean isGameOver(Player player){
        for(Ship ship : player.getShips()){
            if(ship.isAlive()) return false;
        }
        return true;
    }

    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}
