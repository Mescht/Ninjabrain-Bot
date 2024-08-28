package ninjabrainbot.model.datastate.homeportal;

import ninjabrainbot.model.actions.IAction;

public class SetHomePortalModeAction implements IAction {
	
	private final IHomePortalContext homePortalContext;
	private final boolean useHomePortalMode;
	
	public SetHomePortalModeAction(IHomePortalContext homePortalContext, boolean b) {
		this.homePortalContext = homePortalContext;
		useHomePortalMode = b;
	}

	@Override
	public void execute() {
		homePortalContext.useHomePortalMode().set(useHomePortalMode);
	}
}
