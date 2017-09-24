package util.swing;

import java.awt.Rectangle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import util.Window;
import util.adds.UpdateAdd;

public class TextList extends JList<String> {
	private static final long serialVersionUID = 1L;

	private DefaultListModel<String> listModel;
	private JScrollPane sp;

	public TextList(Window window, Rectangle rec) {
		setModel(listModel = new DefaultListModel<String>());
		sp = new JScrollPane(this);
		sp.setBounds(rec);
		
		
		
		addAncestorListener(new AncestorListener() {
			public void ancestorRemoved(AncestorEvent event) {
				System.out.println(event);
			}
			public void ancestorMoved(AncestorEvent event) {
//				System.out.println(event);
				
			}
			public void ancestorAdded(AncestorEvent event) {
				System.out.println(event);
				
			}
		});
		
		
		
		
		TextFieldSetings s = window.textfeldSets;


		if(rec == null)
			rec = new Rectangle();
		
		setBackground(s.bColor);
		setForeground(s.tColor);
		setFont(s.font);
		sp.setBorder(s.border);
		window.panel.add(sp);
		
	}

	public TextList(Window window) {
		this(window, new Rectangle());
	}

	public void addElement(int index, String element) {
		listModel.add(index, element);
	}
	
	public void addImages(String... elements) {
		for (String s : elements)
			addElement(0, s);
	}
	
	public void setBounds(Rectangle r){
		sp.setBounds(r);
	}

}
