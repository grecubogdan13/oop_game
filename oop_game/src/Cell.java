public class Cell {
    enum Type{
        EMPTY,
        ENEMY,
        SHOP,
        FINISH
    }
    private int Ox;
    private int Oy;
    private CellElement interaction;
    private boolean visited;
    private boolean oldVisited;
    private Type cellType;
    public Cell(int x,int y, CellElement e){
        Ox=x;
        Oy=y;
        interaction=e;
        if(interaction.getClass().getSimpleName()=="Enemy")
            cellType=Type.ENEMY;
        else if(interaction.getClass().getSimpleName()=="Shop")
            cellType=Type.SHOP;
        else if(interaction.getClass().getSimpleName()=="Finish")
            cellType=Type.FINISH;
        else
            cellType=Type.EMPTY;
        visited=false;
        oldVisited=false;
    }
    public CellElement getInteraction(){
        return interaction;
    }
    public boolean isVisited() {
        return visited;
    }
    public void setVisited(boolean v) {
        visited=v;
    }
    public boolean getOldVisited() {
        return oldVisited;
    }
    public void setOldVisited(boolean v) {
        oldVisited=v;
    }
    public int getOx(){
        return Ox;
    }
    public int getOy(){
        return Oy;
    }
    public void setPosition(int x, int y){
        Ox=x;
        Oy=y;
    }
    public void setCellType(CellElement e){
        interaction=e;
        if(interaction.getClass().getSimpleName()=="Enemy")
            cellType=Type.ENEMY;
        else if(interaction.getClass().getSimpleName()=="Shop")
            cellType=Type.SHOP;
        else if(interaction.getClass().getSimpleName()=="Finish")
            cellType=Type.FINISH;
        else
            cellType=Type.EMPTY;
    }
}
