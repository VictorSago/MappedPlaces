/**
 * Inlämningsuppgift 2 i PROG2: 
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2.places;

import inlupp2.*;
import javax.swing.*;

/**
 * <code>public abstract class PlaceAddForm<code><br>
 * <code>extends JPanel<code><br><br>
 * A generic input form for adding objects of the class <code>Place<code>.
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */
public abstract class PlaceAddForm extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private JTextField tfPlaceName = new JTextField(12);

    PlaceAddForm() {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	JPanel pnlTop = new JPanel();
	JLabel labelName = new JLabel(MappedPlaces.msgStrings.getString("dlgPlaceName"));
	pnlTop.add(labelName);
	pnlTop.add(tfPlaceName);
	labelName.setLabelFor(tfPlaceName);
	add(pnlTop);
    }

    public String getName() {
	return tfPlaceName.getText();
    }
    
}
