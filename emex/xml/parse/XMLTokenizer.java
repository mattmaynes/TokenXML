package emex.xml.parse;

import java.util.Stack;

public class XMLTokenizer {
	
	private String xml;
	
	private int index;
	
	private int tokenCount;
	
	private int marker;
	
	private Stack<XMLToken> tokenStack;
	
	public XMLTokenizer(){
		this(null);
	}
	
	public XMLTokenizer(String xml){
		this.xml = xml;
		this.index = 0;
		this.marker = 0;
		this.tokenCount = 0;
		tokenStack = new Stack<XMLToken>();
	}

	public String getXML() {
		return xml;
	}

	public void setXML(String xml) {
		this.xml = xml;
	}
	
	public void setMarker(){
		this.marker = this.index;
	}
	
	public void rewind(){
		this.index = this.marker;
	}
	
	public void reset(){
		this.index = 0;
		this.marker = 0;
	}
	
	public XMLToken nextToken() throws XMLMalformedException{
		if(xml == null) throw new NullPointerException("XML input data cannot be null");
		if(this.index >= xml.length()) return validateTokenStack(null);
		if(tokenCount == 0 && this.xml.charAt(0) != '<') throw new XMLMalformedException("XML format exception: missing opening token: " + XMLTokenType.OPEN_TOKEN);
		
		String raw = xml.substring(index);
		int start = getStartIndex(raw);		
		int end = getEndIndex(raw) + 1;
		end = Math.max(end, validateXMLComment(raw, start));
		
		// If we have a potential data token
		if(start > 0){
			this.index += start;
			return new XMLToken(raw.substring(0, start));
		}
		else if(start < 0 || start >= raw.length())
			throw new XMLMalformedException("XML format exception: no start token found: " + XMLTokenType.OPEN_TOKEN + " | " + XMLTokenType.CLOSE_TOKEN);
		else if( end < 0 || end > raw.length())
			throw new XMLMalformedException("XML format exception: no end token found: " + XMLTokenType.END_TOKEN + " | " + XMLTokenType.SHORT_TOKEN );
		else if(start > end)
			throw new XMLMalformedException("XML format exception: found end token @" + end + ": "+ raw.charAt(end) + " before start token @" + start  + ": " + raw.charAt(start));
		
		this.index += end;
		return validateTokenStack(new XMLToken(raw.substring(start, end)));
	}
	
	private int getStartIndex(String xml){
		int start = xml.length();
		int[] starts = {xml.indexOf(XMLTokenType.OPEN_TOKEN.toString()), xml.indexOf(XMLTokenType.CLOSE_TOKEN.toString())};
		for(int i : starts){
			start = i >= 0 ? Math.min(start, i) : start;
		}
		return start;
	}
	
	private int getEndIndex(String xml){
		return xml.indexOf(XMLTokenType.END_TOKEN.toString());
	}
	
	private int validateXMLComment(String xml, int index) throws XMLMalformedException{
		if(xml.indexOf(XMLTokenType.COMMENT_TOKEN.toString()) == index){
			int end = xml.indexOf(XMLTokenType.END_COMMENT_TOKEN.toString());
			if(end == -1)
				throw new XMLMalformedException("XML format exception: unclosed comment block: " + xml.substring(index));
			return end + XMLTokenType.END_COMMENT_TOKEN.toString().length();
		}
		return -1;
	}
	
	private XMLToken validateTokenStack(XMLToken token) throws XMLMalformedException{
		if(token == null && this.tokenStack.size() > 0)
			throw new XMLMalformedException("XML format exception: token stack still has unclosed tokens");
		else if(token == null)
			return token;
		
		switch(token.getType()){
			case OPEN_TOKEN:
				this.tokenStack.push(token);
				break;
			case CLOSE_TOKEN:
				if(this.tokenStack.size() == 0 || !this.tokenStack.peek().getName().equals(token.getName()))
					throw new XMLMalformedException("XML format exception: trying to close unopened tag: " + token.getName());
				this.tokenStack.pop();
				break;
			default:
				break;
				
		}
		
		return token;
	}
	
	
	
}
