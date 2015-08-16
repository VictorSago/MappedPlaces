/**
 * Inlämningsuppgift 2 i PROG2: 
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2.places;

import inlupp2.MappedPlaces;
import java.awt.BorderLayout;
import javax.swing.*;

/**
 * <code>public class DescribedPlaceAddForm<code><br>
 * <code>extends PlaceAddForm<code><br><br>
 * An input form for adding objects of the class <code>DescribedPlace<code>.
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */
public class DescribedPlaceAddForm extends PlaceAddForm {

    private static final long serialVersionUID = 1L;
    
    JTextArea taPlaceDescription = new JTextArea(6, 16);
    
    public DescribedPlaceAddForm() {
	super();
	JPanel pnlMiddle = new JPanel();
	pnlMiddle.setLayout(new BorderLayout(2, 2));
	JLabel labelDescription = new JLabel(MappedPlaces.msgStrings.getString("dlgPlaceDescription"));
	pnlMiddle.add(labelDescription, BorderLayout.NORTH);
	taPlaceDescription.setLineWrap(true);
	taPlaceDescription.setWrapStyleWord(true);
	JScrollPane sp = new JScrollPane(taPlaceDescription, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
								JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	pnlMiddle.add(sp, BorderLayout.CENTER);
	labelDescription.setLabelFor(taPlaceDescription);
	add(pnlMiddle);
    }
    
    public String getDescription() {
	return taPlaceDescription.getText();
    }

}
