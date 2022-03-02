public abstract class Spell {
    private int damage;
    private int manaCost;
    public void setDamage(int d) {
        damage=d;
    }
    public void setManaCost(int mc){
        manaCost=mc;
    }
    public int getDamage() {
        return damage;
    }
    public int getManaCost(){
        return manaCost;
    }
}
