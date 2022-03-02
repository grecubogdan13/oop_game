public abstract class Character extends Entity {
    private String name;
    private int xCoord;
    private int yCoord;
    private Inventory characterInventory=new Inventory(0,0);
    private int exp;
    private int level;
    private int strength;
    private int charisma;
    private int dexterity;
    public void buyPotion(Potion p){
        if(characterInventory.getEmptyWeight()>=p.getWeight()&&characterInventory.getMoney()>=p.getPrice()) {
            characterInventory.setMoney(characterInventory.getMoney()-p.getPrice());
            characterInventory.addPotion(p);
        }
    }

    public void setName(String name){
        this.name=name;
    }
    public void setLevel(int level){
        this.level=level;
    }
    public void setExperience(int experience){
        this.exp=experience;
    }
    public void setPosition(int x, int y){
        xCoord=x;
        yCoord=y;
    }
    public void setStrength(int s){
        strength=s;
    }
    public void setDexterity(int d){
        dexterity=d;
    }
    public void setCharisma(int c){
        charisma=c;
    }
    public void setCharacterInventory(int m,int w){
        characterInventory.setMoney(m);
        characterInventory.setMaxWeight(w);
    }
    public int getStrength(){
        return strength;
    }
    public int getDexterity(){
        return dexterity;
    }
    public int getCharisma(){
        return charisma;
    }
    public String getName(){
        return name;
    }
    public int getOx(){
        return xCoord;
    }
    public int getOy(){
        return yCoord;
    }
    public Inventory getCharacterInventory(){
        return characterInventory;
    }
    public int getLevel(){
        return level;
    }
    public String getStringLevel() {
        return String.valueOf(level);
    }
        public int getExp(){
        return exp;
    }
    public void setExp(int e){
        exp=e;
    }
    public void checkLevel(){
        if(exp>=100){
            level=level+1;
            System.out.println("Your character progressed to level "+level);
            exp=exp-100;
            if(level%2==0){
                if(this.getClass().getSimpleName().equals("Warrior")){
                    strength++;
                    charisma++;
                }
                else if(this.getClass().getSimpleName().equals("Mage")){
                    charisma++;
                    dexterity++;
                }
                else{
                    dexterity++;
                    strength++;
                }
            }
            else{
                if(this.getClass().getSimpleName().equals("Warrior")){
                    strength++;
                    dexterity++;
                }
                else if(this.getClass().getSimpleName().equals("Mage")){
                    charisma++;
                    strength++;
                }
                else{
                    dexterity++;
                    charisma++;
                }
            }
        }
    }
    public abstract void receiveDamage(int d);
    public abstract int getDamage();
}
