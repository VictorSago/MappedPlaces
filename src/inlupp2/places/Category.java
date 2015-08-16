/**
 * Inlämningsuppgift 2 i PROG2: 
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2.places;

import java.awt.Color;
import java.io.Serializable;
import java.util.*;

/**
 * <code>public class Category<code><br>
 * <code>implements Serializable<code><br><br>
 * This class provides <code>Category<code> objects that a <code>Place<code> can be associated with.<br>
 * It stores a name and a color for a <code>Category<code> object.
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */
public class Category implements Serializable {

    private static final long serialVersionUID = -8968727471715296746L;

    /**
     * Used in calculating the color in which the objects associated with this category are to be displayed.
     */
    private static final int baseAlpha = 64;
    
    private String name;
    private Color color;

    /**
     * No nameless <code>Category<code> objects can be instantiated.
     */
    @SuppressWarnings("unused")
    private Category() { }
    
    /**
     * <code>public Category(String name)<code><br><br>
     * Creates a new category with the specified name and a random color.
     * @param name - Name of the category to be created
     */
    public Category(String name) {
	this.name = name;
	Random rnd = new Random(256);
	int r = rnd.nextInt(256);
	int b = rnd.nextInt(256);
	int g = rnd.nextInt(256);
	int alpha = rnd.nextInt(256 - baseAlpha);
	this.color = new Color(r, b, g, baseAlpha + alpha);
    }

    /**
     * <code>public Category(String name, Color col)<code><br><br>
     * Creates a new category with the specified name and color.
     * @param name - Name of the category to be created
     * @param col - Color of the category to be created
     */
    public Category(String name, Color col) {
	this.name = name;
	this.color = col;
    }

    /**
     * <code>public Category(String name, int r, int g, int b, int a)<code><br><br>
     * Creates a new category with the specified name and the RGBa color specified by integer parameters.
     * @param name - Name of the category to be created
     * @param r - The red component of the color of the new category 
     * @param g - The green component of the color of the new category
     * @param b - The blue component of the color of the new category
     * @param a - The alpha of the color of the new category
     */
    public Category(String name, int r, int g, int b, int a) {
	this.name = name;
	this.color = new Color(r, g, b, a);
    }

    /**
     * <code>public String getName()<code><br><br>
     * Returns this category's name
     * @return The name of this Category
     */
    public String getName() {
	return this.name;
    }

    /**
     * <code>public Color getColor()<code><br><br>
     * @return The color of this Category
     */
    public Color getColor() {
	return this.color;
    }

    /**
     * Two categories are equal if they have EITHER the same name OR the same color 
     * @return True if two categories are equal, false otherwise
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object cat) {
	if (this == cat) {
	    return true;
	}
	if (cat == null) {
	    return false;
	}
	if (!(cat instanceof Category)) {
	    return false;
	} else {
	    Category other = (Category) cat;
	    return (name.equalsIgnoreCase(other.getName()) || color.equals(other.getColor()));
	}
    }

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
