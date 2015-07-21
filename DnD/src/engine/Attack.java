package engine;

/**
 * Created by James Lucas (www.sleepycoding.co.uk) on 21/07/15.
 * Class to store the data for each DnD attack row in the CharacterSheetGUI.
 */
public class Attack implements java.io.Serializable {

    private String attackName;
    private int atkBonus;
    private String damageType;

    public Attack() {
        this.attackName = "";
        this.atkBonus = 0;
        this.damageType = "";
    }

    public Attack(String attackName, int atkBonus, String damageType) {
        this.attackName = attackName;
        this.atkBonus = atkBonus;
        this.damageType = damageType;
    }

    public String getAttackName() {
        return attackName;
    }

    public void setAttackName(String attackName) {
        this.attackName = attackName;
    }

    public int getAtkBonus() {
        return atkBonus;
    }

    public void setAtkBonus(int atkBonus) {
        this.atkBonus = atkBonus;
    }

    public String getDamageType() {
        return damageType;
    }

    public void setDamageType(String damageType) {
        this.damageType = damageType;
    }
}
