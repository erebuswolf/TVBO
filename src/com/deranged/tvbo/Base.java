 package com.deranged.tvbo;
 
 import java.util.ArrayList;
 
 public class Base extends SCStructure
 {
   private MineralPatch[] patches;
   private VespeneGeyser[] gas;
   private String upgrading;
   private int energy;
   private double energyRegen;
   private double energyCounter;
   private ArrayList<SCV> idleSCVs = new ArrayList<SCV>();
   private ArrayList<SCV> busySCVs = new ArrayList<SCV>();
 
   public Base(Model model) {
     super(model, "CommandCenter");
     this.supply = 11;
     this.patches = new MineralPatch[8];
     this.patches[0] = new MineralPatch(1500, 1);
     this.patches[1] = new MineralPatch(1500, 1);
     this.patches[2] = new MineralPatch(1500, 1);
     this.patches[3] = new MineralPatch(1500, 1);
     this.patches[4] = new MineralPatch(1500, 2);
     this.patches[5] = new MineralPatch(1500, 2);
     this.patches[6] = new MineralPatch(1500, 3);
     this.patches[7] = new MineralPatch(1500, 3);
 
     this.gas = new VespeneGeyser[2];
     this.gas[0] = new VespeneGeyser(2500);
     this.gas[1] = new VespeneGeyser(2500);
 
     this.idleSCVs = new ArrayList<SCV>();
     this.busySCVs = new ArrayList<SCV>();
     setName("CommandCenter");
     this.upgrading = "";
     this.complete = false;
     this.energy = 0;
     this.energyRegen = 0.0D;
     this.energyCounter = 0.0D;
   }
 
   public void start() {
     for (int i = 0; i < 6; i++) {
       SCV s = new SCV(this.model);
       s.complete();
       this.patches[i].addSCV(s);
       this.model.addFood(1);
     }
     this.model.addSupply(this.supply);
     this.complete = true;
   }
 
   public void update()
   {
     super.update();
     if (this.complete)
     {
       for (int i = 0; i < 8; i++) {
         this.patches[i].update();
       }
       for (int i = 0; i < 2; i++) {
         this.gas[i].update();
       }
       if (this.energy < 200) {
         this.energyCounter += this.energyRegen;
       }
       if (this.energyCounter > 1.0D) {
         this.energy += 1;
         this.energyCounter -= 1.0D;
       }
 
       if ((this.queue == 0) && (this.upgrading.equals("OrbitalCommand")) && (this.progress == 0) && (this.buildtime == 35)) {
         setName(this.upgrading);
         setEnergy(50);
         setEnergyRegen(0.5625D);
         this.upgrading = "";
       }
       if ((this.queue == 0) && (this.upgrading.equals("PlanetaryFortress")) && (this.progress == 0) && (this.buildtime == 50)) {
         setName(this.upgrading);
         this.upgrading = "";
       }
 
       if ((this.constructing != null) && (this.constructing.isComplete()))
       {
         if (this.constructing.getName().equals("SCV"))
         {
           if (!addSCVtoMinerals((SCV)this.constructing))
             System.out.println("model.printTime() + <Base> SCV not added to minerals ...");
           else {
             this.constructing = null;
           }
         }
       }
 
       if ((this.idleSCVs.size() > 0) && (scvCount() < 24)) {
         addSCVtoMinerals((SCV)this.idleSCVs.remove(0));
       }
 
       if (this.busySCVs.size() > 0)
         for (int i = 0; i < this.busySCVs.size(); i++) {
           ((SCV)this.busySCVs.get(i)).update();
           if (((SCV)this.busySCVs.get(i)).getJob() != 0)
             continue;
           addSCVtoMinerals((SCV)this.busySCVs.get(i));
           this.busySCVs.remove(i);
           i--;
         }
     }
   }
 
   public int idleSCVCount()
   {
     return this.idleSCVs.size();
   }
 
   public boolean addSCV(SCV scv) {
     int lowest = 0;
     int low = 3;
     int total = 0;
     for (int i = 0; i < 8; i++) {
       total += this.patches[i].getSCVCount();
       if (this.patches[i].getSCVCount() < low) {
         low = this.patches[i].getSCVCount();
         lowest = i;
       }
     }
     scv.setResource(this.patches[lowest]);
     if (low < 3)
     {
       this.patches[lowest].addSCV(scv);
     }
     else {
       scv.setJob(3);
       this.idleSCVs.add(scv);
     }
 
     return true;
   }
 
   public boolean addSCVtoQueue()
   {
     if ((this.queue == 0) && (this.complete)) {
       this.queue += 1;
       this.progress = 0;
       this.buildtime = this.model.getTime("SCV");
 
       this.model.spendMinerals(this.model.getMineralCost("SCV"));
       this.model.addFood(this.model.getFood("SCV"));
       this.constructing = new SCV(this.model);
       return true;
     }
     return false;
   }
 
   public boolean addMule()
   {
     if (this.energy >= 50) {
       int most = 0;
       int patch = -1;
       boolean found = false;
 
       for (int i = 0; i < 8; i++) {
         if ((!this.patches[i].hasMule()) && (this.patches[i].resource > most)) {
           most = this.patches[i].resource;
           patch = i;
         }
       }
       if (patch >= 0) {
         this.patches[patch].addMule(this.model);
         found = true;
       }
       return found;
     }
     return false;
   }
 
   public boolean upgrade(String name)
   {
     if ((this.queue == 0) && (this.complete)) {
       this.queue += 1;
       this.progress = 0;
       this.buildtime = this.model.getTime(name);
       this.model.spendMinerals(this.model.getMineralCost(name));
       this.model.spendGas(this.model.getGasCost(name));
       this.upgrading = name;
       return true;
     }
     return false;
   }
 
   public boolean addSCVtoMinerals(SCV scv)
   {
     if (scv.isComplete()) {
       int lowest = 0;
       int low = 3;
       for (int i = 0; i < 8; i++)
       {
         if ((this.patches[i].getSCVCount() < low) && (this.patches[i].resource > 0)) {
           low = this.patches[i].getSCVCount();
           lowest = i;
         }
       }
       scv.setResource(this.patches[lowest]);
       if (low < 3)
       {
         this.patches[lowest].addSCV(scv);
       }
       else {
         scv.setJob(3);
         this.idleSCVs.add(scv);
       }
 
       return true;
     }
     System.out.println("<Base> Why are you adding unfinished SCVs here?");
     return false;
   }
 
   public boolean setSCVBuilding(int duration)
   {
     SCV s;
     if (this.idleSCVs.size() > 0) {
       s = (SCV)this.idleSCVs.remove(0);
     } else {
       int most = 0;
       int patch = -1;
       for (int i = 0; i < 8; i++)
         if (this.patches[i].getSCVCount() > most) {
           most = this.patches[i].getSCVCount();
           patch = i;
         }
       if (patch >= 0)
         s = this.patches[patch].removeSCV();
       else
         return false;
     }
     if (s != null) {
       s.setJobtime(duration, 2);
       this.busySCVs.add(s);
 
       return true;
     }
     System.out.println(this.model.printTime() + "   <Base> Null SCV found");
     return false;
   }
 
   public boolean removeSCV()
   {
     int patch = -1;
     int mostDrones = 0;
     for (int i = 0; i < 8; i++) {
       if (this.patches[i].getSCVCount() > mostDrones) {
         mostDrones = this.patches[i].getSCVCount();
         patch = i;
       }
     }
     if (patch >= 0) {
       this.patches[patch].removeSCV();
       this.model.addFood(-1);
       return true;
     }
     return false;
   }
 
   public SCV transferSCV()
   {
     int patch = -1;
     int mostSCVs = 0;
     for (int i = 0; i < 8; i++) {
       if (this.patches[i].getSCVCount() > mostSCVs) {
         mostSCVs = this.patches[i].getSCVCount();
         patch = i;
       }
     }
     if (patch >= 0) {
       SCV d = this.patches[patch].removeSCV();
       return d;
     }
     System.out.println(this.model.printTime() + "   <Base:transferSCV> Returning null scv");
     return null;
   }
 
   public boolean addRefinery()
   {
     int patch = -1;
     int most = 0;
     for (int i = 0; i < 8; i++) {
       if (this.patches[i].getSCVCount() > most) {
         most = this.patches[i].getSCVCount();
         patch = i;
       }
     }
     if (patch >= 0) {
       if (this.gas[0].hasRefinery()) {
         if (this.gas[1].hasRefinery()) {
           System.out.println(this.model.printTime() + "   <Base> Error: Both geysers have been taken!");
           return false;
         }
         this.gas[1].addRefinery(this.model);
 
         this.model.spendMinerals(this.model.getMineralCost("Refinery"));
         return true;
       }
 
       this.gas[0].addRefinery(this.model);
 
       this.model.spendMinerals(this.model.getMineralCost("Refinery"));
       return true;
     }
 
     System.out.println("Add code here for transfering SCV from another base... just cba atm");
     return false;
   }
 
   public int scvCount()
   {
     int t = 0;
     for (int i = 0; i < 8; i++) {
       t += this.patches[i].scvs;
     }
     return t;
   }
   public int scvCountGas() {
     int t = 0;
     t += this.gas[0].getSCVCount();
     t += this.gas[1].getSCVCount();
     return t;
   }
 
   public int getEnergy() {
     return this.energy;
   }
 
   public void setEnergy(int energy) {
     this.energy = energy;
   }
 
   public double getEnergyRegen() {
     return this.energyRegen;
   }
 
   public void setEnergyRegen(double energyRegen) {
     this.energyRegen = energyRegen;
   }
 
   public void useEnergy(int i) {
     this.energy -= i;
   }
 
   public int remainingMinerals() {
     int total = 0;
     for (int i = 0; i < 8; i++) {
       total += this.patches[i].resource;
     }
     return total;
   }
 
   public int freeGeysers() {
     int g = 0;
     if (!this.gas[0].hasRefinery()) g++;
     if (!this.gas[1].hasRefinery()) g++;
     return g;
   }
 
   public int freeRefineries() {
     int g = 0;
     if (this.complete) {
       if ((this.gas[0].scvs < 3) && (this.gas[0].hasCompleteRefinery())) g++;
       if ((this.gas[1].scvs < 3) && (this.gas[1].hasCompleteRefinery())) g++;
     }
     return g;
   }
 
   public boolean transferDroneToGas()
   {
     int m = 0;
     int p = 0;
     int g = 0;
     if ((this.gas[0].hasCompleteRefinery()) && (this.gas[0].getSCVCount() < 3))
       g = 0;
     else if ((this.gas[1].hasCompleteRefinery()) && (this.gas[1].getSCVCount() < 3)) {
       g = 1;
     }
     else {
       return false;
     }
     if (scvCount() < 1)
     {
       return false;
     }
 
     m = 0;
     for (int i = 0; i < 8; i++) {
       if (this.patches[i].scvs >= m) {
         m = this.patches[i].scvs;
         p = i;
       }
     }
     SCV d = this.patches[p].removeSCV();
 
     return this.gas[g].addSCV(d);
   }
 
   public void setSCVScouting(int duration)
   {
     int most = 0;
     int patch = -1;
     for (int i = 0; i < 8; i++) {
       if (this.patches[i].scvs > most) {
         most = this.patches[i].scvs;
         patch = i;
       }
     }
     SCV s = this.patches[patch].removeSCV();
     s.setJobtime(duration, 2);
     this.busySCVs.add(s);
   }
 
   public boolean transferSCVOffGas()
   {
     int g = -1;
     int d = 0;
     if (this.gas[1].hasCompleteRefinery()) {
       d = this.gas[1].getSCVCount();
       g = 1;
     }
     if ((this.gas[0].hasCompleteRefinery()) && (this.gas[0].getSCVCount() > d)) {
       d = this.gas[0].getSCVCount();
       g = 0;
     }
     if (g >= 0) {
       SCV r = this.gas[g].removeSCV();
       addSCV(r);
       return true;
     }
     return false;
   }
 }
