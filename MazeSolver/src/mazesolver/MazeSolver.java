package mazesolver;
import java.util.*;
import java.io.*;  
        

public class MazeSolver {
    private File maze;
    private Scanner reader;
    private int width;
    private int height;
    private int startx;
    private int starty;
    private int endx;
    private int endy;
    
    //Maze stored in 2d char array
    private char mymaze[][];
    
    //Store the route from start to end, each coordinate is stored as int array, {1,1}
    private ArrayList<int[]> route; 
    
    //Store alterative directions for each coordinate in route
    private ArrayList<ArrayList<int[]>> optionaldir;    
    
    private int numofstep;     //Store number of steps taken in route.
    
    //Store the coordinate for south,east,north,west respectively.
    private int[][] direction = {{0,1},{1,0},{0,-1},{-1,0}};
    
    /*Read file and store all initial data from file*/
    public void readmazedata(String mazefile) throws FileNotFoundException{
        maze = new File(mazefile);
        reader = new Scanner(maze);
        width = reader.nextInt();
        height = reader.nextInt();
        startx = reader.nextInt();
        starty = reader.nextInt();
        endx = reader.nextInt();
        endy = reader.nextInt();
    }
    
    /*Using the file, this method read and convert the maze from integer to char*/
    public void buildmymaze(){
        mymaze = new char[width][height];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int block = reader.nextInt();
                if (block == 1){
                    mymaze[j][i] = '#';
                }
                else{
                    mymaze[j][i] = ' ';
                }
            }
        }
        
        mymaze[startx][starty] = 'S';
        mymaze[endx][endy] = 'E';
    }
    
    /*Print out the maze stored in this class*/
    public void printmymaze(){
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                System.out.print(mymaze[j][i]);
            }                
            System.out.println();
        }
        System.out.println();
    }
    
    /*
    *This method finds a path from start to goal of the maze using DFS concept.
    *Each coordinate in the maze is represented as integer list, i.e. {1,1}.
    *The path is stored in the route variable as a ArrayList of integer list.
    *When multiple movements are available for a coordinate, closest height to goal is choosen
    * and the rest is stored in optionaldir variable. numofstep variable store the number of step 
    * taken from start. The method can trace back to alterative movements from a coordinate in route
    * using the number stored in numofstep variable when the route reach a dead end.
    */
    public void findpath(){
        route = new ArrayList<int[]>();
        optionaldir = new ArrayList<ArrayList<int[]>>();
        int[] start = {startx,starty};  //Start coordinate
        int[] end = {endx,endy};        //Goal coordinate
        route.add(start);
        int[] currentcoord = route.get(route.size()-1);
        numofstep = 0;
        //Loop through nextpath method until goal is reached.
        while (true){
            //nextpath method return the chosen coordinate to advance.
            currentcoord = nextpath(currentcoord);
            //If goal is reached, a route to goal is found. Break out of loop.
            if (currentcoord[0] == endx && currentcoord[1] == endy){
                break;
            }
        }        
    }
    
    /*
    *Nextpath methods find all coordinate (movements) available for the given coordinate (int[] location).
    *One valid coordinate will be chosen and stored in route ArrayList and all other valid coordinates
    * are stored in optionaldir ArrayList. 
    */
    private int[] nextpath(int[] currentstate){
        ArrayList<int[]> validcoords = new ArrayList<int[]>();  
        
        //Looping through the NESW direction coordinate in the class
        for (int i = 0; i < 4; i++){
            int [] movement = {direction[i][0], direction[i][1]};
            
            //Use of modulus to conditions for wrapping movements. 
            int coordx = ((((currentstate[0]+movement[0]) % width)+width)%width);
            int coordy = ((((currentstate[1]+movement[1]) % height)+height)%height);
            int [] newcoord = {coordx,coordy};
            
            //Check if coordinate does not contains a wall
            if (mymaze[newcoord[0]][newcoord[1]] != '#'){
                boolean findsame = false;
                
                //Check whether new coordinate has been tranversed.
                for (int j = 0; j <= numofstep; j++){
                    if (comparesame(route.get(j),newcoord)){
                        findsame = true;
                        break;
                    }
                }
                //If not tranversed, add new coordinate to validcoords
                if (!findsame){
                    validcoords.add(newcoord);
                }        
            }     
        }
        //validmoves methods return the chosen coordinate using the given and class data.
        return validmoves(validcoords);
    }
    
    /*
    * validmoves methods takes an ArrayList of availables coordinates (moves) from the current coordinate 
    *as seen from nextpath method and check if one can be chosen from the list. 
    * If so, one coordinate is chosen a removed from 'moves' ArrayList, add to the route ArrayList and add 
    *remaining coordinates to optionaldir ArrayList. 
    * If there is not available coordinates, this implies the current coordinate in route ArrayList reached 
    *a deadend, the method will backtrack to the previous coordinate and check alterative moves from 
    *optionaldir ArrayList using numofsteps variable as a reference. 
    * When it is not possible to backtrack, this implies the maze is unsolvable.
    */
    public int[] validmoves(ArrayList<int[]> moves){
        int[] coord = {-1,-1}; 
        
        //If there is no coordinate (moves)
        if (moves.isEmpty()){
            
            //Remove the last coordinate from route
            route.remove(route.size()-1);
            int currentlevel = numofstep;
            
            //Backtrack coordinates from route
            for (int i = currentlevel-1; i >= 0; i--){
                //If no alterative coordinate available from coordinate before
                if (optionaldir.get(i).isEmpty()){
                    if (i != 0){
                        //Remove the last coordinate from route
                        route.remove(route.size()-1);
                    }
                    //If we backtracked to start with no available coordinates
                    else{
                        //Output no solution and close program.
                        System.out.println("No solution");
                        System.exit(0);
                    }
                }
                //If there exists an alterate coordinate different from one in route
                else{
                    //Get the coordinate and add to route and update number of steps taken.
                    int[] alteratepath = optionaldir.get(i).get(0);
                    optionaldir.get(i).remove(alteratepath);
                    route.add(alteratepath);
                    numofstep = i+1;
                    coord = alteratepath;  
                    break;
                }
            }
        }
        //If there are availables coordinates
        else{
            //Sort and remove duplicate from ArrayList
            moves = sortandremovedup(moves); 
            
            coord = moves.get(0); 
            moves.remove(coord);
            route.add(coord);
            
            //If ArrayList size matches with number of step, we use .add to increase ArrayList.
            if (numofstep == optionaldir.size()){
                optionaldir.add(moves);
                numofstep += 1;
            }
            //If does not match, we use set to match number of step (index) to memory space in optionaldir
            else{
                optionaldir.set(numofstep,moves);
                numofstep += 1;
            }
        }
        return coord;
    }
    
    /*Method to add path from start to end in the maze.*/
    public void addpath(){
        int[] start = {startx,starty};
        int[] end = {endx,endy};
        for (int i = 0; i < route.size(); i++){
            int [] point = route.get(i);
            if (mymaze[point[0]][point[1]] == ' '){
                mymaze[point[0]][point[1]] = 'X';
            }
            
        }
    }
    
    /*Method to compare coordinate, return true if they are the same*/
    public boolean comparesame(int[] a, int[] b){
        if (a[0] == b[0] && a[1] == b[1]){
            return true;
        }
        else{
            return false;
        }
    }
    
    /*
    * This method takes an ArrayList of integers list, remove lists that are repeated,
    *and swap lists when second element (height) is larger than another.
    */
    public ArrayList<int[]> sortandremovedup(ArrayList<int[]> Points){
        for (int m = 0; m < Points.size(); m++){
            for(int n = m+1; n < Points.size(); n++){
                if (comparesame(Points.get(m),Points.get(n))){
                    Points.remove(n);
                }
                else if (Points.get(m)[1] < Points.get(n)[1]){
                    Collections.swap(Points,m,n);
                }
            }
        }
        return Points;
    }

    public static void main(String[] args) {
        MazeSolver Maze = new MazeSolver();   
        try{
            System.out.println("Please enter path to file");
            Scanner scanner = new Scanner(System.in);
            String filename = scanner.nextLine();
            Maze.readmazedata(filename);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not Found.");
            System.exit(0);
        }
        
        Maze.buildmymaze();
        Maze.findpath();
        Maze.addpath();
        Maze.printmymaze();
    }
    
}
