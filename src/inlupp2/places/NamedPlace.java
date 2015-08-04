/**
 * 
 */
/**
 * @author Victor Sago
 *
 */

package inlupp2.places;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class NamedPlace extends Place {
    
    private static final long serialVersionUID = 5L;
    
    private static final int UNFOLDED_WMULT 	= 5;
    private static final int UNFOLDED_HMULT 	= 2;
    
    protected static final int HMARGIN = 4;
    protected static final int VMARGIN = 4;
    
    /**
     * private constructor to prevent instantiation without attributes
     */
    @SuppressWarnings("unused")
    private NamedPlace() {
    }

    /**
     * @param name
     */
    @SuppressWarnings("unused")
    private NamedPlace(String name) {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param name
     * @param pos
     */
    public NamedPlace(String name, PlacePosition pos) {
	this(name, pos, null);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param name
     * @param pos
     * @param cat
     */
    public NamedPlace(String name, PlacePosition pos, PlaceCategory cat) {
	super(name, pos, cat);
	// TODO Auto-generated constructor stub
    }
    
    protected void unfold() {
	int wi = BASIC_WIDTH * UNFOLDED_WMULT + BORDER_THICKNESS*2;
	int hi = BASIC_HEIGHT * UNFOLDED_HMULT + BORDER_THICKNESS*2;
	int x0 = (int) (position.getX() - BASIC_WIDTH/2 - BORDER_THICKNESS);
	int y0 = (int) (position.getY() - BASIC_HEIGHT - BORDER_THICKNESS);
	
	setBounds(x0, y0, wi, hi);
	setPreferredSize(new Dimension(wi, hi));
	
	validate();
    }
    
    protected void paintComponent(Graphics g) {
	if (this.folded) {
	    super.paintComponent(g);
	} else {
	    this.setOpaque(true);
	    unfold();
	    FontMetrics fm = g.getFontMetrics();
	    g.setColor(colOut);
	    g.drawRoundRect(BORDER_THICKNESS, BORDER_THICKNESS, this.getWidth()-BORDER_THICKNESS*2-1, this.getHeight()-BORDER_THICKNESS*2-1, 20, 20);
	    g.drawString(getName(), BORDER_THICKNESS + HMARGIN, (this.getHeight() - fm.getHeight()) / 2 + fm.getAscent());
	    if (this.selected) {
		this.setBorder(Place.selectedBorder);
	    } else {
		this.setBorder(Place.unselectedBorder);
	    }
	}
    }
  
    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!super.equals(other)) {
	    return false;
	}
	if (!(other instanceof NamedPlace)) {
	    return false;
	}
	NamedPlace otherplace = (NamedPlace) other;
	return super.equals(otherplace);
    }

    @Override
    public boolean equalsIgnoreCategory(Object other) {
	if (this == other) {
	    return true;
	}
	if (other == null)
	    return false;
	if (!(other instanceof NamedPlace)) {
	    return false;
	}
	NamedPlace otherplace = (NamedPlace) other;
	return super.equalsIgnoreCategory(otherplace);
    }


    @Override
    public int hashCode() {
	return super.hashCode();
    }

    @Override
    public String toString() {
	String ret = "Named: " + name + " " + position.toString();
	ret += " " + (category != null ? category.toString() : "Uncategorized");
	ret += folded ? " folded" : " unfolded";
	ret += selected ? " selected" : "";
	ret += isVisible() ? " visible" : "";
	return ret;
    }

}
