package ru.ancevt.net.httprequest;

public interface IHTTPRequest {
	void send(String url) throws HTTPException;

	void send(String url, String method) throws HTTPException;

	void send(String url, String method, HTTPHeaders headers) throws HTTPException;

	HTTPRequestBody data();

	int getStatus();

	void setRequestHeaders(HTTPHeaders headers);

	HTTPHeaders getRequestHeaders();

	HTTPHeaders getResponseHeaders();

	int getResponseBodyLength();

}
