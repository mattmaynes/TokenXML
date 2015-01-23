package emex.xml.parse;

import java.util.ArrayList;

import emex.xml.XMLDeclaration;
import emex.xml.XMLDoctype;
import emex.xml.XMLDocument;
import emex.xml.XMLElement;

public class XMLDocumentBuilder {

	
	public static XMLDocument parse(String xml) throws NullPointerException, XMLMalformedException{
		ArrayList<XMLDoctype> doctypes = new ArrayList<XMLDoctype>();
		XMLElement root = null, element = null, head = null, tmp = null;
		XMLDeclaration declaration = null;
		XMLToken token;
		XMLTokenizer tk = new XMLTokenizer(XMLFormatter.stripFormatting(xml));
		
		token = tk.nextToken();
		if(token == null) throw new XMLMalformedException("XML contains no tokens");
		
		while(head == null){
			switch(token.getType()){
				case DECLARATION_TOKEN:
					declaration = new XMLDeclaration();
					declaration.addAllAttributes(token.getAttributes());
					break;
				case DOCTYPE_TOKEN:
					XMLDoctype doctype = new XMLDoctype();
					doctype.addAllAttributes(token.getAttributes());
					doctypes.add(doctype);
					break;
				default:
					root = new XMLElement(token.getName());
					root.addAllAttributes(token.getAttributes());
					head = root;
				}
			token = tk.nextToken();
		}
		
		while(token != null){
			switch(token.getType()){
				case OPEN_TOKEN:
					element = new XMLElement(token.getName());
					element.addAllAttributes(token.getAttributes());
					head.addChild(element);
					head = element;
					break;
				case SHORT_TOKEN:
					if(element.hasValue())
						element.setValue(element.getValue() + token.toString());
					else{
						tmp = new XMLElement(token.getName());
						tmp.addAllAttributes(token.getAttributes());
						head.addChild(tmp);
					}
					break;
				case DECLARATION_TOKEN:
					declaration = new XMLDeclaration();
					declaration.addAllAttributes(token.getAttributes());
					break;
				case DOCTYPE_TOKEN:
					XMLDoctype doctype = new XMLDoctype();
					doctype.addAllAttributes(token.getAttributes());
					doctypes.add(doctype);
					break;
				case CLOSE_TOKEN:
					head = (XMLElement) head.getParent();
					break;
				case DATA_TOKEN:
					if(element == null)
						root.setValue(root.getValue() + token.toString());
					else
						element.setValue(element.getValue() + token.toString());
					break;
				default:
					break;
			}
			token = tk.nextToken();
		}
		
		XMLDocument doc = new XMLDocument(root);
		doc.setDeclaration(declaration);
		for(XMLDoctype doctype : doctypes){
			doc.addDoctype(doctype);
		}
		
		return doc;
	}
	
}
