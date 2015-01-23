package emex.xml.parse;

/**
 * Defines an exception for XML that has a formatting issue. 
 * 
 * @version 0.2.0
 * @since November 16, 2014
 * @author Matthew Maynes
 */
public class XMLMalformedException extends Exception {

	private static final long serialVersionUID = 3604283511139965219L;

	public XMLMalformedException(String message){
		super(message);
	}
	
}
