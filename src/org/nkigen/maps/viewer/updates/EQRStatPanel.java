package org.nkigen.maps.viewer.updates;

import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class EQRStatPanel extends JPanel{
	
	private String title;
	private String toolTip;
	private JPanel inner;
	JScrollPane scrollPane;
	public EQRStatPanel(String title){
	 super();	
	 this.title = title;
	 setBorder(BorderFactory.createTitledBorder(title));
	 setToolTipText(title);
	 inner = new JPanel();
	 add(inner);
	}
	
	public JPanel getInnerPane(){
		System.out.println(getSize());
		return inner;
	}
	
public void refresh(){
	inner.revalidate();
	inner.repaint();
	//scrollPane.revalidate();
	//scrollPane.repaint();
}

}
