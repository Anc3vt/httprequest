package ru.ancevt.net.httprequest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class HTTPRequestBody {
	
	private byte[] data;
	
	HTTPRequestBody() {
		
	}
	
	public final void setData(final byte[] data) {
		this.data = data;
	}
	
	public final void setData(final String data) {
		if(data != null) this.data = data.getBytes();
	}
	
	public final void setData(final String data, final Charset charset){
		if(data != null) this.data = data.getBytes(charset);
	}
	
	public byte[] getData() {
		return data == null ? data = new byte[0] : data;
	}
	
	public int length() {
		return getData().length;
	}
	
	public final boolean isEmpty() {
		return getData().length == 0;
	}
	
	public String getString() {
		return new String(data);
	}
	
	public String getString(final String charset) throws UnsupportedEncodingException {
		return new String(data, charset);
	}
	
}
