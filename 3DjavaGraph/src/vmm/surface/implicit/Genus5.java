/*  This file is part of the source code for 3D-XplorMath-J, Version 1.0 (January 2008).
 *  Copyright (c) 2008 The 3D-XplorMath Consortium (http://3d-xplormath.org).
 *  This source code is released under a BSD License, which allows redistribution   
 *  in source and binary form, with or without modification, provided copyright
 *  and license information are included, and with no warranty or guarantee of
 *  any kind.  For details, see http://3d-xplormath.org/j/source/BSDLicense.txt
 */
 
 package vmm.surface.implicit;

import vmm.core3D.Vector3D;

public class Genus5 extends SurfaceImplicit {


	public static double sqr(double x){return x*x;}
	

	public double heightFunction(double x, double y, double z) {
		double height;
		double xsqr =sqr(x); double ysqr = sqr(y); double zsqr = sqr(z); 
		height = sqr((xsqr + ysqr/4 -1)*(xsqr/4 + ysqr -1)) + 0.5*zsqr; 
		return height;
	}
	
	public Genus5(){
		setDefaultWindow(-2.0, 2.0, -2.0, 2.0);
		setDefaultViewpoint(new Vector3D(10,-10,30));
		setDefaultViewUp(new Vector3D(-0.639602,0.639602,0.426401));
		searchRadius.reset(3);
		randomLineCount.reset(60000);
		pointCloudCount.reset(12000);
		level.reset(0.075,0.075,0.075);
		setFramesForMorphing(12);
	}

}