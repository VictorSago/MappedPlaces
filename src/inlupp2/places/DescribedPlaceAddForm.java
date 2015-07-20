package inlupp2.places;

import java.awt.BorderLayout;
import inlupp2.MappedPlaces;
//import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DescribedPlaceAddForm extends PlaceAddForm {

    JTextArea taPlaceDescription = new JTextArea(6, 16);
    
    public DescribedPlaceAddForm() {
	super();
	JPanel pnlMiddle = new JPanel();
//	pnlMiddle.setLayout(new BoxLayout(pnlMiddle, BoxLayout.Y_AXIS));
	pnlMiddle.setLayout(new BorderLayout(2, 2));
	JLabel labelDescription = new JLabel(MappedPlaces.msgStrings.getString("dlgPlaceDescription")); //$NON-NLS-1$
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
