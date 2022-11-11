public interface SearchAlgorithm {
    State getNextState(State initialState);
    long[] getInfo(State initialState);
}
