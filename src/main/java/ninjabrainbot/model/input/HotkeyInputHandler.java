package ninjabrainbot.model.input;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.boat.ResetBoatStateAction;
import ninjabrainbot.model.actions.boat.ToggleEnteringBoatAction;
import ninjabrainbot.model.actions.boat.ToggleMod360IndicatorAction;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.actions.common.ToggleLockedAction;
import ninjabrainbot.model.actions.endereye.ChangeLastAngleAction;
import ninjabrainbot.model.actions.endereye.ToggleAltStdOnLastThrowAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.highprecision.BoatState;
import ninjabrainbot.model.datastate.homeportal.HomePortalContext;
import ninjabrainbot.model.datastate.homeportal.IHomePortalContext;
import ninjabrainbot.model.datastate.homeportal.ToggleBlindModeAction;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class HotkeyInputHandler implements IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final IDomainModel domainModel;
	private final IDataState dataState;
	private final IActionExecutor actionExecutor;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public HotkeyInputHandler(NinjabrainBotPreferences preferences, IDomainModel domainModel, IDataState dataState, IActionExecutor actionExecutor) {
		this.preferences = preferences;
		this.domainModel = domainModel;
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;

		disposeHandler.add(preferences.hotkeyReset.whenTriggered().subscribe(this::resetIfNotLocked));
		disposeHandler.add(preferences.hotkeyUndo.whenTriggered().subscribe(this::undoIfNotLocked));
		disposeHandler.add(preferences.hotkeyRedo.whenTriggered().subscribe(this::redoIfNotLocked));
		disposeHandler.add(preferences.hotkeyIncrement.whenTriggered().subscribe(__ -> changeLastAngleIfNotLocked(true)));
		disposeHandler.add(preferences.hotkeyDecrement.whenTriggered().subscribe(__ -> changeLastAngleIfNotLocked(false)));
		disposeHandler.add(preferences.hotkeyAltStd.whenTriggered().subscribe(this::toggleAltStdIfNotLocked));
		disposeHandler.add(preferences.hotkeyBoat.whenTriggered().subscribe(this::toggleEnteringBoatIfNotLocked));
		disposeHandler.add(preferences.hotkeyMod360.whenTriggered().subscribe(this::toggleMod360IndicatorIfNotLocked));
		disposeHandler.add(preferences.hotkeyLock.whenTriggered().subscribe(__ -> actionExecutor.executeImmediately(new ToggleLockedAction(dataState))));
		disposeHandler.add(preferences.usePreciseAngle.whenModified().subscribe(this::resetBoatState));
		disposeHandler.add(preferences.hotkeyHomePortal.whenTriggered().subscribe(this::toggleHomePortalMode));
	}

	private void resetIfNotLocked() {
		if (!dataState.locked().get())
			actionExecutor.executeImmediately(new ResetAction(domainModel));
	}

	private void undoIfNotLocked() {
		if (!dataState.locked().get())
			domainModel.undoUnderWriteLock();
	}

	private void redoIfNotLocked() {
		if (!dataState.locked().get())
			domainModel.redoUnderWriteLock();
	}

	private void changeLastAngleIfNotLocked(boolean positive) {
		if (!dataState.locked().get() && !dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get())
			actionExecutor.executeImmediately(new ChangeLastAngleAction(dataState, preferences, positive));
	}

	private void toggleAltStdIfNotLocked() {
		if (!dataState.locked().get() && !dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get())
			actionExecutor.executeImmediately(new ToggleAltStdOnLastThrowAction(dataState, preferences));
	}

	private void toggleEnteringBoatIfNotLocked() {
		if (preferences.usePreciseAngle.get() && !dataState.locked().get() && !dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get()) {
			if (dataState.boatDataState().reducingModulo360().get())
				actionExecutor.executeImmediately(new ToggleMod360IndicatorAction(dataState), new ToggleEnteringBoatAction(dataState));
			else
				actionExecutor.executeImmediately(new ToggleEnteringBoatAction(dataState));
		}
	}

	private void toggleMod360IndicatorIfNotLocked() {
		if (preferences.usePreciseAngle.get() && dataState.boatDataState().boatState().get() == BoatState.VALID && !dataState.locked().get() && !dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get()) {
			actionExecutor.executeImmediately(new ToggleMod360IndicatorAction(dataState));
		}
	}

	private void resetBoatState() {
		if (!preferences.usePreciseAngle.get())
			actionExecutor.executeImmediately(new ResetBoatStateAction(dataState));
	}
	
	private void toggleHomePortalMode() {
		if (preferences.useHomePortalMode.get()) {
			IHomePortalContext hpc = dataState.getHomePortalContext();
			actionExecutor.executeImmediately(new ToggleBlindModeAction(hpc, !hpc.blindModeToggled().get()));	
		}
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
