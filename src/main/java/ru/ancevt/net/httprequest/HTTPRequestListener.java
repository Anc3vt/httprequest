package ru.ancevt.net.httprequest;

public interface HTTPRequestListener {
	void httpResponse(final int status, final HTTPHeaders responseHeaders, final byte[] responseData);
	void httpException(final HTTPException ex);
}
