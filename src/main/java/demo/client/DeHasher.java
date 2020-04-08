package demo.client;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class DeHasher {
	public String deHash(String s) {
		try {
			return new String(Base64.getDecoder().decode(s), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 not supported!");
		}
	}
}
