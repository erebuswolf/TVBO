 package com.deranged.tvbo;
 
 import java.io.PrintStream;
 
 public class SCV extends SCObject
 {
   private int job;
   private double jobtime;
   private double JOBTIME;
   private Resource resource;
 
   public SCV(Model model)
   {
     super(model, "SCV");
     this.job = 3;
     this.jobtime = 0.0D;
     this.JOBTIME = 0.0D;
   }
 
   public void update() {
     super.update();
     if (this.complete) {
       if (this.job == 0) {
         if (this.jobtime <= 0.0D) {
           this.jobtime += this.JOBTIME;
           if (this.resource.reduceResource(5)) {
             this.model.addMinerals(5);
           } else {
             System.out.println(this.model.printTime() + " Mineral patch depleted");
             this.job = 3;
           }
         }
       } else if (this.job == 1) {
         if (this.jobtime <= 0.0D) {
           this.jobtime += this.JOBTIME;
           if (this.resource.reduceResource(4)) {
             this.model.addGas(4);
           } else {
             System.out.println(this.model.printTime() + " Vespene Geyser depleted");
             this.job = 3;
           }
         }
       } else if ((this.job == 2) && 
         (this.jobtime <= 0.0D)) {
         this.job = 0;
       }
 
       if (this.jobtime > 0.0D)
         this.jobtime -= 1.0D;
     }
   }
 
   public void setJobtime(double time, int job)
   {
     if (this.job == job) {
       this.JOBTIME = time;
     } else {
       this.job = job;
       this.jobtime = time;
       this.JOBTIME = time;
     }
   }
 
   public int getJob()
   {
     return this.job;
   }
 
   public void setJob(int job) {
     this.job = job;
   }
 
   public void setResource(Resource r) {
     this.resource = r;
   }
 }
