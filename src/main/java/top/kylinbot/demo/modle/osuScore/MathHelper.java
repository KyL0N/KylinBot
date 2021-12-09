package top.kylinbot.demo.modle.osuScore;

import static java.lang.Math.max;
import static java.lang.Math.min;

@SuppressWarnings( "squid:S00100" )
public final class MathHelper {
	private MathHelper() {
		// utility class
	}

	static double static_cast(int x) {
		return x;
	}

	static double static_cast(double x) {
		return x;
	}

	static double Clamp(double x, double min, double max) {
		return max(min, min(max, x));
	}
}
