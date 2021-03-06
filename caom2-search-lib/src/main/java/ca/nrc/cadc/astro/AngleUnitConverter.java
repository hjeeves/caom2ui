// Created on 19-Feb-2006

package ca.nrc.cadc.astro;

import ca.nrc.cadc.util.ArrayUtil;

/**
 * Convert angles in various units to degrees.
 *
 * @version $Version$
 * @author pdowler
 */
public class AngleUnitConverter implements UnitConverter
{
	private static String coreUnit = "degrees";
	private static String[] allUnits = { "arcsec", "arcmin", "degrees" };
	private static double[] mult = new double[] { 1.0/3600.0, 1.0/60.0, 1.0 };
	
	public String getCoreUnit() { return coreUnit; }
	
	public String[] getSupportedUnits() { return allUnits; }
	
	/**
	 * Convert the supplied value/units to a value expressed in core angle units.
	 */
	public double convert(double value, String units)
	{
		int i = ArrayUtil.find(units, allUnits);
		if (i != -1)
			return value * mult[i];
		throw new IllegalArgumentException("unknown units: " + units);
	}
}
