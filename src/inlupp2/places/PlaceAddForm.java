/**
 * 
 */
package inlupp2.places;

import inlupp2.*;

//import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author zeron
 *
 */
@SuppressWarnings("serial")
public abstract class PlaceAddForm extends JPanel {

    private JTextField tfPlaceName = new JTextField(12);

    PlaceAddForm() {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	JPanel pnlTop = new JPanel();
	JLabel labelName = new JLabel(MappedPlaces.msgStrings.getString("dlgPlaceName")); 		//$NON-NLS-1$
	pnlTop.add(labelName);
	pnlTop.add(tfPlaceName);
	labelName.setLabelFor(tfPlaceName);
	add(pnlTop);
    }

    public String getName() {
	return tfPlaceName.getText();
    }
}
