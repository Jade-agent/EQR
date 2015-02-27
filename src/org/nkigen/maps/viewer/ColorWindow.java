package org.nkigen.maps.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorWindow extends JFrame {

	/*
	Color[] colors = {
			EQRViewerPoint.WAITING_PATIENT_COLOR,
			EQRViewerPoint.
	}
	*/
	public ColorWindow() {
		// TODO Auto-generated constructor stub
		super("Color Codes");
		setSize(200, 600);
		GridLayout layout = new GridLayout(11, 2);
		JPanel panel = new JPanel(layout);

		for (int i = 0; i < 11; i++)
			panel.add(new CircleJPanel(Color.RED), i, 0);
		add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	class CircleJPanel extends JPanel {

		int r = 30;
		int x= 40, y=40;
		Color c;

		public CircleJPanel(Color c) {
			// TODO Auto-generated constructor stub
			this.c = c;
		}

		@Override
		public void paint(Graphics g) {
			 Graphics2D ga = (Graphics2D)g;
			x = x - (r / 2);
			y = y - (r / 2);
			ga.setColor(c);
			ga.fillOval(x, y, r, r);
			ga.setPaint(c);
			
		}
	}
	
	/*
	public static void main(String[] args){
		new ColorWindow();
	}
	*/
}
