package demo.client;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 
 * A simple utility class for for de-hashing a Base64 encoded String
 */
public class DeHasher {
	/**
	 * Base64 decode the input String, assuming UTF-8
	 * @param s the String to decode
	 * @return the decoded String
	 * @throws RuntimeException if UTF-8 is not supported
	 */
	public String deHash(String s) {
		try {
			return new String(Base64.getDecoder().decode(s), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 not supported!");
		}
	}
}
