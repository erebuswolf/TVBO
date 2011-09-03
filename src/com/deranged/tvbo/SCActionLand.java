 package com.deranged.tvbo;
 
 import java.util.ArrayList;
 
 public class SCActionLand extends SCAction
 {
   public SCActionLand(Model model, int startTime, int y, String name)
   {
     super(model, startTime, y, 30, name);
     this.option = "";
     this.options.add("TechLab");
     this.options.add("Reactor");
     this.options.add("none");
   }
 
   public SCActionLand(Model model, int startTime, int y, String name, String option) {
     super(model, startTime, y, 30, name);
     this.option = option;
     this.options.add("TechLab");
     this.options.add("Reactor");
     this.options.add("none");
   }
 
   public boolean execute() {
     boolean f = true;
 
     if (this.complete) {
       f = false;
     } else if (this.option.equals("")) {
       f = false;
       this.errorMsg = "SET OPTION";
     } else if ((!this.option.equals("none")) && (!this.model.freeAddonExists(this.option))) {
       f = false;
       this.errorMsg = "NO ADDON";
     } else if (!this.model.land(this.name, this.option)) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 
   public void setSupplyPoint(String supplyPoint)
   {
     this.supplyPoint = this.option;
   }
 
   public String toString()
   {
     return "Land" + this.name;
   }
 }
