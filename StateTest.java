import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void getColumn() {
        State state = new State(999999999999999999L);
        assertEquals(0b111111111, state.getColumn(7));
        assertEquals(0b111111111, state.getColumn(6));
    }
    @Test
    void getColumnFirstTest() {
        // State here is 111011101 111010111 111011101 111010111 111011101 100010111
        State state = new State(0b111011101111010111111011101111010111111011101100010111L);
        assertEquals(0b100010111, state.getColumn(7));
        assertEquals(0b111011101, state.getColumn(6));
        assertEquals(0b111010111, state.getColumn(5));
        assertEquals(0b111011101, state.getColumn(4));
        assertEquals(0b111010111, state.getColumn(3));
        assertEquals(0b111011101, state.getColumn(2));
        assertEquals(0, state.getColumn(1));
    }

    @Test
    void getColumnLength() {
        State state = new State(999999999999999999L);
        assertEquals(7, state.getColumnLength(7));
        assertEquals(7, state.getColumnLength(6));
    }
    @Test
    void getColumnLengthFirstTest() {
        // State here is 101011101 011010111 001011101 111010111 110011101 100010111
        State state = new State(0b101011101011010111001011101111010111110011101100010111L);
        assertEquals(4, state.getColumnLength(7));
        assertEquals(6, state.getColumnLength(6));
        assertEquals(7, state.getColumnLength(5));
        assertEquals(1, state.getColumnLength(4));
        assertEquals(3, state.getColumnLength(3));
        assertEquals(5, state.getColumnLength(2));
        assertEquals(0, state.getColumnLength(1));
    }
    @Test
    void isColumnFull() {
        State state = new State(999999999999999999L);
        assertTrue(state.isColumnFull(7));
        assertTrue(state.isColumnFull(6));
    }

    @Test
    void isColumnFullFirstTest() {
        // State here is 101011101 011010111 001011101 111010111 110011101 100010111
        State state = new State(0b101011101011010111001011101111010111110011101100010111L);
        assertFalse(state.isColumnFull(7));
        assertTrue(state.isColumnFull(6));
        assertTrue(state.isColumnFull(5));
        assertFalse(state.isColumnFull(4));
        assertFalse(state.isColumnFull(3));
        assertFalse(state.isColumnFull(2));
        assertFalse(state.isColumnFull(1));
    }

    @Test
    void isFull() {
        State state = new State(999999999999999999L);
        assertFalse(state.isFull());
    }
    @Test
    void isFullTure() {
        State state = new State(0b1111111111111111111111111111111111111111111111111111111111111111L);
        assertTrue(state.isFull());
    }
}