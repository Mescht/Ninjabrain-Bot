package ninjabrainbot.util;

import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.ILimitedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPositionInputSource;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.input.IFossilInputSource;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class FakeCoordinateInputSource implements IPlayerPositionInputSource, IFossilInputSource {

	public final ObservableProperty<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted;
	public final ObservableProperty<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted;
	public final ObservableProperty<Fossil> whenNewFossilInputted;

	public FakeCoordinateInputSource() {
		whenNewDetailedPlayerPositionInputted = new ObservableProperty<>();
		whenNewLimitedPlayerPositionInputted = new ObservableProperty<>();
		whenNewFossilInputted = new ObservableProperty<>();
	}

	public ISubscribable<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted() {
		return whenNewDetailedPlayerPositionInputted;
	}

	public ISubscribable<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted() {
		return whenNewLimitedPlayerPositionInputted;
	}

	public ISubscribable<Fossil> whenNewFossilInputted() {
		return whenNewFossilInputted;
	}
}
