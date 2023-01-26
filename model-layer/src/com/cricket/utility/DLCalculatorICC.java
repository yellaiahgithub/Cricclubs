package com.cricket.utility;

import java.util.List;

import com.cricket.dlscalculation.MatchDLRecord;

public class DLCalculatorICC  {
	
  protected String[] maxotyp = { "50", "45", "40" };
  protected int[] minotyp = { 10, 20, 10 };
  protected double[] g50pr = {264.9, 228.1};   // For T1 projection option (men/women)
  protected double[] lprior = {1.0068, 1.0000};  // For T1 projection option (men/women)
  protected double lmbcap = 9.9999;
  protected double lmbcap1 = 2.9999;
  protected double Scap = 1501.27;
  protected double lmb350 = 1.0575;
  protected double lmb450 = 1.1274;
  protected double lmb600 = 1.2800;
  protected double[] p350anchor = { 2.55, 62.0, 110.0, 149.0, 183.0, 214.0, 243.0, 271.0, 298.0, 324.0, 350.0 };
  protected double[] p450anchor = { 2.76, 69.0, 126.0, 175.0, 219.0, 261.0, 301.0, 339.0, 376.0, 413.0, 450.0 };
  protected double[] p600anchor = { 2.83, 77.0, 146.0, 209.0 ,268.0, 325.0, 380.0, 435.0, 490.0, 545.0, 600.0 };
  protected int optnum = minotyp.length + 1;
  protected int curpar, curpar1, home1, horig, g50typ, iabnd, iabnda, icrt, idrwcnt, pdrwcnt, iPtrg, iPtrga, itmp;
  protected int itmp1, iT2trg, lstrow1, lstrow2, maxrows, maxtst, minover, mprx1, mprx2, mpry1, mpry2, mtyp, nb3;
  protected int nb4, ng3, ni39, nommaxo, nstprow, omtyp, prjscr, rsltcde, rtoget, rtoget1, runs1a, runs1, runs2;
  protected int runs2a, trgt, trgttst1, trgttst2, winmarg, wckt1, wckt2, wckt2a, worig, xorig, yorig;
  protected int[] ncs = new int[31];
  protected int[] nc1s = new int[31];
  protected int[] nhs = new int[31];
  protected int[] nh1s = new int[31];
  protected int[] rowcnt1 = new int[30];
  protected int[] rowcnt2 = new int[30];
  protected int[][] impty = new int[61][4];  
  protected int[][] slctd = new int[30][8];
  protected Integer ntmp;
  protected double[] rsrcpr = new double[3];
  protected double[] dbs = new double[31];
  protected double[] dds = new double[31];
  protected double[] dgs = new double[31];
  protected double[] dis = new double[31];
  protected double[] dbsd = new double[31];
  protected double[] ddsd = new double[31];
  protected double[] dgsd = new double[31];
  protected double[] disd = new double[31];
  protected double[] Fw = new double[10];
  protected double[] pw11 = new double[10];
  protected double[] nw = new double[10];
  protected double adjfct, bb, g50, g50u, z0u, lmbdastar, lmbfct, lmbprj, olft2, oltot1, oltot2, omax1, omax2;
  protected double oplyd1, oplyd2, opmax1a, opmax2a, stpint, tmp, tmpa, z0, lmbu, R1;
  protected Double dtmp;

	public DLCalculatorICC() {

		bb = 0.03125; // Set D/L Constants (as of 10/2020)
		g50 = 253.15;
		z0 = g50 / (1 - Math.exp(-50.0 * bb));
		pw11[0] = 0.0;
		pw11[1] = 0.13;
		pw11[2] = 0.1325;
		pw11[3] = 0.1325;
		pw11[4] = 0.1275;
		pw11[5] = 0.1175;
		pw11[6] = 0.105;
		pw11[7] = 0.09;
		pw11[8] = 0.0725;
		pw11[9] = 0.0525;
		stpint = 0.0001;
		lmbdastar = 1.0;

		lmbcap = 9.9999;
		lmbcap1 = 2.9999;
		Scap = 1586.43;
		lmb350 = 1.0531;
		lmb450 = 1.1157;
		lmb600 = 1.2454;

		bb = 0.034;
		g50 = 254.25;
		z0 = g50 / (1 - Math.exp(-50.0 * bb));
	    lmbdastar = 1.0;
		setFw();

	}

// This function checks that an input 'overs.balls' value is valid
  public boolean chkOdB(String cc) {
    int idot, nbll;
    Integer nbll1;
    boolean out;
    String bll;
    idot = cc.indexOf(".");
    out = true;
    if(idot < 0) {
      out = false;
    } else {
      if(idot == (cc.length() - 1)) {
        out = false;
      }
      if(idot == (cc.length() - 2)) {
        bll = cc.substring(idot+1);
        nbll1 = Integer.valueOf(bll);
        nbll = nbll1.intValue();
        if((nbll > -1) & (nbll < 7)) {
          out = false;
        } 
      }
    }
    return out;
  }

// This function translates 'overs.balls' to decimal values
  public double odbTodec(double xx) {
    double ovrs, blls, out;
    ovrs = Math.floor(xx);
    blls = xx - Math.floor(xx);
    out = ovrs + (blls/0.6);
    return out;
  }
  
// This function translates decimal values to 'overs.balls' format
  public String decToodb(double xx) {
    int ovrs, blls;
    String cblls, out;
    Integer dovrs, iblls;
    ovrs = (int)Math.floor(xx);
    blls = (int)Math.round(6*(xx - Math.floor(xx)));
    if(blls == 6) {
      blls = 0;
      ovrs = ovrs + 1;
    }
    iblls = new Integer(blls);
    cblls = iblls.toString();
    dovrs = new Integer(ovrs);
    if(blls == 0) {
      out = dovrs.toString();
    } else {
      out = dovrs.toString();
      out = out + "." + cblls;
    }
    return out;
  }

// This function calculates a par score from overs remaining and wickets lost (uses DL contsants, adjustment factor, lambda factor)
  public String parscore(double olft, int wkt) {
    double Fw1, nw1;
    int par, pard;
    String out;
    Fw1 = Fw[wkt];
    nw1 = nw[wkt];
    pard = (int)Math.round(adjfct*z0*Gfun(olft,lmbfct,wkt)*Fw1*Math.pow(lmbfct,nw1+1)*(1 - Math.exp(-olft*bb*gufun(olft,lmbfct)/(Fw1*Math.pow(lmbfct,nw1)))));
    par = trgt - 1 - pard;
    if((nb4 == 0) & (par < 0)) par = 0;
    Integer ipar = new Integer(par);
    out = ipar.toString();
    return out; 
  }
  
//This function calculates a par score from overs remaining and wickets lost (uses DL contsants, adjustment factor, lambda factor)
	public int parScoreCC(double olft, int wkt, int target, int t1Score, int maxOvers, List<MatchDLRecord> firstInningInterruptions) {

		double Fw1 = 0.0;
		double nw1 = 0.0;
		int par = 0;
		int pard = 0;
		Fw1 = Fw[wkt];
		nw1 = nw[wkt];
		nb4= t1Score;
		trgt = target;
		nb3 = maxOvers;
		
		int k=0;
		    
		for(MatchDLRecord dto : firstInningInterruptions) {
		    	impty[k][0] = CommonUtility.oversToBalls(dto.getOversPlayed()+"", 6);
		    	ncs[k] = dto.getWicketsLost();
		    	dbs[k] = maxOvers - (dto.getOversLost()+dto.getOversPlayed());
		    	dbsd[k] = odbTodec(dbs[k]);
		    	dds[k] = dto.getOversLost();
		    	ddsd[k] = odbTodec(dbs[k]);
		}
		
		lmbfct = lmbFunCC(maxOvers, t1Score, impty, ncs, dbsd, ddsd);
		adjfct = adjfunCC(lmbfct, maxOvers, t1Score, firstInningInterruptions.size(), ncs, dbsd, ddsd, impty);
		pard = (int) Math.round(adjfct * z0 * Gfun(olft, lmbfct, wkt) * Fw1 * Math.pow(lmbfct, nw1 + 1)
				* (1 - Math.exp(-olft * bb * gufun(olft, lmbfct) / (Fw1 * Math.pow(lmbfct, nw1)))));
		par = trgt - 1 - pard;
		if ((nb4 == 0) & (par < 0))
			par = 0;
		
		return par;
	}

// This function calculates a percentage of resources remaining from overs remaining and wickets lost (uses DL contsants, adjustment factor, lambda factor)
  public String resrem(double olft, int wkt, double R1) {
    double Fw1, nw1, dpar, ss;
    int par, par1, par2;
    String out;
    ss = (double)nb4;
    if(nb4 == 0) ss = 1;
    Fw1 = Fw[wkt];
    nw1 = nw[wkt];
    par = (int)Math.round(adjfct*z0*Gfun(olft,lmbfct,wkt)*Fw1*Math.pow(lmbfct, nw1 + 1)*(1 - Math.exp(-olft*bb*gufun(olft,lmbfct)/(Fw1*Math.pow(lmbfct,nw1)))));
    dpar = (double)par*R1/ss;
    par1 = (int)Math.floor(dpar*100);
    par2 = (int)Math.round(dpar*10000) - par1*100;
    if(par2 > 99) {
      par2 = 0;
      par1 = par1 + 1;
    }
    if(par2 < 10) {
      out = ""+par1+".0"+par2;
    } else {
      out = ""+par1+"."+par2;
    }
    return out; 
  }

  public double resR1() {
    int r50 = (int)Math.round(adjfct*z0*Gfun(50,lmbfct,0)*Math.pow(lmbfct,nw[0]+1)*(1-Math.exp(-50*bb/(Math.pow(lmbfct,nw[0])))));
    double rused = (double)nb4/r50;
    return rused;
  }

// This function calculates the adjustment factor
  public double adjfun(double lmb) {
    double sadj, omax, ss, omx1, orem, Fw1, nw1, out;
    int ww;
    omax = (double)nb3;
    ss = (double)nb4;
    sadj = z0*Gfun(omax,lmb,0)*Math.pow(lmb,nw[0]+1)*(1 - Math.exp(-omax*bb*gufun(omax,lmb)/Math.pow(lmb,nw[0])));
    omx1 = omax;
    orem = omax;
    for(int i = 0; i < nstprow; i++) {
      if((impty[i][0] == 0) & (ncs[i+1] < 10)) {
        ww = ncs[i+1];
        Fw1 = Fw[ww];
        nw1 = nw[ww];
        orem = omx1 - dbsd[i+1];
        sadj = sadj - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
        orem = orem - ddsd[i+1];
        omx1 = omx1 - ddsd[i+1];
        sadj = sadj + (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
      }
    }
    if(nb4 == 0) ss = 1;
    out = ss/sadj;
    return out;
  }
  
  public double adjfunCC(double lmb, int maxOvers, int t1Score, 
		  int noOf1StInnInterruptions, int[] firstInnWickets, double[] firstInnoversRem, 
		  double[] firstInnoversLost, int[][] firstInnInterruptions) {
	  
	    double sadj, omax, ss, omx1, orem, Fw1, nw1, out;
	    int ww;
	    
	    nb3 = maxOvers;
	    nb4 = t1Score;
	    nstprow = noOf1StInnInterruptions;
	    impty = firstInnInterruptions;
	    ncs = firstInnWickets;
	    
	    
	    omax = (double)nb3;
	    ss = (double)nb4;
	    sadj = z0*Gfun(omax,lmb,0)*Math.pow(lmb,nw[0]+1)*(1 - Math.exp(-omax*bb*gufun(omax,lmb)/Math.pow(lmb,nw[0])));
	    omx1 = omax;
	    orem = omax;
	    for(int i = 0; i < nstprow; i++) {
	      if((impty[i][0] == 0) & (ncs[i+1] < 10)) {
	        ww = ncs[i+1];
	        Fw1 = Fw[ww];
	        nw1 = nw[ww];
	        orem = omx1 - dbsd[i+1];
	        sadj = sadj - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
	        orem = orem - ddsd[i+1];
	        omx1 = omx1 - ddsd[i+1];
	        sadj = sadj + (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
	      }
	    }
	    if(nb4 == 0) ss = 1;
	    out = ss/sadj;
	    return out;
	  }

// This function calculates the target from the adjustment and lambda factors 
//   (uses DL constants, maximum overs available for 2nd innings and overs played, 
//    wickets lost and overs lost from GUI, and penalty runs)  
  public int trgfun(double adj, double lmb) {
    double omax2, omx2, orem, Fw1, nw1;
    int R2d, ww, R2;
    omax2 = (double)ng3;
    R2d = (int)Math.round(adj*z0*Gfun(omax2,lmb,0)*Math.pow(lmb,nw[0]+1)*(1 - Math.exp(-omax2*bb*gufun(omax2,lmb)/Math.pow(lmb,nw[0]))));
    R2 = R2d;
    omx2 = omax2;
    orem = omax2;
    for(int i = 0; i < nstprow; i++) {
      if(impty[i+30][0] == 0) {
        ww = nhs[i+1];
        if(ww < 10) {
          Fw1 = Fw[ww];
          nw1 = nw[ww];
          orem = omx2 - dgsd[i+1];
          R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          R2 = R2 - R2d;
          orem = orem - disd[i+1];
          omx2 = omx2 - disd[i+1];
          R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          R2 = R2 + R2d;
        } 
      }
    }
    if((nb4 == 0) & (R2 > 0)) R2 = R2 - 1;
    R2 = R2 + 1 + ni39;
    return R2;
  }

// This function calculates the target from the adjustment and lambda factors but only using second innings info upto row lrw
//   (uses DL constants, maximum overs available for 2nd innings and overs played, 
//    wickets lost and overs lost from GUI, and penalty runs)  
  public int trgfuntst1(double adj, double lmb, int lrw) {
    double omax2, omx2, orem, Fw1, nw1;
    int R2, ww, R2d;
    omax2 = (double)ng3;
    R2d = (int)Math.round(adj*z0*Gfun(omax2,lmb,0)*Math.pow(lmb,nw[0]+1)*(1 - Math.exp(-omax2*bb*gufun(omax2,lmb)/Math.pow(lmb,nw[0]))));
    R2 = R2d;
    omx2 = omax2;
    orem = omax2;
    for(int i = 0; i < lrw; i++) {
      if(impty[i+30][0] == 0) {
        ww = nhs[i+1];
        if(ww < 10) {
          Fw1 = Fw[ww];
          nw1 = nw[ww];
          orem = omx2 - dgsd[i+1];
          R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          R2 = R2 - R2d;
          orem = orem - disd[i+1];
          omx2 = omx2 - disd[i+1];
          R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          R2 = R2 + R2d;
        } 
      }
    }
    if((nb4 == 0) & (R2 > 0)) R2 = R2 - 1;
    R2 = R2 + 1 + ni39;
    return R2;
  }
  
  public int parfuntst1(double adj, double lmb, int lrw) {
    double omax2, omx2, orem, Fw1, nw1;
    int R2, ww, R2d;
    omax2 = (double)ng3;
    R2d = (int)Math.round(adj*z0*Gfun(omax2,lmb,0)*Math.pow(lmb,nw[0]+1)*(1 - Math.exp(-omax2*bb*gufun(omax2,lmb)/Math.pow(lmb,nw[0]))));
    R2 = R2d;
    omx2 = omax2;
    orem = omax2;
    for(int i = 0; i < lrw; i++) {
      if(impty[i+30][0] == 0) {
        ww = nhs[i+1];
        if(ww < 10) {
          Fw1 = Fw[ww];
          nw1 = nw[ww];
          orem = omx2 - dgsd[i+1];
          R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          R2 = R2 - R2d;
          orem = orem - disd[i+1];
          omx2 = omx2 - disd[i+1];
          R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          R2 = R2 + R2d;
        } 
      }
    }
    if((nb4 == 0) & (R2 > 0)) R2 = R2 - 1;
    R2 = R2 - R2d + ni39;
    return R2;
  }


// This function calculates the target from the adjustment and lambda factors but only using second innings info upto row lrw
//   (uses DL constants, maximum overs available for 2nd innings and overs played, 
//    wickets lost and overs lost from GUI, and penalty runs)  BUT subtracts 1 from lrw's T2 overs lost value
  public int trgfuntst2(double adj, double lmb, int lrw) {
    double omax2, omx2, orem, Fw1, nw1;
    int R2, ww, R2d;
    omax2 = (double)ng3;
    R2d = (int)Math.round(adj*z0*Gfun(omax2,lmb,0)*Math.pow(lmb,nw[0]+1)*(1 - Math.exp(-omax2*bb*gufun(omax2,lmb)/Math.pow(lmb,nw[0]))));
    R2 = R2d;
    omx2 = omax2;
    orem = omax2;
    for(int i = 0; i < lrw - 1; i++) {
      if(impty[i+30][0] == 0) {
        ww = nhs[i+1];
        if(ww < 10) {
          Fw1 = Fw[ww];
          nw1 = nw[ww];
          orem = omx2 - dgsd[i+1];
          R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          R2 = R2 - R2d;
          orem = orem - disd[i+1];
          omx2 = omx2 - disd[i+1];
          R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          R2 = R2 + R2d;
        } 
      }
    }
    if(impty[lrw+29][0] == 0) {
      ww = nhs[lrw];
      if(ww < 10) {
        Fw1 = Fw[ww];
        nw1 = nw[ww];
        orem = omx2 - dgsd[lrw];
        R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
        R2 = R2 - R2d;
        orem = orem - Math.ceil(disd[lrw]) + 1;
        omx2 = omx2 - Math.ceil(disd[lrw]) + 1;
        R2d = (int)Math.round(adj*z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1 - Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
        R2 = R2 + R2d;
      } 
    }
    if((nb4 == 0) & (R2 > 0)) R2 = R2 - 1;
    R2 = R2 + 1 + ni39;
    return R2;
  }

// This function calculates the lambda factor for projection by assuming remaining overs are lost.
  public double lmbfunpr(double ss, int mxrow) {
    double lmb, odff, dff, smax, omax, omx1, orem, Fw1, nw1;
    int ww;
    omax = (double)nb3;
    lmb = lmbdastar;
    setFw();	
    if(nb3 > 0) {
      odff = 0.0;
      smax = z0*Gfun(omax,lmb,0)*(Math.pow(lmb,nw[0]+1))*(1-Math.exp(-omax*bb*gufun(omax,lmb)/Math.pow(lmb,nw[0])));
      dff = smax - ss;
      omx1 = omax;
      orem = omax;
      for(int i = 0; i < mxrow; i++) {
        if((impty[i][0] == 0) & (ncs[i+1] < 10)) {
          ww = ncs[i+1];
          Fw1 = Fw[ww];
          nw1 = nw[ww];
          orem = omx1 - dbsd[i+1];
          dff = dff - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          orem = orem - ddsd[i+1];
          omx1 = omx1 - ddsd[i+1];
          dff = dff + (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
        }
      }
      if((impty[mxrow][0] == 0) & (ncs[mxrow+1] < 10)) {
        ww = ncs[mxrow+1];
        Fw1 = Fw[ww];
        nw1 = nw[ww];
        orem = omx1 - dbsd[mxrow+1];
        dff = dff - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
      }
      if(dff < 0.0) {
        while((dff < 0.0) & (lmb < lmbcap)) {
          lmb = lmb + stpint;
          odff = dff;
          smax = z0*Gfun(omax,lmb,0)*(Math.pow(lmb,nw[0]+1))*(1-Math.exp(-omax*bb*gufun(omax,lmb)/Math.pow(lmb,nw[0])));
          dff = smax - ss;
          omx1 = omax;
          orem = omax;
          for(int i = 0; i < mxrow; i++) {
            if((impty[i][0] == 0) & (ncs[i+1] < 10)) {
              ww = ncs[i+1];
              Fw1 = Fw[ww];
              nw1 = nw[ww];
              orem = omx1 - dbsd[i+1];
              dff = dff - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
              orem = orem - ddsd[i+1];
              omx1 = omx1 - ddsd[i+1];
              dff = dff + (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
            }
          }
          if((impty[mxrow][0] == 0) & (ncs[mxrow+1] < 10)) {
            ww = ncs[mxrow+1];
            Fw1 = Fw[ww];
            nw1 = nw[ww];
            orem = omx1 - dbsd[mxrow+1];
            dff = dff - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          }
        }
        if(dff >= Math.abs(odff)) {
          lmb = lmb - stpint;
        }
      }
    }
    lmb = (double)Math.round(10000.0*lmb)/10000.0;
    return lmb;
  }

  public double[] rsrcprj(double lmbprj, int nmxrow) {
    double[] rsrc = new double[3];
    double omaxprj = (double)nb3;
    double r50 = z0*Gfun(50,lmbprj,0)*Math.pow(lmbprj,nw[0]+1)*(1-Math.exp(-50*bb/(Math.pow(lmbprj,nw[0]))));
    double r50u = z0u*Gfun(50,lmbu,0)*Math.pow(lmbu,nw[0]+1)*(1-Math.exp(-50*bb/(Math.pow(lmbu,nw[0]))));
    double rstrt = z0*Gfun(omaxprj,lmbprj,0)*Math.pow(lmbprj,nw[0]+1)*(1-Math.exp(-omaxprj*bb*gufun(omaxprj,lmbprj)/(Math.pow(lmbprj,nw[0]))));
    double rused = rstrt;
    double rrem = 0;
    double rremp = 0;
    double omx1p = omaxprj;
    double oremp = omaxprj;
    int wwprj = -1;
    double Fw1prj = -1;
    double nw1prj = -1;
    for(int i = 0; i < nmxrow; i++) {
      if(impty[i][0] == 0) {
        wwprj = ncs[i+1];
        Fw1prj = Fw[wwprj];
        nw1prj = nw[wwprj];
        oremp = omx1p - dbsd[i+1];
        rused = rused - (z0*Gfun(oremp,lmbprj,wwprj)*Fw1prj*Math.pow(lmbprj,nw1prj+1)*(1-Math.exp(-oremp*bb*gufun(oremp,lmbprj)/(Fw1prj*Math.pow(lmbprj,nw1prj)))));
        oremp = oremp - ddsd[i+1];
        omx1p = omx1p - ddsd[i+1];
        rused = rused + (z0*Gfun(oremp,lmbprj,wwprj)*Fw1prj*Math.pow(lmbprj,nw1prj+1)*(1-Math.exp(-oremp*bb*gufun(oremp,lmbprj)/(Fw1prj*Math.pow(lmbprj,nw1prj)))));
      }
    }
    wwprj = ncs[nmxrow+1];
    Fw1prj = Fw[wwprj];
    nw1prj = nw[wwprj];
    oremp = omx1p - dbsd[nmxrow+1];
    rused = rused - (z0*Gfun(oremp,lmbprj,wwprj)*Fw1prj*Math.pow(lmbprj,nw1prj+1)*(1-Math.exp(-oremp*bb*gufun(oremp,lmbprj)/(Fw1prj*Math.pow(lmbprj,nw1prj)))));
    if(impty[nmxrow][2] == 0) {
      oremp = oremp - ddsd[nmxrow+1];
    }
    rused = rused/r50;
    rrem = (z0*Gfun(oremp,lmbprj,wwprj)*Fw1prj*Math.pow(lmbprj,nw1prj+1)*(1-Math.exp(-oremp*bb*gufun(oremp,lmbprj)/(Fw1prj*Math.pow(lmbprj,nw1prj)))))/r50;
    rremp = (z0u*Gfun(oremp,lmbu,wwprj)*Fw1prj*Math.pow(lmbu,nw1prj+1)*(1-Math.exp(-oremp*bb*gufun(oremp,lmbu)/(Fw1prj*Math.pow(lmbu,nw1prj)))))/r50u;
    rsrc[0] = rused;
    rsrc[1] = rrem;
    rsrc[2] = rremp;
    return rsrc;
  }

  public double Gfun(double ovr1, double lmbda, int ww) {
    double sll, Fwi, nwi, b1, bm, b2, uua, sll50, gw, l350s, l450s, l600s;
    double pG50u, p350u, p450u, p600u, pendu, sllp, outp, ovr;
    int novr, nbll, nblls, uui;
    Fwi = Fw[ww];
    nwi = nw[ww];
    if((ww == 9) & (ovr1 > 5.0)) {
      ovr = 5.0;
    } else if((ww==8) & (ovr1 >= 14.0) & (ovr1 < 27.5)) {
      ovr = 14.0;
    } else {
      ovr = ovr1;
    }
    novr = (int)Math.floor(ovr);
    nbll = (int)Math.round(6*(ovr - Math.floor(ovr)));
    nblls = (6*novr) + nbll;
    if(nblls == 1) {
      uui = 1;
    } else {
      uui = (int)Math.ceil(ovr/5.0)+1;
    }
    b1 = z0*(1-Math.exp(-((uui-1)*5)*bb));
    bm = z0*(1-Math.exp(-ovr*bb));
    if(uui > 2) {
      b2 = z0*(1-Math.exp(-((uui-2)*5)*bb));
    } else {
      b2 = z0*(1-Math.exp(-(1.0/6.0)*bb));
    }
    if(uui > 1) {
      uua = (bm-b2)/(b1-b2);
    } else {
      uua = 1.0;
    }
    sll = z0*Fwi*Math.pow(lmbda,nwi+1)*(1-Math.exp(-ovr1*bb*gufun(ovr1,lmbda)/(Fwi*Math.pow(lmbda,nwi))));
    sll50 = z0*Math.pow(lmbda,nw[0]+1)*(1-Math.exp(-50*bb/Math.pow(lmbda,nw[0])));
    gw = Fwi*(1-Math.exp(-ovr*bb/Fwi))/(1-Math.exp(-ovr*bb));
    l350s = z0*Math.pow(lmb350,nw[0]+1)*(1-Math.exp(-50*bb/Math.pow(lmb350,nw[0])));
    l450s = z0*Math.pow(lmb450,nw[0]+1)*(1-Math.exp(-50*bb/Math.pow(lmb450,nw[0])));
    l600s = z0*Math.pow(lmb600,nw[0]+1)*(1-Math.exp(-50*bb/Math.pow(lmb600,nw[0])));
    pG50u = z0*(1-Math.exp(-ovr*bb));
    if(uui == 1) {
      p350u = p350anchor[uui-1];
      p450u = p450anchor[uui-1];
      p600u = p600anchor[uui-1];
    } else if(uui < 12) {
      p350u = p350anchor[uui-1]*uua + p350anchor[uui-2]*(1.0-uua);
      p450u = p450anchor[uui-1]*uua + p450anchor[uui-2]*(1.0-uua);
      p600u = p600anchor[uui-1]*uua + p600anchor[uui-2]*(1.0-uua);
    } else {
      p350u = p350anchor[10] + ((p350anchor[10]-p350anchor[9])/5.0)*(ovr-50.0);
      p450u = p450anchor[10] + ((p450anchor[10]-p450anchor[9])/5.0)*(ovr-50.0);
      p600u = p600anchor[10] + ((p600anchor[10]-p600anchor[9])/5.0)*(ovr-50.0);
    }
    pendu = Scap*(ovr/50.0);
    if(p350u < pG50u) p350u = pG50u;
    if(p450u < p350u) p450u = p350u;
    if(p600u < p450u) p600u = p450u;
    if(pendu < p600u) pendu = p600u;
    if((ovr > 0.0) & (lmbda > 1.0)) {
      if(lmbda < lmb350) {
        sllp = (pG50u*((l350s-sll50)/(l350s-g50)) + p350u*((sll50-g50)/(l350s-g50)))*gw;
      } else if(lmbda < lmb450) {
        sllp = (p350u*((l450s-sll50)/(l450s-l350s)) + p450u*((sll50-l350s)/(l450s-l350s)))*gw;
      } else if(lmbda < lmb600) {
        sllp = (p450u*((l600s-sll50)/(l600s-l450s)) + p600u*((sll50-l450s)/(l600s-l450s)))*gw;
      } else if(lmbda < lmbcap1) {
        sllp = (p600u*((Scap-sll50)/(Scap-l600s)) + pendu*((sll50-l600s)/(Scap-l600s)))*gw;
      } else {
        sllp = sll50*(ovr/50.0)*gw;
      }
      outp = sllp/sll;
    } else {
      outp = 1.0;
    }
    return outp;
  }

  public double gufun(double ovr, double lmbda) {
    double val, val1, val2, g350a, g450a, g600a, g350b, g450b, g600b, g350c, g450c, g600c;
    int novr, nbll, nblls;
    g350a = 1.402;
    g350b = -0.02628;
    g350c = 0.0003901;
    g450a = 1.418;
    g450b = -0.02405;
    g450c = 0.0003423;
    g600a = 1.305;
    g600b = -0.01546;
    g600c = 0.0002000;
    if((ovr > 0.0) & (lmbda > 1.0)) {
      if(lmbda < lmb350) {
        val1 = ((lmb350 - lmbda)/(lmb350 - 1.0)) + (g350a + g350b*ovr + g350c*ovr*ovr)*((lmbda - 1.0)/(lmb350 - 1.0));
        val2 = ((lmb350 - lmbda)/(lmb350 - 1.0)) + (g350a + g350b*50.0 + g350c*2500.0)*((lmbda - 1.0)/(lmb350 - 1.0));
        val = val1/val2;
      } else if(lmbda < lmb450) {
        val1 = (g350a + g350b*ovr + g350c*ovr*ovr)*((lmb450 - lmbda)/(lmb450 - lmb350)) + (g450a + g450b*ovr + g450c*ovr*ovr)*((lmbda - lmb350)/(lmb450 - lmb350));
        val2 = (g350a + g350b*50.0 + g350c*2500.0)*((lmb450 - lmbda)/(lmb450 - lmb350)) + (g450a + g450b*50.0 + g450c*2500.0)*((lmbda - lmb350)/(lmb450 - lmb350));
        val = val1/val2;
      } else if(lmbda < lmb600) {
        val1 = (g450a + g450b*ovr + g450c*ovr*ovr)*((lmb600 - lmbda)/(lmb600 - lmb450)) + (g600a + g600b*ovr + g600c*ovr*ovr)*((lmbda - lmb450)/(lmb600 - lmb450));
        val2 = (g450a + g450b*50.0 + g450c*2500.0)*((lmb600 - lmbda)/(lmb600 - lmb450)) + (g600a + g600b*50.0 + g600c*2500.0)*((lmbda - lmb450)/(lmb600 - lmb450));
        val = val1/val2;
      } else if(lmbda < lmbcap1) {
        val1 = (g600a + g600b*ovr + g600c*ovr*ovr)*((lmbcap1 - lmbda)/(lmbcap1 - lmb600)) + ((lmbda - lmb600)/(lmbcap1 - lmb600));
        val2 = (g600a + g600b*50.0 + g600c*2500.0)*((lmbcap1 - lmbda)/(lmbcap1 - lmb600)) + ((lmbda - lmb600)/(lmbcap1 - lmb600));
        val = val1/val2;
      } else {
        val = 1.0;
      }
    } else {
      val = 1.0;
    }
    return val;
  }

  public double lmbfun() {
    double lmb, odff, dff, smax, omax, ss, omx1, orem, Fw1, nw1;
    int ww;
    omax = (double)nb3;
    ss = (double)nb4;
    lmb = lmbdastar;
    if(nb3 > 0) {
      odff = 0.0;
      smax = z0*Gfun(omax,lmb,0)*(Math.pow(lmb,nw[0]+1))*(1-Math.exp(-omax*bb*gufun(omax,lmb)/Math.pow(lmb,nw[0])));
      dff = smax - ss;
      omx1 = omax;
      orem = omax;
      for(int i = 0; i < nstprow; i++) {
        if((impty[i][0] == 0) & (ncs[i+1] < 10)) {
          ww = ncs[i+1];
          Fw1 = Fw[ww];
          nw1 = nw[ww];
          orem = omx1 - dbsd[i+1];
          dff = dff - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
          orem = orem - ddsd[i+1];
          omx1 = omx1 - ddsd[i+1];
          dff = dff + (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
        }
      }
      if(dff < 0.0) {
        while((dff < 0.0) & (lmb < lmbcap)) {
          lmb = lmb + stpint;
          odff = dff;
          smax = z0*Gfun(omax,lmb,0)*(Math.pow(lmb,nw[0]+1))*(1-Math.exp(-omax*bb*gufun(omax,lmb)/Math.pow(lmb,nw[0])));
          dff = smax - ss;
          omx1 = omax;
          orem = omax;
          for(int i = 0; i < nstprow; i++) {
            if((impty[i][0] == 0) & (ncs[i+1] < 10)) {
              ww = ncs[i+1];
              Fw1 = Fw[ww];
              nw1 = nw[ww];
              orem = omx1 - dbsd[i+1];
              dff = dff - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
              orem = orem - ddsd[i+1];
              omx1 = omx1 - ddsd[i+1];
              dff = dff + (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
            }
          }
        }
        if(dff >= Math.abs(odff)) {
          lmb = lmb - stpint;
        }
      }
    }
    lmb = (double)Math.round(10000.0*lmb)/10000.0;
    return lmb;
  }
  
  public double lmbFunCC(int maxOvers, int t1Score, int[][] intrRecords, int[] wicketsLost, double[] oversRemaining, double[] oversLast) {
	  
	    double lmb, odff, dff, smax, omax, ss, omx1, orem, Fw1, nw1;
	    int ww;
	    nb3 = maxOvers;
	    t1Score = nb4;
	    
	    ncs = wicketsLost;
    	dbsd = oversRemaining;
    	ddsd = oversLast;
    	impty = intrRecords;
	    
	    omax = (double)nb3;
	    ss = (double)nb4;
	    lmb = lmbdastar;
	    if(nb3 > 0) {
	      odff = 0.0;
	      smax = z0*Gfun(omax,lmb,0)*(Math.pow(lmb,nw[0]+1))*(1-Math.exp(-omax*bb*gufun(omax,lmb)/Math.pow(lmb,nw[0])));
	      dff = smax - ss;
	      omx1 = omax;
	      orem = omax;
	      
	      for(int i = 0; i < nstprow; i++) {
	        if((impty[i][0] == 0) & (ncs[i+1] < 10)) {
	          ww = ncs[i+1];
	          Fw1 = Fw[ww];
	          nw1 = nw[ww];
	          orem = omx1 - dbsd[i+1];
	          dff = dff - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
	          orem = orem - ddsd[i+1];
	          omx1 = omx1 - ddsd[i+1];
	          dff = dff + (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
	        }
	      }
	      if(dff < 0.0) {
	        while((dff < 0.0) & (lmb < lmbcap)) {
	          lmb = lmb + stpint;
	          odff = dff;
	          smax = z0*Gfun(omax,lmb,0)*(Math.pow(lmb,nw[0]+1))*(1-Math.exp(-omax*bb*gufun(omax,lmb)/Math.pow(lmb,nw[0])));
	          dff = smax - ss;
	          omx1 = omax;
	          orem = omax;
	          for(int i = 0; i < nstprow; i++) {
	            if((impty[i][0] == 0) & (ncs[i+1] < 10)) {
	              ww = ncs[i+1];
	              Fw1 = Fw[ww];
	              nw1 = nw[ww];
	              orem = omx1 - dbsd[i+1];
	              dff = dff - (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
	              orem = orem - ddsd[i+1];
	              omx1 = omx1 - ddsd[i+1];
	              dff = dff + (z0*Gfun(orem,lmb,ww)*Fw1*Math.pow(lmb,nw1+1)*(1-Math.exp(-orem*bb*gufun(orem,lmb)/(Fw1*Math.pow(lmb,nw1)))));
	            }
	          }
	        }
	        if(dff >= Math.abs(odff)) {
	          lmb = lmb - stpint;
	        }
	      }
	    }
	    lmb = (double)Math.round(10000.0*lmb)/10000.0;
	    return lmb;
	  }
  
// This function sets the Fw and nw values from the given pw D/L constants.  
  public void setFw() {
    double[] pw = new double[11];
    pw[0] = pw11[0];
    pw[1] = pw11[1];
    pw[2] = pw11[2];
    pw[3] = pw11[3];
    pw[4] = pw11[4];
    pw[5] = pw11[5];
    pw[6] = pw11[6];
    pw[7] = pw11[7];
    pw[8] = pw11[8];
    pw[9] = pw11[9];
    pw[10] = 1.0;
    for(int i = 0; i < 10; i++) {
      pw[10] = pw[10] - pw[i];
    }
    pw[10] = ((int)Math.round(1000*pw[10]))/1000.0;
    Fw[0] = 1.0;
    nw[0] = 10;
    for(int i = 1; i < 10; i++) {
      Fw[i] = Fw[i-1] - pw[i];
      nw[i] = nw[0]*Fw[i];
    }
  }

}
