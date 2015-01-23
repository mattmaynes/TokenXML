package emex.xml.parse;

import java.util.StringTokenizer;

import emex.xml.XMLNode;

public class XMLToken extends XMLNode{
	
	private String xml;
		
	private XMLTokenType type;
	
	public XMLToken(String xml) throws XMLMalformedException{
		super();
		this.xml = xml;
		this.parseXML(xml);	
	}
	
	public XMLTokenType getType(){
		return this.type;
	}

	public String serialize(){
		return this.xml;
	}
		
	private void parseXML(String xml) throws XMLMalformedException{
		// If the given xml is a data token then pass
		XMLTokenType type = parseTokenType(xml);
		String stripped = stripFormatting(xml);
		if(type.equals(XMLTokenType.DATA_TOKEN) || type.equals(XMLTokenType.COMMENT_TOKEN)){
			this.setName(stripped);
			return;
		}
		
		StringTokenizer tk = new StringTokenizer(stripped);
		
		if(!tk.hasMoreTokens())
			throw new XMLMalformedException("XML token must have a tag name value");
				
		String token = tk.nextToken();
		this.setName(token);
		
		if (tk.hasMoreTokens()) {
			if(this.type.equals(XMLTokenType.CLOSE_TOKEN))
				throw new XMLMalformedException("XML closing token cannot contain attributes: " + xml);
			
			parseAttributes(tk);
		}
		
	}
	
	private void parseAttributes(StringTokenizer tk){
		String token;
		
		token = tk.nextToken();
		while (token != null) {
			String[] attr = token.split("=");
			if(attr.length == 1){
				this.addAttribute(attr[0], "");
			}
			else if(attr.length == 2){
				this.addAttribute(attr[0], attr[1].replaceAll("\"", ""));
			}

			token = tk.hasMoreTokens() ? tk.nextToken() : null;
		}
	}
	
	private XMLTokenType parseTokenType(String xml) throws XMLMalformedException{
		if(xml.startsWith(XMLTokenType.CLOSE_TOKEN.toString())){
			this.type = XMLTokenType.CLOSE_TOKEN;
		}
		else if(xml.endsWith(XMLTokenType.SHORT_TOKEN.toString())){
			this.type = XMLTokenType.SHORT_TOKEN;
		}
		else if(xml.startsWith(XMLTokenType.COMMENT_TOKEN.toString())){
			if(xml.endsWith(XMLTokenType.END_COMMENT_TOKEN.toString()))
				this.type = XMLTokenType.COMMENT_TOKEN;
			else
				throw new XMLMalformedException("XML format exception: unclosed comment block: " + xml);
		}
		else if(xml.startsWith(XMLTokenType.DECLARATION_TOKEN.toString())){
			if(xml.endsWith(XMLTokenType.END_DECLARATION_TOKEN.toString()))
				this.type = XMLTokenType.DECLARATION_TOKEN;
			else
				throw new XMLMalformedException("XML format exception: unclosed comment block: " + xml);
		}
		else if(xml.startsWith(XMLTokenType.DOCTYPE_TOKEN.toString())){
			this.type = XMLTokenType.DOCTYPE_TOKEN;
		}
		else if(xml.startsWith(XMLTokenType.OPEN_TOKEN.toString())){
			this.type = XMLTokenType.OPEN_TOKEN;
		}
		else{
			this.type = XMLTokenType.DATA_TOKEN;
		}
		return this.type;
	}
	
	private String stripFormatting(String xml){
		String[] formatting = {
			XMLTokenType.COMMENT_TOKEN.toString(),
			XMLTokenType.END_COMMENT_TOKEN.toString(),
			XMLTokenType.DECLARATION_TOKEN.toString(),
			XMLTokenType.END_DECLARATION_TOKEN.toString(),
			XMLTokenType.DOCTYPE_TOKEN.toString(),
			XMLTokenType.CLOSE_TOKEN.toString(),
			XMLTokenType.SHORT_TOKEN.toString(),
			XMLTokenType.OPEN_TOKEN.toString(),			
			XMLTokenType.END_TOKEN.toString(),
		};

		for(String s : formatting){
			xml = xml.replace(s, "");
		}
		return xml;

	}


}
