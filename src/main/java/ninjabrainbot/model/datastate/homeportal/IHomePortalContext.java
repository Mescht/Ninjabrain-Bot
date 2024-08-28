package ninjabrainbot.model.datastate.homeportal;

import ninjabrainbot.model.domainmodel.IDataComponent;

public interface IHomePortalContext {
	
	HomePortalPosition getPosition();
	
	void setPosition(HomePortalPosition pos);

	IDataComponent<HomePortalPosition> position();
	
	IDataComponent<Boolean> useHomePortalMode();
	
	IDataComponent<Boolean> blindModeToggled();
	
	void setHomePortalMode(boolean b);

}
