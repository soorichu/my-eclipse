/*  This file is part of the source code for 3D-XplorMath-J, Version 1.0 (January 2008).
 *  Copyright (c) 2008 The 3D-XplorMath Consortium (http://3d-xplormath.org).
 *  This source code is released under a BSD License, which allows redistribution   
 *  in source and binary form, with or without modification, provided copyright
 *  and license information are included, and with no warranty or guarantee of
 *  any kind.  For details, see http://3d-xplormath.org/j/source/BSDLicense.txt
 */
 
 package vmm.surface.parametric;

import java.awt.Color;

import vmm.core.Complex;
import vmm.core.I18n;
import vmm.core.IntegerParam;
import vmm.core.Parameter;
import vmm.core.RealParamAnimateable;
import vmm.core.View;
import vmm.core3D.ComplexVector3D;
import vmm.core3D.GridTransformMatrix;
import vmm.core3D.Vector3D;
import vmm.core3D.View3DLit;
import vmm.surface.SurfaceView;

public class OtherTriangCatenoid extends WeierstrassMinimalSurface {
	
	private RealParamAnimateable aa = new RealParamAnimateable("vmm.surface.parametric.OtherTriangCatenoid.aa",0.5,0.05,0.98);
	//private RealParamAnimateable bb = new RealParamAnimateable("vmm.surface.parametric.SchwarzHsurface.bb",0.6,0.52,0.98);
	//ComplexVector3D[][] integrationGrid;
	//boolean needsIntegrationGrid = true;
	
	public OtherTriangCatenoid() {
		super();
		addParameter(aa);
		aa.setMaximumValueForInput(0.99); // if one wants to morph that far, increase resolution
		aa.setMinimumValueForInput(0.02);
		setDefaultOrientation(View3DLit.NORMAL_ORIENTATION);
		setDefaultViewUp(new Vector3D(-0.045,  0.4,  0.915));
		setDefaultViewpoint(new Vector3D(12, -44, 20));
		setDefaultWindow(-3,3,-2.8,2.2);
			uPatchCount.setValueAndDefault(18);
			vPatchCount.setValueAndDefault(12);
			umin.reset(0.005);
			umax.reset(0.9975);
			vmin.reset(0.0005); 
			vmax.reset(1.9995);
		removeParameter(vmin);
		removeParameter(vmax);
		removeParameter(umin);
		removeParameter(umax);
		//afp.setValue(Math.PI/2);
		iFirstInHelper = false;
		iBeginMiddleInHelper = true;
		// change the next two booleans for testing (or later for education)
		wantsToSeeDomain = false;//true;//
		wantsToSeeGaussImage = false;//true;//
		if (wantsToSeeGaussImage)  wantsToSeeDomain = true;
		if (wantsToSeeDomain)		 {setDefaultViewUp(new Vector3D(0,0,1));
	  	                           setDefaultViewpoint(new Vector3D(0,0,40));}
		//multipleCopyOptions = new int[] {2};
		canShowConjugateSurface = true;
	}
	public View getDefaultView() {
		//SurfaceView view = (SurfaceView)super.getDefaultView();  // its actually a WMSView
		WMSView view = new WMSView();
		view.setGridSpacing(12);
		float c0 = (float)0.2;
		//view.getLightSettings().setLight0(new Color(c0,c0,c0));
		view.getLightSettings().setAmbientLight(new Color(c0,c0,c0));
		//view.getLightSettings().getDirectionalLight2().setItsColor(new Color((float)0.5,(float)1.0,(float)0.0));
		//view.getLightSettings().getDirectionalLight2().setItsDirection (new Vector3D(0.66,-0.34,-0.66));
		view.getLightSettings ().setSpecularExponent(100);
		view.getLightSettings().setSpecularRatio( (float)0.8 );
		return view;
	}

	private double gbranch,a3,scale;
	private Vector3D trans, trans_, transY, transZ, surf_1, surf_2, surf_3, normSym;
	private GridTransformMatrix Msym;
	
	public void parameterChanged(Parameter param, Object oldValue, Object newValue) {
		     super.parameterChanged(param, oldValue, newValue);
		     AFP = afp.getValue();
		     if ((param != afp)||(AFP==0.0))
		    	 	needsValueArray = true;
	}
	
	protected void createData() {
        super.createData();     // helperArray created, flag0 & flag05 are set
        computePeriodData();
        data.discardGridTransforms();
        GridTransformMatrix[] trList = new GridTransformMatrix[64];
        		trList[0] = new GridTransformMatrix();
        		GridTransformMatrix a = new GridTransformMatrix().scale(1,1,-1);
        		GridTransformMatrix b = new GridTransformMatrix().scale(-1,1,-1).reverse();
        		GridTransformMatrix Z120 = new GridTransformMatrix().rotateZ(120);
        if (!wantsToSeeDomain)		{ 
       	if (flag05||inAssociateMorph) {
        	for (int e=0; e < 2; e++){
        		trList[1+e] = new GridTransformMatrix(trList[e]).leftMultiplyBy(Z120);
        		data.addGridTransform(trList[1+e]); 
        		}
        	for (int e=0; e < 3; e++){
        		trList[3+e] = new GridTransformMatrix(trList[e]).scale(-1,-1,-1).reverse();
        		data.addGridTransform(trList[3+e]); 
        		}
        	} //if (flag05||inAssociateMorph)
       	  	
        	else if (flag0){
        		trList[1] = new GridTransformMatrix(trList[0]).leftMultiplyBy(Z120);
        		data.addGridTransform(trList[1]);
        		trList[2] = new GridTransformMatrix(trList[1]).leftMultiplyBy(Z120);
        		data.addGridTransform(trList[2]);
        	for (int e=0; e < 3; e++){
        		trList[3+e] = new GridTransformMatrix(trList[e]).leftMultiplyBy(Msym);
        		data.addGridTransform(trList[3+e]); 
        		}
    	 	for (int e=0; e < 6; e++){
        		trList[6+e] = new GridTransformMatrix(trList[e]).leftMultiplyBy(b).translate(transY);
        		data.addGridTransform(trList[6+e]); 
        		}
        	for (int e=0; e < 6; e++){
        		trList[12+e] = new GridTransformMatrix(trList[6+e]).leftMultiplyBy(Z120);
        		data.addGridTransform(trList[12+e]); 
        		}
        	} // else   
        } // (!wantsToSeeDomain)
	}
	
	public static double paramRescaleEnd(double x){
		double y;
		y = Math.sin(Math.PI/2*x);
		return y;
	}
	public static double paramRescaleStart(double x){
		double y;
		y = 1 - Math.cos(Math.PI/2*x);
		return y;
	}
	public static double paramRescaleBoth(double x){
		double y;
		y = (1.0 - Math.cos(Math.PI/2*x))/2.0;
		return y;
	}
	public static double paramRescaleMiddle(double x){
		double y;
		y = (1.0 + x*Math.abs(x))/2.0;
		return y;
	}
	// override the default Cartesian Grid
	protected Complex domainGrid(double u, double v){
		double deps = 1.0/1024.0;
		double ph = 0;
		double r = 0;
		Complex z;
			
		    if (u <= 2.0/3.0)	
		    		r = gbranch*paramRescaleBoth(3*u);
			else if (u<=1)
				r = gbranch + (1.0 - gbranch)*paramRescaleStart(3*u-2.0);
		    
	/*	    if (v<= 1.0/2.0)
		    		ph = 0.5*Math.PI/3.0*paramRescaleStart(1.5*v);
		    else if (v<= 3.0/2.0)
		    		ph = 0.5*Math.PI/3.0 + Math.PI/3.0*paramRescaleMiddle(3*v - 3);
		    	else if (v <= 2)
		    		ph = 0.5*Math.PI + 0.5*Math.PI/3.0*paramRescaleEnd(1.5*v - 2.0); 
	*/	    
		    ph = Math.PI/3.0*v;  // Concentration near the branch point conflicts with the symmetry
		    
			if (Math.abs(u-2.0/3.0)< deps) {
	 		if (v <= deps)
	 			ph = Math.PI/3.0*(deps-0.5*(deps-v));
	 		else if ((2-v) <= deps)
	 			ph = Math.PI/3.0*(2-deps + 0.5*(deps-(2-v)));
			}
			z = new Complex(r*Math.cos(ph), r*Math.sin(ph));
			if (wantsToSeeGaussImage) z = mu(z);
			return z;
	}
//	Index of grid point closest to branch value P. jP is the larger of the two branch point indices.
/*	protected void p_Index(){
		jP = (int) Math.floor((PL-vmin.getValue())/(vmax.getValue()-vmin.getValue())*(vcount-1));
	} */
	
	/**
	 * The following two functions are the Weierstrass data that
	 * define this surface. It is best shown on the above domainGrid.
	 */
	protected Complex gauss(Complex z)  {
		return new Complex(z);		
	    }
	
	protected Complex mu(Complex z)  {
		Complex z3 = new Complex(z.integerPower(3));
		Complex w3 = z3.inverse();
		Complex m2 = z3.minus(w3);   
				m2.re = + m2.re - a3;
				m2.assignInvert();
		Complex m; 
		m = m2.squareRootNearer(ONE_C);
		return m;		
	    }
	
	protected Complex hPrime(Complex z) {
		// return mu(z)/gauss(z)
		Complex w = mu(z);
		        w.assignDivide(z);
		        w.assignTimes(scale);
		return w;
		}
	
	protected void redoConstants(){
		// global variables independent of the points, hence thread safe
		super.redoConstants();
		gbranch = aa.getValue();
		a3 = gbranch*gbranch*gbranch;   a3 = a3 - 1/a3;
		scale= Math.sqrt(0.5/gbranch);
	}
	
	/**
	 * We want to center the surface (one hexagon) already at the helper Level.
	 */
	protected ComplexVector3D getCenter(){
		// gC is computed by assuming that half the i=0 parameter line is a 30 degree circle arc
		double savedAFP = AFP;
		double fac = 0.5/Math.sin(Math.PI/12.0);
		Complex rot = new Complex(fac*Math.cos(Math.PI*5.0/12.0), fac*Math.sin(Math.PI*5.0/12.0));
		Vector3D aux;
		ComplexVector3D MS0 = new ComplexVector3D(helperToMinimal(helperArray[0][0]));
		ComplexVector3D MSE = new ComplexVector3D(helperToMinimal(helperArray[0][(int)Math.floor(vcount/2.0)]));
		ComplexVector3D base30 = MSE.minus(MS0);
					   base30.assignTimes(rot);
					   base30.assignPlus(MS0);  base30.z.re = 0.0;
		ComplexVector3D gC = new ComplexVector3D(minimalToHelper(base30));
				// This puts the origin into the vertex of the triangular Catenoid
		if (!wantsToSeeDomain){
		double c6 = Math.cos(Math.PI/3);
		double s6 = Math.sin(Math.PI/3);
		  MS0 = new ComplexVector3D(helperToMinimal(helperArray[(int)Math.floor(ucount*2.0/3.0)][0]));
		  MSE = new ComplexVector3D(helperToMinimal(helperArray[(int)Math.floor(ucount*2.0/3.0)][vcount-1]));
		  AFP = 0.0;
		  	surf_1 = surfacePoint(0.5, 0);
		  	aux = surfacePoint(0.75, 0);
		  surf_1.y = aux.y;
		  		// The following corrects one branch valu ...
		  		MS0.x.re = surf_1.x; MS0.y.re = surf_1.y; MS0.z.re = surf_1.z;
		  		AFP = Math.PI/2; 
		  		aux = surfacePoint(0.5, 0);           MS0.y.im = aux.y;  
		  		aux = surfacePoint(0.75, 0);
		  		MS0.z.im = aux.z;  MS0.x.im = aux.x;
		  		helperArray[(int)Math.floor(ucount*2.0/3.0)][0]=minimalToHelper(MS0);
		  surf_1.assignMinus(base30.re());  surf_1.z = 0.0;
		  surf_2 = new Vector3D(0.5*Math.sqrt(3)*surf_1.y, 0.5*surf_1.y, 0);
		  		// ... and the other branch value
		  		MSE.x.re = surf_2.x+base30.x.re; MSE.y.re = surf_2.y+base30.y.re; MSE.z.re = surf_2.z;
		  		MSE.z.im = -aux.z; 
		  		MSE.x.im = MS0.x.im*c6 + MS0.y.im*s6;   MSE.y.im = -MS0.x.im*s6 + MS0.y.im*c6;
		  		helperArray[(int)Math.floor(ucount*2.0/3.0)][vcount-1]=minimalToHelper(MSE);
		  trans = surf_1.plus(surf_2).times(2.0/3.0); // translation from the vertex to the middle
		  base30.x.re = base30.x.re+trans.x; base30.y.re = base30.y.re+trans.y; 
		  base30.z.re = base30.z.re+trans.z; 

		if (flag0&&!flag05&&!inAssociateMorph)
			gC = minimalToHelper(base30);  // This puts the origin in the middle of the Catenoid
		
		AFP = savedAFP; }
		return gC;
	}
	
	public void computePeriodData(){
		double s3 = Math.cos(Math.PI/6.0);
		double c3 = Math.sin(Math.PI/6.0);
		ComplexVector3D MC = helperToMinimal(getCenter());
		normSym = surfaceNormal(1.0, 0.5);
		surf_1  = surfacePoint(1.0, 0.5);
		transZ  = new Vector3D(0, 0, 2*surf_1.z);
		Msym = GridTransformMatrix.SetGridTransformMatrix(c3, s3, 0, 0, s3,-c3, 0, 0, 0,0,-1,transZ.z, false);
		Vector3D aux = surfacePoint(0.5, 0);
		transY = new Vector3D(2*aux.x, 0, 0);
		
		//System.out.println(normSym.x);System.out.println(normSym.y);System.out.println(c3);
	}
	
	/**
	 * Override to close the hole around the images of P, -1/P.
	 */
/*	public Vector3D surfacePoint(double u, double v) {
			}
	*/   
}


