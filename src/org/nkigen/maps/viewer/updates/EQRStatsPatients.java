package org.nkigen.maps.viewer.updates;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;

public class EQRStatsPatients extends EQRStatusPanelItem {

	long num_died;
	long num_saved;
	long total_patients;
	 public EQRStatsPatients() {
		super();
		}
	@Override
	public Component getDisplayItem() {
		JLabel lbl = new JLabel();
		lbl.setFont(new Font("Serif", Font.PLAIN, 12));
		String text = "<html><b>id</b> "+ getStatusId()+
				"<br /> <b>Died</b> "+num_died +
				"<br /><b>Rescued </b> " + num_saved +
				"<br /><b> Total Patients </b> :"+ total_patients +"<br/></html>";
		lbl.setText(text);
		return lbl;
	}

	@Override
	public String getDisplayText() {
		return null;
	}

	public long getNum_died() {
		return num_died;
	}

	public void setNum_died(long num_died) {
		this.num_died = num_died;
	}

	public long getNum_saved() {
		return num_saved;
	}

	public void setNum_saved(long num_saved) {
		this.num_saved = num_saved;
	}

	public long getTotal_patients() {
		return total_patients;
	}

	public void setTotal_patients(long total_patients) {
		this.total_patients = total_patients;
	}

}
