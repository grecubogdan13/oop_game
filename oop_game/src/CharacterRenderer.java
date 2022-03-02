import javax.swing.*;
import java.awt.*;

public class CharacterRenderer extends JPanel implements ListCellRenderer {
    JLabel name, level, pic;

    public CharacterRenderer() {
        setOpaque(true);
        this.name = new JLabel();
        this.level = new JLabel();
        this.pic = new JLabel();
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        this.pic.setIcon(new ImageIcon("./Imagini/" + ((Character) value).getClass().getSimpleName() + ".png"));
        this.name.setText(((Character) value).getName());
        this.level.setText(((Character) value).getStringLevel());

        add(this.pic);
        add(this.name);
        add(this.level);
        setForeground(Color.black);

        if (isSelected) {
            setBackground(Color.red);
        } else if (index % 2 == 0) {
            setBackground(Color.green);
        } else {
            setBackground(Color.yellow);
        }

        return this;
    }
}