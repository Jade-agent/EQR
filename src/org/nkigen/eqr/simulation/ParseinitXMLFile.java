package org.nkigen.eqr.simulation;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.maps.routing.EQRPoint;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
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

			parseSimulationParams(doc);
			parseRoutingParams(doc.getElementsByTagName("routing"));
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
			System.out.println("Simulations Params :"+ params);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SimulationParamsMessage getSimulationParams() {
		return params;
	}

	private EQRPoint getPoint(String lat, String lon) {
		return new EQRPoint(Double.parseDouble(lat), Double.parseDouble(lon));
	}

	private void parseSimulationParams(Document doc) {
		NodeList nList = doc.getElementsByTagName("simulation");
		NodeList pList = doc.getElementsByTagName("patients");
		NodeList fList = doc.getElementsByTagName("fires");
		NodeList tList = doc.getElementsByTagName("traffic");
		NamedNodeMap items = null;
		NamedNodeMap fItems = null;
		NamedNodeMap pItems = null;
		NamedNodeMap tItems = null;
		try {
			Node nNode = nList.item(0);
			Node fNode = fList.item(0);
			Node pNode = pList.item(0);
			Node tNode = tList.item(0);
			items = nNode.getAttributes();
			fItems = fNode.getAttributes();
			pItems = pNode.getAttributes();
			tItems = tNode.getAttributes();
			
			params.setRate(Double.valueOf(items.getNamedItem("rate")
					.getTextContent()));
			params.setFire_inter_arrival(Long.valueOf(fItems.getNamedItem(
					"inter-arrival").getTextContent()));
			params.setPatient_inter_arrival(Long.valueOf(pItems.getNamedItem(
					"inter-arrival").getTextContent()));
			params.setTraffic_max_delay(Long.valueOf(tItems.getNamedItem(
					"max-delay").getTextContent()));
			params.setTraffic_update_period(Long.valueOf(tItems.getNamedItem(
					"update-period").getTextContent()));
		

		} catch (NullPointerException e) {
		e.printStackTrace();
		}

	}

	private void parseRoutingParams(NodeList nList) {

		try {
			Node nNode = nList.item(0);
			NamedNodeMap items = nNode.getAttributes();
			System.out.println("Data Dir:"
					+ items.getNamedItem("data-dir").getTextContent());
			System.out.println("Routing File:"
					+ items.getNamedItem("config-file").getTextContent());

			params.setRouting_data_dir(items.getNamedItem("data-dir")
					.getTextContent());
			params.setRouting_config_file(items.getNamedItem("config-file")
					.getTextContent());

		} catch (NullPointerException e) {
			System.out
					.println("ERROR: Using default Simulation rate value of : " + 0.1);
		}

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
		params.setFire_engines(bases);
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
		params.setAmbulances(bases);
	}

	 /*
	public static void main(String[] a) {
		new ParseinitXMLFile(new File(
				"/home/nkigen/development/git/EQR/src/config.xml")).Parse();
		;
	}
	 */
}
