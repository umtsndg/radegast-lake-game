import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


/**
 * The class that the world's values and methods are stored
 */
public class World {
    public int numRows; //Total row in the board
    public int numCols; //Total column in the board
    public Chunk[][] board; //2D array list to store the board
    public int highestLevel = 0; //Highest level in the board
    public static int labelNumber = 0; //The label's "index" which will increase after each assignment
    public static ArrayList<ArrayList<Chunk>> lakeList = new ArrayList<>(); //A 2D array list to store lakes
    public static final int[][] neighList = {{0,-1},{-1,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1}}; //A chunk's neighbours

    /**
     * Constructor
     * @param numCols Total column number
     * @param numRows Total row number
     */
    public World(int numCols, int numRows){
        this.numRows = numRows;
        this.numCols = numCols;
        board = new Chunk[numRows][numCols];
    }

    /**
     * This functions prints the board without the lake labels
     */
    public void printBoard(){
        StringBuilder out = new StringBuilder();

        //Creating all the rows except the last.
        for (int r = 0; r < numRows ; r++){
            if(r<10) {
                out.append("  ").append(r);
            }
            else{
                out.append(" ").append(r);
            }
            for (int c =0 ; c < numCols ; c++){
                if (board[r][c].level < 10) {
                    out.append("  ").append(board[r][c].level);
                }
                else{
                    out.append(" ").append(board[r][c].level);
                }
            }
            out.append(" \n");
        }
        //Creating the last row
        out.append("   ");
        for (int c = 0 ; c < numCols; c++){
            if (c <=25) {
                out.append("  ").append(Radegast.alphabet[c]);
            }
            else{
                out.append(" ").append(Radegast.alphabet[c / 26 - 1]).append(Radegast.alphabet[c % 26]);
            }
        }
        out.append(" ");
        System.out.println(out);
    }

    /**
     * Takes 10 input and does modifications if the inputs are appropriate, else gives an error and asks again
     */
    public void doModifications(){
        for (int i = 1; i <= 10 ; i++){
            while (true){
                System.out.println("Add stone " + i + "/ 10 to coordinate:");
                Scanner inp = new Scanner(System.in);
                String input = inp.nextLine();

                //Changes the string input (ex: a0,aa1,aa11) into row and col
                //If the code fails in this section it means faulty input is given
                try {
                    String r0 = String.valueOf(input.charAt(0));
                    String r;
                    int c;
                    try{
                        String r1 = String.valueOf(input.charAt(1));
                        try{
                            r = r0;
                        }
                        catch (Exception e){
                            r = r0 + r1;
                        }
                        try{
                            c = Integer.parseInt(input.substring(1));
                        }
                        catch(Exception e){
                            c = Integer.parseInt(input.substring(2));
                        }
                    }
                    catch (Exception e){
                        c = Integer.parseInt(input.substring(1));
                        r = r0;
                    }
                    board[c][Radegast.toIndex(r)].level ++;
                    if (board[c][Radegast.toIndex(r)].level > highestLevel){
                        highestLevel = board[c][Radegast.toIndex(r)].level;
                    }
                    printBoard();
                    break;
                }


                catch(Exception e){
                    System.out.println("Not a valid step!");
                }
            }
        }
    }

    /**
     * Finds the lakes by traversing the board. Checks all 8 neighbours of a chunk. If each neighbour is either higher leveled , is a lake or checked before
     * Increases tmp parameter. If the tmp reaches 8 turns the chunk into a lake. However, might fail when before chunks block all the way. This situation is
     * handled in the next function
     * @param chunk the chunk which will be checked
     * @param before the chunks that are checked before
     */
    public void findLakes(Chunk chunk, ArrayList<Chunk> before){
        int tmp = 0;
        if (chunk.col > 0 & chunk.row > 0 & chunk.col < numCols-1 & chunk.row < numRows-1 & board[chunk.row][chunk.col].level < highestLevel) {
            for (int[] n : neighList) {
                if (board[chunk.row + n[0]][chunk.col + n[1]].level > board[chunk.row][chunk.col].level ||  before.contains(board[chunk.row + n[0]][chunk.col + n[1]]) || board[chunk.row + n[0]][chunk.col + n[1]].isLake) {
                    tmp++;
                } else if (board[chunk.row + n[0]][chunk.col + n[1]].level <= board[chunk.row][chunk.col].level & !board[chunk.row + n[0]][chunk.col + n[1]].isLake & board[chunk.row + n[0]][chunk.col + n[1]].level < highestLevel) {
                    before.add(board[chunk.row][chunk.col]);
                    findLakes(board[chunk.row + n[0]][chunk.col + n[1]], before);
                    before.remove(before.size()-1);
                }
            }
            if (tmp == 8) {
                board[chunk.row][chunk.col].isLake = true;

            }
        }
    }

    /**
     * Checks the specific case which is described above.
     * @param chunk the chunk which is analyzed
     */
    public void checkLakes(Chunk chunk){
        for (int[] n: neighList){
            if (board[chunk.row + n[0]][chunk.col + n[1]].level == board[chunk.row][chunk.col].level & !board[chunk.row + n[0]][chunk.col + n[1]].isLake) {
                board[chunk.row][chunk.col].isLake = false;
                break;
            }
        }
    }

    /**
     * Labels the lakes by using labelNumber. If finds a lake in the board labels this lake and the others that are connected.
     * Works similarly as findLakes
     * @param chunk chunk the chunk which is analyzed
     * @param before the chunks that are analyzed before
     */
    public void labelLakes(Chunk chunk, ArrayList<Chunk> before){
        int tmp = 0;
        if (chunk.row > 0 & chunk.col > 0 & chunk.row < numRows-1 & chunk.col < numCols-1 & Objects.equals(chunk.label, "empty")){
            for (int[] n : neighList){
                if(!board[chunk.row + n[0]][chunk.col + n[1]].isLake || before.contains(board[chunk.row + n[0]][chunk.col + n[1]])){
                    tmp++;
                }
                else{
                    if (World.labelNumber <= 25){
                        chunk.label = Radegast.upperAlphabet[World.labelNumber];
                    }
                    else{
                        chunk.label = Radegast.upperAlphabet[World.labelNumber/26-1]+ Radegast.upperAlphabet[World.labelNumber%26];
                    }
                    lakeList.get(labelNumber).add(chunk);
                    before.add(board[chunk.row][chunk.col]);
                    labelLakes(board[chunk.row + n[0]][chunk.col + n[1]],before);
                    before.remove(before.size()-1);
                }
            }
            if (tmp == 8){
                if (World.labelNumber <= 25){
                    chunk.label = Radegast.upperAlphabet[World.labelNumber];
                }
                else{
                    chunk.label = Radegast.upperAlphabet[World.labelNumber/26-1]+ Radegast.upperAlphabet[World.labelNumber%26];
                }
                lakeList.get(labelNumber).add(chunk);
                lakeList.add(new ArrayList<>());
                World.labelNumber++;
            }
        }
    }

    /**
     * Calculates the score by first finding the lake limit and doing the calculations
     * @return the score
     */
    public double calculateScore(){
        lakeList.remove(lakeList.size()-1); // Removes the last element because it is null. This element is added so that the former function is implemented easier

        double out = 0;
        int tmp;
        int lakeLevel;

        //Finding the lake border
        for (ArrayList<Chunk> chunks : lakeList) { //Checking all lakes
            lakeLevel = Integer.MAX_VALUE;
            tmp = 0;
            for (Chunk chunk : chunks) {
                for (int[] n : neighList) {
                    if (board[chunk.row + n[0]][chunk.col + n[1]].level < lakeLevel & !chunks.contains(board[chunk.row + n[0]][chunk.col + n[1]])) {
                        lakeLevel = board[chunk.row + n[0]][chunk.col + n[1]].level;
                    }
                }
            }
            //Calculating the score
            for (Chunk chunk : chunks) {
                tmp += lakeLevel - board[chunk.row][chunk.col].level;
            }
            out += Math.pow(tmp, 0.5);
        }
        return out;
    }

    /**
     * Prints the board with lake labels.
     */
    public void printLast(){
        StringBuilder out = new StringBuilder();
        for (int r = 0; r < numRows ; r++){
            if(r<10) {
                out.append("  ").append(r);
            }
            else{
                out.append(" ").append(r);
            }
            for (int c =0 ; c < numCols ; c++){
                if(!board[r][c].isLake) {
                    if (board[r][c].level < 10) {
                        out.append("  ").append(board[r][c].level);
                    } else {
                        out.append(" ").append(board[r][c].level);
                    }
                }

                //If a lake is printed, prints its label
                else{
                    if (board[r][c].label.length() ==2){
                        out.append(" ").append(board[r][c].label);
                    }
                    else if (board[r][c].label.length()==1){
                        out.append("  ").append(board[r][c].label);

                    }
                }
            }
            out.append(" \n");
        }
        out.append("   ");
        for (int c = 0 ; c < numCols; c++){
            if (c <=25) {
                out.append("  ").append(Radegast.alphabet[c]);
            }
            else{
                out.append(" ").append(Radegast.alphabet[c / 26 - 1]).append(Radegast.alphabet[c % 26]);
            }
        }
        out.append(" ");
        System.out.println(out);
    }
    }

