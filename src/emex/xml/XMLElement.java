package emex.xml;

import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * An XMLElement is an XMLObject that can contain a value and/or other XMLElements.
 * Elements inside of this object can be accessed in a DOM style manner using {@link #getElementsByTagName(String)}
 * or {@link #getElementsById(String)}. XMLElements can also be serialized into an XML document.
 *
 * @version 0.2.0
 * @since November 16, 2014
 * @author Matthew Maynes
 */
public class XMLElement extends XMLNode{
	
	/**
	 * Defines an attribute key for an id. This is used for DOM style traversal of the element tree
	 */
	public static final String ID = "id";

	/**
	 * Holds the value content for this element
	 */
	private String value;
	
	/**
	 * Stores all of the children elements inside of this element
	 */
	private ArrayList<XMLElement> children;
	
	/**
	 * A pointer to the parent element of this element so the tree can be traversed bi-directionally
	 */
	private XMLNode parent;

	/**
	 * Creates an empty XMLElement with no name
	 */
	public XMLElement(){
		this("");
	}

	/**
	 * Creates an XMLElement with the given name
	 * 
	 * @param name The tag name for this element
	 */
	public XMLElement(String name){
		this(name, "");
	}

	/**
	 * Creates an XMLElement with the given name and value
	 * 
	 * @param name The tag name for this element
	 * @param value The value of this element
	 */
	public XMLElement(String name, String value){
		super();
		this.setName(name);
		this.value = value;
		this.children = new ArrayList<XMLElement>();
	}

	public XMLElement(XMLElement elem){
		super(elem);
		this.value = elem.getValue();
		this.children = new ArrayList<XMLElement>();
		
		for(XMLElement child : elem.getChildren()){
			this.children.add(child);
		}
	}
	
	/**
	 * Returns this elements text value
	 * 
	 * @return The value of this element if it has any
	 */
	public String getValue(){
		return this.value;
	}
	
	/**
	 * Return this parent of this element if it has one. This can be used for bi-directional 
	 * traversal of the element tree
	 * 
	 * @return The parent element of this element
	 */
	public XMLNode getParent(){
		return this.parent;
	}
	
	/**
	 * Sets the name of this element. If there are multiple components to the name
	 * then the subsequent values separated by spaces are taken to be the id of this element
	 * 
	 * @param name The new name for this element
	 */
	@Override
	public void setName(String name){
		StringTokenizer tk = new StringTokenizer(name, " ");
		if(tk.countTokens() > 1){
			super.setName(tk.nextToken());
			this.setId(tk.nextToken());
		}
		else{
			super.setName(name);
		}
	}

	/**
	 * Sets the text value for this element
	 * 
	 * @param value The value this element will hold
	 */
	public void setValue(String value){
		this.value = XMLCharacter.decodeString(value);
	}
	
	
	/**
	 * Returns the id attribute value that is contained by this element if it has one
	 * 
	 *  @return The value of the "id" attribute
	 */
	public String getId(){
		if(hasId()) 
			return this.getAttribute(ID).getValue();
		return null;
	}
	
	
	/**
	 * Sets the id attribute value for this element
	 * 
	 * @param id The new id value for this element
	 */
	public void setId(String id){
		this.addAttribute(ID, id);
	}
	
	/**
	 * Sets the parent element of this element
	 * 
	 * @param parent This elements parent
	 */
	public void setParent(XMLElement parent){
		this.parent = parent;
	}
	
	/**
	 * Returns if this XMLElement has a value or not
	 * 
	 * @return If this element has a value
	 */
	public boolean hasValue(){
		return !(this.value == null ||this.value.length() == 0);
	}
	
	public boolean hasId(){
		XMLAttribute id = this.getAttribute(ID);
		return id != null;
	}
	

	
	/**
	 * Returns if this element has any child elements or not
	 * 
	 * @return If there are any child elements in this element
	 */
	public boolean hasChildren(){
		return this.children.size() != 0;
	}
	
	/**
	 * Returns if this element has a parent element or not
	 * 
	 * @return If there is a parent to this element
	 */
	public boolean hasParent(){
		return this.parent != null;
	}
	
	/**
	 * Adds a child to this elements structure
	 * 
	 * @param child The child to add to this element
	 */
	public void addChild(XMLElement child){
		child.setParent(this);
		this.children.add(child);
	}
	
	public void addChildren(XMLElement[] children){
		for(XMLElement child : children){
			this.addChild(child);
		}
	}
	
	
	public XMLElement[] getChildren(){
		return this.children.toArray(new XMLElement[0]);
	}
	
	public XMLElement firstChild(){
		return this.children.size() > 0 ? this.children.get(0) : null;
	}
	
	/**
	 * Returns all of the child elements contained by this object that have 
	 * the given tag name. This function performs a single depth search so only 
	 * immediate children are compared against
	 * 
	 * @param name The tag name to search for within the children of this element
	 * 
	 * @return A array of elements that have a matching tag name
	 */
	public XMLElement[] getElementsByTagName(String name){
		ArrayList<XMLElement> elements = new ArrayList<XMLElement>();
		for(XMLElement e : this.children){
			if(name.equals(e.getName()))
				elements.add(e);
		}
		return elements.toArray(new XMLElement[0]);
	}
	
	/**
	 * Returns all of the child elements contained by this object that have 
	 * the given id. This function performs a single depth search so only 
	 * immediate children are compared against
	 * 
	 * @param id The id to search for within the children of this element
	 * 
	 * @return A array of elements that have a matching id
	 */
	public XMLElement[] getElementsById(String id){
		ArrayList<XMLElement> elements = new ArrayList<XMLElement>();
		for(XMLElement e : this.children){
			
			if(id.equals(e.getId()))
				elements.add(e);
		}
		return elements.toArray(new XMLElement[0]);
	}
	

	
	/**
	 * Serializes this XMLElement and all of its children into an XML string. 
	 * The returned string is a representation of the DOM of this XMLElement tree.
	 * 
	 * @return An XML String representation of this object
	 */
	@Override
	public String serialize(){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(this.openingTag());
		
		if(!this.hasValue() && !this.hasChildren()){
			buffer.deleteCharAt(buffer.length() - 1);
			buffer.append("/>");
			return buffer.toString();
		}
		else if(this.hasValue()){
			buffer.append(XMLCharacter.escapeString(this.value));		
		}
		
		for(XMLElement elem : this.children){
			buffer.append(elem.serialize());
		}
		
		buffer.append(this.closingTag());
		return buffer.toString();
	}
	
}
