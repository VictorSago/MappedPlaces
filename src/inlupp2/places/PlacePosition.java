/**
 * 
 */
/**
 * @author Victor Sago
 *
 */

package inlupp2.places;

import java.io.Serializable;
//import java.util.List;

public class PlacePosition implements Serializable {
    
    private static final long serialVersionUID = 6L;
    
    private int xPos;
    private int yPos;

    /*
     * No PlacePosition instance without attributes
     */
    @SuppressWarnings("unused")
    private PlacePosition() {
	// TODO Auto-generated constructor stub
    }
    
    public PlacePosition(int x, int y) {
	this.xPos = x;
	this.yPos = y;
    }

    public PlacePosition(PlacePosition pos) {
	this.xPos = pos.getX();
	this.yPos = pos.getY();
    }

    public int getX() {
	return xPos;
    }

    public int getY() {
	return yPos;
    }
    
    /*
     * 
     * Move a position to new coordinates:
     * newX = x
     * newY = y
     */
//    public void move(int x, int y) {
//	// TODO
//    }
    
    /*
     * 
     * Move a position to new coordinates:
     * newX = oldX + dx
     * newY = oldY + dy
     */
//    public void translate(int dx, int dy) {
//	// TODO
//    }
    
    @Override
    public boolean equals(Object pos) {
	if (this == pos) {
	    return true;
	}
	if (pos == null) {
	    return false;
	}
	if (!(pos instanceof PlacePosition)) {
	    return false;
	} else {
	    PlacePosition other = (PlacePosition) pos;
	    return (xPos == other.xPos && yPos == other.yPos);
	}
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     * Standard Eclipse implementation of hashCode method
     */
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
