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

		private final String label;

		Skills(String s) {
			label = s;
		}

		public String toString(){
			return label;
		}

		public static Skills valueByLabel(String labelText) {
			for (Skills skill : Skills.values()) {
				if (skill.label.equals(labelText)) {
					return skill;
				}
			}
			return null;
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

	public int getStrBonus() {
		return strBonus;
	}

	public void setStrBonus(int strBonus) {
		this.strBonus = strBonus;
	}

	public int getDexBonus() {
		return dexBonus;
	}

	public void setDexBonus(int dexBonus) {
		this.dexBonus = dexBonus;
	}

	public int getConBonus() {
		return conBonus;
	}

	public void setConBonus(int conBonus) {
		this.conBonus = conBonus;
	}

	public int getIntBonus() {
		return intBonus;
	}

	public void setIntBonus(int intBonus) {
		this.intBonus = intBonus;
	}

	public int getWisBonus() {
		return wisBonus;
	}

	public void setWisBonus(int wisBonus) {
		this.wisBonus = wisBonus;
	}

	public int getChaBonus() {
		return chaBonus;
	}

	public void setChaBonus(int chaBonus) {
		this.chaBonus = chaBonus;
	}

	public int[] getSkillBonus() {
		return skillBonus;
	}

	public void setSkillBonusByIndex(int index, int value) {
		this.skillBonus[index] = value;
	}

	public boolean[] getThrowProficiencies() {
		return throwProficiencies;
	}

	public void setThrowProficienciesByIndex(int index, boolean value) {
		this.throwProficiencies[index] = value;
	}

	public int[] getThrowBonus() {
		return throwBonus;
	}

	public void setThrowBonusByIndex(int index, int value) {
		this.throwBonus[index] = value;
	}

	public boolean[] getSkillProficiencies() {
		return skillProficiencies;
	}

	public void setSkillProficienciesByIndex(int index, boolean value) {
		this.skillProficiencies[index] = value;
	}

	public String getAdditionalSpells() {
		return additionalSpells;
	}

	public void setAdditionalSpells(String additionalSpells) {
		this.additionalSpells = additionalSpells;
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

	public Attack[] getAttacks() {
		return attacks;
	}

	public Boolean[][] getDeathSaves() {
		return deathSaves;
	}

	public void setDeathSaves(Boolean[][] deathSaves) {
		this.deathSaves = deathSaves;
	}

	public int getCopper() {
		return copper;
	}

	public void setCopper(int copper) {
		this.copper = copper;
	}

	public int getSilver() {
		return silver;
	}

	public void setSilver(int silver) {
		this.silver = silver;
	}

	public int getElectrum() {
		return electrum;
	}

	public void setElectrum(int electrum) {
		this.electrum = electrum;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getPlatinum() {
		return platinum;
	}

	public void setPlatinum(int platinum) {
		this.platinum = platinum;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
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
	private int strBonus,dexBonus, conBonus, intBonus, wisBonus, chaBonus;

	private boolean[] throwProficiencies = new boolean[6];
	private int[] throwBonus = new int[6];
	private boolean[] skillProficiencies = new boolean[Skills.values().length];
	private int[] skillBonus = new int[Skills.values().length];
	
	// String for additional spells field
	private String additionalSpells;
	
	// Combat
	private int maxHitPoints, currentHitPoints, temporaryHitPoints, armorClass, initiative, speed;

	private Attack[] attacks = new Attack[] { new Attack(), new Attack(), new Attack() };

	// Equipment
	private int copper, silver, electrum, gold, platinum;
	private String equipment;
	
	// Array to store deathsave data.
	private Boolean[][] deathSaves;
	
	public Character()
	{

	}
	
	
	
	
	
}
