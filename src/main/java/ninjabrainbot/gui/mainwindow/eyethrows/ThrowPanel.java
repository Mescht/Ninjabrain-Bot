package ninjabrainbot.gui.mainwindow.eyethrows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import ninjabrainbot.io.preferences.enums.AngleAdjustmentDisplayType;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrowType;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.input.IButtonInputHandler;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.IObservableList;
import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

/**
 * JComponent for showing a Throw.
 */
public class ThrowPanel extends ThemedPanel implements IDisposable {

	protected final DisposeHandler disposeHandler = new DisposeHandler();
	private final NinjabrainBotPreferences preferences;

	DivineContextPanel divineContextPanel;

	private ChunkPrediction lastTopPrediction;

	private final int index;
	private IEnderEyeThrow t;
	final JLabel x;
	final JLabel z;
	final JLabel alpha;
	private final JLabel correction;
	private final JLabel error;
	private final FlatButton removeButton;

	private int correctionSgn;
	private Color colorNeg, colorPos;

	private Subscription chunkPredictionModifiedSubscription;
	private final Runnable whenVisibilityChanged;

	private final WrappedColor negCol;
	private final WrappedColor posCol;
	private final WrappedColor lineCol;

	public ThrowPanel(StyleManager styleManager, IDataState dataState, IButtonInputHandler buttonInputHandler, IObservable<ChunkPrediction> topResult, int index, Runnable whenVisibilityChanged, NinjabrainBotPreferences preferences) {
		super(styleManager);
		this.index = index;
		this.whenVisibilityChanged = whenVisibilityChanged;

		setOpaque(true);
		this.preferences = preferences;
		x = new JLabel((String) null, SwingConstants.CENTER);
		z = new JLabel((String) null, SwingConstants.CENTER);
		alpha = new JLabel((String) null, SwingConstants.CENTER);
		correction = new JLabel((String) null, SwingConstants.CENTER);
		error = new JLabel((String) null, SwingConstants.CENTER);
		removeButton = new FlatButton(styleManager, "-");
		removeButton.setBackgroundColor(styleManager.currentTheme.COLOR_NEUTRAL);
		removeButton.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_NEUTRAL);
		removeButton.setHoverColor(styleManager.currentTheme.COLOR_EXIT_BUTTON_HOVER);
		removeButton.setVisible(false);
		removeButton.setBorder(null);
		removeButton.addActionListener(p -> buttonInputHandler.onRemoveThrowButtonPressed(this.t));
		add(removeButton);
		add(x);
		add(z);
		add(alpha);
		add(correction);
		add(error);
		setLayout(null);
		updateVisibility();

		IObservableList<IEnderEyeThrow> throwSet = dataState.getThrowList();
		setThrow(index < throwSet.size() ? throwSet.get(index) : null);
		disposeHandler.add(throwSet.subscribeEDT(this::updateThrow));

		setPrediction(topResult.get());
		disposeHandler.add(topResult.subscribeEDT(this::setPrediction));

		setBackgroundColor(styleManager.currentTheme.COLOR_NEUTRAL);
		setForegroundColor(styleManager.currentTheme.TEXT_COLOR_NEUTRAL);
		negCol = styleManager.currentTheme.COLOR_NEGATIVE;
		posCol = styleManager.currentTheme.COLOR_POSITIVE;
		lineCol = styleManager.currentTheme.COLOR_DIVIDER;

		disposeHandler.add(preferences.showAngleErrors.whenModified().subscribeEDT(error::setVisible));
		disposeHandler.add(preferences.angleAdjustmentDisplayType.whenModified().subscribeEDT(this::updateCorrection));
	}

	public void setPrediction(ChunkPrediction p) {
		lastTopPrediction = p;
		if (chunkPredictionModifiedSubscription != null)
			chunkPredictionModifiedSubscription.dispose();
		chunkPredictionModifiedSubscription = null;
		if (p != null)
			chunkPredictionModifiedSubscription = p.whenRelativePlayerPositionChanged().subscribeEDT(__ -> updateError(p));
		updateError(p);
	}

	private void updateError(ChunkPrediction p) {
		error.setText(t == null || p == null ? null : String.format(Locale.US, t.getType() == EnderEyeThrowType.Boat ? "%.4f" : "%.3f", p.getAngleError(t)));
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (x != null)
			x.setFont(font);
		if (z != null)
			z.setFont(font);
		if (alpha != null)
			alpha.setFont(font);
		if (correction != null)
			correction.setFont(font);
		if (error != null)
			error.setFont(font);
		if (removeButton != null)
			removeButton.setFont(font);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int w = width - height;
		if (!preferences.showAngleErrors.get()) {
			if (this.x != null)
				this.x.setBounds(0, 0, w / 3, height);
			if (this.z != null)
				this.z.setBounds(w / 3, 0, w / 3, height);
			if (this.alpha != null) {
				if (correctionSgn != 0) {
					int w1 = w / 3 * 3 / 4;
					int dx = w / 3 / 8;
					this.alpha.setBounds(2 * w / 3 - dx, 0, w1, height);
					this.alpha.setHorizontalAlignment(SwingConstants.RIGHT);
					this.correction.setBounds(2 * w / 3 + w1 - dx, 0, w1, height);
					this.correction.setHorizontalAlignment(SwingConstants.LEFT);
				} else {
					this.alpha.setBounds(2 * w / 3, 0, w / 3, height);
					this.alpha.setHorizontalAlignment(SwingConstants.CENTER);
				}
			}
			if (this.removeButton != null)
				this.removeButton.setBounds(w, 0, height, height - 1);
		} else {
			if (this.x != null)
				this.x.setBounds(0, 0, w / 4, height);
			if (this.z != null)
				this.z.setBounds(w / 4, 0, w / 4, height);
			if (this.alpha != null) {
				if (correctionSgn != 0) {
					int w1 = w / 4 * 3 / 4;
					int dx = w / 4 / 8;
					this.alpha.setBounds(2 * w / 4 - dx, 0, w1, height);
					this.alpha.setHorizontalAlignment(SwingConstants.RIGHT);
					this.correction.setBounds(2 * w / 4 + w1 - dx, 0, w1, height);
					this.correction.setHorizontalAlignment(SwingConstants.LEFT);
				} else {
					this.alpha.setBounds(2 * w / 4, 0, w / 4, height);
					this.alpha.setHorizontalAlignment(SwingConstants.CENTER);
				}
			}
			if (this.error != null)
				this.error.setBounds(3 * w / 4, 0, w / 4, height);
			if (this.removeButton != null)
				this.removeButton.setBounds(w, 0, height, height - 1);
		}
		error.setVisible(preferences.showAngleErrors.get());
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (x != null)
			x.setForeground(fg);
		if (z != null)
			z.setForeground(fg);
		if (alpha != null)
			alpha.setForeground(fg);
		if (correction != null)
			correction.setForeground(correctionSgn > 0 ? colorPos : colorNeg);
		if (error != null)
			error.setForeground(fg);
	}

	@Override
	public void updateColors() {
		colorNeg = negCol.color();
		colorPos = posCol.color();
		setBorder(new MatteBorder(0, 0, 1, 0, lineCol.color()));
		super.updateColors();
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		super.updateSize(styleManager);
		setPreferredSize(new Dimension(styleManager.size.WIDTH, styleManager.size.TEXT_SIZE_SMALL + styleManager.size.PADDING_THIN * 2));
	}

	private void updateThrow(IReadOnlyList<IEnderEyeThrow> throwSet) {
		setThrow(index < throwSet.size() ? throwSet.get(index) : null);
	}

	private void setThrow(IEnderEyeThrow t) {
		if (this.t == t)
			return;

		this.t = t;
		if (t == null) {
			x.setText(null);
			z.setText(null);
			alpha.setText(null);
			correction.setText(null);
			error.setText(null);
			removeButton.setVisible(false);
			correctionSgn = 0;
		} else {
			x.setText(String.format(Locale.US, "%.2f", t.xInOverworld()));
			z.setText(String.format(Locale.US, "%.2f", t.zInOverworld()));
			alpha.setText(String.format(Locale.US, "%.2f", t.horizontalAngleWithoutCorrection()));
			updateCorrection(preferences.angleAdjustmentDisplayType.get());
			updateError(lastTopPrediction);
			removeButton.setVisible(true);
		}
		updateVisibility();
		repaint(); // Update dot
	}

	void updateCorrection(AngleAdjustmentDisplayType displayType) {
		if (t == null)
			return;

		if (displayType == AngleAdjustmentDisplayType.INCREMENTS)
			correctionSgn = t.correctionIncrements() == 0 ? 0 : (t.correctionIncrements() > 0 ? 1 : -1);
		else
			correctionSgn = Math.abs(t.correction()) < 1e-7 ? 0 : (t.correction() > 0 ? 1 : -1);

		if (correctionSgn != 0) {
			if (displayType == AngleAdjustmentDisplayType.INCREMENTS)
				correction.setText(String.format(Locale.US, "%+d", t.correctionIncrements()));
			else if (Math.abs(Double.parseDouble(String.format("%.2f", t.correction())) - t.correction()) < 1e-7)
				correction.setText(String.format(Locale.US, "%+.2f", t.correction()));
			else
				correction.setText(String.format(Locale.US, "%+.3f", t.correction()));

			correction.setForeground(correctionSgn > 0 ? colorPos : colorNeg);
		} else {
			correction.setText(null);
		}
	}

	void updateVisibility() {
		int k = (divineContextPanel != null && divineContextPanel.isVisible()) ? 1 : 0;
		boolean newVisibility = index < 3 - k || hasThrow();
		if (newVisibility != isVisible()) {
			setVisible(newVisibility);
			if (whenVisibilityChanged != null)
				whenVisibilityChanged.run();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Paint dot if special std
		if (t != null && t.getType() != EnderEyeThrowType.Normal) {
			if (t.getType() == EnderEyeThrowType.NormalWithAltStd) {
				g.setColor(Color.RED);
			} else if (t.getType() == EnderEyeThrowType.Manual) {
				g.setColor(Color.CYAN);
			} else if (t.getType() == EnderEyeThrowType.Boat) {
				g.setColor(Color.GREEN);
			}
			int a = 3;
			int b = 2;
			g.fillRect(b, b, a, a);
		}
	}

	private boolean hasThrow() {
		return t != null;
	}

	@Override
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}

	@Override
	public void dispose() {
		if (chunkPredictionModifiedSubscription != null)
			chunkPredictionModifiedSubscription.dispose();
		disposeHandler.dispose();
	}

	public void setDivineContextPanel(DivineContextPanel divineContextPanel) {
		this.divineContextPanel = divineContextPanel;
		updateVisibility();
	}

}
