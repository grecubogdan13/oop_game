import java.util.Random;

public class Mage extends Character {
    public Mage(String name, int level, int experience){
        setName(name);
        setLevel(level);
        setExperience(experience);
        setPosition(0,0);
        setCharisma(level);
        if(level%2==0) {
            setStrength(level/2);
            setDexterity(level/2);
        }
        else{
            setStrength(level/2+1);
            setDexterity(level/2);
        }
        setCharacterInventory(15,25);
        setCurrentLife(200);
        setMaxLife(200);
        setMaxMana(100);
        setCurrentMana(100);
        setFireProtection(false);
        setEarthProtection(false);
        setIceProtection(true);
        Random rand = new Random();
        int baseSpells = 2;
        int variableSpells = rand.nextInt(3);
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
    public void receiveDamage(int d)  {
        Random rand = new Random();
        int chance = rand.nextInt(100);
        chance++;
        int miss = 10 + 2 * getStrength();
        if (miss > 60)
            miss = 60;
        if (chance > miss) {
            setCurrentLife(getCurrentLife() - d);
            System.out.println("You were hit for "+d+" damage.");
        }
        else {
            setCurrentLife(getCurrentLife() - d/2);
            System.out.println("You were hit for "+d/2+" damage.");
        }
    }
    public int getDamage(){
        Random rand = new Random();
        int chance = rand.nextInt(100);
        chance++;
        int miss = 10 + 2 * getCharisma();
        if (miss > 50)
            miss = 50;
        if(chance>miss){
            System.out.println("You attacked your enemy for "+(10+2*getCharisma())+" damage.");
            return 10+2*getCharisma();
        }
        else {
            System.out.println("You attacked your enemy for "+(20+4*getCharisma())+" damage.");
            return 20+4*getCharisma();
        }
    }
}
