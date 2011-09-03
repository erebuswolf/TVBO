package com.deranged.tvbo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Model
{
	private int time = 0;
	private int maxTime = 600;
	private int minerals;
	private int totalMineralsMined;
	private int totalGasMined;
	private int gas;
	private int lastTime = 0;
	private int supply = 0;
	private int food = 0;
	private int width;
	private int height;
	private double scale = 3.0D;
	private int scroll = 0;
	private int border = 40;
	private int spacing = 30;
	private int thickness = 15;
	private HashMap<String, Integer> mineralCost;
	private HashMap<String, Integer> gasCost;
	private HashMap<String, Integer> times;
	private HashMap<String, String> prereqs;
	private HashMap<String, Integer> foods;
	private HashMap<String, Integer> supplies;
	private HashMap<String, String> build;
	private HashMap<String, String> tech;
	private ArrayList<Base> bases;
	private ArrayList<SCObject> objects;
	private ArrayList<SCAction> actions;
	private int[] mineralGraph;
	private int[] gasGraph;
	private int[] energyGraph;
	int mX1 = 0;
	int mY1 = 0;
	int mX2 = 0;
	int mY2 = 0;

	public Model() {
		this.minerals = 50;
		this.gas = 0;
		this.totalMineralsMined = 0;
		this.totalGasMined = 0;
		this.bases = new ArrayList<Base>();
		this.objects = new ArrayList<SCObject>();
		this.actions = new ArrayList<SCAction>();
		this.mineralGraph = new int[this.maxTime];
		this.gasGraph = new int[this.maxTime];
		this.energyGraph = new int[this.maxTime];
		setUpHashes();
	}

	public void setup() {
		Base b = new Base(this);
		b.start();
		this.bases.add(b);
	}

	public void clear()
	{
		reset();
		this.actions.clear();
	}

	public void play() {
		this.totalMineralsMined = 0;
		this.totalGasMined = 0;
		int t = 0;
		int totalEnergy = 0;
		int p = 0;
		this.lastTime = 0;
		int s = this.actions.size();

		while (this.time < this.maxTime)
		{
			for (int i = 0; i < s; i++) {
				t = ((SCAction)this.actions.get(i)).getStartTime();
				p = ((SCAction)this.actions.get(i)).getPreactionTime();
				if (p == this.time) {
					((SCAction)this.actions.get(i)).preaction();
				}
				if (t == this.time) {
					((SCAction)this.actions.get(i)).setSupplyPoint(this.food + "/" + this.supply);
					((SCAction)this.actions.get(i)).execute();
					if ((((SCAction)this.actions.get(i)).isComplete()) && (t + ((SCAction)this.actions.get(i)).getDuration() > this.lastTime)) {
						this.lastTime = (t + ((SCAction)this.actions.get(i)).getDuration());
					}
				}
			}
			for (int i = 0; i < this.bases.size(); i++) {
				((Base)this.bases.get(i)).update();
			}

			for (int i = 0; i < this.objects.size(); i++) {
				((SCObject)this.objects.get(i)).update();
			}
			this.mineralGraph[this.time] = this.minerals;
			this.gasGraph[this.time] = this.gas;
			totalEnergy = 0;
			for (int b = 0; b < this.bases.size(); b++) {
				totalEnergy += ((Base)this.bases.get(b)).getEnergy();
			}
			this.energyGraph[this.time] = totalEnergy;

			this.time += 1;
		}
	}

	public void reset()
	{
		this.bases.clear();
		this.objects.clear();
		for (int i = 0; i < this.actions.size(); i++) {
			((SCAction)this.actions.get(i)).reset();
		}
		this.time = 0;
		this.minerals = 50;
		this.gas = 0;
		this.food = 0;
		this.supply = 0;

		Base b = new Base(this);
		b.start();
		this.bases.add(b);
	}

	public void setUpHashes()
	{
		this.mineralCost = new HashMap<String, Integer>();
		this.gasCost = new HashMap<String, Integer>();
		this.foods = new HashMap<String, Integer>();
		this.supplies = new HashMap<String, Integer>();
		this.times = new HashMap<String, Integer>();
		this.prereqs = new HashMap<String, String>();
		this.build = new HashMap<String, String>();
		this.tech = new HashMap<String, String>();

		this.mineralCost.put("SCV", Integer.valueOf(50));
		this.mineralCost.put("Marine", Integer.valueOf(50));
		this.mineralCost.put("Marauder", Integer.valueOf(100));
		this.mineralCost.put("Reaper", Integer.valueOf(50));
		this.mineralCost.put("Ghost", Integer.valueOf(200));
		this.mineralCost.put("Hellion", Integer.valueOf(100));
		this.mineralCost.put("SiegeTank", Integer.valueOf(150));
		this.mineralCost.put("Thor", Integer.valueOf(300));
		this.mineralCost.put("Viking", Integer.valueOf(150));
		this.mineralCost.put("Medivac", Integer.valueOf(100));
		this.mineralCost.put("Banshee", Integer.valueOf(150));
		this.mineralCost.put("Raven", Integer.valueOf(100));
		this.mineralCost.put("Battlecruiser", Integer.valueOf(400));

		this.mineralCost.put("SupplyDepot", Integer.valueOf(100));
		this.mineralCost.put("CommandCenter", Integer.valueOf(400));
		this.mineralCost.put("Refinery", Integer.valueOf(75));
		this.mineralCost.put("Barracks", Integer.valueOf(150));
		this.mineralCost.put("Bunker", Integer.valueOf(100));
		this.mineralCost.put("EngineeringBay", Integer.valueOf(125));
		this.mineralCost.put("Factory", Integer.valueOf(150));
		this.mineralCost.put("Starport", Integer.valueOf(150));
		this.mineralCost.put("OrbitalCommand", Integer.valueOf(150));
		this.mineralCost.put("PlanetaryFortress", Integer.valueOf(150));
		this.mineralCost.put("GhostAcademy", Integer.valueOf(150));
		this.mineralCost.put("FusionCore", Integer.valueOf(150));
		this.mineralCost.put("Armory", Integer.valueOf(150));
		this.mineralCost.put("TechLab", Integer.valueOf(50));
		this.mineralCost.put("Reactor", Integer.valueOf(50));
		this.mineralCost.put("MissileTurret", Integer.valueOf(100));
		this.mineralCost.put("SensorTower", Integer.valueOf(125));

		this.mineralCost.put("CombatShield", Integer.valueOf(100));
		this.mineralCost.put("StimPack", Integer.valueOf(100));
		this.mineralCost.put("ConcussiveShells", Integer.valueOf(50));
		this.mineralCost.put("NitroPack", Integer.valueOf(50));
		this.mineralCost.put("InfernalPreigniter", Integer.valueOf(150));
		this.mineralCost.put("SiegeTech", Integer.valueOf(100));
		this.mineralCost.put("250mmStrikeCannons", Integer.valueOf(150));
		this.mineralCost.put("CaduceusReactor", Integer.valueOf(100));
		this.mineralCost.put("CorvidReactor", Integer.valueOf(150));
		this.mineralCost.put("DurableMaterials", Integer.valueOf(150));
		this.mineralCost.put("SeekerMissile", Integer.valueOf(150));
		this.mineralCost.put("CloakingField", Integer.valueOf(200));

		this.mineralCost.put("InfantryWeaponsLevel1", Integer.valueOf(100));
		this.mineralCost.put("InfantryWeaponsLevel2", Integer.valueOf(175));
		this.mineralCost.put("InfantryWeaponsLevel3", Integer.valueOf(250));
		this.mineralCost.put("InfantryArmorLevel1", Integer.valueOf(100));
		this.mineralCost.put("InfantryArmorLevel2", Integer.valueOf(175));
		this.mineralCost.put("InfantryArmorLevel3", Integer.valueOf(250));
		this.mineralCost.put("NeosteelFrame", Integer.valueOf(100));
		this.mineralCost.put("BuildingArmor", Integer.valueOf(150));
		this.mineralCost.put("HiSecAutoTracking", Integer.valueOf(100));

		this.mineralCost.put("VehicleWeaponsLevel1", Integer.valueOf(100));
		this.mineralCost.put("VehicleWeaponsLevel2", Integer.valueOf(175));
		this.mineralCost.put("VehicleWeaponsLevel3", Integer.valueOf(250));
		this.mineralCost.put("VehicleArmorLevel1", Integer.valueOf(100));
		this.mineralCost.put("VehicleArmorLevel2", Integer.valueOf(175));
		this.mineralCost.put("VehicleArmorLevel3", Integer.valueOf(250));
		this.mineralCost.put("ShipWeaponsLevel1", Integer.valueOf(100));
		this.mineralCost.put("ShipWeaponsLevel2", Integer.valueOf(175));
		this.mineralCost.put("ShipWeaponsLevel3", Integer.valueOf(250));
		this.mineralCost.put("ShipArmorLevel1", Integer.valueOf(150));
		this.mineralCost.put("ShipArmorLevel2", Integer.valueOf(225));
		this.mineralCost.put("ShipArmorLevel3", Integer.valueOf(300));

		this.mineralCost.put("PersonalCloaking", Integer.valueOf(120));
		this.mineralCost.put("Nuke", Integer.valueOf(60));
		this.mineralCost.put("MoebiusReactor", Integer.valueOf(80));
		this.mineralCost.put("BehemothReactor", Integer.valueOf(80));
		this.mineralCost.put("WeaponRefit", Integer.valueOf(60));

		this.gasCost.put("SCV", Integer.valueOf(0));
		this.gasCost.put("Marine", Integer.valueOf(0));
		this.gasCost.put("Marauder", Integer.valueOf(25));
		this.gasCost.put("Reaper", Integer.valueOf(50));
		this.gasCost.put("Ghost", Integer.valueOf(100));
		this.gasCost.put("Hellion", Integer.valueOf(0));
		this.gasCost.put("SiegeTank", Integer.valueOf(125));
		this.gasCost.put("Thor", Integer.valueOf(200));
		this.gasCost.put("Viking", Integer.valueOf(75));
		this.gasCost.put("Medivac", Integer.valueOf(100));
		this.gasCost.put("Banshee", Integer.valueOf(100));
		this.gasCost.put("Raven", Integer.valueOf(200));
		this.gasCost.put("Battlecruiser", Integer.valueOf(300));

		this.gasCost.put("SupplyDepot", Integer.valueOf(0));
		this.gasCost.put("Barracks", Integer.valueOf(0));
		this.gasCost.put("Factory", Integer.valueOf(100));
		this.gasCost.put("Starport", Integer.valueOf(100));
		this.gasCost.put("OrbitalCommand", Integer.valueOf(0));
		this.gasCost.put("PlanetaryFortress", Integer.valueOf(150));
		this.gasCost.put("GhostAcademy", Integer.valueOf(50));
		this.gasCost.put("FusionCore", Integer.valueOf(150));
		this.gasCost.put("Armory", Integer.valueOf(100));
		this.gasCost.put("TechLab", Integer.valueOf(25));
		this.gasCost.put("Reactor", Integer.valueOf(50));
		this.gasCost.put("SensorTower", Integer.valueOf(100));

		this.gasCost.put("CombatShield", Integer.valueOf(100));
		this.gasCost.put("StimPack", Integer.valueOf(100));
		this.gasCost.put("ConcussiveShells", Integer.valueOf(50));
		this.gasCost.put("NitroPack", Integer.valueOf(50));
		this.gasCost.put("InfernalPreigniter", Integer.valueOf(150));
		this.gasCost.put("SiegeTech", Integer.valueOf(100));
		this.gasCost.put("250mmStrikeCannons", Integer.valueOf(150));
		this.gasCost.put("CaduceusReactor", Integer.valueOf(100));
		this.gasCost.put("CorvidReactor", Integer.valueOf(150));
		this.gasCost.put("DurableMaterials", Integer.valueOf(150));
		this.gasCost.put("SeekerMissile", Integer.valueOf(150));
		this.gasCost.put("CloakingField", Integer.valueOf(200));

		this.gasCost.put("InfantryWeaponsLevel1", Integer.valueOf(100));
		this.gasCost.put("InfantryWeaponsLevel2", Integer.valueOf(175));
		this.gasCost.put("InfantryWeaponsLevel3", Integer.valueOf(250));
		this.gasCost.put("InfantryArmorLevel1", Integer.valueOf(100));
		this.gasCost.put("InfantryArmorLevel2", Integer.valueOf(175));
		this.gasCost.put("InfantryArmorLevel3", Integer.valueOf(250));
		this.gasCost.put("NeosteelFrame", Integer.valueOf(100));
		this.gasCost.put("BuildingArmor", Integer.valueOf(150));
		this.gasCost.put("HiSecAutoTracking", Integer.valueOf(100));

		this.gasCost.put("VehicleWeaponsLevel1", Integer.valueOf(100));
		this.gasCost.put("VehicleWeaponsLevel2", Integer.valueOf(175));
		this.gasCost.put("VehicleWeaponsLevel3", Integer.valueOf(250));
		this.gasCost.put("VehicleArmorLevel1", Integer.valueOf(100));
		this.gasCost.put("VehicleArmorLevel2", Integer.valueOf(175));
		this.gasCost.put("VehicleArmorLevel3", Integer.valueOf(250));
		this.gasCost.put("ShipWeaponsLevel1", Integer.valueOf(100));
		this.gasCost.put("ShipWeaponsLevel2", Integer.valueOf(175));
		this.gasCost.put("ShipWeaponsLevel3", Integer.valueOf(250));
		this.gasCost.put("ShipArmorLevel1", Integer.valueOf(150));
		this.gasCost.put("ShipArmorLevel2", Integer.valueOf(225));
		this.gasCost.put("ShipArmorLevel3", Integer.valueOf(300));

		this.gasCost.put("PersonalCloaking", Integer.valueOf(120));
		this.gasCost.put("Nuke", Integer.valueOf(60));
		this.gasCost.put("MoebiusReactor", Integer.valueOf(80));
		this.gasCost.put("BehemothReactor", Integer.valueOf(80));
		this.gasCost.put("WeaponRefit", Integer.valueOf(60));

		this.times.put("SCV", Integer.valueOf(17));
		this.times.put("Marine", Integer.valueOf(25));
		this.times.put("Marauder", Integer.valueOf(30));
		this.times.put("Reaper", Integer.valueOf(45));
		this.times.put("Ghost", Integer.valueOf(40));
		this.times.put("Hellion", Integer.valueOf(30));
		this.times.put("SiegeTank", Integer.valueOf(45));
		this.times.put("Thor", Integer.valueOf(60));
		this.times.put("Viking", Integer.valueOf(42));
		this.times.put("Medivac", Integer.valueOf(42));
		this.times.put("Banshee", Integer.valueOf(60));
		this.times.put("Raven", Integer.valueOf(60));
		this.times.put("Battlecruiser", Integer.valueOf(90));
		this.times.put("Mule", Integer.valueOf(90));
		this.times.put("TransferToGas", Integer.valueOf(20));
		this.times.put("TransferOffGas", Integer.valueOf(20));
		this.times.put("Maynard", Integer.valueOf(20));

		this.times.put("CommandCenter", Integer.valueOf(100));
		this.times.put("Refinery", Integer.valueOf(30));
		this.times.put("SupplyDepot", Integer.valueOf(30));
		this.times.put("Barracks", Integer.valueOf(60));
		this.times.put("Bunker", Integer.valueOf(40));
		this.times.put("Factory", Integer.valueOf(60));
		this.times.put("Starport", Integer.valueOf(50));
		this.times.put("EngineeringBay", Integer.valueOf(35));
		this.times.put("OrbitalCommand", Integer.valueOf(35));
		this.times.put("PlanetaryFortress", Integer.valueOf(50));
		this.times.put("CalldownSupply", Integer.valueOf(30));
		this.times.put("GhostAcademy", Integer.valueOf(40));
		this.times.put("FusionCore", Integer.valueOf(65));
		this.times.put("Armory", Integer.valueOf(65));
		this.times.put("TechLab", Integer.valueOf(25));
		this.times.put("Reactor", Integer.valueOf(50));
		this.times.put("SensorTower", Integer.valueOf(25));
		this.times.put("MissileTurret", Integer.valueOf(25));

		this.times.put("Scan", Integer.valueOf(30));

		this.times.put("CombatShield", Integer.valueOf(110));
		this.times.put("StimPack", Integer.valueOf(170));
		this.times.put("ConcussiveShells", Integer.valueOf(60));
		this.times.put("NitroPack", Integer.valueOf(100));
		this.times.put("InfernalPreigniter", Integer.valueOf(110));
		this.times.put("SiegeTech", Integer.valueOf(80));
		this.times.put("250mmStrikeCannons", Integer.valueOf(110));
		this.times.put("CaduceusReactor", Integer.valueOf(80));
		this.times.put("CorvidReactor", Integer.valueOf(110));
		this.times.put("DurableMaterials", Integer.valueOf(110));
		this.times.put("SeekerMissile", Integer.valueOf(110));
		this.times.put("CloakingField", Integer.valueOf(110));

		this.times.put("InfantryWeaponsLevel1", Integer.valueOf(160));
		this.times.put("InfantryWeaponsLevel2", Integer.valueOf(190));
		this.times.put("InfantryWeaponsLevel3", Integer.valueOf(220));
		this.times.put("InfantryArmorLevel1", Integer.valueOf(160));
		this.times.put("InfantryArmorLevel2", Integer.valueOf(190));
		this.times.put("InfantryArmorLevel3", Integer.valueOf(220));
		this.times.put("NeosteelFrame", Integer.valueOf(110));
		this.times.put("BuildingArmor", Integer.valueOf(140));
		this.times.put("HiSecAutoTracking", Integer.valueOf(80));

		this.times.put("VehicleWeaponsLevel1", Integer.valueOf(160));
		this.times.put("VehicleWeaponsLevel2", Integer.valueOf(190));
		this.times.put("VehicleWeaponsLevel3", Integer.valueOf(220));
		this.times.put("VehicleArmorLevel1", Integer.valueOf(160));
		this.times.put("VehicleArmorLevel2", Integer.valueOf(190));
		this.times.put("VehicleArmorLevel3", Integer.valueOf(220));
		this.times.put("ShipWeaponsLevel1", Integer.valueOf(160));
		this.times.put("ShipWeaponsLevel2", Integer.valueOf(190));
		this.times.put("ShipWeaponsLevel3", Integer.valueOf(220));
		this.times.put("ShipArmorLevel1", Integer.valueOf(160));
		this.times.put("ShipArmorLevel2", Integer.valueOf(190));
		this.times.put("ShipArmorLevel3", Integer.valueOf(220));

		this.times.put("PersonalCloaking", Integer.valueOf(120));
		this.times.put("Nuke", Integer.valueOf(60));
		this.times.put("MoebiusReactor", Integer.valueOf(80));
		this.times.put("BehemothReactor", Integer.valueOf(80));
		this.times.put("WeaponRefit", Integer.valueOf(60));

		this.foods.put("SCV", Integer.valueOf(1));
		this.foods.put("Marine", Integer.valueOf(1));
		this.foods.put("Marauder", Integer.valueOf(2));
		this.foods.put("Reaper", Integer.valueOf(1));
		this.foods.put("Ghost", Integer.valueOf(2));
		this.foods.put("Hellion", Integer.valueOf(2));
		this.foods.put("SiegeTank", Integer.valueOf(3));
		this.foods.put("Thor", Integer.valueOf(6));
		this.foods.put("Viking", Integer.valueOf(2));
		this.foods.put("Medivac", Integer.valueOf(2));
		this.foods.put("Banshee", Integer.valueOf(3));
		this.foods.put("Raven", Integer.valueOf(2));
		this.foods.put("Battlecruiser", Integer.valueOf(6));

		this.supplies.put("SupplyDepot", Integer.valueOf(8));

		this.prereqs.put("Barracks", "SupplyDepot");
		this.prereqs.put("Factory", "Barracks");
		this.prereqs.put("Bunker", "Barracks");
		this.prereqs.put("GhostAcademy", "Barracks");
		this.prereqs.put("Starport", "Factory");
		this.prereqs.put("FusionCore", "Starport");
		this.prereqs.put("Armory", "Factory");
		this.prereqs.put("OrbitalCommand", "Barracks");
		this.prereqs.put("Bunker", "Barracks");
		this.prereqs.put("Ghost", "GhostAcademy");
		this.prereqs.put("Thor", "Armory");
		this.prereqs.put("Battlecruiser", "FusionCore");
		this.prereqs.put("MissileTurret", "EngineeringBay");
		this.prereqs.put("PlanetaryFortress", "EngineeringBay");
		this.prereqs.put("SensorTower", "EngineeringBay");
		this.prereqs.put("Nuke", "Factory");

		this.prereqs.put("NitroPack", "Factory");
		this.prereqs.put("InfantryWeaponsLevel2", "Armory");
		this.prereqs.put("InfantryWeaponsLevel3", "Armory");
		this.prereqs.put("InfantryArmorLevel2", "Armory");
		this.prereqs.put("InfantryArmorLevel3", "Armory");

		this.build.put("Marine", "Barracks");
		this.build.put("Marauder", "Barracks");
		this.build.put("Reaper", "Barracks");
		this.build.put("Ghost", "Barracks");
		this.build.put("Hellion", "Factory");
		this.build.put("SiegeTank", "Factory");
		this.build.put("Thor", "Factory");
		this.build.put("Medivac", "Starport");
		this.build.put("Viking", "Starport");
		this.build.put("Banshee", "Starport");
		this.build.put("Raven", "Starport");
		this.build.put("Battlecruiser", "Starport");

		this.build.put("CombatShield", "Barracks");
		this.build.put("StimPack", "Barracks");
		this.build.put("ConcussiveShells", "Barracks");
		this.build.put("NitroPack", "Barracks");
		this.build.put("InfernalPreigniter", "Factory");
		this.build.put("SiegeTech", "Factory");
		this.build.put("250mmStrikeCannons", "Factory");
		this.build.put("CaduceusReactor", "Starport");
		this.build.put("CorvidReactor", "Starport");
		this.build.put("DurableMaterials", "Starport");
		this.build.put("SeekerMissile", "Starport");
		this.build.put("CloakingField", "Starport");

		this.build.put("InfantryWeaponsLevel1", "EngineeringBay");
		this.build.put("InfantryWeaponsLevel2", "EngineeringBay");
		this.build.put("InfantryWeaponsLevel3", "EngineeringBay");
		this.build.put("InfantryArmorLevel1", "EngineeringBay");
		this.build.put("InfantryArmorLevel2", "EngineeringBay");
		this.build.put("InfantryArmorLevel3", "EngineeringBay");
		this.build.put("NeosteelFrame", "EngineeringBay");
		this.build.put("BuildingArmor", "EngineeringBay");
		this.build.put("HiSecAutoTracking", "EngineeringBay");

		this.build.put("VehicleWeaponsLevel1", "Armory");
		this.build.put("VehicleWeaponsLevel2", "Armory");
		this.build.put("VehicleWeaponsLevel3", "Armory");
		this.build.put("VehicleArmorLevel1", "Armory");
		this.build.put("VehicleArmorLevel2", "Armory");
		this.build.put("VehicleArmorLevel3", "Armory");
		this.build.put("ShipWeaponsLevel1", "Armory");
		this.build.put("ShipWeaponsLevel2", "Armory");
		this.build.put("ShipWeaponsLevel3", "Armory");
		this.build.put("ShipArmorLevel1", "Armory");
		this.build.put("ShipArmorLevel2", "Armory");
		this.build.put("ShipArmorLevel3", "Armory");

		this.build.put("PersonalCloaking", "GhostAcademy");
		this.build.put("BehemothReactor", "FusionCore");
		this.build.put("WeaponRefit", "FusionCore");
		this.build.put("MoebiusReactor", "GhostAcademy");
		this.build.put("Nuke", "GhostAcademy");

		this.tech.put("Marauder", "TechLab");
		this.tech.put("Reaper", "TechLab");
		this.tech.put("Ghost", "TechLab");
		this.tech.put("SiegeTank", "TechLab");
		this.tech.put("Thor", "TechLab");
		this.tech.put("Banshee", "TechLab");
		this.tech.put("Raven", "TechLab");
		this.tech.put("Battlecruiser", "TechLab");

		this.tech.put("CombatShield", "TechLab");
		this.tech.put("StimPack", "TechLab");
		this.tech.put("ConcussiveShells", "TechLab");
		this.tech.put("NitroPack", "TechLab");
		this.tech.put("InfernalPreigniter", "TechLab");
		this.tech.put("SiegeTech", "TechLab");
		this.tech.put("250mmStrikeCannons", "TechLab");
		this.tech.put("CaduceusReactor", "TechLab");
		this.tech.put("CorvidReactor", "TechLab");
		this.tech.put("DurableMaterials", "TechLab");
		this.tech.put("SeekerMissile", "TechLab");
		this.tech.put("CloakingField", "TechLab");
	}

	public void addUnitAction(String dropDown) {
		int x = this.maxTime - 1;
		int y = 1;
		int dur = getTime(dropDown);
		SCAction action;
		if (dropDown.equals("SCV")) {
			action = new SCActionBuildSCV(this, x, y);
		}
		else if (dropDown.equals("Mule")) {
			action = new OCAction(OCAction.OCType.MULE,this, x, y);
		}
		else if (dropDown.equals("TransferToGas")) {
			action = new SCActionTransferToGas(this, x, y);
		}
		else if (dropDown.equals("TransferOffGas")) {
			action = new SCActionTransferOffGas(this, x, y);
		}
		else if (dropDown.equals("Maynard")) {
			action = new SCActionMaynard(this, x, y);
		}
		else if (dropDown.equals("Scout"))
			action = new SCActionScout(this, x, y);
		else
			action = new SCActionBuildUnit(this, x, y, dropDown); 


		this.actions.add(action);
		reset();
		play();

		if (action.isComplete()) {
			do {
				action.addStartTime(-1);
				reset();
				play();
				if (!action.isComplete())
					action.addStartTime(1);
				if (action.getStartTime() <= 0) break; 
			}while (action.isComplete());
		}
		else
		{
			action.setStartTime(this.lastTime);
		}
		x = action.getStartTime();
		int size = this.actions.size();

		int end = x + dur;

		boolean space = false;

		while (!space) {
			space = true;
			for (int i = 0; i < size - 1; i++) {
				SCAction action2 = (SCAction)this.actions.get(i);
				int x2 = action2.getStartTime();
				int end2 = action2.getStartTime() + action2.getDuration();
				if (action2.getY() == y) {
					if ((x <= x2) && (x2 < end) && (end <= end2))
					{
						space = false;
					} else if ((x2 <= x) && (x < end2) && (end2 <= end))
					{
						space = false;
					} else if ((x < x2) && (end2 < end))
					{
						space = false; } else {
							if ((x2 >= x) || (end >= end2))
								continue;
							space = false;
						}
				}

			}

			if (!space) {
				y++;
			}
		}
		action.setY(y);
	}

	public void addBuildingAction(String dropDown)
	{
		int x = this.maxTime - 1;
		int y = 1;
		int dur = getTime(dropDown);
		SCAction action;
		if (dropDown.equals("OrbitalCommand")) {
			action = new SCActionUpgradeBase(this, x, y, "OrbitalCommand");
		}
		else
		{
			if (dropDown.equals("PlanetaryFortress")) {
				action = new SCActionUpgradeBase(this, x, y, "PlanetaryFortress");
			}
			else if (dropDown.equals("CalldownSupply")) {
				action = new OCAction(OCAction.OCType.CALLDOWNSUPPLY,this, x, y);
			}
			else if (dropDown.equals("Refinery")) {
				action = new SCActionBuildRefinery(this, x, y);
			}
			else if (dropDown.equals("TechLab")) {
				action = new SCActionBuildAddon(this, x, y, dropDown);
			}
			else if (dropDown.equals("Reactor")) {
				action = new SCActionBuildAddon(this, x, y, dropDown);
			}
			else if (dropDown.equals("LiftBarracks")) {
				action = new SCActionLift(this, x, y, "Barracks");
			}
			else if (dropDown.equals("LiftFactory")) {
				action = new SCActionLift(this, x, y, "Factory");
			}
			else if (dropDown.equals("LiftStarport")) {
				action = new SCActionLift(this, x, y, "Starport");
			}
			else if (dropDown.equals("LandBarracks")) {
				action = new SCActionLand(this, x, y, "Barracks");
			}
			else if (dropDown.equals("LandFactory")) {
				action = new SCActionLand(this, x, y, "Factory");
			}
			else if (dropDown.equals("LandStarport")) {
				action = new SCActionLand(this, x, y, "Starport");
			}
			else if (dropDown.equals("CommandCenter")) {
				action = new SCActionBuildBase(this, x, y);
			}
			else if (dropDown.equals("Scan"))
				action = new OCAction(OCAction.OCType.SCAN,this, x, y);
			else
				action = new SCActionBuilding(this, x, y, dropDown); 
		}

		this.actions.add(action);
		reset();
		play();

		if (action.isComplete()) {
			do {
				action.addStartTime(-1);
				reset();
				play();
				if (!action.isComplete())
					action.addStartTime(1);
				if (action.getStartTime() <= 0) break; 
			}while (action.isComplete());
		}
		else
		{
			action.setStartTime(this.lastTime);
		}
		x = action.getStartTime();
		int size = this.actions.size();

		int end = x + dur;
		boolean space = false;

		while (!space) {
			space = true;
			for (int i = 0; i < size - 1; i++) {
				SCAction action2 = (SCAction)this.actions.get(i);
				int x2 = action2.getStartTime();
				int end2 = action2.getStartTime() + action2.getDuration();
				if (action2.getY() == y) {
					if ((x <= x2) && (x2 < end) && (end <= end2))
						space = false;
					else if ((x2 <= x) && (x < end2) && (end2 <= end))
						space = false;
					else if ((x < x2) && (end2 < end))
						space = false;
					else if ((x2 < x) && (end < end2)) {
						space = false;
					}
				}
			}
			if (!space) {
				y++;
			}
		}
		action.setY(y);
	}

	public void addResearchAction(String dropDown)
	{
		int x = this.maxTime - 1;
		int y = 1;
		int dur = getTime(dropDown);
		SCAction action;
		if(dropDown.equals("Nuke")){
			action = new SCActionBuildAddon(this,x,y,dropDown);
		}else{
			action = new SCActionResearch(this, x, y, dropDown);
		}
		this.actions.add(action);
		reset();
		play();

		if (action.isComplete()) {
			do {
				action.addStartTime(-1);
				reset();
				play();
				if (!action.isComplete())
					action.addStartTime(1);
				if (action.getStartTime() <= 0) break; 
			}while (action.isComplete());
		}
		else
		{
			action.setStartTime(this.lastTime);
		}
		x = action.getStartTime();
		int size = this.actions.size();

		int end = x + dur;
		boolean space = false;

		while (!space) {
			space = true;
			for (int i = 0; i < size - 1; i++) {
				SCAction action2 = (SCAction)this.actions.get(i);
				int x2 = action2.getStartTime();
				int end2 = action2.getStartTime() + action2.getDuration();
				if (action2.getY() == y) {
					if ((x <= x2) && (x2 < end) && (end <= end2))
						space = false;
					else if ((x2 <= x) && (x < end2) && (end2 <= end))
						space = false;
					else if ((x < x2) && (end2 < end))
						space = false;
					else if ((x2 < x) && (end < end2)) {
						space = false;
					}
				}
			}
			if (!space) {
				y++;
			}
		}
		action.setY(y);
	}

	public boolean save(File file)
	{
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xml = xml + "<Actions>\n";
		int s = this.actions.size();
		int index = -1;
		for (int i = 0; i < s; i++) {
			SCAction action = (SCAction)this.actions.get(i);
			String c = action.getClass().toString();
			index = c.lastIndexOf(".");
			c = c.substring(index + 1, c.length());
			xml = xml + "  <Action Class=\"" + c + "\">\n";
			xml = xml + "    <Name>" + action.getName() + "</Name>\n";
			xml = xml + "    <StartTime>" + action.getStartTime() + "</StartTime>\n";
			xml = xml + "    <Y>" + action.getY() + "</Y>\n";

			xml = xml + "    <Option>" + action.getOption() + "</Option>\n";
			xml = xml + "  </Action>\n";
		}
		xml = xml + "</Actions>\n";

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(xml);
		} catch (IOException e) {
			System.out.println("Can't write XML to file - " + e);
			return false;
		} finally {
			try {
				if (writer != null)
					writer.close();
			}
			catch (IOException e) {
				System.out.println("Something else didn't work - " + e);
				return false;
			}
		}

		return true;
	}

	public boolean load(File file) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc;
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(file);
		}
		catch (ParserConfigurationException e)
		{
			return false;
		}
		catch (SAXException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
		DocumentBuilder db;
		this.actions.clear();
		Element docEle = doc.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("Action");
		if ((nl != null) && (nl.getLength() > 0)) {
			int size = nl.getLength();
			for (int i = 0; i < size; i++) {
				Element element = (Element)nl.item(i);
				this.actions.add(getActionFromXML(element));
			}
		}
		return true;
	}

	public SCAction getActionFromXML(Element element)
	{
		String c = element.getAttribute("Class");
		int startTime = getIntValue(element, "StartTime");
		int y = getIntValue(element, "Y");

		String name = getTextValue(element, "Name");
		String option = getTextValue(element, "Option");
		SCAction action = null;
		if (c.equals("SCActionBuilding"))
			action = new SCActionBuilding(this, startTime, y, name, option);
		else if (c.equals("SCActionBuildRefinery"))
			action = new SCActionBuildRefinery(this, startTime, y);
		else if (c.equals("SCActionBuildSCV"))
			action = new SCActionBuildSCV(this, startTime, y);
		else if (c.equals("SCActionBuildUnit"))
			action = new SCActionBuildUnit(this, startTime, y, name);
		else if (c.equals("SCActionTransferToGas"))
			action = new SCActionTransferToGas(this, startTime, y);
		else if (!c.equals("SCActionTransferOffGas"))
		{
			if (c.equals("OCAction")){
				if (name.equals("Mule")){
					action = new OCAction(OCAction.OCType.MULE,this, startTime, y);
				}else if(name.equals("CalldownSupply")){
					action = new OCAction(OCAction.OCType.CALLDOWNSUPPLY,this, startTime, y);
				}else if(name.equals("Scan")){
					action = new OCAction(OCAction.OCType.SCAN,this, startTime, y);
				}
			}
			else if (c.equals("SCActionUpgradeBase"))
				action = new SCActionUpgradeBase(this, startTime, y, name);
			else if (c.equals("SCActionBuildAddon"))
				action = new SCActionBuildAddon(this, startTime, y, name, option);
			else if (c.equals("SCActionLift"))
				action = new SCActionLift(this, startTime, y, name, option);
			else if (c.equals("SCActionLand"))
				action = new SCActionLand(this, startTime, y, name, option);
			else if (c.equals("SCActionResearch"))
				action = new SCActionResearch(this, startTime, y, name);
			else if (c.equals("SCActionBuildBase"))
				action = new SCActionBuildBase(this, startTime, y);
			else if (c.equals("SCActionMaynard"))
				action = new SCActionMaynard(this, startTime, y);
			else if (c.equals("SCActionScout"))
				action = new SCActionScout(this, startTime, y, option);
			else
				System.out.println("Unknown class type = " + c);
		}
		return action;
	}

	private String getTextValue(Element ele, String tagName)
	{
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if ((nl != null) && (nl.getLength() > 0)) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	private int getIntValue(Element ele, String tagName)
	{
		int r = 0;
		try {
			r = Integer.parseInt(getTextValue(ele, tagName));
		} catch (NumberFormatException e) {
			r = 0;
		}
		return r;
	}

	public boolean buildSCV()
	{
		int selectedBase = -1;
		int leastSCVs = 200;
		for (int i = 0; i < this.bases.size(); i++) {
			if ((((Base)this.bases.get(i)).isComplete()) && (((Base)this.bases.get(i)).getQueueLength() == 0) && (((Base)this.bases.get(i)).scvCount() < leastSCVs)) {
				leastSCVs = ((Base)this.bases.get(i)).scvCount();
				selectedBase = i;
			}
		}
		if (selectedBase >= 0)
		{
			return ((Base)this.bases.get(selectedBase)).addSCVtoQueue();
		}

		return false;
	}

	public boolean upgradeBase(String name)
	{
		int base = -1;
		int i = 0;
		while ((i < this.bases.size()) && (base < 0)) {
			if ((((Base)this.bases.get(i)).isComplete()) && (((Base)this.bases.get(i)).getName().equals("CommandCenter")) && (((Base)this.bases.get(i)).getQueueLength() == 0)) {
				base = i;
			}
			i++;
		}
		if (base >= 0)
		{
			return ((Base)this.bases.get(base)).upgrade(name);
		}

		return false;
	}

	public boolean makeBuilding(String name, String addon)
	{
		boolean addonFound = false;
		int c = -1;

		int i = 0;
		if (addon.equals("none")) {
			if (this.objects.add(new SCStructure(this, name))) {
				spendMinerals(getMineralCost(name));
				spendGas(getGasCost(name));
				return true;
			}
			return false;
		}

		do
		{
			if ((this.objects.get(i) instanceof SCAddon)) {
				SCAddon a = (SCAddon)this.objects.get(i);
				if ((a.getName().equals(addon)) && (a.getQueueLength() == 0) && (a.isAvailable()) && (a.isComplete()) && (a.getAttachedTo().equals(""))) {
					c = i;
					addonFound = true;
				}
			}
			i++;
		}
		while ((i < this.objects.size()) && (!addonFound));

		if (addonFound) {
			SCStructure s = new SCStructure(this, name);
			s.setAddonName(addon);
			if (this.objects.add(s)) {
				spendMinerals(getMineralCost(name));
				spendGas(getGasCost(name));
				if (((SCObject)this.objects.get(c)).attach(name)) {
					return true;
				}
				System.out.println(printTime() + "   <Model:makeBuilding> Attach addon failed");
				return false;
			}

			return false;
		}

		System.out.println(printTime() + "   <Model:makeBuilding> AddonFound = " + addonFound);
		return false;
	}

	public boolean buildRefinery()
	{
		boolean f = false;
		int b = 0;
		while ((!f) && (b < this.bases.size())) {
			if (((Base)this.bases.get(b)).freeGeysers() > 0)
				f = true;
			else {
				b++;
			}
		}
		if (!f) {
			System.out.println(printTime() + "   <Model:buildRefinery> Couldn't find a base with free geysers");
		}
		else {
			return ((Base)this.bases.get(b)).addRefinery();
		}

		return f;
	}

	public boolean mule()
	{
		int most = 0;
		int baseFrom = -1;
		int baseTo = -1;
		for (int i = 0; i < this.bases.size(); i++) {
			if (((Base)this.bases.get(i)).getEnergy() > most) {
				most = ((Base)this.bases.get(i)).getEnergy();
				baseFrom = i;
			}
		}
		most = 0;
		for (int i = 0; i < this.bases.size(); i++) {
			if ((((Base)this.bases.get(i)).isComplete()) && (((Base)this.bases.get(i)).remainingMinerals() > most)) {
				most = ((Base)this.bases.get(i)).remainingMinerals();
				baseTo = i;
			}
		}
		if ((baseFrom >= 0) && (baseTo >= 0)) {
			if (((Base)this.bases.get(baseTo)).addMule())
			{
				((Base)this.bases.get(baseFrom)).useEnergy(50);
				return true;
			}

			return false;
		}

		return false;
	}

	public boolean calldownSupply()
	{
		int most = 0;
		int baseFrom = -1;
		int depot = -1;
		boolean found = false;
		for (int i = 0; i < this.bases.size(); i++) {
			if (((Base)this.bases.get(i)).getEnergy() > most) {
				most = ((Base)this.bases.get(i)).getEnergy();
				baseFrom = i;
			}
		}
		int i = 0;
		while ((i < this.objects.size()) && (!found)) {
			if (((SCObject)this.objects.get(i)).getName().equals("SupplyDepot")) {
				SCStructure s = (SCStructure)this.objects.get(i);
				if (s.getSupply() <= 8) {
					depot = i;
					found = true;
				}
			}
		}
		if (found) {
			((SCStructure)this.objects.get(depot)).setSupply(16);
			addSupply(8);
			((Base)this.bases.get(baseFrom)).useEnergy(50);
			return true;
		}
		return false;
	}

	public boolean scout(String option)
	{
		int duration = Integer.parseInt(option);
		int most = 0;
		int base = -1;
		for (int i = 0; i < this.bases.size(); i++) {
			if (((Base)this.bases.get(i)).scvCount() > 0) {
				most = ((Base)this.bases.get(i)).scvCount();
				base = i;
			}
		}
		if (base >= 0) {
			((Base)this.bases.get(base)).setSCVScouting(duration);
			return true;
		}
		return false;
	}

	public boolean OCAction(OCAction.OCType action){
		switch(action){
		case MULE :
			return mule();
		case CALLDOWNSUPPLY :
			return calldownSupply();
		case SCAN :
			return scan();
		}
		System.out.println("that isn't an action smokey");
		return false;
	}
	public boolean scan()
	{
		int most = 0;
		int baseFrom = -1;
		for (int i = 0; i < this.bases.size(); i++) {
			if (((Base)this.bases.get(i)).getEnergy() > most) {
				most = ((Base)this.bases.get(i)).getEnergy();
				baseFrom = i;
			}
		}
		if (baseFrom >= 0) {
			((Base)this.bases.get(baseFrom)).useEnergy(50);
			return true;
		}
		return false;
	}

	public boolean isFreeBase()
	{
		boolean free = false;
		for (int i = 0; i < this.bases.size(); i++) {
			if ((((Base)this.bases.get(i)).isComplete()) && (((Base)this.bases.get(i)).getQueueLength() == 0)) {
				free = true;
			}
		}
		return free;
	}
	public boolean hasEnergy(int e) {
		boolean energy = false;
		for (int i = 0; i < this.bases.size(); i++) {
			if (((Base)this.bases.get(i)).getEnergy() >= e) {
				energy = true;
			}
		}
		return energy;
	}
	public boolean isObjectComplete(String name) {
		boolean ready = false;
		for (int i = 0; i < this.objects.size(); i++) {
			if ((((SCObject)this.objects.get(i)).isComplete()) && (((SCObject)this.objects.get(i)).getName().equals(name))) {
				ready = true;
			}
		}
		return ready;
	}
	public boolean alreadyStarted(String name) {
		boolean found = false;
		int i = 0;
		while ((i < this.objects.size()) && (!found)) {
			if (((SCObject)this.objects.get(i)).getName().equals(name))
				found = true;
			else if (((this.objects.get(i) instanceof SCStructure)) && 
					(((SCStructure)this.objects.get(i)).getConstructingName().equals(name))) {
				found = true;
			}

			i++;
		}
		return found;
	}

	public boolean hasAddon(String build, String tech)
	{
		boolean foundBuilding = false;
		boolean foundTech = false;

		int i = 0;
		while (((!foundTech) || (!foundBuilding)) && (i < this.objects.size())) {
			if (((SCObject)this.objects.get(i)).getName().equals(build)) {
				SCStructure building = (SCStructure)this.objects.get(i);
				if (building.getAddonName().equals(tech)) {
					foundBuilding = true;
				}
			}

			if (((SCObject)this.objects.get(i)).getName().equals(tech)) {
				SCAddon addon = (SCAddon)this.objects.get(i);
				if (addon.getAttachedTo().equals(build)) {
					foundTech = true;
				}
			}

			i++;
		}

		return (foundBuilding) && (foundTech);
	}

	public boolean isAvailable(String name)
	{
		boolean ready = false;
		int i = 0;
		while ((i < this.objects.size()) && (!ready)) {
			if ((((SCObject)this.objects.get(i)).isComplete()) && (((SCObject)this.objects.get(i)).getName().equals(name)) && (((SCObject)this.objects.get(i)).isAvailable())) {
				ready = true;
			}
			i++;
		}
		return ready;
	}

	public boolean setSCVBuilding(int duration) {
		int most = 0;
		int base = -1;
		for (int i = 0; i < this.bases.size(); i++) {
			if (((Base)this.bases.get(i)).scvCount() > most) {
				most = ((Base)this.bases.get(i)).scvCount();
				base = i;
			}
		}
		if (base >= 0)
		{
			return ((Base)this.bases.get(base)).setSCVBuilding(duration);
		}

		return false;
	}

	public boolean addUnitToQueue(String name)
	{
		String build = getBuild(name);
		String tech = getTech(name);
		int i = 0;
		int building = -1;
		boolean found = false;
		while ((!found) && (i < this.objects.size())) {
			if ((((SCObject)this.objects.get(i)).getName().equals(build)) && (((SCObject)this.objects.get(i)).isComplete()) && (((SCObject)this.objects.get(i)).isAvailable())) {
				SCStructure s = (SCStructure)this.objects.get(i);
				if (s.getQueueLength() == 0) {
					if (tech != null) {
						if (s.getAddonName().equals(tech)) {
							found = true;
							building = i;
						}
					} else {
						found = true;
						building = i;
					}
				}
			}
			i++;
		}
		if (found)
		{
			return ((SCStructure)this.objects.get(building)).addObjectToQueue(name);
		}

		return false;
	}

	public boolean addResearch(String name)
	{
		String build = getBuild(name);
		String tech = getTech(name);
		boolean buildingFound = false;
		boolean addonFound = false;
		int i = 0;
		int b = -1;
		int c = -1;
		int size = this.objects.size();
		if (tech == null)
		{
			while (i < size) {
				if ((((SCObject)this.objects.get(i)).getName().equals(build)) && (((SCObject)this.objects.get(i)).isAvailable())) {
					buildingFound = true;
					b = i;
				}
				i++;
			}
			if (buildingFound)
			{
				return ((SCStructure)this.objects.get(b)).addObjectToQueue(name);
			}

		}
		else
		{
			do
			{
				if ((((SCObject)this.objects.get(i)).getName().equals(build)) && (((SCObject)this.objects.get(i)).isComplete()) && 
						(((SCStructure)this.objects.get(i)).getAddonName().equals(tech))) {
					buildingFound = true;
					b = i;
				}

				if ((((SCObject)this.objects.get(i)).getName().equals(tech)) && (((SCObject)this.objects.get(i)).isAvailable()) && 
						(((SCAddon)this.objects.get(i)).getAttachedTo().equals(build))) {
					addonFound = true;
					c = i;
				}

				i++;
			}
			while (i < size);

			if ((buildingFound) && (addonFound))
			{
				return ((SCAddon)this.objects.get(c)).addObjectToQueue(name);
			}

		}

		return true;
	}

	public int getmX1() {
		return this.mX1;
	}

	public void setmX1(int mX1) {
		this.mX1 = mX1;
	}

	public int getmY1() {
		return this.mY1;
	}

	public void setmY1(int mY1) {
		this.mY1 = mY1;
	}

	public int getmX2() {
		return this.mX2;
	}

	public void setmX2(int mX2) {
		this.mX2 = mX2;
	}

	public int getmY2() {
		return this.mY2;
	}

	public void setmY2(int mY2) {
		this.mY2 = mY2;
	}

	public boolean buildBase() {
		spendMinerals(getMineralCost("CommandCenter"));
		Base b = new Base(this);
		this.bases.add(b);
		return true;
	}

	public int completedBases() {
		int b = 0;
		for (int i = 0; i < this.bases.size(); i++) {
			if (((Base)this.bases.get(i)).isComplete()) {
				b++;
			}
		}
		return b;
	}

	public boolean buildAddon(String name, String building) {
		int i = 0;
		int b = -1;
		boolean found = false;

		while ((i < this.objects.size()) && (!found)) {
			if ((this.objects.get(i) instanceof SCStructure)) {
				SCStructure s = (SCStructure)this.objects.get(i);
				if ((s.getName().equals(building)) && (s.getQueueLength() == 0) && (s.isAvailable()) && (s.getAddonName().equals(""))) {
					b = i;
					found = true;
				}
			}
			i++;
		}
		if (found) {
			SCStructure s = (SCStructure)this.objects.get(b);
			s.buildAddon(name);
			SCAddon a = new SCAddon(this, name);
			a.setAttachedTo(building);
			this.objects.add(a);
		}

		return found;
	}

	public boolean freeAddonExists(String addon) {
		boolean addonFound = false;

		int i = 0;
		while ((i < this.objects.size()) && (!addonFound)) {
			if ((this.objects.get(i) instanceof SCAddon)) {
				SCAddon a = (SCAddon)this.objects.get(i);
				if ((a.getName().equals(addon)) && (a.getQueueLength() == 0) && 
						(a.isComplete()) && (a.getAttachedTo().equals(""))) {
					addonFound = true;
				}
			}

			i++;
		}
		return addonFound;
	}

	public boolean lift(String name, String addon)
	{
		int i = 0;
		int b = -1;
		int c = -1;
		boolean buildingFound = false;
		boolean addonFound = false;

		if (addon.equals("none")) {
			while ((i < this.objects.size()) && (!buildingFound)) {
				if ((this.objects.get(i) instanceof SCStructure)) {
					SCStructure s = (SCStructure)this.objects.get(i);
					if ((s.getName().equals(name)) && (s.isComplete()) && (s.getQueueLength() == 0) && 
							(s.isAvailableToLift()) && (s.getAddonName().equals("")) && (!s.isLifted())) {
						b = i;
						buildingFound = true;
					}
				}
				i++;
			}

			if (buildingFound) {
				if (((SCObject)this.objects.get(b)).lift()) {
					return true;
				}
				System.out.println(printTime() + "   <Model:lift> Lift structure failed");
				return false;
			}

			System.out.println(printTime() + "   <Model:lift> BuildingFound = " + buildingFound + " AddonFound = " + addonFound);
			return false;
		}
		do
		{
			if ((this.objects.get(i) instanceof SCStructure)) {
				SCStructure s = (SCStructure)this.objects.get(i);
				if ((s.getName().equals(name)) && (s.isComplete()) && (s.getQueueLength() == 0) && 
						(s.isAvailableToLift()) && (s.getAddonName().equals(addon)) && (!s.isLifted())) {
					b = i;
					buildingFound = true;
				}
			}
			if ((this.objects.get(i) instanceof SCAddon)) {
				SCAddon a = (SCAddon)this.objects.get(i);
				if ((a.getName().equals(addon)) && (a.getQueueLength() == 0) && (a.isAvailable()) && 
						(a.isComplete()) && (a.getAttachedTo().equals(name))) {
					c = i;
					addonFound = true;
				}
			}

			i++;
		}
		while ((i < this.objects.size()) && ((!buildingFound) || (!addonFound)));

		if ((buildingFound) && (addonFound)) {
			if (((SCObject)this.objects.get(b)).lift()) {
				if (((SCObject)this.objects.get(c)).detach()) {
					return true;
				}
				System.out.println(printTime() + "   <Model:lift> Detach addon failed");
				return false;
			}

			System.out.println(printTime() + "   <Model:lift> Lift structure failed");
			return false;
		}

		System.out.println(printTime() + "   <Model:lift> BuildingFound = " + buildingFound + " AddonFound = " + addonFound);
		return false;
	}

	public boolean land(String name, String addon)
	{
		int i = 0;
		int b = -1;
		int c = -1;
		boolean buildingFound = false;
		boolean addonFound = false;

		if (addon.equals("none")) {
			while ((i < this.objects.size()) && (!buildingFound)) {
				if ((this.objects.get(i) instanceof SCStructure)) {
					SCStructure s = (SCStructure)this.objects.get(i);
					if ((s.getName().equals(name)) && (s.isComplete()) && (s.getQueueLength() == 0) && 
							(s.getAddonName().equals("")) && (s.isLifted())) {
						b = i;
						buildingFound = true;
					}
				}
				i++;
			}
			if (buildingFound) {
				if (((SCObject)this.objects.get(b)).land(addon)) {
					return true;
				}
				System.out.println(printTime() + "   <Model:land> Land building failed");
				return false;
			}

			System.out.println(printTime() + "   <Model:land> BuildingFound = " + buildingFound);
			return false;
		}

		do
		{
			if ((this.objects.get(i) instanceof SCStructure)) {
				SCStructure s = (SCStructure)this.objects.get(i);
				if ((s.getName().equals(name)) && (s.isComplete()) && (s.getQueueLength() == 0) && 
						(s.getAddonName().equals("")) && (s.isLifted())) {
					b = i;
					buildingFound = true;
				}
			}
			if ((this.objects.get(i) instanceof SCAddon)) {
				SCAddon a = (SCAddon)this.objects.get(i);
				if ((a.getName().equals(addon)) && (a.getQueueLength() == 0) && (a.isAvailable()) && 
						(a.isComplete()) && (a.getAttachedTo().equals(""))) {
					c = i;
					addonFound = true;
				}
			}

			i++;
		}
		while ((i < this.objects.size()) && ((!buildingFound) || (!addonFound)));

		if ((buildingFound) && (addonFound)) {
			if (((SCObject)this.objects.get(b)).land(addon)) {
				if (((SCObject)this.objects.get(c)).attach(name)) {
					return true;
				}
				System.out.println(printTime() + "   <Model:lift> Attach addon failed");
				return false;
			}

			System.out.println(printTime() + "   <Model:lift> Land structure failed");
			return false;
		}

		System.out.println(printTime() + "   <Model:lift> BuildingFound = " + buildingFound + " AddonFound = " + addonFound);
		return false;
	}

	public boolean maynard()
	{
		int scvsFrom = 0;
		int scvsTo = 200;
		int baseFrom = -1;
		int baseTo = -1;
		int moving = -1;
		for (int b = 0; b < this.bases.size(); b++) {
			if (((Base)this.bases.get(b)).scvCount() + ((Base)this.bases.get(b)).idleSCVCount() > scvsFrom) {
				scvsFrom = ((Base)this.bases.get(b)).scvCount() + ((Base)this.bases.get(b)).idleSCVCount();
				baseFrom = b;
			}
			if (((Base)this.bases.get(b)).scvCount() + ((Base)this.bases.get(b)).idleSCVCount() < scvsTo) {
				scvsTo = ((Base)this.bases.get(b)).scvCount() + ((Base)this.bases.get(b)).idleSCVCount();
				baseTo = b;
			}

		}

		if ((baseFrom >= 0) && (baseTo >= 0) && (baseFrom != baseTo)) {
			moving = (int)Math.round((scvsFrom - scvsTo) * 0.5D);

			for (int i = 0; i < moving; i++) {
				SCV s = ((Base)this.bases.get(baseFrom)).transferSCV();
				((Base)this.bases.get(baseTo)).addSCV(s);
			}
			return true;
		}
		return false;
	}

	public void startMarquee(int x, int y)
	{
		this.mX1 = x;
		this.mY1 = y;
	}

	public void updateMarquee(int x, int y) {
		this.mX2 = x;
		this.mY2 = y;
	}

	public void endMarquee(int x, int y)
	{
		this.mX1 -= this.border;
		this.mY1 -= this.border;
		this.mY1 /= this.spacing;
		this.mX1 = (int)(this.mX1 / this.scale);
		this.mX1 += this.scroll;

		this.mX2 -= this.border;
		this.mY2 -= this.border;
		this.mY2 /= this.spacing;
		this.mX2 = (int)(this.mX2 / this.scale);
		this.mX2 += this.scroll;

		if (this.mX2 < this.mX1) {
			int tmp = this.mX2;
			this.mX2 = this.mX1;
			this.mX1 = tmp;
		}
		if (this.mY2 < this.mY1) {
			int tmp = this.mY2;
			this.mY2 = this.mY1;
			this.mY1 = tmp;
		}
		if ((this.mX1 >= 0) && (this.mX2 >= 0) && (this.mY1 >= 0) && (this.mY2 >= 0)) {
			for (int i = 0; i < this.actions.size(); i++) {
				int ax = ((SCAction)this.actions.get(i)).getStartTime();
				int ay = ((SCAction)this.actions.get(i)).getY();
				int aend = ax + ((SCAction)this.actions.get(i)).getDuration();
				if ((ax > this.mX1) && (aend < this.mX2) && (ay >= this.mY1) && (ay <= this.mY2)) {
					((SCAction)this.actions.get(i)).select();
				}
			}
		}

		this.mX1 = -1;
		this.mY1 = -1;
		this.mX2 = -1;
		this.mY2 = -1;
	}

	public void selectMultipleAction(int x, int y)
	{
		x -= this.border;
		y -= this.border;
		y /= this.spacing;
		x = (int)(x / this.scale);
		x += this.scroll;

		int a = 0;
		int size = this.actions.size();
		boolean f = false;
		while ((!f) && (a < size)) {
			if ((x >= ((SCAction)this.actions.get(a)).getStartTime()) && 
					(x < ((SCAction)this.actions.get(a)).getStartTime() + ((SCAction)this.actions.get(a)).getDuration()) && 
					(y >= ((SCAction)this.actions.get(a)).getY()) && 
					(y < ((SCAction)this.actions.get(a)).getY() + 1)) {
				System.out.println("select toggled");
				((SCAction)this.actions.get(a)).toggleSelect();
				f = true;
			}
			a++;
		}
	}
	public void selectNoActions() {
		for (int i = 0; i < this.actions.size(); i++) {
			((SCAction)this.actions.get(i)).deselect();
		}

	}
	public void selectAllActions(int x, int y) {
		x -= this.border;
		y -= this.border;
		y /= this.spacing;
		x = (int)(x / this.scale);
		x += this.scroll;
		for (int i = 0; i < this.actions.size(); i++) {
			((SCAction)this.actions.get(i)).deselect();
		}
		int a = 0;
		boolean f = false;
		String name = "";
		while ((!f) && (a < this.actions.size()))
		{
			if ((x >= ((SCAction)this.actions.get(a)).getStartTime()) && 
					(x < ((SCAction)this.actions.get(a)).getStartTime() + ((SCAction)this.actions.get(a)).getDuration()) && 
					(y >= ((SCAction)this.actions.get(a)).getY()) && 
					(y < ((SCAction)this.actions.get(a)).getY() + 1)) {
				name = ((SCAction)this.actions.get(a)).getName();
				((SCAction)this.actions.get(a)).select();
				f = true;
			}

			a++;
		}
		for (int i = 0; i < this.actions.size(); i++)
			if (((SCAction)this.actions.get(i)).getName().equals(name))
				((SCAction)this.actions.get(i)).select();
	}

	public void rightClick(int x, int y)
	{
		x -= this.border;
		y -= this.border;
		y /= this.spacing;
		x = (int)(x / this.scale);
		x += this.scroll;
		int a = 0;
		boolean f = false;
		int size = this.actions.size();
		while ((!f) && (a < size)) {
			if ((x >= ((SCAction)this.actions.get(a)).getStartTime()) && 
					(x < ((SCAction)this.actions.get(a)).getStartTime() + ((SCAction)this.actions.get(a)).getDuration()) && 
					(y >= ((SCAction)this.actions.get(a)).getY()) && (y < ((SCAction)this.actions.get(a)).getY() + 1)) {
				((SCAction)this.actions.get(a)).setPopup(true);
				((SCAction)this.actions.get(a)).select();
				f = true;
			}

			a++;
		}
	}

	public void selectAction(int x, int y)
	{
		int x2 = x;
		int y2 = y;

		x -= this.border;
		y -= this.border;
		y /= this.spacing;
		x = (int)(x / this.scale);
		x += this.scroll;

		x2 -= this.border;
		x2 = (int)(x2 + this.scale * this.scroll);

		y2 -= this.border;
		for (int i = 0; i < this.actions.size(); i++) {
			if ((!((SCAction)this.actions.get(i)).getPopup()) || 
					(((SCAction)this.actions.get(i)).getOptionsSize() <= 0))
				continue;
			SCAction a = (SCAction)this.actions.get(i);
			int x3 = (int)(x2 - this.scale * (a.getStartTime() + a.getDuration()));
			int y3 = y2 - ((SCAction)this.actions.get(i)).getY() * this.spacing - this.thickness;
			y3 /= 14;

			if ((x3 > 0) && (x3 < 60) && (y3 < ((SCAction)this.actions.get(i)).getOptionsSize())) {
				((SCAction)this.actions.get(i)).setOption(y3);
			}

		}

		y2 -= y * this.spacing;
		y2 -= this.thickness;
		for (int i = 0; i < this.actions.size(); i++) {
			((SCAction)this.actions.get(i)).setPopup(false);
			if (((SCAction)this.actions.get(i)).getOptionsSize() > 0) {
				SCAction a = (SCAction)this.actions.get(i);
				if (a.isSelected()) {
					int t = (int)(this.scale * (a.getStartTime() + a.getDuration()) - x2);

					if ((t > 0) && (t < 14) && (y2 > 0) && (y2 < 14)) {
						a.setPopup(true);
					}
				}
			}
			((SCAction)this.actions.get(i)).deselect();
		}
		int a = 0;
		int size = this.actions.size();
		boolean f = false;
		while ((!f) && (a < size)) {
			if ((x >= ((SCAction)this.actions.get(a)).getStartTime()) && 
					(x < ((SCAction)this.actions.get(a)).getStartTime() + ((SCAction)this.actions.get(a)).getDuration()) && 
					(y >= ((SCAction)this.actions.get(a)).getY()) && 
					(y < ((SCAction)this.actions.get(a)).getY() + 1)) {
				((SCAction)this.actions.get(a)).select();
				f = true;
			}

			a++;
		}
	}

	public void moveSelected(int x, int y)
	{
		for (int i = 0; i < this.actions.size(); i++)
			if (((SCAction)this.actions.get(i)).isSelected()) {
				((SCAction)this.actions.get(i)).addStartTime(x);
				((SCAction)this.actions.get(i)).moveY(y);
			}
	}

	public void moveSelectedToEarliest()
	{
		int size = this.actions.size();
		for (int i = 0; i < size; i++)
			if (((SCAction)this.actions.get(i)).isSelected()) {
				SCAction action = (SCAction)this.actions.get(i);
				if (action.isComplete()) {
					do {
						action.addStartTime(-1);
						reset();
						play();
						if (!action.isComplete())
							action.addStartTime(1);
						if (action.getStartTime() <= 0) break; 
					}while (action.isComplete());
				}
				else
				{
					int r = action.getStartTime();
					while ((action.getStartTime() < this.maxTime) && (!action.isComplete())) {
						action.addStartTime(1);
						reset();
						play();
					}
					if (!action.isComplete()) {
						action.setStartTime(r);
					}
				}

				int x = action.getStartTime();
				int y = action.getY();
				int dur = getTime(action.getName());
				int end = x + dur;
				boolean space = false;

				while (!space) {
					space = true;
					for (int j = 0; j < size; j++) {
						if (i != j) {
							SCAction action2 = (SCAction)this.actions.get(j);
							if (action2.getY() == y) {
								int x2 = action2.getStartTime();
								int end2 = action2.getStartTime() + action2.getDuration();
								if ((x <= x2) && (x2 < end) && (end <= end2))
								{
									space = false;
								} else if ((x2 <= x) && (x < end2) && (end2 <= end))
								{
									space = false;
								} else if ((x < x2) && (end2 < end))
								{
									space = false; } else {
										if ((x2 >= x) || (end >= end2))
											continue;
										space = false;
									}
							}

						}

					}

					if (!space) {
						y++;
					}
				}
				if (space)
					action.setY(y);
			}
	}

	public String setTotalsText()
	{
		ArrayList objectNames = new ArrayList();

		int n = 13;

		int[] counts = new int[n];
		for (int i = 0; i < n; i++) {
			counts[i] = 0;
		}
		for (int i = 0; i < this.objects.size(); i++) {
			if ((((SCObject)this.objects.get(i)).getName().equals("Barracks")) || 
					(((SCObject)this.objects.get(i)).getName().equals("Factory")) || 
					(((SCObject)this.objects.get(i)).getName().equals("Starport"))) {
				SCStructure b = (SCStructure)this.objects.get(i);
				objectNames.add(b.getName() + ":" + b.getAddonName()); } else {
					if ((((SCObject)this.objects.get(i)).getName().equals("TechLab")) || 
							(((SCObject)this.objects.get(i)).getName().equals("Reactor"))) {
						continue;
					}
					objectNames.add(((SCObject)this.objects.get(i)).getName());
				}
		}

		for (int i = 0; i < objectNames.size(); i++) {
			String name = (String)objectNames.get(i);
			if (name.equals("Marine"))
				counts[0] += 1;
			else if (name.equals("Marauder"))
				counts[1] += 1;
			else if (name.equals("Ghost"))
				counts[2] += 1;
			else if (name.equals("Reaper"))
				counts[3] += 1;
			else if (name.equals("Hellion"))
				counts[4] += 1;
			else if (name.equals("SiegeTank"))
				counts[5] += 1;
			else if (name.equals("Thor"))
				counts[6] += 1;
			else if (name.equals("Viking"))
				counts[7] += 1;
			else if (name.equals("Medivac"))
				counts[8] += 1;
			else if (name.equals("Banshee"))
				counts[9] += 1;
			else if (name.equals("Raven"))
				counts[10] += 1;
			else if (name.equals("Battlecruiser"))
				counts[11] += 1;
			else if (name.equals("SupplyDepot")) {
				counts[12] += 1;
			}
		}
		String s = "";
		s = s + "Supply        : " + this.food + "/" + this.supply + "\n";
		s = s + "Total Minerals: " + this.totalMineralsMined + "\n";
		s = s + "Total Gas     : " + this.totalGasMined + "\n";
		s = s + "\n";

		for (int i = 0; i < this.bases.size(); i++) {
			if (i == 0)
				s = s + ((Base)this.bases.get(i)).scvCount() + "&" + ((Base)this.bases.get(i)).scvCountGas() + 
				"(" + ((Base)this.bases.get(i)).idleSCVCount() + ") SCVs at main\n";
			else if (i == 1)
				s = s + ((Base)this.bases.get(i)).scvCount() + "&" + ((Base)this.bases.get(i)).scvCountGas() + 
				"(" + ((Base)this.bases.get(i)).idleSCVCount() + ") SCVs at natural\n";
			else {
				s = s + ((Base)this.bases.get(i)).scvCount() + "&" + ((Base)this.bases.get(i)).scvCountGas() + 
				"(" + ((Base)this.bases.get(i)).idleSCVCount() + ") SCVs at base " + (i + 1) + "\n";
			}
		}
		s = s + "\n";
		if (counts[0] > 0) s = s + counts[0] + " Marines\n";
		if (counts[1] > 0) s = s + counts[1] + " Marauders\n";
		if (counts[2] > 0) s = s + counts[2] + " Ghosts\n";
		if (counts[3] > 0) s = s + counts[3] + " Reapers\n";
		if (counts[4] > 0) s = s + counts[4] + " Hellions\n";
		if (counts[5] > 0) s = s + counts[5] + " Siege Tanks\n";
		if (counts[6] > 0) s = s + counts[6] + " Thors\n";
		if (counts[7] > 0) s = s + counts[7] + " Vikings\n";
		if (counts[8] > 0) s = s + counts[8] + " Medivacs\n";
		if (counts[9] > 0) s = s + counts[9] + " Banshees\n";
		if (counts[10] > 0) s = s + counts[10] + " Ravens\n";
		if (counts[11] > 0) s = s + counts[11] + " Battlecruisers\n";
		if (counts[12] > 0) s = s + counts[12] + " Supply Depots\n";

		s = s + "\n";
		for (int i = 0; i < objectNames.size(); i++) {
			String name = (String)objectNames.get(i);
			if ((name.equals("Marine")) || (name.equals("Marauder")) || (name.equals("Ghost")) || 
					(name.equals("Reaper")) || (name.equals("Hellion")) || 
					(name.equals("SiegeTank")) || (name.equals("Thor")) || 
					(name.equals("Viking")) || (name.equals("Medivac")) || 
					(name.equals("Banshee")) || (name.equals("Raven")) || 
					(name.equals("Battlecruiser")) || (name.equals("SupplyDepot"))) continue;
			s = s + name;
			s = s + "\n";
		}

		return s;
	}

	public int freeGeysers()
	{
		int g = 0;
		for (int i = 0; i < this.bases.size(); i++) {
			g += ((Base)this.bases.get(i)).freeGeysers();
		}
		return g;
	}

	public int freeRefineries() {
		int r = 0;
		for (int i = 0; i < this.bases.size(); i++) {
			r += ((Base)this.bases.get(i)).freeRefineries();
		}

		return r;
	}

	public int scvsOnGas() {
		int s = 0;
		for (int i = 0; i < this.bases.size(); i++) {
			s += ((Base)this.bases.get(i)).scvCountGas();
		}
		return s;
	}

	public void addUnit(SCObject s)
	{
		this.objects.add(s);
	}

	public int actionCount() {
		return this.actions.size();
	}

	public int getSupply()
	{
		return this.supply;
	}

	public int getFood() {
		return this.food;
	}

	public void addSupply(int s)
	{
		this.supply += s;
	}
	public void addFood(int f) {
		this.food += f;
	}
	public int getMinerals() {
		return this.minerals;
	}
	public int getGas() {
		return this.gas;
	}

	public String[] getUnitOptions() {
		String[] s = { "SCV", "Mule", "TransferToGas", "TransferOffGas", "Maynard", "Marine", "Marauder", "Reaper", "Ghost", "Hellion", 
				"SiegeTank", "Thor", "Viking", "Medivac", "Banshee", "Battlecruiser", "Raven", "Scout" };
		return s;
	}

	public String[] getBuildingOptions() {
		String[] s = { "SupplyDepot", "Refinery", "Barracks", "OrbitalCommand", "Factory", 
				"Starport", "TechLab", "Reactor", "LiftBarracks", "LiftFactory", 
				"LiftStarport", "LandBarracks", "LandFactory", "LandStarport", "CommandCenter", "CalldownSupply", 
				"Bunker", "EngineeringBay", "Armory", "GhostAcademy", "MissileTurret", 
				"PlanetaryFortress", "FusionCore", "SensorTower", "Scan" };
		return s;
	}

	public String[] getResearchOptions() {
		String[] s = { "StimPack", 
				"CombatShield", 
				"ConcussiveShells", 
				"NitroPack", 
				"InfernalPreigniter", 
				"SiegeTech", 
				"250mmStrikeCannons", 
				"CloakingField", 
				"SeekerMissile", 
				"CaduceusReactor", 
				"CorvidReactor", 
				"BehemothReactor", 
				"PersonalCloaking", 
				"MoebiusReactor", 
				"Nuke", 
				"InfantryWeaponsLevel1", 
				"InfantryWeaponsLevel2", 
				"InfantryWeaponsLevel3", 
				"InfantryArmorLevel1", 
				"InfantryArmorLevel2", 
				"InfantryArmorLevel3", 
				"VehicleWeaponsLevel1", 
				"VehicleWeaponsLevel2", 
				"VehicleWeaponsLevel3", 
				"VehicleArmorLevel1", 
				"VehicleArmorLevel2", 
				"VehicleArmorLevel3", 
				"ShipWeaponsLevel1", 
				"ShipWeaponsLevel2", 
				"ShipWeaponsLevel3", 
				"ShipArmorLevel1", 
				"ShipArmorLevel2", 
				"ShipArmorLevel3", 
				"WeaponRefit", 
				"NeosteelFrame", 
				"BuildingArmor", 
				"DurableMaterials", 
		"HiSecAutoTracking" };

		return s;
	}

	public int getMineralCost(String n) {
		int i = 0;
		try {
			i = ((Integer)this.mineralCost.get(n)).intValue();
		}
		catch (NullPointerException e) {
			i = 0;
		}
		return i;
	}

	public int getGasCost(String n) {
		int i = 0;
		try {
			i = ((Integer)this.gasCost.get(n)).intValue();
		}
		catch (NullPointerException e) {
			i = 0;
		}
		return i;
	}

	public int getTime(String n) {
		int i = 0;
		try {
			i = ((Integer)this.times.get(n)).intValue();
		}
		catch (NullPointerException e) {
			i = 0;
		}
		return i;
	}
	public String getPrereq(String n) {
		String s;
		try {
			s = (String)this.prereqs.get(n);
		}
		catch (NullPointerException e)
		{
			s = null;
		}

		return s;
	}
	public String getBuild(String n) {
		String s;
		try { s = (String)this.build.get(n);
		}
		catch (NullPointerException e)
		{
			s = null;
		}
		return s;
	}
	public String getTech(String n) {
		String s;
		try { s = (String)this.tech.get(n);
		}
		catch (NullPointerException e)
		{
			s = null;
		}
		return s;
	}
	public int getFood(String n) {
		int i = 0;
		try {
			i = ((Integer)this.foods.get(n)).intValue();
		}
		catch (NullPointerException e) {
			i = 0;
		}
		return i;
	}
	public int getSupply(String n) {
		int i = 0;
		try {
			i = ((Integer)this.supplies.get(n)).intValue();
		}
		catch (NullPointerException e) {
			i = 0;
		}
		return i;
	}

	public void addMinerals(int c)
	{
		this.minerals += c;
		this.totalMineralsMined += c;
	}

	public void spendMinerals(int c) {
		this.minerals -= c;
	}

	public int getTotalMineralsMined() {
		return this.totalMineralsMined;
	}

	public void setTotalMineralsMined(int totalMineralsMined) {
		this.totalMineralsMined = totalMineralsMined;
	}

	public int getTotalGasMined() {
		return this.totalGasMined;
	}

	public void setTotalGasMined(int totalGasMined) {
		this.totalGasMined = totalGasMined;
	}
	public SCAction getAction(int i) {
		return (SCAction)this.actions.get(i);
	}
	public void addGas(int g) {
		this.gas += g;
		this.totalGasMined += g;
	}
	public int getMineralGraph(int t) {
		return this.mineralGraph[t];
	}

	public int getGasGraph(int t) {
		return this.gasGraph[t];
	}

	public int getEnergyGraph(int t) {
		return this.energyGraph[t];
	}

	public void spendGas(int g) {
		this.gas -= g;
	}

	public double getScale() {
		return this.scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public int getScroll() {
		return this.scroll;
	}

	public void setScroll(int scroll) {
		this.scroll = scroll;
	}

	public int getBorder() {
		return this.border;
	}

	public void setBorder(int border) {
		this.border = border;
	}

	public int getSpacing() {
		return this.spacing;
	}
	public int getThickness() {
		return this.thickness;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public int getMaxTime() {
		return this.maxTime;
	}
	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String printTime()
	{
		int min = this.time / 60;
		String s;
		if (min < 10)
			s = "0" + min;
		else {
			s = ""+min;
		}
		int sec = this.time - 60 * min;
		if (sec < 10)
			s = s + ":0" + sec;
		else {
			s = s + ":" + sec;
		}
		return s;
	}

	public String printTime(int i)
	{
		int min = i / 60;
		String s;
		if (min < 10)
			s = "0" + min;
		else {
			s = ""+min;
		}
		int sec = i - 60 * min;
		if (sec < 10)
			s = s + ":0" + sec;
		else {
			s = s + ":" + sec;
		}
		return s;
	}

	public void deleteAction() {
		for (int i = 0; i < this.actions.size(); i++)
			if (((SCAction)this.actions.get(i)).isSelected()) {
				this.actions.remove(i);
				i--;
			}
	}

	public void changeScale(double d)
	{
		this.scale += d;
		if (this.scale < 1.1D) {
			this.scale = 1.1D;
		}
		if (this.scale > 6.0D)
			this.scale = 6.0D;
	}

	public void scroll(int i)
	{
		this.scroll += i;
		if (this.scroll < 0)
			this.scroll = 0;
	}

	public boolean transferOffGas()
	{
		int g = 0;
		int base = -1;
		for (int i = 0; i < this.bases.size(); i++) {
			if (((Base)this.bases.get(i)).scvCountGas() > g) {
				g = ((Base)this.bases.get(i)).scvCountGas();
				base = i;
			}
		}

		if (base >= 0)
		{
			return ((Base)this.bases.get(base)).transferSCVOffGas();
		}

		return false;
	}

	public boolean transferToGas()
	{
		int f = 3;
		int base = -1;
		for (int i = 0; i < this.bases.size(); i++) {
			if ((((Base)this.bases.get(i)).isComplete()) && (((Base)this.bases.get(i)).freeRefineries() < f) && (((Base)this.bases.get(i)).freeRefineries() > 0)) {
				base = i;
				f = ((Base)this.bases.get(i)).freeRefineries();
			}
		}
		if (base >= 0)
		{
			return ((Base)this.bases.get(base)).transferDroneToGas();
		}

		return false;
	}

	public String printBuild()
	{
		String s = "";
		for (int t = 0; t < this.maxTime; t++) {
			for (int i = 0; i < this.actions.size(); i++) {
				if (((SCAction)this.actions.get(i)).getStartTime() == t) {
					s = s + printTime(t) + " - " + ((SCAction)this.actions.get(i)).getSupplyPoint() + " - " + ((SCAction)this.actions.get(i)).toString() + "\n";
				}
			}
		}
		return s;
	}

	public void selectNext() {
		int i = 0;
		int k = -1;
		while (i < this.actions.size()) {
			if (((SCAction)this.actions.get(i)).isSelected()) {
				k = i;
			}
			i++;
		}
		if ((k >= 0) && (k == this.actions.size() - 1)) {
			((SCAction)this.actions.get(k)).deselect();
			((SCAction)this.actions.get(0)).select();
		} else if (k >= 0) {
			((SCAction)this.actions.get(k)).deselect();
			((SCAction)this.actions.get(k + 1)).select();
		}
	}
}
