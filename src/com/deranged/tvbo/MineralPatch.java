 package com.deranged.tvbo;
 
 import java.io.PrintStream;
 
 public class MineralPatch extends Resource
 {
   private int distance;
   private Mule mule;
 
   public MineralPatch(int r, int d)
   {
     super(r);
     this.scvsMining = new SCV[this.max];
     this.distance = d;
   }
 
   public void update()
   {
     super.update();
     if (this.mule != null) {
       this.mule.update();
       if (this.mule.getLife() == 0)
         this.mule = null;
     }
   }
 
   public boolean addSCV(SCV s)
   {
     if (this.scvs < this.max) {
       if (this.scvsMining[this.scvs] == null)
       {
         s.setResource(this);
         this.scvsMining[this.scvs] = s;
         this.scvs += 1;
         setJobtimes();
         return true;
       }
       return true;
     }
 
     return false;
   }
 
   public void setJobtimes()
   {
     double n = 0.0D;
     if (this.distance == 1) {
       if (this.scvs == 1)
         n = 6.666666666666667D;
       else if (this.scvs == 2)
         n = 6.666666666666667D;
       else if (this.scvs == 3)
         n = 8.709677419354838D;
       else
         System.out.println("Error: <MineralPatch:setJobtimes> Too many scvs on this patch!");
     }
     else if (this.distance == 2) {
       if (this.scvs == 1)
         n = 7.5D;
       else if (this.scvs == 2)
         n = 7.5D;
       else if (this.scvs == 3)
         n = 8.709677419354838D;
       else
         System.out.println("Error: <MineralPatch:setJobtimes> Too many scvs on this patch!");
     }
     else if (this.distance == 3) {
       if (this.scvs == 1)
         n = 7.659574468085107D;
       else if (this.scvs == 2)
         n = 7.659574468085107D;
       else if (this.scvs == 3)
         n = 8.709677419354838D;
       else
         System.out.println("Error: <MineralPatch:setJobtimes> Too many scvs on this patch!");
     }
     else {
       System.out.println("Error: <MineralPatch:setJobtimes> Invalid distance parameter");
     }
     for (int i = 0; i < this.max; i++)
       if (this.scvsMining[i] != null)
         this.scvsMining[i].setJobtime(n, 0);
   }
 
   public boolean hasMule()
   {
     return (this.mule != null) && (this.mule.getLife() > 0);
   }
 
   public void addMule(Model model)
   {
     this.mule = new Mule(model);
     this.mule.setResource(this);
     if (this.distance == 1)
       this.mule.setJobtime(9.0D, 0);
     else if (this.distance == 2)
       this.mule.setJobtime(9.5D, 0);
     else if (this.distance == 3)
       this.mule.setJobtime(10.0D, 0);
   }
 }
