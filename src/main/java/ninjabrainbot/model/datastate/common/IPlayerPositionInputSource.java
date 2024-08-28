package ninjabrainbot.model.datastate.common;

import ninjabrainbot.event.ISubscribable;

public interface IPlayerPositionInputSource {

	/**
	 * Notifies subscribers whenever new coordinates have been inputted, e.g. as a result of an F3+C command.
	 */
	ISubscribable<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted();

	/**
	 * Notifies subscribers whenever new limited coordinates have been inputted (for pre 1.12, e.g. "12 2000 180")
	 */
	ISubscribable<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted();

}
