/**
 * Inlämningsuppgift 2 i PROG2
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2;

import inlupp2.places.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.HeadlessException;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import javax.swing.filechooser.*;

/**
 * The main class. <br>
 * Creates the GUI and provides functionality for GUI elements.
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com"> VictorSago01@gmail.com</a>
 */
public class MappedPlaces extends JFrame {

    private static final long serialVersionUID = 7501471877325818300L;

    // For "What is here?"-button
    private static final int lookupRadius = 11;		// Value of 7 makes the area to be examined too small
    
//    static Locale 			currentLocale 	= new Locale("sv", "SE");
    static Locale			currentLocale 	= new Locale("en", "GB");
    public static ResourceBundle 	msgStrings 	= ResourceBundle.getBundle("inlupp2.resources.messages", currentLocale);

    private JFileChooser 	jfc 	= new JFileChooser(".");
    private JColorChooser 	jcc	= new JColorChooser();
    
    // Name of the current file
    private String docfile = "";

    // GUI components
    private MapPanel 	pnlMainPane 	= new MapPanel();
    private JPanel 	pnlTopPane 	= new JPanel();
    private JPanel 	pnlEastPane 	= new JPanel();
    private JPanel	pnlSouthPane	= new JPanel();
    private JTextField 	tfSearch 	= new JTextField(25);
    private JLabel	lblStatus	= new JLabel(msgStrings.getString("strStatus"), SwingConstants.LEFT);
        
    private JComboBox<String> 			cbxPlaceTypes 	= new JComboBox<>();
    private DefaultListModel<Category> 	lstmod 		= new DefaultListModel<>();
    private JList<Category> 		lstCategories 	= new JList<>(lstmod);
    
    // Menu bar and items
    private JMenuBar	mymenubar = new JMenuBar();
    private JMenu	fileMenu  = new JMenu(msgStrings.getString("menuFile"));
    private JMenuItem 	miNew 	  = new JMenuItem(msgStrings.getString("miNewMap")),
	    		miOpen 	  = new JMenuItem(msgStrings.getString("miOpen")),
	    		miExit 	  = new JMenuItem(msgStrings.getString("miExit")),
	    		miSave	  = new JMenuItem(msgStrings.getString("miSave")),
	    		miSaveAs  = new JMenuItem(msgStrings.getString("miSaveAs"));
    
    // Buttons
    private JButton 	btnSearch 	= new JButton(msgStrings.getString("btnSearch")),
	    		btnHide 	= new JButton(msgStrings.getString("btnHidePlaces")),
	    		btnDel 		= new JButton(msgStrings.getString("btnDelPlaces")),
	    		btnWhat 	= new JButton(msgStrings.getString("btnWhatHere")),
	    		btnCatHide 	= new JButton(msgStrings.getString("btnCatHide")),
	    		btnCatNew 	= new JButton(msgStrings.getString("btnCatNew")),
	    		btnCatDel 	= new JButton(msgStrings.getString("btnCatDel"));
    
    // Put all visible buttons into one array list
    // and their corresponding ActionListener into another
    private ArrayList<JButton> 		buttonList 	= new ArrayList<>();
    private ArrayList<ActionListener> 	btnListenerList = new ArrayList<>();

    // Listeners that can be added to or removed from a GUI component
    private CreatePlaceListener placeCreator	= new CreatePlaceListener();
    private QueryListener	whatsHere	= new QueryListener();
    private PlaceMouseListener 	placeListener 		= new PlaceMouseListener();
    private SearchListener 	searchlistener 	= new SearchListener();
    private ExitListener 	exLis 		= new ExitListener();
    private SaveListener 	saveLis 	= new SaveListener();
    private NewPlaceListener 	chooseType 	= new NewPlaceListener();

    /**
     * @param title The title of the main window
     * @throws HeadlessException
     */
    public MappedPlaces(String title) throws HeadlessException {
	
	super(title);
	
	// Fill the button list with buttons
	buttonList.add(btnSearch);
	buttonList.add(btnHide);
	buttonList.add(btnDel);
	buttonList.add(btnWhat);
	buttonList.add(btnCatHide);
	buttonList.add(btnCatNew);
	buttonList.add(btnCatDel);
	
	// Fill the listener list with listeners
	btnListenerList.add(searchlistener);
	btnListenerList.add(new HidePlacesListener());
	btnListenerList.add(new DeletePlacesListener());
	btnListenerList.add(new WhatHereListener());
	btnListenerList.add(new HideCategoryListener());
	btnListenerList.add(new NewCategoryListener());
	btnListenerList.add(new DeleteCategoryListener());		

	setLayout(new BorderLayout(2, 2));
	
	// Used in the placement of components in the top and east panes
	Dimension hSpace = new Dimension(4, 1);
	Dimension vSpace = new Dimension(1, 4);
	
	// Create main menu bar and add menu items
	setJMenuBar(mymenubar);
	mymenubar.add(fileMenu);
	fileMenu.add(miNew);
	fileMenu.add(miOpen);
	fileMenu.add(miSave);
	fileMenu.add(miSaveAs);
	fileMenu.addSeparator();
	fileMenu.add(miExit);
	miSave.setEnabled(false);
	miSaveAs.setEnabled(false);
	
	// Add Listeners to all Menu Items
	miNew.addActionListener(new NewMapListener());
	miOpen.addActionListener(new OpenListener());
	miSave.addActionListener(saveLis);
	miSaveAs.addActionListener(saveLis);
	miExit.addActionListener(exLis);
	
	// Add central area
	pnlMainPane.setLayout(null);	
	add(pnlMainPane, BorderLayout.CENTER);
		
	// Add top panel with buttons and other elements
	pnlTopPane.setLayout(new BoxLayout(pnlTopPane, BoxLayout.X_AXIS));
	JLabel lblNew = new JLabel(msgStrings.getString("lblNew"));
	pnlTopPane.add(Box.createRigidArea(hSpace));
	pnlTopPane.add(lblNew);
	pnlTopPane.add(Box.createRigidArea(hSpace));
	cbxPlaceTypes.addItem(msgStrings.getString("cbxNamedPlaces"));
	cbxPlaceTypes.addItem(msgStrings.getString("cbxDescribedPlaces"));
	cbxPlaceTypes.setMaximumSize(cbxPlaceTypes.getPreferredSize());	
	pnlTopPane.add(cbxPlaceTypes);
	pnlTopPane.add(Box.createRigidArea(hSpace));
	tfSearch.setForeground(Color.GRAY);
	tfSearch.setText(msgStrings.getString("strSearch"));
	tfSearch.setMaximumSize(tfSearch.getPreferredSize());
	pnlTopPane.add(tfSearch);
	pnlTopPane.add(Box.createRigidArea(hSpace));
	pnlTopPane.add(btnSearch);
	pnlTopPane.add(Box.createRigidArea(hSpace));
	pnlTopPane.add(btnHide);
	pnlTopPane.add(Box.createRigidArea(hSpace));
	pnlTopPane.add(btnDel);
	pnlTopPane.add(Box.createRigidArea(hSpace));
	pnlTopPane.add(btnWhat);
	pnlTopPane.add(Box.createHorizontalGlue());
	add(pnlTopPane, BorderLayout.NORTH);
	
	// Add right panel with category list and buttons
	pnlEastPane.setLayout(new BoxLayout(pnlEastPane, BoxLayout.Y_AXIS));
	pnlEastPane.add(Box.createVerticalGlue());
	pnlEastPane.add(Box.createRigidArea(vSpace));
	JLabel lblCategories = new JLabel(msgStrings.getString("lblCategories"));
	pnlEastPane.add(lblCategories);
	pnlEastPane.add(Box.createRigidArea(vSpace));	
	JScrollPane spCatList = new JScrollPane(lstCategories, 
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	lstCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	lstCategories.setCellRenderer(new CategoryListRenderer());
	lstCategories.addListSelectionListener(new CategorySelectionListener());
	spCatList.setPreferredSize(new Dimension(120, 140));
	pnlEastPane.add(spCatList);
	pnlEastPane.add(Box.createRigidArea(vSpace));
	pnlEastPane.add(btnCatHide);
	pnlEastPane.add(Box.createRigidArea(vSpace));
	pnlEastPane.add(btnCatNew);
	pnlEastPane.add(Box.createRigidArea(vSpace));
	pnlEastPane.add(btnCatDel);
	pnlEastPane.add(Box.createRigidArea(vSpace));
	pnlEastPane.add(Box.createVerticalGlue());
	add(pnlEastPane, BorderLayout.EAST);
	
	pnlSouthPane.setLayout(new BoxLayout(pnlSouthPane, BoxLayout.X_AXIS));
	pnlSouthPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED));	
	
	pnlSouthPane.setPreferredSize(new Dimension(this.getWidth(), 20));
	pnlSouthPane.add(lblStatus);
	
	pnlSouthPane.add(Box.createHorizontalGlue());
	
	add(pnlSouthPane, BorderLayout.SOUTH);
	
	// Add the rest of the Listeners to GUI elements
	cbxPlaceTypes.addActionListener(chooseType);
	tfSearch.addFocusListener(new SearchFieldFocusListener());	
	tfSearch.addActionListener(searchlistener);
	addWindowListener(exLis);
	
	// Buttons cannot be used until a map is loaded
	disableButtons();
	
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	setMinimumSize(new Dimension(980, 300));
	pack();
	setVisible(true);
    }
    
    /**
     * Enable button actions by adding to each button a corresponding ActionListener.
     * 
     */
    private void enableButtons() {
	for(int i = 0; i < buttonList.size(); i++) {
	    buttonList.get(i).addActionListener(btnListenerList.get(i));
	}
    }
    
    /**
     * Disable button actions by removing from each button that button's ActionListener.
     * 
     */
    private void disableButtons() {
	for(int i = 0; i < buttonList.size(); i++) {
	    buttonList.get(i).removeActionListener(btnListenerList.get(i));
	}
    }

    /**
     * Listener for menu item "New map. <br>
     * Loads a new map from file.
     */
    class NewMapListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent aev) {
	    // If the contents of the main pane has been modified
	    // and not saved then ask for confirmation
	    if (pnlMainPane.isModified()) {
		int dialogAnswer = JOptionPane.showConfirmDialog(MappedPlaces.this, 
							msgStrings.getString("strConfirmContinue"));
		if (dialogAnswer != JOptionPane.OK_OPTION) {
		    return;
		}
	    }
	    
	    // Prepare FileChooser dialogue and then diplay it
	    jfc.resetChoosableFileFilters();
	    FileNameExtensionFilter fnef = 
		    new FileNameExtensionFilter(msgStrings.getString("strImageFiles"),
			    "png", "jpg", "jpeg", "gif", "bmp", "wbmp");
	    jfc.addChoosableFileFilter(fnef);
	    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    jfc.setFileFilter(fnef);
	    jfc.setSelectedFile(new File("")); 	// In case a file was opened before reset it
	    int dlgResult = jfc.showOpenDialog(MappedPlaces.this);
	    if (dlgResult != JFileChooser.APPROVE_OPTION) {
		return;
	    }
	    
	    // Read the image from chosen file
	    File imgfile = jfc.getSelectedFile();
	    BufferedImage bgImage = null;
	    jfc.resetChoosableFileFilters();		// Reset file filters so that next time the dialogue is opened no name is filled in
	    try {
		bgImage = ImageIO.read(imgfile);
	    } catch (IOException e) {
		System.err.println(msgStrings.getString("errorMapLoad") + e.getMessage());
		lblStatus.setText(msgStrings.getString("errorMapLoad"));
		return;
	    }
	    try {
	        remove(pnlMainPane); 			// Remove the main panel,  
	        pnlMainPane = new MapPanel(bgImage);	//  initialize a new one with the image that has just been read
	        add(pnlMainPane, BorderLayout.CENTER);	//  and add it to the main frame
	        lstmod.clear();				// Clear the old category list
	        // The file where everything will be saved hasn't been given a name yet
	        docfile = "";
            } catch (Exception e) {
        	System.err.println(e.getMessage());
	        e.printStackTrace();
            }
	    String mapname = imgfile.getAbsolutePath();
	    if (!(miSave.isEnabled() && miSaveAs.isEnabled())) {		// Enable saving
		miSave.setEnabled(true);
		miSaveAs.setEnabled(true);
	    }
	    lblStatus.setText(msgStrings.getString("msgStatusMapLoaded") + mapname + "\"");
	    // After the map has been loaded the buttons can perform their actions
	    enableButtons();
	    pack();
	    validate();
	    repaint();
	}
    }

    /**
     * Listener for menu item "Open". <br>
     * Loads a previously saved file.
     */
    class OpenListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent aev) {
	    // If the contents of the main pane has been modified
	    // and not saved then ask for confirmation
	    if (pnlMainPane.isModified()) {
		int dialogAnswer = JOptionPane.showConfirmDialog(MappedPlaces.this, 
							msgStrings.getString("strConfirmContinue"));
		if (dialogAnswer != JOptionPane.OK_OPTION) {
		    return;
		}
	    }
	    
	    // Prepare FileChooser dialogue and then display it
	    jfc.resetChoosableFileFilters();
	    FileNameExtensionFilter fnef = 
		    new FileNameExtensionFilter(msgStrings.getString("strMappedFiles"), "kart");
	    jfc.addChoosableFileFilter(fnef);
	    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    jfc.setFileFilter(fnef);
	    int dlgResult = jfc.showOpenDialog(MappedPlaces.this);
	    if (dlgResult != JFileChooser.APPROVE_OPTION) {
		return;
	    }
	    
	    // Read the contents from chosen file
	    File file = jfc.getSelectedFile();
	    docfile = file.getAbsolutePath();
	    jfc.resetChoosableFileFilters();
	    try {
		lstmod.clear();				// Clear the old category list
		remove(pnlMainPane);			// Remove the main panel,
		pnlMainPane = new MapPanel(file);	//  initialize a new one with the file that has just been read
	        add(pnlMainPane, BorderLayout.CENTER);	//  and add it to the main frame
	        // Add mouse listeners to each place from a loaded file
	        for(Component comp : pnlMainPane.getComponents()) {
	            comp.addMouseListener(placeListener);
	        }		    
            } catch (ClassNotFoundException cnfe) {
		System.err.println(msgStrings.getString("errorFileFormat") + cnfe.getMessage());
		lblStatus.setText(msgStrings.getString("errorFileFormat"));
	        cnfe.printStackTrace();
	        return;
            } catch (FileNotFoundException fnfe) {
		System.err.println(msgStrings.getString("errorFileNotFound") + fnfe.getMessage());
		lblStatus.setText(msgStrings.getString("errorFileNotFound"));
	        fnfe.printStackTrace();
	        return;
            } catch (IOException ioe) {
		System.err.println(msgStrings.getString("errorGeneralIO") + ioe.getMessage());
		lblStatus.setText(msgStrings.getString("errorGeneralIO"));
	        ioe.printStackTrace();
	        return;
            }
	    for (Category c : pnlMainPane.getCategories()) {		// Fill the category list
		lstmod.addElement(c);
	    }
	    if (!(miSave.isEnabled() && miSaveAs.isEnabled())) {		// Enable saving
		miSave.setEnabled(true);
		miSaveAs.setEnabled(true);
	    }
	    lblStatus.setText(msgStrings.getString("msgStatusOpened") + 
		    docfile.substring(docfile.lastIndexOf(File.separator)+1) + 
		    msgStrings.getString("msgStatusAt") + 
		    docfile.substring(0, docfile.lastIndexOf(File.separator) + 1) + "\"");
	    
	    // After the document has been loaded the buttons can perform their actions
	    enableButtons();
	    pack();
	    validate();
	    repaint();
	}
    }

    /**
     * Listener for menu items "Save" and "Save as". <br>
     * Saves the current Place Map to a file.
     */
    class SaveListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent aev) {
	    if (!pnlMainPane.hasMapImage()) {
		JOptionPane.showMessageDialog(MappedPlaces.this, msgStrings.getString("msgNothingToSave"),
			msgStrings.getString("msgNoMapTitle"), JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    
	    // Determine whether it's "Save" or "Save as"
	    JMenuItem mi = (JMenuItem) aev.getSource();
	    // If it's "Save as" or if no file has been named/loaded then ask for a file name
	    if (mi.getText() == msgStrings.getString("miSaveAs") || docfile.equalsIgnoreCase("")) {
		jfc.resetChoosableFileFilters();
		FileNameExtensionFilter fnef = 
			new FileNameExtensionFilter(msgStrings.getString("strMappedFiles"), "kart");
		jfc.addChoosableFileFilter(fnef);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setFileFilter(fnef);
		int dlgResult = jfc.showSaveDialog(MappedPlaces.this);
		if (dlgResult != JFileChooser.APPROVE_OPTION)
		    return;
		File file = jfc.getSelectedFile();
		docfile = file.getAbsolutePath();	// docfile is reused at subsequent "Save" actions
		if (!docfile.endsWith(".kart")) {	// The extension for data files is ".kart"
		    docfile += ".kart";
		}
		jfc.resetChoosableFileFilters();
	    }
	    try {
		for(Component comp : pnlMainPane.getComponents())	// Remove mouse listeners prior to saving
		    comp.removeMouseListener(placeListener);
	        pnlMainPane.saveData(docfile);				// Save data file
	        for(Component comp : pnlMainPane.getComponents())	// Re-add mouse listeners
		    comp.addMouseListener(placeListener);
	    } catch (FileNotFoundException fnfe) {
		System.err.println(msgStrings.getString("errorFileNotFound") + fnfe.getMessage());
		lblStatus.setText(msgStrings.getString("errorFileNotFound"));
	        fnfe.printStackTrace();
	        return;
            } catch (IOException ioe) {
		System.err.println(msgStrings.getString("errorGeneralIO") + ioe.getMessage());
		lblStatus.setText(msgStrings.getString("errorGeneralIO"));
	        ioe.printStackTrace();
	        return;
            }
	    lblStatus.setText(msgStrings.getString("msgStatusSaved") + 
		    docfile.substring(docfile.lastIndexOf(File.separator)+1) + 
		    msgStrings.getString("msgStatusAt") + 
		    docfile.substring(0, docfile.lastIndexOf(File.separator)+1) + "\"");
	}
    }
    
    /**
     * Listener for "Exit" menu item and exit button. <br>
     * Exits program if document hasn't been modified,
     * otherwise asks for confirmation first.
     */
    class ExitListener extends WindowAdapter implements ActionListener {
	
	private void exitProgram(AWTEvent aev){
	    // If document has been modified ask for confirmation
	    if (pnlMainPane.isModified()) {
		int dialogAnswer = JOptionPane.showConfirmDialog(MappedPlaces.this, 
							msgStrings.getString("strConfirmExit"));
		if (dialogAnswer == JOptionPane.OK_OPTION) {
		    System.exit(0);
		}		    
	    } else {
		System.exit(0);
	    }
	}
	
	@Override
	public void actionPerformed(ActionEvent aev){
	    exitProgram(aev);
	}
	
	@Override
	public void windowClosing(WindowEvent wev){
	    exitProgram(wev);
	}
    }
        
    /**
     * Listener that reacts to choice of place type to create. <br>
     * Changes the cursor and prepares for creation of a new place marker.
     */
    public class NewPlaceListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (pnlMainPane.hasMapImage()) {			// New place markers can be created only if there is a map loaded
		pnlMainPane.addMouseListener(placeCreator);
		pnlMainPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		cbxPlaceTypes.removeActionListener(chooseType);		// Only one place marker at a time can be created
		cbxPlaceTypes.setEnabled(false);
		lblStatus.setText(msgStrings.getString("msgStatusCreatePlace"));
	    }
	}
    }
    
    /**
     * Listener for creating new place markers on the map. <br>
     * Creates new place marker, stores it and restores normal cursor.
     */
    class CreatePlaceListener extends MouseAdapter {
	
	@Override
	public void mouseClicked(MouseEvent mev) {
	    // determine click position and create new Position object
	    int x = mev.getX();
	    int y = mev.getY();
	    Position newPos = new Position(x, y);

	    // Determine chosen placeType: read the combobox choice
	    String placeType = (String)cbxPlaceTypes.getSelectedItem();
	    
	    // Determine if a category is selected and which one it is
	    Category currentCat = null;
	    if (lstCategories.getSelectedIndex() != -1) {
		currentCat = lstCategories.getSelectedValue();
	    }
	    
	    // create place of chosen type and category
	    Place newPlace = null;
	    PlaceAddForm inputFrm;
	    String dlgTitle;
	    if (placeType.equalsIgnoreCase(msgStrings.getString("cbxNamedPlaces"))) {
		inputFrm = new NamedPlaceAddForm();
		dlgTitle = msgStrings.getString("strNewNamedPlace");
	    } else if (placeType.equalsIgnoreCase(msgStrings.getString("cbxDescribedPlaces"))) {
		inputFrm = new DescribedPlaceAddForm();
		dlgTitle = msgStrings.getString("strNewDescribedPlace");
	    } else {
		// If something is wrong and placeType is assigned an unrecognizable value
		// 	show an error message and return.
		lblStatus.setText(msgStrings.getString("msgError") + ": " 
			+ msgStrings.getString("msgNoPlaceType"));
		JOptionPane.showMessageDialog(MappedPlaces.this, msgStrings.getString("msgNoPlaceType"), 
				msgStrings.getString("msgError"), JOptionPane.ERROR_MESSAGE);
		restoreState();
		return;
	    }
	    // Open a dialogue with an input form for appropriate place type
	    int dlgResult = JOptionPane.showConfirmDialog(MappedPlaces.this, inputFrm, dlgTitle,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    if (dlgResult != JOptionPane.OK_OPTION) {
		lblStatus.setText(msgStrings.getString("msgStatusCancel"));
		restoreState();
		return;
	    }
	    String newPlaceName = inputFrm.getName();
	    if (newPlaceName.equalsIgnoreCase("")) {
		// If name for the new place marker hasn't been filled in
		// 	show an error message and return.
		lblStatus.setText(msgStrings.getString("msgNewPlaceErr"));
		JOptionPane.showMessageDialog(MappedPlaces.this, msgStrings.getString("msgNewPlaceErr"), 
	    			msgStrings.getString("dlgPlaceErrTitle"), JOptionPane.ERROR_MESSAGE);
		restoreState();
		return;
	    }
	    // Create a new place of appropriate type
	    if (placeType.equalsIgnoreCase(msgStrings.getString("cbxNamedPlaces"))) {
		// Create Named Place of selected category
		newPlace = new NamedPlace(newPlaceName, newPos, currentCat);
	    } else if (placeType.equalsIgnoreCase(msgStrings.getString("cbxDescribedPlaces"))) {
		// If the chosen placeType is "described Place" get its description
		String newPlaceDescription = ((DescribedPlaceAddForm) inputFrm).getDescription();
		if (newPlaceDescription.equalsIgnoreCase("")) {
		    // If description for a Described Place is empty
		    // 		show an error message and return.
		    lblStatus.setText(msgStrings.getString("msgNewMissingDescription"));
		    JOptionPane.showMessageDialog(MappedPlaces.this, 
	    			msgStrings.getString("msgNewMissingDescription"), 
	    			msgStrings.getString("dlgPlaceErrTitle"), JOptionPane.ERROR_MESSAGE);
		    restoreState();
		    return;
		}
		// Create Described Place of selected category
		newPlace = new DescribedPlace(newPlaceName, newPos, currentCat, newPlaceDescription);
	    } else {
		lblStatus.setText(msgStrings.getString("msgError") + ": " 
					+ msgStrings.getString("msgNoPlaceType"));
		restoreState();
		return;
	    }
	    
	    // If a place has been created and no errors were generated
	    // 		add it to the main pane and associate an action listener with it.
	    pnlMainPane.addPlace(newPos, newPlaceName, newPlace);
	    newPlace.addMouseListener(placeListener);
	    lblStatus.setText(msgStrings.getString("msgStatusCreated1") 
			    + newPlace.getName() + msgStrings.getString("msgStatusCreated2"));
	    // Lastly, make it possible to save the document if it hasn't been done already
	    if (!(miSave.isEnabled() && miSaveAs.isEnabled())) {
		miSave.setEnabled(true);
		miSaveAs.setEnabled(true);
	    }
	    
	    restoreState();
	    pnlMainPane.revalidate();
	    pnlMainPane.repaint();
	}
	
	/**
	 * Common actions when leaving the mouseClicked method. <br>
	 * Removes place creation listener and restores cursor and actions that were disabled for 
	 * creation of a new place marker.
	 */
	private void restoreState() {
	    pnlMainPane.removeMouseListener(placeCreator);
	    pnlMainPane.setCursor(Cursor.getDefaultCursor());
	    cbxPlaceTypes.addActionListener(chooseType);
	    cbxPlaceTypes.setEnabled(true);
	}
    }

    /**
     * Listener for the search field. <br>
     * Changes the field's text when it gets or looses focus.
     */
    class SearchFieldFocusListener extends FocusAdapter {

	@Override
	public void focusGained(FocusEvent fev) {
	    if (tfSearch.getText().equals(msgStrings.getString("strSearch"))) {
		tfSearch.setForeground(Color.BLACK);
		tfSearch.setText("");
		lblStatus.setText(msgStrings.getString("msgStatusWriteName"));
	    }
	}

	@Override
	public void focusLost(FocusEvent fev) {
	    if (tfSearch.getText().equals("")) {
		tfSearch.setForeground(Color.GRAY);
		tfSearch.setText(msgStrings.getString("strSearch"));
		lblStatus.setText("");
	    }
	}
    }

    /**
     * Listener for the search field "Enter"-action and for "Search"-button. <br>
     * Searches the data structures for a place by name.
     */
    class SearchListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    String name = tfSearch.getText();
	    
	    // Can't search if there is no map and no data structures or if no place name has been entered
	    if(!pnlMainPane.hasMapImage()) {
		lblStatus.setText(msgStrings.getString("msgNoMapTitle"));
		return;
	    }
	    if(name.equals("") || name.equals(msgStrings.getString("strSearch"))) {
		lblStatus.setText(msgStrings.getString("msgStatusWriteName"));
		return;
	    }
	    
	    String status = msgStrings.getString("msgStatusSearching") + name + " ... ";
	    Collection<Place> places = pnlMainPane.getPlacesByName(name);
	    if(places.isEmpty()) {
		status += msgStrings.getString("msgStatusNoSuchPlaces");
	    } else {
		pnlMainPane.setSelectedPlaces(places);
		status += msgStrings.getString("msgStatusFound") + " " + places.size() + ".";
	    }
	    lblStatus.setText(status);
	    pnlMainPane.repaint();
	}
    }

    /**
     * Listener for the "Hide places"-button. <br>
     * Hides all selected places. If no places are selected does nothing.
     */
    class HidePlacesListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    String status;
	    // Get a list of all selected places
	    Collection<Place> places = pnlMainPane.getSelectedPlaces();
	    if (places.isEmpty()) {
		status = 0 + " " + msgStrings.getString("msgPlacesSelected");
		lblStatus.setText(status);
	    } else {
		status = places.size() + " " + msgStrings.getString("msgStatusPlaces") 
			+ " " + msgStrings.getString("msgStatusHidden");
		// Hide all places in the list
		pnlMainPane.hidePlaces(places);
		lblStatus.setText(status);
		pnlMainPane.repaint();
	    }
	}
    }

    /**
     * Listener for the "Delete places"-button. <br>
     * Deletes all selected places. If no places are selected does nothing.
     */
    class DeletePlacesListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    String status;
	    // Get a list of all selected places
	    Collection<Place> places = pnlMainPane.getSelectedPlaces();
	    if (places.isEmpty()) {
		status = 0 + " " + msgStrings.getString("msgPlacesSelected");
		lblStatus.setText(status);
	    } else {
		status = places.size() + " " + msgStrings.getString("msgStatusPlaces") 
			+ " " + msgStrings.getString("msgStatusRemoved");
		// Delete all places in the list
		pnlMainPane.removePlaces(places);
		lblStatus.setText(status);
		pnlMainPane.repaint();
	    }
	}
    }

    /**
     * Listener for the "What is here?"-button. <br>
     * Changes the cursor and allows to query the map hidden about places at a specific position.
     */
    class WhatHereListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (pnlMainPane.hasMapImage()) {
		pnlMainPane.addMouseListener(whatsHere);
		pnlMainPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		lblStatus.setText(msgStrings.getString("msgStatusWhatIsHere"));
	    }
	}
    }
    
    /**
     * Listener for querying the map about hidden places at a clicked position. <br>
     * Finds out if there are any hidden places within an area of the clicked position.
     */
    class QueryListener extends MouseAdapter {

	@Override
	public void mouseClicked(MouseEvent mev) {
	    // determine click position
	    int x = mev.getX();
	    int y = mev.getY();
	    lblStatus.setText("(" + x + ", " + y + ")");
	    // Generate an array of coordinates of a 7x7 square centred in the click
	    Collection<Position> coordinates = new ArrayList<Position>();
	    for (int i = -lookupRadius/2; i <= lookupRadius/2; i++) {
		for (int j = -lookupRadius/2; j <= lookupRadius/2; j++) {
		    coordinates.add(new Position(x+j, y+i));
		}
	    }
	    // Step through the array and for each pair of coordinates query MapPanel for a place with these coordinates
	    for (Position pos : coordinates) {
		Place place = pnlMainPane.getPlaceByPosition(pos);
		// If a place exists at these coordinates make it visible
		if (place != null) {
		    place.setVisible(true);
		}
	    }
	    // Remove this QueryListener and restore the cursor to default
	    pnlMainPane.removeMouseListener(whatsHere);
	    pnlMainPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    pnlMainPane.revalidate();
	    pnlMainPane.repaint();
	}
    }
        
    /**
     * Listener for Category List. <br>
     * On category selection change makes all the places of the selected category visible and selected.
     */
    class CategorySelectionListener implements ListSelectionListener {

	@Override
	public void valueChanged(ListSelectionEvent e) {
	    Category selectedCategory = lstCategories.getSelectedValue();
	    if (selectedCategory != null) {
		Collection<Place> places = pnlMainPane.getPlacesByCategory(selectedCategory);
		String statustxt = msgStrings.getString("msgStatusCategory") + selectedCategory + " " 
				+ msgStrings.getString("msgStatusSelected") + ": ";
		statustxt += places.size() + " " + msgStrings.getString("msgStatusPlaces");
		pnlMainPane.setSelectedPlaces(places);
		lblStatus.setText(statustxt);
		pnlMainPane.repaint();
	    }
	}
    }

    /**
     * Listener for "Hide category"-button. <br>
     * Hides all places in the selected category.
     */
    class HideCategoryListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    Category selectedCategory = lstCategories.getSelectedValue();
	    if (selectedCategory != null) {
		// Get a list of all places in the selected category
		Collection<Place> places = pnlMainPane.getPlacesByCategory(selectedCategory);
		String statustxt = msgStrings.getString("msgStatusCategory") + selectedCategory + " " 
			+ msgStrings.getString("msgStatusSelected") + ": ";
		statustxt += places.size() + " " + msgStrings.getString("msgStatusPlaces");
		statustxt += msgStrings.getString("msgStatusHidden");
		// Hide all places in the list
		pnlMainPane.hidePlaces(places);
		lblStatus.setText(statustxt);
		pnlMainPane.repaint();
	    }
	}
    }


    /**
     * Listener for "New category"-button. <br>
     * Opens a dialogue for creation of a new category. Creates a new category if it doesn't already exist.
     */
    class NewCategoryListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    // If there is no map present categories cannot be created
	    if (!pnlMainPane.hasMapImage()) {
		lblStatus.setText(msgStrings.getString("msgMapMissing"));
		JOptionPane.showMessageDialog(MappedPlaces.this, msgStrings.getString("msgMapMissing"), 
			msgStrings.getString("msgNoMapTitle"), JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    
	    // Create NewCategory-dialogue with ColorChooser panel in the centre
	    // 		and Name input panel at the top
	    JPanel dlgNewCat = new JPanel(new BorderLayout());
	    JPanel pnlCatName = new JPanel();
	    JTextField tfCatName = new JTextField(20);
	    JLabel lblCatName = new JLabel(msgStrings.getString("dlgCatName"));
	    lblCatName.setLabelFor(tfCatName);
	    pnlCatName.add(lblCatName);
	    pnlCatName.add(tfCatName);
	    dlgNewCat.add(pnlCatName, BorderLayout.NORTH);
	    dlgNewCat.add(jcc, BorderLayout.CENTER);

	    // Display NewCategory-dialogue
	    int dlgResult = JOptionPane.showConfirmDialog(MappedPlaces.this, dlgNewCat, 
		    		msgStrings.getString("dlgNewCat"), JOptionPane.OK_CANCEL_OPTION, 
		    		JOptionPane.PLAIN_MESSAGE);		    
	    if (dlgResult == JOptionPane.OK_OPTION) {
		String catName = tfCatName.getText();
		if (catName.equals("")) {
		    // Nameless category cannot be created
		    lblStatus.setText(msgStrings.getString("msgNewCatErr"));
		    JOptionPane.showMessageDialog(MappedPlaces.this, 
			    	msgStrings.getString("msgNewCatErr"), 
			    	msgStrings.getString("dlgCatErrTitle"), JOptionPane.ERROR_MESSAGE);
		} else {
		    Color newCol = jcc.getColor();
		    Category newCat = new Category(catName, newCol);
		    
		    if (pnlMainPane.addCategory(newCat)) {		// The new category is added if it doesn't exist
			lstmod.addElement(newCat);
			lblStatus.setText(msgStrings.getString("msgStatusCategory") + 
				"\"" + newCat.getName() + "\" " + 
				msgStrings.getString("msgStatusWithColor") + "(" + 
				newCat.getColor().getRed() + ", " + 
				newCat.getColor().getGreen() + ", " + 
				newCat.getColor().getBlue() + ") " + 
				msgStrings.getString("msgStatusCreated"));
			// Make it possible to save the document if it hasn't been done already
			if (!(miSave.isEnabled() && miSaveAs.isEnabled())) {
			    miSave.setEnabled(true);
			    miSaveAs.setEnabled(true);
			}
		    } else {						// If category already exists
			lblStatus.setText(msgStrings.getString("msgNewCatErr2"));
			JOptionPane.showMessageDialog(MappedPlaces.this, 
				msgStrings.getString("msgNewCatErr2"), 
				msgStrings.getString("dlgCatErrTitle"),	
				JOptionPane.ERROR_MESSAGE);
		    }
		}
	    }
	}
    }

    /**
     * Listener for "Delete category"-button. <br>
     * Deletes a category and all the places that belong to it.
     */
    class DeleteCategoryListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    Category selectedCategory = lstCategories.getSelectedValue();
	    if (selectedCategory != null) {
		String statustxt = msgStrings.getString("msgStatusCategory") + selectedCategory + ": ";
		// Get a list of all places in the selected category
		Collection<Place> places = pnlMainPane.getPlacesByCategory(selectedCategory);
		statustxt += places.size() + " " + msgStrings.getString("msgStatusPlaces");
		// Delete all places in the list
		pnlMainPane.removePlaces(places);
		boolean rem = pnlMainPane.removeCategory(selectedCategory);
		statustxt += rem ? (" " + msgStrings.getString("msgStatusRemoved")) 
				 : (" " + msgStrings.getString("msgStatusNotRemoved"));
		// Remove the category from the category list
		lstmod.removeElement(selectedCategory);
		lblStatus.setText(statustxt);
		pnlMainPane.repaint();
	    }
	}
    }

    /**
     * The listener for all Places. <br>
     * Folds/unfolds, selects/unselects places, displays the information about a place in the status line.
     */
    class PlaceMouseListener extends MouseAdapter {
	
	@Override
	public void mouseClicked(MouseEvent mev) {
	    if (mev.getSource() instanceof Place) {
		Place p = (Place) mev.getSource();
		if (mev.getButton() == MouseEvent.BUTTON1) {		// If the left mouse button is clicked
		    if (p.isSelected()) {				// Toggle selection of the clicked place
			pnlMainPane.setSelectedPlace(p, false);
		    } else {
			pnlMainPane.setSelectedPlace(p, true);
		    }
		} else if (mev.getButton() == MouseEvent.BUTTON3) {	// If the right mouse button is clicked
		    if (p.isFolded()) {					// Toggle folded/unfolded state of the clicked place
			p.setFolded(false);
		    } else {
			p.setFolded(true);
		    }
		}
	    }
	    pnlMainPane.validate();
	    pnlMainPane.repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent mev) {
	    Place p = (Place) mev.getSource();
	    String statustxt = "";
	    if (p instanceof NamedPlace)
		statustxt = msgStrings.getString("strNamedPlace") + ": ";
	    else if (p instanceof DescribedPlace)
		statustxt = msgStrings.getString("strDescPlace") + ": ";
	    statustxt += p.getName();
	    if (p.hasCategory()) {
		statustxt += ", " + msgStrings.getString("msgStatusCategory") + ": " + p.getCategory().getName();
	    }
	    statustxt += ", " + p.getPosition();
	    lblStatus.setText(statustxt);
	}
	
	@Override
	public void mouseExited(MouseEvent mev) {
	    lblStatus.setText("");
	}
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
//	Locale.setDefault(currentLocale);
	new MappedPlaces(msgStrings.getString("MainTitle"));
    }

}
