package org.nkigen.maps.viewer.updates;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;

public class EQRStatsFire extends EQRStatusPanelItem {

	long num_incidents;
	long num_cleared;
	
	
	public EQRStatsFire() {
	super();
	}
	@Override
	public Component getDisplayItem() {
		JLabel lbl = new JLabel();
		lbl.setFont(new Font("Serif", Font.PLAIN, 12));
		String text = "<html><b>id</b> "+ getItemId()+
				"<br /> <b>Incidences</b> "+num_incidents +
				"<br /><b>Attended </b> " + num_cleared +"<br/></html>";
		lbl.setText(text);
		return lbl;
	}

	@Override
	public String getDisplayText() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getNumIncidents() {
		return num_incidents;
	}

	public synchronized void setNumIncidents(long num_incidents) {
		this.num_incidents = num_incidents;
	}

	public long getNumCleared() {
		return num_cleared;
	}

	public synchronized void setNumCleared(long num_cleared) {
		this.num_cleared = num_cleared;
	}

}
