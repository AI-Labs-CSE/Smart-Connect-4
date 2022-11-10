import java.util.ArrayList;
import java.util.List;

public class State {

    long state;

    public State(long state) {
        this.state = state;
    }

    public String toString() {
        return Long.toBinaryString(state);
    }

    /***
     * @return 2D array of characters that represent the state of the board
     ***/
    public char[][] stateToMatrix(){
        char[][] matrix = new char[7][];
        for (int i = 0; i < 7; i++)
            matrix[i] = getColumn(i);
        return matrix;
    }

    /***
     * @param column - the column to get
     * @return 2d array with the start and end index on the column bits
     ***/
    public char[] getColumn(int column){
        int length = getColumnLength(column);
        long thisColumn = (state >> (column * 9L + 3)); // the plus 3 to remove the size bits
        char[] res = {'0', '0', '0', '0', '0', '0'};
        int i = 0;
        while (length>0){
            res[i] = (thisColumn & 0b1) == 0 ? 'y' : 'r';
            thisColumn = thisColumn >> 1;
            length--;
            i++;
        }
        return res;
    }

    /***
     * @param column - the column to get the number of pieces in
     * @return the number of pieces in the column
     ***/
    public int getColumnLength(int column){
        /* the first 3 bits in every column represents the last place that a disk was put in
           ex: 010011101 here we have 5 disks the first one is red second is red third is yellow fourth is yellow fifth is red
           the last bit is zero, but it's an empty place because we knew from the first 3 bits that our size is 5
           to get the size we just need to extract the first 3 bits of the column
           to get a specific column we need to right shift by 9 * the column number we want
         */
        return (int)(state >> (column * 9)) & 0b111;
    }

    public State[] getSuccessors(int turn) {
        List<State> successors = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            State temp = new State(this.state);
            if (!temp.isColumnFull(i)) {
                temp.addToColumn(i, turn);
                successors.add(temp);
            }
        }
        return successors.toArray(new State[0]);
    }

    /***
     * @param column - the column to check
     * @return true if the column has value 6 in the first 3 bits and false otherwise
     ***/
    public boolean isColumnFull(int column){
        // since our board is 6*7 the max number of pieces to play is 6
        return getColumnLength(column) >= 0b110;
    }

    /***
     * Adds a new piece to column
     * @param column - column index (0 indexed)
     * @param value - 0 for yellow and 1 for red
     ***/
    public boolean addToColumn(int column, int value){
        if (isColumnFull(column))
            return false;
        int length = getColumnLength(column);
        long thisColumn = (state >> (column * 9 + 3));
        long newColumn = ((((long) value << length) | thisColumn) << 3) | (length + 1);

        state = state & ~(0b111111111L << (column*9));
        state = state | newColumn << (column * 9);
        return true;
    }

    /***
     * @return true if the board is full and false otherwise
     ***/
    public boolean isTerminal(){
        for (int i = 0; i < 7; i++){
            if (!isColumnFull(i))
                return false;
        }
        return true;
    }

    public int heuristic() {
        return 0;
    }

    public int getScore() {
        return 0;
    }

}
