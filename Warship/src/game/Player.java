package game;

import gameObjects.Board;
import gameObjects.Ship;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private List<Ship> ships;
    private Board board;

    public Player(String name) {
        this.name = name;
        this.ships = new ArrayList<Ship>();
        this.board = new Board();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
