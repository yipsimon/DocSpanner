/*This class is created to store an integer array (containing the 
* x-coordinates of the queens of the current state) and the reference number
* which reference to the state it came from.
*/

public class Tree {
    private int [] solcoordx;
    private int ref;

    public void setTree(int [] solcoordx,int ref) { //Constructor for storing the integer array and it's reference number
        this.solcoordx = solcoordx;
        this.ref = ref;
    }
    
    public int [] getsol(){ //Getting the integer array from Tree object
        return solcoordx;
    }
    
    public int getref(){ //Getting the reference integer from Tree object
        return ref;
    }
       
    public boolean Samesol(int [] a,int [] b){  //Comparing two integer array, return true if they are the same
        for (int i = 0; i < a.length; i++){
            if (a[i] != b[i]){
                return false;
            }
        }
        return true;    
    }
    
}

