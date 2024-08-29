package ninjabrainbot.model.datastate.homeportal;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class HomePortalContext implements IHomePortalContext, IDisposable{
	
	private final DataComponent<HomePortalPosition> position;
	private final DataComponent<Boolean> useHomePortalMode;
	private final DataComponent<Boolean> blindModeToggled;
	
	private final DisposeHandler disposeHandler = new DisposeHandler();
	
	
	public HomePortalContext(IDomainModel domainModel, boolean useHomePortal) {
		position = new DataComponent<>(domainModel, null);
		this.useHomePortalMode = new DataComponent<>(null, useHomePortal);
		blindModeToggled = new DataComponent<>(domainModel, false);
	}
	
	@Override
	public IDataComponent<HomePortalPosition> position() {
		if (position.get() != null)
			System.out.println(String.format("Position returned as %f, %f, %f", position.get().x, position.get().y, position.get().z));
		else
			System.out.println("Position returned as null");
		
		return position;
	}
	
	@Override
	public IDataComponent<Boolean> useHomePortalMode() {
		return useHomePortalMode;
	}
	
	@Override
	public IDataComponent<Boolean> blindModeToggled() {
		return blindModeToggled;
	}

	@Override
	public void setHomePortalMode(boolean b) {
		useHomePortalMode.set(b);
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
	
}
