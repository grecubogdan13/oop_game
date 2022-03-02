public class HealthPotion implements Potion {
    private int value;
    private int price;
    private int weight;
    public HealthPotion(int v,int w,int p){
        value=v;
        price=p;
        weight=w;
    }
    public void usePotion(Character c){
        c.setCurrentLife(c.getCurrentLife()+getValue());
        if(c.getCurrentLife()>c.getMaxLife())
            c.setCurrentLife(c.getMaxLife());
        System.out.println("You used a Health Potion.");
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
