import java.util.*;

public class Main {
   public static void main(String[] args)  throws InputMismatchException{
       
       Scanner scanner = new Scanner(System.in); 
       
       System.out.println("Welcome to Breadth First Search N-Queen Solver");
       System.out.println("                                        by Simon Yip");
       System.out.println("Please Enter the size of the chess board between 4 and 10");
       
       try{
        int N = scanner.nextInt();  //Scan the inputted integer values
        if ( N < 4 || N > 10){      //Only accept integer values between 4 and 10
           System.out.println("Did not input a number greater than 4.");
           System.out.println("Terminate program");
        }
        else{
           Queens States = new Queens();    //Create a new class object
           States.RandBoard(N);             //Generate a NxN chess board with random N queens placed
           States.FixRow();                 //Put each queen into individual row
           States.Rowstates();              //Store moves from InitialState to where queens are placed in individual row
           States.BFS();                    //Breadth First Search on where queens are placed in individual row
           States.searchoptimal();          //Connecting states from generated solutions to InitialState and find the most optimal one
           States.printSolution();          //Print the optimal solution
           States.printTrace();             //Print the trace from InitialState to the optimal solution
        }
       }
       catch (InputMismatchException e){    //Catch exception if inputted value is not an integer
           System.out.println("Did not input a number");
           System.out.println("Terminate program");
       }
    }
}

