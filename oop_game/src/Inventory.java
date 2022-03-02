import java.util.ArrayList;

public class Inventory {
    private ArrayList<Potion> inventoryPotions = new ArrayList<Potion>();
    private int money;
    private int maxWeight;
    public Inventory(int mon,int weight){
        int money=mon;
        int maxWeight=weight;
    }
    public void addPotion(Potion p){
        inventoryPotions.add(p);
    }
    public void removePotion(Potion p){
        inventoryPotions.remove(p);
    }
    public ArrayList<Potion> getInventoryPotions(){
        return inventoryPotions;
    }
    public int getMoney(){
        return money;
    }
    public void setMoney(int m){
        money=m;
    }
    public void setMaxWeight(int w){
        maxWeight=w;
    }
    public int getEmptyWeight(){
        int emptyWeight=maxWeight;
        for(Potion p:inventoryPotions){
            emptyWeight=emptyWeight-p.getWeight();
        }
        return emptyWeight;
    }
}
