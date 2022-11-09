public class State {

    long state;

    public State(long state) {
        this.state = state;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public String toString() {
        return Long.toString(state);
    }

    /***
     * @return 2 D array of the characters that represent the state
     ***/
    public char[][] stateToMatrix(){
        return null;
    }

    /***
     * @param column - the column to add the piece to
     * @param value - the value to add 0 for yellow and 1 for red
     ***/
    public void addToColumn(int column, int value){

    }

    /***
     * @param column - the column to get
     * @return 2d array with the start and end index on the column bits
     ***/
    public int[] getColumn(int column){
        return null;
    }

    /***
     * @param column - the column to get the number of pieces in
     * @return
     ***/
    public int getColumnLength(int column){
        return 0;
    }

    /***
     * @param column - the column to check
     * @return true if the column is full and false if it is not
     ***/
    public boolean isColumnFull(int column){
        return false;
    }

    /***
     * @return true if the state is a game over state board is full
     ***/
    public boolean isFull(){
        return false;
    }

}
