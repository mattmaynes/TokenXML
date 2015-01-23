package emex.xml.parse;

public class XMLFormatter {

	private static String[] whitespace = {"\n", "\t", "\r", "\f"};
	
	public static String stripFormatting(String xml) throws XMLMalformedException{		
		StringBuffer xmlBuffer = new StringBuffer();
		XMLTokenizer xmlTk = new XMLTokenizer(xml);
		XMLToken token = xmlTk.nextToken();
		
		while(token != null){
			String fmt = token.toString();
			
			for(String f : whitespace){
				fmt = fmt.replace(f, "");
			}
			xmlBuffer.append(fmt);
	
			token = xmlTk.nextToken();
		}
		
		return xmlBuffer.toString();
	}
	

	public static String prettyPrint(String xml) throws XMLMalformedException{		
		StringBuffer xmlBuffer = new StringBuffer();
		XMLTokenizer xmlTk = new XMLTokenizer(stripFormatting(xml));
		XMLToken token = xmlTk.nextToken();
		XMLToken lastToken = token;
		String line = "";
		int tabDepth = 0;
		
		while(token != null){
			line = token.toString();
			switch(token.getType()){
				case SHORT_TOKEN:
				case DOCTYPE_TOKEN:
				case END_COMMENT_TOKEN:
				case DECLARATION_TOKEN:
				case END_DECLARATION_TOKEN:
					line = prependCharacter(line, '\t', tabDepth);
					if(!lastToken.getType().equals(XMLTokenType.CLOSE_TOKEN))
						line = '\n' + line;
					break;
				case CLOSE_TOKEN:
				case END_TOKEN:
					tabDepth--;
				case COMMENT_TOKEN:
					line = line + '\n';
					if(!lastToken.getType().equals(XMLTokenType.DATA_TOKEN))
						line = prependCharacter(line, '\t', tabDepth);
					if(lastToken.getType().equals(XMLTokenType.SHORT_TOKEN))
						line = '\n' + line;
					break;
				case OPEN_TOKEN:
					line = prependCharacter(line, '\t', tabDepth++);
					if(!lastToken.getType().equals(XMLTokenType.CLOSE_TOKEN))
						line = '\n' + line;	
					break;
				default:
					break;
			}
			xmlBuffer.append(line);
			lastToken = token;
			token = xmlTk.nextToken();
		}
		
		if (xmlBuffer.charAt(0) == '\n')
			xmlBuffer.deleteCharAt(0);
		return xmlBuffer.toString();
	}
	
	private static String prependCharacter(String str, char c, int times){
		String pre = "";
		for(int i = 0; i < times; i++)
			pre += c;
		return pre + str;
	}
	
}
