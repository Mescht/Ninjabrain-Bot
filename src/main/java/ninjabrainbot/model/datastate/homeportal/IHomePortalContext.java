package ninjabrainbot.model.datastate.homeportal;

import ninjabrainbot.model.domainmodel.IDataComponent;

public interface IHomePortalContext {

	IDataComponent<HomePortalPosition> position();
	
	IDataComponent<Boolean> useHomePortalMode();
	
	IDataComponent<Boolean> blindModeToggled();
	
	void setHomePortalMode(boolean b);
}
