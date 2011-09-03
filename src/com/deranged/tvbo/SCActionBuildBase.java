 package com.deranged.tvbo;
 
 public class SCActionBuildBase extends SCAction
 {
   public SCActionBuildBase(Model model, int startTime, int y)
   {
     super(model, startTime, y, model.getTime("CommandCenter"), "CommandCenter");
     this.preactionTime = 10;
     this.preactionComplete = false;
   }
 
   public boolean execute() {
     boolean f = true;
     String prereq = this.model.getPrereq(this.name);
     if (this.complete) {
       f = false;
     } else if (!this.preactionComplete) {
       f = false;
       this.errorMsg = "SCV";
     } else if (this.model.getMinerals() < this.model.getMineralCost(this.name)) {
       f = false;
       this.errorMsg = "MINERALS";
     } else if (this.model.getGas() < this.model.getGasCost(this.name)) {
       f = false;
       this.errorMsg = "GAS";
     } else if (!this.model.buildBase()) {
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
 }
