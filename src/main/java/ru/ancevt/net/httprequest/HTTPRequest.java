package ru.ancevt.net.httprequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class HTTPRequest implements IHTTPRequest {
	
	public static final int DEFAULT_CONNECTION_TIMEOUT = 30000; //ms

	private static final String PREFIX_HTTP = "http://";
	private static final String PREFIX_HTTPS = "https://";

	private static final String SCHEME_SUPPORTED_MESSAGE = "Only http or https scheme supported";

	public static void main(String[] args) throws HTTPException {
		final HTTPRequest request = new HTTPRequest();

		request.data().setData("a=12");

		request.send("http://ancevt.ru/test/test.php?b=148", HTTPMethod.POST, null);
		
		final int status = request.getStatus();

		System.out.println("Status: " + status);
		System.out.println("Response:");
		System.out.println(request.readString());
	}

	private int responseBodyLength;
	private int status;

	private InputStream inputStream;
	private BufferedReader bufferedReader;
	private final HTTPRequestBody requestBody;
	private HTTPHeaders requestHeaders;
	private HTTPHeaders responseHeaders;

	private String responseString;
	
	public HTTPRequest() {
		requestBody = new HTTPRequestBody();
	}

	public final int getStatus() {
		return status;
	}

	public final HTTPHeaders getRequestHeaders() {
		return requestHeaders;
	}
	
	public final void setRequestHeaders(HTTPHeaders headers) {
		this.requestHeaders = headers;
	}

	public final HTTPHeaders getResponseHeaders() {
		return responseHeaders;
	}

	@Override
	public final HTTPRequestBody data() {
		return requestBody;
	}

	@Override
	public final void send(final String url) throws HTTPException {
		send(url, HTTPMethod.GET, null);
	}

	@Override
	public final void send(final String url, final String method) throws HTTPException {
		send(url, method, null);
	}

	@Override
	public final void send(final String url, final String method, HTTPHeaders headers) throws HTTPException {

		setRequestHeaders(headers);

		try {
			final HttpURLConnection connection = createHTTPConnection(new URL(url));

			connection.setDoInput(true); 
			connection.setRequestMethod(method);

			if (this.requestHeaders == null)
				this.requestHeaders = new HTTPHeaders();

			if (!requestBody.isEmpty()) {
				connection.setDoOutput(true);

				if (!this.requestHeaders.containsKey(HTTPHeaders.CONTENT_LENGTH)) {
					final String value = String.valueOf(requestBody.length());
					this.requestHeaders.put(HTTPHeaders.CONTENT_LENGTH, value);
					connection.setRequestProperty(HTTPHeaders.CONTENT_LENGTH, value);
				}

			}

			final Set<String> headersSet = this.requestHeaders.keySet();
			for (final String key : headersSet) {
				final String value = this.requestHeaders.get(key);
				connection.setRequestProperty(key, value);
			}

			if (!requestBody.isEmpty())
				try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
					wr.write(requestBody.getData());
				}

			responseBodyLength = connection.getContentLength();
			status = connection.getResponseCode();

			responseHeaders = new HTTPHeaders();
			readResponseHeaders(connection, responseHeaders);

			prepareInput(connection, status);

		} catch (MalformedURLException e) {
			throw new HTTPException(e.getMessage());
		} catch (IOException e) {
			throw new HTTPException(e.getMessage());
		}
	}

	private final void readResponseHeaders(final HttpURLConnection connection, final HTTPHeaders headers) {
		final Map<String, List<String>> map = connection.getHeaderFields();

		final Set<String> keySet = map.keySet();
		for (final String key : keySet) {
			final List<String> values = map.get(key);

			final StringBuilder stringBuilder = new StringBuilder();
			final int size = values.size();
			for (int i = 0; i < size; i++) {
				stringBuilder.append(values.get(i));
				if (size > 1 && i != size - 1)
					stringBuilder.append(';');
			}

			headers.put(key, stringBuilder.toString());
		}
	}

	private HttpURLConnection createHTTPConnection(final URL url) throws IOException {
		final String urlString = url.toString();

		HttpURLConnection result = null;
		
		if (urlString.startsWith(PREFIX_HTTPS)) {
			result = (HttpsURLConnection) url.openConnection();
		} else if (urlString.startsWith(PREFIX_HTTP)) {
			result = (HttpURLConnection) url.openConnection();
		} else {
			throw new RuntimeException(SCHEME_SUPPORTED_MESSAGE);
		}
		
		result.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
		
		return result;
	}

	public final int getResponseBodyLength() {
		return responseBodyLength;
	}

	private final void prepareInput(HttpURLConnection con, int status) throws IOException {
		inputStream = status < 400 ? con.getInputStream() : con.getErrorStream();
		if(inputStream != null) {
			bufferedReader = 
					new BufferedReader(
							new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		}
	}

	public final InputStream getInputStream() {
		return inputStream;
	}

	public final BufferedReader getBufferedReader() {
		return bufferedReader;
	}
	
	public final byte[] readBytes() throws HTTPException {
		final ByteArrayBuilder byteArrayBuilder = 
				new ByteArrayBuilder(Short.MAX_VALUE);
		try{
			while(true){
				if(byteArrayBuilder.length() >= responseBodyLength)
					break;
				final int available = inputStream.available();
				if(available == 0)
					continue;
				final byte[] bs = new byte[available];
				inputStream.read(bs);
				byteArrayBuilder.add(bs);
			}
			return byteArrayBuilder.getArray();
		}catch(IOException e){
			throw new HTTPException(e.getMessage());
		}
	}

	public final void close() throws IOException {
		if(bufferedReader != null) {
			inputStream.close();
		}
	}

	public final String readString() throws HTTPException {
		if(responseString != null) return responseString;
		
		try {
			final StringBuilder stringBuilder = new StringBuilder();
			final BufferedReader bufferedReader = getBufferedReader();

			if(bufferedReader == null) return null;
			
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append('\n');
			}

			bufferedReader.close();

			responseString = stringBuilder.toString();
			
			return responseString;
		} catch (IOException e) {
			throw new HTTPException(e.getMessage());
		}
	}

}
