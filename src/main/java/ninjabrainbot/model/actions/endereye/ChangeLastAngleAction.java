package ninjabrainbot.model.actions.endereye;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.domainmodel.IListComponent;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class ChangeLastAngleAction implements IAction {

	private final IListComponent<IEnderEyeThrow> throwList;
	private final IObservable<Boolean> locked;
	private final NinjabrainBotPreferences preferences;
	private final int correctionIncrements;

	public ChangeLastAngleAction(IDataState dataState, NinjabrainBotPreferences preferences, int correctionIncrements) {
		this(dataState.getThrowList(), dataState.locked(), preferences, correctionIncrements);
	}

	public ChangeLastAngleAction(IListComponent<IEnderEyeThrow> throwList, IObservable<Boolean> locked, NinjabrainBotPreferences preferences, int correctionIncrements) {
		this.throwList = throwList;
		this.locked = locked;
		this.preferences = preferences;
        this.correctionIncrements = correctionIncrements;
    }

	@Override
	public void execute() {
		if (locked.get())
			return;

		if (throwList.size() == 0)
			return;

		IEnderEyeThrow lastThrow = throwList.get(throwList.size() - 1);
		double newCorrection = lastThrow.correction() + getAngleCorrectionAmountInDegrees(lastThrow.verticalAngle());
		IEnderEyeThrow newThrow = lastThrow.withCorrection(newCorrection, lastThrow.correctionIncrements() + correctionIncrements);

		throwList.replace(lastThrow, newThrow);
	}

	private double getAngleCorrectionAmountInDegrees(double beta) {
		double change;
		switch (preferences.angleAdjustmentType.get()) {
			case TALL:
				final double toRad = Math.PI / 180.0;
				change = Math.atan(2 * Math.tan(15 * toRad) / preferences.resolutionHeight.get()) / Math.cos(beta * toRad) / toRad;
				break;
			case CUSTOM:
				change = preferences.customAdjustment.get();
				break;
			default:
				change = 0.01;
		}

		change *= correctionIncrements;
		return change;
	}

}
