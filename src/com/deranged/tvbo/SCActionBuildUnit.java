 package com.deranged.tvbo;
 
 public class SCActionBuildUnit extends SCAction
 {
   public SCActionBuildUnit(Model model, int startTime, int y, String name)
   {
     super(model, startTime, y, model.getTime(name), name);
   }
 
   public boolean execute() {
     boolean f = true;
     String prereq = this.model.getPrereq(this.name);
     String build = this.model.getBuild(this.name);
     String tech = this.model.getTech(this.name);
 
     if (this.complete) {
       f = false;
     } else if ((prereq != null) && (!this.model.isObjectComplete(prereq))) {
       f = false;
       this.errorMsg = "PREREQ";
     } else if ((prereq != null) && (!this.model.isObjectComplete(build))) {
       f = false;
       this.errorMsg = "BUILD";
     } else if ((tech != null) && (!this.model.hasAddon(build, tech))) {
       f = false;
       this.errorMsg = "TECHLAB";
     } else if (this.model.getMinerals() < this.model.getMineralCost(this.name)) {
       f = false;
       this.errorMsg = "MINERALS";
     } else if (!this.model.isAvailable(build)) {
       f = false;
       this.errorMsg = "QUEUE";
     } else if (this.model.getGas() < this.model.getGasCost(this.name)) {
       f = false;
       this.errorMsg = "GAS";
     } else if ((this.model.getFood(this.name) > 0) && (this.model.getFood() + this.model.getFood(this.name) > this.model.getSupply())) {
       f = false;
       this.errorMsg = "SUPPLY BLOCKED";
     } else if (!this.model.addUnitToQueue(this.name)) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 }
