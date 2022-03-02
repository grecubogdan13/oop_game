import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class WorldOfMarcel extends JFrame implements ActionListener {

    int wonBattles=0;
    Container container=getContentPane();
    Grid game_map;
    Character character;
    JSONArray stories;
    boolean ending=false;

    public WorldOfMarcel(Grid gm, Character ch, JSONArray s){
        Image icon = Toolkit.getDefaultToolkit().getImage("./Imagini/WoM.png");
        setIconImage(icon);
        game_map=gm;
        character=ch;
        stories=s;
        setTitle("WorldOfMarcel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        container.setLayout(null);
        setGridView();//game_map,character,stories);
        show();
        setVisible(true);
    }
    public void setGridView(){//Grid game_map,Character character,JSONArray stories) {
        container.removeAll();
        container.repaint();
        JLabel current_Life = new JLabel("Life: " + character.getCurrentLife());
        current_Life.setBounds(20, 20, 79, 19);
        JLabel max_Life = new JLabel(" / " + character.getMaxLife());
        max_Life.setBounds(100, 20, 79, 19);
        JLabel current_Mana = new JLabel("Mana: " + character.getCurrentMana());
        current_Mana.setBounds(20, 40, 79, 19);
        JLabel max_Mana = new JLabel(" / " + character.getMaxMana());
        max_Mana.setBounds(100, 40, 79, 19);
        JLabel money = new JLabel("Money: " + character.getCharacterInventory().getMoney());
        money.setBounds(20, 60, 79, 19);
        JLabel fireProtection = new JLabel("Fire Protection: " + character.getFireProtection());
        fireProtection.setBounds(20, 80, 149, 19);
        JLabel iceProtection = new JLabel("Ice Protection: " + character.getIceProtection());
        iceProtection.setBounds(20, 100, 149, 19);
        JLabel earthProtection = new JLabel("Earth Protection: " + character.getEarthProtection());
        earthProtection.setBounds(20, 120, 149, 19);
        JLabel level = new JLabel("Level: " + character.getLevel());
        level.setBounds(20, 140, 79, 19);
        JLabel experience = new JLabel("Experience: " + character.getExp());
        experience.setBounds(20, 160, 109, 19);
        int xoffset = 300;
        int yoffset = 10;
        int i, j;
        ImageIcon cellImg = new ImageIcon();
        for (i = 0; i < game_map.getLength(); i++) {
            for (j = 0; j < game_map.getWidth(); j++) {
                if(i==game_map.getCurrent().getOx()&&j==game_map.getCurrent().getOy())
                    cellImg = new ImageIcon("./Imagini/Player.png");
                else if (game_map.get(i).get(j).isVisited() == false)
                    cellImg = new ImageIcon("./Imagini/question.png");
                else
                    cellImg = new ImageIcon("./Imagini/" + game_map.get(i).get(j).getInteraction().getClass().getSimpleName() + ".png");
                JLabel image = new JLabel(cellImg);
                image.setBounds(xoffset + j * 101, yoffset + i * 101, 100, 100);
                container.add(image);
            }
        }
        if(game_map.getCurrent().getOldVisited()==false) {
            JTextArea story = new JTextArea(printStory(game_map.getCurrent(), stories));
            story.setLineWrap(true);
            story.setWrapStyleWord(true);
            story.setBounds(20,180,149,113);
            container.add(story);
            game_map.getCurrent().setOldVisited(true);
        }
        else{
            JTextArea story = new JTextArea();
            story.setLineWrap(true);
            story.setWrapStyleWord(true);
            story.setBounds(20,180,149,113);
            container.add(story);
        }
        ImageIcon up = new ImageIcon("./Imagini/up.png");
        JButton upButton = new JButton(up);
        upButton.setActionCommand("up");
        upButton.addActionListener(this);
        upButton.setBounds(71,300,50,50);
        ImageIcon down = new ImageIcon("./Imagini/down.png");
        JButton downButton = new JButton(down);
        downButton.setActionCommand("down");
        downButton.addActionListener(this);
        downButton.setBounds(71,402,50,50);
        ImageIcon left = new ImageIcon("./Imagini/left.png");
        JButton leftButton = new JButton(left);
        leftButton.setActionCommand("left");
        leftButton.addActionListener(this);
        leftButton.setBounds(20,351,50,50);
        ImageIcon right = new ImageIcon("./Imagini/right.png");
        JButton rightButton = new JButton(right);
        rightButton.setActionCommand("right");
        rightButton.addActionListener(this);
        rightButton.setBounds(122,351,50,50);
        JLabel desc = new JLabel("Current cell");
        desc.setBounds(20,453,100,20);
        ImageIcon c = new ImageIcon(("./Imagini/" + game_map.get(game_map.getCurrent().getOx()).get(game_map.getCurrent().getOy()).getInteraction().getClass().getSimpleName() + ".png"));
        JLabel cImg = new JLabel(c);
        cImg.setBounds(20, 474, 100, 100);
        container.add(current_Life);
        container.add(max_Life);
        container.add(current_Mana);
        container.add(max_Mana);
        container.add(money);
        container.add(fireProtection);
        container.add(iceProtection);
        container.add(earthProtection);
        container.add(level);
        container.add(experience);
        container.add(upButton);
        container.add(downButton);
        container.add(leftButton);
        container.add(rightButton);
        container.add(desc);
        container.add(cImg);
    }
    public String printStory(Cell c,JSONArray stories){
        for(int i=0;i<stories.length();i++){
            JSONObject story = stories.getJSONObject(i);
            if(story.getString("type").equals(c.getInteraction().getClass().getSimpleName().toUpperCase()))
            {
                String st = story.getString("value");
                stories.remove(i);
                return st;
            }
        }
        return "No more stories for this type of field :). You are in an "+c.getInteraction().getClass().getSimpleName()+" cell.";
    }
    public void setShopView(){
        JLabel current_Life = new JLabel("Life: " + character.getCurrentLife());
        current_Life.setBounds(20, 20, 79, 19);
        JLabel max_Life = new JLabel(" / " + character.getMaxLife());
        max_Life.setBounds(100, 20, 79, 19);
        JLabel current_Mana = new JLabel("Mana: " + character.getCurrentMana());
        current_Mana.setBounds(20, 40, 79, 19);
        JLabel max_Mana = new JLabel(" / " + character.getMaxMana());
        max_Mana.setBounds(100, 40, 79, 19);
        JLabel money = new JLabel("Money: " + character.getCharacterInventory().getMoney());
        money.setBounds(20, 60, 79, 19);
        JLabel fireProtection = new JLabel("Fire Protection: " + character.getFireProtection());
        fireProtection.setBounds(20, 80, 149, 19);
        JLabel iceProtection = new JLabel("Ice Protection: " + character.getIceProtection());
        iceProtection.setBounds(20, 100, 149, 19);
        JLabel earthProtection = new JLabel("Earth Protection: " + character.getEarthProtection());
        earthProtection.setBounds(20, 120, 149, 19);
        JLabel level = new JLabel("Level: " + character.getLevel());
        level.setBounds(20, 140, 79, 19);
        JLabel experience = new JLabel("Experience: " + character.getExp());
        experience.setBounds(20, 160, 109, 19);
        JLabel space = new JLabel("Space left: "+character.getCharacterInventory().getEmptyWeight());
        space.setBounds(20,180,109,19);
        JLabel title = new JLabel("Shop");
        title.setBounds(300,20,50,19);
        container.add(current_Life);
        container.add(max_Life);
        container.add(current_Mana);
        container.add(max_Mana);
        container.add(money);
        container.add(fireProtection);
        container.add(iceProtection);
        container.add(earthProtection);
        container.add(level);
        container.add(experience);
        container.add(title);
        container.add(space);
        int i;
        int xoffset = 300;
        int yoffset = 40;
        for(i=0;i<((Shop) game_map.getCurrent().getInteraction()).getShopInventory().size();i++){
            ImageIcon potion = new ImageIcon("./Imagini/"+((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(i).getClass().getSimpleName()+".png");
            JLabel potionL = new JLabel(potion);
            potionL.setBounds(xoffset,yoffset+51*i,50,50);
            JLabel potionN = new JLabel(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(i).getClass().getSimpleName());
            potionN.setBounds(xoffset+51,yoffset+51*i,80,30);
            JLabel potionV = new JLabel(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(i).getClass().getSimpleName()+" restore value: "+((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(i).getValue());
            potionV.setBounds(xoffset+132,yoffset+51*i,180,30);
            JLabel potionC = new JLabel("Cost: "+((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(i).getPrice());
            potionC.setBounds(xoffset+313,yoffset+51*i,60,30);
            JLabel potionW = new JLabel("Weight: "+((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(i).getWeight());
            potionW.setBounds(xoffset+374,yoffset+51*i,70,30);
            JButton buyB = new JButton("BUY");
            buyB.setBounds(xoffset+445,yoffset+51*i,70,30);
            buyB.setActionCommand("Buy nr "+i);
            buyB.addActionListener(this);
            container.add(potionL);
            container.add(potionN);
            container.add(potionV);
            container.add(potionC);
            container.add(potionW);
            container.add(buyB);
        }
        JButton backB = new JButton("BACK");
        backB.setBounds(xoffset,yoffset+51*i,100,20);
        backB.setActionCommand("Back from Shop");
        backB.addActionListener(this);
        container.add(backB);
    }
    public void setEndView(){
        container.removeAll();
        container.repaint();
        JLabel current_Life = new JLabel("Life: " + character.getCurrentLife());
        current_Life.setBounds(20, 20, 79, 19);
        JLabel max_Life = new JLabel(" / " + character.getMaxLife());
        max_Life.setBounds(100, 20, 79, 19);
        JLabel current_Mana = new JLabel("Mana: " + character.getCurrentMana());
        current_Mana.setBounds(20, 40, 79, 19);
        JLabel max_Mana = new JLabel(" / " + character.getMaxMana());
        max_Mana.setBounds(100, 40, 79, 19);
        JLabel money = new JLabel("Money: " + character.getCharacterInventory().getMoney());
        money.setBounds(20, 60, 79, 19);
        JLabel fireProtection = new JLabel("Fire Protection: " + character.getFireProtection());
        fireProtection.setBounds(20, 80, 149, 19);
        JLabel iceProtection = new JLabel("Ice Protection: " + character.getIceProtection());
        iceProtection.setBounds(20, 100, 149, 19);
        JLabel earthProtection = new JLabel("Earth Protection: " + character.getEarthProtection());
        earthProtection.setBounds(20, 120, 149, 19);
        JLabel level = new JLabel("Level: " + character.getLevel());
        level.setBounds(20, 140, 79, 19);
        JLabel experience = new JLabel("Experience: " + character.getExp());
        experience.setBounds(20, 160, 109, 19);
        int xoffset = 300;
        int yoffset = 10;
        int i, j;
        ImageIcon cellImg = new ImageIcon();
        for (i = 0; i < game_map.getLength(); i++) {
            for (j = 0; j < game_map.getWidth(); j++) {
                if(i==game_map.getCurrent().getOx()&&j==game_map.getCurrent().getOy())
                    cellImg = new ImageIcon("./Imagini/Player.png");
                else if (game_map.get(i).get(j).isVisited() == false)
                    cellImg = new ImageIcon("./Imagini/question.png");
                else
                    cellImg = new ImageIcon("./Imagini/" + game_map.get(i).get(j).getInteraction().getClass().getSimpleName() + ".png");
                JLabel image = new JLabel(cellImg);
                image.setBounds(xoffset + j * 101, yoffset + i * 101, 100, 100);
                container.add(image);
            }
        }
        if(game_map.getCurrent().getOldVisited()==false) {
            JTextArea story = new JTextArea(printStory(game_map.getCurrent(), stories));
            story.setLineWrap(true);
            story.setWrapStyleWord(true);
            story.setBounds(20,180,149,113);
            container.add(story);
            game_map.getCurrent().setOldVisited(true);
        }
        else{
            JTextArea story = new JTextArea();
            story.setLineWrap(true);
            story.setWrapStyleWord(true);
            story.setBounds(20,180,149,113);
            container.add(story);
        }
        JLabel desc = new JLabel("Current cell");
        desc.setBounds(20,453,100,20);
        ImageIcon c = new ImageIcon(("./Imagini/" + game_map.get(game_map.getCurrent().getOx()).get(game_map.getCurrent().getOy()).getInteraction().getClass().getSimpleName() + ".png"));
        JLabel cImg = new JLabel(c);
        cImg.setBounds(20, 474, 100, 100);
        JButton end = new JButton("EXIT");
        end.setBounds(1100,20,100,20);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        end.setActionCommand("exit");
        end.addActionListener(this);
        container.add(current_Life);
        container.add(max_Life);
        container.add(current_Mana);
        container.add(max_Mana);
        container.add(money);
        container.add(fireProtection);
        container.add(iceProtection);
        container.add(earthProtection);
        container.add(level);
        container.add(experience);
        container.add(desc);
        container.add(cImg);
        container.add(end);
    }
    public void setBattleView(int move){
        container.removeAll();
        container.repaint();
        Enemy e = (Enemy) game_map.getCurrent().getInteraction();
        JLabel current_Life = new JLabel("Life: " + character.getCurrentLife());
        current_Life.setBounds(20, 20, 79, 19);
        JLabel max_Life = new JLabel(" / " + character.getMaxLife());
        max_Life.setBounds(100, 20, 79, 19);
        JLabel current_Mana = new JLabel("Mana: " + character.getCurrentMana());
        current_Mana.setBounds(20, 40, 79, 19);
        JLabel max_Mana = new JLabel(" / " + character.getMaxMana());
        max_Mana.setBounds(100, 40, 79, 19);
        JLabel money = new JLabel("Money: " + character.getCharacterInventory().getMoney());
        money.setBounds(20, 60, 79, 19);
        JLabel fireProtection = new JLabel("Fire Protection: " + character.getFireProtection());
        fireProtection.setBounds(20, 80, 149, 19);
        JLabel iceProtection = new JLabel("Ice Protection: " + character.getIceProtection());
        iceProtection.setBounds(20, 100, 149, 19);
        JLabel earthProtection = new JLabel("Earth Protection: " + character.getEarthProtection());
        earthProtection.setBounds(20, 120, 149, 19);
        JLabel level = new JLabel("Level: " + character.getLevel());
        level.setBounds(20, 140, 79, 19);
        JLabel experience = new JLabel("Experience: " + character.getExp());
        experience.setBounds(20, 160, 109, 19);
        ImageIcon player = new ImageIcon("./Imagini/"+character.getClass().getSimpleName()+"_big.png");
        JLabel playerImg = new JLabel(player);
        playerImg.setBounds(350,100,200,200);
        JLabel enemy_current_Life = new JLabel("Enemy life: " + e.getCurrentLife());
        enemy_current_Life.setBounds(1000, 20, 99, 19);
        JLabel enemy_max_Life = new JLabel(" / " + e.getMaxLife());
        enemy_max_Life.setBounds(1100, 20, 79, 19);
        JLabel enemy_current_Mana = new JLabel("Enemy mana: " + e.getCurrentMana());
        enemy_current_Mana.setBounds(1000, 40, 109, 19);
        JLabel enemy_max_Mana = new JLabel(" / " + e.getMaxMana());
        enemy_max_Mana.setBounds(1100, 40, 79, 19);
        JLabel enemy_fireProtection = new JLabel("Enemy fire Protection: " + e.getFireProtection());
        enemy_fireProtection.setBounds(1000, 60, 189, 19);
        JLabel enemy_iceProtection = new JLabel("Enemy ice Protection: " + e.getIceProtection());
        enemy_iceProtection.setBounds(1000, 80, 189, 19);
        JLabel enemy_earthProtection = new JLabel("Enemy earth Protection: " + e.getEarthProtection());
        enemy_earthProtection.setBounds(1000, 100, 189, 19);
        ImageIcon en = new ImageIcon("./Imagini/Enemy_big.png");
        JLabel enemyImg = new JLabel(en);
        enemyImg.setBounds(750,100,200,200);
        ImageIcon vs = new ImageIcon("./Imagini/VS.png");
        JLabel vsImg = new JLabel(vs);
        vsImg.setBounds(600,150,100,100);
        int i;
        int xoffset=20;
        int yoffset=200;
        int yyoffset=120;
        for(i=0;i<character.getCharacterInventory().getInventoryPotions().size();i++){
            JLabel potion = new JLabel(character.getCharacterInventory().getInventoryPotions().get(i).getClass().getSimpleName()+" Regen: "+character.getCharacterInventory().getInventoryPotions().get(i).getValue());
            potion.setBounds(xoffset,yoffset+i*20,150,19);
            container.add(potion);
            if(move==1) {
                JButton useP = new JButton("USE");
                useP.setActionCommand("Use potion " + i);
                useP.addActionListener(this);
                useP.setBounds(171, yoffset + i * 20, 70, 19);
                container.add(useP);
            }
        }
        yoffset=yoffset+i*20;
        for(i=0;i<character.getAbilities().size();i++){
            JLabel ability = new JLabel(character.getAbilities().get(i).getClass().getSimpleName()+" spell, Mana Cost "+character.getAbilities().get(i).getManaCost()+", Damage: "+character.getAbilities().get(i).getDamage());
            ability.setBounds(xoffset,yoffset+i*20,230,19);
            container.add(ability);
            if(move==1){
                JButton useA = new JButton("USE");
                useA.setActionCommand("Use spell "+i);
                useA.addActionListener(this);
                useA.setBounds(251,yoffset+i*20,60,19);
                container.add(useA);
            }
        }
        for(i=0;i<e.getAbilities().size();i++){
            JLabel ability = new JLabel(e.getAbilities().get(i).getClass().getSimpleName()+" spell, Mana Cost "+e.getAbilities().get(i).getManaCost()+", Damage: "+e.getAbilities().get(i).getDamage());
            ability.setBounds(1000,yyoffset+i*20,230,19);
            container.add(ability);
        }
        container.add(current_Life);
        container.add(max_Life);
        container.add(current_Mana);
        container.add(max_Mana);
        container.add(money);
        container.add(fireProtection);
        container.add(iceProtection);
        container.add(earthProtection);
        container.add(level);
        container.add(experience);
        container.add(playerImg);
        container.add(enemy_current_Life);
        container.add(enemy_max_Life);
        container.add(enemy_current_Mana);
        container.add(enemy_max_Mana);
        container.add(enemy_fireProtection);
        container.add(enemy_iceProtection);
        container.add(enemy_earthProtection);
        container.add(enemyImg);
        container.add(vsImg);
        if(move==2){
            JButton next = new JButton("Enemy Attack");
            next.setActionCommand("Enemy turn");
            next.addActionListener(this);
            next.setBounds(750,400,150,20);
            container.add(next);
        }
        else if(move==1){
            JButton attack = new JButton("Attack");
            attack.setActionCommand("Attack");
            attack.addActionListener(this);
            attack.setBounds(20,180,100,19);
            container.add(attack);
        }
    }
    public void setLostView(){
        container.removeAll();
        container.repaint();
        Enemy e = (Enemy) game_map.getCurrent().getInteraction();
        JLabel current_Life = new JLabel("Life: " + character.getCurrentLife());
        current_Life.setBounds(20, 20, 79, 19);
        JLabel max_Life = new JLabel(" / " + character.getMaxLife());
        max_Life.setBounds(100, 20, 79, 19);
        JLabel current_Mana = new JLabel("Mana: " + character.getCurrentMana());
        current_Mana.setBounds(20, 40, 79, 19);
        JLabel max_Mana = new JLabel(" / " + character.getMaxMana());
        max_Mana.setBounds(100, 40, 79, 19);
        JLabel money = new JLabel("Money: " + character.getCharacterInventory().getMoney());
        money.setBounds(20, 60, 79, 19);
        JLabel fireProtection = new JLabel("Fire Protection: " + character.getFireProtection());
        fireProtection.setBounds(20, 80, 149, 19);
        JLabel iceProtection = new JLabel("Ice Protection: " + character.getIceProtection());
        iceProtection.setBounds(20, 100, 149, 19);
        JLabel earthProtection = new JLabel("Earth Protection: " + character.getEarthProtection());
        earthProtection.setBounds(20, 120, 149, 19);
        JLabel level = new JLabel("Level: " + character.getLevel());
        level.setBounds(20, 140, 79, 19);
        JLabel experience = new JLabel("Experience: " + character.getExp());
        experience.setBounds(20, 160, 109, 19);
        ImageIcon player = new ImageIcon("./Imagini/"+character.getClass().getSimpleName()+"_big.png");
        JLabel playerImg = new JLabel(player);
        playerImg.setBounds(300,100,200,200);
        JLabel enemy_current_Life = new JLabel("Enemy life: " + e.getCurrentLife());
        enemy_current_Life.setBounds(1000, 20, 99, 19);
        JLabel enemy_max_Life = new JLabel(" / " + e.getMaxLife());
        enemy_max_Life.setBounds(1100, 20, 79, 19);
        JLabel enemy_current_Mana = new JLabel("Enemy mana: " + e.getCurrentMana());
        enemy_current_Mana.setBounds(1000, 40, 99, 19);
        JLabel enemy_max_Mana = new JLabel(" / " + e.getMaxMana());
        enemy_max_Mana.setBounds(1100, 40, 79, 19);
        JLabel enemy_fireProtection = new JLabel("Enemy fire Protection: " + e.getFireProtection());
        enemy_fireProtection.setBounds(1000, 60, 189, 19);
        JLabel enemy_iceProtection = new JLabel("Enemy ice Protection: " + e.getIceProtection());
        enemy_iceProtection.setBounds(1000, 80, 189, 19);
        JLabel enemy_earthProtection = new JLabel("Enemy earth Protection: " + e.getEarthProtection());
        enemy_earthProtection.setBounds(1000, 100, 189, 19);
        ImageIcon en = new ImageIcon("./Imagini/Enemy_big.png");
        JLabel enemyImg = new JLabel(en);
        enemyImg.setBounds(700,100,200,200);
        ImageIcon vs = new ImageIcon("./Imagini/VS.png");
        JLabel vsImg = new JLabel(vs);
        vsImg.setBounds(550,150,100,100);
        JButton end = new JButton("EXIT");
        end.setBounds(550,400,100,20);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        end.setActionCommand("exit");
        end.addActionListener(this);
        container.add(current_Life);
        container.add(max_Life);
        container.add(current_Mana);
        container.add(max_Mana);
        container.add(money);
        container.add(fireProtection);
        container.add(iceProtection);
        container.add(earthProtection);
        container.add(level);
        container.add(experience);
        container.add(playerImg);
        container.add(enemy_current_Life);
        container.add(enemy_max_Life);
        container.add(enemy_current_Mana);
        container.add(enemy_max_Mana);
        container.add(enemy_fireProtection);
        container.add(enemy_iceProtection);
        container.add(enemy_earthProtection);
        container.add(enemyImg);
        container.add(vsImg);
        container.add(end);
    }
    public void actionPerformed(ActionEvent a) {
        if(a.getActionCommand().equals("up")){
            if(game_map.getCurrent().getOx()==0){
                JOptionPane.showMessageDialog(this,"Cannot move up");
            }
            else{
                container.removeAll();
                container.repaint();
                boolean newCell = game_map.goNorth();
                setGridView();//game_map,character,stories);
                if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Shop")){
                    JOptionPane.showMessageDialog(this,"You found a shop");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    container.removeAll();
                    container.repaint();
                    setShopView();//game_map,character,stories);
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Empty")){
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Finish")){
                    JOptionPane.showMessageDialog(this,"Congrats, you finished the game");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    ending=true;
                    container.removeAll();
                    container.repaint();
                    setEndView();
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Enemy")){
                    JOptionPane.showMessageDialog(this,"You encountered an enemy");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    container.removeAll();
                    container.repaint();
                    setBattleView(1);
                }
            }
        }
        else if(a.getActionCommand().equals("down")){
            if(game_map.getCurrent().getOx()==game_map.getLength()-1){
                JOptionPane.showMessageDialog(this,"Cannot move down");
            }
            else{
                container.removeAll();
                container.repaint();
                boolean newCell= game_map.goSouth();
                setGridView();//game_map,character,stories);
                if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Shop")){
                    JOptionPane.showMessageDialog(this,"You found a shop");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    container.removeAll();
                    container.repaint();
                    setShopView();//game_map,character,stories);
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Empty")){
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Finish")){
                    JOptionPane.showMessageDialog(this,"Congrats, you finished the game");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    ending=true;
                    container.removeAll();
                    container.repaint();
                    setEndView();
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Enemy")){
                    JOptionPane.showMessageDialog(this,"You encountered an enemy");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    container.removeAll();
                    container.repaint();
                    setBattleView(1);
                }
            }
        }
        else if(a.getActionCommand().equals("left")){
            if(game_map.getCurrent().getOy()==0){
                JOptionPane.showMessageDialog(this,"Cannot move left");
            }
            else{
                container.removeAll();
                container.repaint();
                boolean newCell=game_map.goWest();
                setGridView();//game_map,character,stories);
                if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Shop")){
                    JOptionPane.showMessageDialog(this,"You found a shop");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    container.removeAll();
                    container.repaint();
                    setShopView();//game_map,character,stories);
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Empty")){
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Finish")){
                    JOptionPane.showMessageDialog(this,"Congrats, you finished the game");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    ending=true;
                    container.removeAll();
                    container.repaint();
                    setEndView();
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Enemy")){
                    JOptionPane.showMessageDialog(this,"You encountered an enemy");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    container.removeAll();
                    container.repaint();
                    setBattleView(1);
                }
            }
        }
        else if(a.getActionCommand().equals("right")){
            if(game_map.getCurrent().getOy()==game_map.getWidth()-1){
                JOptionPane.showMessageDialog(this,"Cannot move right");
            }
            else{
                container.removeAll();
                container.repaint();
                boolean newCell = game_map.goEast();
                setGridView();//game_map,character,stories);
                if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Shop")){
                    JOptionPane.showMessageDialog(this,"You found a shop");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    container.removeAll();
                    container.repaint();
                    setShopView();//game_map,character,stories);
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Empty")){
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Finish")){
                    JOptionPane.showMessageDialog(this,"Congrats, you finished the game");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    ending=true;
                    container.removeAll();
                    container.repaint();
                    setEndView();
                }
                else if(game_map.getCurrent().getInteraction().getClass().getSimpleName().equals("Enemy")){
                    JOptionPane.showMessageDialog(this,"You encountered an enemy");
                    if(newCell==true) {
                        character.setExp(character.getExp() + 5);
                        character.checkLevel();
                    }
                    container.removeAll();
                    container.repaint();
                    setBattleView(1);
                }
            }
        }
        else if(a.getActionCommand().equals("Back from Shop")){
            setGridView();
        }
        else if(a.getActionCommand().equals("Buy nr 0")){
            if(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(0).getWeight() > character.getCharacterInventory().getEmptyWeight())
                JOptionPane.showMessageDialog(this,"Cannot buy potion, not enough space in inventory.");
            else if(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(0).getPrice() > character.getCharacterInventory().getMoney())
                JOptionPane.showMessageDialog(this,"Cannot buy potion, not enough money to pay it");
            else{
                Potion p = ((Shop) game_map.getCurrent().getInteraction()).select(0);
                character.buyPotion(p);
                JOptionPane.showMessageDialog(this,"You bought a potion");
                container.removeAll();
                container.repaint();
                setShopView();
            }
        }
        else if(a.getActionCommand().equals("Buy nr 1")){
            if(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(1).getWeight() > character.getCharacterInventory().getEmptyWeight())
                JOptionPane.showMessageDialog(this,"Cannot buy potion, not enough space in inventory.");
            else if(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(1).getPrice() > character.getCharacterInventory().getMoney())
                JOptionPane.showMessageDialog(this,"Cannot buy potion, not enough money to pay it");
            else{
                Potion p = ((Shop) game_map.getCurrent().getInteraction()).select(1);
                character.buyPotion(p);
                JOptionPane.showMessageDialog(this,"You bought a potion");
                container.removeAll();
                container.repaint();
                setShopView();
            }
        }
        else if(a.getActionCommand().equals("Buy nr 2")){
            if(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(2).getWeight() > character.getCharacterInventory().getEmptyWeight())
                JOptionPane.showMessageDialog(this,"Cannot buy potion, not enough space in inventory.");
            else if(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(2).getPrice() > character.getCharacterInventory().getMoney())
                JOptionPane.showMessageDialog(this,"Cannot buy potion, not enough money to pay it");
            else{
                Potion p = ((Shop) game_map.getCurrent().getInteraction()).select(2);
                character.buyPotion(p);
                JOptionPane.showMessageDialog(this,"You bought a potion");
                container.removeAll();
                container.repaint();
                setShopView();
            }
        }
        else if(a.getActionCommand().equals("Buy nr 3")){
            if(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(3).getWeight() > character.getCharacterInventory().getEmptyWeight())
                JOptionPane.showMessageDialog(this,"Cannot buy potion, not enough space in inventory.");
            else if(((Shop) game_map.getCurrent().getInteraction()).getShopInventory().get(3).getPrice() > character.getCharacterInventory().getMoney())
                JOptionPane.showMessageDialog(this,"Cannot buy potion, not enough money to pay it");
            else{
                Potion p = ((Shop) game_map.getCurrent().getInteraction()).select(3);
                character.buyPotion(p);
                JOptionPane.showMessageDialog(this,"You bought a potion");
                container.removeAll();
                container.repaint();
                setShopView();
            }
        }
        else if(a.getActionCommand().equals("Enemy turn")){
            int checkForAvailableSpells=-1;
            Enemy e =(Enemy) game_map.getCurrent().getInteraction();
            Character c = character;
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
                JOptionPane.showMessageDialog(this,"You lost");
                container.removeAll();
                container.repaint();
                setLostView();
            }
            else{
                c.manaRegen(5);
                c.healthRegen(5);
                e.healthRegen(10);
                e.manaRegen(10);
                setBattleView(1);
            }
        }
        else if(a.getActionCommand().equals("Attack")){
            Enemy e =(Enemy) game_map.getCurrent().getInteraction();
            Character c = character;
            e.receiveDamage(c.getDamage());
            if(e.getCurrentLife()<=0){
                JOptionPane.showMessageDialog(this,"You won the battle");
                container.removeAll();
                container.repaint();
                c.setExp(c.getExp()+e.getMaxLife());
                c.checkLevel();
                Random rand =new Random();
                int money_chance=rand.nextInt(5);
                if(money_chance!=0){
                    c.getCharacterInventory().setMoney(c.getCharacterInventory().getMoney()+e.getMaxLife());
                }
                game_map.get(game_map.getCurrent().getOx()).get(game_map.getCurrent().getOy()).setCellType(new Empty());
                setGridView();
                wonBattles++;
            }
            else{
                setBattleView(2);
            }
        }
        else if(a.getActionCommand().equals("Use potion 0")){
            Potion p = character.getCharacterInventory().getInventoryPotions().get(0);
            if(p.getClass().getSimpleName()=="ManaPotion")
                ((ManaPotion)p).usePotion(character);
            else if(p.getClass().getSimpleName()=="HealthPotion")
                ((HealthPotion)p).usePotion(character);
            container.removeAll();
            container.repaint();
            setBattleView(2);
        }
        else if(a.getActionCommand().equals("Use potion 1")){
            Potion p = character.getCharacterInventory().getInventoryPotions().get(1);
            if(p.getClass().getSimpleName()=="ManaPotion")
                ((ManaPotion)p).usePotion(character);
            else if(p.getClass().getSimpleName()=="HealthPotion")
                ((HealthPotion)p).usePotion(character);
            container.removeAll();
            container.repaint();
            setBattleView(2);
        }
        else if(a.getActionCommand().equals("Use potion 2")){
            Potion p = character.getCharacterInventory().getInventoryPotions().get(2);
            if(p.getClass().getSimpleName()=="ManaPotion")
                ((ManaPotion)p).usePotion(character);
            else if(p.getClass().getSimpleName()=="HealthPotion")
                ((HealthPotion)p).usePotion(character);
            container.removeAll();
            container.repaint();
            setBattleView(2);
        }
        else if(a.getActionCommand().equals("Use potion 3")){
            Potion p = character.getCharacterInventory().getInventoryPotions().get(3);
            if(p.getClass().getSimpleName()=="ManaPotion")
                ((ManaPotion)p).usePotion(character);
            else if(p.getClass().getSimpleName()=="HealthPotion")
                ((HealthPotion)p).usePotion(character);
            container.removeAll();
            container.repaint();
            setBattleView(2);
        }
        else if(a.getActionCommand().equals("Use potion 4")){
            Potion p = character.getCharacterInventory().getInventoryPotions().get(4);
            if(p.getClass().getSimpleName()=="ManaPotion")
                ((ManaPotion)p).usePotion(character);
            else if(p.getClass().getSimpleName()=="HealthPotion")
                ((HealthPotion)p).usePotion(character);
            container.removeAll();
            container.repaint();
            setBattleView(2);
        }
        else if(a.getActionCommand().equals("Use potion 5")){
            Potion p = character.getCharacterInventory().getInventoryPotions().get(5);
            if(p.getClass().getSimpleName()=="ManaPotion")
                ((ManaPotion)p).usePotion(character);
            else if(p.getClass().getSimpleName()=="HealthPotion")
                ((HealthPotion)p).usePotion(character);
            container.removeAll();
            container.repaint();
            setBattleView(2);
        }
        else if(a.getActionCommand().equals("Use potion 6")){
            Potion p = character.getCharacterInventory().getInventoryPotions().get(6);
            if(p.getClass().getSimpleName()=="ManaPotion")
                ((ManaPotion)p).usePotion(character);
            else if(p.getClass().getSimpleName()=="HealthPotion")
                ((HealthPotion)p).usePotion(character);
            container.removeAll();
            container.repaint();
            setBattleView(2);
        }
        else if(a.getActionCommand().equals("Use spell 0")){
            character.useSpell(character.getAbilities().get(0),((Enemy) game_map.getCurrent().getInteraction()));
            if(((Enemy) game_map.getCurrent().getInteraction()).getCurrentLife()<=0){
                JOptionPane.showMessageDialog(this,"You won the battle");
                container.removeAll();
                container.repaint();
                character.setExp(character.getExp()+((Enemy) game_map.getCurrent().getInteraction()).getMaxLife());
                character.checkLevel();
                Random rand =new Random();
                int money_chance=rand.nextInt(5);
                if(money_chance!=0){
                    character.getCharacterInventory().setMoney(character.getCharacterInventory().getMoney()+((Enemy) game_map.getCurrent().getInteraction()).getMaxLife());
                }
                game_map.get(game_map.getCurrent().getOx()).get(game_map.getCurrent().getOy()).setCellType(new Empty());
                setGridView();
                wonBattles++;
            }
            else{
                setBattleView(2);
            }
        }
        else if(a.getActionCommand().equals("Use spell 1")){
            character.useSpell(character.getAbilities().get(1),((Enemy) game_map.getCurrent().getInteraction()));
            if(((Enemy) game_map.getCurrent().getInteraction()).getCurrentLife()<=0){
                JOptionPane.showMessageDialog(this,"You won the battle");
                container.removeAll();
                container.repaint();
                character.setExp(character.getExp()+((Enemy) game_map.getCurrent().getInteraction()).getMaxLife());
                character.checkLevel();
                Random rand =new Random();
                int money_chance=rand.nextInt(5);
                if(money_chance!=0){
                    character.getCharacterInventory().setMoney(character.getCharacterInventory().getMoney()+((Enemy) game_map.getCurrent().getInteraction()).getMaxLife());
                }
                game_map.get(game_map.getCurrent().getOx()).get(game_map.getCurrent().getOy()).setCellType(new Empty());
                setGridView();
                wonBattles++;
            }
            else{
                setBattleView(2);
            }
        }
        else if(a.getActionCommand().equals("Use spell 2")){
            character.useSpell(character.getAbilities().get(2),((Enemy) game_map.getCurrent().getInteraction()));
            if(((Enemy) game_map.getCurrent().getInteraction()).getCurrentLife()<=0){
                JOptionPane.showMessageDialog(this,"You won the battle");
                container.removeAll();
                container.repaint();
                character.setExp(character.getExp()+((Enemy) game_map.getCurrent().getInteraction()).getMaxLife());
                character.checkLevel();
                Random rand =new Random();
                int money_chance=rand.nextInt(5);
                if(money_chance!=0){
                    character.getCharacterInventory().setMoney(character.getCharacterInventory().getMoney()+((Enemy) game_map.getCurrent().getInteraction()).getMaxLife());
                }
                game_map.get(game_map.getCurrent().getOx()).get(game_map.getCurrent().getOy()).setCellType(new Empty());
                setGridView();
                wonBattles++;
            }
            else{
                setBattleView(2);
            }
        }
        else if(a.getActionCommand().equals("Use spell 3")){
            character.useSpell(character.getAbilities().get(3),((Enemy) game_map.getCurrent().getInteraction()));
            if(((Enemy) game_map.getCurrent().getInteraction()).getCurrentLife()<=0){
                JOptionPane.showMessageDialog(this,"You won the battle");
                container.removeAll();
                container.repaint();
                character.setExp(character.getExp()+((Enemy) game_map.getCurrent().getInteraction()).getMaxLife());
                character.checkLevel();
                Random rand =new Random();
                int money_chance=rand.nextInt(5);
                if(money_chance!=0){
                    character.getCharacterInventory().setMoney(character.getCharacterInventory().getMoney()+((Enemy) game_map.getCurrent().getInteraction()).getMaxLife());
                }
                game_map.get(game_map.getCurrent().getOx()).get(game_map.getCurrent().getOy()).setCellType(new Empty());
                setGridView();
                wonBattles++;
            }
            else{
                setBattleView(2);
            }
        }
        else if(a.getActionCommand().equals("exit")){
            dispose();
        }
    }
}
