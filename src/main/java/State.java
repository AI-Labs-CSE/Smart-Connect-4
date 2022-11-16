import java.util.ArrayList;
import java.util.List;

public class State {

    public long state;
    private int value; // to be used while visualizing the tree

    public State(long state) {
        this.state = state;
    }

    public String toString() {
        return Long.toBinaryString(state);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
        long thisColumn = (state >> (column * 9L + 3)); // plus 3 to remove the size bits
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
           ex: 010011101 here we have 5 disks the first one is red second is red third is yellow
           fourth is yellow fifth is red the last bit is zero, but it's an empty place because
           we knew from the first 3 bits that our size is 5.
           to get the size we just need to extract the first 3 bits of the column
           to get a specific column we need to right shift by 9 * the column number we want
         */
        return (int)(state >> (column * 9)) & 0b111;
    }

    /**
     * @param turn specifies whether it's the MAX or MIN agent
     *             turn = 1 for MAX agent and 0 for MIN agent
     * @return successors states for the current state
     * */
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

    /**
     * @return the expected value of the current game state
     * */
    public int heuristic() {
        int score = 0;
        char[][] matrix = stateToMatrix();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                if (matrix [i][j] != '0'){
                    score += cellPower(i, j, matrix);
                }
            }
        }
        return score;
    }

    private int cellPower(int i, int j, char[][] matrix){
        int res = 0;
        int[][] possibleMoves = {{1,0}, {0,1}, {1,1}, {1,-1}};
        for (int[] move : possibleMoves){
            int k = i + move[0];
            int l = j + move[1];
            // add the power of the cell if there is possible index to avoid -1 index exception
            if (k >= 0 && k < 7 && l >= 0 && l < 6){
                res += cellPowerCalculation(i, j, k, l, matrix);
            }
        }
        return res;
    }

    private int cellPowerCalculation(int i, int j, int k, int l, char[][] matrix){
        int res = 0;
        if (matrix[i][j] == 'r' && matrix[k][l] == 'r'){
            res += 15;
        }
        else if (matrix[i][j] == 'y' && matrix[k][l] == 'y'){
            res -= 15;
        }
        else if (matrix[i][j] == 'r' && matrix[k][l] == '0'){
            if( isPossibleNextMove(k, l, matrix))
                res += 10;
            else
                res += 5;
        }
        else if (matrix[i][j] == 'y' && matrix[k][l] == '0'){
            if( isPossibleNextMove(k, l, matrix))
                res -= 10;
            else
                res -= 5;
        }

        return res;
    }

    private boolean isPossibleNextMove(int column, int row, char[][] matrix){
        if (row == 0)
            return true;
        return matrix[column][row - 1] != '0';
    }

    /**
     * @return the MAX agent score minus the player score which is the MIN agent
     * */
    public int getScoreDiff() {
        int[] score = getScore();
        return score[1] - score[0];
    }

    /**
     * @return 2d array of scores. MAX agent score is at index 0 and player score is at index 1
     * */
    public int[] getScore() {
        char[][] board = stateToMatrix();
        int[] horzScore = horizontalScore(board);
        int[] verticalScore = verticalScore(board);
        int[] positive = positiveDiagonalScore(board);
        int[] negative = negativeDiagonalScore(board);
        int yellow = horzScore[0] + verticalScore[0] + positive[0] + negative[0];
        int red = horzScore[1] + verticalScore[1] + positive[1] + negative[1];
        return new int[]{yellow, red};
    }

    private int[] negativeDiagonalScore(char[][] board){
        int yellowScore = 0;
        int redScore = 0;
        int redPieces = 0;
        int yellowPieces = 0;
        int colController = 3;
        boolean contCol = true;
        int contRow = 6;
        int i=6;
        int k=6;
        int j=2;
        int w=2;
        while (contCol || contRow >= 0) {
            yellowPieces = 0;
            redPieces = 0;
            while (i>=0 && k>=0 && j<60 && w<6) {
                if (board[i][j] == 'y') {
                    yellowPieces++;
                    if (yellowPieces == 4) {
                        yellowPieces = 3;
                        yellowScore++;
                    }
                } else {
                    yellowPieces = 0;
                }
                if (board[k][w] == 'r') {
                    redPieces++;
                    if (redPieces == 4) {
                        redPieces = 3;
                        redScore++;
                    }
                } else {
                    redPieces = 0;
                }
                i--;
                k--;
                j++;
                w++;
            }
            if (colController == 0){
                contCol = false;
                contRow--;
            }
            colController = colController > 0 ? colController - 1 : 0;
            i = contRow;
            k = contRow;
            j = colController;
            w = colController;
        }
        return new int[]{yellowScore, redScore};
    }

    private int[] positiveDiagonalScore(char[][] board){
        int yellowScore = 0;
        int redScore = 0;
        int redPieces = 0;
        int yellowPieces = 0;
        int rowController = 3;
        boolean contRow = true;
        int contCol = 0;
        int i=3;
        int k=3;
        int j=0;
        int w=0;
        while (contRow || contCol < 5) {
            yellowPieces = 0;
            redPieces = 0;
            while (i<7 && k<7 && j<6 && w<6) {
                if (board[i][j] == 'y') {
                    yellowPieces++;
                    if (yellowPieces == 4) {
                        yellowPieces = 3;
                        yellowScore++;
                    }
                } else {
                    yellowPieces = 0;
                }
                if (board[k][w] == 'r') {
                    redPieces++;
                    if (redPieces == 4) {
                        redPieces = 3;
                        redScore++;
                    }
                } else {
                    redPieces = 0;
                }
                i++;
                k++;
                j++;
                w++;
            }
            if (rowController == 0){
                contRow = false;
                contCol++;
            }
            rowController = rowController > 0 ? rowController - 1 : 0;
            i = rowController;
            k = i;
            j = contCol;
            w = contCol;
        }
        return new int[]{yellowScore, redScore};
    }

    private int[] verticalScore (char[][] board){
        int yellowScore = 0;
        int redScore = 0;
        int redPieces = 0;
        int yellowPieces = 0;
        for (int i=0, k=0; i<6 && k<6; i++, k++){
            yellowPieces = 0;
            redPieces = 0;
            for (int j=0, w=0; j<7 && w<7; j++, w++){
                if (board[j][i] == 'y'){
                    yellowPieces++;
                    if (yellowPieces == 4) {
                        yellowPieces = 3;
                        yellowScore++;
                    }
                }
                else {
                    yellowPieces = 0;
                }
                if (board[w][k] == 'r'){
                    redPieces++;
                    if (redPieces == 4){
                        redPieces = 3;
                        redScore++;
                    }
                }
                else{
                    redPieces = 0;
                }
            }
        }
        return new int[]{yellowScore, redScore};
    }

    private int[] horizontalScore (char[][] board) {
        int yellowScore = 0;
        int redScore = 0;
        int redPieces = 0;
        int yellowPieces = 0;
        for (int i=0, k=0; i<7 && k<7; i++, k++){
            yellowPieces = 0;
            redPieces = 0;
            for (int j=0, w=0; j<6 && w<6; j++, w++){
                if (board[i][j] == 'y'){
                    yellowPieces++;
                    if (yellowPieces == 4) {
                        yellowPieces = 3;
                        yellowScore++;
                    }
                }
                else {
                    yellowPieces = 0;
                }
                if (board[k][w] == 'r'){
                    redPieces++;
                    if (redPieces == 4){
                        redPieces = 3;
                        redScore++;
                    }
                }
                else{
                    redPieces = 0;
                }
            }
        }
        return new int[]{yellowScore, redScore};
    }

}
