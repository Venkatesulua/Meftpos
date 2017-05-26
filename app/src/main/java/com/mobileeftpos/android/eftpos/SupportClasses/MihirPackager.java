package com.mobileeftpos.android.eftpos.SupportClasses;
/**
 * Created by venkat on 5/19/2017.
 */

import java.io.IOException;
import java.io.InputStream;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MihirPackager extends GenericPackager {

	public MihirPackager() throws ISOException {
		super();
	}

	public MihirPackager(String filename) throws ISOException {
		this();
		readFile(filename);
	}

	public MihirPackager(InputStream inputstream) throws ISOException {
		this();
		readFile(inputstream);
	}

	protected XMLReader createXMLReader() throws SAXException {
		XMLReader reader = null;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp;
		try {
			sp = spf.newSAXParser();
			reader = sp.getXMLReader();
			GenericContentHandler handler = new GenericContentHandler();
			reader.setContentHandler(handler);
			reader.setErrorHandler(handler);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reader;
	}

	public void readFile(InputStream input) throws ISOException {
		try {
			createXMLReader().parse(new InputSource(input));
		} catch (Exception e) {
			throw new ISOException(e);
		}
	}
}