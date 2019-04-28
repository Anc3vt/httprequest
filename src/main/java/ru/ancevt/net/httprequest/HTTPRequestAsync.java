package ru.ancevt.net.httprequest;

import java.util.ArrayList;
import java.util.List;

public class HTTPRequestAsync implements IHTTPRequest {
	
	public static void main(String[] args) throws HTTPException {
		final HTTPRequestAsync req = new HTTPRequestAsync();
		req.addHTTPRequestListener(new HTTPRequestListener() {
			
			@Override
			public void httpResponse(int status, final HTTPHeaders responseHeaders, byte[] responseData) {
				final String response = new String(responseData);
				System.out.println("status: " + status);
				System.out.println(responseHeaders.toString());
				System.out.println("Response: " + response);
			}
			
			@Override
			public void httpException(HTTPException ex) {
				ex.printStackTrace();
			}
		});
		req.send("https://google.com", HTTPMethod.GET, null);
		System.out.println("done");
		
		
	}
	
	private final List<HTTPRequestListener> listeners;
	
	private final HTTPRequest httpRequest;
	private final Thread thread;
	private String url;
	private String method;
	private HTTPHeaders requestHeaders;
	private HTTPHeaders responseHeaders;
	private int responseBodyLength;
	private int status;
	
	public HTTPRequestAsync() {
		listeners = new ArrayList<HTTPRequestListener>();
		httpRequest = new HTTPRequest();
		thread = new Thread(runnable);
	}
	
	public final void addHTTPRequestListener(final HTTPRequestListener httpRequestListener) {
		listeners.add(httpRequestListener);
	}
	
	public final void removeHTTPRequestListener(final HTTPRequestListener httpRequestListener) {
		listeners.remove(httpRequestListener);
	}
	
	private final void dispatchHTTPRequestResponse(final int status, final HTTPHeaders responseHeaders, final byte[] responseData) {
		this.status = status;
		this.responseHeaders = responseHeaders;
		
		final int length = listeners.size();
		for(int i = 0; i < length; i ++) {
			final HTTPRequestListener listener = listeners.get(i);
			listener.httpResponse(status, responseHeaders, responseData);
		}
	}
	
	private final void dispatchHTTPException(final HTTPException exception) {
		final int length = listeners.size();
		for(int i = 0; i < length; i ++) {
			final HTTPRequestListener listener = listeners.get(i);
			listener.httpException(exception);
		}
	}
	
	public final int getStatus() {
		return status;
	}
	
	private final Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			
			try {
				httpRequest.send(url, method, requestHeaders);
				
				final int status = httpRequest.getStatus();
				final HTTPHeaders responseHeaders = httpRequest.getResponseHeaders();
				final byte[] responseData = httpRequest.readBytes();
				
				responseBodyLength = httpRequest.getResponseBodyLength();
				
				dispatchHTTPRequestResponse(status, responseHeaders, responseData);
			} catch (HTTPException e) {
				dispatchHTTPException(e);
			}
		}
	};

	@Override
	public final void send(final String url) throws HTTPException {
		send(url, HTTPMethod.GET, null);
	}

	@Override
	public final void send(final String url, final String method) throws HTTPException {
		send(url, method, null);
	}
	
	@Override
	public final void send(final String url, final String method, final HTTPHeaders headers) throws HTTPException {
		this.url = url;
		this.method = method;
		this.requestHeaders = headers;
		
		thread.start();
	}

	@Override
	public final HTTPRequestBody data() {
		return httpRequest.data();
	}

	@Override
	public void setRequestHeaders(HTTPHeaders headers) {
		this.requestHeaders = headers;
	}

	@Override
	public HTTPHeaders getRequestHeaders() {
		return requestHeaders;
	}

	@Override
	public HTTPHeaders getResponseHeaders() {
		return responseHeaders;
	}

	@Override
	public int getResponseBodyLength() {
		return responseBodyLength;
	}
	
	
}
