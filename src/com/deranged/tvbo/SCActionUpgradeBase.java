 package com.deranged.tvbo;
 
 public class SCActionUpgradeBase extends SCAction
 {
   public SCActionUpgradeBase(Model model, int startTime, int y, String name)
   {
     super(model, startTime, y, 35, name);
   }
 
   public boolean execute() {
     boolean f = true;
     String prereq = this.model.getPrereq(this.name);
     if (this.complete) {
       f = false;
     } else if (this.model.getMinerals() < this.model.getMineralCost(this.name)) {
       f = false;
       this.errorMsg = "MINERALS";
     } else if (this.model.getGas() < this.model.getGasCost(this.name)) {
       f = false;
       this.errorMsg = "GAS";
     } else if (!this.model.isObjectComplete(prereq)) {
       f = false;
       this.errorMsg = "PREREQ";
     } else if (!this.model.isFreeBase()) {
       f = false;
       this.errorMsg = "BASE";
     } else if (!this.model.upgradeBase(this.name)) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 }
