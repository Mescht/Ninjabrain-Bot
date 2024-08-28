package ninjabrainbot.model.datastate.homeportal;

import ninjabrainbot.model.actions.IAction;

public class ToggleBlindModeAction implements IAction {
	
	private final IHomePortalContext homePortalContext;
	private final boolean blindModeToggled;
	
	public ToggleBlindModeAction(IHomePortalContext homePortalContext, boolean b) {
		this.homePortalContext = homePortalContext;
		blindModeToggled = b;
	}

	@Override
	public void execute() {
		homePortalContext.blindModeToggled().set(blindModeToggled);
	}
}
