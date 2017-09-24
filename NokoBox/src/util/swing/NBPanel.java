package util.swing;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class NBPanel extends JPanel implements NBTheamHolder { 
	
	public ButtonSetings    buttonSets;
	public TextFieldSetings textfeldSets;
	public LabelSetings     labelSets;
	public TextAreaSetings  textareaSets;
	
	public NBPanel(JPanel panel) {
		
		setSetings();
		
	}
	
	public NBPanel() {
		setSetings();
	}
	
	private void setSetings(){
		this.buttonSets = new ButtonSetings();
		this.textfeldSets = new TextFieldSetings();
		this.labelSets = new LabelSetings();
		this.textareaSets = new TextAreaSetings();
	}
	

	@Override
	public void applySentings(JComponent component) {
		if (component instanceof NBButton) {
			buttonSets.applySettings((NBButton) component);
		} else if (component instanceof NBTextField ) {
			textfeldSets.applySettings((NBTextField) component);
		} else if (component instanceof Label) {
			labelSets.applySettings((Label) component);
		} else if (component instanceof TextArea) {
			textareaSets.applySettings((TextArea) component);
		}
	}
	
	
	
	

}
