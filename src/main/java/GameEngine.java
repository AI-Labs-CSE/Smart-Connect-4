package src.main.java;

public class GameEngine {
    public static void main(String[] args) {
        Minimax minimax = new Minimax(4);
        State initialState = new State(0L);
        System.out.println(minimax.value(initialState, NextAgent.MAX, 0));
        System.out.println(minimax.getNextState(initialState).toString());
        minimax.printGameTree(initialState);
    }
}
