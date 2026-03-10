package gameObjects;



public class Board {

    private int[][] grid;
    public static final int EMPTY = 0;
    public static final int SHIP  = 1;
    public static final int HIT   = 2;
    public static final int MISS  = 3;
    public static final int SUNK  = 4;

    public static final String RESET  = "\u001B[0m";
    public static final String RED    = "\u001B[31m";
    public static final String BLUE   = "\u001B[34m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";

    public Board() {
        grid = new int[10][10];
    }

    public void printBoard(boolean hidden) {
        for (int row = 0; row <= 10; row++) {
            for (int col = 0; col <= 10; col++) {

                if(row == 0 && col == 0)
                    System.out.printf("    ");
                else if(row == 0){
                    System.out.printf("%d  ", col);
                }
                else if(row != 0 && col == 0){
                    System.out.printf("%c  ", 'A' + row - 1);
                }
                else {
                    int cell = grid[row - 1][col - 1];
                    String symbol;

                    if      (cell == SHIP && !hidden) symbol = GREEN  + "[0]" + RESET;
                    else if (cell == HIT)             symbol = YELLOW    + "[#]" + RESET;
                    else if (cell == SUNK)            symbol = RED  + "[*]" + RESET;
                    else if (cell == MISS)            symbol = BLUE + "[x]" + RESET;
                    else                              symbol = "[ ]";

                    System.out.print(symbol + "");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public String getBoardAsString(boolean hidden) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row <= 10; row++) {
            for (int col = 0; col <= 10; col++) {

                if(row == 0 && col == 0)
                    sb.append("    ");
                else if(row == 0){
                    sb.append(String.format("%d  ", col));
                }
                else if(row != 0 && col == 0){
                    sb.append(String.format("%c  ", 'A' + row - 1));
                }
                else {
                    int cell = grid[row - 1][col - 1];
                    String symbol;

                    if (cell == SHIP && !hidden) symbol = GREEN + "[0]" + RESET;
                    else if (cell == HIT) symbol = YELLOW + "[#]" + RESET;
                    else if (cell == SUNK) symbol = RED + "[*]" + RESET;
                    else if (cell == MISS) symbol = BLUE + "[x]" + RESET;
                    else symbol = "[ ]";

                    sb.append(symbol + "");
                }
            }
            sb.append("\n");
        }
        sb.append("\n");

        return sb.toString();
    }

    public boolean placeShip(Ship ship, int row, int col, boolean horizontal) {
        int size = ship.getSize();

        if (horizontal && col + size > 10) return false;
        if (!horizontal && row + size > 10) return false;

        for (int k = 0; k < size; k++) {
            if (horizontal && grid[row][col + k] != EMPTY) return false;
            if (!horizontal && grid[row + k][col] != EMPTY) return false;
        }

        for (int k = 0; k < size; k++) {
            if (horizontal) {
                grid[row][col + k] = SHIP;
                ship.getCordinates().add(new Coordinates(col + k, row));
            }
            else {
                grid[row + k][col] = SHIP;
                ship.getCordinates().add(new Coordinates(col, row + k));
            }
        }

        return true;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

}























