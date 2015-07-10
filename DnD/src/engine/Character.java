package engine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Character {
	
	public enum Races {
		Dwarf, Elf, Halfling, Human, DragonBorn, Gnome, HalfElf, HalfOrc, Tiefling
	}
	
	public enum Classes {
		Barbarian, Bard, Cleric, Druid, Fighter, Monk, Paladin, Ranger, Rogue, Sorcerer, Warlock, Wizard
	}
	
	public enum Alignment {
		LG, NG, CG, LN, N, CN, LE, NE, CE 
	}
	
	public enum Skills {
		Acrobatics ("Acrobatics (Dex)"), AnimalHandling ("AnimalHandling (Wis)"), Arcana ("Arcana (Int)"),
		Athletics ("Athletics (Str)"), Deception ("Deception (Cha)"), History ("History (Int)"), Insight ("Insight (Wis)"),
		Intimidation ("Intimidation (Cha)"), Investigation ("Investigation (Int)"), Medicine ("Medicine (Wis)"),
		Nature ("Nature (Int)"), Perception ("Perception (Wis)"), Performance ("Performance (Cha)"), Persuasion ("Persuasion (Cha)"),
		Religion ("Religion (Int)"), SleightOfHand ("Sleight of Hand (Dex)"), Stealth ("Stealth (Dex)"), Survival ("Survival (Wis)");

		private final String name;

		Skills(String s) {
			name = s;
		}

		public String toString(){
			return name;
		}
	}
	
	// User name
	private String playerName;

	
	// Character details
	private String characterName;
	private String size;
	private Boolean isMale;
	private Classes dndClass;
	private Races race;
	private Alignment Alignment;
	private ArrayList<String> classFeatures;
	private ArrayList<String> raceFeatures;
	// Progress
	private int experiencePoints;
	private int level;
	
	// Attributes
	private int strength, dexterity, constitution, intelligence, wisdom, charisma;
	
	// Map from skills to levels.
	private ArrayList<Spell> spells;
	
	// Combat
	private int maxHitPoints, currentHitPoints, temporaryHitPoints, armorClass, initiative, speed;
	
	// Array to store deathsave data.
	private Boolean[][] deathSaves;
	
	public Character()
	{
		Spell test = new Spell();
		test.setAtkBonus(1);
	}
	
	
	
	
	
}
