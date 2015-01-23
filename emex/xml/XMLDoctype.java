package emex.xml;

public class XMLDoctype extends XMLNode {

	public XMLDoctype(){
		super("DOCTYPE");
	}
	
	@Override
	protected String openingTag(){
		StringBuffer buffer = new StringBuffer("<!" + this.getName());		
		for (XMLAttribute attr : this.getAttributes()){
			buffer.append(" " + attr.serialize());
		}		
		return buffer.toString() + ">";
	}
	
	@Override
	public String serialize() {
		return this.openingTag();
	}

}
