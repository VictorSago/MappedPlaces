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
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
//import java.awt.LayoutManager;
//import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MapPanel extends JPanel implements Serializable {
    
    private static final long serialVersionUID = 394224287468012096L;

    // 2015-05-14 Changed from Image to BufferedImage
    private BufferedImage mapImage;
    private boolean modified;

    private Map<PlacePosition, Place> 		placesByPosition;
    private Map<String, List<Place>> 		placesByName;
    private Map<PlaceCategory, List<Place>> 	placesByCategory;    
    private Set<Place> 				selectedPlaces;
    
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
	placesByCategory = new HashMap<PlaceCategory, List<Place>>();
//	categories = new ArrayList<PlaceCategory>();
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

    /**
     * @return all categories as list
     */
    public Collection<PlaceCategory> getCategories() {
	// Changed on 2015-08-09
//	return categories;
	return new ArrayList<>(placesByCategory.keySet());
    }    
    
    /**
     * @param categories the categories to set
     */
    public void setCategories(Collection<PlaceCategory> cats) {
	// TODO
	// Changed 2015-08-09
//        this.categories = new ArrayList<PlaceCategory>(cats);
	
        Iterator<PlaceCategory> iter = cats.iterator();
        while (iter.hasNext()) {
            placesByCategory.putIfAbsent(iter.next(), new ArrayList<Place>());
        }
    }

    /**
     * @param catName
     * @return
     */
    public PlaceCategory getCategoryByName(String catName) {
	// TODO
	// Changed 2015-08-09
//	for (PlaceCategory cat : categories) {
//	    if (cat.getName().equalsIgnoreCase(catName)) {
//		return cat;
//	    }
//	}
	for (PlaceCategory cat : placesByCategory.keySet()) {
	    if (cat.getName().equalsIgnoreCase(catName)) {
		return cat;
	    }
	}
	return null;
    }    
    
    /**
     * @param newcat
     * @return
     */
    public boolean addCategory(PlaceCategory newcat) {
	// Changed 2015-08-09
//	boolean mod1 = categories.add(newcat);
	boolean mod = true;
	if (placesByCategory.containsKey(newcat)) {
	    mod = false;
	} else {
	    placesByCategory.put(newcat, new ArrayList<>());
	}
	modified = mod || modified;
	return mod;
    }
    
    public boolean removeCategory(PlaceCategory cat) {
	// TODO
	// Changed 2015-08-09
//	boolean ret = categories.remove(cat);
	boolean mod = false;
//	System.out.println("Remove Category " + cat);
//	System.out.println("PlacesByPosition: " + placesByPosition);
//	System.out.println("PlacesByName: " + placesByName);
//	System.out.println("PlacesByCategory: " + placesByCategory);
//	System.out.println("SelectedPlaces: " + selectedPlaces);
//	System.out.println("Argument: " + cat);
	
	if (placesByCategory.containsKey(cat)) {
//	    System.out.println("\nPlacesByCategory contains key " + cat + ": true.");
	    List<Place> places = placesByCategory.remove(cat);
//	    placesByCategory.remove(cat);
//	    System.out.println("After placesByCategory.remove(cat)");
//	    System.out.println("PlacesByCategory: " + placesByCategory);
//	    System.out.println("List of Places " + places + " removed from placesByCategory");
	    mod = true;
	    if (places != null) {
//		System.out.println("places != null...");
//		mod = true;
		int i = 0;
		for (Place p : places) {
		    System.out.println("\nIteration " + i + ": place p=" + p);
		    PlacePosition pos = p.getPosition();
		    String name = p.getName();
//		    System.out.println("Position: " + pos + " Name: " + name);
		    placesByPosition.remove(pos);
//		    System.out.println("After PlacesByPosition.remove(" + pos + "): ");
//		    System.out.println("PlacesByPosition: " + placesByPosition);
//		    System.out.println("PlacesByName: " + placesByName);
//		    System.out.println("PlacesByCategory: " + placesByCategory);
//		    System.out.println("SelectedPlaces: " + selectedPlaces);
//		    System.out.println("placesByName.get(name): " + placesByName.get(name)!=null ? placesByName.get(name) : "placesByName.get(name) is Null000");

		    System.out.println("Remove operation from placesByName...1...");
		    placesByName.get(name).remove(p);
		    System.out.println("Remove operation from placesByName...2...");
		    if (placesByName.get(name).isEmpty()) {
			System.out.println("Remove operation from placesByName...3...");
			placesByName.remove(name);
			System.out.println("Remove operation from placesByName...4...");
		    }
		    System.out.println("Remove operation from placesByName...5...");
		    this.remove(p);
		    System.out.println("Remove operation from placesByName...6...");
		    i++;
		    System.out.println("Remove operation from placesByName...7...");
		}
	    }
//	    modified = true;
	} 
//	else {
//	    modified = false;
//	}
	modified = mod || modified;
	return mod;
    }

    /**
     * @return the placesByPosition Map
     */
//    public Map<PlacePosition, Place> getAllPlacesByPos() {
//        return placesByPosition;
//    }
    
    /**
     * @return the placesByName Map
     */    
//    public Map<String, List<Place>> getAllPlacesByName() {
//        return placesByName;
//    }
    
    
    /**
     * @param pos PlacePosition
     * @return Place
     * Returns Place corresponding to position pos or null if no such place exists
     * At the moment unused
     */
    public Place getPlaceByPosition(PlacePosition pos) {
	// TODO unused at the moment
	if (placesByPosition.containsKey(pos))
	    return placesByPosition.get(pos);
	else
	    return null;
    }
        
    /**
     * @param name Place name
     * @return List of all places with this name or an empty list if no such places exist
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
     * @param cat PlaceCategory
     * @return List of all places in this category or an empty list if no such places exist
     */
    public Collection<Place> getPlacesByCategory(PlaceCategory cat) {
	Collection<Place> ret;
	if (placesByCategory.containsKey(cat)) {
	    ret = new ArrayList<Place>(placesByCategory.get(cat));
	} else {
	    ret = new ArrayList<Place>();
	}
	return ret;
    }

    /**
     * @param place
     */
    public void addPlace(Place place) {
	// TODO
    }
    
    /**
     * @param pos
     * @param name
     * @param place
     */
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
	if (place.hasCategory()) {
	    PlaceCategory cat = place.getCategory();
	    if (placesByCategory.containsKey(cat)) {
		placesByCategory.get(cat).add(place);
	    } else {
		List<Place> l = new ArrayList<Place>();
		l.add(place);
		placesByCategory.put(cat, l);
	    }
	}
	modified = true;
	this.add(place);
    }
    
    // TODO Unused
    public void removePlace(Place pl) {
	// TODO
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
	System.out.println("placesByPosition.remove(pos): " + first + " " + second + " " + liststate + third);
    }

    // TODO Unused
    public Place removePlace(PlacePosition pos) {
	// TODO
	Place ret = placesByPosition.remove(pos);
	if (ret != null) {	    
	    modified = true;
	}
	return ret;
    }
    
    /**
     * @param places
     */
//    public void removePlaces(Collection<Place> places) {
//	// TODO
//	System.out.println("Remove Collection of Places");
//	System.out.println("PlacesByPosition: " + placesByPosition);
//	System.out.println("PlacesByName: " + placesByName);
//	System.out.println("PlacesByCategory: " + placesByCategory);
//	System.out.println("SelectedPlaces: " + selectedPlaces);
//	System.out.println("Argument: " + places);
//	
//	System.out.println("Removing All from collection by iterator.");
//	Iterator<Place> iter = places.iterator();
//	while (iter.hasNext()) {
//	    Place p = iter.next();
//	    System.out.println("1: p=" + p);
//	    
//	    PlacePosition pos = p.getPosition();
//	    Place first = placesByPosition.remove(pos);
//	    System.out.println("2: p=" + p);
//	    
//	    String name = p.getName();
//	    List<Place> list1 = placesByName.get(name);
//	    boolean second = list1.remove(p);
//	    System.out.println("3: p=" + p);
//	    String list1state = "non-empty,";
//	    if (list1.isEmpty()) {
//		placesByName.remove(name);
//		list1state = "empty,";
//	    }	    
//	    
//	    PlaceCategory cat = p.getCategory();
//	    String list2state = "?";
//	    boolean third = false;	    
//	    if (cat != null) {
//		List<Place> list2 = placesByCategory.get(cat);
//		System.out.println("list2 = placesByCategory.get(" + cat + "): " + list2);
//		if (list2 != null) {
//		    third = list2.remove(p);
//		}
//		if (list2.isEmpty()) {
//		    list2state = "category-empty;";
//		} else {
//		    list2state = "category-non-empty;";
//		}
//	    } else {
//		list2state = "--category-null-;";
//	    }
//
//	    this.remove(p);
//	    modified = true;
//	    System.out.println(first + " ¤# " + second + " ¤# " + list1state + " ¤# " + third + " ¤# " + list2state);
//	}
////	System.out.println("PlacesByPosition: " + placesByPosition);
////	System.out.println("PlacesByName: " + placesByName);
////	System.out.println("SelectedPlaces: " + selectedPlaces);
////	System.out.println("Argument: " + places);
//	boolean fourth = selectedPlaces.removeAll(places);
//	System.out.println("selectedPlaces.removeAll(places): " + fourth);
//	revalidate();
//    }
    
    public void removePlaces(Collection<Place> places) {
	Iterator<Place> iter = places.iterator();
	while (iter.hasNext()) {
	    Place p = iter.next();
	    PlacePosition pos = p.getPosition();
	    String name = p.getName();
	    PlaceCategory cat = p.getCategory();
//	    if (p.hasCategory()) {
//		cat = p.getCategory();
//	    }
	    placesByPosition.remove(pos);
	    List<Place> list1 = placesByName.get(name);
	    if (list1 != null) {
		list1.remove(p);
		if (list1.isEmpty()) {
		    placesByName.remove(name);
		}
	    }
	    if (cat != null) {
		List<Place> list2 = placesByCategory.get(cat);
		list2.remove(p);
	    }
	    this.remove(p);
	    modified = true;
	}
	selectedPlaces.removeAll(places);
	revalidate();
    }

    // TODO Unused
    public void removeSelected() {
	System.out.println("removeSlected()...");
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
    
    /**
     * @param places
     */
    public void hidePlaces(Collection<Place> places) {
//	System.out.println("\nHide places");
//	System.out.println("PlacesByPosition: " + placesByPosition);
//	System.out.println("PlacesByName: " + placesByName);
//	System.out.println("SelectedPlaces: " + selectedPlaces);
//	System.out.println("Argument: " + places);
	Iterator<Place> iter = places.iterator();
	while (iter.hasNext()) {
	    Place p = iter.next();
	    p.setVisible(false);
	    if (p.isSelected()) {
		p.setSelected(false);		
	    }
	}
	selectedPlaces.removeAll(places);
//	System.out.println("\nAfter iteration");
//	System.out.println("PlacesByPosition: " + placesByPosition);
//	System.out.println("PlacesByName: " + placesByName);
//	System.out.println("SelectedPlaces: " + selectedPlaces);
//	System.out.println("Argument: " + places);
	revalidate();
    }


    /**
     * @return the selectedPlaces
     */
    public Collection<Place> getSelectedPlaces() {
        return this.selectedPlaces;
    }

    /**
     * @param place
     * @param sel
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
		p.setVisible(true);
		p.setSelected(true);
		selectedPlaces.add(p);
	    }
	}
	modified = true;
    }
    

    /**
     * @param filePath
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveData(String filePath) throws FileNotFoundException, IOException {
	// Open file stream for saving
	FileOutputStream fos = new FileOutputStream(filePath);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	// Save mapImage
//	oos.writeObject(mapImage);
	ImageIO.write(mapImage, "jpg", oos);
	// Save categories
//	oos.writeObject(categories);
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
     * @param docfile
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveData(File docfile) throws ClassNotFoundException, FileNotFoundException, IOException {
	// Open file stream for saving
	FileOutputStream fos = new FileOutputStream(docfile);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	// Save mapImage
//	oos.writeObject(mapImage);
	ImageIO.write(mapImage, "jpg", oos);
	// Save categories
//	oos.writeObject(categories);
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
	setPreferredSize(new Dimension(mapImage.getWidth(this), mapImage.getHeight(this)));
    }
    
    /**
     * @param filePath
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
//	mapImage = (Image)ois.readObject();
	this.mapImage = ImageIO.read(ois);
	if (mapImage.getWidth(this) < 0)
	    throw new IllegalArgumentException("Can't load the map image from the file!");
	// Load categories
//	this.categories = (ArrayList<PlaceCategory>)ois.readObject();
	// Load Places
	this.placesByPosition = (HashMap<PlacePosition, Place>) ois.readObject();
	this.placesByName = (HashMap<String, List<Place>>) ois.readObject();
	this.placesByCategory = (HashMap<PlaceCategory, List<Place>>) ois.readObject();
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
     * @param docfile
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
//	mapImage = (Image)ois.readObject();
	this.mapImage = ImageIO.read(ois);
	// Load categories
//	this.categories = (ArrayList<PlaceCategory>)ois.readObject();
	// Load positionedPlaces
	this.placesByPosition = (HashMap<PlacePosition, Place>) ois.readObject();
	this.placesByName = (HashMap<String, List<Place>>) ois.readObject();
	this.placesByCategory = (HashMap<PlaceCategory, List<Place>>) ois.readObject();
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
