 package com.deranged.tvbo;
 
 public class SCActionMaynard extends SCAction
 {
   public SCActionMaynard(Model model, int startTime, int y)
   {
     super(model, startTime, y, 20, "Maynard");
   }
 
   public boolean execute()
   {
     boolean f = true;
     if (this.complete) {
       f = false;
     } else if (this.model.completedBases() < 2) {
       f = false;
       this.errorMsg = "NO BASE";
     } else if (!this.model.maynard()) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 }
