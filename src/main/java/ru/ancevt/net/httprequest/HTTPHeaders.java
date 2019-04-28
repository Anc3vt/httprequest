package ru.ancevt.net.httprequest;

import java.util.HashMap;
import java.util.Set;

public class HTTPHeaders extends HashMap<String, String> {

	private static final long serialVersionUID = 6209959349101487343L;

	public static final String ACCEPT = "Accept";
	public static final String ACCEPT_CHARSET = "Accept-Charset";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
	public static final String ACCEPT_RANGES = "Accept-Ranges";
	public static final String AGE = "Age";
	public static final String ALLOW = "Allow";
	public static final String ALTERNATES = "Alternates";
	public static final String AUTHORIZATION = "Authorization";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String CONNECTION = "Connection";
	public static final String CONTENT_BASE = "Content-Base";
	public static final String CONTENT_DISPOSITI = "Content-Disposition";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String CONTENT_LANGUAGE = "Content-Language";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String CONTENT_LOCATION = "Content-Location";
	public static final String CONTENT_MD5 = "Content-MD5";
	public static final String CONTENT_RANGE = "Content-Range";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_VERSION = "Content-Version";
	public static final String DATE = "Date";
	public static final String DERIVED_FROM = "Derived-From";
	public static final String ETAG = "ETag";
	public static final String EXPECT = "Expect";
	public static final String EXPIRES = "Expires";
	public static final String FROM = "From";
	public static final String HOST = "Host";
	public static final String IF_MATCH = "If-Match";
	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	public static final String IF_NONE_MATCH = "If-None-Match";
	public static final String IF_RANGE = "If-Range";
	public static final String IF_UNMODIFIED_SIN = "If-Unmodified-Since";
	public static final String LAST_MODIFIED = "Last-Modified";
	public static final String LINK = "Link";
	public static final String LOCATION = "Location";
	public static final String MAX_FORWARDS = "Max-Forwards";
	public static final String MIME_VERSION = "MIME-Version";
	public static final String PRAGMA = "Pragma";
	public static final String PROXY_AUTHENTICAT = "Proxy-Authenticate";
	public static final String PROXY_AUTHORIZATI = "Proxy-Authorization";
	public static final String PUBLIC = "Public";
	public static final String RANGE = "Range";
	public static final String REFERER = "Referer";
	public static final String RETRY_AFTER = "Retry-After";
	public static final String SERVER = "Server";
	public static final String TITLE = "Title";
	public static final String TE = "TE";
	public static final String TRAILER = "Trailer";
	public static final String TRANSFER_ENCODING = "Transfer-Encoding";
	public static final String UPGRADE = "Upgrade";
	public static final String URI = "URI";
	public static final String USER_AGENT = "User-Agent";
	public static final String VARY = "Vary";
	public static final String VIA = "Via";
	public static final String WARNING = "Warning";
	public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
	
	public HTTPHeaders() {
	
	}
	
	public HTTPHeaders(final String[][] keysValues) {
		for(int i = 0; i < keysValues.length; i ++) {
			final String key = keysValues[i][0];
			final String value = keysValues[i][1];
			put(key, value);
		}
	}
	
	@Override 
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(this.getClass().getName());
		stringBuilder.append("[\n");
		
		final Set<String> keySet = keySet();
		for(final String key : keySet) {
			final String value = get(key);
			stringBuilder.append('\t')
						 .append(key)
						 .append(':')
						 .append(' ')
						 .append(value)
						 .append('\n');
		}
		
		stringBuilder.append(']');
		
		return stringBuilder.toString();
	}
	
	@Override
	public HTTPHeaders clone() {
		final HTTPHeaders r = new HTTPHeaders();
		
		final Set<String> keySet = keySet();
		for(final String key : keySet) {
			final String value = get(key);
			
			r.put(key, value);
		}
		
		return r;
	}
}











