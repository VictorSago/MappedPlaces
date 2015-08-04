/**
 * 
 */
package inlupp2.places;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
//import java.awt.Dimension;
//import java.awt.Font;
import java.awt.Graphics;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
//import javax.swing.JLabel;
import javax.swing.JList;

/**
 * @author zeron
 *
 */
@SuppressWarnings("serial")
public class CategoryListRenderer extends DefaultListCellRenderer {

    private static final int STANDARD_WIDTH = 32;
    private static final int STANDARD_HEIGHT = 16;
    
    /**
     * @author zeron
     *
     */
    class CategoryIcon implements Icon {
	
	int height, width;
	Color color;

	/**
	 * 
	 */
	public CategoryIcon() {
	    // TODO Auto-generated constructor stub
	    width = STANDARD_WIDTH;
	    height = STANDARD_HEIGHT;	    
	}

	public CategoryIcon(int w, int h) {
	    // TODO Auto-generated constructor stub
	    width = w;
	    height = h;	    
	}
	
	public CategoryIcon(Color col) {
	    // TODO Auto-generated constructor stub
	    color = col;
	    width = 32;
	    height = 16;	    
	}
	
	public CategoryIcon(Color col, int w, int h) {
	    // TODO Auto-generated constructor stub
	    color = col;
	    width = w;
	    height = h;	    
	}
	
	public CategoryIcon(Color col, Dimension size) {
	    // TODO Auto-generated constructor stub
	    color = col;
	    width = size.width;
	    height = size.height;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
	    // TODO Auto-generated method stub
	    g.setColor(color);
	    g.fillRect(0, 1, width, height);
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth() {
	    // TODO Auto-generated method stub
	    return width;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight() {
	    // TODO Auto-generated method stub
	    return height;
	}

    }

    /**
     * 
     */
    public CategoryListRenderer() {
	// TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
	    					boolean isSelected, boolean cellHasFocus) {
	super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	PlaceCategory cat = (PlaceCategory)value;
	
	setText(cat.getName());
	if (cat.isHidden()) {
	    setForeground(Color.GRAY);
	}
	Color col = cat.getColor();
	int h = getFont().getSize();
	
	CategoryIcon catIcon = new CategoryIcon(col, 2 * (h + 2), h + 2);
	setIcon(catIcon);
	
	return this;
    }
}
