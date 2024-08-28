package ninjabrainbot.gui.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.inputfields.LimitedThemedTextField;
import ninjabrainbot.gui.components.inputfields.ThemedTextField;
import ninjabrainbot.gui.components.layout.LabeledField;
import ninjabrainbot.gui.components.layout.TitledDivider;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.ConfigurableColor;
import ninjabrainbot.gui.style.theme.CustomTheme;
import ninjabrainbot.gui.style.theme.Theme;
import ninjabrainbot.gui.themeeditor.PreviewButtonInputHandler;
import ninjabrainbot.gui.themeeditor.PreviewCalculatorResult;
import ninjabrainbot.gui.themeeditor.PreviewDataState;
import ninjabrainbot.gui.themeeditor.PreviewUpdateChecker;
import ninjabrainbot.gui.themeeditor.ThemeSerializer;
import ninjabrainbot.gui.themeeditor.panels.ColorPickerPanel;
import ninjabrainbot.gui.themeeditor.panels.ConfigurableColorPanel;
import ninjabrainbot.gui.themeeditor.panels.FramePreviewPanel;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.io.preferences.enums.MainViewType;
import ninjabrainbot.io.preferences.enums.McVersion;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.DetailedPlayerPosition;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.endereye.NormalEnderEyeThrow;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.I18n;

public class ThemeEditorDialog extends ThemedDialog {

	private ConfigurableColorPanel selectedPanel;
	private ColorPickerPanel colorPickerPanel;

	private FramePreviewPanel ninBotPreviewBasic;
	private FramePreviewPanel ninBotPreviewDetailed;

	private final StyleManager previewStyleManager;
	private final NinjabrainBotPreferences preferences;
	private final CustomTheme customTheme; // theme that is saved
	private final CustomTheme previewTheme; // used for previewing

	public ThemeEditorDialog(StyleManager styleManager, NinjabrainBotPreferences preferences, JFrame owner, CustomTheme customTheme) {
		super(styleManager, preferences, owner, I18n.get("settings.themeeditor.themeeditor"));
		Assert.isTrue(SwingUtilities.isEventDispatchThread());
		this.preferences = preferences;
		this.customTheme = customTheme;
		previewTheme = new CustomTheme();
		previewTheme.setFromTheme(customTheme, true);
		previewStyleManager = new StyleManager(previewTheme, SizePreference.REGULAR);

		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setOpaque(false);
		add(panel);
		panel.setLayout(new GridLayout(1, 3));

		panel.add(createMiddlePanel(styleManager));
		panel.add(createConfigurableColorsPanel(styleManager), 0);
		panel.add(createPreviewsPanel(styleManager));

		previewStyleManager.init();
		SwingUtilities.invokeLater(() -> ninBotPreviewBasic.postInit());
		SwingUtilities.invokeLater(() -> ninBotPreviewDetailed.postInit());

		colorPickerPanel.whenColorChanged().subscribe(this::onColorChanged);
	}

	private ThemedPanel createConfigurableColorsPanel(StyleManager styleManager) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new TitledDivider(styleManager, I18n.get("settings.themeeditor.selectedcolor")));
		panel.add(Box.createVerticalStrut(10));

		for (ConfigurableColor cc : previewTheme.getConfigurableColors()) {
			ConfigurableColorPanel ccPanel = new ConfigurableColorPanel(styleManager, previewStyleManager, cc);
			ccPanel.addActionListener(__ -> setSelectedConfigurableColorPanel(ccPanel));
			if (cc == previewTheme.getConfigurableColors().get(0))
				setSelectedConfigurableColorPanel(ccPanel);
			panel.add(ccPanel);
		}
		return panel;
	}

	private ThemedPanel createMiddlePanel(StyleManager styleManager) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new EmptyBorder(10, 0, 10, 0));

		colorPickerPanel = new ColorPickerPanel(styleManager);

		ThemedTextField nameField = new LimitedThemedTextField(styleManager, 16);
		nameField.setText(customTheme.toString());
		nameField.whenTextChanged().subscribe(previewTheme::setName);

		FlatButton selectPresetButton = new FlatButton(styleManager, I18n.get("settings.themeeditor.selectpreset"));
		selectPresetButton.addActionListener(__ -> openSelectPresetDialog());

		FlatButton saveButton = new FlatButton(styleManager, I18n.get("settings.themeeditor.save"));
		saveButton.addActionListener(__ -> saveTheme());

		FlatButton exportButton = new FlatButton(styleManager, I18n.get("settings.themeeditor.copy_theme_string"));
		exportButton.addActionListener(__ -> exportThemeToClipboard());

		FlatButton importButton = new FlatButton(styleManager, I18n.get("settings.themeeditor.paste_theme_string"));
		importButton.addActionListener(__ -> importThemeFromClipboard());

		FlatButton resetColorButton = new FlatButton(styleManager, I18n.get("settings.themeeditor.reset_color"));
		resetColorButton.addActionListener(__ -> resetColor());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0;

		panel.add(new TitledDivider(styleManager, I18n.get("settings.themeeditor.tools")), gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(new LabeledField(styleManager, I18n.get("settings.themeeditor.name_colon"), nameField, true), gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(selectPresetButton, gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(saveButton, gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(exportButton, gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(importButton, gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(new TitledDivider(styleManager, I18n.get("settings.themeeditor.color")), gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(resetColorButton, gbc);
		panel.add(colorPickerPanel, gbc);

		gbc.weighty = 1;
		panel.add(Box.createGlue(), gbc);

		return panel;
	}

	private ThemedPanel createPreviewsPanel(StyleManager styleManager) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		NinjabrainBotPreferences defaultPreferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		ArrayList<IEnderEyeThrow> eyeThrows = new ArrayList<>();

		IEnderEyeThrow t1 = new NormalEnderEyeThrow(new DetailedPlayerPosition(659.70, 80, 1950.30, -253.82, -31.75, false), 0).withCorrection(0.01, 1);
		IEnderEyeThrow t2 = new NormalEnderEyeThrow(new DetailedPlayerPosition(-3.75, 80, 2002.63, -184.67, -31.75, false), 0).withCorrection(-0.01, -1).withToggledAltStd();
		eyeThrows.add(t1);
		eyeThrows.add(t2);
		Fossil f = new Fossil(3);

		NinjabrainBotPreferences previewPreferences1 = new NinjabrainBotPreferences(new UnsavedPreferences());
		previewPreferences1.view.set(MainViewType.BASIC);
		IDataState dataState1 = new PreviewDataState(new PreviewCalculatorResult(McVersion.PRE_119), eyeThrows, f);
		ninBotPreviewBasic = new FramePreviewPanel(new NinjabrainBotFrame(previewStyleManager, previewPreferences1, new PreviewUpdateChecker(), dataState1, new PreviewButtonInputHandler(), new InformationMessageList()));

		NinjabrainBotPreferences previewPreferences2 = new NinjabrainBotPreferences(new UnsavedPreferences());
		previewPreferences2.view.set(MainViewType.DETAILED);
		previewPreferences2.showAngleErrors.set(true);
		IDataState dataState2 = new PreviewDataState(new PreviewCalculatorResult(McVersion.PRE_119), eyeThrows, null);
		dataState2.locked().set(true);
		ninBotPreviewDetailed = new FramePreviewPanel(new NinjabrainBotFrame(previewStyleManager, previewPreferences2, new PreviewUpdateChecker(), dataState2, new PreviewButtonInputHandler(), new InformationMessageList()));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0;
		panel.add(new TitledDivider(styleManager, I18n.get("settings.themeeditor.preview")), gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(ninBotPreviewBasic, gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(ninBotPreviewDetailed, gbc);
		gbc.weighty = 1;
		panel.add(Box.createGlue(), gbc);

		return panel;
	}

	private void openSelectPresetDialog() {
		Object[] themes = Theme.getStandardThemes().toArray();
		Theme chosenTheme = (Theme) JOptionPane.showInputDialog(this, I18n.get("settings.themeeditor.any_unsaved_changes_will_be_lost"), I18n.get("settings.themeeditor.selectpreset"), JOptionPane.PLAIN_MESSAGE, null, themes, themes[0]);
		if (chosenTheme == null)
			return;
		previewTheme.setFromTheme(chosenTheme);
		updateComponents();
	}

	private void saveTheme() {
		customTheme.setFromTheme(previewTheme, true);
	}

	private void resetColor() {
		if (selectedPanel == null)
			return;
		String uid = selectedPanel.getConfigurableColor().uid;
		ConfigurableColor configurableColor = customTheme.getConfigurableColor(uid);
		if (configurableColor != null) {
			selectedPanel.getConfigurableColor().color.set(configurableColor.color);
			updateComponents();
		}
	}

	private void exportThemeToClipboard() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(previewTheme.getThemeString()), null);
	}

	private void importThemeFromClipboard() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
			try {
				String clipboardString = (String) clipboard.getData(DataFlavor.stringFlavor);
				CustomTheme customTheme = ThemeSerializer.deserialize(clipboardString);
				if (customTheme != null) {
					previewTheme.setFromTheme(customTheme);
					updateComponents();
					return;
				}
			} catch (Exception e) {
			}
		}
		JOptionPane.showMessageDialog(this, I18n.get("settings.themeeditor.clipboard_does_not_contain_a_theme_string"));
	}

	private void setSelectedConfigurableColorPanel(ConfigurableColorPanel configurableColorPanel) {
		if (selectedPanel != null)
			selectedPanel.setSelected(false);
		selectedPanel = configurableColorPanel;
		if (configurableColorPanel != null)
			configurableColorPanel.setSelected(true);
		colorPickerPanel.setColor(configurableColorPanel != null ? configurableColorPanel.getConfigurableColor().color.color() : Color.WHITE);
	}

	private void onColorChanged(Color color) {
		if (selectedPanel != null)
			selectedPanel.getConfigurableColor().color.set(color);
		updateComponents();
	}

	private void updateComponents() {
		previewStyleManager.currentTheme.setTheme(previewTheme);
		if (selectedPanel != null) {
			Color c = selectedPanel.getConfigurableColor().color.color();
			colorPickerPanel.setColor(c);
		}

		SwingUtilities.invokeLater(() -> {
			ninBotPreviewBasic.renderImage();
			ninBotPreviewDetailed.renderImage();
		});
	}

	@Override
	public void updateBounds(StyleManager styleManager) {
		super.updateBounds(styleManager);
		Dimension d = getPreferredSize();
		setSize(d.width, d.height);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	@Override
	protected void onExitButtonClicked() {
		if (!customTheme.isEquivalentTo(previewTheme)) {
			int result = JOptionPane.showConfirmDialog(this, I18n.get("settings.themeeditor.do_you_want_to_save"), I18n.get("settings.themeeditor.unsaved_changes"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.YES_OPTION)
				saveTheme();
			else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.PLAIN_MESSAGE)
				return;
		}
		dispose();
	}

	@Override
	public void dispose() {
		ninBotPreviewBasic.dispose();
		ninBotPreviewDetailed.dispose();
		super.dispose();
	}

}
