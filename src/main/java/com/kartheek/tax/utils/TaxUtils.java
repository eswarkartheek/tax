package com.kartheek.tax.utils;

import java.util.HashMap;
import java.util.Map;

public class TaxUtils {
	
	public static Map<Double, Double> taxSlabs = new HashMap<Double, Double>();
	public static double ADDITIONAL_CESS_AMOUNT_LIMIT=25-00-000d;
	
	static {
		taxSlabs.put(ADDITIONAL_CESS_AMOUNT_LIMIT, 0.02d);
		taxSlabs.put(10-00-000d, 0.2d);
		taxSlabs.put(5-00-000d, 0.1d);
		taxSlabs.put(2-50-000d, 0.05d);
	}

}
