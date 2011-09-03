 package com.deranged.tvbo;
 
 public class SCActionResearch extends SCAction
 {
   public SCActionResearch(Model model, int startTime, int y, String name)
   {
     super(model, startTime, y, model.getTime(name), name);
   }
 
   public boolean execute() {
     boolean f = true;
     String prereq = this.model.getPrereq(this.name);
     String build = this.model.getBuild(this.name);
     String tech = this.model.getTech(this.name);
 
     boolean pre = true;
     if (this.name.matches(".*Level.*")) {
       int level = Integer.parseInt(this.name.substring(this.name.length() - 1, this.name.length()));
 
       if (level > 1) {
         String s = this.name.substring(0, this.name.length() - 1) + (level - 1);
 
         if (this.model.isObjectComplete(s))
           pre = true;
         else {
           pre = false;
         }
       }
     }
 
     if (this.complete) {
       f = false;
     } else if (this.model.alreadyStarted(this.name)) {
       f = false;
       this.errorMsg = "ALREADY";
     } else if (!pre) {
       f = false;
       this.errorMsg = "PREREQ";
     } else if ((prereq != null) && (!this.model.isObjectComplete(prereq))) {
       f = false;
       this.errorMsg = "PREREQ";
     } else if ((build != null) && (!this.model.isObjectComplete(build))) {
       f = false;
       this.errorMsg = "BUILD";
     } else if ((tech != null) && (!this.model.hasAddon(build, tech))) {
       f = false;
       this.errorMsg = "TECHLAB";
     } else if ((tech != null) && (!this.model.isAvailable(tech))) {
       f = false;
       this.errorMsg = "QUEUE";
     } else if ((tech == null) && (!this.model.isAvailable(build))) {
       f = false;
       this.errorMsg = "QUEUE";
     } else if (this.model.getMinerals() < this.model.getMineralCost(this.name)) {
       f = false;
       this.errorMsg = "MINERALS";
     } else if (this.model.getGas() < this.model.getGasCost(this.name)) {
       f = false;
       this.errorMsg = "GAS";
     } else if (!this.model.addResearch(this.name)) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 }
