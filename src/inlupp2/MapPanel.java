/**
 * 
 */
/**
 * @author Victor Sago
 *
 */

package inlupp2;

import inlupp2.places.PlaceCategory;
import inlupp2.places.Place;
import inlupp2.places.PlacePosition;

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

import javax.imageio.ImageIO;
//import java.awt.LayoutManager;
//import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MapPanel extends JPanel implements Serializable {
    
    private static final long serialVersionUID = 2L;

    // 2015-05-14 Changed from Image to BufferedImage
    private BufferedImage mapImage;
    private boolean modified;

    private HashMap<PlacePosition, Place> placesByPosition;
    private HashMap<String, List<Place>> placesByName;

    private ArrayList<PlaceCategory> categories;
    
    private HashSet<Place> selectedPlaces;
    
    /**
     * Empty constructor is needed so that the panel can be initialized without an image
     */
    public MapPanel() {
	// TODO Auto-generated constructor stub
	super();
	mapImage = null;
	modified = false;
    }

    // 2015-05-14 Changed from Image to BufferedImage
    public MapPanel(BufferedImage newMapImage) {
	this.setLayout(null);
	mapImage = newMapImage;
	if (mapImage.getWidth(this) < 0)
	    throw new IllegalArgumentException("Can't open the image file!");
	modified = false;
	placesByPosition = new HashMap<PlacePosition, Place>();
	placesByName = new HashMap<String, List<Place>>();
	categories = new ArrayList<PlaceCategory>();
	selectedPlaces = new HashSet<Place>();
	// set preferredSize to make the panel resize itself
	setPreferredSize(new Dimension(mapImage.getWidth(this), mapImage.getHeight(this)));
    }

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
    
    public boolean hasMapImage() {
	if (mapImage != null)
	    return true;
	else
	    return false;
    }
    
    public boolean isModified() {
	return modified;
    }

    public void setModified(boolean state) {
	modified = state;
    }

    public void addPlace(Place place) {
	// TODO
    }
    
    public void addPlace(PlacePosition pos, String name, Place place) {
	// TODO Ensure that no duplicate positions are added
	placesByPosition.put(pos, place);
	if (placesByName.containsKey(name)) {
	    placesByName.get(name).add(place);
	} else {
	    List<Place> l = new ArrayList<Place>();
	    l.add(place);
	    placesByName.put(name, l);
	}
	modified = true;
	this.add(place);
    }
    
    public void removePlace(Place pl) {
	String name = pl.getName();
	PlacePosition pos = pl.getPosition();
	List<Place> li = placesByName.get(name);
	Place first = placesByPosition.remove(pos);
	boolean second = li.remove(pl);
	String liststate = "non-empty ";
	if (li.isEmpty()) {
	    placesByName.remove(name);
	    liststate = "empty ";
	}
	boolean third = selectedPlaces.remove(pl);
	this.remove(pl);
	modified = true;
	System.out.println(first + " " + second + " " + liststate + third);
    }

    public Place removePlace(PlacePosition pos) {
	// TODO
	Place ret = placesByPosition.remove(pos);
	if (ret != null) {	    
	    modified = true;
	}
	return ret;
    }

    public void removeSelected() {
	Iterator<Place> iter = selectedPlaces.iterator();
	while (iter.hasNext()) {
	    Place p = iter.next();
	    String name = p.getName();
	    PlacePosition pos = p.getPosition();
	    List<Place> li = placesByName.get(name);
	    Place first = placesByPosition.remove(pos);
	    boolean second = li.remove(p);
	    String liststate = "non-empty;";
	    if (li.isEmpty()) {
		placesByName.remove(name);
		liststate = "empty;";
	    }
	    this.remove(p);
	    modified = true;
	    System.out.println(first + " " + second + " " + liststate);
	}
	selectedPlaces.clear();
	revalidate();
    }

    
    public boolean addCategory(PlaceCategory newcat) {
	boolean ret = categories.add(newcat);
	if (ret) {
	    modified = true;
	}
	return ret;
    }
    
    public boolean removeCategory(PlaceCategory cat) {
	// TODO
	boolean ret = categories.remove(cat);
	if (ret) {
	    modified = true;
	}
	return ret;
    }
    
    public PlaceCategory getCategoryByName(String catName) {
	// TODO
	for (PlaceCategory cat : categories) {
	    if (cat.getName().equalsIgnoreCase(catName)) {
		return cat;
	    }
	}
	return null;
    }
    

    /**
     * @return the categories
     */
    public ArrayList<PlaceCategory> getCategories() {
        return categories;
    }

    /**
     * @return the placesByPosition Map
     */
    public HashMap<PlacePosition, Place> getAllPlacesByPos() {
        return placesByPosition;
    }
    
    /**
     * @return the placesByName Map
     */    
    public HashMap<String, List<Place>> getAllPlacesByName() {
        return placesByName;
    }
    
    
    /**
     * @param name
     * @return
     */
    public List<Place> getPlacesByName(String name) {
	ArrayList<Place> ret;
	if (placesByName.containsKey(name)) {
	    ret = (ArrayList<Place>) placesByName.get(name);
	} else {
	    ret = new ArrayList<Place>();
	}
	return ret;
    }

    /**
     * @return the selectedPlaces
     */
    public Collection<Place> getSelectedPlaces() {
        return this.selectedPlaces;
    }

    public void setSelectedPlace(Place place, boolean sel) {
	place.setSelected(sel);
	if (sel) {
	    selectedPlaces.add(place);
	}
	else {
	    selectedPlaces.remove(place);
	}
	modified = true;
    }
    
    /**
     * @param placelist the selectedPlaces to set
     */
    public void setSelectedPlaces(Collection<Place> placelist) {
	Iterator<Place> iter;
	if (!selectedPlaces.isEmpty()) {
	    iter = selectedPlaces.iterator();
	    while (iter.hasNext()) {
		Place p = iter.next();
		p.setSelected(false);
	    }
	    selectedPlaces.clear();
	}
	if (placelist != null) {	
	    iter = placelist.iterator();
	    while (iter.hasNext()) {
		Place p = iter.next();
		p.setSelected(true);
		selectedPlaces.add(p);
	    }
	}
	modified = true;
    }
    
    /**
     * @param categories the categories to set
     */
    public void setCategories(ArrayList<PlaceCategory> categories) {
        this.categories = categories;
    }

    public void saveData(String filePath) throws FileNotFoundException, IOException {
	// Open file stream for saving
	FileOutputStream fos = new FileOutputStream(filePath);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	// Save mapImage
//	oos.writeObject(mapImage);
	ImageIO.write(mapImage, "jpg", oos);
	// Save categories
	oos.writeObject(categories);
	// Save positionedPlaces
	oos.writeObject(placesByPosition);
	oos.writeObject(placesByName);
	oos.writeObject(selectedPlaces);
	// Close streams
	oos.close();
	fos.close();
	// Set modified to false
	this.modified = false;
    }
    
    public void saveData(File docfile) throws ClassNotFoundException, FileNotFoundException, IOException {
	// Open file stream for saving
	FileOutputStream fos = new FileOutputStream(docfile);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	// Save mapImage
//	oos.writeObject(mapImage);
//	ImageIO.write(mapImage, "jpg", oos);
	// Save categories
	oos.writeObject(categories);
	// Save positionedPlaces
	oos.writeObject(placesByPosition);
	oos.writeObject(placesByName);
	oos.writeObject(selectedPlaces);
	// Close streams
	oos.close();
	fos.close();
	// Set modified to false
	this.modified = false;
	setPreferredSize(new Dimension(mapImage.getWidth(this), mapImage.getHeight(this)));
    }
    
    @SuppressWarnings("unchecked")
    public void loadData(String filePath) throws ClassNotFoundException, FileNotFoundException, IOException {
	// Open file stream for loading
	FileInputStream fis = new FileInputStream(filePath);
	ObjectInputStream ois = new ObjectInputStream(fis);
	// Load mapImage
//	mapImage = (Image)ois.readObject();
	this.mapImage = ImageIO.read(ois);
	if (mapImage.getWidth(this) < 0)
	    throw new IllegalArgumentException("Can't load the map image from the file!");
	// Load categories
	this.categories = (ArrayList<PlaceCategory>)ois.readObject();
	// Load positionedPlaces
	this.placesByPosition = (HashMap<PlacePosition, Place>) ois.readObject();
	this.placesByName = (HashMap<String, List<Place>>) ois.readObject();
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

    @SuppressWarnings("unchecked")
    public void loadData(File docfile) throws ClassNotFoundException, FileNotFoundException, IOException {
	// Open file stream for loading
	FileInputStream fis = new FileInputStream(docfile);
	ObjectInputStream ois = new ObjectInputStream(fis);
	// Load mapImage
//	mapImage = (Image)ois.readObject();
	this.mapImage = ImageIO.read(ois);
	// Load categories
	this.categories = (ArrayList<PlaceCategory>)ois.readObject();
	// Load positionedPlaces
	this.placesByPosition = (HashMap<PlacePosition, Place>) ois.readObject();
	this.placesByName = (HashMap<String, List<Place>>) ois.readObject();
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
