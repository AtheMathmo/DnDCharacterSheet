package engine;

public class Spell {
	private String spellName;
	private int atkBonus;
	private String attribute;
	private String damageType;
	
	
	public String getSpellName() {
		return spellName;
	}

	public void setSpellName(String spellName) {
		this.spellName = spellName;
	}

	public int getAtkBonus() {
		return atkBonus;
	}

	public void setAtkBonus(int atkBonus) {
		this.atkBonus = atkBonus;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}

	public Spell()
	{
		
	}
	
	public Spell(String spellName, int atkBonus, String attribute, String damageType)
	{
		this.spellName = spellName;
		this.atkBonus = atkBonus;
		this.attribute = attribute;
		this.damageType = damageType;
	}
	

}
