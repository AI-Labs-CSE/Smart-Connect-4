public class GameEngine {
    public static void main(String[] args) {
        Minimax minimax = new Minimax(2);
        State initialState = new State(0L);
        long[] info = minimax.getInfo(initialState);
        System.out.println("time taken: " + info[0]);
        System.out.println("nodes expanded: " + info[1]);
        System.out.println("next play: " + minimax.getNextState(initialState).toString());
        minimax.printGameTree(initialState);
    }
}
