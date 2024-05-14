/**
 * This class stores the contents of a chunk in the board
 */

public class Chunk {
    public int col; //The column of the chunk
    public int row; //The row of the chunk
    public int level;//The height of the chunk
    public boolean isLake = false;
    public String label = "empty"; //Label of the chunk. Initially empty


    /**
     * Constructor
     * @param r row
     * @param c col
     * @param level level
     */
    public Chunk(int r,int c, int level){
        row = r;
        col = c;
        this.level = level;
    }


}
