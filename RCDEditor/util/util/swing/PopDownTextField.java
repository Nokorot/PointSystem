package util.swing;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import util.Window;

public class PopDownTextField extends JComboBox<String> {
	private static final long serialVersionUID = 1L;
	public String code = "-1";

	public int MAX_SIZE;
	public Window window;
	private TextFieldSetings s;
	
	private DefaultComboBoxModel<String> model;

	public PopDownTextField(Window window, Rectangle rec, String code) {
		TextFieldSetings s = window.textfeldSets;


		model = (DefaultComboBoxModel<String>) getModel();
		model.removeAllElements();
		setModel(model);
		
		
		if(rec == null)
			rec = new Rectangle();
		
		if (rec.width < 0)
			rec.width = this.s.width;
		if (rec.height < 0)
			rec.height = this.s.height;
		if (rec.x < 0)
			rec.x = this.s.x;
		if (rec.y < 0)
			rec.y = this.s.y;

		this.window = window;
		if (code != null)
			this.code = code;
		setBackground(s.bColor);
		setForeground(s.tColor);
		setFont(s.font);
		setBorder(s.border);
		setBounds(rec);
		addActionListener(s.pListener);
		window.panel.add(this);
	}
	
	public PopDownTextField(Window window, Rectangle rec) {
		this(window, rec, null);
	}

	public PopDownTextField(Window window, String code) {
		this(window, null, code);
	}

	public PopDownTextField(Window window) {
		this(window, null, null);
	}

	public void setFonrSize(int size) {
		Font f = getFont();
		setFont(new Font(f.getName(), f.getStyle(), size));
	}
	
	public void addElements(String... elements){
		for (String e : elements) 
			model.addElement(e);
	}
	
	public void addElements(List<String> elements){
		for (String e : elements) 
			model.addElement(e);
	}

	public void insertElementAt(String element, int index){
		model.insertElementAt(element, index);
	}
	
	public boolean isSelected(String s){
		if(getSelectedItem().equals(s))
			return true;
		return false;
	}
	
	public void clear(){
		model.removeAllElements();
	}
	
}