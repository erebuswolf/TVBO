 package com.deranged.tvbo;
 
 import java.io.PrintStream;
 
 public class Mule extends SCObject
 {
   private Resource resource;
   private int life;
   private int job;
   private double jobtime;
   private double JOBTIME;
 
   public Mule(Model model)
   {
     super(model, "Mule");
     this.complete = true;
     this.life = 90;
     this.job = 0;
   }
 
   public void update()
   {
     if ((this.job == 0) && (this.life > 0) && 
       (this.jobtime <= 0.0D)) {
       this.jobtime += this.JOBTIME;
       if (this.resource.reduceResource(30)) {
         this.model.addMinerals(30);
       } else {
         System.out.println(this.model.printTime() + "   <Mule> Mineral patch depleted");
         this.job = 3;
       }
     }
 
     if (this.jobtime > 0.0D) {
       this.jobtime -= 1.0D;
     }
     if (this.life > 0)
       this.life -= 1;
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
 
   public int getLife() {
     return this.life;
   }
 
   public int getJob() {
     return this.job;
   }
 
   public void setJob(int job) {
     this.job = job;
   }
 
   public void setResource(Resource r) {
     this.resource = r;
   }
 }
