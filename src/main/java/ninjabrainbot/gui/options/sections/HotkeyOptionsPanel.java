package ninjabrainbot.gui.options.sections;

import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.HotkeyPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class HotkeyOptionsPanel extends StackPanel {

	public HotkeyOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		setOpaque(false);
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));

		if (KeyboardListener.registered) {
			add(new HotkeyPanel(styleManager, I18n.get("settings.up_001_to_last_angle"), preferences.hotkeyIncrement));
			add(new HotkeyPanel(styleManager, I18n.get("settings.down_001_to_last_angle"), preferences.hotkeyDecrement));
			add(new HotkeyPanel(styleManager, I18n.get("reset"), preferences.hotkeyReset));
			add(new HotkeyPanel(styleManager, I18n.get("undo"), preferences.hotkeyUndo));
			add(new HotkeyPanel(styleManager, I18n.get("redo"), preferences.hotkeyRedo));
			add(new HotkeyPanel(styleManager, I18n.get("lock"), preferences.hotkeyLock));
			add(new HotkeyPanel(styleManager, I18n.get("hide_show_window"), preferences.hotkeyMinimize));
			add(new HotkeyPanel(styleManager, I18n.get("settings.homeportal.hotkey"), preferences.hotkeyHomePortal));
		}
	}

}
