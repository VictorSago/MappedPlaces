/**
 * Inlämningsuppgift 2 i PROG2: 
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2.places;

import inlupp2.resources.StringHelper;

import java.awt.*;
import java.util.ArrayList;
/**
 * <code>public class DescribedPlace<code><br>
 * <code>extends Place<code><br><br>
 * This class describes <code>DescribedPlace<code> objects.<br>
 * It is an extension of the Place class. It adds a description attribute.
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */
public class DescribedPlace extends Place {

    private static final long serialVersionUID = 3817375932283600404L;

    private String description;
    
    // Description split into lines to facilitate painting this object in unfolded state
    private ArrayList<String> descriptionSplit;
    
    // Multiplication factor for unfolded state
    //   Basic width of a folded component is multiplied 
    // 		by this factors to get standard unfolded width
    private static final int UNFOLDED_WMULT 	= 5;
    
    protected static final int HMARGIN = 4;		// Horizontal and vertical margins between text and outer edges
    protected static final int VMARGIN = 2;		// 	in the unfolded state
    
    // The background color in the unfolded state
    private static final Color UNFOLDED_BACKGROUND = new Color(1.0f, 1.0f, 0.0f, 0.75f);

    /**
     * <code>protected DescribedPlace(String name, Position pos)<code><br><br>
     * Constructs a new uncategorized object of this class with specified name and at specified location.<br>
     * Delegates to the 4-argument constructor with the third argument as <code>null<code>
     *  and the last argument as an empty string.
     * @param name - The name of the object to be constructed
     * @param pos - The location of the object to be constructed
     */
    protected DescribedPlace(String name, Position pos) {
	this(name, pos, null, "");
    }

    /**
     * <code>protected DescribedPlace(String name, Position pos, Category cat)<code><br><br>
     * Constructs a new object of this class with specified name and category and at specified location.<br>
     * Delegates to the 4-argument constructor with the last argument as an empty string.
     * @param name - The name of the object to be constructed
     * @param pos - The location of the object to be constructed
     * @param cat - The location of the object to be constructed
     */
    protected DescribedPlace(String name, Position pos, Category cat) {
	this(name, pos, cat, "");
    }
    
    /**
     * <code>protected DescribedPlace(String name, Position pos, Category cat)<code><br><br>
     * Constructs a new object of this class with specified name and at specified location.<br>
     * @param name - The name of the object to be constructed
     * @param pos - The location of the object to be constructed
     * @param descr - The description of the new object
     */
    protected DescribedPlace(String name, Position pos, String descr) {
	super(name, pos, null);
	this.description = descr;
    }
    
    /**
     * <code>public DescribedPlace(String name, Position pos, Category cat, String descr)<code><br><br>
     * Constructs a new object of this class with specified name, category and description at specified location.<br>
     * @param name - The name of the object to be constructed
     * @param pos - The location of the object to be constructed
     * @param cat - The location of the object to be constructed
     * @param descr - The description of the new object
     */
    public DescribedPlace(String name, Position pos, Category cat, String descr) {
	super(name, pos, cat);
	this.description = descr;
    }
        
    protected void unfold() {	
	int x0 = position.getX() - BASIC_WIDTH/2 - BORDER_THICKNESS;
	int y0 = position.getY() - BASIC_HEIGHT - BORDER_THICKNESS;
	
	int wi = BASIC_WIDTH * UNFOLDED_WMULT + BORDER_THICKNESS*2;
	
	FontMetrics fm = this.getFontMetrics(getFont());
	// Split description into lines
	descriptionSplit = new ArrayList<String>(StringHelper.wrap(description, fm, wi - BORDER_THICKNESS*2 - HMARGIN*2));
	
	int hi = fm.getHeight() * (descriptionSplit.size()+1) + BORDER_THICKNESS*2 + VMARGIN*2;
	
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
	    
	    g.setColor(UNFOLDED_BACKGROUND);
	    g.fillRect(BORDER_THICKNESS, BORDER_THICKNESS, 
		    this.getWidth()-BORDER_THICKNESS*2-1, this.getHeight()-BORDER_THICKNESS*2-1);
	    g.setColor(colOut);
	    
	    int fontHeight = g.getFontMetrics().getHeight();
	    g.drawString(getName(), BORDER_THICKNESS + HMARGIN*2, fontHeight + VMARGIN);
	    for (int i = 0; i < descriptionSplit.size(); i++) {
		g.drawString(descriptionSplit.get(i), BORDER_THICKNESS + HMARGIN, fontHeight*(i+2)+VMARGIN);
	    }
	}
    }

    /**
     * @return The description of this Place object
     */
    public String getDescription() {
	return this.description;
    }

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

    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((description == null) ? -prime : description.hashCode());
	return result;
    }

    @Override
    public String toString() {
	String ret = name + " " + position.toString();
	ret += (category != null ? " " + category.toString() : "");
	ret += ": \"" + description.substring(0, 10) + "...\"";
	return ret;
    }
    
}
