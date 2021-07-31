import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;


public class Visualization extends Application{

    private Pane root;
    private Node[][] map;
    private Rectangle[][] grid;
    private int dimension;
    private int margin;
    private Timer timer;
    private TimerTask task;
    private SequentialTransition seqt;
    private int speed;

    public Visualization(){
        root = new Pane();
        dimension = 10;
        margin = 10;
        grid = new Rectangle[dimension][dimension];
        AnimationTimer animationTimer = new TimerMethod();
        animationTimer.start();
        seqt = new SequentialTransition();
        speed = 100;
    }

    private class TimerMethod extends AnimationTimer{
        @Override
        public void handle(long now) {
            update();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("BFS");
        primaryStage.setScene(scene);
        primaryStage.show();
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
        int squareSize = (int) (((root.getPrefWidth()-margin)/(dimension)) - margin);
        for(int r =0; r<dimension; r++){
            for(int c=0; c<dimension; c++){
                Rectangle rect = new Rectangle(margin + (c*(margin + squareSize)),margin + (r*(margin + squareSize)), squareSize,squareSize);
                rect.setStroke(Color.BLACK);
                if(map[r][c].getType() == Node.Type.OPEN){
                    rect.setFill(Color.TRANSPARENT);
                }else if(map[r][c].getType() == Node.Type.START){
                    rect.setFill(Color.BLUE);
                }else if(map[r][c].getType() == Node.Type.GOAL) {
                    rect.setFill(Color.GREEN);
                }else if(map[r][c].getType() == Node.Type.WALL){
                    rect.setFill(Color.BLACK);
                }
                grid[r][c] = rect;
                root.getChildren().addAll(rect);
            }
        }
        bfs();
        seqt.play();
    }

    private void update(){

    }

    private Pane createContent(){
        root.setPrefSize(600,600);
        root.setMinSize(600,600);
        root.setMaxSize(600,600);
        return root;
    }

    public static void main(String[] args){
        Visualization v = new Visualization();
        launch(args);
    }

    private void initMap (String config){
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

    private ArrayList<Node> bfs () throws InterruptedException {
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
        ArrayList<int[]> neighbours;
        while(!queue.isEmpty()){
            Node node = queue.poll();
            FillTransition ft = new FillTransition(Duration.millis(speed),grid[node.getRow()][node.getCol()],Color.GRAY,Color.ORANGE);
            seqt.getChildren().add(ft);
            grid[node.getRow()][node.getCol()].setFill(Color.TURQUOISE);
            neighbours = new ArrayList<>();
            for(int r = node.getRow()-1; r<= node.getRow()+1; r++){
                for(int c = node.getCol()-1; c<= node.getCol()+1; c++){
                    if(r>=0 && c>=0 && r<dimension && c<dimension) {
                        if ((!map[r][c].isVisited()) && (map[r][c].getType() == Node.Type.OPEN || map[r][c].getType() == Node.Type.GOAL)) {
                            queue.add(map[r][c]);
                            map[r][c].visit();
                            System.out.println("HI");
                            ft = new FillTransition(Duration.millis(speed),grid[r][c],Color.WHITE,Color.TURQUOISE);
                            seqt.getChildren().add(ft);
                            parents[r][c] = node;
                            neighbours.add(new int[]{r,c});
                        }
                    }
                }
            }
            ParallelTransition pt = new ParallelTransition();
            for(int[] neighbour : neighbours){
                ft = new FillTransition(Duration.millis(speed),grid[neighbour[0]][neighbour[1]],Color.TURQUOISE,Color.GRAY);
                pt.getChildren().add(ft);
            }
            pt.getChildren().add(new FillTransition(Duration.millis(speed),grid[node.getRow()][node.getCol()],Color.ORANGE,Color.GRAY));
            seqt.getChildren().add(pt);
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

}
