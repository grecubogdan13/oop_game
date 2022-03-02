import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class CharacterSelectionFrame extends JFrame implements ListSelectionListener,ActionListener {
    Vector<Character> characters = new Vector<Character>();
    JList<Character> JLCharacters;
    JButton buton = new JButton("SELECT");
    int chosen_character=-1;

    public CharacterSelectionFrame(ArrayList<Character> characters){
        Image icon = Toolkit.getDefaultToolkit().getImage("./Imagini/WoM.png");
        setIconImage(icon);
        setTitle("Character Selection");
        setVisible(true);
        setBounds(10,10,300,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        this.characters=new Vector<Character>(characters);
        //System.out.println(characters.get(1).getClass().getSimpleName());
        setLayout(new FlowLayout());
        JScrollPane scrollPane=new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(250,100));
        JLCharacters = new JList<Character>(this.characters);
        JLCharacters.setLayoutOrientation(JList.VERTICAL);
        JLCharacters.setCellRenderer(new CharacterRenderer());
        scrollPane.setViewportView(JLCharacters);
        add(scrollPane);
        buton.addActionListener(this);
        add(buton);
        show();
    }

    public void actionPerformed(ActionEvent a) {
        chosen_character=JLCharacters.getSelectedIndex();
        if(chosen_character==-1)
            JOptionPane.showMessageDialog(this, "Nothing was selected.");
        else {
            JOptionPane.showMessageDialog(this, "Character was loaded. Starting game");
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            dispose();
        }
    }
    public void valueChanged(ListSelectionEvent e){
        if(JLCharacters.isSelectionEmpty()){
            return;
        }
    }
}
