package ninjabrainbot.model.datastate.homeportal;

import ninjabrainbot.model.datastate.common.IPlayerPosition;

public class HomePortalPosition {

	public final double x, y, z;

	public HomePortalPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public HomePortalPosition(IPlayerPosition playerPos) {
		this(playerPos.xInPlayerDimension(), playerPos.yInPlayerDimension(), playerPos.zInPlayerDimension());
	}
}
