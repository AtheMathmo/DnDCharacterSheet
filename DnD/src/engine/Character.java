package engine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Character implements java.io.Serializable {
	
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

	//region Getters and Setters
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Boolean getIsMale() {
		return isMale;
	}

	public void setIsMale(Boolean isMale) {
		this.isMale = isMale;
	}

	public Classes getDndClass() {
		return dndClass;
	}

	public void setDndClass(Classes dndClass) {
		this.dndClass = dndClass;
	}

	public Races getRace() {
		return race;
	}

	public void setRace(Races race) {
		this.race = race;
	}

	public Character.Alignment getAlignment() {
		return Alignment;
	}

	public void setAlignment(Character.Alignment alignment) {
		Alignment = alignment;
	}

	public String getPersonalityTraits() {
		return personalityTraits;
	}

	public void setPersonalityTraits(String personalityTraits) {
		this.personalityTraits = personalityTraits;
	}

	public String getIdeals() {
		return ideals;
	}

	public void setIdeals(String ideals) {
		this.ideals = ideals;
	}

	public String getBonds() {
		return bonds;
	}

	public void setBonds(String bonds) {
		this.bonds = bonds;
	}

	public String getFlaws() {
		return flaws;
	}

	public void setFlaws(String flaws) {
		this.flaws = flaws;
	}

	public String getFeatureTraits() {
		return featureTraits;
	}

	public void setFeatureTraits(String featureTraits) {
		this.featureTraits = featureTraits;
	}

	public int getExperiencePoints() {
		return experiencePoints;
	}

	public void setExperiencePoints(int experiencePoints) {
		this.experiencePoints = experiencePoints;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	public int getConstitution() {
		return constitution;
	}

	public void setConstitution(int constitution) {
		this.constitution = constitution;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int getWisdom() {
		return wisdom;
	}

	public void setWisdom(int wisdom) {
		this.wisdom = wisdom;
	}

	public int getCharisma() {
		return charisma;
	}

	public void setCharisma(int charisma) {
		this.charisma = charisma;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}

	public int getMaxHitPoints() {
		return maxHitPoints;
	}

	public void setMaxHitPoints(int maxHitPoints) {
		this.maxHitPoints = maxHitPoints;
	}

	public int getCurrentHitPoints() {
		return currentHitPoints;
	}

	public void setCurrentHitPoints(int currentHitPoints) {
		this.currentHitPoints = currentHitPoints;
	}

	public int getTemporaryHitPoints() {
		return temporaryHitPoints;
	}

	public void setTemporaryHitPoints(int temporaryHitPoints) {
		this.temporaryHitPoints = temporaryHitPoints;
	}

	public int getArmorClass() {
		return armorClass;
	}

	public void setArmorClass(int armorClass) {
		this.armorClass = armorClass;
	}

	public int getInitiative() {
		return initiative;
	}

	public void setInitiative(int initiative) {
		this.initiative = initiative;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Boolean[][] getDeathSaves() {
		return deathSaves;
	}

	public void setDeathSaves(Boolean[][] deathSaves) {
		this.deathSaves = deathSaves;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}
	//endregion

	// User name
	private String playerName;

	// Character details
	private String characterName;
	private String background;
	private String size;
	private Boolean isMale;
	private Classes dndClass;
	private Races race;
	private Alignment Alignment;

	// Trait boxes
	private String personalityTraits;
	private String ideals;
	private String bonds;
	private String flaws;
	private String featureTraits;

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

	}
	
	
	
	
	
}
