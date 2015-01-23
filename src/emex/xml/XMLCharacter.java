package emex.xml;

public class XMLCharacter {
	
	public static final char XML_ESCAPE_DELIMETER = '&';
	
	public static final char XML_ESCAPE_TERMINATOR = ';';
	
	public static final char[] XML_ESCAPE_CHARS = {
		'"','&','\'','<','>','¡','¢','£','¤','¥','¦','§','¨','©','ª','«','¬','®','¯',
		'°','±','²','³','´','µ','¶','·','¸','¹','º','»','¼','½','¾','¿','À','Á','Â',
		'Ã','Ä','Å','Æ','Ç','È','É','Ê','Ë','Ì','Í','Î','Ï','Ð','Ñ','Ò','Ó','Ô','Õ',
		'Ö','×','Ø','Ù','Ú','Û','Ü','Ý','Þ','ß','à','á','â','ã','ä','å','æ','ç','è',
		'é','ê','ë','ì','í','î','ï','ð','ñ','ò','ó','ô','õ','ö','÷','ø','ù','ú','û',
		'ü','ý','þ','ÿ','Œ','œ','Š','š','Ÿ','ƒ','ˆ','˜','Β','Γ','Δ','Ε','Θ','Ι','Κ',
		'Λ','Ξ','Ο','Π','Ρ','Σ','Τ','Υ','Φ','Χ','Ψ','Ω','α','β','γ','δ','ε','ζ','η',
		'θ','ι','κ','λ','μ','ν','ξ','ο','π','ρ','ς','σ','τ','υ','φ','χ','ψ','ω','ϑ',
		'ϒ','ϖ','–','—','‘','’','‚','“','”','„','†','‡','•','…','‰','′','″','‹','›',
		'‾','⁄','€','™','←','↑','→','↓','↔','∀','∂','∃','∇','∈','∋','∏','∑','−','√',
		'∝','∞','∠','∧','∨','∩','∪','∫','∴','∼','≈','≠','≡','≤','≥','⊂','⊃','⊆','⊇',
		'⊕','⊥','⋮','◊','♠','♣','♥','♦'};
	
	public static final String[] XML_ESCAPE_STRINGS = {
		"quot","amp","apos","lt","gt","iexcl","cent","pound","curren","yen","brvbar",
		"sect","uml","copy","ordf","laquo","not","reg","macr","deg","plusmn","sup2",
		"sup3","acute","micro","para","middot","cedil","sup1","ordm","raquo","frac14",
		"frac12","frac34","iquest","Agrave","Aacute","Acirc","Atilde","Auml","Aring",
		"AElig","Ccedil","Egrave","Eacute","Ecirc","Euml","Igrave","Iacute","Icirc",
		"Iuml","ETH","Ntilde","Ograve","Oacute","Ocirc","Otilde","Ouml","times",
		"Oslash","Ugrave","Uacute","Ucirc","Uuml","Yacute","THORN","szlig","agrave",
		"aacute","acirc","atilde","auml","aring","aelig","ccedil","egrave","eacute",
		"ecirc","euml","igrave","iacute","icirc","iuml","eth","ntilde","ograve","oacute",
		"ocirc","otilde","ouml","divide","oslash","ugrave","uacute","ucirc","uuml",
		"yacute","thorn","yuml","OElig","oelig","Scaron","scaron","Yuml","fnof","circ",
		"tilde","ΑBeta","Gamma","Delta","Epsilon","Theta","Iota","Kappa","Lambda","ΝXi",
		"Omicron","Pi","Rho","Sigma","Tau","Upsilon","Phi","Chi","Psi","Omega","alpha",
		"beta","gamma","delta","epsilon","zeta","eta","theta","iota","kappa","lambda","mu",
		"nu","xi","omicron","pi","rho","sigmaf","sigma","tau","upsilon","phi","chi","psi",
		"omega","thetasym","upsih","piv","ndash","mdash","lsquo","rsquo","sbquo","ldquo",
		"rdquo","bdquo","dagger","Dagger","bull","hellip","permil","prime","Prime","lsaquo",
		"rsaquo","oline","frasl","euro","trade","larr","uarr","rarr","darr","harr","forall",
		"part","exist","nabla","isin","ni","prod","sum","minus","radic","prop","infin","ang",
		"and","or","cap","cup","int","there4","sim","asymp","ne","equiv","le","ge","sub","sup",
		"sube","supe","oplus","perp","vellip","loz","spades","clubs","hearts","diams"
	};
		
	
	public static String escapeString(String str){
		String escape;
		char[] chars = str.toCharArray();
		int offset = 0;
		for(int i = 0; i < chars.length; i++){
			char c = chars[i];
			if((c < '0' || c > '9') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z')){
				escape = lookupEscapeString(c);
				if(escape != null && i + offset < str.length()){
					str = str.substring(0, i + offset) 
							+ XML_ESCAPE_DELIMETER 
							+ escape 
							+ XML_ESCAPE_TERMINATOR 
							+ (1 + i + offset < str.length() ? str.substring(1 + i + offset) : "");
					
					offset += 1 + escape.length();
					
				}
			}
		}
		return str;
	}
	
	public static String decodeString(String str){
		char original;
		int start = 0;
		int offset = 0;
		char[] chars = str.toCharArray();
		for(int i = 0; i < chars.length; i++){
			char c = chars[i];
			if(c == XML_ESCAPE_DELIMETER){
				start = i + 1;
			}
			else if(c == XML_ESCAPE_TERMINATOR){
				original = lookupEscapedChar(str.substring(start + offset, i + offset));
				if(original > 0){
					str = str.substring(0, start + offset - 1) + original + str.substring(1 + i + offset);
					offset -= i - start + 1;
				}
			}
				
		}
		
		return str;
	}
	
	public static String lookupEscapeString(char c){
		for(int i = 0; i < XML_ESCAPE_CHARS.length; i++){
				if(c == XML_ESCAPE_CHARS[i])
					return XML_ESCAPE_STRINGS[i];
		}
		return null;
	}
	
	public static char lookupEscapedChar(String s){
		for(int i = 0; i < XML_ESCAPE_STRINGS.length; i++){
				if(s.equals(XML_ESCAPE_STRINGS[i]))
					return XML_ESCAPE_CHARS[i];
		}
		return 0;
	}
}
