import java.util.*;

public class Queens {
    private Coord [] InitialState;
    private Coord [] FixedRow;
    private Coord [] SortRow;
    private Coord [] Converted;
    private int N,soli;
    private int [] ValueXs;
    private ArrayList<int []> Allnodes = new ArrayList<int []>();
    private ArrayList<Coord []> queueR = new ArrayList<Coord []>();
    private ArrayList<Tree> path;
    private Tree [][] tree;
    private LinkedList<int []> queue = new LinkedList<int []>();
    private ArrayList<int []> SolutionXs = new ArrayList<int []>();
    private List<Integer> iteration = new ArrayList<>();
    
    public void RandBoard (int N){  //Generate a NxN chess board with random N queens placed
        InitialState = new Coord[N];    //Setting the size of the Coord array to N
        Random rand = new Random(); 
        this.N =N;                      //Store N within the class.
        for (int i = 0; i < N; i++){
            int x = rand.nextInt(N)+1;  //Get a random variable
            int y = rand.nextInt(N)+1;
            InitialState[i] = new Coord(x,y); //Add the two random variable as a new coordinate to the InitialState Coord array
        }   
        for (int j = 0; j < N; j++){    //The follow are to check whether there are repeats with the randomly generated coordinate
            for (int k = 1; k < N; k++){    //The first for loop hold the item to be check with other items in the array, the second for loop are to iterate through the array to check against the item hold in the first for loop, this way it will check compare every items in the array
            if ( k != j && InitialState[j].getX() == InitialState[k].getX() && 
                    InitialState[j].getY() == InitialState[k].getY()){  //If statement checking every whether the x,y integers (coordinates) within InitialState array are the same and making sure the loop is not checking the same item in the array
                int u = rand.nextInt(N)+1;  //If they are, replace one of the repeated one with new random integers
                int v = rand.nextInt(N)+1;
                InitialState[j].setX(u);
                InitialState[j].setY(v);
                k = 0;                      //Repeat the second for loop, to check whether the new values are not repeated in the array. 
            }
        }
        }
        sorty(InitialState);    //Sorting the InitialState (coordinates) into ascending order
        System.out.println("Initial State");    
        Display(InitialState);  //Display the items in the array into coordinate form i.e (x0,y0) (x1,y1) ...
        DisplayBoard(InitialState); //Display the items in the array on a chess board
        System.out.println();
    }
    
    public void FixRow(){   //This methods sort the queens from the InitialState into individual row.
        SortRow = new Coord[N];
        for (int p = 0; p < N; p++) { //Copy the InitialState Coordinates to SortRow array
            SortRow[p] = new Coord(InitialState[p].getX(),InitialState[p].getY());
        }
        for (int i = 0; i < N; i++){ //Similar to before the two for loop checks through the array, comparing two elements at a time
            for (int j = 0; j < N; j++){
                int a = SortRow[i].getY();  //Getting the y coordinate from the array
                int b = SortRow[j].getY(); 
                if (i == j){    //If statement here prevent comparing the same item from the array
                    continue;   //Continue the loop;
                }
                else if ( a == b ){ //If the y coordinates are the same, add 1 to the y coordinate
                     if ( a+1 > N){ //If adding 1 to the y coordinate exceed the N value (i.e out of the chess board), 
                         SortRow[i].setY(1);    //Set the first y coordinate to 1
                         j = -1;                //Reset the second for loop, checking whether the new value is not repeated again
                     }
                     else{
                         SortRow[i].setY(a+1);  //Add 1 to first y coordinate
                         j = -1;
                     }
                }                    
            }
        }
        FixedRow = new Coord[N];
        for (int q = 0; q < N; q++) {   //Copy SortRow array to FixedRow
            FixedRow[q] = new Coord(SortRow[q].getX(),SortRow[q].getY());
        }
        sorty(FixedRow);    //Sort the y coordinate of FixedRow array in ascending order 
    }
    
    public void Rowstates (){   //Getting the intermediate step from InitialState to where the queens are sorted into individual rows.
        int [] vec = new int[N];
        for (int v = 0; v < N; v++){ //Getting the differences between the y-coordinates of InitialState and SortedRow queens
            int y = InitialState[v].getY() - SortRow[v].getY();
            vec[v] = y;     //Storing the differences into the vector
        }
        Coord [] TsortR = new Coord[N];
        for (int q = 0; q < N; q++) {   //Copying the SortRow array to TsortR
            TsortR[q] = new Coord(SortRow[q].getX(),SortRow[q].getY());
        }
        for (int i = 0; i < N; i++){ //Looping through the element of vector
            if (vec[i] != 0){   //If the element of the vector is not zero, this means there is a difference between the y coordinate which mean a queen was moved from InitiaLState to SortRow
                int z = i;  //Getting the value of i (This is important as there might be changes to i later)
                int a = vec[z]; //Getting the z element of the vector
                int b = TsortR[z].getY();   //Getting the y coord of TsortR from position z
                int c = TsortR[z].getX();   //Getting the x coord of TsortR from position z
                for (int k = 0; k < N; k++){    //Second loop iterating 
                    if (i == k){    //Not comparing the same item in the TsortR array
                        continue;   //Continue the loop
                    }
                    else if ( a > 0){   //If the element of the vector is positive
                        for (int j = 1; j < a; j++){    //This loop increase the value of j until it reaches before a
                            if (b+j == TsortR[k].getY() &&  
                                                      c == TsortR[k].getX()){   //This if statement and combining the for loop before, checks if there exist a queen between the space from where the queen is (the one i am moving) to where that the queen is moving to
                                int temp = TsortR[k].getY();    //A temp variable storing the value of conflicted queen
                                TsortR[z].setY(temp);           //Swaping the y-coordinate between the two
                                TsortR[k].setY(b);
                                int t = InitialState[i].getY() - TsortR[z].getY();  //Getting the new differences between the swapped coordinate
                                vec[z] = t;                                         //Setting the new values to the respective position in the vector
                                int u = InitialState[k].getY() - TsortR[k].getY();
                                vec[k] = u;
                                if( k < i ){    //If the position of conflict is before the i
                                    i = k-1;    //Resetting the first for loop to position of k-1 (where the element was swapped) so we can check through the array again for repeating elements
                                }
                                else {
                                    i = i-1;    //Looping through the same position again 
                                }
                                k = -1;     //Resetting the second for loop
                                a = vec[z]; //Replacing the value of vector with the new one
                                b = TsortR[z].getY();   //Replacing the y-coordinate with the new one
                                break;  //break the current loop and finish finding a queen that move to the position
                            }
                        }
                    }
                    else{   //if( a < 0 )
                        for (int m = -1; m > a; m--){ //This means that the queen has to move downward, so it is checking whether there is a queen between the space from and to where that the queen is moving to by decreasing m until it is before a
                            if (b+m == TsortR[k].getY() &&  
                                                      c == TsortR[k].getX()){   //The following are the same as before, only with the decreasing for loop 
                                int temp = TsortR[k].getY();
                                TsortR[z].setY(temp);
                                TsortR[k].setY(b);
                                int t = InitialState[i].getY() - TsortR[z].getY();
                                vec[z] = t;
                                int u = InitialState[k].getY() - TsortR[k].getY();
                                vec[k] = u;
                                if( k < i ){    
                                    i = k-1;    
                                }
                                else {
                                    i = i-1;     
                                }
                                k = -1;
                                a = vec[z];
                                b = TsortR[z].getY();
                                break;
                            }
                        }
                    }
                }
                TsortR[z].setY(a+b);    //Add the value of the vector element to the z position on the array to y-coordinate
                Coord [] add = new Coord[N];
                for (int r = 0; r < N; r++) {   //Copy the TsortR array
                    add[r] = new Coord(TsortR[r].getX(),TsortR[r].getY());
                }
                queueR.add(add);    //Add the array (state of the board) to queueR so we can retrive them later
                vec[z] = 0;         //Setting the vector element to zero since we have added the state which show the move of it.
            }
        }
    }

//Breadth First Search
/*This search works as follows, first taking all the x-coordinate from the FixedRow array where 
*we previously sorted the queen into indivdual row on the board (i.e, all the x-coord of the queens). 
*Add the x-coordinate (state) into the queue, then using a while loop
take the first item of the queue out of the queue and move the queen horizonally by 1 on the first row checking 
whether the position is in conflict with the row(s) above. Only add to the queue, if the queen are not in conflict
start checking the next row when all the state generated by the first row is checked
Continue to check until the last row where the states that are not in conflict would be the solutions.
The solution would be add to the SolutionXs Array. The states that are popped out of the queue are stored within the Allnodes array.
While adding states to the queue, an indicator added to a integer array where it links the state to its parent node*/
    public void BFS(){ 
        ValueXs = new int[N];   
        for (int p = 0; p < N; p++){    //Getting the x-coordinates of the from the FixedRow
            ValueXs[p] = FixedRow[p].getX();
        }
        queue.add(ValueXs); //Add the state to the queue
        
        while (!queue.isEmpty()){
            int queuesize = queue.size();
            int posit = 0;      //level of the nodes
            int nodenum = 0;
            iteration.add(-1);  //The iteration array store the connection between the state and the state before, the initial state doesn't connect to anything so it is labelled as -1
            for (int i = 0; i < queuesize; i++){
                int [] temp = new int[N];
                temp = queue.poll();    //Take out the first item from the queue
                Allnodes.add(temp);     //Take the item to Allnodes array where it stores all the state outputted from the queue
                int [] copy = Arrays.copyOf(temp, N);   
                for (int k = 0; k < N; k++){    //Checking all horizonal moves in the row
                    int [] tobeadded = Arrays.copyOf(copy, N);
                    int valueposit = copy[posit];   //Getting the x coordinate of the array from the posit integer position
                    if ( conflict(copy,posit) ){    //Check if moving a queen along the row has any conflict with the other queen by the posit number. (Note: it doens't check for the first row) See conflict methods
                        if(valueposit % N == 0){    //If the x-coordinate of the queen is on the edge of the N board
                            copy[posit] = 1;        //Move the x-coordinate of the queen to 1
                        }
                        else{
                            copy[posit] = valueposit+1; //Move the x-coordinate of the queen by 1
                        }
                    }
                    else if( posit == N-1 ){    //When we reach the last row of the board
                        SolutionXs.add(copy);   //If there is no conflict in the last row, this means there aren't any conflict in the board, add the solution state to the array
                        iteration.add(nodenum); //Add the link from the current state to it parent state
                        k = N;                  //Setting the k = N will continue through this loop and ends it.
                    }
                    else{    
                        queue.add(tobeadded);   //If the state is not in conflict, add the state to the queue
                        iteration.add(nodenum); //Add the link from the current state to it parent state
                        if(valueposit % N == 0){     
                            copy[posit] = 1;    
                        }
                        else{
                            copy[posit] = valueposit+1;
                        }
                    }
                }
                if( i == queuesize-1 ){ //When we have check all the state for the first row
                    queuesize = 0;  
                    queuesize = queue.size();   //Set first for loop limit to the current queue.size()
                    if (queuesize == 0){    //When queue is empty, break out of the loop
                        break;
                    }
                    i = -1; //Reset i integer;
                    posit = posit+1;    
                }
                nodenum = nodenum + 1; //Add 1 to the connection to parent node
                }
            for (int j = 0; j < SolutionXs.size(); j++){
                Allnodes.add(SolutionXs.get(j));    //Add the solutions to the array
            }
        }
    }
    
    public void searchoptimal(){ //Finds the optimal solutions by tracing from the solution states to the FixedRow state
        int posit = -1;
        int nextind = -1;
        path = new ArrayList<Tree>();
        tree = new Tree[SolutionXs.size()][] ; //This is an Tree array within a Tree array, setting the outer array to number of solutions it has 
     
        for (int i = 0; i < SolutionXs.size(); i++){    //
            for (int j = Allnodes.size()-1; j >= 0; j--){   //Searching from the end of the array to the start
                if (Allnodes.get(j) == SolutionXs.get(i)){  //If solution is found in Allnodes array
                    posit = j;                              //Get the position in the Allnodes
                    Tree a = new Tree();
                    a.setTree(SolutionXs.get(i),j);         //Put the solution state and its parent node into a tree Object
                    path.add(a);                            //Add the object to path arraylist
                    nextind = iteration.get(j);             //Get the next parent node from the iteration array based on the position of the Allnodes array
                    while (nextind != -1){
                        Tree b = new Tree();                
                        b.setTree(Allnodes.get(nextind),nextind);   //Put the state and its parent node into a tree Object
                        path.add(b);                                //Add the object to path arraylist
                        nextind = iteration.get(nextind);           //Get the next parent node from the iteration array
                    }
                    for (int l = 0; l < path.size()-1; l++){        //This loop delete all the identical state in the path
                        Tree c = new Tree();
                        int [] d = new int [N];
                        int [] e = new int [N];
                        d = path.get(l).getsol();                  
                        e = path.get(l+1).getsol(); //Get next state in the path 
                        if (c.Samesol(d,e)){        //If the two states are the same, 
                            path.remove(l);         //Remove the repeated state
                            l = l - 1;
                        }
                    }       
                    int pathsize = path.size(); //Get path.size
                    tree[i] = new Tree[pathsize];   //Set the size of inner array to path size
                    for (int k = 0; k < pathsize; k++){ 
                        tree[i][k] = path.get(pathsize-1-k);   //Add all state in path to tree.
                    }
                    path.clear();    //Clear all the items in the path
                    break;
                }   
            }
        }
        
        soli = 0;
        int m = 0;
        int n = 1;
        while(n < tree.length){     //Searching for the optimal solution, by searching for the shortest path to the solution. This will get the position of optimal path and solution in the arrays.
            if(tree[m].length < tree[n].length){    
                soli = m;
                n++;
            }
            else if(tree[m].length == tree[n].length){
                soli = m;
                n++;
            }
            else{
               soli = n;
               m = n; 
               n = m+1;
            }
        }
    }
    
    private Coord [] converttoxy(int [] a){ //This method convert the an array of integers containing x-coordinate to a cartesian coordinate system
        Converted = new Coord[a.length];
        for (int i = 0; i < a.length; i++){
            int x = a[i];
            Converted[i] = new Coord(x,i+1);
        }
        return Converted;
    }

    public void printSolution(){    //This method prints out the optimal solution on the to screen
        System.out.println("Solution");
        Coord [] a = new Coord [N];
        a = converttoxy(SolutionXs.get(soli));
        Display(a);
        DisplayBoard(a);
        System.out.println();
    }
  
    public void printTrace(){   //This method prints out the trace from the initialstate to solution
        System.out.println("Trace");
        System.out.println("Initial State");
        DisplayBoard(InitialState); //Display
        System.out.println();
        int a = 0;
        
        for (int s = queueR.size()-2; s >= 0; s--){ //Getting the state from initialstate to where the queens are in indivdual row
            DisplayBoard(queueR.get(s));    //Display the chess board
            System.out.println();
        }
        
        boolean same = false;           //Indicator for the check below
        for (int q = 0; q < N; q++) {   //Check if initialstate and the state where the queens are in indivdual row are the same
            if (InitialState[q].getX() != SortRow[q].getX() 
                     || InitialState[q].getY() != SortRow[q].getY()){
                same = false;
                q = N;
            }
            else{
                same = true;
            } 
        }
        if (same){  //If the state are the same, skip the first element in the loop below since it will be the same state.
            a = 1;
        } 

        for (int o = a; o < tree[soli].length; o++){ //From tree array, get the solution path and displaying all the state from that path
            Coord [] b = new Coord [N];
            b = converttoxy(tree[soli][o].getsol());
            if ( b != FixedRow){
                Display(b);
                DisplayBoard(b);
                System.out.println();
            }
        }
    }

    private void sorty(Coord [] R){     //Sort the coordinates in th coordinate array into ascending order based on y value.
        for (int i = 0; i < R.length-1; i++){
            for (int j = 0; j < R.length-1; j++){
                if(R[j].getY() > R[j+1].getY()){
                    Coord temp = R[j];
                    R[j] = R[j+1];
                    R[j+1]=temp;
                }
            }   
        }
    }
    
    private boolean conflict(int [] Array, int h){  //This method check whether the queens are in conflict with other queens from the row above
        if  (h == 0){       //h integer is an indicater to determine how many rows above to check for conflict. If h equals to zero, there is no row to check above.
            return false;
        }
        else{
            int v = Array[h];
            for (int i = 0; i < h; i++){
                int u = Array[i];
                int a = v-u;    //the difference between the x-coordinates
                int b = h-i;    //the difference between the y-coordinates
                if ( u == v || a == b || -a == b ){ //check that the queens are not in the same column, diagonally positive or diagonally negative each other
                    return true;
                }
            }
            return false;
        }
    }
       
    private void Display(Coord [] c){ //Display the Coord in cartesian coordinate system (i.e (x0,y0) (x1,y1) ...)
        for (int i = 0; i < c.length; i++){
            int a = c[i].getX();
            int b = c[i].getY();
            System.out.printf("%s%d%s%d%s", "(",a,",",b,") ");
        }
        System.out.println();
    }
    
    private void DisplayBoard(Coord [] queens){ //Displaying the queens on a chessboard
        int noempty = 0;
        for (int i = 1; i < N+1; i++){  //Iterate through the y axis
            for (int j = 1; j < N+1; j++){ //Iterate through the x axis
                for (int k = 0; k < N; k++){ //Iterate through coordinates
                    if (queens[k].getY() == i && queens[k].getX() == j){    //If there exist a queen for within these coordinate
                        System.out.print("[Q]");    //Print a queen
                        noempty = 1;
                        break;
                    }    
                }
                if (noempty == 1){ //If printed a queen, don't print a blank space
                    noempty = 0; 
                }
                else{
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }     
    }
    
}