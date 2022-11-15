import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;

public class GUIController {

    private State currentState = new State(0L);
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

    public void applyMove(int columnNum){
        if (columnNum > 6)
            return;
        if (currentState.addToColumn(columnNum, 0)) {
            updateScore();
            updateBoard();
            guiView.frame.setVisible(true);
            long[] info = searchAlgorithm.getInfo(currentState);
            treeVisualizer.drawMinimaxTree(searchAlgorithm.getGameTree(), currentState);
            currentState = searchAlgorithm.getNextState(currentState);
            updateScore();
            updateBoard();
            guiView.timeTakenLabel.setText("Time Taken(ms): " + info[0]);
            guiView.nodesExpandedLabel.setText("Nodes Expanded: " + info[1]);
            guiView.frame.setVisible(true);
        }
    }

    void updateScore(){
        int[] score = currentState.getScore();
        guiView.computerScoreLabel.setText("Computer Score: " + score[0]);
        guiView.playerScoreLabel.setText("Player Score: " + score[1]);
    }

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

}
