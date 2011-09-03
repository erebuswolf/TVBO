 package com.deranged.tvbo;
 
 public class SCActionBuildSCV extends SCAction
 {
   public SCActionBuildSCV(Model model, int startTime, int y)
   {
     super(model, startTime, y, model.getTime("SCV"), "SCV");
   }
 
   public boolean execute() {
     boolean f = true;
 
     if (this.complete) {
       f = false;
     } else if (this.model.getMinerals() < this.model.getMineralCost(this.name)) {
       f = false;
       this.errorMsg = "MINERALS";
     } else if ((this.model.getFood(this.name) > 0) && (this.model.getFood() + this.model.getFood(this.name) > this.model.getSupply())) {
       f = false;
       this.errorMsg = "SUPPLY BLOCKED";
     } else if (!this.model.isFreeBase()) {
       f = false;
       this.errorMsg = "QUEUE";
     } else if (!this.model.buildSCV()) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 }
