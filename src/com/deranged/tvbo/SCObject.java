 package com.deranged.tvbo;
 
 import java.io.PrintStream;
 
 public class SCObject
 {
   protected Model model;
   protected String name;
   protected boolean complete;
   protected int progress;
   protected int buildtime;
 
   public SCObject(Model model, String name)
   {
     this.model = model;
     this.name = name;
     this.buildtime = model.getTime(name);
     this.progress = 0;
     this.complete = false;
   }
 
   public boolean isComplete()
   {
     return this.complete;
   }
 
   public void update()
   {
     if (!this.complete) {
       if (this.progress < this.buildtime) {
         this.progress += 1;
       }
       if (this.progress >= this.buildtime) {
         this.complete = true;
         this.progress = 0;
       }
     }
   }
 
   public void complete()
   {
     this.complete = true;
   }
 
   public int getProgress() {
     return this.progress;
   }
 
   public void setProgress(int progress) {
     this.progress = progress;
   }
 
   public boolean isAvailable() {
     return this.complete;
   }
 
   public int getBuildtime()
   {
     return this.buildtime;
   }
 
   public void setBuildtime(int buildtime) {
     this.buildtime = buildtime;
   }
 
   public String toString() {
     return this.name;
   }
 
   public String getName() {
     return this.name;
   }
 
   public void setName(String name) {
     this.name = name;
   }
 
   public boolean lift() {
     if ((this.name.equals("Barracks")) || (this.name.equals("Factory")) || (this.name.equals("Starport"))) {
       return true;
     }
     System.out.println(this.model.printTime() + "   <SCObject> Cannot lift " + this.name);
     return false;
   }
 
   public boolean land(String addon)
   {
     if ((this.name.equals("Barracks")) || (this.name.equals("Factory")) || (this.name.equals("Starport"))) {
       return true;
     }
     System.out.println(this.model.printTime() + "   <SCObject> Cannot land " + this.name);
     return false;
   }
 
   public boolean detach()
   {
     if ((this.name.equals("TechLab")) || (this.name.equals("Reactor"))) {
       return true;
     }
     System.out.println(this.model.printTime() + "   <SCObject> Cannot detach " + this.name);
     return false;
   }
 
   public boolean attach(String building)
   {
     if ((this.name.equals("TechLab")) || (this.name.equals("Reactor"))) {
       return true;
     }
     System.out.println(this.model.printTime() + "   <SCObject> Cannot detach " + this.name);
     return false;
   }
 }
