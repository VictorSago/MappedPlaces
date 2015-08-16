/**
 * Inlämningsuppgift 2 i PROG2: 
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2.places;

import java.io.Serializable;

/**
 * <code>public class Position<code><br>
 * <code>implements Serializable<code><br><br>
 * This class provides <code>Position<code> objects to keep track of places' positions.<br>
 * It represents a location in (x,y) coordinate space, specified in integer precision.
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */
public class Position implements Serializable {

    private static final long serialVersionUID = 4060227657766138778L;
    
    private int xPos;
    private int yPos;

    /**
     * <code>private Position()<code><br><br>
     * This empty constructor is made <code>private<code> to prevent instantiation of a <code>Position<code> 
     *  object without any attributes.
     * No Position instance without attributes
     */
    @SuppressWarnings("unused")
    private Position() { }
    
    /**
     * <code>public Position(int x, int y)<code><br><br>
     * Constructs and initializes a position at the specified (x,y) location in the coordinate space.
     * @param x - the X coordinate of the newly constructed Position
     * @param y - the Y coordinate of the newly constructed Position
     */
    public Position(int x, int y) {
	this.xPos = x;
	this.yPos = y;
    }

    /**
     * <code>public Position(Position pos)<code><br><br>
     * Constructs and initializes a position with the same location as the specified <code>Position<code> object.
     * @param pos - a position
     */
    public Position(Position pos) {
	this.xPos = pos.getX();
	this.yPos = pos.getY();
    }

    /**
     * Returns the X coordinate of this <code>Position<code>.
     * @return The X coordinate of this Position
     */
    public int getX() {
	return xPos;
    }

    /**
     * Returns the Y coordinate of this <code>Position<code>.
     * @return The Y coordinate of this Position
     */
    public int getY() {
	return yPos;
    }
    
    @Override
    public boolean equals(Object pos) {
	if (this == pos) {
	    return true;
	}
	if (pos == null) {
	    return false;
	}
	if (!(pos instanceof Position)) {
	    return false;
	} else {
	    Position other = (Position) pos;
	    return (xPos == other.xPos && yPos == other.yPos);
	}
    }
    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 11;
	result = prime * result + xPos;
	result = prime * result + yPos;
	return result;
    }

    @Override
    public String toString() {
	return "(" + xPos + ", " + yPos + ")";
    }
}
