import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Account {
    private Information player_info;
    private ArrayList<Character> player_characters;
    private int number_of_plays;
    public Account(Information info, int nop, ArrayList<Character> pc){
      player_info=info;
      number_of_plays=nop;
      player_characters=pc;
    }
    public Information getPlayer_info(){
        return player_info;
    }
    public ArrayList<Character> getPlayer_characters(){
        return player_characters;
    }
    public static class Information{
        private final Credentials player_cred;
        private final TreeSet games;
        private final String name;
        private final String country;
        private Information(InformationBuilder b) {
            this.country=b.country;
            this.player_cred=b.player_cred;
            this.games=b.games;
            this.name=b.name;
        }
        public String getName(){
            return name;
        }
        public Credentials getCredentials(){
            return player_cred;
        }
        public static class InformationBuilder{
            private final Credentials player_cred;
            private TreeSet games;
            private final String name;
            private String country;
            public InformationBuilder(Credentials pc,String n){
                this.player_cred=pc;
                this.name=n;
            }
            public InformationBuilder games(TreeSet g){
                this.games=g;
                return this;
            }
            public InformationBuilder country(String country){
                this.country=country;
                return this;
            }
            public Information build() {
                Information information = new Information(this);
                try {
                    validateInformation(information);
                } catch (InformationIncompleteException e) {
                    e.printStackTrace();
                } finally {
                    return information;
                }
            }
            private void validateInformation(Information information)  throws InformationIncompleteException {
                if(information.player_cred==null) throw new InformationIncompleteException();
                else if(information.name==null) throw new InformationIncompleteException();
            }
            class InformationIncompleteException extends Exception{
                public InformationIncompleteException(){
                    super("Cannot create user, incomplete information.");
                }
            }
        }
    }

}
