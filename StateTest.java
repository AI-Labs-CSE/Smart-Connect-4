import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void getColumn() {
        State state = new State(0b000000011111001110L);
        assertArrayEquals(new char[]{'y','y','y','0','0','0'}, state.getColumn(1));
        assertArrayEquals(new char[]{'r','y','y','r','r','r'}, state.getColumn(0));
    }
    @Test
    void getColumnFirstTest() {
        // State here is 111011101 111010111 111011101 111010111 111011101 100010111
        State state = new State(0);
        state.addToColumn(0, 1);
        state.addToColumn(0, 0);
        state.addToColumn(0, 1);
        state.addToColumn(1, 0);
        state.addToColumn(0, 1);
        state.addToColumn(0, 0);
        state.addToColumn(0, 1);
        assertArrayEquals(new char[]{'r','y','r','r','y','r'}, state.getColumn(0));
        assertArrayEquals(new char[]{'y','0','0','0','0','0'}, state.getColumn(1));
    }

    @Test
    void getColumnSecondTest() { // test triying to insert more than 6 pieces in column 3
        // State here is 111011101 111010111 111011101 111010111 111011101 100010111
        State state = new State(0);
        state.addToColumn(6, 1);
        state.addToColumn(6, 0);
        state.addToColumn(5, 1);
        state.addToColumn(5, 0);
        state.addToColumn(4, 1);
        state.addToColumn(4, 0);
        state.addToColumn(3, 1);
        state.addToColumn(3, 1);
        state.addToColumn(3, 1);
        state.addToColumn(3, 1);
        state.addToColumn(3, 1);
        state.addToColumn(3, 1);
        state.addToColumn(3, 1);
        state.addToColumn(3, 1);
        state.addToColumn(2, 1);

        assertArrayEquals(new char[]{'0','0','0','0','0','0'}, state.getColumn(0));
        assertArrayEquals(new char[]{'0','0','0','0','0','0'}, state.getColumn(1));
        assertArrayEquals(new char[]{'r','0','0','0','0','0'}, state.getColumn(2));
        assertArrayEquals(new char[]{'r','r','r','r','r','r'}, state.getColumn(3));
        assertArrayEquals(new char[]{'r','y','0','0','0','0'}, state.getColumn(4));
        assertArrayEquals(new char[]{'r','y','0','0','0','0'}, state.getColumn(5));
        assertArrayEquals(new char[]{'r','y','0','0','0','0'}, state.getColumn(6));


    }


        @Test
    void getColumnLength() {
        State state = new State(999999999999999999L);
        assertEquals(7, state.getColumnLength(0));
        assertEquals(7, state.getColumnLength(1));
    }
    @Test
    void getColumnLengthFirstTest() {
        // State here is 101011101 011010111 001011101 111010111 110011101 100010111
        State state = new State(0b101011101011010111001011101111010111110011101100010111L);
        assertEquals(7, state.getColumnLength(0));
        assertEquals(5, state.getColumnLength(1));
        assertEquals(7, state.getColumnLength(2));
        assertEquals(5, state.getColumnLength(3));
        assertEquals(7, state.getColumnLength(4));
        assertEquals(5, state.getColumnLength(5));
        assertEquals(0, state.getColumnLength(6));
    }
    @Test
    void isColumnFull() {
        State state = new State(999999999999999999L);
        assertTrue(state.isColumnFull(0));
        assertTrue(state.isColumnFull(1));
    }

    @Test
    void isColumnFullFirstTest() {
        // State here is 101011101 011010111 001011101 111010111 110011101 100010111
        State state = new State(0b101011101011010111001011101111010111110011101100010111L);
        assertTrue(state.isColumnFull(0));
        assertFalse(state.isColumnFull(1));
        assertTrue(state.isColumnFull(2));
        assertFalse(state.isColumnFull(3));
        assertTrue(state.isColumnFull(4));
        assertFalse(state.isColumnFull(5));
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