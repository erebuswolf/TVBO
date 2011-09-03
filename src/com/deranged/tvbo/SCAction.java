 package com.deranged.tvbo;
 
 import java.util.ArrayList;
 
 public class SCAction
 {
   protected Model model;
   protected String name;
   protected String supplyPoint;
   private int startTime;
   protected int preactionTime;
   private int y;
   protected boolean complete;
   protected boolean preactionComplete;
   protected int duration;
   private boolean selected;
   protected String errorMsg;
   private boolean popup;
   protected String option;
   protected ArrayList<String> options;
 
   public SCAction(Model model, int startTime, int y, int duration, String name)
   {
     this.model = model;
     this.startTime = startTime;
     this.y = y;
     this.duration = duration;
 
     this.name = name;
     this.errorMsg = "";
     this.supplyPoint = "";
     this.complete = false;
     this.selected = false;
     this.preactionTime = 0;
     this.preactionComplete = false;
     this.popup = false;
     this.options = new ArrayList<String>();
   }
 
   public void reset() {
     this.complete = false;
     this.errorMsg = "";
     this.supplyPoint = "";
   }
 
   public void addStartTime(int x)
   {
     this.startTime += x;
     if (this.startTime < 0)
       this.startTime -= x;
   }
 
   public void moveY(int dy) {
     this.y += dy;
     if ((this.y < 1) || (this.y > this.model.getHeight() / this.model.getSpacing()))
       this.y -= dy;
   }
 
   public boolean execute()
   {
     return true;
   }
 
   public boolean preaction() {
     return true;
   }
 
   public Model getModel() {
     return this.model;
   }
 
   public String getName() {
     return this.name;
   }
 
   public int getStartTime() {
     return this.startTime;
   }
   public int getPreactionTime() {
     return getStartTime() - this.preactionTime;
   }
 
   public int getY() {
     return this.y;
   }
 
   public boolean isComplete() {
     return this.complete;
   }
 
   public int getDuration() {
     return this.duration;
   }
 
   public boolean isSelected()
   {
     return this.selected;
   }
 
   public String getErrorMsg() {
     return this.errorMsg;
   }
 
   public void setModel(Model model) {
     this.model = model;
   }
 
   public void setName(String name) {
     this.name = name;
   }
 
   public void setStartTime(int startTime) {
     this.startTime = startTime;
   }
 
   public void setY(int y) {
     this.y = y;
   }
 
   public void setComplete(boolean complete) {
     this.complete = complete;
   }
 
   public void setDuration(int duration) {
     this.duration = duration;
   }
 
   public void select() {
     this.selected = true;
   }
 
   public void toggleSelect() {
     if (this.selected)
       this.selected = false;
     else
       this.selected = true;
   }
 
   public void deselect() {
     this.selected = false;
   }
 
   public void setErrorMsg(String errorMsg) {
     this.errorMsg = errorMsg;
   }
   public String getSupplyPoint() {
     return this.supplyPoint;
   }
   public void setSupplyPoint(String supplyPoint) {
     this.supplyPoint = supplyPoint;
   }
   public boolean getPopup() {
     return this.popup;
   }
   public int getOptionsSize() {
     return this.options.size();
   }
   public String getOption(int i) {
     return this.options.get(i);
   }
 
   public String getOption() {
     return this.option;
   }
 
   public void setOption(String option) {
     this.option = option;
   }
 
   public void togglePopup() {
     if (this.popup)
       this.popup = false;
     else
       this.popup = true;
   }
 
   public void setPopup(boolean f) {
     this.popup = f;
   }
   public void setOption(int i) {
     this.option = this.options.get(i);
 
     this.model.reset();
     this.model.play();
   }
 
   public String toString() {
     return this.name;
   }
 }
