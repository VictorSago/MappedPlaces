/**
 * Inlämningsuppgift 2 i PROG2: 
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2.places;

import java.awt.*;
import javax.swing.*;

/**
 * <code>public class CategoryListRenderer<code><br>
 * <code>extends DefaultListCellRenderer<code><br><br>
 * This class allows rendering of <code>Category<code> objects in a <code>JList<code> component.<br>
 * @see javax.swing.DefaultListCellRenderer
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */
public class CategoryListRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 3669451354353408636L;
    
    // Standard width and height of a category icon as it appears in a JList
    private static final int STANDARD_WIDTH = 32;
    private static final int STANDARD_HEIGHT = 16;
    
    class CategoryIcon implements Icon {
	int height, width;
	Color color;

	public CategoryIcon() {
	    width = STANDARD_WIDTH;
	    height = STANDARD_HEIGHT;	    
	}

	public CategoryIcon(int w, int h) {
	    width = w;
	    height = h;	    
	}
	
	public CategoryIcon(Color col) {
	    color = col;
	    width = STANDARD_WIDTH;
	    height = STANDARD_HEIGHT;	    
	}
	
	public CategoryIcon(Color col, int w, int h) {
	    color = col;
	    width = w;
	    height = h;	    
	}
	
	public CategoryIcon(Color col, Dimension size) {
	    color = col;
	    width = size.width;
	    height = size.height;
	}
	
	/**
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
	    g.setColor(color);
	    g.fillRect(0, 1, width, height);
	}

	/**
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth() {
	    return width;
	}

	/**
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight() {
	    return height;
	}

    }

    public CategoryListRenderer() {
	super();
    }

    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
	    					boolean isSelected, boolean cellHasFocus) {
	super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	Category cat = (Category)value;
	
	setText(cat.getName());
	Color col = cat.getColor();
	int h = getFont().getSize();
	
	CategoryIcon catIcon = new CategoryIcon(col, 2 * (h + 2), h + 2);
	setIcon(catIcon);
	
	return this;
    }

}
