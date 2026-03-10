package gameObjects;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private String name;
    private int size;
    private boolean alive;
    private List<Coordinates> cordinates;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        alive = true;
        this.cordinates = new ArrayList<>();
    }

    public boolean isHit(Coordinates target){
        for(Coordinates c : cordinates){
            if(c.getX() == target.getX() && c.getY() == target.getY()){
                c.setHit(true);
                alive = cordinates.stream().anyMatch(coord -> !coord.isHit());
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public List<Coordinates> getCordinates() {
        return cordinates;
    }

    public void setCordinates(List<Coordinates> cordinates) {
        this.cordinates = cordinates;
    }
}
