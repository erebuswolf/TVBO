 package com.deranged.tvbo;
 
 import java.io.PrintStream;
 
 public class SCStructure extends SCObject
 {
   protected int queue;
   protected int supply = 0;
   protected SCObject constructing;
   protected SCObject constructing2;
   private String addonName;
   private boolean lifted;
 
   public SCStructure(Model model, String name)
   {
     super(model, name);
     this.queue = 0;
     this.supply = model.getSupply(name);
     this.addonName = "";
     this.lifted = false;
   }
 
   public void update()
   {
     if (this.complete)
     {
       if (this.queue > 0) {
         this.progress += 1;
         if ((this.progress >= this.buildtime) && 
           (this.progress >= this.buildtime)) {
           this.queue -= 1;
 
           this.progress = 0;
         }
       }
       if (this.constructing != null)
       {
         this.constructing.update();
         if ((this.constructing.isComplete()) && (!this.constructing.getName().equals("SCV"))) {
           this.model.addUnit(this.constructing);
 
           this.constructing = null;
         }
 
       }
 
       if ((this.addonName.equals("Reactor")) && (this.constructing2 != null))
       {
         this.constructing2.update();
         if ((this.constructing2.isComplete()) && (!this.constructing2.getName().equals("SCV"))) {
           this.model.addUnit(this.constructing2);
           this.constructing2 = null;
         }
       }
 
     }
     else
     {
       if (this.progress < this.buildtime) {
         this.progress += 1;
       }
       if (this.progress >= this.buildtime) {
         this.complete = true;
         this.progress = 0;
         this.model.addSupply(this.supply);
       }
     }
   }
 
   public boolean addObjectToQueue(String name)
   {
     this.buildtime = this.model.getTime(name);
 
     if ((this.addonName.equals("Reactor")) && (this.complete)) {
       if ((this.constructing == null) && (this.queue == 0) && (this.complete)) {
         this.constructing = new SCObject(this.model, name);
         this.model.spendMinerals(this.model.getMineralCost(name));
         this.model.spendGas(this.model.getGasCost(name));
         this.model.addFood(this.model.getFood(name));
         return true;
       }if ((this.constructing != null) && (this.constructing2 == null)) {
         this.constructing2 = new SCObject(this.model, name);
         this.model.spendMinerals(this.model.getMineralCost(name));
         this.model.spendGas(this.model.getGasCost(name));
         this.model.addFood(this.model.getFood(name));
         return true;
       }
 
       return false;
     }
     if ((this.constructing == null) && (this.queue == 0) && (this.complete))
     {
       this.constructing = new SCObject(this.model, name);
       this.model.spendMinerals(this.model.getMineralCost(name));
       this.model.spendGas(this.model.getGasCost(name));
       this.model.addFood(this.model.getFood(name));
 
       return true;
     }
     return false;
   }
 
   public boolean isAvailable()
   {
     if (this.complete) {
       if ((!this.lifted) && (this.addonName.equals("Reactor")))
       {
         return (this.progress <= 0) || (this.constructing == null) || (this.constructing2 == null);
       }
 
       return (this.progress == 0) && (!this.lifted) && (this.constructing == null);
     }
 
     return false;
   }
   public boolean isAvailableToLift()
   {
     if (this.complete) {
       return (this.progress == 0) && (!this.lifted) && (this.constructing == null);
     }
      return false;
   }
 
   public boolean buildAddon(String addonName)
   {
     setBuildtime(this.model.getTime(addonName));
     this.queue += 1;
     this.model.spendMinerals(this.model.getMineralCost(addonName));
     this.model.spendGas(this.model.getGasCost(addonName));
     this.progress = 0;
     this.addonName = addonName;
 
     return true;
   }
 
   public String getConstructingName() {
     if (this.constructing != null) {
       return this.constructing.getName();
     }
     return "";
   }
 
   public int getQueueLength()
   {
     return this.queue;
   }
 
   public int getSupply() {
     return this.supply;
   }
 
   public void setSupply(int supply) {
     this.supply = supply;
   }
 
   public String getAddonName() {
     return this.addonName;
   }
 
   public void setAddonName(String addonName) {
     this.addonName = addonName;
   }
 
   public boolean isLifted() {
     return this.lifted;
   }
 
   public void setLifted(boolean lifted) {
     this.lifted = lifted;
   }
   public boolean lift() {
     if ((this.name.equals("Barracks")) || (this.name.equals("Factory")) || (this.name.equals("Starport"))) {
       if (this.lifted) {
         return false;
       }
       this.lifted = true;
       this.progress = 0;
       this.buildtime = 3;
       this.queue += 1;
       this.addonName = "";
       return true;
     }
 
     System.out.println(this.model.printTime() + "   <SCObject> Cannot lift " + this.name);
     return false;
   }
 
   public boolean land(String addon)
   {
     if ((this.name.equals("Barracks")) || (this.name.equals("Factory")) || (this.name.equals("Starport"))) {
       if (!this.lifted) {
         return false;
       }
       this.lifted = false;
       this.progress = 0;
       this.buildtime = 3;
       this.queue += 1;
       if (addon.equals("none"))
         this.addonName = "";
       else {
         this.addonName = addon;
       }
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
