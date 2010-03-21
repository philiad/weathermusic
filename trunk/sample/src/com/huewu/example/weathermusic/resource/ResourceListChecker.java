package com.huewu.example.weathermusic.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import android.util.Log;
import android.util.Xml;
import android.util.Xml.Encoding;


public class ResourceListChecker {
	
	private final String TAG = "ResourceListChecker";
	private ArrayList<NetworkResource> resources = null;
	
	public ResourceListChecker(InputStream is){
		try {
			resources = new ArrayList<NetworkResource>();
			Xml.parse(is, Encoding.UTF_8, new ResourceListParser());
			is.close();
		} catch (IOException e) {
		} catch (SAXException e) {
		}
	}
	
	public boolean isAllResourcListAvailable(){
		boolean isAllAvailable = true;
		for(NetworkResource res : resources){
			checkResource(res);
			if( isResourceAvailable(res) == false )
				isAllAvailable = false;
		}
		
		return isAllAvailable;
	}
	
	public NetworkResource[] getResourceList(){
		NetworkResource[] res = new NetworkResource[resources.size()];
		return resources.toArray(res);
	}
	
	public NetworkResource[] getMissingResourceList(){
		
		ArrayList<NetworkResource> missingList = new ArrayList<NetworkResource>();
		
		for(NetworkResource res : resources){
			if( isResourceAvailable(res) == false )
				missingList.add(res);
		}
		
		NetworkResource[] res = new NetworkResource[missingList.size()];
		return missingList.toArray(res);
	}
	
	public long getRequiredSize(){
		long size = 0;
		for(NetworkResource res : resources){
			size += res.totalSize - res.currentSize;
		}
		return size;
	}
	
	private void checkResource(NetworkResource res){
		File file = new File(res.data);
		if( file.exists() == true )
			res.currentSize = file.length();
		else
			res.currentSize = 0;
	}	
	
	private boolean isResourceAvailable(NetworkResource res){
		//TODO Check resouce integritiy
		if(res.currentSize == res.totalSize)
			return true;
		return false;
	}

	//ResourceList XML Parser.
	private class ResourceListParser implements ContentHandler{
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
		}
		
		@Override
		public void startElement(String uri, String localName,
				String name, Attributes atts) throws SAXException {
			Log.e(TAG, "Start Element: " + localName);
			NetworkResource res = NetworkResource.parseResource(uri,localName,name,atts);
			if(res != null)
				resources.add(res);
		}		

		@Override
		public void endDocument() throws SAXException {
			Log.e(TAG, "Parse End Res Size: " + resources.size());
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
		}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		@Override
		public void processingInstruction(String target, String data)
				throws SAXException {
		}

		@Override
		public void setDocumentLocator(Locator locator) {
		}

		@Override
		public void skippedEntity(String name) throws SAXException {
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
		}	
	}
}//end of class
