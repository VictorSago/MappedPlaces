/**
 * Inlämningsuppgift 2 i PROG2: 
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2.places;

import java.awt.*;
/**
 * <code>public class NamedPlace<code><br>
 * <code>extends Place<code><br><br>
 * This class describes <code>NamedPlace<code> objects.<br>
 * It is an extension of the Place class.
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */
public class NamedPlace extends Place {

    private static final long serialVersionUID = -5939991734440041295L;
    
    // Multiplication factors for unfolded state
    //   Basic width and height of a folded component are multiplied 
    // 		by these factors to get standard unfolded size
    private static final int UNFOLDED_WMULT 	= 5;
    private static final int UNFOLDED_HMULT 	= 2;
    
    protected static final int HMARGIN = 4;			// Horizontal margin between text and outer edges
    
    /**
     * <code>public NamedPlace(String name, Position pos)<code><br><br>
     * Constructs a new uncategorized object of this class with specified name and at specified location.<br>
     * Delegates to the three-argument constructor with the last argument as <code>null<code>.
     * @param name - The name of the object to be constructed
     * @param pos - The location of the object to be constructed
     */
    public NamedPlace(String name, Position pos) {
	this(name, pos, null);
    }

    /**
     * <code>public NamedPlace(String name, Position pos, Category cat)<code><br><br>
     * Constructs a new object of this class with specified name and category and at specified location.<br>
     * Delegates to the three-argument constructor of the super class.
     * @param name - The name of the object to be constructed
     * @param pos - The location of the object to be constructed
     * @param cat - The category of the object to be constructed
     */
    public NamedPlace(String name, Position pos, Category cat) {
	super(name, pos, cat);
    }
    
    /**
     * <code>protected void unfold()<code><br><br>
     * Unfolds object of this class.<br>
     * Shows this NamedPlace unfolded as a rectangle with rounded corners
     * and with place name written in the middle in the color of the corresponding category.
     * @see inlupp2.places.Place#unfold()
     */
    @Override
    protected void unfold() {
	FontMetrics fm = this.getFontMetrics(getFont());
	int wi1 = BASIC_WIDTH * UNFOLDED_WMULT + BORDER_THICKNESS*2;
	int wi2 = fm.stringWidth(name) + BORDER_THICKNESS*2;
	// If the standard width is too small make it as wide as needed to accommodate this object's name
	int wi = Math.max(wi1, wi2);
	int hi = BASIC_HEIGHT * UNFOLDED_HMULT + BORDER_THICKNESS*2;
	int x0 = position.getX() - BASIC_WIDTH/2 - BORDER_THICKNESS;
	int y0 = position.getY() - BASIC_HEIGHT - BORDER_THICKNESS;
	
	// If this object's borders are outside the map's borders 
	// 	move the left upper corner so that the object fits fully within the map.
	Dimension dim = this.getParent().getSize();
	if (x0 < 0) {
	    x0 = 0;
	} else if (x0 + wi > dim.width) {
	    x0 = dim.width - wi;
	}
	if (y0 < 0) {
	    y0 = 0;
	} else if (y0 + hi > dim.height) {
	    y0 = dim.height - hi;
	}
	
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
	    g.drawRoundRect(BORDER_THICKNESS, BORDER_THICKNESS, 
		    this.getWidth()-BORDER_THICKNESS*2-1, this.getHeight()-BORDER_THICKNESS*2-1, 20, 20);
	    g.drawString(getName(), BORDER_THICKNESS + HMARGIN, (this.getHeight() - fm.getHeight()) / 2 + fm.getAscent());
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
	String ret = name + " " + position.toString();
	ret += (category != null ? " " + category.toString() : "");
	return ret;
    }

}
