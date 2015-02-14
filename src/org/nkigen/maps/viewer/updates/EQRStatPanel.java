package org.nkigen.maps.viewer.updates;

import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class EQRStatPanel extends JPanel{
	
	private String title;
	private String toolTip;
	private JPanel inner;
	public EQRStatPanel(String title){
	 super();	
	 this.title = title;
	 setBorder(BorderFactory.createTitledBorder(title));
	 setToolTipText(title);
	 inner = new JPanel();
	 add(inner);
	}

}
