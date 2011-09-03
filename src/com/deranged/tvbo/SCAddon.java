 package com.deranged.tvbo;
 
 import java.io.PrintStream;
 
 public class SCAddon extends SCStructure
 {
   private String attachedTo;
 
   public SCAddon(Model model, String name)
   {
     super(model, name);
   }
 
   public String getAttachedTo() {
     return this.attachedTo;
   }
 
   public void setAttachedTo(String attachedTo) {
     this.attachedTo = attachedTo;
   }
 
   public boolean detach()
   {
     if ((this.name.equals("TechLab")) || (this.name.equals("Reactor")))
     {
       this.attachedTo = "";
       return true;
     }
     System.out.println(this.model.printTime() + "   <SCObject> Cannot detach " + this.name);
     return false;
   }
 
   public boolean attach(String building)
   {
     if ((this.name.equals("TechLab")) || (this.name.equals("Reactor")))
     {
       this.attachedTo = building;
       this.progress = 0;
       this.buildtime = 3;
       this.queue += 1;
       return true;
     }
     System.out.println(this.model.printTime() + "   <SCObject> Cannot detach " + this.name);
     return false;
   }
 }
