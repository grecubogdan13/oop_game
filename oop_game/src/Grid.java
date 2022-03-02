import java.util.ArrayList;
import java.util.Random;

public class Grid extends ArrayList<ArrayList<Cell>> {
    private int length;
    private int width;
    private Character player;
    private Cell current;
    private static Grid game_map=null;
    private Grid(int length,int width,Character player){
        this.length=length;
        this.width=width;
        this.player=player;
        this.current=null;
    }
    public static Grid generate_test_map(int length, int width, Character player){
        if(game_map==null){
            game_map=new Grid(length,width,player);
            int i;
            int j;
            for(i=0;i<length;i++) {
                game_map.add(new ArrayList<Cell>());
                for (j = 0; j < width; j++) {
                    if ((i == 0 && j == 3) || (i == 1 && j == 3) || (i == 2 && j == 0)) {
                        game_map.get(i).add(new Cell(i, j, new Shop()));
                    } else if (i == 3 && j == 4) {
                        game_map.get(i).add(new Cell(i, j, new Enemy()));
                    } else if (i == 4 && j == 4) {
                        game_map.get(i).add(new Cell(i, j, new Finish()));
                    } else {
                        game_map.get(i).add(new Cell(i, j, new Empty()));
                    }
                }
            }
        }
        return game_map;
    }
    public static Grid generate_random_map(int length,int width,Character player){
        if(game_map==null){
            game_map=new Grid(length,width,player);
            int i;
            int j;
            int e1x=0;
            int e1y=0;
            int e2x=0;
            int e2y=0;
            int e3x=0;
            int e3y=0;
            int e4x=0;
            int e4y=0;
            int s1x=0;
            int s1y=0;
            int s2x=0;
            int s2y=0;
            int fx=0;
            int fy=0;
            Random rand=new Random();
            while(e1x==0&&e1y==0){
                e1x=rand.nextInt(length);
                e1y=rand.nextInt(width);
            }
            while((e2x==0&&e2y==0)||(e2x==e1x&&e2y==e1y)){
                e2x=rand.nextInt(length);
                e2y=rand.nextInt(width);
            }
            while((e3x==0&&e3y==0)||(e3x==e1x&&e3y==e1y)||(e3x==e2x&&e3y==e2y)){
                e3x=rand.nextInt(length);
                e3y=rand.nextInt(width);
            }
            while((e4x==0&&e4y==0)||(e4x==e1x&&e4y==e1y)||(e4x==e2x&&e4y==e2y)||(e4x==e3x&&e4y==e3y)){
                e4x=rand.nextInt(length);
                e4y=rand.nextInt(width);
            }
            while((s1x==0&&s1y==0)||(s1x==e1x&&s1y==e1y)||(s1x==e2x&&s1y==e2y)||(s1x==e3x&&s1y==e3y)||(s1x==e4x&&s1y==e4y)){
                s1x=rand.nextInt(length);
                s1y=rand.nextInt(width);
            }
            while((s2x==0&&s2y==0)||(s2x==s1x&&s2y==s1y)||(s2x==e1x&&s2y==e1y)||(s2x==e2x&&s2y==e2y)||(s2x==e3x&&s2y==e3y)||(s2x==e4x&&s2y==e4y)){
                s2x=rand.nextInt(length);
                s2y=rand.nextInt(width);
            }
            while((fx==0&&fy==0)||(fx==s2x&&fy==s2y)||(fx==s1x&&fy==s1y)||(fx==e1x&&fy==e1y)||(fx==e2x&&fy==e2y)||(fx==e3x&&fy==e3y)||(fx==e4x&&fy==e4y)){
                fx=rand.nextInt(length);
                fy=rand.nextInt(width);
            }
            for(i=0;i<length;i++) {
                game_map.add(new ArrayList<Cell>());
                for (j = 0; j < width; j++) {
                    if ((i == s1x && j == s1y) || (i == s2x && j == s2y)) {
                        game_map.get(i).add(new Cell(i, j, new Shop()));
                    } else if ((i == e1x && j == e1y)||(i == e2x && j == e2y)||(i == e4x && j == e4y)||(i == e3x && j == e3y)) {
                        game_map.get(i).add(new Cell(i, j, new Enemy()));
                    } else if (i == fx && j == fy) {
                        game_map.get(i).add(new Cell(i, j, new Finish()));
                    } else {
                        game_map.get(i).add(new Cell(i, j, new Empty()));
                    }
                }
            }
        }
        return game_map;
    }
    public void setCurrent(int x,int y){
        current=game_map.get(x).get(y);
    }
    public Cell getCurrent(){
        return current;
    }
    public boolean goNorth(){
        int ox=player.getOx();
        int oy=player.getOy();
        if(ox==0)
            System.out.println("Cannot go North.");
        else{
            player.setPosition(ox-1,oy);
            current=game_map.get(ox-1).get(oy);
            if(current.isVisited()==false) {
                Random rand =new Random();
                int chance = rand.nextInt(5);
                if(chance == 0){
                    int baseMoney=5;
                    int randomMoney= rand.nextInt(10);
                    System.out.println("You found "+(baseMoney+randomMoney)+" coins.");
                    player.getCharacterInventory().setMoney(player.getCharacterInventory().getMoney()+baseMoney+randomMoney);
                }
                game_map.get(ox - 1).get(oy).setVisited(true);
                return true;
            }
        }
        return false;
    }
    public boolean goSouth(){
        int ox=player.getOx();
        int oy=player.getOy();
        if(ox==(length-1))
            System.out.println("Cannot go South.");
        else{
            player.setPosition(ox+1,oy);
            current=game_map.get(ox+1).get(oy);
            if(current.isVisited()==false) {
                Random rand =new Random();
                int chance = rand.nextInt(5);
                if(chance == 0){
                    int baseMoney=5;
                    int randomMoney= rand.nextInt(10);
                    System.out.println("You found "+(baseMoney+randomMoney)+" coins.");
                    player.getCharacterInventory().setMoney(player.getCharacterInventory().getMoney()+baseMoney+randomMoney);
                }
                game_map.get(ox + 1).get(oy).setVisited(true);
                return true;
            }
        }
        return false;
    }
    public boolean goEast(){
        int ox=player.getOx();
        int oy=player.getOy();
        if(oy==(width-1))
            System.out.println("Cannot go East.");
        else{
            player.setPosition(ox,oy+1);
            current=game_map.get(ox).get(oy+1);
            if(current.isVisited()==false) {
                Random rand =new Random();
                int chance = rand.nextInt(5);
                if(chance == 0){
                    int baseMoney=5;
                    int randomMoney= rand.nextInt(10);
                    System.out.println("You found "+(baseMoney+randomMoney)+" coins.");
                    player.getCharacterInventory().setMoney(player.getCharacterInventory().getMoney()+baseMoney+randomMoney);
                }
                game_map.get(ox).get(oy + 1).setVisited(true);
                return true;
            }
        }
        return false;
    }
    public boolean goWest(){
        int ox=player.getOx();
        int oy=player.getOy();
        if(oy==0)
            System.out.println("Cannot go West.");
        else{
            player.setPosition(ox,oy-1);
            current=game_map.get(ox).get(oy-1);
            if(current.isVisited()==false) {
                Random rand =new Random();
                int chance = rand.nextInt(5);
                if(chance == 0){
                    int baseMoney=5;
                    int randomMoney= rand.nextInt(10);
                    System.out.println("You found "+(baseMoney+randomMoney)+" coins.");
                    player.getCharacterInventory().setMoney(player.getCharacterInventory().getMoney()+baseMoney+randomMoney);
                }
                game_map.get(ox).get(oy - 1).setVisited(true);
                return true;
            }
        }
        return false;
    }
    public int getLength(){
        return length;
    }
    public int getWidth(){
        return width;
    }
}
