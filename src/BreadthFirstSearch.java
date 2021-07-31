import java.util.*;

public class BreadthFirstSearch {

    private static int dimension = 10;

    private static Node[][] map;

    private static void initMap (String config){
        map = new Node[dimension][dimension];
        for(int r=0; r<dimension; r++){
            for(int c=0; c<dimension; c++){
                switch (config.charAt((r*dimension)+c)){
                    case 'S':
                        map[r][c] = new Node(r,c, Node.Type.START);
                        break;
                    case 'G':
                        map[r][c] = new Node(r,c, Node.Type.GOAL);
                        break;
                    case 'W':
                        map[r][c] = new Node(r,c, Node.Type.WALL);
                        break;
                    case 'O':
                        map[r][c] = new Node(r,c, Node.Type.OPEN);
                        break;
                }
            }
        }
    }

    private static ArrayList<Node> bfs (){
        Queue<Node> queue = new LinkedList<Node>();
        Node parents[][] = new Node[dimension][dimension];
        Node start = null;
        Node goal = null;
        for(int r= 0 ; r<dimension; r++){
            for(int c = 0; c<dimension; c++){
                parents[r][c] = null;
                if(map[r][c].getType()==Node.Type.START){
                    queue.add(map[r][c]);
                    start = map[r][c];
                    start.visit();
                }else if(map[r][c].getType() == Node.Type.GOAL){
                    goal = map[r][c];
                }
            }
        }
        while(!queue.isEmpty()){
            Node node = queue.poll();
            for(int r = node.getRow()-1; r<= node.getRow()+1; r++){
                for(int c = node.getCol()-1; c<= node.getCol()+1; c++){
                    if(r>=0 && c>=0 && r<dimension && c<dimension) {
                        if ((!map[r][c].isVisited()) && (map[r][c].getType() == Node.Type.OPEN || map[r][c].getType() == Node.Type.GOAL)) {
                            queue.add(map[r][c]);
                            map[r][c].visit();
                            parents[r][c] = node;
                        }
                    }
                }
            }
        }

        ArrayList<Node> path = new ArrayList<>();
        for(Node current = goal; current != null; current = parents[current.getRow()][current.getCol()]){
            path.add(current);
            if(current.getType() == Node.Type.OPEN){ current.setType(Node.Type.PATH);}
        }
        Collections.reverse(path);
        if(path.get(0) == start ){
            return path;
        }
        return new ArrayList<Node>();
    }

    private static void printMap(){
        String row = "+";
        for(int i=0; i<dimension; i++){
            row = row+ "---+";
        }

        for(int r=0; r<dimension; r++) {
            System.out.println(row);
            System.out.print("|");
            for (int c = 0; c < dimension; c++) {
                switch (map[r][c].getType()){
                    case START:
                        System.out.print(" S ");
                        break;
                    case GOAL:
                        System.out.print(" G ");
                        break;
                    case OPEN:
                        System.out.print("   ");
                        break;
                    case WALL:
                        System.out.print(" X ");
                        break;
                    case PATH:
                        System.out.print("|||");
                        break;
                    case DEBUG:
                        System.out.print(" D ");
                        break;
                }
                System.out.print("|");
            }
            System.out.println();
        }
        System.out.println(row);
    }

    public static void main(String[] args) {
        String mapState = "S";
        for(int i=1; i<(dimension*dimension)-1;i++){
            if((i>20 && i<25)|| (i>81&&i<89)){
                mapState = mapState + "W";
            }else {
                mapState = mapState + "O";
            }
        }
        mapState = mapState + "G";
        initMap(mapState);
        printMap();
        //queueTest();
        System.out.println(bfs());
        printMap();
    }

    public static Node[][] getMap() {
        return map;
    }
}
