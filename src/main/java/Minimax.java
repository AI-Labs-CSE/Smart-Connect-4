import java.util.HashMap;

public class Minimax implements SearchAlgorithm {

    private final int maxDepth;
    private int expandedNodes;
    private final int MAXHEURISTIC = 630;
    HashMap<State, State[]> actionMap; // parent -> list of children
    HashMap<State, State> optimalMap; // parent -> child

    public Minimax(int maxDepth) {
        this.maxDepth = maxDepth;
        expandedNodes = 0;
        actionMap = new HashMap<>();
        optimalMap = new HashMap<>();
    }

    /**
     * @param initialState: The current game state to start with
     * @return long[] that contains the taken time to define the next state and its value
     * using minimax without alpha beta pruning, and the number of expanded nodes
     * */
    public long[] getInfo(State initialState) {
        expandedNodes = 0;
        long start = System.currentTimeMillis();
        value(initialState, NextAgent.MAX, 0);
        long end = System.currentTimeMillis();
        return new long[]{end - start, expandedNodes};
    }

    /**
     * @Param initialState: The current game state to start with
     * @return the next optimal state starting with given initialState
     * */
    public State getNextState(State initialState) {
        return optimalMap.get(initialState);
    }

    /**
     * @param state: The current game state
     * @param nextAgent: specifies MIN or MAX agent turn
     * @param currDepth: represents the current game depth
     * @return the expected game value starting with given state
     * */
    private int value(State state, NextAgent nextAgent, int currDepth) {
        expandedNodes++;
        if(currDepth >= maxDepth) return state.heuristic() + state.getScoreDiff() * MAXHEURISTIC;
        if(state.isTerminal()) return state.getScoreDiff();
        if(nextAgent == NextAgent.MAX) return maxValue(state, currDepth);
        else return minValue(state, currDepth);
    }

    /**
     * @return tha max value for the MAX agent
     * by selecting the max value from current state successors
     * */
    private int maxValue(State state, int currDepth) {// odd depth with max agent
        int score = Integer.MIN_VALUE;
        State[] successors = state.getSuccessors(1);
        actionMap.put(state, successors);
        int optimalStateIdx = 0;
        for(int i = 0; i < successors.length; i++) {
            int utility = value(successors[i], NextAgent.MIN, currDepth + 1);
            if(score < utility) {
                score = utility;
                optimalStateIdx = i;
            }
        }
        // set the next optimal state from the current state
        optimalMap.put(state, successors[optimalStateIdx]);
        // set the state value to be used with tree visualization
        state.setValue(score);
        return score;
    }

    /**
     * @return tha min value for the MIN agent
     * by selecting the min value from current state successors
     * */
    private int minValue(State state, int currDepth) {// even depth with MIN agent
        int score = Integer.MAX_VALUE;
        State[] successors = state.getSuccessors(0);
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

    /**
     * @return the optimal path taken to reach the optimal next state
     * */
    public HashMap<State, State> getOptimalPath() {
        return optimalMap;
    }

    /**
     * @return the graph tree constructed during searching for the optimal state
     * */
    public HashMap<State, State[]> getGameTree() {
        return actionMap;
    }

}
