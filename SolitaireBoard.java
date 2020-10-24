import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;

/*
   class SolitaireBoard
   The board for Bulgarian Solitaire.  You can change what the total 
   number of cards is for the game by changing NUM_FINAL_PILES, below.  
   Don't change CARD_TOTAL directly, because there are only some values
   for CARD_TOTAL that result in a game that terminates.  (See comments 
   below next to named constant declarations for more details on this.)
*/


public class SolitaireBoard {
   
   public static final int NUM_FINAL_PILES = 9;
   // number of piles in a final configuration
   // (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)
   
   public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
   // bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
   // see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
   // the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES

   // Note to students: you may not use an ArrayList -- see assgt 
   // description for details.
   
  
   
   /**
      Uses a partially-filled array representation.

      REPRESENTATION INVARIANT:
      
      * currentArrSize is the number of piles (where each pile contains a particular number of cards)
      * 0 <= currentArrSize <= cardsArray.length
      * If currentArrSize > 0, the number of cards are in cardsArray[0] through cardsArray[currentArrSize-1]
      * Number of cards are positive integers, hence, non-integers (characters,alphabets etc.), negative integers and 0 are not allowed
      * Number of cards in any pile must be between 1 and CARD_TOTAL (both inclusive)
      * Sum of cardsArray[0] through cardsArray[currentArrSize-1] must be CARD_TOTAL for a given NUM_FINAL_PILES. Hence, there are always 
        CARD_TOTAL cards on the board.
   */
   
   
   
   private int [] cardsArray = new int [CARD_TOTAL];
   // Partially filled array to hold card configuration. Value at an index represents number of cards in that pile
   // Array capacity is CARD_TOTAL to cater to the worst case scenario where a user may enter 1 card for each pile
   
   private int currentArrSize;
   // Indicates number of piles or elements in partially filled array
   
   
 
   /**
      Creates a solitaire board with the configuration specified in piles.
      piles has the number of cards in the first pile, then the number of 
      cards in the second pile, etc.
      PRE: piles contains a sequence of positive numbers that sum to 
      SolitaireBoard.CARD_TOTAL
   */
   public SolitaireBoard(ArrayList<Integer> piles) {                     // Total no. of lines : 8
      
      currentArrSize = 0;
      
      // Storing initial card configuration input by user via the received Arraylist into an array
      for (int i = 0; i < piles.size(); i++) {                           
         
         cardsArray[i] = piles.get(i);
        
      }
      
      for (int i = 0; i < cardsArray.length; i++) {                      // Calculating current size / number of elements in partially filled array
         
         if (cardsArray[i] > 0 ) {
            currentArrSize ++;
         }
         
      }
     
      printInitialConfig (currentArrSize, cardsArray);                   // Calling method to print initial configuration
      
      assert isValidSolitaireBoard();                                    
      
   }

        
   
   
   
   /**
      Creates a solitaire board with a random initial configuration.
   */
   public SolitaireBoard() {                                             // Total no. of lines : 18
      
      int value;
      currentArrSize = 0;
      int i = 0;
      int sumOfCards = 0;                                                // Sum of cards in randomly generated configuration initialized to 0
      Random generator = new Random();                                     
      
      
      /** Generating the first random pile using Random class object with range 0(inclusive) and CARD_TOTAL(exclusive)
          However, adding 1 explicitly ensures that 0 can't be generated and value = CARD_TOTAL can be generated. */
      
      value = generator.nextInt (CARD_TOTAL) + 1;  
      
      cardsArray[i]=value;
      i++;
      currentArrSize ++;
      sumOfCards += value;
     
      
      /** Subsequent random piles are generated till sum of cards becomes equal to CARD_TOTAL 
          Upper bound for random numbers generated decreases by a factor of sumOfCards for every successive iteration.*/
      
      while (sumOfCards != CARD_TOTAL) {
     
         value= generator.nextInt (CARD_TOTAL - sumOfCards) + 1;
         
         cardsArray[i] = value;
         i++;
         sumOfCards += value;
         currentArrSize ++; 
      
      }
    
      printInitialConfig(currentArrSize,cardsArray);                     
      
      assert isValidSolitaireBoard(); 
    
   }
      
   
   
   
   
   /**
      Plays one round of Bulgarian solitaire.  Updates the configuration 
      according to the rules of Bulgarian solitaire: Takes one card from each
      pile, and puts them all together in a new pile.
      The old piles that are left will be in the same relative order as before, 
      and the new pile will be at the end.
   */
   public void playRound() {                                                 // Total no. of lines : 24
      
      int cardsInLastPile = 0;                                               // Holds no. of cards in last pile for the round
      int zerosAfterRound = 0;                                               // Holds no. of zeros after decrementing each pile value by 1
      
      
      // Each element is decremented by 1, and no. of cards in last pile is incremented by 1
      // If value becomes 0 , no. of zeros is incremented by 1
      for (int x = 0; x < currentArrSize; x++) {                         
         
         cardsArray[x] -= 1;
         cardsInLastPile += 1;
         
         if (cardsArray[x] == 0) {
            zerosAfterRound ++;
         }
         
      }
      
      int interimArraySize = currentArrSize - zerosAfterRound;                   // Holds array size after removing elements that are 0
      
      
      /** We plan to obtain O(n) time complexity without allocating a new array by utilizing the extra amount of unused space present 
          towards the right of the partially filled  'cardsArray' itself. Hence, we define a variable 'back' to indicate the starting 
          index from the right side to hold the elements after a round of play. 
          The '- 1' in the formula is to make space for the last pile we will be adding that holds value 'cardsInLastPile' */
      
      int back = CARD_TOTAL - interimArraySize - 1;                              
      
      
      /** Iterating over the elements from left, we keep inserting them towards the right side - starting from index 'back' 
          We ensure 'back' doesn't exceed the length of array after incrementing to prevent 'ArrayIndexOutOfBoundsException' */
      
      for (int j = 0; j < currentArrSize; j++) {
            
         if(cardsArray[j] != 0 ) {
            cardsArray[back] = cardsArray[j];
               
               if (back < cardsArray.length - 1) {
                  back++; 
               }
            
         }
                  
      }
      
      back = CARD_TOTAL - interimArraySize - 1;
      cardsArray[cardsArray.length - 1] = cardsInLastPile;                   // Inserting cardsInLastPile at the extreme right index of array
      
      
      // Moving elements from right end to left of the same array so that the index is read from 0
      for (int k = 0; k < interimArraySize + 1; k++) { 
            
         cardsArray[k] = cardsArray[back];
         
         if (back <cardsArray.length - 1) {
            back++;
         }
         
      }
            
      currentArrSize = interimArraySize + 1;                                     // Updating currentArrSize as new pile has been added
      
      for (int i = currentArrSize; i < cardsArray.length; i++) {             // Making sure values at indices outside currentArrSize are not used
         
         cardsArray[i] = 0;
         
      }
      
      assert isValidSolitaireBoard(); 
      
   }
   
   
   
   
   
   /**
      Returns true iff the current board is at the end of the game.  That is, 
      there are NUM_FINAL_PILES piles that are of sizes 
      1, 2, 3, . . . , NUM_FINAL_PILES, 
      in any order.
   */
   public boolean isDone() {                                             // Total no. of lines : 17
      
      boolean isDone = false;
      
      // Holds number of duplicate elements (if any) in a configuration returned after every round of play
      int duplicates = 0;                                                     
      
      HashSet<Integer> isDuplicate = new HashSet<Integer>();             // HashSet created to obtain O(n) time complexity          
      
      if (currentArrSize != NUM_FINAL_PILES) {                           // Checks if number of piles = NUM_FINAL_PILES                 
         isDone = false;
      }
      
      else {
         
         /**
            For every element checks if value is between 1 and NUM_FINAL_PILES (both inclusive). If so values added to HashSet one by one.
            In case, an element is being added more than 1 time, it indicates duplicates and counter is incremented 
         */
         
         for (int i = 0; i < currentArrSize; i++) {                      
            
            if (cardsArray[i] >=1 && cardsArray[i] <= NUM_FINAL_PILES) {
         
               if (isDuplicate.contains( cardsArray[i] )) {
                  duplicates += 1;
               }
             
               else { 
                  isDuplicate.add( cardsArray[i] );
               }
            
            }
         } 
         
         
         // If duplicates don't exist, means all conditions checked and current board is at the end of the game. 
         // Variable set to 'true' , else variable value remains 'false'
         if (duplicates == 0) {
            isDone = true;
            System.out.println ("Done!");
         }
      }      
      
      assert isValidSolitaireBoard(); 
      
      return isDone;
      
   }
      
   
   
      
   
   /**
      Returns current board configuration as a string with the format of
      a space-separated list of numbers with no leading or trailing spaces.
      The numbers represent the number of cards in each non-empty pile.
   */
   public String configString() {                                                       // Total no. of lines : 7
       
      int [] removeZero = new int [currentArrSize];                       
      
      /**
         When an array is created it is initailized with all zeros. So all empty indices will show a zero.
         Hence, we are storing all non-zero elements in an array for display purpose on console only.
         replace() method is not useful as it replaces zeros in numbers too
      */
      
      for (int i = 0; i < cardsArray.length; i++ ) {
         
         if (cardsArray[i] != 0) {
            removeZero[i] = cardsArray[i];
         }
           
      } 
      
      
      // Replacing [ ] and , signs obtained after Arrays.toString(). Trims leading and trailing spaces too
      String boardConfig = Arrays.toString(removeZero).replace("[", "").replace("]", "").replace(",","").trim();
      
      assert isValidSolitaireBoard(); 

      return boardConfig;   
      
   }
   
   
   
   
   
   /**
      Returns true iff the solitaire board data is in a valid state
      (See representation invariant comment for more details.)
   */
   private boolean isValidSolitaireBoard() {                                // Total no. of lines : 11
      
      int sum =0 ;
      
      if (currentArrSize < 0 || currentArrSize > cardsArray.length) {       // Checks no. of elements is > 0 and less than capacity of array
         return false;
      }
   

      for (int i = 0; i < currentArrSize ; i++) {
      
         if (cardsArray[i] <= 0 || cardsArray[i] > CARD_TOTAL) {            // Checks if number is non-negative, non-zero and max val = CARD_TOTAL 
            return false;                                                   
         }
         
      }
   
      for (int i = 0; i < currentArrSize ; i++) {                           // Check sum of cards in all the piles  
         sum += cardsArray[i];
      }
      
      if (sum != CARD_TOTAL ) {                                             // If sum is not equal to CARD_TOTAL, test failed
         return false;   
      }
   
      return true;                                                          // Passed all the tests!
       
   }
   

   
   

   /**
      Prints the initial board configuration on the console after being called from constructors.
      @Param currentArrSize : No. of elemennts in partially filled array
              cardsArray    : The partially filled array being used 
    */
   private static void printInitialConfig (int currentArrSize, int[] cardsArray) {                  
      
      int [] removeZero = new int [currentArrSize];                        // Total no. of lines : 6
      
      for (int j = 0; j < cardsArray.length; j++ ) {                       // Does not consider trailing zeros generated during array initialization
         
         if (cardsArray[j] != 0) {
            removeZero[j] = cardsArray[j];
         }
        
      }
       
      String initial = Arrays.toString(removeZero).replace("[", "").replace("]", "").replace(",","").trim();
      
      System.out.println ("Initial configuration: " + initial);
      
   }
   
}
      
