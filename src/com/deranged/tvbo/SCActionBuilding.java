 package com.deranged.tvbo;
 
 import java.io.PrintStream;
 import java.util.ArrayList;
 
 public class SCActionBuilding extends SCAction
 {
   public SCActionBuilding(Model model, int startTime, int y, String name)
   {
     super(model, startTime, y, model.getTime(name), name);
     this.preactionTime = 3;
     this.preactionComplete = false;
     if ((name.equals("Barracks")) || (name.equals("Factory")) || (name.equals("Starport"))) {
       this.options.add("TechLab");
       this.options.add("Reactor");
       this.options.add("none");
     }
     this.option = "none";
   }
   public SCActionBuilding(Model model, int startTime, int y, String name, String option) {
     super(model, startTime, y, model.getTime(name), name);
     this.option = option;
     this.preactionTime = 3;
     this.preactionComplete = false;
     if ((name.equals("Barracks")) || (name.equals("Factory")) || (name.equals("Starport"))) {
       this.options.add("TechLab");
       this.options.add("Reactor");
       this.options.add("none");
     }
   }
 
   public boolean execute() {
     boolean f = true;
     String prereq = this.model.getPrereq(this.name);
     if (this.complete) {
       f = false;
     } else if (!this.preactionComplete) {
       f = false;
       this.errorMsg = "SCV";
     } else if ((prereq != null) && (!this.model.isObjectComplete(prereq))) {
       f = false;
       this.errorMsg = "PREREQ";
     } else if ((!this.option.equals("none")) && (!this.model.freeAddonExists(this.option))) {
       f = false;
       this.errorMsg = "ADDON";
     } else if (this.model.getMinerals() < this.model.getMineralCost(this.name)) {
       f = false;
       this.errorMsg = "MINERALS";
     } else if (this.model.getGas() < this.model.getGasCost(this.name)) {
       f = false;
       this.errorMsg = "GAS";
     } else if (!this.model.makeBuilding(this.name, this.option)) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 
   public boolean preaction() {
     boolean p = true;
     if (!this.model.setSCVBuilding(2 * this.preactionTime + this.duration)) {
       p = false;
     }
     if (p)
     {
       this.preactionComplete = true;
     }
     return p;
   }
 
   public void setSupplyPoint(String supplyPoint)
   {
     if (this.option == null) {
       System.out.println("<SCActionBuilding> option is null");
     }
     else if (this.option.equals("none"))
     {
       this.supplyPoint = supplyPoint;
     }
     else this.supplyPoint = (supplyPoint + "  on " + this.option);
   }
 }
