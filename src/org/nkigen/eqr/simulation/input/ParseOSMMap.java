package org.nkigen.eqr.simulation.input;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.xml.sax.SAXException;

import info.pavie.basicosmparser.controller.OSMParser;
import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;

public class ParseOSMMap {
	OSMParser parser;
	Map<String, Element> result;
	public ParseOSMMap(String file) {
		parser = new OSMParser();
		try {
			result = parser.parse(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OSMParser getParser() {
		return parser;
	}


	public void setParser(OSMParser parser) {
		this.parser = parser;
	}


	public Map<String, Element> getResult() {
		return result;
	}


	public void setResult(Map<String, Element> result) {
		this.result = result;
	}	

}
