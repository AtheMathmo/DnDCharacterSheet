package engine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		Acrobatics, AnimalHandling, Arcana, Athletics, Deception, History, Insight, Intimidation, Investigation, Medicine, Nature, Perception, Performance, Persuasion, Religion, SleightOfHand, Stealth, Survival
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
