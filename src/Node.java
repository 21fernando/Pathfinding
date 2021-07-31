import java.util.ArrayList;

public class Node{

    private int row;
    private int col;
    private Type type;
    private ArrayList<Node> adjacencies;
    private boolean visited;

    public enum Type{
        START,
        GOAL,
        WALL,
        OPEN,
        PATH,
        DEBUG
    }

    public Node(int row, int col, Type type){
        this.row = row;
        this.col = col;
        this.type = type;
        visited = false;
    }

    public void addNeighbour(Node other){
        adjacencies.add(other);
    }

    public ArrayList<Node> getAdjacencies(){
        return adjacencies;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public Type getType(){
        return type;
    }

    public boolean isVisited(){ return visited; }

    public void visit(){ visited = true; }

    public void setType(Node.Type type){ this.type = type;}

    public String toString(){
        return "(" + row + ", " + col + ") " + type;
    }

}
