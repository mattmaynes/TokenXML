package emex.xml.parse;

public enum XMLTokenType{
	OPEN_TOKEN("<"),
	CLOSE_TOKEN("</"),
	END_TOKEN( ">"),
	SHORT_TOKEN("/>"),
	DECLARATION_TOKEN("<?"),
	END_DECLARATION_TOKEN("?>"),
	DOCTYPE_TOKEN("<!"),
	COMMENT_TOKEN("<!--"),
	END_COMMENT_TOKEN("-->"),
	DATA_TOKEN("*");
	
	private final String value;
	
	XMLTokenType(String value){
		this.value = value;
	}
	
	@Override
	public String toString(){
		return this.value;
	}
}
