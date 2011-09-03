package com.deranged.tvbo;

public class OCAction extends SCAction {
	public static enum OCType{SCAN,MULE,CALLDOWNSUPPLY};
	static int [] time = {20,90,30};
	static int [] energy = {50,50,50};
	static String [] stringTypes = {"Scan","Mule","CalldownSupply"};

	private OCType type;
	public OCAction(OCType type, Model model, int startTime, int y)
	{
		super(model, startTime, y, time[type.ordinal()], stringTypes[type.ordinal()]);
		this.type=type;
	}
	public boolean execute() {
		boolean f = true;
		if (this.complete) {
			f = false;
		} else if (!this.model.hasEnergy(energy[type.ordinal()])) {
			f = false;
			this.errorMsg = "ENERGY";
		} else if (!this.model.OCAction(type)) {
			f = false;
			this.errorMsg = "UNKNOWN";
		}
		if (f) {
			this.complete = true;
		}
		return f;
	}
}
