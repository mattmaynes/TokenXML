package emex.xml;

public class XMLDeclaration extends XMLNode{
	
	public XMLDeclaration(){
		super("xml");
	}

	@Override
	protected String openingTag(){
		StringBuffer buffer = new StringBuffer("<?" + this.getName());		
		for (XMLAttribute attr : this.getAttributes()){
			buffer.append(" " + attr.serialize());
		}		
		return buffer.toString() + "?>";
	}
	
	@Override
	public String serialize() {
		return this.openingTag();
	}

	
	
}
