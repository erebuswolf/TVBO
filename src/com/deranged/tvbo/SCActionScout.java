 package com.deranged.tvbo;
 
 import java.util.ArrayList;
 
 public class SCActionScout extends SCAction
 {
   public SCActionScout(Model model, int startTime, int y)
   {
     super(model, startTime, y, 30, "Scout");
     this.option = "30";
     this.options.add("15");
     this.options.add("30");
     this.options.add("45");
     this.options.add("60");
   }
   public SCActionScout(Model model, int startTime, int y, String option) {
     super(model, startTime, y, 30, "Scout");
     this.option = option;
     this.options.add("15");
     this.options.add("30");
     this.options.add("45");
     this.options.add("60");
   }
 
   public boolean execute() {
     boolean f = true;
     if (this.option.equals("")) {
       f = false;
       this.errorMsg = "SET OPTION";
     } else if (!this.model.scout(this.option)) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 
   public void setOption(int i)
   {
     this.option = ((String)this.options.get(i));
     this.duration = Integer.parseInt(this.option);
 
     this.model.reset();
     this.model.play();
   }
 }
 