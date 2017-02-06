/*
 * DEFAULT LICENSE
 * Do not make illegal copies
 * This software is provided as is, without any warranty at all
 * Not responsible for any damage to anything that may occur
 * Copyright © 2012 William Peckham
 */
package com.matrixpeckham.libnoise.util;

import static com.matrixpeckham.libnoise.util.NoiseQuality.STD;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.logging.Logger;

/**
 * Class that houses global values and global helper functions.
 *
 * @author William Matrix Peckham
 */
public class Globals {

    /**
     * Number of meters per point in a Terragen terrain (TER) file.
     */
    public static final double DEFAULT_METERS_PER_POINT = 30.0;

    /**
     * Same as the DEFAULT_METERS_PER_POINT constant, but for us canuckleheads.
     */
    public static final double DEFAULT_METRES_PER_POINT
	    = DEFAULT_METERS_PER_POINT;

    /**
     * alias for Math.PI
     */
    public static final double PI = 3.1415926535897932385;

    /**
     * Converts an angle from degrees to radians
     */
    public static final double DEG_TO_RAD = PI / 180.0;

    /**
     * Converts an angle from radians to degrees.
     */
    public static final double RAD_TO_DEG = 1.0 / DEG_TO_RAD;

    /**
     * The maximum height of a raster.
     */
    public static final int RASTER_MAX_HEIGHT = 32_767;

    /**
     * The maximum width of a raster.
     */
    public static final int RASTER_MAX_WIDTH = 32_767;

    /**
     * The raster's stride length must be a multiple of this constant. may not
     * be needed in java
     */
    public static final int RASTER_STRIDE_BOUNDARY = 4;

    private static final int SEED_NOISE_GEN = 1_013;

    private static final int SHIFT_NOISE_GEN = 8;

    /**
     * Square root of 2
     */
    public static final double SQRT_2 = 1.4142135623730950488;

    /**
     * Square root of 3
     */
    public static final double SQRT_3 = 1.7320508075688772935;

    //we implement version 2 noise from noiselib here and do not offer version 1.
    private static final int X_NOISE_GEN = 1_619;

    private static final int Y_NOISE_GEN = 31_337;

    private static final int Z_NOISE_GEN = 6_971;

    /**
     * A table of 256 random normalized vectors. Each row is an (x, y, z, 0)
     * coordinate. The 0 is used as padding so we can use bit shifts to index
     * any row in the table. These vectors have an even statistical
     * distribution, which improves the quality of the coherent noise generated
     * by these vectors. For more information, see "GPU Gems", Chapter 5 -
     * Implementing Improved Perlin Noise by Ken Perlin, specifically page 76.
     */
    static final double[] randomVectors = new double[]{
	//<editor-fold defaultstate="collapsed" desc="vectors">
	-0.763874, -0.596439, -0.246489, 0.0,
	0.396055, 0.904518, -0.158073, 0.0,
	-0.499004, -0.8665, -0.0131631, 0.0,
	0.468724, -0.824756, 0.316346, 0.0,
	0.829598, 0.43195, 0.353816, 0.0,
	-0.454473, 0.629497, -0.630228, 0.0,
	-0.162349, -0.869962, -0.465628, 0.0,
	0.932805, 0.253451, 0.256198, 0.0,
	-0.345419, 0.927299, -0.144227, 0.0,
	-0.715026, -0.293698, -0.634413, 0.0,
	-0.245997, 0.717467, -0.651711, 0.0,
	-0.967409, -0.250435, -0.037451, 0.0,
	0.901729, 0.397108, -0.170852, 0.0,
	0.892657, -0.0720622, -0.444938, 0.0,
	0.0260084, -0.0361701, 0.999007, 0.0,
	0.949107, -0.19486, 0.247439, 0.0,
	0.471803, -0.807064, -0.355036, 0.0,
	0.879737, 0.141845, 0.453809, 0.0,
	0.570747, 0.696415, 0.435033, 0.0,
	-0.141751, -0.988233, -0.0574584, 0.0,
	-0.58219, -0.0303005, 0.812488, 0.0,
	-0.60922, 0.239482, -0.755975, 0.0,
	0.299394, -0.197066, -0.933557, 0.0,
	-0.851615, -0.220702, -0.47544, 0.0,
	0.848886, 0.341829, -0.403169, 0.0,
	-0.156129, -0.687241, 0.709453, 0.0,
	-0.665651, 0.626724, 0.405124, 0.0,
	0.595914, -0.674582, 0.43569, 0.0,
	0.171025, -0.509292, 0.843428, 0.0,
	0.78605, 0.536414, -0.307222, 0.0,
	0.18905, -0.791613, 0.581042, 0.0,
	-0.294916, 0.844994, 0.446105, 0.0,
	0.342031, -0.58736, -0.7335, 0.0,
	0.57155, 0.7869, 0.232635, 0.0,
	0.885026, -0.408223, 0.223791, 0.0,
	-0.789518, 0.571645, 0.223347, 0.0,
	0.774571, 0.31566, 0.548087, 0.0,
	-0.79695, -0.0433603, -0.602487, 0.0,
	-0.142425, -0.473249, -0.869339, 0.0,
	-0.0698838, 0.170442, 0.982886, 0.0,
	0.687815, -0.484748, 0.540306, 0.0,
	0.543703, -0.534446, -0.647112, 0.0,
	0.97186, 0.184391, -0.146588, 0.0,
	0.707084, 0.485713, -0.513921, 0.0,
	0.942302, 0.331945, 0.043348, 0.0,
	0.499084, 0.599922, 0.625307, 0.0,
	-0.289203, 0.211107, 0.9337, 0.0,
	0.412433, -0.71667, -0.56239, 0.0,
	0.87721, -0.082816, 0.47291, 0.0,
	-0.420685, -0.214278, 0.881538, 0.0,
	0.752558, -0.0391579, 0.657361, 0.0,
	0.0765725, -0.996789, 0.0234082, 0.0,
	-0.544312, -0.309435, -0.779727, 0.0,
	-0.455358, -0.415572, 0.787368, 0.0,
	-0.874586, 0.483746, 0.0330131, 0.0,
	0.245172, -0.0838623, 0.965846, 0.0,
	0.382293, -0.432813, 0.81641, 0.0,
	-0.287735, -0.905514, 0.311853, 0.0,
	-0.667704, 0.704955, -0.239186, 0.0,
	0.717885, -0.464002, -0.518983, 0.0,
	0.976342, -0.214895, 0.0240053, 0.0,
	-0.0733096, -0.921136, 0.382276, 0.0,
	-0.986284, 0.151224, -0.0661379, 0.0,
	-0.899319, -0.429671, 0.0812908, 0.0,
	0.652102, -0.724625, 0.222893, 0.0,
	0.203761, 0.458023, -0.865272, 0.0,
	-0.030396, 0.698724, -0.714745, 0.0,
	-0.460232, 0.839138, 0.289887, 0.0,
	-0.0898602, 0.837894, 0.538386, 0.0,
	-0.731595, 0.0793784, 0.677102, 0.0,
	-0.447236, -0.788397, 0.422386, 0.0,
	0.186481, 0.645855, -0.740335, 0.0,
	-0.259006, 0.935463, 0.240467, 0.0,
	0.445839, 0.819655, -0.359712, 0.0,
	0.349962, 0.755022, -0.554499, 0.0,
	-0.997078, -0.0359577, 0.0673977, 0.0,
	-0.431163, -0.147516, -0.890133, 0.0,
	0.299648, -0.63914, 0.708316, 0.0,
	0.397043, 0.566526, -0.722084, 0.0,
	-0.502489, 0.438308, -0.745246, 0.0,
	0.0687235, 0.354097, 0.93268, 0.0,
	-0.0476651, -0.462597, 0.885286, 0.0,
	-0.221934, 0.900739, -0.373383, 0.0,
	-0.956107, -0.225676, 0.186893, 0.0,
	-0.187627, 0.391487, -0.900852, 0.0,
	-0.224209, -0.315405, 0.92209, 0.0,
	-0.730807, -0.537068, 0.421283, 0.0,
	-0.0353135, -0.816748, 0.575913, 0.0,
	-0.941391, 0.176991, -0.287153, 0.0,
	-0.154174, 0.390458, 0.90762, 0.0,
	-0.283847, 0.533842, 0.796519, 0.0,
	-0.482737, -0.850448, 0.209052, 0.0,
	-0.649175, 0.477748, 0.591886, 0.0,
	0.885373, -0.405387, -0.227543, 0.0,
	-0.147261, 0.181623, -0.972279, 0.0,
	0.0959236, -0.115847, -0.988624, 0.0,
	-0.89724, -0.191348, 0.397928, 0.0,
	0.903553, -0.428461, -0.00350461, 0.0,
	0.849072, -0.295807, -0.437693, 0.0,
	0.65551, 0.741754, -0.141804, 0.0,
	0.61598, -0.178669, 0.767232, 0.0,
	0.0112967, 0.932256, -0.361623, 0.0,
	-0.793031, 0.258012, 0.551845, 0.0,
	0.421933, 0.454311, 0.784585, 0.0,
	-0.319993, 0.0401618, -0.946568, 0.0,
	-0.81571, 0.551307, -0.175151, 0.0,
	-0.377644, 0.00322313, 0.925945, 0.0,
	0.129759, -0.666581, -0.734052, 0.0,
	0.601901, -0.654237, -0.457919, 0.0,
	-0.927463, -0.0343576, -0.372334, 0.0,
	-0.438663, -0.868301, -0.231578, 0.0,
	-0.648845, -0.749138, -0.133387, 0.0,
	0.507393, -0.588294, 0.629653, 0.0,
	0.726958, 0.623665, 0.287358, 0.0,
	0.411159, 0.367614, -0.834151, 0.0,
	0.806333, 0.585117, -0.0864016, 0.0,
	0.263935, -0.880876, 0.392932, 0.0,
	0.421546, -0.201336, 0.884174, 0.0,
	-0.683198, -0.569557, -0.456996, 0.0,
	-0.117116, -0.0406654, -0.992285, 0.0,
	-0.643679, -0.109196, -0.757465, 0.0,
	-0.561559, -0.62989, 0.536554, 0.0,
	0.0628422, 0.104677, -0.992519, 0.0,
	0.480759, -0.2867, -0.828658, 0.0,
	-0.228559, -0.228965, -0.946222, 0.0,
	-0.10194, -0.65706, -0.746914, 0.0,
	0.0689193, -0.678236, 0.731605, 0.0,
	0.401019, -0.754026, 0.52022, 0.0,
	-0.742141, 0.547083, -0.387203, 0.0,
	-0.00210603, -0.796417, -0.604745, 0.0,
	0.296725, -0.409909, -0.862513, 0.0,
	-0.260932, -0.798201, 0.542945, 0.0,
	-0.641628, 0.742379, 0.192838, 0.0,
	-0.186009, -0.101514, 0.97729, 0.0,
	0.106711, -0.962067, 0.251079, 0.0,
	-0.743499, 0.30988, -0.592607, 0.0,
	-0.795853, -0.605066, -0.0226607, 0.0,
	-0.828661, -0.419471, -0.370628, 0.0,
	0.0847218, -0.489815, -0.8677, 0.0,
	-0.381405, 0.788019, -0.483276, 0.0,
	0.282042, -0.953394, 0.107205, 0.0,
	0.530774, 0.847413, 0.0130696, 0.0,
	0.0515397, 0.922524, 0.382484, 0.0,
	-0.631467, -0.709046, 0.313852, 0.0,
	0.688248, 0.517273, 0.508668, 0.0,
	0.646689, -0.333782, -0.685845, 0.0,
	-0.932528, -0.247532, -0.262906, 0.0,
	0.630609, 0.68757, -0.359973, 0.0,
	0.577805, -0.394189, 0.714673, 0.0,
	-0.887833, -0.437301, -0.14325, 0.0,
	0.690982, 0.174003, 0.701617, 0.0,
	-0.866701, 0.0118182, 0.498689, 0.0,
	-0.482876, 0.727143, 0.487949, 0.0,
	-0.577567, 0.682593, -0.447752, 0.0,
	0.373768, 0.0982991, 0.922299, 0.0,
	0.170744, 0.964243, -0.202687, 0.0,
	0.993654, -0.035791, -0.106632, 0.0,
	0.587065, 0.4143, -0.695493, 0.0,
	-0.396509, 0.26509, -0.878924, 0.0,
	-0.0866853, 0.83553, -0.542563, 0.0,
	0.923193, 0.133398, -0.360443, 0.0,
	0.00379108, -0.258618, 0.965972, 0.0,
	0.239144, 0.245154, -0.939526, 0.0,
	0.758731, -0.555871, 0.33961, 0.0,
	0.295355, 0.309513, 0.903862, 0.0,
	0.0531222, -0.91003, -0.411124, 0.0,
	0.270452, 0.0229439, -0.96246, 0.0,
	0.563634, 0.0324352, 0.825387, 0.0,
	0.156326, 0.147392, 0.976646, 0.0,
	-0.0410141, 0.981824, 0.185309, 0.0,
	-0.385562, -0.576343, -0.720535, 0.0,
	0.388281, 0.904441, 0.176702, 0.0,
	0.945561, -0.192859, -0.262146, 0.0,
	0.844504, 0.520193, 0.127325, 0.0,
	0.0330893, 0.999121, -0.0257505, 0.0,
	-0.592616, -0.482475, -0.644999, 0.0,
	0.539471, 0.631024, -0.557476, 0.0,
	0.655851, -0.027319, -0.754396, 0.0,
	0.274465, 0.887659, 0.369772, 0.0,
	-0.123419, 0.975177, -0.183842, 0.0,
	-0.223429, 0.708045, 0.66989, 0.0,
	-0.908654, 0.196302, 0.368528, 0.0,
	-0.95759, -0.00863708, 0.288005, 0.0,
	0.960535, 0.030592, 0.276472, 0.0,
	-0.413146, 0.907537, 0.0754161, 0.0,
	-0.847992, 0.350849, -0.397259, 0.0,
	0.614736, 0.395841, 0.68221, 0.0,
	-0.503504, -0.666128, -0.550234, 0.0,
	-0.268833, -0.738524, -0.618314, 0.0,
	0.792737, -0.60001, -0.107502, 0.0,
	-0.637582, 0.508144, -0.579032, 0.0,
	0.750105, 0.282165, -0.598101, 0.0,
	-0.351199, -0.392294, -0.850155, 0.0,
	0.250126, -0.960993, -0.118025, 0.0,
	-0.732341, 0.680909, -0.0063274, 0.0,
	-0.760674, -0.141009, 0.633634, 0.0,
	0.222823, -0.304012, 0.926243, 0.0,
	0.209178, 0.505671, 0.836984, 0.0,
	0.757914, -0.56629, -0.323857, 0.0,
	-0.782926, -0.339196, 0.52151, 0.0,
	-0.462952, 0.585565, 0.665424, 0.0,
	0.61879, 0.194119, -0.761194, 0.0,
	0.741388, -0.276743, 0.611357, 0.0,
	0.707571, 0.702621, 0.0752872, 0.0,
	0.156562, 0.819977, 0.550569, 0.0,
	-0.793606, 0.440216, 0.42, 0.0,
	0.234547, 0.885309, -0.401517, 0.0,
	0.132598, 0.80115, -0.58359, 0.0,
	-0.377899, -0.639179, 0.669808, 0.0,
	-0.865993, -0.396465, 0.304748, 0.0,
	-0.624815, -0.44283, 0.643046, 0.0,
	-0.485705, 0.825614, -0.287146, 0.0,
	-0.971788, 0.175535, 0.157529, 0.0,
	-0.456027, 0.392629, 0.798675, 0.0,
	-0.0104443, 0.521623, -0.853112, 0.0,
	-0.660575, -0.74519, 0.091282, 0.0,
	-0.0157698, -0.307475, -0.951425, 0.0,
	-0.603467, -0.250192, 0.757121, 0.0,
	0.506876, 0.25006, 0.824952, 0.0,
	0.255404, 0.966794, 0.00884498, 0.0,
	0.466764, -0.874228, -0.133625, 0.0,
	0.475077, -0.0682351, -0.877295, 0.0,
	-0.224967, -0.938972, -0.260233, 0.0,
	-0.377929, -0.814757, -0.439705, 0.0,
	-0.305847, 0.542333, -0.782517, 0.0,
	0.26658, -0.902905, -0.337191, 0.0,
	0.0275773, 0.322158, -0.946284, 0.0,
	0.0185422, 0.716349, 0.697496, 0.0,
	-0.20483, 0.978416, 0.0273371, 0.0,
	-0.898276, 0.373969, 0.230752, 0.0,
	-0.00909378, 0.546594, 0.837349, 0.0,
	0.6602, -0.751089, 0.000959236, 0.0,
	0.855301, -0.303056, 0.420259, 0.0,
	0.797138, 0.0623013, -0.600574, 0.0,
	0.48947, -0.866813, 0.0951509, 0.0,
	0.251142, 0.674531, 0.694216, 0.0,
	-0.578422, -0.737373, -0.348867, 0.0,
	-0.254689, -0.514807, 0.818601, 0.0,
	0.374972, 0.761612, 0.528529, 0.0,
	0.640303, -0.734271, -0.225517, 0.0,
	-0.638076, 0.285527, 0.715075, 0.0,
	0.772956, -0.15984, -0.613995, 0.0,
	0.798217, -0.590628, 0.118356, 0.0,
	-0.986276, -0.0578337, -0.154644, 0.0,
	-0.312988, -0.94549, 0.0899272, 0.0,
	-0.497338, 0.178325, 0.849032, 0.0,
	-0.101136, -0.981014, 0.165477, 0.0,
	-0.521688, 0.0553434, -0.851339, 0.0,
	-0.786182, -0.583814, 0.202678, 0.0,
	-0.565191, 0.821858, -0.0714658, 0.0,
	0.437895, 0.152598, -0.885981, 0.0,
	-0.92394, 0.353436, -0.14635, 0.0,
	0.212189, -0.815162, -0.538969, 0.0,
	-0.859262, 0.143405, -0.491024, 0.0,
	0.991353, 0.112814, 0.0670273, 0.0,
	0.0337884, -0.979891, -0.196654, 0.0
//</editor-fold>
    };

    /**
     * Performs linear interpolation between two 8-bit channel values.
     *
     * @param channel0 value at alpha 0
     * @param channel1 value at alpha 1
     * @param alpha value of interpolation
     * @return interpolated value
     */
    public static short blendChannel(short channel0, short channel1,
	    double alpha) {
	double c0 = channel0 / 255.0;
	double c1 = channel1 / 255.0;
	return (short) (((c1 * alpha) + (c0 * (1.0f - alpha))) * 255.0f);
    }

    /**
     * Clamps a value onto a clamping range. @param value The value to clamp.
     *
     * @param value value to clamp
     * @param lowerBound The lower bound of the clamping range.
     *
     * @param upperBound The upper bound of the clamping range.
     * @return - @a value if @a value lies between @a lowerBound and @a
     * upperBound. - @a lowerBound if @a value is less than @a lowerBound. - @a
     * upperBound if @a value is greater than @a upperBound. This function does
     * not modify any parameters.
     */
    public static int clampValue(int value, int lowerBound, int upperBound) {
	if (value < lowerBound) {
	    return lowerBound;
	} else if (value > upperBound) {
	    return upperBound;
	} else {
	    return value;
	}
    }

    /**
     * Generic version of clamp.
     *
     * @param <T> parameter type value, must be comparable
     * @param value value to clamp
     * @param lowerBound lower bound
     * @param upperBound upper bound
     * @return
     */
    public static <T extends Comparable<? super T>> T clampValue(T value,
	    T lowerBound, T upperBound) {
	if (value.compareTo(lowerBound) < 0) {
	    return lowerBound;
	}
	if (value.compareTo(upperBound) > 0) {
	    return upperBound;
	}
	return value;
    }

    /**
     * Performs cubic interpolation between two values bound between two other
     * values.
     *
     * @param n0 The value before the first value.
     * @param n1 The first value.
     * @param n2 The second value.
     * @param n3 The value after the second value.
     * @param a The alpha value
     * @return The alpha value
     *
     * The alpha value should range from 0.0 to 1.0. If the alpha value is 0.0,
     * this function returns @a n1. If the alpha value is 1.0, this function
     * returns @a n2.
     *
     */
    public static double cubicInterp(double n0, double n1, double n2, double n3,
	    double a) {
	double p = (n3 - n2) - (n0 - n1);
	double q = (n0 - n1) - p;
	double r = n2 - n0;
	double s = n1;
	return p * a * a * a + q * a * a + r * a + s;
    }

    /**
     * Returns the maximum of two values.
     *
     * @param <T> Type to change
     * @param a The first value.
     * @param b The second value.
     *
     * @return The maximum of the two values.
     */
    public static <T extends Comparable<? super T>> T getMax(T a, T b) {
	if (a.compareTo(b) > 0) {
	    return a;
	}
	return b;
    }

    /**
     * Returns the minimum of two values.
     *
     * @param <T> type parameter that needs to be comparable
     * @param a The first value.
     * @param b The second value.
     *
     * @return The minimum of the two values.
     */
    public static <T extends Comparable<? super T>> T getMin(T a, T b) {
	if (a.compareTo(b) > 0) {
	    return b;
	}
	return a;
    }

    /**
     * Generates a gradient-coherent-noise value from the coordinates of a
     * three-dimensional input value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     * @param seed The random number seed.
     * @param noiseQuality The quality of the coherent-noise.
     *
     * @return The generated gradient-coherent-noise value.
     *
     * The return value ranges from -1.0 to +1.0.
     *
     * For an explanation of the difference between <i>gradient</i> noise and
     * <i>value</i> noise, see the comments for the GradientNoise3D() function.
     */
    public static double gradientCoherentNoise3D(double x, double y, double z,
	    int seed, NoiseQuality noiseQuality) {
	// Create a unit-length cube aligned along an integer boundary.  This cube
	// surrounds the input point.
	int x0 = (x > 0.0 ? (int) x : (int) x - 1);
	int x1 = x0 + 1;
	int y0 = (y > 0.0 ? (int) y : (int) y - 1);
	int y1 = y0 + 1;
	int z0 = (z > 0.0 ? (int) z : (int) z - 1);
	int z1 = z0 + 1;
	double xs = 0, ys = 0, zs = 0;
	switch (noiseQuality) {
	    case FAST:
		xs = (x - x0);
		ys = (y - y0);
		zs = (z - z0);
		break;
	    case STD:
		xs = sCurve3(x - x0);
		ys = sCurve3(y - y0);
		zs = sCurve3(z - z0);
		break;
	    case BEST:
		xs = sCurve5(x - x0);
		ys = sCurve5(y - y0);
		zs = sCurve5(z - z0);
		break;
	}
	double n0, n1, ix0, ix1, iy0, iy1;
	n0 = gradientNoise3D(x, y, z, x0, y0, z0, seed);
	n1 = gradientNoise3D(x, y, z, x1, y0, z0, seed);
	ix0 = linearInterp(n0, n1, xs);
	n0 = gradientNoise3D(x, y, z, x0, y1, z0, seed);
	n1 = gradientNoise3D(x, y, z, x1, y1, z0, seed);
	ix1 = linearInterp(n0, n1, xs);
	iy0 = linearInterp(ix0, ix1, ys);
	n0 = gradientNoise3D(x, y, z, x0, y0, z1, seed);
	n1 = gradientNoise3D(x, y, z, x1, y0, z1, seed);
	ix0 = linearInterp(n0, n1, xs);
	n0 = gradientNoise3D(x, y, z, x0, y1, z1, seed);
	n1 = gradientNoise3D(x, y, z, x1, y1, z1, seed);
	ix1 = linearInterp(n0, n1, xs);
	iy1 = linearInterp(ix0, ix1, ys);
	return linearInterp(iy0, iy1, zs);
    }

    /**
     * Generates a gradient-coherent-noise value from the coordinates of a
     * three-dimensional input value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     *
     * @return The generated gradient-coherent-noise value.
     *
     * The return value ranges from -1.0 to +1.0.
     *
     * For an explanation of the difference between <i>gradient</i> noise and
     * <i>value</i> noise, see the comments for the GradientNoise3D() function.
     */
    public static double gradientCoherentNoise3D(double x, double y, double z) {
	return gradientCoherentNoise3D(x, y, z, 0, STD);
    }

    /**
     * Generates a gradient-noise value from the coordinates of a
     * three-dimensional input value and the integer coordinates of a nearby
     * three-dimensional value.
     *
     * @param fx The floating-point @a x coordinate of the input value.
     * @param fy The floating-point @a y coordinate of the input value.
     * @param fz The floating-point @a z coordinate of the input value.
     * @param ix The integer @a x coordinate of a nearby value.
     * @param iy The integer @a y coordinate of a nearby value.
     * @param iz The integer @a z coordinate of a nearby value.
     * @param seed The random number seed.
     *
     * @return The generated gradient-noise value.
     *
     * @noise.pre The difference between @a fx and @a ix must be less than or
     * equal to one.
     *
     * @noise.pre The difference between @a fy and @a iy must be less than or
     * equal to one.
     *
     * @noise.pre The difference between @a fz and @a iz must be less than or
     * equal to one.
     *
     * A <i>gradient</i>-noise function generates better-quality noise than a
     * <i>value</i>-noise function. Most noise modules use gradient noise for
     * this reason, although it takes much longer to calculate.
     *
     * The return value ranges from -1.0 to +1.0.
     *
     * This function generates a gradient-noise value by performing the
     * following steps: - It first calculates a random normalized vector based
     * on the nearby integer value passed to this function. - It then calculates
     * a new value by adding this vector to the nearby integer value passed to
     * this function. - It then calculates the dot product of the
     * above-generated value and the floating-point input value passed to this
     * function.
     *
     * A noise function differs from a random-number generator because it always
     * returns the same output value if the same input value is passed to it.
     */
    public static double gradientNoise3D(double fx, double fy, double fz, int ix,
	    int iy, int iz, int seed) {
	// Randomly generate a gradient vector given the integer coordinates of the
	// input value.  This implementation generates a random number and uses it
	// as an index into a normalized-vector lookup table.
	int vectorIndex
		= (X_NOISE_GEN * ix
		+ Y_NOISE_GEN * iy
		+ Z_NOISE_GEN * iz
		+ SEED_NOISE_GEN * seed);
	vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
	vectorIndex &= 0xff;
	double xvGradient = randomVectors[(vectorIndex << 2)];
	double yvGradient = randomVectors[(vectorIndex << 2) + 1];
	double zvGradient = randomVectors[(vectorIndex << 2) + 2];
	// Set up us another vector equal to the distance between the two vectors
	// passed to this function.
	double xvPoint = fx - ix;
	double yvPoint = fy - iy;
	double zvPoint = fz - iz;
	// Now compute the dot product of the gradient vector with the distance
	// vector.  The resulting value is gradient noise.  Apply a scaling value
	// so that this noise value ranges from -1.0 to 1.0.
	return ((xvGradient * xvPoint)
		+ (yvGradient * yvPoint)
		+ (zvGradient * zvPoint)) * 2.12;
    }

    /**
     * Generates a gradient-noise value from the coordinates of a
     * three-dimensional input value and the integer coordinates of a nearby
     * three-dimensional value.
     *
     * @param fx The floating-point @a x coordinate of the input value.
     * @param fy The floating-point @a y coordinate of the input value.
     * @param fz The floating-point @a z coordinate of the input value.
     * @param ix The integer @a x coordinate of a nearby value.
     * @param iy The integer @a y coordinate of a nearby value.
     * @param iz The integer @a z coordinate of a nearby value.
     *
     * @return The generated gradient-noise value.
     *
     * @noise.pre The difference between @a fx and @a ix must be less than or
     * equal to one.
     *
     * @noise.pre The difference between @a fy and @a iy must be less than or
     * equal to one.
     *
     * @noise.pre The difference between @a fz and @a iz must be less than or
     * equal to one.
     *
     * A <i>gradient</i>-noise function generates better-quality noise than a
     * <i>value</i>-noise function. Most noise modules use gradient noise for
     * this reason, although it takes much longer to calculate.
     *
     * The return value ranges from -1.0 to +1.0.
     *
     * This function generates a gradient-noise value by performing the
     * following steps: - It first calculates a random normalized vector based
     * on the nearby integer value passed to this function. - It then calculates
     * a new value by adding this vector to the nearby integer value passed to
     * this function. - It then calculates the dot product of the
     * above-generated value and the floating-point input value passed to this
     * function.
     *
     * A noise function differs from a random-number generator because it always
     * returns the same output value if the same input value is passed to it.
     */
    public static double gradientNoise3D(double fx, double fy, double fz, int ix,
	    int iy, int iz) {
	return gradientNoise3D(fx, fy, fz, ix, iy, iz, 0);
    }

    /**
     * Generates an integer -noise value from the coordinates of a three
     * -dimensional input value.@param x The integer @a x coordinate of the
     * input value.@param y The integer @a y coordinate of the input
     * value.@param z The integer @a z coordinate of the input value.@param seed
     * A random number seed.@return The generated integer -noise value
     *
     * The return value ranges from 0 to 2147483647. A noise function differs
     * from a random -number generator because it always returns the same output
     * value if the same input value is passed to it
     *
     * @param x integer location x
     * @param y integer location y
     * @param z integer location z
     * @param seed seed value
     * @return noise value at cell
     */
    public static int intValueNoise3D(int x, int y, int z, int seed) {
	// All constants are primes and must remain prime in order for this noise
	// function to work correctly.
	int n
		= (X_NOISE_GEN * x
		+ Y_NOISE_GEN * y
		+ Z_NOISE_GEN * z
		+ SEED_NOISE_GEN * seed) & 0x7fff_ffff;
	n = (n >> 13) ^ n;
	return (n * (n * n * 60_493 + 19_990_303) + 1_376_312_589) & 0x7fff_ffff;
    }

    /**
     * Generates an integer -noise value from the coordinates of a three
     * -dimensional input value.@param x The integer @a x coordinate of the
     * input value.@param y The integer @a y coordinate of the input
     * value.@param z The integer @a z coordinate of the input value.@param seed
     * A random number seed.@return The generated integer -noise value
     *
     * The return value ranges from 0 to 2147483647. A noise function differs
     * from a random -number generator because it always returns the same output
     * value if the same input value is passed to it
     *
     * @param x integer location x
     * @param y integer location y
     * @param z integer location z
     * @return noise value at cell
     */
    public static int intValueNoise3D(int x, int y, int z) {
	return intValueNoise3D(x, y, z, 0);
    }

    /**
     * Converts a latitude/longitude coordinates on a unit sphere into 3D
     * Cartesian coordinates.
     *
     * @param lat Latitude in degrees
     * @param lon Longitude in degrees
     * @return Vec3 object that has the coordinates of the longitude/latitude.
     *
     * lat should be in range -90 to 90
     *
     * lon should be in range -180 to 180
     */
    public static Vec3 latLonToXYZ(double lat, double lon) {
	Vec3 vec = new Vec3();
	double r = cos(DEG_TO_RAD * lat);
	vec.x = r * cos(DEG_TO_RAD * lon);
	vec.y = sin(DEG_TO_RAD * lat);
	vec.z = r * sin(DEG_TO_RAD * lon);
	return vec;
    }

    /**
     * Performs linear interpolation between two values.
     *
     * @param n0 The first value.
     * @param n1 The second value.
     * @param a The alpha value.
     * @return The interpolated value
     *
     * the alpha value should range from 0.0 to 1.0. If the alpha value is 0.0
     * the function returns n0, if the alpha value is 1.0, this function returns
     * n1.
     */
    public static double linearInterp(double n0, double n1, double a) {
	return ((1.0 - a) * n0) + (a * n1);
    }

    /**
     * Performs linear interpolation between two colors and stores the result in
     * out.
     *
     * @param color0 color at 0 alpha
     * @param color1 color at 1 alpha
     * @param alpha interpolation value
     * @param out color to set to the value of the interpolation
     */
    public static void linearInterpColor(Color color0, Color color1,
	    double alpha, Color out) {
	out.alpha = blendChannel(color0.alpha, color1.alpha, alpha);
	out.blue = blendChannel(color0.blue, color1.blue, alpha);
	out.green = blendChannel(color0.green, color1.green, alpha);
	out.red = blendChannel(color0.red, color1.red, alpha);
    }

    /**
     * Modifies a floating-point value so that it can be stored in a
     * noise::int32 variable.
     *
     * @param n A floating-point number.
     *
     * @return The modified floating-point number.
     *
     * This function does not modify @a n.
     *
     * In libnoise, the noise-generating algorithms are all integer-based; they
     * use variables of type noise::int32. Before calling a noise function, pass
     * the @a x, @a y, and @a z coordinates to this function to ensure that
     * these coordinates can be cast to a noise::int32 value.
     *
     * Although you could do a straight cast from double to noise::int32, the
     * resulting value may differ between platforms. By using this function, you
     * ensure that the resulting value is identical between platforms.
     */
    public static double makeIntRange(double n) {
	if (n >= 1073741824.0) {
	    return (2.0 * (n % 1073741824.0)) - 1073741824.0;
	} else if (n <= -1073741824.0) {
	    return (2.0 * (n % 1073741824.0)) + 1073741824.0;
	} else {
	    return n;
	}
    }

    /**
     * Maps a value onto a cubic S-curve.
     *
     * @param a The value to map onto a cubic S-curve.
     * @return The mapped value.
     *
     * Parameter a should range from 0.0 to 1.0
     *
     * The derivitive of a cubic S-curve is zero at a=0 and at a=1
     */
    public static double sCurve3(double a) {
	return (a * a * (3.0 - 2.0 * a));
    }

    /**
     * Maps a value onto a quintic S-curve
     *
     * @param a The value to map onto a quintic S-curve.
     * @return The mapped value
     *
     * Parameter a should range from 0.0 to 1.0.
     *
     * The first derivitive of a quintic S-curve is zero at a=0.0 and at a=1.0
     *
     * The second derivitive of a quintic S-curve is zero at a=0 and at a=1
     */
    public static double sCurve5(double a) {
	double a3 = a * a * a;
	double a4 = a3 * a;
	double a5 = a4 * a;
	return (6.0 * a5) - (15.0 * a4) + (10.0 * a3);
    }

    /**
     * Generates a value-coherent-noise value from the coordinates of a
     * three-dimensional input value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     * @param seed The random number seed.
     * @param noiseQuality The quality of the coherent-noise.
     *
     * @return The generated value-coherent-noise value.
     *
     * The return value ranges from -1.0 to +1.0.
     *
     * For an explanation of the difference between <i>gradient</i> noise and
     * <i>value</i> noise, see the comments for the GradientNoise3D() function.
     */
    public static double valueCoherentNoise3D(double x, double y, double z,
	    int seed, NoiseQuality noiseQuality) {
	// Create a unit-length cube aligned along an integer boundary.  This cube
	// surrounds the input point.
	int x0 = (x > 0.0 ? (int) x : (int) x - 1);
	int x1 = x0 + 1;
	int y0 = (y > 0.0 ? (int) y : (int) y - 1);
	int y1 = y0 + 1;
	int z0 = (z > 0.0 ? (int) z : (int) z - 1);
	int z1 = z0 + 1;
	double xs = 0, ys = 0, zs = 0;
	switch (noiseQuality) {
	    case FAST:
		xs = (x - x0);
		ys = (y - y0);
		zs = (z - z0);
		break;
	    case STD:
		xs = sCurve3(x - x0);
		ys = sCurve3(y - y0);
		zs = sCurve3(z - z0);
		break;
	    case BEST:
		xs = sCurve5(x - x0);
		ys = sCurve5(y - y0);
		zs = sCurve5(z - z0);
		break;
	}
	double n0, n1, ix0, ix1, iy0, iy1;
	n0 = valueNoise3D(x0, y0, z0, seed);
	n1 = valueNoise3D(x1, y0, z0, seed);
	ix0 = linearInterp(n0, n1, xs);
	n0 = valueNoise3D(x0, y1, z0, seed);
	n1 = valueNoise3D(x1, y1, z0, seed);
	ix1 = linearInterp(n0, n1, xs);
	iy0 = linearInterp(ix0, ix1, ys);
	n0 = valueNoise3D(x0, y0, z1, seed);
	n1 = valueNoise3D(x1, y0, z1, seed);
	ix0 = linearInterp(n0, n1, xs);
	n0 = valueNoise3D(x0, y1, z1, seed);
	n1 = valueNoise3D(x1, y1, z1, seed);
	ix1 = linearInterp(n0, n1, xs);
	iy1 = linearInterp(ix0, ix1, ys);
	return linearInterp(iy0, iy1, zs);
    }

    /**
     * Generates a value-coherent-noise value from the coordinates of a
     * three-dimensional input value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     *
     * @return The generated value-coherent-noise value.
     *
     * The return value ranges from -1.0 to +1.0.
     *
     * For an explanation of the difference between <i>gradient</i> noise and
     * <i>value</i> noise, see the comments for the GradientNoise3D() function.
     */
    public static double valueCoherentNoise3D(double x, double y, double z) {
	return valueCoherentNoise3D(x, y, z, 0, STD);
    }

    /**
     * Generates a value-noise value from the coordinates of a three-dimensional
     * input value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     * @param seed A random number seed.
     *
     * @return The generated value-noise value.
     *
     * The return value ranges from -1.0 to +1.0.
     *
     * A noise function differs from a random-number generator because it always
     * returns the same output value if the same input value is passed to it.
     */
    public static double valueNoise3D(int x, int y, int z, int seed) {
	return 1.0 - (intValueNoise3D(x, y, z, seed) / 1073741824.0);
    }

    /**
     * Generates a value-noise value from the coordinates of a three-dimensional
     * input value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     *
     * @return The generated value-noise value.
     *
     * The return value ranges from -1.0 to +1.0.
     *
     * A noise function differs from a random-number generator because it always
     * returns the same output value if the same input value is passed to it.
     */
    public static double valueNoise3D(int x, int y, int z) {
	return valueNoise3D(x, y, z, 0);
    }

    private static final Logger LOG = Logger.getLogger(Globals.class.getName());

}
