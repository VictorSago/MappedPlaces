/**
 * 
 */
/**
 * @author Victor Sago
 *
 */

package inlupp2;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.HeadlessException;
import java.io.*;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.*;

import inlupp2.places.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.*;

public class MappedPlaces extends JFrame {

    private static final long serialVersionUID = 7329740490539533214L;
    
//    static Locale 			currentLocale 	= new Locale("sv", "SE");	//$NON-NLS-1$ //$NON-NLS-2$
    static Locale			currentLocale 	= new Locale("en", "GB");	//$NON-NLS-1$ //$NON-NLS-2$
    public static ResourceBundle 	msgStrings 	= ResourceBundle.getBundle("inlupp2.resources.messages", currentLocale); //$NON-NLS-1$ //$NON-NLS-2$

    private JFileChooser 	jfc 	= new JFileChooser(".");			//$NON-NLS-1$
    private JColorChooser 	jcc	= new JColorChooser(Color.BLACK);
    
    // Name of the current file
    private String docfile = "";							//$NON-NLS-1$

    private JTextField 	tfSearch 	= new JTextField(25);
    private JPanel 	pnlTopPane 	= new JPanel();
    private JPanel 	pnlEastPane 	= new JPanel();
    private JPanel	pnlSouthPane	= new JPanel();
    private JLabel	lblStatus	= new JLabel(msgStrings.getString("strStatus"), SwingConstants.LEFT); //$NON-NLS-1$
    
    private MapPanel pnlMainPane = new MapPanel();
    
    private JComboBox<String> 			cbxPlaceTypes 	= new JComboBox<>();
    private DefaultListModel<PlaceCategory> 	lstmod 		= new DefaultListModel<>();
    private JList<PlaceCategory> 		lstCategories 	= new JList<>(lstmod);
    
    private JMenuItem 	miSave 		= new JMenuItem(msgStrings.getString("miSave")), 		//$NON-NLS-1$
	    		miSaveAs 	= new JMenuItem(msgStrings.getString("miSaveAs")); 		//$NON-NLS-1$
    
    // Buttons
//    private JButton 	btnSearch 	= new JButton(msgStrings.getString("btnSearch")), 		//$NON-NLS-1$
//	    		btnHide 	= new JButton(msgStrings.getString("btnHidePlaces")),		//$NON-NLS-1$
//	    		btnDel 		= new JButton(msgStrings.getString("btnDelPlaces")), 		//$NON-NLS-1$
//	    		btnWhat 	= new JButton(msgStrings.getString("btnWhatHere")), 		//$NON-NLS-1$
//	    		btnCatHide 	= new JButton(msgStrings.getString("btnCatHide")), 		//$NON-NLS-1$
//	    		btnCatNew 	= new JButton(msgStrings.getString("btnCatNew")), 		//$NON-NLS-1$
//	    		btnCatDel 	= new JButton(msgStrings.getString("btnCatDel")); 		//$NON-NLS-1$
    
    private ArrayList<JButton> 		buttonList 	= new ArrayList<>();
    private ArrayList<ActionListener> 	btnListenerList = new ArrayList<>();

    // Listeners
    private CreatePlaceListener mAdapt 		= new CreatePlaceListener();
    private PlaceMouseListener 	pAdapt 		= new PlaceMouseListener();
//    private SearchListener 	searchlistener 	= new SearchListener();

    /**
     * @param title
     * @throws HeadlessException
     */
    public MappedPlaces(String title) throws HeadlessException {
	
	super(title);
	
	// Menu Bar
	JMenuBar  mymenubar 	= new JMenuBar();
	JMenu 	  fileMenu	= new JMenu(msgStrings.getString("menuFile")); 			//$NON-NLS-1$
	JMenuItem miNew 	= new JMenuItem(msgStrings.getString("miNewMap")), 		//$NON-NLS-1$
		  miOpen 	= new JMenuItem(msgStrings.getString("miOpen")), 		//$NON-NLS-1$
		  miExit 	= new JMenuItem(msgStrings.getString("miExit")); 		//$NON-NLS-1$
	
	SearchListener 	searchlistener 	= new SearchListener();
	ExitListener 	exLis 		= new ExitListener();
	SaveListener 	saveLis 	= new SaveListener();
	
	JButton 	btnSearch 	= new JButton(msgStrings.getString("btnSearch")), 		//$NON-NLS-1$
    		btnHide 	= new JButton(msgStrings.getString("btnHidePlaces")),		//$NON-NLS-1$
    		btnDel 		= new JButton(msgStrings.getString("btnDelPlaces")), 		//$NON-NLS-1$
    		btnWhat 	= new JButton(msgStrings.getString("btnWhatHere")), 		//$NON-NLS-1$
    		btnCatHide 	= new JButton(msgStrings.getString("btnCatHide")), 		//$NON-NLS-1$
    		btnCatNew 	= new JButton(msgStrings.getString("btnCatNew")), 		//$NON-NLS-1$
    		btnCatDel 	= new JButton(msgStrings.getString("btnCatDel")); 		//$NON-NLS-1$
	
	buttonList.add(btnSearch);
	buttonList.add(btnHide);
	buttonList.add(btnDel);
	buttonList.add(btnWhat);
	buttonList.add(btnCatHide);
	buttonList.add(btnCatNew);
	buttonList.add(btnCatDel);
	
	btnListenerList.add(searchlistener);
	btnListenerList.add(new HidePlacesListener());
	btnListenerList.add(new DeletePlacesListener());
	btnListenerList.add(new WhatHereListener());
	btnListenerList.add(new HideCategoryListener());
	btnListenerList.add(new NewCategoryListener());
	btnListenerList.add(new DeleteCategoryListener());		

	setLayout(new BorderLayout(2, 2));
	
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
	Dimension hSpace = new Dimension(4, 1);
	Dimension vSpace = new Dimension(1, 4);
	pnlTopPane.setLayout(new BoxLayout(pnlTopPane, BoxLayout.X_AXIS));
	JLabel lblNew = new JLabel(msgStrings.getString("lblNew")); 					//$NON-NLS-1$
	pnlTopPane.add(Box.createRigidArea(hSpace));
	pnlTopPane.add(lblNew);
	pnlTopPane.add(Box.createRigidArea(hSpace));
	cbxPlaceTypes.addItem(msgStrings.getString("cbxNamedPlaces")); 					//$NON-NLS-1$
	cbxPlaceTypes.addItem(msgStrings.getString("cbxDescribedPlaces")); 				//$NON-NLS-1$
	cbxPlaceTypes.setMaximumSize(cbxPlaceTypes.getPreferredSize());
	
	pnlTopPane.add(cbxPlaceTypes);
	pnlTopPane.add(Box.createRigidArea(hSpace));
	tfSearch.setForeground(Color.GRAY);
	tfSearch.setText(msgStrings.getString("strSearch")); //$NON-NLS-1$
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
	JLabel lblCategories = new JLabel(msgStrings.getString("lblCategories")); //$NON-NLS-1$
	pnlEastPane.add(lblCategories);
	pnlEastPane.add(Box.createRigidArea(vSpace));	
	JScrollPane spCatList = new JScrollPane(lstCategories, 
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//	Dimension dim = lstCategories.getPreferredSize();
	lstCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	lstCategories.setCellRenderer(new CategoryListRenderer());
	lstCategories.addListSelectionListener(new CategorySelectionListener());
//	dim.width = 200;
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
	
	// Add Listeners to buttons and other visual elements
	cbxPlaceTypes.addActionListener(new NewPlaceListener());
	tfSearch.addFocusListener(new SearchFieldFocusListener());	
	tfSearch.addActionListener(searchlistener);
	disableButtons();
//	btnSearch.addActionListener(searchlistener);
//	btnSearch.setEnabled(false);
//	btnHide.addActionListener(new HidePlacesListener());
//	btnHide.setEnabled(false);
//	btnDel.addActionListener(new DeletePlacesListener());
//	btnDel.setEnabled(false);
//	btnWhat.addActionListener(new WhatHereListener());
//	btnWhat.setEnabled(false);
//	btnCatHide.addActionListener(new HideCategoryListener());
//	btnCatHide.setEnabled(false);
//	btnCatNew.addActionListener(new NewCategoryListener());
//	btnCatNew.setEnabled(false);
//	btnCatDel.addActionListener(new DeleteCategoryListener());
//	btnCatDel.setEnabled(false);

	
	pnlSouthPane.setLayout(new BoxLayout(pnlSouthPane, BoxLayout.X_AXIS));
	pnlSouthPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	pnlSouthPane.setPreferredSize(new Dimension(this.getWidth(), 18));
	pnlSouthPane.add(lblStatus);
	pnlSouthPane.add(Box.createHorizontalGlue());
	add(pnlSouthPane, BorderLayout.SOUTH);
	
	addWindowListener(exLis);
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	setMinimumSize(new Dimension(980, 300));
	pack();
	setVisible(true);
    }
    
    private void enableButtons() {
	for(int i = 0; i < buttonList.size(); i++) {
	    buttonList.get(i).addActionListener(btnListenerList.get(i));
	}
    }
    
    private void disableButtons() {
	for(int i = 0; i < buttonList.size(); i++) {
	    buttonList.get(i).removeActionListener(btnListenerList.get(i));
	}
    }

    /**
     * @param title
     * @param gc
     */
    /*
    public MappedPlaces(String title, GraphicsConfiguration gc) {
	super(title, gc);
	// TODO Auto-generated constructor stub
    }
    */
    
    /**
     * Listener for menu item "New"
     * Load new map
     */
    class NewMapListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent aev) {
	    if (pnlMainPane.isModified()) {
		int dialogAnswer = JOptionPane.showConfirmDialog(MappedPlaces.this, 
							msgStrings.getString("strConfirmContinue")); 	//$NON-NLS-1$
		if (dialogAnswer != JOptionPane.OK_OPTION) {
		    return;
		}
	    }
//	    jfc.setLocale(new Locale("sv"));
	    jfc.resetChoosableFileFilters();
	    FileNameExtensionFilter fnef = 
		    new FileNameExtensionFilter(msgStrings.getString("strImageFiles"), 			//$NON-NLS-1$
			    "png", "jpg", "jpeg", "gif", "bmp", "wbmp"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	    jfc.addChoosableFileFilter(fnef);
	    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    jfc.setFileFilter(fnef);
	    jfc.setSelectedFile(new File(""));
	    int dlgResult = jfc.showOpenDialog(MappedPlaces.this);
	    if (dlgResult != JFileChooser.APPROVE_OPTION) {
		return;
	    }		
	    File imgfile = jfc.getSelectedFile();
	    // 2015-05-14 Changed from Image to BufferedImage
	    BufferedImage bgImage = null;
	    jfc.resetChoosableFileFilters();
	    try {
		bgImage = ImageIO.read(imgfile);
	    } catch (IOException e) {
		System.err.println(msgStrings.getString("errorMapLoad") + e.getMessage()); 		//$NON-NLS-1$
	    }
	    try {
	        remove(pnlMainPane);
	        pnlMainPane = new MapPanel(bgImage);
	        add(pnlMainPane, BorderLayout.CENTER);
	        lstmod.clear();
	        docfile = "";
            } catch (Exception e) {
	        // TODO Auto-generated catch block
        	System.err.println(e.getMessage());
	        e.printStackTrace();
            }
	    docfile = imgfile.getAbsolutePath();	    
	    lblStatus.setText(msgStrings.getString("msgStatusMapLoaded") + docfile + "\""); 		//$NON-NLS-1$
	    docfile = docfile.substring(0, docfile.lastIndexOf(".")) + ".kart";
	    
	    enableButtons();
//	    btnSearch.setEnabled(true);
//	    btnHide.setEnabled(true);
//	    Dimension t = pnlTopPane.getPreferredSize();
//	    Dimension e = pnlEastPane.getPreferredSize();
//	    Dimension c = pnlMainPane.getPreferredSize();
//	    setMaximumSize(new Dimension(c.width + e.width, t.height + c.height));
	    pack();
	    validate();
	    repaint();
	}
    }

    /**
     * @author zeron
     *
     */
    class OpenListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent aev) {
	    if (pnlMainPane.isModified()) {
		int dialogAnswer = JOptionPane.showConfirmDialog(MappedPlaces.this, 
							msgStrings.getString("strConfirmContinue")); 	//$NON-NLS-1$
		if (dialogAnswer != JOptionPane.OK_OPTION) {
		    return;
		}		    
	    }
//	    jfc.setLocale(currentLocale);
	    jfc.resetChoosableFileFilters();
	    FileNameExtensionFilter fnef = 
		    new FileNameExtensionFilter(msgStrings.getString("strMappedFiles"), "kart"); //$NON-NLS-1$ //$NON-NLS-2$
	    jfc.addChoosableFileFilter(fnef);
	    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    jfc.setFileFilter(fnef);
	    int dlgResult = jfc.showOpenDialog(MappedPlaces.this);
	    if (dlgResult != JFileChooser.APPROVE_OPTION) {
		return;
	    }		
	    File file = jfc.getSelectedFile();
	    docfile = file.getAbsolutePath();
	    jfc.resetChoosableFileFilters();
	    try {
		remove(pnlMainPane);
		lstmod.clear();
		pnlMainPane = new MapPanel(file);
	        add(pnlMainPane, BorderLayout.CENTER);	        
	        for(Component comp : pnlMainPane.getComponents()) {
	            comp.addMouseListener(pAdapt);
	        }		    
            } catch (ClassNotFoundException cnfe) {
	        // TODO Handle exception
	        cnfe.printStackTrace();
            } catch (FileNotFoundException fnfe) {
	        // TODO Handle exception
	        fnfe.printStackTrace();
            } catch (IOException ioe) {
	        // TODO Handle exception
	        ioe.printStackTrace();
            }
	    for (PlaceCategory c : pnlMainPane.getCategories()) {
		lstmod.addElement(c);
	    }
	    if (!(miSave.isEnabled() && miSaveAs.isEnabled())) {
		miSave.setEnabled(true);
		miSaveAs.setEnabled(true);
	    }
//	    if (!btnSearch.isEnabled()) {
//		btnSearch.setEnabled(true);
//	    }
	    lblStatus.setText(msgStrings.getString("msgStatusOpened") + 
		    docfile.substring(docfile.lastIndexOf(File.separator)+1) + 
		    msgStrings.getString("msgStatusAt") + 
		    docfile.substring(0, docfile.lastIndexOf(File.separator) + 1) + "\"");
	    enableButtons();
//	    Dimension t = pnlTopPane.getPreferredSize();
//	    Dimension e = pnlEastPane.getPreferredSize();
//	    Dimension c = pnlMainPane.getPreferredSize();
//	    Dimension s = pnlSouthPane.getPreferredSize();
//	    setMaximumSize(new Dimension(c.width + e.width, t.height + c.height + s.height));
	    pack();
	    validate();
	    repaint();
	}
    }

    /**
     * @author zeron
     *
     */
    class SaveListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent aev) {
	    if (!pnlMainPane.hasMapImage()) {
		JOptionPane.showMessageDialog(MappedPlaces.this, msgStrings.getString("msgNothingToSave"), 	//$NON-NLS-1$
			msgStrings.getString("msgNoMapTitle"), JOptionPane.ERROR_MESSAGE); 			//$NON-NLS-1$
		return;
	    }
	    JMenuItem mi = (JMenuItem) aev.getSource();
	    if (mi.getText() == msgStrings.getString("miSaveAs") || docfile.equalsIgnoreCase("")) {	//$NON-NLS-1$ //$NON-NLS-2$
//		jfc.setLocale(currentLocale);
		jfc.resetChoosableFileFilters();
		FileNameExtensionFilter fnef = 
			new FileNameExtensionFilter(msgStrings.getString("strMappedFiles"), "kart"); 	//$NON-NLS-1$ //$NON-NLS-2$
		jfc.addChoosableFileFilter(fnef);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setFileFilter(fnef);
		int dlgResult = jfc.showSaveDialog(MappedPlaces.this);
		if (dlgResult != JFileChooser.APPROVE_OPTION)
		    return;
		File file = jfc.getSelectedFile();
		docfile = file.getAbsolutePath();
		if (!docfile.endsWith(".kart")) {
		    docfile += ".kart";
		}
		jfc.resetChoosableFileFilters();
	    }
	    try {
		for(Component comp : pnlMainPane.getComponents())
		    comp.removeMouseListener(pAdapt);
	        pnlMainPane.saveData(docfile);
	        for(Component comp : pnlMainPane.getComponents())
		    comp.addMouseListener(pAdapt);
	    } catch (FileNotFoundException fnfe) {
		// TODO Handle exception
		fnfe.printStackTrace();
            } catch (IOException ioe) {
	        // TODO Handle exception
	        ioe.printStackTrace();
            }
	    lblStatus.setText(msgStrings.getString("msgStatusSaved") + 
		    docfile.substring(docfile.lastIndexOf(File.separator)+1) + 
		    msgStrings.getString("msgStatusAt") + 
		    docfile.substring(0, docfile.lastIndexOf(File.separator)+1) + "\"");
	}
    }
    
    /**
     * Listener for exit menu item and exit button
     * Exits program if document hasn't been modified
     * otherwise asks for confirmation first
     */
    class ExitListener extends WindowAdapter implements ActionListener {
	
	private void exitProgram(){
	    if (pnlMainPane.isModified()) {
		int dialogAnswer = JOptionPane.showConfirmDialog(MappedPlaces.this, 
							msgStrings.getString("strConfirmExit")); 	//$NON-NLS-1$
		if (dialogAnswer == JOptionPane.OK_OPTION) {
		    System.exit(0);
		}		    
	    } else {
		System.exit(0);
	    }
	}
	
	public void actionPerformed(ActionEvent aev){
	    exitProgram();
	}
	
	public void windowClosing(WindowEvent wev){
	    exitProgram();
	}
    }
    
    /**
     * Listener for creating new place markers on the map
     * Changes the cursor and prepares for creation of a new place marker
     */
    public class NewPlaceListener implements ActionListener {
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (pnlMainPane.hasMapImage()) {
		pnlMainPane.addMouseListener(mAdapt);
		pnlMainPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		lblStatus.setText(msgStrings.getString("msgStatusCreatePlace"));
	    }
	}
    }
    
    /**
     * Listener for creating new place markers on the map
     * Creates new place markers and restores normal cursor
     */
    class CreatePlaceListener extends MouseAdapter {
	
	@Override
	public void mouseClicked(MouseEvent mev) {
	    // determine click position
	    int x = mev.getX();
	    int y = mev.getY();
	    
	    PlacePosition newPos = new PlacePosition(x, y);

	    // determine chosen placeType: read the combobox choice
	    String placeType = (String)cbxPlaceTypes.getSelectedItem();
	    
	    // determine chosen category
	    PlaceCategory currentCat = null;
	    // read the JList choice
	    // if an element is selected - find right category
	    if (lstCategories.getSelectedIndex() != -1) {
		currentCat = lstCategories.getSelectedValue();
	    }
	    
	    // create place of chosen type and category
	    Place newPlace;
	    if (placeType.equalsIgnoreCase(msgStrings.getString("cbxNamedPlaces"))) { 			//$NON-NLS-1$
		NamedPlaceAddForm inputFrm = new NamedPlaceAddForm();
		int dlgResult = JOptionPane.showConfirmDialog(MappedPlaces.this, inputFrm, 
						msgStrings.getString("strNewNamedPlace"), 		//$NON-NLS-1$
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); 
		if (dlgResult == JOptionPane.OK_OPTION) {
		    String newPlaceName = inputFrm.getName();
		    if (newPlaceName == "") {   							//$NON-NLS-1$
			JOptionPane.showMessageDialog(MappedPlaces.this, 
	    			msgStrings.getString("msgNewPlaceErr"),  				//$NON-NLS-1$
	    			msgStrings.getString("dlgPlaceErrTitle"), JOptionPane.ERROR_MESSAGE); 	//$NON-NLS-1$
			return;
		    }
		    // if a category is selected
		    if (currentCat != null) {
			// Create Named Place of selected category
			newPlace = new NamedPlace(newPlaceName, newPos, currentCat);			
		    } else {
			// otherwise create Named Place without category
			newPlace = new NamedPlace(newPlaceName, newPos);
		    }
		    pnlMainPane.addPlace(newPos, newPlaceName, newPlace);
		    lblStatus.setText(msgStrings.getString("msgStatusCreated1") 			//$NON-NLS-1$
			    + newPlace.getName() + msgStrings.getString("msgStatusCreated2"));		//$NON-NLS-1$
		    newPlace.addMouseListener(pAdapt);
		}
	    } else if (placeType.equalsIgnoreCase(msgStrings.getString("cbxDescribedPlaces"))) { 	//$NON-NLS-1$
		DescribedPlaceAddForm inputFrm = new DescribedPlaceAddForm();
		int dlgResult = JOptionPane.showConfirmDialog(MappedPlaces.this, inputFrm, 
						msgStrings.getString("strNewDescribedPlace"), 		//$NON-NLS-1$
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); 
		if (dlgResult == JOptionPane.OK_OPTION) {
		    String newPlaceName = inputFrm.getName();
		    String newPlaceDescription = inputFrm.getDescription();
		    if (newPlaceName == "") {   							//$NON-NLS-1$
			JOptionPane.showMessageDialog(MappedPlaces.this, 
	    			msgStrings.getString("msgNewPlaceErr"),  				//$NON-NLS-1$
	    			msgStrings.getString("dlgPlaceErrTitle"), JOptionPane.ERROR_MESSAGE); 	//$NON-NLS-1$
			return;
		    }
		    if (newPlaceDescription == "") {   							//$NON-NLS-1$
			JOptionPane.showMessageDialog(MappedPlaces.this, 
	    			msgStrings.getString("msgNewMissingDescription"),  			//$NON-NLS-1$
	    			msgStrings.getString("dlgPlaceErrTitle"), JOptionPane.ERROR_MESSAGE); 	//$NON-NLS-1$
			return;
		    }
		    // if a category is selected
		    if (currentCat != null) {
			// Create Described Place of selected category
			newPlace = new DescribedPlace(newPlaceName, new PlacePosition(x, y), currentCat, newPlaceDescription);						
		    } else {
			// otherwise create Described Place without category
			newPlace = new DescribedPlace(newPlaceName, new PlacePosition(x, y), newPlaceDescription);
		    }		    
		    pnlMainPane.addPlace(newPos, newPlaceName, newPlace);
		    lblStatus.setText(msgStrings.getString("msgStatusCreated1")				//$NON-NLS-1$
			    + newPlaceName + msgStrings.getString("msgStatusCreated2"));		//$NON-NLS-1$
		    newPlace.addMouseListener(pAdapt);
		}
	    } else {
		JOptionPane.showMessageDialog(MappedPlaces.this, 
			msgStrings.getString("msgNoPlaceType"), 					//$NON-NLS-1$
			msgStrings.getString("msgError"), JOptionPane.ERROR_MESSAGE);  			//$NON-NLS-1$
	    }	    
	    if (!(miSave.isEnabled() && miSaveAs.isEnabled())) {
		miSave.setEnabled(true);
		miSaveAs.setEnabled(true);
	    }
	    
	    pnlMainPane.removeMouseListener(mAdapt);
	    pnlMainPane.setCursor(Cursor.getDefaultCursor());
	    pnlMainPane.revalidate();
	    pnlMainPane.repaint();
	}
    }

    /**
     * Listener for the search field
     * Changes the field's text when it gets or looses focus
     */
    class SearchFieldFocusListener extends FocusAdapter {

	@Override
	public void focusGained(FocusEvent fev) {
	    if (tfSearch.getText().equals(msgStrings.getString("strSearch"))) { 			//$NON-NLS-1$
		tfSearch.setForeground(Color.BLACK);
		tfSearch.setText(""); 									//$NON-NLS-1$
	    }
	}

	@Override
	public void focusLost(FocusEvent fev) {
	    if (tfSearch.getText().equals("")) { 							//$NON-NLS-1$
		tfSearch.setForeground(Color.GRAY);
		tfSearch.setText(msgStrings.getString("strSearch")); 					//$NON-NLS-1$
	    }
	}
    }

    /**
     * @author zeron
     *
     */
    class SearchListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    String name = tfSearch.getText();
	    	    
	    if(!pnlMainPane.hasMapImage()) {
		return;
	    }
	    if(name.equals("") || name.equals(msgStrings.getString("strSearch"))) {		//$NON-NLS-1$ //$NON-NLS-2$
		lblStatus.setText(msgStrings.getString("msgStatusWriteName"));			//$NON-NLS-1$
		return;
	    }
	    
	    String status = msgStrings.getString("msgStatusSearching") + name;			//$NON-NLS-1$
//	    lblStatus.setText("Searching: " + name);

	    ArrayList<Place> list = new ArrayList<Place>(pnlMainPane.getPlacesByName(name));
	    pnlMainPane.setSelectedPlaces(list);
	    if(list.isEmpty()) {
		status += " ... " + msgStrings.getString("msgStatusNoSuchPlaces");		//$NON-NLS-1$ //$NON-NLS-2$
	    } else {
		status += " ... " + msgStrings.getString("msgStatusFound") 			//$NON-NLS-1$ //$NON-NLS-2$
			+ " " + list.size() + ".";						//$NON-NLS-1$ //$NON-NLS-2$
	    }
	    lblStatus.setText(status);
	    pnlMainPane.repaint();
	}
    }

    /**
     * @author zeron
     *
     */
    class HidePlacesListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    Collection<Place> places = pnlMainPane.getSelectedPlaces();
	    pnlMainPane.hidePlaces(places);
	    pnlMainPane.repaint();
	}
    }

    /**
     * @author zeron
     *
     */
    class DeletePlacesListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    Collection<Place> places = pnlMainPane.getSelectedPlaces();
	    pnlMainPane.removePlaces(places);
//	    pnlMainPane.removeSelected();
//	    pnlMainPane.revalidate();
	    pnlMainPane.repaint();
	}

    }

    /**
     * @author zeron
     *
     */
    class WhatHereListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    // TODO Auto-generated method stub
	}
    }
    
    /**
     * @author zeron
     *
     */
    class CategorySelectionListener implements ListSelectionListener {

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
	    // TODO Auto-generated method stub
	    PlaceCategory selectedCategory = lstCategories.getSelectedValue();
	    System.out.println("Selected category: " + selectedCategory == null ? "null" : selectedCategory);
	    if (selectedCategory != null) {
		Collection<Place> places = pnlMainPane.getPlacesByCategory(selectedCategory);
		pnlMainPane.setSelectedPlaces(places);
		pnlMainPane.repaint();
	    }
	}
    }

    /**
     * @author zeron
     *
     */
    class HideCategoryListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    // TODO Auto-generated method stub
	    PlaceCategory selectedCategory = lstCategories.getSelectedValue();
	    if (selectedCategory != null) {
		Collection<Place> places = pnlMainPane.getPlacesByCategory(selectedCategory);
		pnlMainPane.hidePlaces(places);
		pnlMainPane.repaint();
	    }
	}
    }


    /**
     * @author zeron
     *
     */
    class NewCategoryListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    // If there is no map present categories cannot be created
	    if (!pnlMainPane.hasMapImage()) {
		JOptionPane.showMessageDialog(MappedPlaces.this, msgStrings.getString("msgMapMissing"), //$NON-NLS-1$
			msgStrings.getString("msgNoMapTitle"), JOptionPane.ERROR_MESSAGE); 		//$NON-NLS-1$
		return;
	    }
	    
	    JPanel main = new JPanel(new BorderLayout());
	    JPanel top = new JPanel();
	    JTextField tfCatName = new JTextField(20);
	    JLabel label1 = new JLabel(msgStrings.getString("dlgCatName")); 				//$NON-NLS-1$
	    top.add(label1);
	    top.add(tfCatName);
	    main.add(top, BorderLayout.NORTH);
	    main.add(jcc, BorderLayout.CENTER);

	    int dlgResult = JOptionPane.showConfirmDialog(MappedPlaces.this, main, 
		    		msgStrings.getString("dlgNewCat"), JOptionPane.OK_CANCEL_OPTION, 	//$NON-NLS-1$
		    		JOptionPane.PLAIN_MESSAGE);		    
	    if (dlgResult == JOptionPane.OK_OPTION) {
		String catName = tfCatName.getText();
		if (catName.equals("")) { 								//$NON-NLS-1$
		    JOptionPane.showMessageDialog(MappedPlaces.this, 
			    	msgStrings.getString("msgNewCatErr"),  					//$NON-NLS-1$
			    	msgStrings.getString("dlgCatErrTitle"), JOptionPane.ERROR_MESSAGE); 	//$NON-NLS-1$
		    return;
		} else {
		    Color newCol = jcc.getColor();
		    PlaceCategory newCat = new PlaceCategory(catName, newCol);
		    lstmod.addElement(newCat);
		    
		    if (!pnlMainPane.addCategory(newCat)) {
			JOptionPane.showMessageDialog(MappedPlaces.this, 
							msgStrings.getString("msgNewCatErr"),		//$NON-NLS-1$
							msgStrings.getString("msgDlgTitleErr"),		//$NON-NLS-1$ 
							JOptionPane.ERROR_MESSAGE);
		    }
		    lblStatus.setText(msgStrings.getString("msgStatusCategory") + newCat.getName() + 		//$NON-NLS-1$
			    		msgStrings.getString("msgStatusWithColor") + newCat.getColor() + 	//$NON-NLS-1$
			    		msgStrings.getString("msgStatusCreated"));				//$NON-NLS-1$
		}
		if (!(miSave.isEnabled() && miSaveAs.isEnabled())) {
		    miSave.setEnabled(true);
		    miSaveAs.setEnabled(true);
		}
	    }
	}
    }

    /**
     * @author zeron
     *
     */
    class DeleteCategoryListener implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    // TODO Auto-generated method stub
	    PlaceCategory selectedCategory = lstCategories.getSelectedValue();
	    if (selectedCategory != null) {
		pnlMainPane.removeCategory(selectedCategory);
		lstmod.removeElement(selectedCategory);
		pnlMainPane.repaint();
	    }
	}
    }

    /**
     * @author zeron
     *
     */
    class PlaceMouseListener extends MouseAdapter {
	
	@Override
	public void mouseClicked(MouseEvent mev) {
	    // TODO Adjust unfolded place position
//	    System.out.println(mev.getSource());
	    if (mev.getSource() instanceof Place) {
		Place p = (Place) mev.getSource();
		if (mev.getButton() == MouseEvent.BUTTON1) {
		    if (p.isSelected()) {
//			p.setSelected(false);
			pnlMainPane.setSelectedPlace(p, false);
		    } else {
//			p.setSelected(true);
			pnlMainPane.setSelectedPlace(p, true);
		    }
		} else if (mev.getButton() == MouseEvent.BUTTON3) {
		    String status = "Right button clicked: " + p.getPosition().toString();
		    Dimension d = p.getParent().getSize();
		    status += " of " + d.width + " and " + d.height; 
		    lblStatus.setText(status);
		    if (p.isFolded()) {
			p.setFolded(false);
		    } else {
			p.setFolded(true);
		    }
		}
//		System.out.println(source.toString());
	    }
	    pnlMainPane.validate();
	    pnlMainPane.repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent mev) {
	    Place p = (Place) mev.getSource();
	    String statustxt = "Name: " + p.getName();
	    if (p.hasCategory()) {
		statustxt += ", Category: " + p.getCategory().getName();
	    }
	    if (p.isSelected()) {
		statustxt += ", Selected";
	    } else {
		statustxt += ", Unselected";
	    }
	    if (p.isFolded()) {
		statustxt += ", Folded";
	    } else {
		statustxt += ", Unfolded";
	    }
	    statustxt += ", Position: " + p.getPosition();
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
	Locale.setDefault(currentLocale);
	new MappedPlaces(msgStrings.getString("MainTitle")); 					//$NON-NLS-1$
    }
}
