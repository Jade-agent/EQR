package org.nkigen.maps.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.nkigen.eqr.messages.TrafficUpdateMessage;

public class TrafficWindow extends JFrame {

	Color[] colors = { Color.GREEN, Color.YELLOW, Color.RED };
	JPanel panel;
	Color color;
	JLabel label;
	long delay;

	public TrafficWindow() {
		super("TRAFFIC UPDATES");
		panel = getPanel();

		add(panel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = (int) rect.getMaxX() - getWidth();
		int y = (int) rect.getMaxY() - getHeight();
		setLocation(x, y);
		setAlwaysOnTop(true);
		setVisible(true);
		updateTraffic(TrafficUpdateMessage.TRAFFIC_LOW, 0);
	}

	public void updateTraffic(int how, long delay) {
		String text = "";
		switch (how) {
		case TrafficUpdateMessage.TRAFFIC_HIGH:
			color = colors[2];
			text = "<html><b>HIGH<b><br /><b>Delay:</b>" + delay + " ms</html>";
			break;
		case TrafficUpdateMessage.TRAFFIC_MEDIUM:
			color = colors[1];
			text = "<html><b>MEDIUM<b><br /><b>Delay:</b>" + delay
					+ " ms</html>";
			break;
		case TrafficUpdateMessage.TRAFFIC_LOW:
			color = colors[0];
			text = "<html><b>NORMAL<b><br /><b>Delay:</b>" + delay
					+ " ms</html>";
			break;
		default:
			color = colors[0];
			text = "<html><b>NORMAL<b><br /><b>Delay:</b>" + delay
					+ " ms</html>";
			break;
		}
		panel.setBackground(color);
		label.setText(text);

		this.revalidate();
		// this.repaint();
	}

	private JPanel getPanel() {
		label = new JLabel();
		label.setFont(new Font("Serif", Font.PLAIN, 12));
		JPanel panel = new JPanel() {

			@Override
			// placeholder for actual content
			public Dimension getPreferredSize() {
				return new Dimension(240, 100);
			}

		};
		panel.add(label);
		return panel;
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * TrafficWindow t = new TrafficWindow();
	 * 
	 * for (int i = 0; i < 3; i++) { try {
	 * 
	 * Thread.sleep(5000); t.updateTraffic(i,i*100+1000);
	 * 
	 * } catch (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * }
	 */
}
