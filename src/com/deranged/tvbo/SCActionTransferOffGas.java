 package com.deranged.tvbo;
 
 public class SCActionTransferOffGas extends SCAction
 {
   public SCActionTransferOffGas(Model model, int startTime, int y)
   {
     super(model, startTime, y, 20, "1 off gas");
   }
 
   public boolean execute() {
     boolean f = true;
     if (this.complete) {
       f = false;
     } else if (this.model.scvsOnGas() == 0) {
       f = false;
       this.errorMsg = "NONE";
     } else if (!this.model.transferOffGas()) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 }
