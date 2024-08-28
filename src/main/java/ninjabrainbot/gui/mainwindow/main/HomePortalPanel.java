package ninjabrainbot.gui.mainwindow.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;
import ninjabrainbot.model.datastate.homeportal.HomePortalResult;
import ninjabrainbot.util.I18n;
import ninjabrainbot.gui.components.labels.ColorMapLabel;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.labels.TwoColorLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;

class HomePortalPanel extends ThemedPanel {

	public final ColorMapLabel distanceLabel, heightLabel;
	public final ThemedLabel spacerLabel;
	public final TwoColorLabel posLabel;


	public HomePortalPanel(StyleManager styleManager) {
		super(styleManager);
		setLayout(new GridBagLayout());
		setAlignmentX(0);
		posLabel = new TwoColorLabel(styleManager);
		spacerLabel = new ThemedLabel(styleManager);
		distanceLabel = new ColorMapLabel(styleManager, true, false);
		heightLabel = new ColorMapLabel(styleManager, true, false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1;
		add(posLabel, gbc);
		add(spacerLabel, gbc);
		add(distanceLabel, gbc);
		add(heightLabel, gbc);
		gbc.weighty = 1;
		add(Box.createGlue(), gbc);

		setBackgroundColor(styleManager.currentTheme.COLOR_SLIGHTLY_WEAK);
		posLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK,
									styleManager.currentTheme.TEXT_COLOR_HEADER);
		distanceLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
		heightLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_SLIGHTLY_WEAK);
	}

	public void setResult(HomePortalResult result) {
		if (result == null) {
			posLabel.setText("");
			distanceLabel.clear();
			heightLabel.clear();
			return;
		}
		posLabel.setText(I18n.get("homeportal.position_text"), I18n.get("homeportal.position_values", result.x, result.y, result.z));
		spacerLabel.setText(" ");
		if(result.showDirection) {
			distanceLabel.setText(I18n.get("homeportal.distance", (int)result.distance, (float)(result.angle * 180.0 / Math.PI)));
			distanceLabel.setColoredText(result.getFormatedAngleDiff(), result.getAngleDiffColor());
			
			heightLabel.setText(I18n.get("homeportal.height_text"));
			heightLabel.setColoredText(I18n.get("homeportal.height_value", (int)result.getHeightDiff(), result.getAboveBelow()), result.getHeightDiffColor());
		}
	}

	@Override
	public void updateColors() {
		super.updateColors();
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		setPreferredSize(new Dimension(0, 3 * (styleManager.size.PADDING + styleManager.size.TEXT_SIZE_MEDIUM) + 2 * styleManager.size.PADDING_THIN));
		setBorder(new EmptyBorder(styleManager.size.PADDING_THIN, styleManager.size.PADDING, styleManager.size.PADDING_THIN, styleManager.size.PADDING));
		super.updateSize(styleManager);
	}

}