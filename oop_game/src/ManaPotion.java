public class ManaPotion implements Potion {
    private int value;
    private int price;
    private int weight;
    public ManaPotion(int v,int w,int p){
        value=v;
        price=p;
        weight=w;
    }
    public void usePotion(Character c){
        c.setCurrentMana(c.getCurrentMana()+getValue());
        if(c.getCurrentMana()>c.getMaxMana())
            c.setCurrentMana(c.getMaxMana());
        System.out.println("You used a Mana Potion.");
        c.getCharacterInventory().getInventoryPotions().remove(this);
    }
    public int getPrice(){
        return price;
    }
    public int getValue(){
        return value;
    }
    public int getWeight(){
        return weight;
    }
}
