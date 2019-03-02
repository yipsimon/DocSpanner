// The purpose of this class is to store two integers as a coordinate.

public class Coord {
    private int x;
    private int y;

    public Coord(int x, int y) {  //Constructor for storing two integers and labeling them as x and y.
        this.x = x;
        this.y = y;
    }
    
    public int getX() {     //Getting only the x coordinate
        return x;
    }

    public void setX(int x) {   //Setting a new value for the x coordinate
        this.x = x;
    }

    public int getY() {     //Getting only the y coordinate
        return y;
    }

    public void setY(int y) {   //Setting a new value for the y coordinate
        this.y = y;
    }

}
