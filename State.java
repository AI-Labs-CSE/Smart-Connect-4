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
        return Long.toBinaryString(state);
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
     *
     * @param column - the column to get
     * @return the value of the column between 0 and 511 (inclusive) that represents the state of the column
     ***/
    public long getColumn(int column){
        column = 7 - column; //flip the column to get the correct column from the left 0 , 1 , 2 , 3 , 4 , 5 , 6 , 7
        long columnPosition = 511L << (column * 9); // 511 = represents 111 111 111
        return (state & columnPosition ) >> (column * 9);
    }

    /***
     * @param column - the column to get the number of pieces in
     * @return the number of pieces in the column
     ***/
    public long getColumnLength(int column){
        long columnValue = getColumn(column);
        int lengthBits = 7 << 6; // that value represents 111 000 000
        long length = columnValue & lengthBits;
        return length >> 6;
    }

    /***
     * @param column - the column to check
     * @return true if the column is full and false if it is not
     ***/
    public boolean isColumnFull(int column){
        return getColumnLength(column) >= 6;
    }

    /***
     * @return true if the state is a game over state board is full
     ***/
    public boolean isFull(){
        for(int i = 0; i < 7; i++){
            if(!isColumnFull(i)){
                return false;
            }
        }
        return true;
    }

    public String columnsToString(){

        StringBuilder result = new StringBuilder();
        for(int i = 1; i <= 7; i++){
            String column = Long.toBinaryString(getColumn(i));
            for(int j= 0; j < 9-column.toString().length(); j++){
                result.append("0");
            }
            result.append(column);
            result.append("\n");
        }


        return result.toString();
    }


    public static void main(String[] args) {
        State state = new State(999999999999999999L);
        System.out.println(state.toString());
        System.out.println(state.columnsToString());

        System.out.println(state.getColumn(7));
        System.out.println(state.getColumn(6));
        System.out.println(state.getColumn(5));
        System.out.println(state.getColumnLength(7));
        System.out.println(state.isColumnFull(7));
        System.out.println(state.getColumnLength(6));
        System.out.println(state.getColumnLength(5));





    }

}
