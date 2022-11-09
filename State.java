public class State {

    long state;

    public State(long state) {
        this.state = state;
    }

    public String toString() {
        return Long.toString(state);
    }

    /***
     * @return 2 D array of the characters that represent the state of the board
     ***/
    public char[][] stateToMatrix(){
        char[][] matrix = new char[7][];
        for (int i = 0; i < 7; i++) {
            matrix[i] = getColumn(i);
        }
        return matrix;
    }

    /***
     * @param column - the column to add the piece to
     * @param value - the value to add 0 for yellow and 1 for red
     ***/
    public void addToColumn(int column, int value){
        if (isColumnFull(column))
            return;
        int length = getColumnLength(column);
        long thisColumn = (state >> (column * 9 + 3));
        long newColumn = ((((long) value << length) | thisColumn) << 3) | (length + 1);

        state = state & ~(0b111111111L << (column*9));
        state = state | newColumn << (column * 9);
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
        // the first 3 bits in every column represents the last place that a disk was put in
        // ex: 010011 101 here we have 5 disks the first one is red second is red third is yellow fourth is yellow firth is red
        // the last bit is zero but its an empty place because we knew from the first 3 bits that our size is 5
        // to get the size we just need to extract the first 3 bits of the column
        // to get a specific column we need to right shift by 9 * the column number we want
        return (int)(state >> (column * 9)) & 0b111;
    }

    /***
     * @param column - the column to check
     * @return true if the column is full and false if it is not
     ***/
    public boolean isColumnFull(int column){
        // since our board is 6*7 the max number of disk to be put is 6
        return ((state >> column * 9) & 0b111) >= 0b110;
    }

    /***
     * @return true if the state is a game over state board is full
     ***/
    public boolean isFull(){
        for (int i=0; i<7; i++){
            if (!isColumnFull(i)){
                return false;
            }
        }
        return true;
    }

}
