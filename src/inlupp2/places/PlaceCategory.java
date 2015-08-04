/**
 * 
 */
/**
 * @author Victor Sago
 *
 */

package inlupp2.places;

import java.awt.Color;
import java.io.Serializable;
import java.util.*;

public class PlaceCategory implements Serializable {
    
    private static final long serialVersionUID = 3L;
    
    private static final int baseAlpha = 64;
    
    private String name;
    private Color color;
    private boolean hidden;

    /**
     * 
     * @element-type Place
     */
//    public List placeList;

    /**
     * No nameless categories can be instantiated
     */
    @SuppressWarnings("unused")
    private PlaceCategory() {
	// TODO Auto-generated constructor stub
    }
    
    public PlaceCategory(String name) {
	this.name = name;
	Random rnd = new Random(256);
	int r = rnd.nextInt(256);
	int b = rnd.nextInt(256);
	int g = rnd.nextInt(256);
	int alpha = rnd.nextInt(256 - baseAlpha);
	this.color = new Color(r, b, g, baseAlpha + alpha);
    }

    public PlaceCategory(String name, Color col) {
	this.name = name;
	this.color = col;
    }

    public PlaceCategory(String name, int r, int b, int g, int a) {
	this.name = name;
	this.color = new Color(r, g, b, a);
    }

    public String getName() {
	return this.name;
    }

    public Color getColor() {
	return this.color;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
	return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
	this.hidden = hidden;
    }

    @Override
    public boolean equals(Object cat) {
	if (this == cat) {
	    return true;
	}
	if (cat == null) {
	    return false;
	}
	if (!(cat instanceof PlaceCategory)) {
	    return false;
	} else {
	    PlaceCategory other = (PlaceCategory) cat;
	    return (name.equalsIgnoreCase(other.getName()) && color.equals(other.getColor()));
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
	int result = 13;
	result = prime * result + ((color == null) ? 0 : color.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }
    
    @Override
    public String toString() {
	// TODO
	String ret = this.name;
	int r = color.getRed();
	int g = color.getGreen();
	int b = color.getBlue();
	ret += " (" + r + ", " + g + ", " + b;
	if (color.getTransparency() == Color.TRANSLUCENT) {
	    int a = color.getAlpha();
	    ret += ", " + a;
	}
	ret += ")";
	return ret;
    }
}
