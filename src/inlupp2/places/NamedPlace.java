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

//import javax.swing.BorderFactory;
//import javax.swing.border.Border;

public class NamedPlace extends Place {
    
    private static final long serialVersionUID = 5L;
    
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
    
    protected void setUnfoldedSize() {
//	NamedPlace np = (NamedPlace) p;
	int x0 = (int) (position.getX() - triWidth/2);
	int y0 = (int) (position.getY() - triHight);
	int wi = triWidth * 5;
	int hi = triHight * 2;
	setBounds(x0, y0, wi, hi);
	Dimension d = new Dimension(wi, hi);
	setPreferredSize(d);
    }
    
    protected void paintComponent(Graphics g) {
	if (this.folded) {
	    super.paintComponent(g);
	} else {
	    this.setOpaque(true);
	    setUnfoldedSize();
	    Color col;
	    if (category != null) {
		col = category.getColor();
	    } else {
		col = Color.DARK_GRAY;
	    }
	    g.setColor(col);
	    g.drawRect(0+1, 0+1, this.getWidth()-3, this.getHeight()-3);
//	    g.setColor(Color.BLACK);
	    g.drawString(getName(), 0 + 4, (getHeight()-4)/2);
//	    Border selectedBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
//	    Border unselectedBorder = BorderFactory.createEmptyBorder();
	    if (this.selected) {
		this.setBorder(Place.selectedBorder);
	    } else {
		this.setBorder(Place.unselectedBorder);
	    }
	}
    }
    
//    protected void paintComponent(Graphics g) {
//	System.out.print("b1 ");
//	if (this.folded) {
//	    System.out.print("b1a ");
//	    super.paintComponent(g);
//	} else {
//	    System.out.print("b1b ");
//	    this.setUnfoldedSize();
//	    Color colOut;
//	    if (category != null) {
//		colOut = category.getColor();
//	    } else {
//		colOut = Color.DARK_GRAY;
//	    }
//	    int x0 = this.getX();
//	    int y0 = this.getY();
//	    int wi = this.getWidth();
//	    int hi = this.getHeight();
//	    g.setColor(colOut);
//	    g.drawRect(x0+2, y0+2, wi-4, hi-4);
//	    g.drawString(getName(), x0 + 8, (hi-4)/2);
//	    System.out.print("b2 ");
//	    // Paint a border if this place is selected
//	    Border selectedBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
//	    Border unselectedBorder = BorderFactory.createEmptyBorder();
//	    if (this.selected) {
//		System.out.print("b3a ");
//		this.setBorder(selectedBorder);
//	    } else {
//		System.out.print("b3b ");
//		this.setBorder(unselectedBorder);
//	    }
//	}
//    }
    
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
	// TODO
	return super.toString();
    }

}
