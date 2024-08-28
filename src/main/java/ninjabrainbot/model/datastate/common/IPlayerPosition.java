package ninjabrainbot.model.datastate.common;

public interface IPlayerPosition extends IOverworldRay {

	double xInPlayerDimension();
	
	double yInPlayerDimension();

	double zInPlayerDimension();

	boolean isInOverworld();

	boolean isInNether();

}
