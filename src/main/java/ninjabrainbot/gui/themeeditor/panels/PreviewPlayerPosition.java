package ninjabrainbot.gui.themeeditor.panels;

import ninjabrainbot.model.datastate.common.IPlayerPosition;

public class PreviewPlayerPosition implements IPlayerPosition {

	private final double x, y, z;

	public PreviewPlayerPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public double xInOverworld() {
		return x;
	}

	@Override
	public double zInOverworld() {
		return z;
	}

	@Override
	public double xInPlayerDimension() {
		return x;
	}
	
	@Override
	public double yInPlayerDimension() {
		return y;
	}

	@Override
	public double zInPlayerDimension() {
		return z;
	}

	@Override
	public boolean isInOverworld() {
		return true;
	}

	@Override
	public boolean isInNether() {
		return false;
	}

	@Override
	public double horizontalAngle() {
		return 0;
	}

}
