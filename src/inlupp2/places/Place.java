/**
 * 
 */
/**
 * @author Victor Sago
 *
 */

package inlupp2.places;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.BasicStroke;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

public abstract class Place extends JComponent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    protected String          name;
    protected PlacePosition   position;
    protected PlaceCategory   category;
    protected boolean         folded, selected;
    
    // Attributes needed for painting the component
    protected static final int BASIC_WIDTH = 16;
    protected static final int BASIC_HEIGHT = 24;
    protected static final float REL_ALPHA = 0.75f;
    // Border thickness
    protected static final int BORDER_THICKNESS = 2;
    // Size difference between inner and outer triangle
    protected static final int INNER_DISTANCE = 1;
    
    protected int[] xPoints, yPoints, xiPoints, yiPoints, xi2Points, yi2Points;
    
    protected Color colOut, colIn;
    
    protected static Border selectedBorder = BorderFactory.createLineBorder(Color.GRAY, BORDER_THICKNESS);
    protected static Border unselectedBorder = BorderFactory.createEmptyBorder();

    /**
     * New objects of this class can't be instantiated without attributes
     */
    Place() {
    }
    
    /**
     * @param name
     * Prevent instantiation of position-less places
     */
    @SuppressWarnings("unused")
    private Place(String name) {
    }

    public Place(String name, PlacePosition pos) {
	this(name, pos, null);
    }

    public Place(String name, PlacePosition pos, PlaceCategory cat) {
	this.name = name;
	this.position = pos;
	this.category = cat;
	this.selected = false;
	fold();
	if (category != null) {
	    colOut = category.getColor();
	} else {
	    colOut = Color.DARK_GRAY;
	}
	int newAlpha = (int) (colOut.getAlpha()*REL_ALPHA);
	colIn = new Color(colOut.getRed(), colOut.getGreen(), colOut.getBlue(), newAlpha);
	// The points of the triangle are defined in the following order:
	// 1 - the lower point, 2 - the upper left corner, 3 - the upper right corner
	xPoints = new int[3];
	yPoints = new int[3];
	xPoints[0] = BASIC_WIDTH/2 + BORDER_THICKNESS;
	yPoints[0] = BASIC_HEIGHT + BORDER_THICKNESS;
	xPoints[1] = BORDER_THICKNESS;
	yPoints[1] = BORDER_THICKNESS;
	xPoints[2] = BASIC_WIDTH + BORDER_THICKNESS;
	yPoints[2] = BORDER_THICKNESS;
	xiPoints = new int[3];
	yiPoints = new int[3];
	xiPoints[0] = xPoints[0];
	yiPoints[0] = yPoints[0] - INNER_DISTANCE;
	xiPoints[1] = xPoints[1] + INNER_DISTANCE;
	yiPoints[1] = yPoints[1] + INNER_DISTANCE;
	xiPoints[2] = xPoints[2] - INNER_DISTANCE;
	yiPoints[2] = yPoints[2] + INNER_DISTANCE;
	xi2Points = new int[3];
	yi2Points = new int[3];
	xi2Points[0] = xPoints[0];
	yi2Points[0] = yPoints[0] - INNER_DISTANCE*2;
	xi2Points[1] = xPoints[1] + INNER_DISTANCE*2;
	yi2Points[1] = yPoints[1] + INNER_DISTANCE*2;
	xi2Points[2] = xPoints[2] - INNER_DISTANCE*2;
	yi2Points[2] = yPoints[2] + INNER_DISTANCE*2;
    }
    
    protected void fold() {
	this.folded = true;
	int wi = BASIC_WIDTH + BORDER_THICKNESS*2;
	int hi = BASIC_HEIGHT + BORDER_THICKNESS*2;
	
	int x0 = (int) (position.getX() - BASIC_WIDTH/2 - BORDER_THICKNESS);
	int y0 = (int) (position.getY() - BASIC_HEIGHT - BORDER_THICKNESS);
	
	setBounds(x0, y0, wi, hi);
	setPreferredSize(new Dimension(wi, hi));
	
	validate();
    }
    
    protected abstract void unfold();
    
    protected void paintComponent(Graphics g) {
	if (!this.isVisible()) {
	    return;
	}
	if (this.category != null)
	    if (category.isHidden())
		return;
	super.paintComponent(g);
	this.setOpaque(true);
	g.setColor(colIn);
	g.fillPolygon(xPoints, yPoints, 3);	
	g.setColor(colOut);
	g.drawPolygon(xiPoints, yiPoints, 3);
	g.drawPolygon(xi2Points, yi2Points, 3);
	g.setColor(Color.BLACK);
	g.drawPolygon(xPoints, yPoints, 3);

	if (this.selected) {
	    this.setBorder(Place.selectedBorder);
	} else {
	    this.setBorder(Place.unselectedBorder);
	}
    }
  
    public String getName() {
	return name;
    }

    public PlacePosition getPosition() {
	return position;
    }
    
    public boolean hasCategory() {
	return this.category != null;
    }

    public PlaceCategory getCategory() {
	return category;
    }

    public boolean isFolded() {
	return folded;
    }

    public void setFolded(boolean val) {
	this.folded = val;
	if (folded)
	    this.fold();
	else
	    this.unfold();
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean val) {
	this.selected = val;
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

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     * Standard Eclipse implementation of hashCode method
     */
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
	// TODO
	String ret = name + " " + position.toString();
	ret += " " + (category != null ? category.toString() : "Uncategorized");
	ret += folded ? " folded" : " unfolded";
	ret += selected ? " selected" : "";
	ret += isVisible() ? " visible" : "";
	return ret;
    }
}
