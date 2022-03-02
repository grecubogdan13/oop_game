import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.CharacterCodingException;
import java.util.*;

public class Game {
    private static Game instance=null;
    private Game(){
    }
    public static Game getInstance(){
        if(instance==null){
            instance = new Game();
        }
        return instance;
    }
    private ArrayList<Account> accounts=new ArrayList<Account>();
    private Map<Cell.Type, List<String>> stories;
    public void run() throws FileNotFoundException {
        Scanner scan= new Scanner(System.in);
        System.out.println("Select how you want to play: C-CLI mode, G-GUI mode, T-Test mode.");
        String text= scan.nextLine();
        if(text.equals("T")){
            String content_story = new Scanner(new File("stories.json")).useDelimiter("\\Z").next();
            JSONObject story_reader = new JSONObject(content_story);
            JSONArray story_json_array = story_reader.getJSONArray("stories");
            System.out.println("Playing game in CLI mode.");
            String content = new Scanner(new File("accounts.json")).useDelimiter("\\Z").next();
            JSONObject account_reader = new JSONObject(content);
            JSONArray json_accounts = account_reader.getJSONArray("accounts");
            //System.out.println("Select an account by number");
            for(int i=0;i<json_accounts.length();i++){
                JSONObject json_account = json_accounts.getJSONObject(i);
                String country = json_account.getString("country");
                String name = json_account.getString("name");
                int number_of_plays = json_account.getInt("maps_completed");
                JSONObject json_credentials = json_account.getJSONObject("credentials");
                JSONArray json_fav_games = json_account.getJSONArray("favorite_games");
                JSONArray json_characters = json_account.getJSONArray("characters");
                String email = json_credentials.getString("email");
                String password = json_credentials.getString("password");
                TreeSet<String> fav_games=new TreeSet<String>();
                for(int j=0;j<json_fav_games.length();j++){
                    fav_games.add(json_fav_games.getString(j));
                }
                Credentials player_cred = new Credentials(email,password);
                Account.Information info= new Account.Information.InformationBuilder(player_cred,name).country(country).games(fav_games).build();
                ArrayList<Character> charactersList = new ArrayList<Character>();
                for(CharacterFactory.CharacterType chType : CharacterFactory.CharacterType.values()){
                    Character ch = CharacterFactory.createCharacter(chType,json_characters);
                    charactersList.add(ch);
                }
                Account acc = new Account(info,number_of_plays,charactersList);
                accounts.add(acc);
                System.out.println("Account number "+ (i+1));
                System.out.println(json_account.toString());
            }
            Account playing_account = accounts.get(1);
            System.out.println(playing_account.getPlayer_info().getName());
            System.out.println("Select a character");
            for(int i=0;i<playing_account.getPlayer_characters().size();i++){
                System.out.println("Character number "+(i+1));
                System.out.println("Name: "+ playing_account.getPlayer_characters().get(i).getName()+" Type: " + playing_account.getPlayer_characters().get(i).getClass().getSimpleName() + " Level: " +  playing_account.getPlayer_characters().get(i).getLevel());
            }
            Character playing_character = playing_account.getPlayer_characters().get(1);
            System.out.println(playing_character.getName());
            Grid game_table = Grid.generate_test_map(5,5,playing_character);
            game_table.setCurrent(0,0);
            game_table.get(0).get(0).setVisited(true);
            String cmd = scan.nextLine();
            boolean game_over = false;
            boolean newField=true;
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            newField=game_table.goEast();
            cmd = scan.nextLine();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            newField=game_table.goEast();
            cmd = scan.nextLine();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            newField=game_table.goEast();
            cmd = scan.nextLine();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            cmd = scan.nextLine();
            game_table.goEast();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            cmd = scan.nextLine();
            game_table.goSouth();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            cmd = scan.nextLine();
            game_table.goSouth();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            cmd = scan.nextLine();
            game_table.goSouth();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                    return;
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            cmd = scan.nextLine();
            game_table.goEast();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            cmd = scan.nextLine();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            game_table.goSouth();
            for(int i=0;i<game_table.getLength();i++) {
                for(int j=0;j<game_table.getWidth();j++) {
                    if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                        System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                    }
                    else if (game_table.get(i).get(j).isVisited() == true)
                        System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                    else
                        System.out.print("?  ");
                }
                System.out.println();
            }
            System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
            if(newField==true) {
                printStory(game_table.getCurrent(), story_json_array);
                playing_character.setExp(playing_character.getExp()+5);
                playing_character.checkLevel();
            }
            if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                    playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    playing_character.checkLevel();
                    Random rand =new Random();
                    int money_chance=rand.nextInt(5);
                    if(money_chance!=0){
                        playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                    }
                    game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else{
                    game_over=true;
                    System.out.println("You lost the game. Try again another time.");
                }
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                System.out.println("You finished the game. Congrats");
                // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                game_over=true;
            }
            else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
            cmd = scan.nextLine();
        }
        else if(text.equals("C")){
            String content_story = new Scanner(new File("stories.json")).useDelimiter("\\Z").next();
            JSONObject story_reader = new JSONObject(content_story);
            JSONArray story_json_array = story_reader.getJSONArray("stories");
            System.out.println("Playing game in CLI mode.");
            String content = new Scanner(new File("accounts.json")).useDelimiter("\\Z").next();
            JSONObject account_reader = new JSONObject(content);
            JSONArray json_accounts = account_reader.getJSONArray("accounts");
            //System.out.println("Select an account by number");
            for(int i=0;i<json_accounts.length();i++){
                JSONObject json_account = json_accounts.getJSONObject(i);
                String country = json_account.getString("country");
                String name = json_account.getString("name");
                int number_of_plays = json_account.getInt("maps_completed");
                JSONObject json_credentials = json_account.getJSONObject("credentials");
                JSONArray json_fav_games = json_account.getJSONArray("favorite_games");
                JSONArray json_characters = json_account.getJSONArray("characters");
                String email = json_credentials.getString("email");
                String password = json_credentials.getString("password");
                TreeSet<String> fav_games=new TreeSet<String>();
                for(int j=0;j<json_fav_games.length();j++){
                    fav_games.add(json_fav_games.getString(j));
                }
                Credentials player_cred = new Credentials(email,password);
                Account.Information info= new Account.Information.InformationBuilder(player_cred,name).country(country).games(fav_games).build();
                ArrayList<Character> charactersList = new ArrayList<Character>();
                for(CharacterFactory.CharacterType chType : CharacterFactory.CharacterType.values()){
                    Character ch = CharacterFactory.createCharacter(chType,json_characters);
                    charactersList.add(ch);
                }
                Account acc = new Account(info,number_of_plays,charactersList);
                accounts.add(acc);
                System.out.println("Account number "+ (i+1));
                System.out.println(json_account.toString());
            }
            System.out.println("Email:");
            String em = scan.nextLine();
            System.out.println("Password:");
            String pwd=scan.nextLine();
            int found = -1;
            for(int i = 0; i< accounts.size(); i++){
                if(accounts.get(i).getPlayer_info().getCredentials().getEmail().equals(em) && accounts.get(i).getPlayer_info().getCredentials().getPassword().equals(pwd))
                    found = i;
            }
            if(found==-1) {
                System.out.println("This account does not exist");
                return ;
            }
            Account playing_account = accounts.get(found);
            System.out.println(playing_account.getPlayer_info().getName());
            System.out.println("Select a character");
            for(int i=0;i<playing_account.getPlayer_characters().size();i++){
                System.out.println("Character number "+(i+1));
                System.out.println("Name: "+ playing_account.getPlayer_characters().get(i).getName()+" Type: " + playing_account.getPlayer_characters().get(i).getClass().getSimpleName() + " Level: " +  playing_account.getPlayer_characters().get(i).getLevel());
            }
            int nc = scan.nextInt();
            Character playing_character = playing_account.getPlayer_characters().get(nc-1);
            System.out.println("Move with w,a,s,d. One step at a time");
            Grid game_table = Grid.generate_test_map(5,5,playing_character);
            game_table.setCurrent(0,0);
            game_table.get(0).get(0).setVisited(true);
            String cmd = scan.nextLine();
            boolean game_over = false;
            boolean newField=true;
            while(game_over==false){
                for(int i=0;i<game_table.getLength();i++) {
                    for(int j=0;j<game_table.getWidth();j++) {
                        if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                            System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                        }
                        else if (game_table.get(i).get(j).isVisited() == true)
                            System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                        else
                            System.out.print("?  ");
                    }
                    System.out.println();
                }
                System.out.println("Current position: ("+(game_table.getCurrent().getOx()+1)+","+((game_table).getCurrent().getOy()+1)+")");
                if(newField==true) {
                    printStory(game_table.getCurrent(), story_json_array);
                    playing_character.setExp(playing_character.getExp()+5);
                    playing_character.checkLevel();
                }
                if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Shop") {
                    openShop(playing_character, (Shop) game_table.getCurrent().getInteraction());
                    for(int i=0;i<game_table.getLength();i++) {
                        for(int j=0;j<game_table.getWidth();j++) {
                            if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                            }
                            else if (game_table.get(i).get(j).isVisited() == true)
                                System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                            else
                                System.out.print("?  ");
                        }
                        System.out.println();
                    }
                }
                else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Enemy"){
                    battleEnemy(playing_character, (Enemy) game_table.getCurrent().getInteraction());
                    if(( (Enemy) game_table.getCurrent().getInteraction()).getCurrentLife()<=0){
                        playing_character.setExp(playing_character.getExp()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                        playing_character.checkLevel();
                        Random rand =new Random();
                        int money_chance=rand.nextInt(5);
                        if(money_chance!=0){
                            playing_character.getCharacterInventory().setMoney(playing_character.getCharacterInventory().getMoney()+((Enemy) game_table.getCurrent().getInteraction()).getMaxLife());
                        }
                        game_table.get(game_table.getCurrent().getOx()).get(game_table.getCurrent().getOy()).setCellType(new Empty());
                        for(int i=0;i<game_table.getLength();i++) {
                            for(int j=0;j<game_table.getWidth();j++) {
                                if(game_table.getCurrent().getOx()==i && game_table.getCurrent().getOy()==j) {
                                    System.out.print("P"+game_table.get(i).get(j).getInteraction().toCharacter()+" ");
                                }
                                else if (game_table.get(i).get(j).isVisited() == true)
                                    System.out.print(game_table.get(i).get(j).getInteraction().toCharacter() + "  ");
                                else
                                    System.out.print("?  ");
                            }
                            System.out.println();
                        }
                    }
                    else{
                        game_over=true;
                        System.out.println("You lost the game. Try again another time.");
                    }
                }
                else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Finish") {
                    System.out.println("You finished the game. Congrats");
                   // Shop s= (Shop) game_table.get(0).get(3).getInteraction();
                    game_over=true;
                }
                else if(game_table.getCurrent().getInteraction().getClass().getSimpleName()=="Empty");
                if(game_over==false) {
                    cmd = scan.nextLine();
                    if (cmd.equals("w")) {
                        newField=game_table.goNorth();
                    } else if (cmd.equals("s")) {
                        newField=game_table.goSouth();
                    } else if (cmd.equals("d")) {
                        newField=game_table.goEast();
                    } else if (cmd.equals("a")) {
                        newField=game_table.goWest();
                    }
                }
            }
        }
        else if(text.equals("G")){
            String content_story = new Scanner(new File("stories.json")).useDelimiter("\\Z").next();
            JSONObject story_reader = new JSONObject(content_story);
            JSONArray story_json_array = story_reader.getJSONArray("stories");
            String content = new Scanner(new File("accounts.json")).useDelimiter("\\Z").next();
            JSONObject account_reader = new JSONObject(content);
            JSONArray json_accounts = account_reader.getJSONArray("accounts");
            for(int i=0;i<json_accounts.length();i++){
                JSONObject json_account = json_accounts.getJSONObject(i);
                String country = json_account.getString("country");
                String name = json_account.getString("name");
                int number_of_plays = json_account.getInt("maps_completed");
                JSONObject json_credentials = json_account.getJSONObject("credentials");
                JSONArray json_fav_games = json_account.getJSONArray("favorite_games");
                JSONArray json_characters = json_account.getJSONArray("characters");
                String email = json_credentials.getString("email");
                String password = json_credentials.getString("password");
                TreeSet<String> fav_games=new TreeSet<String>();
                for(int j=0;j<json_fav_games.length();j++){
                    fav_games.add(json_fav_games.getString(j));
                }
                Credentials player_cred = new Credentials(email,password);
                Account.Information info= new Account.Information.InformationBuilder(player_cred,name).country(country).games(fav_games).build();
                ArrayList<Character> charactersList = new ArrayList<Character>();
                for(CharacterFactory.CharacterType chType : CharacterFactory.CharacterType.values()){
                    Character ch = CharacterFactory.createCharacter(chType,json_characters);
                    charactersList.add(ch);
                }
                Account acc = new Account(info,number_of_plays,charactersList);
                accounts.add(acc);
            }
            LoginFrame loginPage = new LoginFrame(accounts);
            Thread waitingThread = new Thread();
            while(loginPage.isVisible()) {
                try {
                    Thread.sleep(100);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            int acc = loginPage.chosen_account;
            if(acc==-1)
                return;
            else {
                Account playing_account = accounts.get(acc);
                //System.out.println(playing_account.getPlayer_info().getCredentials().getEmail());
                loginPage.setVisible(false);
                CharacterSelectionFrame characterSelectionPage = new CharacterSelectionFrame(playing_account.getPlayer_characters());
                waitingThread = new Thread();
                while(characterSelectionPage.isVisible()) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(characterSelectionPage.chosen_character==-1)
                    return;
                else {
                    Character playing_character = playing_account.getPlayer_characters().get(characterSelectionPage.chosen_character);
                    //System.out.println(playing_character.getClass().getSimpleName());
                    Grid game_table = Grid.generate_random_map(6,6,playing_character);
                    game_table.setCurrent(0,0);
                    game_table.get(0).get(0).setVisited(true);
                    WorldOfMarcel game = new WorldOfMarcel(game_table,playing_character,story_json_array);
                    waitingThread=new Thread();
                    while(game.isVisible()){
                        try{
                            Thread.sleep(100);
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    EndGame endPage=new EndGame(game.ending,playing_character,game.wonBattles);
                }
            }
        }
    }

    public void openShop(Character c, Shop s){
        System.out.println("You can buy potions here, or exit with 'e'.");
        System.out.println("You have "+c.getCharacterInventory().getMoney()+" coins.");
        System.out.println("You have "+c.getCharacterInventory().getEmptyWeight()+" space left.");
        System.out.println("Available potions:");
        for(int i=0;i<s.getShopInventory().size();i++){
            System.out.println("Potion number "+(i+1));
            System.out.println("Type: "+s.getShopInventory().get(i).getClass().getSimpleName()+" Cost: "+s.getShopInventory().get(i).getPrice()+" Weight: "+s.getShopInventory().get(i).getWeight()+" Effect: "+s.getShopInventory().get(i).getValue());
        }
        Scanner scan= new Scanner(System.in);
        String cmd= scan.nextLine();
        while(!cmd.equals("e")){
            if(cmd.equals("1") && s.getShopInventory().size()>=1){
                if(s.getShopInventory().get(0).getWeight()>c.getCharacterInventory().getEmptyWeight())
                    System.out.println("Cannot buy potion, not enough space in inventory.");
                else if(s.getShopInventory().get(0).getPrice()>c.getCharacterInventory().getMoney())
                    System.out.println("Cannot buy potion, not enough money to pay it");
                else{
                    Potion p = s.select(0);
                    c.buyPotion(p);
                    System.out.println("You bought a potion");
                }
            }
            else if(cmd.equals("2")&&s.getShopInventory().size()>=2){
                if(s.getShopInventory().get(1).getWeight()>c.getCharacterInventory().getEmptyWeight())
                    System.out.println("Cannot buy potion, not enough space in inventory.");
                else if(s.getShopInventory().get(1).getPrice()>c.getCharacterInventory().getMoney())
                    System.out.println("Cannot buy potion, not enough money to pay it");
                else{
                    Potion p = s.select(1);
                    c.buyPotion(p);
                    System.out.println("You bought a potion");
                }
            }
            else if(cmd.equals("3")&&s.getShopInventory().size()>=3){
                if(s.getShopInventory().get(2).getWeight()>c.getCharacterInventory().getEmptyWeight())
                    System.out.println("Cannot buy potion, not enough space in inventory.");
                else if(s.getShopInventory().get(2).getPrice()>c.getCharacterInventory().getMoney())
                    System.out.println("Cannot buy potion, not enough money to pay it");
                else{
                    Potion p = s.select(2);
                    c.buyPotion(p);
                    System.out.println("You bought a potion");
                }
            }
            else if(cmd.equals("4")&&s.getShopInventory().size()>=4){
                if(s.getShopInventory().get(3).getWeight()>c.getCharacterInventory().getEmptyWeight())
                    System.out.println("Cannot buy potion, not enough space in inventory.");
                else if(s.getShopInventory().get(3).getPrice()>c.getCharacterInventory().getMoney())
                    System.out.println("Cannot buy potion, not enough money to pay it");
                else{
                    Potion p = s.select(3);
                    c.buyPotion(p);
                    System.out.println("You bought a potion");
                }
            }
            System.out.println("You have "+c.getCharacterInventory().getMoney()+" coins.");
            System.out.println("You have "+c.getCharacterInventory().getEmptyWeight()+" space left.");
            System.out.println("Available potions:");
            for(int i=0;i<s.getShopInventory().size();i++){
                System.out.println("Potion number "+(i+1));
                System.out.println("Type: "+s.getShopInventory().get(i).getClass().getSimpleName()+" Cost: "+s.getShopInventory().get(i).getPrice()+" Weight: "+s.getShopInventory().get(i).getWeight()+" Effect: "+s.getShopInventory().get(i).getValue());
            }
            cmd= scan.nextLine();
        }

    }

    public void battleEnemy(Character c, Enemy e){
        System.out.println("You are now in a battle");
        int turn = 1;
        int over = 0;
        while(over!=1){
            System.out.println("You: ");
            System.out.println("Life: "+c.getCurrentLife() +" Mana: "+c.getCurrentMana());
            System.out.println("Protections:");
            System.out.println("Fire protection: "+c.getFireProtection());
            System.out.println("Ice protection: "+c.getIceProtection());
            System.out.println("Earth protection: "+c.getEarthProtection());
            System.out.println();
            System.out.println("Enemy:");
            System.out.println("Life: "+e.getCurrentLife() +" Mana: "+e.getCurrentMana());
            System.out.println("Protections:");
            System.out.println("Fire protection: "+e.getFireProtection());
            System.out.println("Ice protection: "+e.getIceProtection());
            System.out.println("Earth protection: "+e.getEarthProtection());
            if(turn == 1){
                System.out.println("Your turn:");
                System.out.println("1-Normal Attack; 2-Use Spells; 3-Use Potions");
                Scanner scan = new Scanner(System.in);
                String cmd= scan.nextLine();
                if(cmd.equals("1")){
                    e.receiveDamage(c.getDamage());
                }
                else if(cmd.equals("2")){
                    System.out.println("Use spells:");
                    for(int i=0;i<c.getAbilities().size();i++){
                        System.out.println("Spell number "+(i+1));
                        System.out.println("Type: "+c.getAbilities().get(i).getClass().getSimpleName()+" Mana Cost: "+c.getAbilities().get(i).getManaCost()+" Damage: "+c.getAbilities().get(i).getDamage());
                    }
                    cmd=scan.nextLine();
                    if(cmd.equals("1")){
                        c.useSpell(c.getAbilities().get(0),e);
                    }
                    else if(cmd.equals("2")){
                        c.useSpell(c.getAbilities().get(1),e);
                    }
                    else if(cmd.equals("3") && c.getAbilities().size()>=3){
                        c.useSpell(c.getAbilities().get(2),e);
                    }
                    else if(cmd.equals("4") && c.getAbilities().size()>=4){
                        c.useSpell(c.getAbilities().get(3),e);
                    }
                }
                else if(cmd.equals("3")){
                    System.out.println("Use Potions:");
                    for(int i=0;i<c.getCharacterInventory().getInventoryPotions().size();i++){
                        System.out.println("Potion number "+(i+1));
                        System.out.println("Type: "+c.getCharacterInventory().getInventoryPotions().get(i).getClass().getSimpleName()+" Effect: "+c.getCharacterInventory().getInventoryPotions().get(i).getValue());
                    }
                    int ic=scan.nextInt();
                    cmd=scan.nextLine();
                    if(ic==0)
                        System.out.println("You chose to use no potion.");
                    else if(ic<=c.getCharacterInventory().getInventoryPotions().size()){
                        Potion p = c.getCharacterInventory().getInventoryPotions().get(ic-1);
                        if(p.getClass().getSimpleName()=="ManaPotion")
                            ((ManaPotion)p).usePotion(c);
                        else if(p.getClass().getSimpleName()=="HealthPotion")
                            ((HealthPotion)p).usePotion(c);
                    }
                }
                turn=2;
                if(e.getCurrentLife()<=0){
                    over=1;
                }
            }
            else if(turn == 2){
                System.out.println("Enemy turn:");
                Scanner scan = new Scanner(System.in);
                String cmd= scan.nextLine();
                turn=1;
                int checkForAvailableSpells=-1;
                for(int i=0;i<e.getAbilities().size();i++){
                    Spell s=e.getAbilities().get(i);
                    if(s.getManaCost()<=e.getCurrentMana()&&((s.getClass().getSimpleName().equals("Fire")&&c.getFireProtection()==false)||(s.getClass().getSimpleName().equals("Ice")&&c.getIceProtection()==false)||(s.getClass().getSimpleName().equals("Earth")&&c.getEarthProtection()==false)))
                    checkForAvailableSpells=i;
                }
                if(checkForAvailableSpells==-1){
                    c.receiveDamage(e.getDamage());
                }
                else {
                    Random rand = new Random();
                    int attackType=rand.nextInt(5);
                    if(attackType==0)
                        c.receiveDamage(e.getDamage());
                    else {
                        System.out.println("Enemy uses a "+e.getAbilities().get(checkForAvailableSpells).getClass().getSimpleName()+" spell for "+e.getAbilities().get(checkForAvailableSpells).getDamage()+" damage.");
                        e.useSpell(e.getAbilities().get(checkForAvailableSpells),c);
                    }
                }
                if(c.getCurrentLife()<=0){
                    over=1;
                }
                else{
                    c.manaRegen(5);
                    c.healthRegen(5);
                    e.healthRegen(10);
                    e.manaRegen(10);
                }
            }
        }
        if(e.getCurrentLife()<=0){
            System.out.println("You defeated the enemy.");
        }
        else if(c.getCurrentLife()<=0){
            System.out.println("You were defeated");
        }
    }

    public void printStory(Cell c,JSONArray stories){
        for(int i=0;i<stories.length();i++){
            JSONObject story = stories.getJSONObject(i);
            if(story.getString("type").equals(c.getInteraction().getClass().getSimpleName().toUpperCase()))
            {
                System.out.println(story.getString("value"));
                stories.remove(i);
                return;
            }
        }
    }
}
