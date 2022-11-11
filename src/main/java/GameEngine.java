public class GameEngine {
    public static void main(String[] args) {
        Minimax minimax = new Minimax(4);
        MiniMaxPruning minimaxPruning = new MiniMaxPruning(8);
        State initialState = new State(0L);
        long[] info = minimax.getInfo(initialState);
        long[] info1 = minimaxPruning.getInfo(initialState);
        System.out.println("time taken: " + info[0]);
        System.out.println("nodes expanded: " + info[1]);
        System.out.println("next play: " + minimax.getNextState(initialState).toString());
        System.out.println("time taken: " + info1[0]);
        System.out.println("nodes expanded: " + info1[1]);
        System.out.println("next play: " + minimaxPruning.getNextState(initialState).toString());
        System.out.println(minimax.getNextState(initialState).getValue());
        System.out.println(minimaxPruning.getNextState(initialState).getValue());
    }
}
