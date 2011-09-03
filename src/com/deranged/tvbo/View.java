 package com.deranged.tvbo;
 
 import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JPanel;
 
 public class View extends JPanel
 {
   private Model model;
   private int border;
   private double scale;
   private int scroll;
   private int spacing;
   private int thickness;
   private int width;
   private int height;
   private Font font;
   private Font littleFont;
   private Hashtable <String,Image> images = new Hashtable<String,Image>();
   private Hashtable <String,Image> nameToImage = new Hashtable<String,Image>();
   
 
   public View(Model model)
   {
     this.model = model;
     this.font = new Font("Tahoma", 0, 11);
     this.littleFont = new Font("Tahoma", 0, 9);
     setUpImages();
   }
 
   public void paint(Graphics g) {
     this.width = getWidth();
     this.height = getHeight();
 
     drawAllIcons(g);
     g.clearRect(0, 0, getWidth(), getHeight());
     g.setColor(new Color(150, 150, 220, 200));
     g.setFont(this.font);
 
     this.border = this.model.getBorder();
     this.scale = this.model.getScale();
     this.scroll = this.model.getScroll();
     this.spacing = this.model.getSpacing();
     this.thickness = this.model.getThickness();
 
     g.drawRect(this.border, this.border, this.width - 2 * this.border, this.height - 2 * this.border);
     for (int i = 0; i < this.width; i += 30) {
       if ((int)(this.scale * (i - this.scroll) + this.border) < this.width - this.border) {
         g.setColor(Color.black);
         if ((int)(this.scale * (i - this.scroll)) >= 0)
         {
           g.drawString(this.model.printTime(i), (int)(this.scale * (i - this.scroll) + this.border - 10.0D), this.border - 4);
         }
         g.setColor(new Color(150, 150, 220, 200));
         if ((int)(this.scale * (i - this.scroll)) >= 0) {
           g.drawLine((int)(this.scale * (i - this.scroll) + this.border), this.border, (int)(this.scale * (i - this.scroll) + this.border), this.height - this.border);
         }
       }
     }
 
     for (int i = 50; i < this.height - 2 * this.border; i += 50) {
       g.setColor(new Color(150, 150, 150, 100));
       g.drawLine(this.border, this.height - this.border - i, this.width - this.border, this.height - this.border - i);
       g.setColor(new Color(100, 100, 100, 250));
       g.drawString(""+i, this.width - this.border + 5, this.height - this.border - i + 3);
     }
 
     for (int i = 1 + this.scroll; i < this.model.getMaxTime(); i++)
     {
       g.setColor(new Color(150, 150, 220, 150));
       int x1 = (int)(this.border + this.scale * (i - 1 - this.scroll));
       int y1 = this.height - this.border - this.model.getMineralGraph(i - 1);
       int x2 = (int)(this.border + this.scale * (i - this.scroll));
       int y2 = this.height - this.border - this.model.getMineralGraph(i);
       if ((y1 > this.border) && (y2 > this.border) && (x2 < this.width - this.border)) {
         g.drawLine(x1, y1, x2, y2);
       }
 
       g.setColor(new Color(150, 220, 150, 150));
       x1 = (int)(this.border + this.scale * (i - 1 - this.scroll));
       y1 = this.height - this.border - this.model.getGasGraph(i - 1);
       x2 = (int)(this.border + this.scale * (i - this.scroll));
       y2 = this.height - this.border - this.model.getGasGraph(i);
       if ((y1 > this.border) && (y2 > this.border) && (x2 < this.width - this.border)) {
         g.drawLine(x1, y1, x2, y2);
       }
 
       g.setColor(new Color(220, 150, 220, 150));
       x1 = (int)(this.border + this.scale * (i - 1 - this.scroll));
       y1 = this.height - this.border - this.model.getEnergyGraph(i - 1);
       x2 = (int)(this.border + this.scale * (i - this.scroll));
       y2 = this.height - this.border - this.model.getEnergyGraph(i);
       if ((y1 <= this.border) || (y2 <= this.border) || (x2 >= this.width - this.border) || (
         (y1 <= 0) && (y2 <= 0))) continue;
       g.drawLine(x1, y1, x2, y2);
     }
 
     int s = this.model.actionCount();
 
     int optionsSize = 0;
     for (int i = 0; i < s; i++) {
       SCAction action = this.model.getAction(i);
       int actionY = action.getY();
       int t = action.getStartTime();
       String aName = action.toString();
       int left = (int)(this.border + this.scale * (t - this.scroll));
       int len = (int)(this.scale * action.getDuration());
       int top = this.border + this.spacing * actionY;
       boolean popup = action.getPopup();
       if (action.isComplete()) {
         if (action.isSelected()) {
           g.setColor(new Color(150, 220, 150, 150));
           g.fillRect(left, top, len, this.thickness);
           g.setColor(new Color(0, 100, 0, 255));
           g.drawRect(left, top, len, this.thickness);
           if (action.getOptionsSize() > 0) {
             g.setColor(new Color(150, 220, 150, 150));
             g.fillRect(left + len - 13, top + this.thickness, 13, 13);
             g.setColor(new Color(0, 100, 0, 255));
             g.drawRect(left + len - 13, top + this.thickness, 13, 13);
           }
           g.setColor(new Color(75, 75, 75, 255));
 
           g.setFont(this.littleFont);
           g.drawString(action.getSupplyPoint(), left + 5, top + 24);
         } else {
           g.setColor(new Color(150, 220, 150, 150));
           g.fillRect(left, top, len, this.thickness);
           g.setColor(new Color(150, 220, 150, 200));
           g.drawRect(left, top, len, this.thickness);
           g.setColor(new Color(75, 75, 75, 255));
           g.setFont(this.littleFont);
           g.drawString(action.getSupplyPoint(), left + 5, top + 24);
         }
       }
       else if (action.isSelected()) {
         g.setColor(new Color(220, 150, 150, 150));
         g.fillRect(left, top, len, this.thickness);
         g.setColor(new Color(100, 0, 0, 255));
         g.drawRect(left, top, len, this.thickness);
         if (action.getOptionsSize() > 0) {
           g.setColor(new Color(220, 150, 150, 150));
           g.fillRect(left + len - 13, top + this.thickness, 13, 13);
           g.setColor(new Color(100, 0, 0, 255));
           g.drawRect(left + len - 13, top + this.thickness, 13, 13);
         }
         g.setColor(new Color(255, 0, 0));
         g.setFont(this.littleFont);
         g.drawString(action.getErrorMsg(), left, top + 24);
       } else {
         g.setColor(new Color(220, 150, 150, 150));
         g.fillRect(left, top, len, this.thickness);
         g.setColor(new Color(220, 150, 150, 200));
         g.drawRect(left, top, len, this.thickness);
         g.setColor(new Color(255, 0, 0));
         g.setFont(this.littleFont);
         g.drawString(action.getErrorMsg(), left, top + 24);
       }
 
       drawIcon(g, left + len - 26, top + 1, aName);
       g.setFont(this.font);
       g.drawString(aName, left + 3, top + 12);
     }
 
     for (int i = 0; i < s; i++) {
       SCAction action = this.model.getAction(i);
       boolean popup = action.getPopup();
       String aName = action.toString();
       int left = (int)(this.border + this.scale * (action.getStartTime() - this.scroll));
       int len = (int)(this.scale * action.getDuration());
       int top = this.border + this.spacing * action.getY();
       if ((popup) && (action.isSelected())) {
         optionsSize = action.getOptionsSize();
         if (optionsSize > 0) {
           g.setColor(new Color(200, 200, 200, 200));
           g.fillRect(left + len, top + this.thickness, 60, optionsSize * 14);
           g.setColor(new Color(100, 100, 100, 200));
           g.drawRect(left + len, top + this.thickness, 60, optionsSize * 14);
           g.setColor(new Color(100, 100, 100, 255));
           for (int op = 0; op < optionsSize; op++) {
             g.drawString(action.getOption(op), left + len + 3, top + this.thickness + op * 14 + 11);
           }
         }
       }
     }
 
     g.setColor(new Color(150, 150, 220, 200));
     int ay = this.height - this.border + 5;
     int ax = this.width - this.border - 30;
 
     g.drawRect(ax, ay, 30, 30);
     g.drawRect(ax - 35, ay, 30, 30);
     g.drawRect(ax - 80, ay, 30, 30);
     g.drawRect(ax - 115, ay, 30, 30);
     int ad = 5;
 
     g.drawLine(ax + ad, ay + 10, ax + ad + 10, ay + 10);
     g.drawLine(ax + ad + 10, ay + 10, ax + ad + 10, ay + 5);
     g.drawLine(ax + ad + 10, ay + 5, ax + 25, ay + 15);
     g.drawLine(ax + ad + 10, ay + 25, ax + 25, ay + 15);
     g.drawLine(ax + ad + 10, ay + 20, ax + ad + 10, ay + 25);
     g.drawLine(ax + ad, ay + 20, ax + ad + 10, ay + 20);
     g.drawLine(ax + ad, ay + 10, ax + ad, ay + 20);
 
     ax = this.width - this.border - 65;
     g.drawLine(ax + 25, ay + 10, ax + 15, ay + 10);
     g.drawLine(ax + 15, ay + 10, ax + 15, ay + 5);
     g.drawLine(ax + 15, ay + 5, ax + 5, ay + 15);
     g.drawLine(ax + 15, ay + 25, ax + 5, ay + 15);
     g.drawLine(ax + 15, ay + 20, ax + 15, ay + 25);
     g.drawLine(ax + 25, ay + 20, ax + 15, ay + 20);
     g.drawLine(ax + 25, ay + 10, ax + 25, ay + 20);
 
     ax = this.width - this.border - 110;
     g.drawLine(ax + 12, ay + 3, ax + 18, ay + 3);
     g.drawLine(ax + 12, ay + 27, ax + 18, ay + 27);
     g.drawLine(ax + 3, ay + 12, ax + 3, ay + 18);
     g.drawLine(ax + 27, ay + 12, ax + 27, ay + 18);
 
     g.drawLine(ax + 12, ay + 3, ax + 12, ay + 12);
     g.drawLine(ax + 12, ay + 27, ax + 12, ay + 18);
     g.drawLine(ax + 18, ay + 3, ax + 18, ay + 12);
     g.drawLine(ax + 18, ay + 27, ax + 18, ay + 18);
 
     g.drawLine(ax + 3, ay + 12, ax + 12, ay + 12);
     g.drawLine(ax + 3, ay + 18, ax + 12, ay + 18);
     g.drawLine(ax + 27, ay + 12, ax + 18, ay + 12);
     g.drawLine(ax + 27, ay + 18, ax + 18, ay + 18);
 
     g.drawRect(this.width - this.border - 142, ay + 11, 24, 7);
 
     int mX1 = this.model.getmX1();
     int mY1 = this.model.getmY1();
     int mX2 = this.model.getmX2();
     int mY2 = this.model.getmY2();
     if ((mX1 != mX2) && (mY1 != mY2)) {
       if (mX2 < mX1) {
         int tmp = mX2;
         mX2 = mX1;
         mX1 = tmp;
       }
       if (mY2 < mY1) {
         int tmp = mY2;
         mY2 = mY1;
         mY1 = tmp;
       }
       g.setColor(new Color(150, 150, 255, 50));
       g.fillRect(mX1, mY1, mX2 - mX1, mY2 - mY1);
       g.setColor(new Color(100, 100, 200, 150));
       g.drawRect(mX1, mY1, mX2 - mX1, mY2 - mY1);
     }
   }
 
   public void drawIcon(Graphics g, int x, int y, String name)
   {
     if (g == null) {
       System.out.println("graphics are null");
     }
     Image temp = nameToImage.get(name);
     if(temp != null)
     {
    	 g.drawImage(temp, x - 3, y, null);
     }else{
         System.out.println("\"" + name + "\" not found in <drawIcon>");
     }
   }
 
   public void drawAllIcons(Graphics g)
   {
     int x = 0;
     int y = 0;
     if (g == null) {
       System.out.println("graphics are null");
     } else {
    	 Iterator<Image> it= images.values().iterator();
    	 while(it.hasNext()){
    	       g.drawImage(it.next(), x, y, null); 
    	 }
     }
   }
 
   public void setUpImages()
   {
     try
     {
    	 Scanner fileRead=null;
    	 ArrayList <String> imagepaths=new ArrayList<String>();
    	 ArrayList <String> names=new ArrayList<String>();
    	 try{
    	 InputStream dataFile=getClass().getResourceAsStream("/assets/data");
    	 fileRead = new Scanner(dataFile);
    	 }catch(Exception e){
    		 System.out.println("input file failure");
    		 e.printStackTrace();
    	 }
    	 while(fileRead.hasNext()){
	    	 String a=fileRead.nextLine();
	    	 int split=a.indexOf("\"", 1);
	    	 String name=a.substring(1, split);
	    	 names.add(name);
	    	 String imagePath=a.substring(split+1,a.length()-1);
	    	 imagepaths.add(imagePath);
	    	 images.put(imagePath, Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath)));
	    	 nameToImage.put(name, images.get(imagePath));
    	 }
     }catch(Exception e){
    	 System.out.println("exception loading images");
    	 e.printStackTrace();
     }
   }
 }
