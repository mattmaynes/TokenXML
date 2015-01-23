package emex.xml;

import java.util.ArrayList;

/**
 * An XMLDocument is a serializable XMLNode that can be persisted to a file or an output stream.
 * 
 * @version 0.2.0
 * @since November 16, 2014
 * @author Matthew Maynes
 */
public class XMLDocument extends XMLNode{

	/**
	 * The root element for this document 
	 */
	private XMLElement root;
	
	private XMLDeclaration declaration;
	
	private ArrayList<XMLDoctype> doctypes;
	
	public XMLDocument(XMLElement root){
		super();
		this.root = root;
		this.doctypes = new ArrayList<XMLDoctype>();
		this.addAttribute("version", "1.0");
		this.addAttribute("encoding", "UTF-8");
	}
	
	public XMLElement getRoot() {
		return root;
	}

	public void setRoot(XMLElement root) {
		this.root = root;
	}

	public XMLDeclaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(XMLDeclaration declaration) {
		this.declaration = declaration;
	}
	
	public void addDoctype(XMLDoctype doctype){
		this.doctypes.add(doctype);
	}
	
	@Override
	public String serialize() {
		StringBuffer buffer = new StringBuffer();
		if(declaration != null) buffer.append(declaration.serialize());
		
		for(XMLDoctype doctype : this.doctypes){
			buffer.append(doctype.serialize());
		}
		
		buffer.append(root.serialize());
		return buffer.toString();
	}

	
	
}
