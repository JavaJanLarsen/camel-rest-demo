package demo.server;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 
 * A simple utility class for for creating a reversible hash, i.e. Base64 encoding, of a String
 */
public class Hasher {
	
	/**
	 * Base64 encode the input String using UTF-8
	 * @param s the String to encode
	 * @return the encoded String 
	 * @throws RuntimeException if UTF-8 is not supported
	 */
	public String hash(String s) {
		try {
			return Base64.getEncoder().encodeToString(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 not supported!");
		}
	}
}
