package ninjabrainbot.gui.components.labels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JPanel;

import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class TwoColorLabel extends JPanel implements ILabel{
	ThemedLabel label1, label2;

	public TwoColorLabel(StyleManager styleManager) {
		label1 = new ThemedLabel(styleManager);
		label2 = new ThemedLabel(styleManager);
		
		setLayout(new GridBagLayout());
		setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.weightx = 0;
		add(label1);
		add(label2);
		gbc.weightx = 1;
		add(Box.createGlue(), gbc);
		setAlignmentX(0);
	}

	@Override
	public void setText(String text) {
		setText(text, "");
	}
	
	public void setText(String text1, String text2) {
		label1.setText(text1);
		label2.setText(text2);
	}
	
	public void setForegroundColor(WrappedColor color1, WrappedColor color2) {
		label1.setForegroundColor(color1);
		label2.setForegroundColor(color2);
	}
}
