package emex.xml;

/**
 * An XMLAttribute is a key value pair that describes information about an element in its opening tag. 
 * If the value of an attribute is unspecified then only the key is relevant. Both the key and value 
 * of an attribute must be a string that can be parsed into valid XML.
 * 
 * {@code
 * XMLAttribute attr = new XMLAttribute("int", "2");
 * attr.toString()
 * 
 * > int="2"
 * 
 *  XMLAttribute attr = new XMLAttribute("int");
 * attr.toString()
 * 
 * > int
 * }
 * 
 * @version 0.2.0
 * @since November 16, 2014
 * @author Matthew Maynes
 */
public class XMLAttribute{
	
	/**
	 * Defines an identifier for an attribute that should not have a value
	 */
	public static String NO_VALUE = "";
	
	/**
	 * The key of this attribute
	 */
	private String key;
	
	/**
	 * The value of this attribute or nothing if there is no value
	 */
	private String value;
	
	/**
	 * Creates an XMLAttribute with a key but no value.
	 * 
	 * @param key The key for this attribute
	 */
	public XMLAttribute(String key){
		this(key, NO_VALUE);
	}
	
	/**
	 * Creates an XMLAttribute the given key and value
	 * 
	 * @param key The key for this attribute
	 * @param value The value to associate with the given key
	 */
	public XMLAttribute(String key, String value){
		this.key = key;
		this.value = value;
	}

	/**
	 * Creates a new XMLAttribute with the same key value pair as the given attribute.
	 * This is a copy constructor
	 * 
	 * @param attr The attribute to copy
	 */
	public XMLAttribute(XMLAttribute attr){
		this.key = attr.key;
		this.value = attr.value;
	}
	
	/**
	 * Returns the key value for this attribute
	 * 
	 * @return This attributes key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key for this attribute
	 * 
	 * @param key The new key for this attribute
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Returns if this attribute has a value that will be serialized or not
	 * 
	 * @return If there will be a value attached to the given key
	 */
	public boolean hasValue(){
		return value != null && value.length() > 0;
	}

	/**
	 * Returns the value of this attribute if there is one. 
	 * 
	 * @return The value or a blank string
	 */
	public String getValue() {			
		return value;
	}

	/**
	 * Sets a value for this attribute. If the value for this attribute needs to 
	 * be removed, set the value to XMLAttribute.NO_VALUE or null
	 * 
	 * @param value The new value for this attribute
	 * 
	 * @see #NO_VALUE
	 */
	public void setValue(String value) {		
		if(value.startsWith("\"") && value.endsWith("\""))
			value = value.substring(1, value.length() - 1);
		
		this.value = XMLCharacter.decodeString(value);
	}
	
	/**
	 * Serializes this attribute into XML key value pairs with escaped quotes
	 * 
	 * TODO: escape all XML special characters
	 * 
	 * @return A string representation of this attribute
	 */
	public String serialize(){
		if(!this.hasValue()){
			return this.key;
		}
		return this.key + "=\"" + XMLCharacter.escapeString(this.value) + "\"";
	}

	/**
	 * Returns a string representation of this object
	 */
	@Override
	public String toString(){
		return this.serialize();
	}
	
	
	/**
	 * Compares this attribute to another object for equality. Two XMLAttributes are equals
	 * if their keys and values are equal
	 * 
	 * @param other The object to compare to
	 * 
	 * @return If this object and the other are considered equal
	 */
	@Override
	public boolean equals(Object other){
		if(other instanceof XMLAttribute){
			boolean keysMatch = this.key.equals(((XMLAttribute) other).key);
			if(this.hasValue())
				return keysMatch && this.value.equals(((XMLAttribute) other).value);
			return keysMatch;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.serialize().hashCode();
	}
}


