package ru.ancevt.net.httprequest;

import java.nio.charset.StandardCharsets;

class ByteArrayBuilder{
	
	private static final int DEFAULT_SIZE = 2048;

	private byte[] array;
	private int count;
	
	ByteArrayBuilder(final int size){
		array = new byte[size];
	}
	
	ByteArrayBuilder(){
		this(DEFAULT_SIZE);
	}
	
	public void add(final byte b){
		updateArray();
		array[count] = b;
		count++;
	}
	
	public void add(final String string){
		add(string.getBytes(StandardCharsets.UTF_8));
	}
	
	public void add(final byte[] bs){
		for(int i = 0; i < bs.length; i++)
			add(bs[i]);
	}
	
	private void updateArray(){
		if(array.length > count)
			return;
		final int l = count + DEFAULT_SIZE;
		final byte[] array = new byte[l];
		for(int i = 0; i < this.array.length; i++)
			array[i] = this.array[i];
		this.array = array;
	}
	
	public byte[] getArray(){
		if(this.array.length == count)
			return array;
		final byte[] array = new byte[count];
		for(int i = 0; i < count; i++)
			array[i] = this.array[i];
		return array;
	}
	
	public byte[] getArray(final int begin, final int end){
		final int min = Math.min(end, count);
		if(this.array.length == min)
			return array;
		final byte[] array = new byte[min];
		for(int i = begin; i < min; i++)
			array[i] = this.array[i];
		return array;
	}
	
	public final int length(){
		return count;
	}
	
	@Override
	public String toString(){
		return toString(getArray());
	}
	
	public static final String toString(final byte[] array){
		final StringBuilder out = new StringBuilder();
		for(int i = 0; i < array.length; i++){
			out.append('[');
			out.append(array[i]);
			out.append(']');
			if(i != array.length - 1)
				out.append(',');
		}
		return out.toString();
	}
	
	public void clear(){
		count = 0;
	}
	
}
