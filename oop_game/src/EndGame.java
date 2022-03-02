import javax.swing.*;
import java.awt.*;

public class EndGame extends JFrame {

    Container container=getContentPane();

    public EndGame(Boolean ending,Character c,int wonBattles){
        Image icon = Toolkit.getDefaultToolkit().getImage("./Imagini/WoM.png");
        setIconImage(icon);
        setTitle("Ending");
        setVisible(true);
        setBounds(10, 10, 350, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        container.setLayout(null);
        if(ending==false) {
            JLabel end = new JLabel("Unfortunately, you lost.");
            end.setBounds(20, 20, 250, 19);
            JLabel end2 = new JLabel("Try again another time");
            end2.setBounds(20,40,250,19);
            container.add(end);
            container.add(end2);
            JLabel end3 = new JLabel("Your character reached level "+c.getLevel());
            end3.setBounds(20,60,250,19);
            JLabel end4 = new JLabel("Your character reached "+c.getExp()+" experience");
            end4.setBounds(20,80,250,19);
            JLabel end5 = new JLabel("Your character earned "+(c.getCharacterInventory().getMoney()-15)+" coins");
            end5.setBounds(20,100,250,19);
            JLabel end6 = new JLabel("Your character defeated "+wonBattles+" enemies.");
            end6.setBounds(20,120,250,19);
            JLabel end7 = new JLabel("Credits:");
            end7.setBounds(20,140,250,19);
            JLabel end8 = new JLabel("Made by Grecu Bogdan");
            end8.setBounds(20,160,250,19);
            JLabel end9 = new JLabel("All game art from www.flaticon.com");
            end9.setBounds(20,180,250,19);
            container.add(end3);
            container.add(end4);
            container.add(end5);
            container.add(end6);
            container.add(end7);
            container.add(end8);
            container.add(end9);
        }
        else {
            JLabel end = new JLabel("Congrats, you won the game!");
            end.setBounds(20, 20, 250, 19);
            JLabel end2 = new JLabel("Try to see if you can do it again.");
            end2.setBounds(20,40,250,19);
            container.add(end);
            container.add(end2);
            JLabel end3 = new JLabel("Your character reached level "+c.getLevel());
            end3.setBounds(20,60,250,19);
            JLabel end4 = new JLabel("Your character reached "+c.getExp()+" experience");
            end4.setBounds(20,80,250,19);
            JLabel end5 = new JLabel("Your character earned "+(c.getCharacterInventory().getMoney()-15)+" coins");
            end5.setBounds(20,100,250,19);
            JLabel end6 = new JLabel("Your character defeated "+wonBattles+" enemies.");
            end6.setBounds(20,120,250,19);
            JLabel end7 = new JLabel("Credits:");
            end7.setBounds(20,140,250,19);
            JLabel end8 = new JLabel("Made by Grecu Bogdan");
            end8.setBounds(20,160,250,19);
            JLabel end9 = new JLabel("All game art from www.flaticon.com");
            end9.setBounds(20,180,250,19);
            container.add(end3);
            container.add(end4);
            container.add(end5);
            container.add(end6);
            container.add(end7);
            container.add(end8);
            container.add(end9);
        }
        show();
    }
}
