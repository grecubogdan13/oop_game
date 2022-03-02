import org.json.JSONArray;
import org.json.JSONObject;

public class CharacterFactory {
    public enum CharacterType{
        Warrior,
        Rogue,
        Mage
    }
    public static Character createCharacter(CharacterType chType, JSONArray characters) {
        JSONObject ch1= characters.getJSONObject(0);
        JSONObject ch2= characters.getJSONObject(1);
        JSONObject ch3= characters.getJSONObject(2);
        switch (chType){
            case Warrior: {
                JSONObject ch = new JSONObject();
                if(ch1.getString("profession").equals("Warrior")) {
                    ch = ch1;
                }
                else if(ch2.getString("profession").equals("Warrior")) {
                    ch = ch2;
                }
                else if(ch3.getString("profession").equals("Warrior")) {
                    ch = ch3;
                }
                int level = ch.getInt("level");
                String name = ch.getString("name");
                int experience = ch.getInt("experience");
                return new Warrior(name,level,experience);
            }
            case Rogue: {
                JSONObject ch = new JSONObject();
                if(ch1.getString("profession").equals("Rogue")) {
                    ch = ch1;
                }
                else if(ch2.getString("profession").equals("Rogue")) {
                    ch = ch2;
                }
                else if(ch3.getString("profession").equals("Rogue")) {
                    ch = ch3;
                }
                int level = ch.getInt("level");
                String name = ch.getString("name");
                int experience = ch.getInt("experience");
                return new Rogue(name, level, experience);
            }
            case Mage: {
                JSONObject ch = new JSONObject();
                if(ch1.getString("profession").equals("Mage")) {
                    ch = ch1;
                }
                else if(ch2.getString("profession").equals("Mage")) {
                    ch = ch2;
                }
                else if(ch3.getString("profession").equals("Mage")) {
                    ch = ch3;
                }
                int level = ch.getInt("level");
                String name = ch.getString("name");
                int experience = ch.getInt("experience");
                return new Mage(name, level, experience);
            }
        }
        throw new IllegalArgumentException("The character type "+chType+" is not recognized.");
    }
}
