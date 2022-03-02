import java.util.Random;

public class Enemy extends Entity implements CellElement {
    public Enemy() {
        int baseLife=50;
        int baseMana=50;
        Random rand=new Random();
        int variableLife = rand.nextInt(51);
        int variableMana = rand.nextInt(51);
        int iceP = rand.nextInt(2);
        int fireP = rand.nextInt(2);
        int earthP = rand.nextInt(2);
        int baseSpells = 2;
        int variableSpells = rand.nextInt(3);
        setCurrentLife(baseLife+variableLife);
        setMaxLife(baseLife+variableLife);
        setCurrentMana(baseMana+variableMana);
        setMaxMana(baseMana+variableMana);
        if(iceP==1)
            setIceProtection(true);
        else
            setIceProtection(false);
        if(fireP==1)
            setFireProtection(true);
        else
            setFireProtection(false);
        if(earthP==1)
            setEarthProtection(true);
        else
            setEarthProtection(false);
        int i;
        for(i=1;i<=(baseSpells+variableSpells);i++) {
            int type = rand.nextInt(3);
            int spellBaseManaCost=10;
            int spellVariableManaCost = rand.nextInt(16);
            int spellBaseDamage=10;
            int spellVariableDamage = rand.nextInt(16);
            if(type==0) {
                Spell newSpell=new Ice(spellBaseManaCost+spellVariableManaCost,spellBaseDamage+spellVariableDamage);
                addAbilities(newSpell);
            }
            else if(type==1) {
                Spell newSpell=new Fire(spellBaseManaCost+spellVariableManaCost,spellBaseDamage+spellVariableDamage);
                addAbilities(newSpell);
            }
            else {
                Spell newSpell=new Earth(spellBaseManaCost+spellVariableManaCost,spellBaseDamage+spellVariableDamage);
                addAbilities(newSpell);
            }
        }
    }
    public void receiveDamage(int d) {
        Random rand=new Random();
        int chance= rand.nextInt(2);
        if(chance==1) {
            setCurrentLife(getCurrentLife() - d);
            System.out.println("You hit the enemy for "+d +" damage.");
        }
        else{
            System.out.println("You missed!");
        }
    }
    public int getDamage() {
        int baseDamage=10;
        Random rand =new Random();
        int variableDamage=rand.nextInt(16);
        System.out.println("Your enemy attacked you for "+(baseDamage+variableDamage)+" damage");
        return baseDamage+variableDamage;
    }
    public char toCharacter() {
        return 'E';
    }
}
