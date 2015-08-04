/**
 * 
 */
/**
 * @author Victor Sago
 *
 */

package inlupp2.places;

import inlupp2.resources.StringUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
//import javax.swing.JTextArea;
//import javax.swing.BorderFactory;
//import javax.swing.border.Border;
import java.util.ArrayList;

public class DescribedPlace extends Place {
    
    private static final long serialVersionUID = 4L;
    
    private String description;
    
    // Test 2015-07-27
    private ArrayList<String> descriptionSplit;
    
    private static final int UNFOLDED_WMULT 	= 5;
//    private static final int UNFOLDED_HMULT 	= 10;
    
    protected static final int HMARGIN = 4;
    protected static final int VMARGIN = 2;
    
    private static final Color UNFOLDED_BACKGROUND = new Color(1.0f, 1.0f, 0.0f, 0.75f);
    
    /**
     * private constructor to prevent instantiation without attributes
     */
    @SuppressWarnings("unused")
    private DescribedPlace() {
    }

    /**
     * @param name
     */
    @SuppressWarnings("unused")
    private DescribedPlace(String name) {
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
    }
        
    protected void unfold() {	
	int x0 = (int) (position.getX() - BASIC_WIDTH/2 - BORDER_THICKNESS);
	int y0 = (int) (position.getY() - BASIC_HEIGHT - BORDER_THICKNESS);
	
	int width = BASIC_WIDTH * UNFOLDED_WMULT + BORDER_THICKNESS*2;
	
	// Test 2015-07-27
	FontMetrics fm = this.getFontMetrics(getFont());
	descriptionSplit = new ArrayList<String>(StringUtils.wrap(description, fm, width - BORDER_THICKNESS*2 - HMARGIN*2));
	
	int height = fm.getHeight() * (descriptionSplit.size()+1) + BORDER_THICKNESS*2 + VMARGIN*2;
	
	setBounds(x0, y0, width, height);
	setPreferredSize(new Dimension(width, height));
	
	validate();
    }
    
    protected void paintComponent(Graphics g) {
	if (this.folded) {
	    super.paintComponent(g);
	} else {
	    this.setOpaque(true);
	    
	    g.setColor(UNFOLDED_BACKGROUND);
	    g.fillRect(BORDER_THICKNESS, BORDER_THICKNESS, this.getWidth()-BORDER_THICKNESS*2-1, this.getHeight()-BORDER_THICKNESS*2-1);
	    g.setColor(colOut);
	    
	    int fontHeight = g.getFontMetrics().getHeight();
	    g.drawString(getName(), BORDER_THICKNESS + HMARGIN*2, fontHeight + VMARGIN);
	    for (int i = 0; i < descriptionSplit.size(); i++) {
		g.drawString(descriptionSplit.get(i), BORDER_THICKNESS + HMARGIN, fontHeight*(i+2)+VMARGIN);
	    }
	    
	    if (this.selected) {
		this.setBorder(Place.selectedBorder);
	    } else {
		this.setBorder(Place.unselectedBorder);
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
	String ret = "Described: " + name + " " + position.toString();
	ret += " " + (category != null ? category.toString() : "Uncategorized");
	ret += folded ? " folded" : " unfolded";
	ret += selected ? " selected" : "";
	ret += isVisible() ? " visible" : "";
	return ret;
    }
}
