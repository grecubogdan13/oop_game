import java.util.ArrayList;

public abstract class Entity {
    private ArrayList<Spell> abilities=new ArrayList<Spell>();
    private int maxLife;
    private int currentLife;
    private int maxMana;
    private int currentMana;
    private boolean fireProtection;
    private boolean iceProtection;
    private boolean earthProtection;
    public void healthRegen(int hr){
        currentLife=currentLife+hr;
        if(currentLife>maxLife)
            currentLife=maxLife;
    }
    public void manaRegen(int mr){
        currentMana=currentMana+mr;
        if(currentMana>maxMana)
            currentMana=maxMana;
    }
    public void useSpell (Spell s, Entity e) {
        if (e.getClass().getSimpleName().equals("Enemy")) {
            Enemy enemy=(Enemy) e;
            if(s.getClass().getSimpleName().equals("Ice")){
                if(enemy.getIceProtection() == false && currentMana>=s.getManaCost()){
                    currentMana=currentMana-s.getManaCost();
                    enemy.receiveDamage(s.getDamage());
                }
                else if(enemy.getIceProtection()==true){
                    System.out.println("The spell had no effect. Enemy has Ice protection");
                }
                else if(currentMana<s.getManaCost()){
                    System.out.println("You failed to produce the spell. You do not have enough mana");
                }
            }
            if(s.getClass().getSimpleName().equals("Fire")){
                if(enemy.getFireProtection() == false && currentMana>=s.getManaCost()){
                    currentMana=currentMana-s.getManaCost();
                    enemy.receiveDamage(s.getDamage());
                }
                else if(enemy.getFireProtection()==true){
                    System.out.println("The spell had no effect. Enemy has Fire protection");
                }
                else if(currentMana<s.getManaCost()){
                    System.out.println("You failed to produce the spell. You do not have enough mana");
                }
            }
            if(s.getClass().getSimpleName().equals("Earth")){
                if(enemy.getEarthProtection() == false && currentMana>=s.getManaCost()){
                    currentMana=currentMana-s.getManaCost();
                    enemy.receiveDamage(s.getDamage());
                }
                else if(enemy.getEarthProtection()==true){
                    System.out.println("The spell had no effect. Enemy has Earth protection");
                }
                else if(currentMana<s.getManaCost()){
                    System.out.println("You failed to produce the spell. You do not have enough mana");
                }
            }
        }
        else{
            Character c=(Character) e;
            currentMana=currentMana-s.getManaCost();
            c.receiveDamage(s.getDamage());
        }
    }
    public int getCurrentLife(){
        return currentLife;
    }
    public int getCurrentMana(){
        return currentMana;
    }
    public int getMaxLife(){
        return maxLife;
    }
    public int getMaxMana(){
        return maxMana;
    }
    public boolean getIceProtection(){
        return iceProtection;
    }
    public boolean getFireProtection(){
        return fireProtection;
    }
    public boolean getEarthProtection(){
        return earthProtection;
    }
    public void setMaxLife(int ml){
        maxLife=ml;
    }
    public void setCurrentLife(int cl){
        currentLife=cl;
    }
    public void setMaxMana(int mm){
        maxMana=mm;
    }
    public void setCurrentMana(int cm){
        currentMana=cm;
    }
    public void setIceProtection(boolean ip){
        iceProtection=ip;
    }
    public void setFireProtection(boolean fp){
        fireProtection=fp;
    }
    public void setEarthProtection(boolean ep){
        earthProtection=ep;
    }
    public ArrayList<Spell> getAbilities(){
        return abilities;
    }
    public void addAbilities(Spell s){
        abilities.add(s);
    }
    public abstract void receiveDamage(int d);
    public abstract int getDamage();
}
