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
		 main_panel.add(fire_panel);
		 main_panel.add(stat_patients);
		 main_panel.add(stat_fires);
		 main_panel.add(loc_ambulance);
		 main_panel.add(loc_fire_engines);
		 
		 
		 items_patients_panel = new ArrayList<EQRPatientStatusItem>();
	 }
	 
	 public EQRUpdateWindow newItem(int type, EQRStatusPanelItem item){
		 switch(type){
		 case EQRStatusPanelItem.PATIENT_STATUS_ITEM:
			 items_patients_panel.add((EQRPatientStatusItem)item);
			 break;
			 default:
				 System.out.println("Status Item not recognized");
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
	 private void refreshPanels(){
		refreshPatientsPanel();
		 
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
		 
		 win.newItem(EQRStatusPanelItem.PATIENT_STATUS_ITEM, item);
		 win.newItem(EQRStatusPanelItem.PATIENT_STATUS_ITEM, item);
		 win.newItem(EQRStatusPanelItem.PATIENT_STATUS_ITEM, item1);
		 win.newItem(EQRStatusPanelItem.PATIENT_STATUS_ITEM, item1);
		 win.refreshWindow();
		 
	 }
}
