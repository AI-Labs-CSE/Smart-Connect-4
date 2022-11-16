import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;

public class GUIController {

    private State currentState = new State(0L);
    private State oldState = currentState;
    private final TreeVisualizer treeVisualizer = new TreeVisualizer();
    private final SearchAlgorithm searchAlgorithm;
    private final GUIView guiView;
    private ImageIcon redCircleImg;
    private ImageIcon yellowCircleImg;

    public GUIController(GUIView guiView, int maxDepth, boolean withPruning){
        try{
            this.redCircleImg = new ImageIcon(ImageIO.read(new File(".\\src\\main\\resources\\RedCircleImg.png")));
            this.yellowCircleImg = new ImageIcon(ImageIO.read(new File(".\\src\\main\\resources\\YellowCircleImg.png")));
        }
        catch (Exception ignored){}
        this.guiView = guiView;
        if(withPruning)
            searchAlgorithm = new MiniMaxPruning(maxDepth);
        else
            searchAlgorithm = new Minimax(maxDepth);
    }

    // apply the user move, if it was valid start the computer search algorithm, save the old state root, so we can visualise the tree
    // update the board and the score according to the new state after the computer turn
    public void applyMove(int columnNum){
        if (columnNum > 6)
            return;
        if (currentState.addToColumn(columnNum, 0)) {
            long[] info = searchAlgorithm.getInfo(currentState);
            oldState = currentState;
            currentState = searchAlgorithm.getNextState(currentState);
            updateScore();
            updateBoard();
            guiView.timeTakenLabel.setText("Time Taken(ms): " + info[0]);
            guiView.nodesExpandedLabel.setText("Nodes Expanded: " + info[1]);
            guiView.frame.setVisible(true);
        }
    }

    // get the score of the current state for both user and computer, update the score labels accordingly
    void updateScore(){
        int[] score = currentState.getScore();
        guiView.computerScoreLabel.setText("Computer Score: " + score[1]);
        guiView.playerScoreLabel.setText("Player Score: " + score[0]);
    }

    // iterate over the current state, updating the board colors accordingly
    void updateBoard(){
        char[][] newBoard = currentState.stateToMatrix();
        for (int column = 0; column < 7; column++)
        {
            for (int row = 0; row < 6; row++){
                if (newBoard[column][row] == 'r')
                    ((JLabel)guiView.boardPanel.getComponent((5 - row) * 7 + column)).setIcon(redCircleImg);
                else if (newBoard[column][row] == 'y')
                    ((JLabel)guiView.boardPanel.getComponent((5 - row) * 7 + column)).setIcon(yellowCircleImg);
            }
        }
    }

    void showLastGameTree(){
        treeVisualizer.drawMinimaxTree(searchAlgorithm.getGameTree(), oldState);
    }

}
