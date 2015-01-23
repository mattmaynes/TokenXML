package emex.xml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public abstract class XMLNode {

	private String name;
	
	private Set<XMLAttribute> attributes;
	
	public XMLNode(){
		this("");
	}
	
	public XMLNode(String name){
		this.name = name;
		this.attributes = new HashSet<XMLAttribute>();
	}
	
	public XMLNode(XMLNode node){
		this.name = node.name;
		this.attributes = new HashSet<XMLAttribute>(node.attributes);
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public XMLAttribute[] getAttributes(){
		return this.attributes.toArray(new XMLAttribute[0]);
	}
	
	public XMLAttribute getAttribute(String key){
		for(XMLAttribute attr : this.attributes){
			if(attr.getKey().equals(key))
				return new XMLAttribute(attr);
		}
		return null;
	}

	public void addAttribute(String key, String value){
		this.addAttribute(new XMLAttribute(key, value));
	}
	
	public void addAttribute(XMLAttribute attr){
		this.attributes.add(attr);
	}
	
	public void addAllAttributes(XMLAttribute[] attrs){
		for(XMLAttribute attr : attrs){
			this.attributes.add(attr);
		}
	}
	
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
