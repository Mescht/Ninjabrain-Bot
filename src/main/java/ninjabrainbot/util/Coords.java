package ninjabrainbot.util;

public class Coords {

	public static double getPhi(double x, double z) {
		return -Math.atan2(x, z);
	}

	public static double getX(double r, double phi) {
		return -r * Math.sin(phi);
	}

	public static double getZ(double r, double phi) {
		return r * Math.cos(phi);
	}

	public static double dist2(double x1, double z1, double x2, double z2) {
		double dx = (x2 - x1);
		double dz = (z2 - z1);
		return dx * dx + dz * dz;
	}
	
	public static double dist2(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = (x2 - x1);
		double dy = (y2 - y1);
		double dz = (z2 - z1);
		return dx * dx + dz * dz + dy * dy;
	}

	public static double dist(double x1, double z1, double x2, double z2) {
		return Math.sqrt(dist2(x1, z1, x2, z2));
	}
	
	public static double dist(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(dist2(x1, y1, z1, x2, y2, z2));
	}

}
