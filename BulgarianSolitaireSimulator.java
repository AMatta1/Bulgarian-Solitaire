/**
   class BulgarianSolitaireSimulator
   This class models the game Bulgarian Solitaire. It drives the game and modifies its
   appearance, based on the mode specified by user from command line. Also lets user input  
   initial board configuration depending on mode. Depends on class SolitaireBoard and run
   through console window using java BulgarianSolitaireSimulator <mode> command.
   
   Modes used :
   1) -s (Single Step) : Output of each round displayed after pressing return, takes random initial configuration
   2) -u (User Input)  : User provides initial configuration, no single step - entire output displayed in one go
   3) -u -s / -s -u (User Input and Single Step) : User enters input and single step 
   4) <none> : Random initial configuration, no single step
   
   Demonstrates method decompostion as part of procedural design
*/

import java.util.Scanner;
import java.util.ArrayList;

public class BulgarianSolitaireSimulator {
   
   public static void main(String[] args) {                        // Total number of lines in method : 29
     
      boolean singleStep = false;
      boolean userConfig = false;
      int j = 1;                                                   // Variable used while printing configuration on console 
      
                                 
      Scanner read = new Scanner(System.in);                       
      
      for (int i = 0; i < args.length; i++) {                      // Reading the command-line arguments passed by user while running the program
         
         if (args[i].equals("-u")) {
            userConfig = true;
         }
        
         else if (args[i].equals("-s")) {
            singleStep = true;
         }
         
      }
            
      if (userConfig && !singleStep) {                              // Checks if -u (User Input) mode
            
         ArrayList<Integer> userInputList = userInput(read);        // Holds user entered configuration returned via userInput() method
         
         
         // Call to parameterized constructor of class SolitaireBoard 
         // userInputList passed as parameter. Prints initial configuration
         SolitaireBoard board = new SolitaireBoard(userInputList);    
         
         currentConfigTillDone(j, board, read);                     // Calls method to print configurations in one go till end of game
         
      }
            
      else if (!userConfig  && singleStep) {                        // Checks if -s (Single Step) mode
            
         SolitaireBoard board = new SolitaireBoard();               // Prints initial random configuration
           
         board.playRound();                                        
         System.out.println("[" + j + "] Current configuration: " + board.configString());
         j ++ ;
      
         currentConfigSingleStep(j, board, read);                   // Print one configuration at a time. Asks to type return at every step
         
      }
            
      else if (userConfig && singleStep) {                          // Checks if -s -u / -u -s (User Input and Single Step) mode
            
         ArrayList<Integer> userInputList = userInput(read);
         SolitaireBoard board = new SolitaireBoard(userInputList);
            
         board.playRound();                                        
         System.out.println("[" + j + "] Current configuration: " + board.configString());
         j ++ ;
     
         currentConfigSingleStep(j, board, read);
         
      }
            
      else {                                                       // Checks left out cases like no arguments passed (No single step/user input)
         
         SolitaireBoard board = new SolitaireBoard();
         currentConfigTillDone(j, board, read);
         
      }
      
   }
      
   
   
         
         
   /**
      Prompts user to input initial configuration in -u and -u -s/-s -u modes
      Performs checks for non - negative, non-zero integers that sum to CARD_TOTAL - in subsequent steps
      Displays error message if fails at any check. Continues till correct list entered
      
      @Param : Scanner in - Scanner object to read from System.in
       Returns arrayList containing user input list to main
       
       Total no. of lines : 30
   */   
   private static ArrayList<Integer> userInput( Scanner in) {
      
      boolean isValid = false;
      int sum = 0;
      int currentSize = 0;
      
      ArrayList<Integer> inputArrList = new ArrayList<Integer>();               // Lists created to read user input and return to main
      ArrayList<Integer> finalArrList = new ArrayList<Integer>();
     
      Scanner lineScanner = userPrompt(in);                                     // Prompts for user input and holds returned Scanner object
      
      while (!isValid) {                                                        // Error checking starts for each number one-by-one             
       
         while (lineScanner.hasNext()) {                                         
        
            if (lineScanner.hasNextInt()) {                                     // Checks if input has an integer
               int val = lineScanner.nextInt();
                     
               if (val > 0) {                                                   // Checks if an integer is positive. 
                  inputArrList.add(val);                                        // If so, appends to arraylist, adds to sum and increments list size
                  sum += inputArrList.get(currentSize) ;                  
                  currentSize ++;                                                               
               }
                  
               
               // If integer is negative or 0, calls IncorrectInput() method for error prompt and re-entering input   
               // Sum and arraylist size made 0 to start fresh checks on new input
               else {                                                           
                   lineScanner = IncorrectInput(in,inputArrList);
                   sum = 0;
                   currentSize = 0;
               }
            }
                 
            else {                                                              // If input is non-integer(e.g, alphabet, symbol)calls error prompt
               lineScanner = IncorrectInput(in,inputArrList);
               sum = 0;
               currentSize = 0;
            }
           
         }
           
         if (sum != SolitaireBoard.CARD_TOTAL) {                                // If above checks passed, sees if all integers add to CARD_TOTAL  
            lineScanner = IncorrectInput(in,inputArrList);
            sum = 0;
            currentSize = 0;
         }
                     
         else {                                                                 // Passed all checks!
            isValid = true; 
            finalArrList = inputArrList;                                        // Arraylist made to enable returning input to main
         }
         
      }
      
      return finalArrList;                                                  
      
   }
     
   
   
   
   
   /**
      Prompts the user to enter the initial configuration for the first time after running
      the program. Total number of lines : 6
   */ 
   private static Scanner userPrompt(Scanner prompt) {
      
      System.out.println ("Number of total cards is " + SolitaireBoard.CARD_TOTAL);
      System.out.println ("You will be entering the initial configuration of the cards (i.e., how many in each pile).");
      System.out.println ("Please enter a space-separated list of positive integers followed by newline:");
      String input = prompt.nextLine();
      
      Scanner lineScanner=new Scanner(input);                                   // Object created to read input string and break it up for checks
      
      return lineScanner;
   }
        
   
   
   
   
   /**
      Displays error prompt in various scenarios like encountering 0, non-integers, negative
      integers or when sum is not equal to CARD_TOTAL. Asks for user input again.
      Also removes any existing elements in arraylist before encountering an erroneous input 
      value. Eg. if input is 45 0 , removes valid entry 45 that was appended before 0 was checked
      Returns a scanner object to userInput() having new input
      
      Total number of lines : 8
   */                          
   private  static Scanner IncorrectInput(Scanner scan, ArrayList<Integer> inputList) {
           
      int i = 0;
      
      System.out.println ("ERROR: Each pile must have at least one card and the total number of cards must be " + SolitaireBoard.CARD_TOTAL);
      System.out.println ("Please enter a space-separated list of positive integers followed by newline:");
      String input1= scan.nextLine();
      Scanner lineScanner1 = new Scanner(input1);
      
      
      while (i<inputList.size()) {                                                             // Removes any existing elements in arraylist 
      
         inputList.remove(i);
         
      }
             
      return lineScanner1;
      
   }
           
   
   
        
           
   /**
      Checks if end of game reached after every round of play by calling isDone(). If not, prompts user
      to type return to play next round and view its configuration. Called in Single Step mode.
      
      @ Param: j - counter carried over from main for display purpose
               board - Object of class SolitaireBoard to call its functions
               input - Scanner object to read from System.in
               
      Total number of lines : 6
   */         
   private static void currentConfigSingleStep (int j, SolitaireBoard board, Scanner input) {
          
      while (!board.isDone()) {                                                            
        
         System.out.print("<Type return to continue>");                                        
         input.nextLine();
        
         board.playRound();                                                                    // Plays next round
          
         System.out.println("[" + j + "] Current configuration: " + board.configString());     // Prints current configuration
         j ++ ; 
         
      }
      
   }
            
   
   
            
            
   /**
      Checks if end of game reached after every round of play by calling isDone(). If not, plays
      successive rounds till game terminates. Called in non- Single Step mode and displays all
      configurations in one go.
      
      @ Param: j - counter carried over from main for display purpose
               board - Object of class SolitaireBoard to call its functions
               input - Scanner object to read from System.in
               
      Total number of lines : 4
   */      
   private static void currentConfigTillDone(int j, SolitaireBoard board, Scanner input) {
             
      while (!board.isDone()) {
              
         board.playRound();                                                                    // Plays next round
        
         System.out.println("[" + j + "] Current configuration: " + board.configString());     // Prints current configuration
         j ++ ;
         
      }
      
   }
   
}                                                                                              
            
            
             
             
          
          
          
          
       
         
                                                                  

  