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
    
    protected final int triWidth = 16;
    protected final int triHight = 24;
    protected final float alphaRel = 0.75f;
//    protected final float thickness = 2;
    private final int d = 1;
    
    protected String          name;
    protected PlacePosition   position;
    protected PlaceCategory   category;
    protected boolean         folded, selected;

    /**
     * New objects of this class can't be instantiated without attributes
     */
    Place() {
	// TODO Auto-generated constructor stub
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
	this.folded = true;
	this.selected = false;
//	int x0 = (int) (position.getX() - triWidth/2);
//	int y0 = (int) (position.getY() - triHight);
//	int wi = triWidth;
//	int hi = triHight;
//	setBounds(x0, y0, wi, hi);
//	Dimension d = new Dimension(wi, hi);
//	setPreferredSize(d);
	this.setFoldedSize();
    }
    
    protected void setFoldedSize() {
	int x0 = (int) (position.getX() - triWidth/2);
	int y0 = (int) (position.getY() - triHight);
	int wi = triWidth;
	int hi = triHight;
	setBounds(x0, y0, wi, hi);
	Dimension d = new Dimension(wi, hi);
	setPreferredSize(d);
    }
    
    abstract protected void setUnfoldedSize();
    
    protected void paintComponent(Graphics g) {
	if (!this.isVisible()) {
	    return;
	}
	if (this.category != null)
	    if (category.isHidden())
		return;
	super.paintComponent(g);
	this.setFoldedSize();
	this.setOpaque(true);
	Color colOut;
	Color colIn;
	// The points of the triangle are defined in the following order:
	// 1 - the lower point, 2 - the upper left corner, 3 - the upper right corner
	int[] xPoints = {triWidth/2, 0, triWidth};
	int[] yPoints = {triHight, 0, 0};
	int[] xiPoints = {xPoints[0], xPoints[1] + d, xPoints[2] - d};
	int[] yiPoints = {yPoints[0] - d, yPoints[1] + d, yPoints[2] + d};
	int[] xi2Points = {xPoints[0], xPoints[1] + d*2, xPoints[2] - d*2};
	int[] yi2Points = {yPoints[0] - d*2, yPoints[1] + d*2, yPoints[2] + d*2};
	if (category != null) {
	    colOut = category.getColor();
	} else {
	    colOut = Color.DARK_GRAY;
	}
	int newAlpha = (int) (colOut.getAlpha()*alphaRel);
	colIn = new Color(colOut.getRed(), colOut.getGreen(), colOut.getBlue(), newAlpha);
	
	g.setColor(colIn);
//	g.fillOval(0, 0, getWidth(), getHeight());
	g.fillPolygon(xPoints, yPoints, 3);	
	g.setColor(colOut);
	g.drawPolygon(xiPoints, yiPoints, 3);
	g.drawPolygon(xi2Points, yi2Points, 3);
	g.setColor(Color.BLACK);
	g.drawPolygon(xPoints, yPoints, 3);
	
	Border selectedBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
	Border unselectedBorder = BorderFactory.createEmptyBorder();
	if (this.selected) {
	    this.setBorder(selectedBorder);
	} else {
	    this.setBorder(unselectedBorder);
	}
    }

//    protected void paintComponent(Graphics g) {
//	System.out.print("a1 ");
//	if (this.category != null)
//	    if (category.isHidden())
//		return;
//	if (!this.isVisible()) {
//	    return;
//	}
//	super.paintComponent(g);
//	this.setFoldedSize();
//	System.out.print("a2 ");
//	Color colOut;
//	Color colIn;
//	// The points of the triangle are defined in the following order:
//	// 1 - the lower point, 2 - the upper left corner, 3 - the upper right corner
//	int[] xPoints = {triWidth/2, 0, triWidth};
//	int[] yPoints = {triHight, 0, 0};
//	int[] xiPoints = {xPoints[0], xPoints[1] + d, xPoints[2] - d};
//	int[] yiPoints = {yPoints[0] - d, yPoints[1] + d, yPoints[2] + d};
//	int[] xi2Points = {xPoints[0], xPoints[1] + d*2, xPoints[2] - d*2};
//	int[] yi2Points = {yPoints[0] - d*2, yPoints[1] + d*2, yPoints[2] + d*2};
//	if (category != null) {
//	    colOut = category.getColor();
//        } else {
//            colOut = Color.DARK_GRAY;
//        }
//	int newAlpha = (int) (colOut.getAlpha()*alphaRel);
//	colIn = new Color(colOut.getRed(), colOut.getGreen(), colOut.getBlue(), newAlpha);
////	colIn = Color.WHITE;
//
//	g.setColor(colIn);	
//	g.fillPolygon(xPoints, yPoints, 3);	
//	g.setColor(colOut);
//	g.drawPolygon(xiPoints, yiPoints, 3);
//	g.drawPolygon(xi2Points, yi2Points, 3);
//	g.setColor(Color.BLACK);
//	g.drawPolygon(xPoints, yPoints, 3);
//	
//	System.out.print("a3 ");
//	
//	// Paint a border if this place is selected
//	Border selectedBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
//	Border unselectedBorder = BorderFactory.createEmptyBorder();
//	if (this.selected) {
//	    System.out.print("a4a ");
//	    this.setBorder(selectedBorder);
//	} else {
//	    System.out.print("a4b ");
//	    this.setBorder(unselectedBorder);
//	}
//    }
    
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
	ret += " " + (category != null ? category.toString() : "Uncategorized ");
	ret += folded ? " folded" : "unfolded";
	ret += selected ? " selected" : "";
	ret += isVisible() ? " visible" : "";
	return ret;
    }

//  /* (non-Javadoc)
//  * @see java.lang.Object#equals(java.lang.Object)
//  */
// @Override
// public boolean equals(Object obj) {
//	if (this == obj) {
//	    return true;
//	}
//	if (obj == null) {
//	    return false;
//	}
//	if (!(obj instanceof Place)) {
//	    return false;
//	}
//	Place other = (Place) obj;
//	if (category == null) {
//	    if (other.category != null) {
//		return false;
//	    }
//	} else if (!category.equals(other.category)) {
//	    return false;
//	}
//	if (name == null) {
//	    if (other.name != null) {
//		return false;
//	    }
//	} else if (!name.equals(other.name)) {
//	    return false;
//	}
//	if (position == null) {
//	    if (other.position != null) {
//		return false;
//	    }
//	} else if (!position.equals(other.position)) {
//	    return false;
//	}
//	return true;
// }

}
