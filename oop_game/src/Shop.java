import java.util.ArrayList;
import java.util.Random;

public class Shop implements CellElement{
    public char toCharacter() {
        return 'S';
    }
    private ArrayList<Potion> shopInventory=new ArrayList<Potion>();
    public Shop() {
        Random rand = new Random();
        int baseCount=2;
        int variableCount= rand.nextInt(3);
        int i;
        for(i=1;i<=baseCount+variableCount;i++) {
            int type= rand.nextInt(2);
            int baseValue=10;
            int variableValue=rand.nextInt(16);
            int baseWeight=10;
            int variableWeight=rand.nextInt(16);
            int basePrice=10;
            int variablePrice=rand.nextInt(16);
            if(type==0){
                Potion newPotion = new HealthPotion(baseValue+variableValue,baseWeight+variableWeight,basePrice+variablePrice);
                shopInventory.add(newPotion);
            }
            else{
                Potion newPotion = new ManaPotion(baseValue+variableValue,baseWeight+variableWeight,basePrice+variablePrice);
                shopInventory.add(newPotion);
            }
        }
    }
    public ArrayList<Potion> getShopInventory(){
        return shopInventory;
    }
    public Potion select(int i){
        Potion p=shopInventory.get(i);
        shopInventory.remove(i);
        return p;
    }
}
