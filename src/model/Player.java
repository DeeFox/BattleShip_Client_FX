package model;



public class Player {
    
    protected String name;
    protected Field field;
    
    public Player(String name) {
        this.name = name;
        this.field = new Field();
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Field getField() {
        return field;
    }
    public void setField(Field field) {
        this.field = field;
    }
}
