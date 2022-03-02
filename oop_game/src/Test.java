import java.io.FileNotFoundException;

public class Test {
    public static void main(String args[]) throws FileNotFoundException {
        Game game=Game.getInstance();
        game.run();
    }
}
