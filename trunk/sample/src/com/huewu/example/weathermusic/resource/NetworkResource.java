package com.huewu.example.weathermusic.resource;

import java.text.AttributedCharacterIterator.Attribute;

import org.xml.sax.Attributes;

//represent one network resource.
public class NetworkResource {
	public String uri;
	public String data; 
	public String title;
	public long totalSize;
	public long currentSize;
	public String signature;

	/**
	 * In order to create NetworkResouce Class, call factory method parseResource.
	 */
	private NetworkResource(){
		
	}

	/**
	 * Create NetworkResource Class.
	 * @param uri
	 * @param localName
	 * @param name
	 * @param atts
	 * @return null or new NetworkResource.
	 */
	public static NetworkResource parseResource(String uri, String localName,
			String name, Attributes atts){
		
		if(localName.equalsIgnoreCase("resource") == false)
			return null;
		
		NetworkResource res = new NetworkResource();

		String value = null;

		value = atts.getValue("uri");
		if(value == null)
			return null;
		res.uri = value;
		
		value = atts.getValue("data");
		if(value == null)
			return null;
		res.data = value;

		value = atts.getValue("title");
		if(value == null)
			return null;
		res.title = value;
		
		value = atts.getValue("totalSize");
		try{
			res.totalSize = Long.parseLong(value);
		}catch(Exception e){
			return null;
		}
		
		value = atts.getValue("signature");
		if(value == null)
			return null;
		res.signature = value;

		return res;
	}
}//end of class
