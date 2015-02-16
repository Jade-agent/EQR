package org.nkigen.maps.viewer.updates;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.nkigen.maps.routing.EQRPoint;

public class EQRUpdateWindow extends JFrame{
	private final static String window_title = "EQR: Status Updates";
	EQRUpdateWindow window;
	JPanel main_panel;
	EQRStatPanel patients_panel;
	EQRStatPanel fire_panel;
	EQRStatPanel stat_patients;
	EQRStatPanel stat_fires;
	EQRStatPanel loc_ambulance;
	EQRStatPanel loc_fire_engines;
	
	ArrayList<EQRPatientStatusItem> items_patients_panel;
	ArrayList<EQRFiresUpdatesItem> items_fires_panel;
	ArrayList<EQRAmbulanceLocations> items_ambulance_location;
	EQRStatsPatients stat_patient_item;
	EQRStatsFire stat_fires_item;
	
	 protected EQRUpdateWindow(){
		 super(window_title);
		 setSize(800,500);
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 setResizable(false);
		 createPanels();
		 this.add(main_panel);
	 }
	 
	 public EQRUpdateWindow getInstance(){
		 if(window == null){
			 window = new EQRUpdateWindow();
		 }
		 
		 return window;
	 }
	 
	 private void createPanels(){
		 main_panel = new JPanel(new GridLayout(3, 2));
		 fire_panel = new EQRStatPanel("Fires");
		 stat_patients = new EQRStatPanel("Patient Stats");
		 stat_fires = new EQRStatPanel("Fire Stats");
		 loc_ambulance = new EQRStatPanel("Ambulance Locations");
		 loc_fire_engines = new EQRStatPanel("Fire Engine Locations");
		 patients_panel = new EQRStatPanel("Patients");
		 
		 main_panel.add(new JScrollPane(patients_panel));
		 main_panel.add(new JScrollPane(fire_panel));
		 main_panel.add(stat_patients);
		 main_panel.add(stat_fires);
		 main_panel.add(new JScrollPane(loc_ambulance));
		 main_panel.add(loc_fire_engines);
		 
		 
		 items_patients_panel = new ArrayList<EQRPatientStatusItem>();
		 items_fires_panel = new ArrayList<EQRFiresUpdatesItem>();
		 stat_patient_item = new EQRStatsPatients();
		 stat_fires_item = new  EQRStatsFire();
		 items_ambulance_location = new ArrayList<EQRAmbulanceLocations>();
	 }
	 
	 public EQRUpdateWindow newItem(int type, EQRStatusPanelItem item){
		 switch(type){
		 case EQRStatusPanelItem.PATIENT_STATUS_ITEM:
			 items_patients_panel.add((EQRPatientStatusItem)item);
			 break;
		 case EQRStatusPanelItem.FIRE_STATUS_ITEM:
			items_fires_panel.add((EQRFiresUpdatesItem)item);
			 break;
		 case EQRStatusPanelItem.STAT_PATIENT_ITEM:
			 	stat_patient_item = (EQRStatsPatients)item;
			 	break;
		 case EQRStatusPanelItem.STAT_FIRE_ITEM:
			 stat_fires_item = (EQRStatsFire)item;
			 break;
		 case EQRStatusPanelItem.AMBULANCE_LOCATION_ITEM:
			 items_ambulance_location.add((EQRAmbulanceLocations)item);
			 break;
		 default:
				 System.out.println("Status Item not recognized");
				 break;
		 }
		 return window;
	 }
	 
	 private void refreshPatientsPanel(){
		 patients_panel.getInnerPane(). removeAll();
		 JLabel lbl = new JLabel();
		 lbl.setFont(new Font("Serif", Font.PLAIN, 14));
		 String txt = "<html>";
		 for(int i=0; i< items_patients_panel.size(); i++)
			txt += items_patients_panel.get(i).getDisplayText();
		 txt+="</html>";
		 lbl.setText(txt);
		 patients_panel.getInnerPane().add(lbl);
		 patients_panel.refresh();
	 }
	 
	 private void refreshStats(){
		 stat_patients.getInnerPane().removeAll();
   		 stat_patients.getInnerPane().add(stat_patient_item.getDisplayItem());
		 stat_patients.refresh();
		 
		 stat_fires.getInnerPane().removeAll();
   		 stat_fires.getInnerPane().add(stat_fires_item.getDisplayItem());
		 stat_fires.refresh();
	 }
	 
	 private void refreshLocationAmbulance(){
		 loc_ambulance.getInnerPane().removeAll();
		 JLabel lbl = new JLabel();
		 lbl.setFont(new Font("Serif", Font.PLAIN, 14));
		 String txt = "<html>";
		 for(int i=0; i< items_ambulance_location.size(); i++)
			txt += items_ambulance_location.get(i).getDisplayText();
		 txt+="</html>";
		 lbl.setText(txt);
		 loc_ambulance.getInnerPane().add(lbl);
		 loc_ambulance.refresh(); 
	 }
	 private void refreshFiresPanel(){
		 fire_panel.getInnerPane().removeAll();
		 JLabel lbl = new JLabel();
		 lbl.setFont(new Font("Serif", Font.PLAIN, 14));
		 String txt = "<html>";
		 for(int i=0; i< items_fires_panel.size(); i++)
			txt += items_fires_panel.get(i).getDisplayText();
		 txt+="</html>";
		 lbl.setText(txt);
		 fire_panel.getInnerPane().add(lbl);
		 fire_panel.refresh();
	 }
	 private void refreshPanels(){
		refreshPatientsPanel();
		refreshFiresPanel();
		refreshLocationAmbulance();
		refreshStats();
		 
	 }
	 public void refreshWindow(){
		 refreshPanels();
		 //getInstance().repaint();
	 }

	 public static void main(String[] args){
		 EQRUpdateWindow win =new EQRUpdateWindow().getInstance();
		 win.setVisible(true);
		 EQRPoint p = new EQRPoint(0.00332, 22.123112);
		 p.setMillis(7832);
		 EQRPatientStatusItem item = new EQRPatientStatusItem();
		 item.setClosest_vehicle_loc(p);
		 item.setPatientLocation(p);
		 item.setDeadline(21322);
		 item.setEst_time_to_reach(182);
		 
		 EQRPatientStatusItem item1 = new EQRPatientStatusItem();
		 item1.setClosest_vehicle_loc(p);
		 item1.setPatientLocation(p);
		 item1.setDeadline(21323);
		 item1.setEst_time_to_reach(1827);
		 
		 EQRFiresUpdatesItem fi = new EQRFiresUpdatesItem();
		 fi.setClosest_engine(p);
		 fi.setFireLocation(p);
		 fi.setEst_time_to_reach(3242);
		 
		 EQRAmbulanceLocations loc = new EQRAmbulanceLocations();
		 loc.setHeading(p);
		 loc.setLocation(p);
		 loc.isAtBase(false);
		 EQRAmbulanceLocations loc1 = new EQRAmbulanceLocations();
		 loc1.setHeading(p);
		 loc1.setLocation(p);
		 loc1.isAtBase(true);
		 win.newItem(EQRStatusPanelItem.AMBULANCE_LOCATION_ITEM, loc);
		 win.newItem(EQRStatusPanelItem.AMBULANCE_LOCATION_ITEM, loc1);
		 win.newItem(EQRStatusPanelItem.AMBULANCE_LOCATION_ITEM, loc);
		 //loc.isAtBase(true);
		 win.newItem(EQRStatusPanelItem.AMBULANCE_LOCATION_ITEM, loc1);
		 win.newItem(EQRStatusPanelItem.PATIENT_STATUS_ITEM, item);
		 win.newItem(EQRStatusPanelItem.PATIENT_STATUS_ITEM, item);
		 win.newItem(EQRStatusPanelItem.PATIENT_STATUS_ITEM, item1);
		 win.newItem(EQRStatusPanelItem.PATIENT_STATUS_ITEM, item1);
		 win.newItem(EQRStatusPanelItem.FIRE_STATUS_ITEM, fi);
		 win.newItem(EQRStatusPanelItem.FIRE_STATUS_ITEM, fi);
		 win.newItem(EQRStatusPanelItem.FIRE_STATUS_ITEM, fi);
		 win.refreshWindow();
		 
	 }
}
