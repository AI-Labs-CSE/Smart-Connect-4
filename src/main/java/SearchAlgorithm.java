import java.util.HashMap;

public interface SearchAlgorithm {
    State getNextState(State initialState);
    long[] getInfo(State initialState);
    HashMap<State, State[]> getGameTree();
}
