package org.nkigen.eqr.simulation;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.maps.routing.EQRPoint;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ParseinitXMLFile {

	File file;
	SimulationParamsMessage params = new SimulationParamsMessage();

	public ParseinitXMLFile(File file) {
		// TODO Auto-generated constructor stub
		this.file = file;
	}

	public void Parse() {
		System.out.println(" Parsing config file");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			/* Parse Patients */
			NodeList nList = doc.getElementsByTagName("patient-locations");
			parsePatientLocations(nList);
			nList = doc.getElementsByTagName("fire-locations");
			parseFireLocations(nList);
			nList = doc.getElementsByTagName("hospital-locations");
			parseHospitalLocations(nList);
			nList = doc.getElementsByTagName("fire-engine-location");
			parseFireEngineLocations(nList);
			nList = doc.getElementsByTagName("ambulance-locations");
			parseAmbulanceLocations(nList);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public SimulationParamsMessage getSimulationParams(){
		return params;
	}
	private EQRPoint getPoint(String lat, String lon) {
		return new EQRPoint(Double.parseDouble(lat), Double.parseDouble(lon));
	}

	private void parsePatientLocations(NodeList nList) {

		ArrayList<EQRPoint> points = new ArrayList<EQRPoint>();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
			NodeList loc = eElement.getChildNodes();
			System.out.println(nList.getLength() + " Current Element :"
					+ nNode.getNodeName());
			for (int i = 0; i < loc.getLength(); i++) {
				Node nLoc = loc.item(i);
				if (nLoc.getNodeType() == Node.ELEMENT_NODE) {
					NodeList l = nLoc.getChildNodes();
					String lat = l.item(1).getTextContent();
					String lon = l.item(3).getTextContent();
					System.out.println("New Patient Location at :" + lat + ", "
							+ lon);
					points.add(getPoint(lat, lon));
				}

			}
		}
		params.setPatients(points);
	}

	private void parseFireLocations(NodeList nList) {

		ArrayList<EQRPoint> points = new ArrayList<EQRPoint>();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
			NodeList loc = eElement.getChildNodes();
			System.out.println(nList.getLength() + " Current Element :"
					+ nNode.getNodeName());
			for (int i = 0; i < loc.getLength(); i++) {
				Node nLoc = loc.item(i);
				if (nLoc.getNodeType() == Node.ELEMENT_NODE) {
					NodeList l = nLoc.getChildNodes();
					String lat = l.item(1).getTextContent();
					String lon = l.item(3).getTextContent();
					System.out.println("New Fire Location at :" + lat + ", "
							+ lon);
					points.add(getPoint(lat, lon));
				}

			}
		}
		params.setFires(points);
	}

	private void parseHospitalLocations(NodeList nList) {

		ArrayList<EmergencyResponseBase> bases = new ArrayList<EmergencyResponseBase>();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
			NodeList loc = eElement.getChildNodes();
			System.out.println(nList.getLength() + " Current Element :"
					+ nNode.getNodeName());
			for (int i = 0; i < loc.getLength(); i++) {
				Node nLoc = loc.item(i);
				if (nLoc.getNodeType() == Node.ELEMENT_NODE) {
					NodeList l = nLoc.getChildNodes();
					String lat = l.item(1).getTextContent();
					String lon = l.item(3).getTextContent();
					System.out.println("New Hospital Location at :" + lat
							+ ", " + lon);
					EmergencyResponseBase base = new EmergencyResponseBase(
							EmergencyResponseBase.HOSPITAL_BASE);
					base.setLocation(getPoint(lat, lon));
					base.setMax(1);
					bases.add(base);
				}

			}
		}
		params.setHospitals(bases);
	}

	private void parseFireEngineLocations(NodeList nList) {

		ArrayList<EmergencyResponseBase> bases = new ArrayList<EmergencyResponseBase>();

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
			NodeList loc = eElement.getChildNodes();
			System.out.println(nList.getLength() + " Current Element :"
					+ nNode.getNodeName());
			for (int i = 0; i < loc.getLength(); i++) {
				Node nLoc = loc.item(i);
				if (nLoc.getNodeType() == Node.ELEMENT_NODE) {
					NodeList l = nLoc.getChildNodes();
					int num = Integer.parseInt(l.item(1).getTextContent());
					String lat = l.item(3).getTextContent();
					String lon = l.item(5).getTextContent();
					System.out.println(num + " New Engine Location at :" + lat
							+ ", " + lon);
					EmergencyResponseBase base = new EmergencyResponseBase(
							EmergencyResponseBase.FIREENGINE_BASE);
					base.setLocation(getPoint(lat, lon));
					base.setMax(num);
					bases.add(base);
				}

			}
		}
	}

	private void parseAmbulanceLocations(NodeList nList) {

		ArrayList<EmergencyResponseBase> bases = new ArrayList<EmergencyResponseBase>();

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
			NodeList loc = eElement.getChildNodes();
			System.out.println(nList.getLength() + " Current Element :"
					+ nNode.getNodeName());
			for (int i = 0; i < loc.getLength(); i++) {
				Node nLoc = loc.item(i);
				if (nLoc.getNodeType() == Node.ELEMENT_NODE) {
					NodeList l = nLoc.getChildNodes();
					int num = Integer.parseInt(l.item(1).getTextContent());
					String lat = l.item(3).getTextContent();
					String lon = l.item(5).getTextContent();
					System.out.println(num + " New ambulance Location at :"
							+ lat + ", " + lon);
					EmergencyResponseBase base = new EmergencyResponseBase(
							EmergencyResponseBase.AMBULANCE_BASE);
					base.setLocation(getPoint(lat, lon));
					base.setMax(num);
					bases.add(base);
					
				}

			}
		}
	}

	public static void main(String[] a) {
		new ParseinitXMLFile(new File(
				"/home/nkigen/development/git/EQR/src/config.xml")).Parse();
		;
	}
}
