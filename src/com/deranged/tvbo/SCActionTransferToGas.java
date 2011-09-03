 package com.deranged.tvbo;
 
 public class SCActionTransferToGas extends SCAction
 {
   public SCActionTransferToGas(Model model, int startTime, int y)
   {
     super(model, startTime, y, 20, "TransferToGas");
   }
 
   public boolean execute() {
     boolean f = true;
     if (this.complete) {
       f = false;
     } else if (this.model.freeRefineries() == 0) {
       f = false;
       this.errorMsg = "NO REFINERY";
     } else if (!this.model.transferToGas()) {
       f = false;
       this.errorMsg = "UNKNOWN";
     }
     if (f) {
       this.complete = true;
     }
     return f;
   }
 
   public String toString() {
     return "+1 on gas";
   }
 }
