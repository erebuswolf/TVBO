 package com.deranged.tvbo;
 
 import java.io.PrintStream;
 
 public class VespeneGeyser extends Resource
 {
   private SCStructure refinery;
 
   public VespeneGeyser(int r)
   {
     super(r);
     this.scvs = 0;
   }
 
   public void update()
   {
     super.update();
     if (this.refinery != null)
       this.refinery.update();
   }
 
   public void addRefinery(Model model)
   {
     this.refinery = new SCStructure(model, "Refinery");
   }
 
   public boolean hasRefinery()
   {
     return this.refinery != null;
   }
 
   public boolean hasCompleteRefinery()
   {
     if (this.refinery == null) {
       return false;
     }
 
     return this.refinery.isComplete();
   }
 
   public boolean addSCV(SCV s)
   {
     if (this.scvs < this.max) {
       if (this.scvsMining[this.scvs] == null) {
         s.setResource(this);
         this.scvsMining[this.scvs] = s;
         this.scvs += 1;
         setJobtimes(1);
         return true;
       }
       return true;
     }
 
     System.out.println("<VespeneGeyser> Too many SCVs already here");
     return false;
   }
 
   public void setJobtimes(int job)
   {
     double n = 0.0D;
     if (this.scvs == 1)
       n = 5.581395348837209D;
     else if (this.scvs == 2)
       n = 5.581395348837209D;
     else if (this.scvs == 3)
       n = 6.288209606986899D;
     else {
       System.out.println("Error: <VespeneGeyser:setJobtimes> Too many scvs on this geyser!");
     }
     for (int i = 0; i < 3; i++)
       if (this.scvsMining[i] != null)
         this.scvsMining[i].setJobtime(n, job);
   }
 }
