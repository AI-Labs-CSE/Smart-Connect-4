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

    public long[] getInfo(State initialState) {
        expandedNodes = 0;
        long start = System.currentTimeMillis();
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        value(initialState, NextAgent.MAX, 0, alpha, beta);
        long end = System.currentTimeMillis();
        return new long[]{end - start, expandedNodes};
    }

    public State getNextState(State initialState) {
        return optimalMap.get(initialState);
    }

    private int value(State state, NextAgent nextAgent, int currDepth, int alpha, int beta) {
        expandedNodes++;
        if(currDepth >= maxDepth) return state.heuristic() + state.getScoreDiff() * MAXHEURISTIC;
        if(state.isTerminal()) return state.getScoreDiff();
        if(nextAgent == NextAgent.MAX) return maxValue(state, currDepth, alpha, beta);
        else return minValue(state, currDepth, alpha, beta);
    }
    
    private int maxValue(State state, int currDepth, int alpha, int beta) {
    	int val = Integer.MIN_VALUE;
    	State[] succ = state.getSuccessors(1);
    	int optimalStateIdx = 0;
    	actionMap.put(state, succ);
    	for (int i=0; i<succ.length; i++) {
    		int currentVal = value(succ[i], NextAgent.MIN, currDepth+1, alpha, beta);
    		if (val < currentVal) {
    			val = currentVal;
    			optimalStateIdx = i;
    		}
    		alpha = Math.max(alpha, val);
    		if (val >= beta) {
    			break;
    		}
    	}
    	optimalMap.put(state, succ[optimalStateIdx]);
        state.setValue(val);
    	return val;
    }
    
    private int minValue(State state, int currDepth, int alpha, int beta) {
    	int val = Integer.MAX_VALUE;
    	State[] succ = state.getSuccessors(0);
    	int optimalStateIdx = 0;
    	actionMap.put(state, succ);
    	for (int i=0; i<succ.length; i++) {
    		int currentVal = value(succ[i], NextAgent.MAX, currDepth+1, alpha, beta);
    		if (val > currentVal) {
    			val = currentVal;
    			optimalStateIdx = i;
    		}
    		beta = Math.min(beta, val);
    		if (val <= alpha) {
    			break;
    		}
    	}
    	optimalMap.put(state, succ[optimalStateIdx]);
        state.setValue(val);
    	return val;
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


}
