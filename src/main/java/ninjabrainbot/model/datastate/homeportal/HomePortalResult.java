package ninjabrainbot.model.datastate.homeportal;

public class HomePortalResult {

	public final double x, y, z;
	public final double angle;
	public final double distance;
	public final double angleDiff;
	public final double heightDiff;

	public HomePortalResult(double x, double y, double z, double distance, double angle, double angleDiff, double heightDiff) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
		this.distance = distance;
		this.angleDiff = angleDiff;
		this.heightDiff = heightDiff;
	}

	public String getFormatedAngleDiff() {
		double absChange = Math.abs(angleDiff);
		return String.format(" (%s %.1f)", angleDiff > 0 ? "->" : "<-", absChange);
	}

	public float getAngleDiffColor() {
		return (float) (1 - Math.abs(angleDiff) / 180.0);
	}

	public double getHeightDiff() {
		return Math.abs(heightDiff);
	}

	public String getAboveBelow() {
		return heightDiff > 0 ? "above" : "below";
	}

	public float getHeightDiffColor() {
		return (float)(1 - Math.abs(heightDiff)/30);
	}

	public boolean showDirection() {
		return distance == 0 && heightDiff == 0;
	}
}
