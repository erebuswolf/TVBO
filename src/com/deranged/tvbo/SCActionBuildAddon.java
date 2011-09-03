 package com.deranged.tvbo;
 
 public class SCActionBuildAddon extends SCAction
 {
   public SCActionBuildAddon(Model model, int startTime, int y, String name)
   {
     super(model, startTime, y, model.getTime(name), name);
     if(name.equals("Nuke")){
    	 this.option = "GhostAcademy";
         this.options.add("GhostAcademy");
         return;
     }
     this.option = "";
     this.options.add("Barracks");
     this.options.add("Factory");
     this.options.add("Starport");
   }
   public SCActionBuildAddon(Model model, int startTime, int y, String name, String option) {
     super(model, startTime, y, model.getTime(name), name);
     if(name.equals("Nuke")){
    	 this.option = "GhostAcademy";
         this.options.add("GhostAcademy");
         return;
     }
     setOption(option);
     this.options.add("Barracks");
     this.options.add("Factory");
     this.options.add("Starport");
   }
 
   public boolean execute() {
     boolean f = true;

     
     if(name.equals("Nuke")){
    	 String prereq = this.model.getPrereq(this.name);
    	 if ((prereq != null) && (!this.model.isObjectComplete(prereq))) {
    	       f = false;
    	       this.errorMsg = "PREREQ";
    	       return f;
    	 }
     }
     if (this.complete) {
       f = false;
     } else if (this.option.equals("")) {
       f = false;
       this.errorMsg = "SET OPTION";
     } else if (this.model.getMinerals() < this.model.getMineralCost(this.name)) {
       f = false;
       this.errorMsg = "MINERALS";
     } else if (this.model.getGas() < this.model.getGasCost(this.name)) {
       f = false;
       this.errorMsg = "GAS";
     } else if (!this.model.isObjectComplete(this.option)) {
       f = false;
       this.errorMsg = "QUEUE";
     } else if (!this.model.isAvailable(this.option)) {
       f = false;
       this.errorMsg = "QUEUE";
     } else if (!this.model.buildAddon(this.name, this.option)) {
       f = false;
       this.errorMsg = "NO EMPTY "+this.option;
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 
   public void setOption(int i) {
     this.option = ((String)this.options.get(i));
 
     this.model.reset();
     this.model.play();
   }
 }
