 package com.deranged.tvbo;
 
 public class SCActionBuildRefinery extends SCAction
 {
   public SCActionBuildRefinery(Model model, int startTime, int y)
   {
     super(model, startTime, y, model.getTime("Refinery"), "Refinery");
     this.preactionTime = 1;
     this.preactionComplete = false;
   }
 
   public boolean execute() {
     boolean f = true;
     if (this.complete) {
       f = false;
     } else if (this.model.freeGeysers() == 0) {
       f = false;
       this.errorMsg = "NO GEYSERS";
     } else if (this.model.getMinerals() < this.model.getMineralCost("Refinery")) {
       f = false;
       this.errorMsg = "MINERALS";
     } else if (!this.model.buildRefinery()) {
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
     if (!this.model.setSCVBuilding(this.preactionTime + this.duration)) {
       p = false;
     }
     if (p)
     {
       this.preactionComplete = true;
     }
     return p;
   }
 }

/* Location:           C:\Users\Jesse\Desktop\TVBO.jar
 * Qualified Name:     com.deranged.tvbo.SCActionBuildRefinery
 * JD-Core Version:    0.6.0
 */