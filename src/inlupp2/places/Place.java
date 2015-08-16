/**
 * Inlämningsuppgift 2 i PROG2: 
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2.places;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.border.*;

/**
 * <code>public abstract class Place<code><br>
 * <code>extends JComponent<code><br>
 * <code>implements Serializable<code><br><br>
 * This is a generic <code>Place<code> class.<br>
 * It represents an abstract Place object at a specified location and category, with a name and state expressed as
 * folded/unfolded and selected/unselected. The superclass provides visible/hidden state.
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */
public abstract class Place extends JComponent implements Serializable {

    private static final long serialVersionUID = -189874980451302955L;

    protected String	name;
    protected Position	position;
    protected Category	category;
    protected boolean	folded, selected;
    
    // Attributes needed for painting the component
    protected static final int BASIC_WIDTH = 16;		// The width of a Place component when displayed normally
    protected static final int BASIC_HEIGHT = 24;		// The height --''--
    protected static final float REL_ALPHA = 0.75f;		// The relationship between alpha value of the inner and outer color
    // Border thickness
    protected static final int BORDER_THICKNESS = 2;
    // The points of the triangle that represents this object on a map
    protected int[] xPoints, yPoints;
    // The colors of this object
    protected Color colOut, colIn;
    // The borders of this object when selected or unselected
    protected static Border selectedBorder = BorderFactory.createLineBorder(Color.RED, BORDER_THICKNESS);
    protected static Border unselectedBorder = BorderFactory.createEmptyBorder();

    /**
     * <code>protected Place()<code><br><br>
     * New objects of this class can't be instantiated without attributes
     * but an empty constructor is needed for declaration of Place objects.
     */
    protected Place() { }
    
    /**
     * <code>private Place(String name)<code><br><br>
     * Prevents instantiation of position-less places
     * @param name
     * 
     */
    @SuppressWarnings("unused")
    private Place(String name) { }

    /**
     * <code>public Place(String name, Position pos)<code><br><br>
     * Constructs a new uncategorized object of this class with specified name and at specified location.<br>
     * Delegates to the three-argument constructor with the last argument as <code>null<code>.
     * @param name - The name of the object to be constructed
     * @param pos - The location of the object to be constructed
     */
    public Place(String name, Position pos) {
	this(name, pos, null);
    }

    /**
     * <code>public Place(String name, Position pos, Category cat)<code><br><br>
     * Constructs a new object of this class with specified name and category and at specified location.<br>
     * Calculates all the attributes that are later used in the <code>paintComponent<code> method.
     * @param name - The name of the object to be constructed
     * @param pos - The location of the object to be constructed
     * @param cat - The category of the object to be constructed
     */
    public Place(String name, Position pos, Category cat) {
	this.name = name;
	this.position = pos;
	this.category = cat;
	this.selected = false;
	fold();						// Make new objects folded by default.
	if (category != null) {				// Outer color is the solid color of the category or dark gray
	    colOut = category.getColor();
	} else {
	    colOut = Color.DARK_GRAY;
	}
	// Inner color is the same as outer but translucent
	int newAlpha = (int) (colOut.getAlpha()*REL_ALPHA);
	colIn = new Color(colOut.getRed(), colOut.getGreen(), colOut.getBlue(), newAlpha);
	// The points of the triangle are defined in the following order:
	// 0 - the lower point, 1 - the upper left corner, 2 - the upper right corner
	xPoints = new int[3];
	yPoints = new int[3];
	xPoints[0] = BASIC_WIDTH/2 + BORDER_THICKNESS;
	yPoints[0] = BASIC_HEIGHT + BORDER_THICKNESS;
	xPoints[1] = BORDER_THICKNESS;
	yPoints[1] = BORDER_THICKNESS;
	xPoints[2] = BASIC_WIDTH + BORDER_THICKNESS;
	yPoints[2] = BORDER_THICKNESS;
    }
    
    /**
     * <code>protected void fold()<code><br><br>
     * Folds this place object.
     */
    protected void fold() {
	this.folded = true;
	int wi = BASIC_WIDTH + BORDER_THICKNESS*2;
	int hi = BASIC_HEIGHT + BORDER_THICKNESS*2;
	
	int x0 = position.getX() - BASIC_WIDTH/2 - BORDER_THICKNESS;
	int y0 = position.getY() - BASIC_HEIGHT - BORDER_THICKNESS;
	
	setBounds(x0, y0, wi, hi);
	setPreferredSize(new Dimension(wi, hi));
	
	validate();
    }
    
    /**
     * <code>protected abstract void unfold()<code><br><br>
     * Unfolds object of this class.<br>
     * Must be implemented in every subclass since each subclass would have a different form for its unfolded objects.
     */
    protected abstract void unfold();
    
    protected void paintComponent(Graphics g) {
	if (!this.isVisible()) {
	    return;
	}
	super.paintComponent(g);
	this.setOpaque(true);
	g.setColor(colIn);
	g.fillPolygon(xPoints, yPoints, 3);	
	g.setColor(colOut);
	g.drawPolygon(xPoints, yPoints, 3);
	g.setColor(Color.BLACK);
	g.drawPolygon(xPoints, yPoints, 3);
    }
  
    /**
     * @return The name of this object
     */
    public String getName() {
	return name;
    }

    /**
     * @return Location of this object
     */
    public Position getPosition() {
	return position;
    }
    
    /**
     * @return True if this object is associated with a category
     */
    public boolean hasCategory() {
	return this.category != null;
    }

    /**
     * @return The Category object associated with this Place object
     */
    public Category getCategory() {
	return category;
    }
    
    /**
     * @return True if this object is hidden
     * @see java.lang.JComponent#isVisible()
     * @see java.lang.JComponent#setVisible(boolean aFlag)
     */
    public boolean isHidden() {
	return this.isVisible() ? false : true; 
    }
    
    /**
     * <code>public void setHidden(boolean val)<code><br><br>
     * Sets this object hidden or visible depending on the argument.
     * If the argument is <code>true<code> then the object is made non-visible, 
     * if it's <code>false<code> the object is made visible.
     * @param val - The value to which the "hidden" attribute is to be set
     * @see java.lang.JComponent#setVisible(boolean aFlag)
     * @see java.lang.JComponent#isVisible()
     */
    public void setHidden(boolean val) {
	this.setVisible(!val);
    }

    /**
     * @return True if this object is folded
     */
    public boolean isFolded() {
	return folded;
    }

    /**
     * <code>public void setFolded(boolean val)<code><br><br>
     * Makes this object folded/unfolded depending on the argument.
     * @param val
     */
    public void setFolded(boolean val) {
	this.folded = val;
	if (folded)
	    this.fold();
	else
	    this.unfold();
    }

    /**
     * @return True if this Place object is selected
     */
    public boolean isSelected() {
	return selected;
    }

    /**
     * Sets this object selected/unselected depending on the argument.
     * @param val
     */
    public void setSelected(boolean val) {
	this.selected = val;
	if (val) {
	    this.setBorder(Place.selectedBorder);
	} else {
	    this.setBorder(Place.unselectedBorder);
	}
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (other == null)
	    return false;
	if (!(other instanceof Place)) {
	    return false;
	}
	Place otherplace = (Place) other;
	boolean ret1 = name.equalsIgnoreCase(otherplace.getName()) 
		&& position.equals(otherplace.getPosition());
	boolean ret2;
	if (this.category == null) {
	    if (otherplace.category == null) {
		ret2 = true;
	    } else {
		ret2 = false;
	    }
	} else {
	    if (otherplace.category == null) {
		ret2 = false;
	    } else {
		ret2 = this.category.equals(otherplace.getCategory());
	    }
	}
	return ret1 && ret2;
    }
    
    public boolean equalsIgnoreCategory(Object other) {
	if (this == other) {
	    return true;
	}
	if (other == null)
	    return false;
	if (!(other instanceof Place)) {
	    return false;
	} else {
	    Place otherplace = (Place) other;
	    return name.equalsIgnoreCase(otherplace.getName()) 
		    && position.equals(otherplace.getPosition());
	}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 7;
	result = prime * result + ((position == null) ? 0 : position.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((category == null) ? 0 : category.hashCode());
	return result;
    }

    @Override
    public String toString() {
	String ret = name + " " + position.toString();
	ret += (category != null ? " " + category.toString() : "");
	return ret;
    }

}
