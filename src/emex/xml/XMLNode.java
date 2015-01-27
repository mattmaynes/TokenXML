package emex.xml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * An XMLNode is any node within an XML document. All nodes have a name or a type.
 * All nodes can have attributes that are declared in key value pairs.
 *
 * @author Matthew Maynes
 */
public abstract class XMLNode {

	/**
	 * The name of the node
	 */
	private String name;
	
	/**
	 * All of the attributes in the node
	 */
	private Set<XMLAttribute> attributes;
	
	/**
	 * Constructs an empty node
	 */
	public XMLNode(){
		this("");
	}
	
	/**
	 * Constructs a node with the given name
	 *
	 * @param name The name of the node (i.e. "body" would be <body>)
	 */
	public XMLNode(String name){
		this.name = name;
		this.attributes = new HashSet<XMLAttribute>();
	}
	
	/**
	 * Constructs node by copying the nodes data
	 *
	 * @param node The node to copy
	 */
	public XMLNode(XMLNode node){
		this.name = node.name;
		this.attributes = new HashSet<XMLAttribute>(node.attributes);
	}
	
	/**
	 * Returns the name of the node
	 *
	 * @return The name of the node
	 */ 
	public String getName(){
		return this.name;
	}
	
	/**
	 * Sets the name of the node
	 *
	 * @param name The new name of the node
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Returns an array of all of the attribtues in this node
	 *
	 * @return The set of all of the attributes and values in this node
	 */
	public XMLAttribute[] getAttributes(){
		return this.attributes.toArray(new XMLAttribute[0]);
	}
	
	/**
	 * Returns the attribute with the corresponding key
	 *
	 * @param key The name of the attribute to get
	 *
	 * @return The attribute that corresponds to the given key
	 */
	public XMLAttribute getAttribute(String key){
		for(XMLAttribute attr : this.attributes){
			if(attr.getKey().equals(key))
				return new XMLAttribute(attr);
		}
		return null;
	}

	/**
	 * Adds an attribute to this node with the given key and value
	 *
	 * @param key 	The key for the attribute
	 * @param value	The value for the attribute
	 */
	public void addAttribute(String key, String value){
		this.addAttribute(new XMLAttribute(key, value));
	}
	
	/**
	 * Adds an attribute object to this node
	 *
	 * @param attr The attribute to add
	 */
	public void addAttribute(XMLAttribute attr){
		this.attributes.add(attr);
	}
	
	/**
	 * Adds all of the attributes in the given array to this node
	 *
	 * @param attrs The attributes to add
	 */
	public void addAllAttributes(XMLAttribute[] attrs){
		for(XMLAttribute attr : attrs){
			this.attributes.add(attr);
		}
	}
	
	/**
	 * Adds all of the attrubutes to the node from the given collection
	 *
	 * @param attrs The attributes to add
	 */
	public void addAllAttributes(Collection<? extends XMLAttribute> attrs){
		this.attributes.addAll(attrs);
	}
	
	public boolean hasAttribute(String key){
		for(XMLAttribute attr : this.attributes){
			if(attr.getKey().equals(key)){
				return true;
			}
		}
		return false;
	}
	
	public abstract String serialize();
	
	@Override
	public String toString(){
		return this.serialize();
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof XMLNode){
			XMLNode obj = (XMLNode)other;
			boolean match = true;
			
			if(obj.attributes.size() != this.attributes.size())
				return false;
			
			for(XMLAttribute attr : this.attributes)
				match &= obj.attributes.contains(attr);
			
			return match && obj.getName().equals(this.name);
		}
		return false;
	}
	
	protected String openingTag(){
		StringBuffer buffer = new StringBuffer();
		
		if(this.name == null)
			return buffer.toString();

		buffer.append("<" + this.name);

		for (XMLAttribute attr : this.attributes){
			buffer.append(" " + attr.serialize());
		}
		return buffer.toString() + ">";
	}
	
	protected String closingTag(){
		if(this.name == null)
			return "";

		return "</" + this.name + ">";
	}

}
