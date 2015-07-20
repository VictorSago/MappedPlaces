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

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class DescribedPlace extends Place {
    
    private static final long serialVersionUID = 4L;
    
    private String description;
    
    /**
     * private constructor to prevent instantiation without attributes
     */
    @SuppressWarnings("unused")
    private DescribedPlace() {
	// TODO
    }

    /**
     * @param name
     */
    @SuppressWarnings("unused")
    private DescribedPlace(String name) {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param name
     * @param pos
     */
    public DescribedPlace(String name, PlacePosition pos) {
	this(name, pos, null, "");
	// TODO Auto-generated constructor stub
    }

    /**
     * @param name
     * @param pos
     * @param cat
     */
    public DescribedPlace(String name, PlacePosition pos, PlaceCategory cat) {
	this(name, pos, cat, "");
	// TODO Auto-generated constructor stub
    }
    
    /**
     * @param name
     * @param pos
     * @param descr
     */
    public DescribedPlace(String name, PlacePosition pos, String descr) {
	super(name, pos, null);
	this.description = descr;
	// TODO Auto-generated constructor stub
    }
    
    /**
     * @param name
     * @param pos
     * @param cat
     * @param descr
     */
    public DescribedPlace(String name, PlacePosition pos, PlaceCategory cat, String descr) {
	super(name, pos, cat);
	this.description = descr;
	// TODO Auto-generated constructor stub
    }
    
    protected void setUnfoldedSize() {
	int x0 = (int) (position.getX() - triWidth/2);
	int y0 = (int) (position.getY() - triHight);
	int wi = triWidth * 4;
	int hi = triHight * 10;
	setBounds(x0, y0, wi, hi);
	Dimension d = new Dimension(wi, hi);
	setPreferredSize(d);
    }
    
    protected void paintComponent(Graphics g) {
	System.out.println("paintComponent method in DecribedPlace:");
	if (this.folded) {
	    System.out.println("This DecribedPlace is folded - delegating to Super");
	    super.paintComponent(g);
	} else {
	    System.out.println("This DecribedPlace is unfolded - painting here");
	    this.setUnfoldedSize();
	    Color colIn;
	    Color colOut;
	    if (category != null) {
		colOut = category.getColor();
	    } else {
		colOut = Color.DARK_GRAY;
	    }
//	    int newAlpha = (int) (colOut.getAlpha()*alphaRel);
//	    colIn = new Color(colOut.getRed(), colOut.getGreen(), colOut.getBlue(), newAlpha);
	    colIn = new Color(255, 255, 0, 160);
	    int x0 = this.getX();
	    int y0 = this.getY();
	    int wi = this.getWidth();
	    int hi = this.getHeight();
	    g.setColor(colIn);
	    g.fillRect(x0+2, y0+2, wi-4, hi-4);
	    g.setColor(colOut);
	    g.drawString(getName(), x0 + 8, y0+16);
	    g.drawString(description, x0 + 2, y0+32);
	    // Paint a border if this place is selected
	    Border selectedBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
	    Border unselectedBorder = BorderFactory.createEmptyBorder();
	    if (this.selected) {
		this.setBorder(selectedBorder);
	    } else {
		this.setBorder(unselectedBorder);
	    }
	}
    }
    
    public String getDescription() {
	return this.description;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!super.equals(other)) {
	    return false;
	}
	if (!(other instanceof DescribedPlace)) {
	    return false;
	}
	DescribedPlace otherplace = (DescribedPlace) other;
	if (description == null) {
	    if (otherplace.description != null) {
		return false;
	    }
	} else if (!description.equals(otherplace.description)) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean equalsIgnoreCategory(Object other) {
	if (this == other) {
	    return true;
	}
	if (other == null)
	    return false;
	if (!(other instanceof DescribedPlace)) {
	    return false;
	}
	DescribedPlace otherplace = (DescribedPlace) other;
	boolean ret1 = description.equals(otherplace.description);
	return ret1 && super.equalsIgnoreCategory(other);
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	return result;
    }

    @Override
    public String toString() {
	// TODO
	String ret = super.toString();
	ret += "\n" + description + "\n";
	return ret;
    }
    
//  @Override
//  public boolean equals(Object other) {
//	if (other == null)
//	    return false;
//	if (!(other instanceof DescribedPlace)) {
//	    return false;
//	} else {
//	    DescribedPlace otherplace = (DescribedPlace) other;
//	    boolean ret1 = name.equalsIgnoreCase(otherplace.getName()) 
//		    && position.equals(otherplace.getPosition())
//		    && description.equalsIgnoreCase(otherplace.getDescription());
//	    boolean ret2;
//	    if (this.category == null && otherplace.getCategory() == null) {
//		ret2 = true;
//	    } else if ((this.category == null && otherplace.getCategory() != null) ||
//		    (this.category != null && otherplace.getCategory() == null)) {
//		ret2 = false;
//	    } else {
//		ret2 = this.category.equals(otherplace.getCategory());
//	    }
//	    return ret1 && ret2;
//	}
//  }
    
}
