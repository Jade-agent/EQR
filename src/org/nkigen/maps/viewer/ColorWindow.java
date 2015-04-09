package org.nkigen.maps.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ColorWindow extends JFrame {

	Color[] colors = { EQRViewerPoint.WAITING_PATIENT_COLOR,
			EQRViewerPoint.PICKED_PATIENT_COLOR,
			EQRViewerPoint.DEAD_PATIENT_COLOR,
			EQRViewerPoint.DELIVERED_PATIENT_COLOR,
			EQRViewerPoint.WAITING_FIRE_COLOR,
			EQRViewerPoint.ATTENDED_FIRE_COLOR,
			EQRViewerPoint.MOVING_AMBULANCE_COLOR,
			EQRViewerPoint.WAITING_AMBULANCE_COLOR,
			EQRViewerPoint.MOVING_FIRE_COLOR,
			EQRViewerPoint.LOCATION_FIRE_COLOR, EQRViewerPoint.HOSPITAL_COLOR };
	
	String[] names = { "<html>Waiting <br> Patient</html>", "<html>Picked <br>Patient</html>", "<html>Dead <br>Patient</html>",
			"<html>Delivered<br> Patient</html>", "<html>Waiting<br> Fire</html>", "<html>Attended <br>Fire</html>",
			"<html>Moving <br>Ambulance", "<html>Waiting \nAmbulance", "<html>Moving \nFire Engine",
			"<html>Waiting <br>Fire Engine</html>", "Hospital"

	};

	public ColorWindow() {
		// TODO Auto-generated constructor stub
		super("");
		setSize(170, 500);
		GridLayout layout = new GridLayout(11, 2);
		JPanel panel = new JPanel(layout);

		for (int i = 0; i < colors.length; i++){
			panel.add(new CircleJPanel(colors[i]), i, 0);
			panel.add(new JLabel(names[i]), i, 1);
		}
		add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		//setResizable(false);
		setVisible(true);
	}

	class CircleJPanel extends JPanel {

		int r = 20;
		int x = 40, y = 35;
		Color c;

		public CircleJPanel(Color c) {
			// TODO Auto-generated constructor stub
			this.c = c;
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D ga = (Graphics2D) g;
			x = x - (r / 2);
			y = y - (r / 2);
			ga.setColor(c);
			ga.fillOval(x, y, r, r);
			ga.setPaint(c);

		}
	}
/*
	public static void main(String[] args) {
		new ColorWindow();
	}
*/
}
