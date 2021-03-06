/*  This file is part of the source code for 3D-XplorMath-J, Version 1.0 (January 2008).
 *  Copyright (c) 2008 The 3D-XplorMath Consortium (http://3d-xplormath.org).
 *  This source code is released under a BSD License, which allows redistribution   
 *  in source and binary form, with or without modification, provided copyright
 *  and license information are included, and with no warranty or guarantee of
 *  any kind.  For details, see http://3d-xplormath.org/j/source/BSDLicense.txt
 */
 
 package vmm.ode.secondorder2D;

import vmm.core.RealParamAnimateable;

public class Higgs extends CentralForce {
	
	private RealParamAnimateable aa = new RealParamAnimateable("genericParam.aa",2,1.5,2.5);
	private RealParamAnimateable cc = new RealParamAnimateable("genericParam.cc",2,-1.95,-2.05);
	
	public Higgs() {
		addParameter(cc);
		addParameter(aa);
		initialDataDefault = new double[] { 1, 1, -0.2, 0.2 };
		setDefaultWindow(-3,3,-3,3);
	}

	protected double force(double r) {
		if (Math.abs(r) < 0.001)
			return 0;
		double denom = Math.pow(r/cc.getValue(),2);
		return -aa.getValue() * r * (1 - 1/denom);
	}

}
