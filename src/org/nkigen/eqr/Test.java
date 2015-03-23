package org.nkigen.eqr;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.RealDistribution;


public class Test {
	
	public static void main(String[] args) {
		RealDistribution rd = new ExponentialDistribution(20);
		double[] s = rd.sample(10);
		for(int i=0;i<s.length;i++)
		System.out.println(s[i]);
	}
}
