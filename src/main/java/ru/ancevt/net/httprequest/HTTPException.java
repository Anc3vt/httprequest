package ru.ancevt.net.httprequest;

import java.io.IOException;

public class HTTPException extends IOException {

	private static final long serialVersionUID = 6634678464391316027L;

	public HTTPException(final String message) {
		super(message);
	}
}
