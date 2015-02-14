package org.nkigen.maps.viewer.updates;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EQRUpdateWindow extends JFrame{
	private final static String window_title = "EQR: Status Updates";
	EQRUpdateWindow window;
	JPanel main_panel;
	JPanel patients_panel;
	JPanel fire_panel;
	JPanel stat_patients;
	JPanel stat_fires;
	JPanel loc_ambulance;
	JPanel loc_fire_engines;
	
	 protected EQRUpdateWindow(){
		 super(window_title);
		 setSize(800,300);
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
		 
		 main_panel.add(patients_panel);
		 main_panel.add(fire_panel);
		 main_panel.add(stat_patients);
		 main_panel.add(stat_fires);
		 main_panel.add(loc_ambulance);
		 main_panel.add(loc_fire_engines);
		 
	 }
	 
	 
	 public EQRUpdateWindow updateStatusPanel(JPanel panel, ArrayList<EQRStatusPanelItem> items){
	 	 if(items.size() == 0){
	 		 if(panel !=null)
	 			 panel.add(new JLabel("INFO not available now"), null);
	 	 }
	 	 else{
	 		 for(int i=0; i<items.size();++i)
	 			 panel.add(items.get(i), null);
	 		 
	 	 }
	 	 panel.repaint();
	 	 return window;
	 }
	 
	 public void refreshWindow(){
		 getInstance().repaint();
	 }

	 public static void main(String[] args){
		 new EQRUpdateWindow().getInstance().setVisible(true);
	 }
}
