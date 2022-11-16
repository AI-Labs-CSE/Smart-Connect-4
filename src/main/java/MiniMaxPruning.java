import java.util.HashMap;

public class MiniMaxPruning implements SearchAlgorithm {
	private final int maxDepth;
    private int expandedNodes;
    private final int MAXHEURISTIC = 630;
    HashMap<State, State[]> actionMap;
    HashMap<State, State> optimalMap;
    
    public MiniMaxPruning (int maxDepth) {
    	this.maxDepth = maxDepth;
    	expandedNodes = 0;
        actionMap = new HashMap<>();
        optimalMap = new HashMap<>();
    }

    /**
     * @param initialState: The current game state to start with
     * @return long[] that contains the taken time to define the next state and its value
     * using minimax with alpha beta pruning, and the number of expanded nodes
     * */
    public long[] getInfo(State initialState) {
        expandedNodes = 0;
        long start = System.currentTimeMillis();
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        value(initialState, NextAgent.MAX, 0, alpha, beta);
        long end = System.currentTimeMillis();
        return new long[]{end - start, expandedNodes};
    }

    /**
     * @param initialState: The current game state to start with
     * @return the next optimal state starting with given initialState
     * */
    public State getNextState(State initialState) {
        return optimalMap.get(initialState);
    }

    /**
     * @param state: The current game state
     * @param nextAgent: specifies MIN or MAX agent turn
     * @param currDepth: represents the current game depth
     * @param alpha the max value for the MAX agent can reach. set initially to -infinity
     * @param beta the min value for the MIN agent can reach. set initially to infinity
     * @return the expected game value starting with given state
     * */
    private int value(State state, NextAgent nextAgent, int currDepth, int alpha, int beta) {
        expandedNodes++;
        if(currDepth >= maxDepth) return state.heuristic() + state.getScoreDiff() * MAXHEURISTIC;
        if(state.isTerminal()) return state.getScoreDiff();
        if(nextAgent == NextAgent.MAX) return maxValue(state, currDepth, alpha, beta);
        else return minValue(state, currDepth, alpha, beta);
    }

    /**
     * @return tha max value for tha MAX agent can get using alpha beta pruning
     * by selecting the max value from current state successors
     * */
    private int maxValue(State state, int currDepth, int alpha, int beta) {
    	int val = Integer.MIN_VALUE;
    	State[] succ = state.getSuccessors(1);
        actionMap.put(state, succ);
        int optimalStateIdx = 0;
    	for (int i=0; i<succ.length; i++) {
    		int currentVal = value(succ[i], NextAgent.MIN, currDepth+1, alpha, beta);
    		if (val < currentVal) {
    			val = currentVal;
    			optimalStateIdx = i;
    		}
    		alpha = Math.max(alpha, val);
            // detects that pruning should happen
    		if (val >= beta) {
    			break;
    		}
    	}
    	optimalMap.put(state, succ[optimalStateIdx]);
        state.setValue(val);
    	return val;
    }

    /**
     * @return tha min value for the MIN agent can get using alpha beta pruning
     * by selecting the min value from current state successors
     * */
    private int minValue(State state, int currDepth, int alpha, int beta) {
    	int val = Integer.MAX_VALUE;
    	State[] succ = state.getSuccessors(0);
    	actionMap.put(state, succ);
        int optimalStateIdx = 0;
        for (int i=0; i<succ.length; i++) {
    		int currentVal = value(succ[i], NextAgent.MAX, currDepth+1, alpha, beta);
    		if (val > currentVal) {
    			val = currentVal;
    			optimalStateIdx = i;
    		}
    		beta = Math.min(beta, val);
            // detects that pruning should happen
    		if (val <= alpha) {
    			break;
    		}
    	}
    	optimalMap.put(state, succ[optimalStateIdx]);
        state.setValue(val);
    	return val;
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
