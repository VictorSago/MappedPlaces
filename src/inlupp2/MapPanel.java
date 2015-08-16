/**
 * Inlämningsuppgift 2 i PROG2: 
 * 
 * 	Mapped Places
 *
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */

package inlupp2;

import inlupp2.places.Category;
import inlupp2.places.Place;
import inlupp2.places.Position;

import java.awt.Dimension;
import java.awt.Graphics;
//import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
//import java.io.InputStream;
import java.io.ObjectOutputStream;
//import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
//import java.awt.LayoutManager;
//import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * <code>public class MapPanel<code><br>
 * <code>extends JPanel<code><br>
 * <code>implements Serializable<code><br><br>
 * This class provides a panel to hold a map image and all the data structures necessary 
 * to keep track of the place markers.
 * @author Victor Sago, <a href="mailto:VictorSago01@gmail.com">VictorSago01@gmail.com</a>
 */
public class MapPanel extends JPanel implements Serializable {

    private static final long serialVersionUID = 5181809308560213438L;

    private BufferedImage mapImage;
    private boolean modified;

    private Map<Position, Place> 		placesByPosition;
    private Map<String, List<Place>> 		placesByName;
    private Map<Category, List<Place>> 	placesByCategory;    
    private Set<Place> 				selectedPlaces;
    
    /**
     * Empty constructor is needed so that the panel can be initialized without an image.
     */
    public MapPanel() {
	super();
	mapImage = null;
	modified = false;
    }

    /**
     * Constructor for the MapPanel class.<br>
     * Creates a new MapPanel, Initializes it with a map image and 
     * creates all the data structures needed to keep track of place markers.
     * @param newMapImage The map image to be displayed in this panel.
     */
    public MapPanel(BufferedImage newMapImage) {
	this.setLayout(null);
	mapImage = newMapImage;
	if (mapImage.getWidth(this) < 0)
	    throw new IllegalArgumentException("Can't open the image file!");
	modified = false;
	placesByPosition = new HashMap<Position, Place>();
	placesByName = new HashMap<String, List<Place>>();
	placesByCategory = new HashMap<Category, List<Place>>();
	selectedPlaces = new HashSet<Place>();
	// set preferredSize to make the panel resize itself
	setPreferredSize(new Dimension(mapImage.getWidth(this), mapImage.getHeight(this)));
    }

    /**
     * Constructor for the MapPanel class.<br>
     * Initializes a new MapPanel from a saved file document.
     *  
     * @param file Previously saved document containing all the data 
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public MapPanel(File file) throws ClassNotFoundException, FileNotFoundException, IOException {
	super();
	this.setLayout(null);
	loadData(file);
    }
    
    protected void paintComponent(Graphics g){
	super.paintComponent(g);
	if (mapImage != null) {
	    setSize(new Dimension(mapImage.getWidth(this), mapImage.getHeight(this)));
	    g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    /**
     * Tests if this <code>MapPanel<code> contains a map image.
     * @return True if this <code>MapPanel<code> contains a map image
     */
    public boolean hasMapImage() {
	if (mapImage != null)
	    return true;
	else
	    return false;
    }
    
    /**
     * Tests if this <code>MapPanel<code> has been modified.
     * @return True if this <code>MapPanel<code> has been modified
     */
    public boolean isModified() {
	return modified;
    }

    /**
     * Sets the <code>modified<code> flag for this <code>MapPanel<code>.
     * @param state The new state of the <code>modified<code> flag for this <code>MapPanel<code>
     */
    public void setModified(boolean state) {
	modified = state;
    }

    /**
     * Returns a list of all the categories in this <code>MapPanel<code>.
     * @return All categories as list
     */
    public Collection<Category> getCategories() {
	return new ArrayList<>(placesByCategory.keySet());
    }    
    
    /**
     * Returns a reference to a <code>Category<code> object that corresponds to the name given as a parameter.
     * @param catName The name of the <code>Category<code> to be found
     * @return A reference to the <code>Category<code> with the name <code>catName<code>
     */
    public Category getCategoryByName(String catName) {
	for (Category cat : placesByCategory.keySet()) {
	    if (cat.getName().equalsIgnoreCase(catName)) {
		return cat;
	    }
	}
	return null;
    }    
    
    /**
     * Adds a new category if it doesn't already exist. The existence is tested with <code>hashCode()<code> method of the
     *  Category class. This method will add a new category if it differs in any way from all existing categories.
     * @param newcat Category to be added
     * @return True if the new category has been added, false otherwise
     */
/*    public boolean addCategory(Category newcat) {
	boolean mod = true;
	if (placesByCategory.containsKey(newcat)) {
	    mod = false;
	} else {
	    placesByCategory.put(newcat, new ArrayList<>());
	}
    
	modified = mod || modified;
	return mod;
    } */
    
    /**
     * Adds a new category if it doesn't already exist. The existence is tested with <code>equals(Object o)<code> method
     *  of the Category class, which returns <code>true<code> if either the name or the color of the new category
     *  is the same as in the category being tested. So this method adds a new category only if no category with either 
     *  the same name or the same color already exists. If, instead, it is preferable that a new category be added if 
     *  either its name or its color differs from all existing then the line 
     *  <code>List<Category> categories = new ArrayList<>(placesByCategory.keySet());<code> can be commented out
     *  and the condition in the if statement should be changed to <code>placesByCategory.containsKey(newcat)<code>.
     * @param newcat Category to be added
     * @return True if the new category has been added, false otherwise
     */
    public boolean addCategory(Category newcat) {
	boolean mod = true;
	List<Category> categories = new ArrayList<>(placesByCategory.keySet());
	if (categories.contains(newcat)) {
	    mod = false;
	} else {
	    placesByCategory.put(newcat, new ArrayList<>());
	}
	    
	modified = mod || modified;
	return mod;
    }

    /**
     * Removes a category and all places belonging to it.
     * @param cat Category to be removed
     * @return True if the operation was successful
     */
    public boolean removeCategory(Category cat) {
	boolean mod = false;	
	if (placesByCategory.containsKey(cat)) {
	    List<Place> places = placesByCategory.remove(cat);
	    mod = true;
	    if (places != null) {
		for (Place p : places) {
		    Position pos = p.getPosition();
		    String name = p.getName();
		    placesByPosition.remove(pos);
		    placesByName.get(name).remove(p);
		    if (placesByName.get(name).isEmpty()) {
			placesByName.remove(name);
		    }
		    this.remove(p);
		}
	    }
	} 
	modified = mod || modified;
	return mod;
    }
    
    /**
     * Returns the Place at the specified position.
     * @param pos Position The position to be tested
     * @return Place A place corresponding to position pos or null if no such place exists
     */
    public Place getPlaceByPosition(Position pos) {
	if (placesByPosition.containsKey(pos))
	    return placesByPosition.get(pos);
	else
	    return null;
    }
        
    /**
     * Returns a list of all places with the specified name.
     * @param name The name of the place(s)
     * @return Copy of the list of all places with this name or an empty list if no such places exist
     */
    public Collection<Place> getPlacesByName(String name) {
	Collection<Place> ret;
	if (placesByName.containsKey(name)) {
	    ret = new ArrayList<Place>(placesByName.get(name));
	} else {
	    ret = new ArrayList<Place>();
	}
	return ret;
    }
    
    /**
     * Returns a list of all places of the specified category.
     * @param cat Category The category
     * @return Copy of the list of all places in this category or an empty list if no such places exist
     */
    public Collection<Place> getPlacesByCategory(Category cat) {
	Collection<Place> ret;
	if (placesByCategory.containsKey(cat)) {
	    ret = new ArrayList<Place>(placesByCategory.get(cat));
	} else {
	    ret = new ArrayList<Place>();
	}
	return ret;
    }

    /**
     * Adds a new place.
     * @param place The place to be added
     */
    public void addPlace(Place place) {
	this.addPlace(place.getPosition(), place.getName(), place);
    }
    
    /**
     * Adds a new place.
     * @param pos The position of the new place
     * @param name The name of the new place
     * @param place The reference to the new place
     */
    public void addPlace(Position pos, String name, Place place) {
	placesByPosition.put(pos, place);
	if (placesByName.containsKey(name)) {		// If places with this name already exist
	    placesByName.get(name).add(place);		// Add the new place to the list of places with the same name
	} else {					// Otherwise
	    List<Place> l = new ArrayList<Place>();	// Create a new list corresponding to this name
	    l.add(place);				// 	add the reference to the new place to this list
	    placesByName.put(name, l);			// 	and create a new key/value pair in the corresponding data structure
	}
	if (place.hasCategory()) {			// If the new place is categorized
	    Category cat = place.getCategory();
	    if (placesByCategory.containsKey(cat)) {	//	and if this category already exists
		placesByCategory.get(cat).add(place);	// Add a reference to the new place to the list corresponding to this category
	    } else {					// If this category doesn't exist
		List<Place> l = new ArrayList<Place>();	// Create a new list corresponding to this category
		l.add(place);				// 	add the reference to the new place to this list
		placesByCategory.put(cat, l);		// 	and create a new key/value pair in the corresponding data structure
	    }
	}
	modified = true;
	this.add(place);
    }
    
    /**
     * Removes all the places in the collection sent as argument.
     * @param places A collection of place references
     */
    public void removePlaces(Collection<Place> places) {
	Iterator<Place> iter = places.iterator();
	while (iter.hasNext()) {
	    Place p = iter.next();				// For each place in the collection
	    Position pos = p.getPosition();		//	find its position,
	    String name = p.getName();				//	its name and
	    Category cat = p.getCategory();		//	its category
	    placesByPosition.remove(pos);			// Remove the place from the Position map
	    List<Place> list1 = placesByName.get(name);		//	and from the Name map
	    if (list1 != null) {
		list1.remove(p);
		if (list1.isEmpty()) {				// If this was the last place with this name
		    placesByName.remove(name);			// 	remove the name
		}
	    }
	    if (cat != null) {					// If the place was categorized
		List<Place> list2 = placesByCategory.get(cat);
		list2.remove(p);				// 	remove it from the category map
	    }
	    this.remove(p);					// Remove the place element from this MapPanel
	    modified = true;
	}
	selectedPlaces.removeAll(places);
	revalidate();
    }
    
    /**
     * Hides all the places in a collection by making corresponding <code>JComponent<code> invisible.<br>
     * Deselects each place in the collection to prevent accidental removing.
     * @param places Collection of places to hide
     */
    public void hidePlaces(Collection<Place> places) {
	Iterator<Place> iter = places.iterator();
	while (iter.hasNext()) {
	    Place p = iter.next();
	    if (p.isSelected()) {
		p.setSelected(false);		
	    }
	    p.setVisible(false);
	}
	selectedPlaces.removeAll(places);
	modified = true;
	revalidate();
    }

    /**
     * Returns a copy of the collection of all selected places.
     * @return A <code>HashSet<code> copy of the set of all selected places
     */
    public Collection<Place> getSelectedPlaces() {
        return new HashSet<Place>(selectedPlaces);
    }

    /**
     * Makes a place selected or unselected depending on the second parameter to the method.
     * @param place The place to be selected or unselected
     * @param sel If <code>true<code> make the place selected, if <code>false<code> make it unselected
     */
    public void setSelectedPlace(Place place, boolean sel) {
	place.setSelected(sel);
	if (sel) {
	    selectedPlaces.add(place);
	    place.setVisible(sel);
	}
	else {
	    selectedPlaces.remove(place);
	}
	modified = true;
    }
    
    /**
     * Selects a collection of places in a batch.
     * @param placelist The collection of places to make selected
     */
    public void setSelectedPlaces(Collection<Place> placelist) {
	Iterator<Place> iter;
	if (!selectedPlaces.isEmpty()) {		// First unselect any places that are selected
	    iter = selectedPlaces.iterator();
	    while (iter.hasNext()) {
		Place p = iter.next();
		p.setSelected(false);
	    }
	    selectedPlaces.clear();
	}
	if (placelist != null) {			// Second make all places in the collection selected and visible
	    iter = placelist.iterator();
	    while (iter.hasNext()) {
		Place p = iter.next();
		p.setVisible(true);
		p.setSelected(true);
		selectedPlaces.add(p);
	    }
	}
	modified = true;
    }
    

    /**
     * Saves all the data in this <code>MapPanel<code>, including the map image and all the data structures holding the 
     *  place objects with their state, to a file.
     * @param filePath The path of the file where the data is to be saved
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveData(String filePath) throws FileNotFoundException, IOException {
	// Open file stream for saving
	FileOutputStream fos = new FileOutputStream(filePath);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	// Save mapImage
	ImageIO.write(mapImage, "jpg", oos);
	// Save Places
	oos.writeObject(placesByPosition);
	oos.writeObject(placesByName);
	oos.writeObject(placesByCategory);
	oos.writeObject(selectedPlaces);
	// Close streams
	oos.close();
	fos.close();
	// Set modified to false
	this.modified = false;
    }
    
    /**
     * Saves all the data in this <code>MapPanel<code>, including the map image and all the data structures holding the
     *  place objects with their state, to a file.
     * @param docfile The reference to the file where the data is to be saved
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveData(File docfile) throws ClassNotFoundException, FileNotFoundException, IOException {
	// Open file stream for saving
	FileOutputStream fos = new FileOutputStream(docfile);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	// Save mapImage
	ImageIO.write(mapImage, "jpg", oos);
	// Save Places
	oos.writeObject(placesByPosition);
	oos.writeObject(placesByName);
	oos.writeObject(placesByCategory);
	oos.writeObject(selectedPlaces);
	// Close streams
	oos.close();
	fos.close();
	// Set modified to false
	this.modified = false;
    }
    
    /**
     * Loads all the data from a file into this <code>MapPanel<code>, including the map image and 
     *  all the data structures holding the place objects with their state.
     * @param filePath The path of the file from which the data is to be loaded
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void loadData(String filePath) throws ClassNotFoundException, FileNotFoundException, IOException {
	// Open file stream for loading
	FileInputStream fis = new FileInputStream(filePath);
	ObjectInputStream ois = new ObjectInputStream(fis);
	// Load mapImage
	this.mapImage = ImageIO.read(ois);
	if (mapImage.getWidth(this) < 0)
	    throw new IllegalArgumentException("Can't load the map image from the file!");
	// Load Places
	this.placesByPosition = (HashMap<Position, Place>) ois.readObject();
	this.placesByName = (HashMap<String, List<Place>>) ois.readObject();
	this.placesByCategory = (HashMap<Category, List<Place>>) ois.readObject();
	this.selectedPlaces = (HashSet<Place>) ois.readObject();
	// Close streams
	ois.close();
	fis.close();
	// Set modified to false
	for (Place p : placesByPosition.values()) {
		this.add(p);
	    }
	this.modified = false;
	setPreferredSize(new Dimension(mapImage.getWidth(this), mapImage.getHeight(this)));
    }

    /**
     * Loads all the data from a file into this <code>MapPanel<code>, including the map image and 
     *  all the data structures holding the place objects with their state.
     * @param docfile The reference to the file from which the data is to be loaded
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void loadData(File docfile) throws ClassNotFoundException, FileNotFoundException, IOException {
	// Open file stream for loading
	FileInputStream fis = new FileInputStream(docfile);
	ObjectInputStream ois = new ObjectInputStream(fis);
	// Load mapImage
	this.mapImage = ImageIO.read(ois);
	if (mapImage.getWidth(this) < 0)
	    throw new IllegalArgumentException("Can't load the map image from the file!");
	// Load Places
	this.placesByPosition = (HashMap<Position, Place>) ois.readObject();
	this.placesByName = (HashMap<String, List<Place>>) ois.readObject();
	this.placesByCategory = (HashMap<Category, List<Place>>) ois.readObject();
	this.selectedPlaces = (HashSet<Place>) ois.readObject();
	// Close streams
	ois.close();
	fis.close();
	for (Place p : placesByPosition.values()) {
	    this.add(p);
	}
	// Set modified to false
	this.modified = false;
	setPreferredSize(new Dimension(mapImage.getWidth(this), mapImage.getHeight(this)));
    }

}
