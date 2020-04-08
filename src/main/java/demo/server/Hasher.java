package demo.server;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Hasher {
	public String hash(String s) {
		try {
			return Base64.getEncoder().encodeToString(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 not supported!");
		}
	}
}
