import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * Radegast class is the class that main operations are done.
 */
public class Radegast {
    public static String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static String[] upperAlphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    /**
     *
     * The main method that runs the game
     */
    public static void runRadegast() throws FileNotFoundException {
        World world = createStartingBoard(); //creates the block terrain
        world.printBoard(); //Printing the starting board
        world.doModifications(); //Modifying the board 10 times
        Chunk[][] copy; //Declaration of the board's copy which will be used to determine if all lakes are found

        //Finding the lakes in the board after the modifications
        while(true){
            copy = ChunkDeepCopy(world.board);
            for (int r = 1; r < world.numRows -1;r++){
                for (int c = 1; c < world.numCols-1; c++){
                    ArrayList<Chunk> before = new ArrayList<>();
                    if(!world.board[r][c].isLake) {
                        world.findLakes(world.board[r][c], before);
                    }
                }
            }
            if (Arrays.deepEquals(copy,world.board)){ //If the board didn't change, there are no more rivers to be found
                break;
            }
        }


        //Checks a specific error that might happen in the upper segment
        for (int r = 1; r < world.numRows -1; r++) {
            for (int c = 1; c < world.numCols - 1; c++) {
                world.checkLakes(world.board[r][c]);
            }
        }


        //Labeling the lakes and adding all lakes to an ArrayList
        World.lakeList.add(new ArrayList<>());
        for (int r = 1; r < world.numRows -1; r++){
            for(int c = 1; c < world.numCols-1; c++){
                if (world.board[r][c].isLake & Objects.equals(world.board[r][c].label, "empty")){
                    ArrayList<Chunk> before = new ArrayList<>();
                    world.labelLakes(world.board[r][c], before);
                }
            }
        }

        //Printing the ending screen and finding the score
        System.out.println("---------------");
        world.printLast();
        System.out.printf("Final score: " + "%.2f", world.calculateScore());


    }

    /**
     * Takes the file input and creates the starting board
     * @return the first world object which will be modified
     * @throws FileNotFoundException
     */
    public static World createStartingBoard() throws FileNotFoundException {
        File file = new File("input2.txt");
        Scanner boardFile = new Scanner(file);
        World world = new World(boardFile.nextInt(),boardFile.nextInt());
        for (int r = 0; r < world.numRows; r++){
            for (int c = 0; c < world.numCols; c++){
                world.board[r][c] = new Chunk(r,c,boardFile.nextInt());
                if (world.board[r][c].level > world.highestLevel){
                    world.highestLevel = world.board[r][c].level;
                }
            }
        }
        return world;
    }

    /**
     * Finds the index/es of a letter or two letter in the alphabet array
     * @param s the letter/s which will be found
     * @return the index of the letter/s in the alphabet array
     */
    public static int toIndex(String s){

        //If it is only one letter finds it in the alphabet
        if (s.length() == 1) {
            for (int i = 0; i < alphabet.length; i++) {
                if (alphabet[i].equals(s)) {
                    return i;
                }
            }
        }

        //If there are 2 letters divides them and finds them one by one with recursion
        else if (s.length() == 2){
            if(arrayContains(String.valueOf(s.charAt(0))) & arrayContains(String.valueOf(s.charAt(1)))){
                return (toIndex(String.valueOf(s.charAt(0))) + 1) * 26 + toIndex(String.valueOf(s.charAt(1)));
            }
        }
        return -1;
    }

    /**
     * Finds whether the alphabet array contains a string
     * @param s the string which will be checked
     * @return true if the string is in the alphabet array, false otherwise
     */
    public static boolean arrayContains(String s){
        for (String letter : Radegast.alphabet){
            if (letter .equals(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * Deep copies a two-dimensional array
     * @param original original array which will be copied
     * @return the copy
     */
    public static Chunk[][] ChunkDeepCopy(Chunk[][] original){
        Chunk[][] out = new Chunk[original.length][];
        for (int i = 0; i < original.length ; i++){
            out[i] = Arrays.copyOf(original[i],original[i].length);
        }
        return out;
    }
}
