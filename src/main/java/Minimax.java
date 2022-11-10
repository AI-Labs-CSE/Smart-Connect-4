import java.util.HashMap;

public class Minimax {
    private final int maxDepth;
    private int expandedNodes;
    private final int maxHeuristic = 630;
    HashMap<State, State[]> actionMap; // parent -> child
    HashMap<State, State> optimalMap; // parent -> child

    public Minimax(int maxDepth) {
        this.maxDepth = maxDepth;
        expandedNodes = 0;
        actionMap = new HashMap<>();
        optimalMap = new HashMap<>();
    }

    public long[] getInfo(State initialState) {
        long start = System.currentTimeMillis();
        value(initialState, NextAgent.MAX, 0);
        long end = System.currentTimeMillis();
        return new long[]{end - start, expandedNodes};
    }

    private int value(State state, NextAgent nextAgent, int currDepth) {
        expandedNodes++;
        if(currDepth >= maxDepth) return state.heuristic() + state.getScore();
        if(state.isTerminal()) return state.getScore() * maxHeuristic;
        if(nextAgent == NextAgent.MAX) return maxValue(state, currDepth);
        else return minValue(state, currDepth);
    }

    private int maxValue(State state, int currDepth) {
        int score = Integer.MIN_VALUE;
        State[] successors = state.getSuccessors(0);
        actionMap.put(state, successors);
        int optimalStateIdx = 0;
        for(int i = 0; i < successors.length; i++) {
            int utility = value(successors[i], NextAgent.MIN, currDepth + 1);
            if(score < utility) {
                score = utility;
                optimalStateIdx = i;
            }
        }
        optimalMap.put(state, successors[optimalStateIdx]);
        state.setValue(score);
        return score;
    }

    private int minValue(State state, int currDepth) {
        int score = Integer.MAX_VALUE;
        State[] successors = state.getSuccessors(1);
        actionMap.put(state, successors);
        int optimalStateIdx = 0;
        for(int i = 0; i < successors.length; i++) {
            int utility = value(successors[i], NextAgent.MAX, currDepth + 1);
            if(score > utility) {
                score = utility;
                optimalStateIdx = i;
            }
        }
        optimalMap.put(state, successors[optimalStateIdx]);
        state.setValue(score);
        return score;
    }

    public HashMap<State, State> getOptimalPath() {
        return optimalMap;
    }

    public HashMap<State, State[]> getGameTree() {
        return actionMap;
    }

    public void printGameTree(State initialState) {
        System.out.println(initialState.toString());
        printSuccessors(initialState);
    }

    private void printSuccessors(State initialState) {
        State[] successors = actionMap.get(initialState);
        if(successors == null)
            return;
        for (State s: successors)
            System.out.print(s.toString() + "\t");
        for (State s: successors)
            printSuccessors(s);
    }

    public State getNextState(State initialState) {
        return optimalMap.get(initialState);
    }

}
