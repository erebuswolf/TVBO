 package com.deranged.tvbo;
 
 public class Resource
 {
   public int resource;
   public SCV[] scvsMining;
   public int max = 3;
   public int scvs = 0;
 
   public Resource(int r) {
     this.resource = r;
     this.scvsMining = new SCV[this.max];
   }
 
   public void update() {
     for (int i = 0; i < this.max; i++) {
       if (this.scvsMining[i] != null) {
         this.scvsMining[i].update();
       }
     }
     if (this.resource <= 0)
       for (int i = 0; i < this.max; i++)
         if (this.scvsMining[i] != null)
           this.scvsMining[i].setJob(3);
   }
 
   public boolean reduceResource(int r)
   {
     if (this.resource >= r) {
       this.resource -= r;
       return true;
     }
     return false;
   }
 
   public SCV removeSCV()
   {
     if (this.scvs > 0)
     {
       SCV s = this.scvsMining[(this.scvs - 1)];
       this.scvsMining[(this.scvs - 1)] = null;
       this.scvs -= 1;
       s.setJob(3);
       return s;
     }
     return null;
   }
 
   public int getSCVCount()
   {
     return this.scvs;
   }
 }
