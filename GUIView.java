import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

public class GUIView {

    private final String maxDepthDefaultText = "Enter Max Search Depth and Press Start";
    private final String columnDefaultText = "Enter Column Number and Press Enter";
    Border border = BorderFactory.createLineBorder(Color.BLACK);

    GUIController guiController = new GUIController();
    Utilities utilities = new Utilities();

    JFrame frame = new JFrame();

    JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 250, 5));
    JLabel playerScoreLabel = new JLabel("Player Score: ", JLabel.CENTER);
    JLabel computerScoreLabel = new JLabel("Computer Score: ", JLabel.CENTER);

    JPanel boardPanel = new JPanel(new GridLayout(6, 7));

    JPanel southPanel = new JPanel();
    JPanel columnsNumPanel = new JPanel(new GridLayout(1, 7));
    JPanel optionsPanel = new JPanel();

    ButtonGroup pruningRadioGroup = new ButtonGroup();
    JRadioButton withoutPruningRadio = new JRadioButton("Without Pruning", true);
    JRadioButton withPruningRadio = new JRadioButton("With Alpha Beta Pruning", false);

    JTextField maxDepthInput = new JTextField(maxDepthDefaultText);

    JTextField columnInput = new JTextField(columnDefaultText);

    JButton startButton = new JButton("Start");

    boolean withPruning = false;
    String maxSearchDepth = "";

    void prepareGUI(){
        scorePanel.add(playerScoreLabel);
        scorePanel.add(computerScoreLabel);

        buildBoard();

        for (int i = 0; i < 7; i++)
            columnsNumPanel.add(new JLabel(String.valueOf(i), JLabel.CENTER));

        pruningRadioGroup.add(withPruningRadio);
        pruningRadioGroup.add(withoutPruningRadio);

        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.add(withoutPruningRadio);
        optionsPanel.add(withPruningRadio);
        optionsPanel.add(maxDepthInput);
        optionsPanel.add(startButton);

        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(columnsNumPanel);
        southPanel.add(optionsPanel);

        frame.setTitle("Minimax Connect Four");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scorePanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        attachListeners();
    }

    void buildBoard(){
        try {
            ImageIcon whiteCircleImg = new ImageIcon(ImageIO.read(new File(".\\WhiteCircleImg.png")));
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    JLabel tempLabel = new JLabel(whiteCircleImg);
                    tempLabel.setBorder(border);
                    boardPanel.add(tempLabel);
                }
            }
        }catch (Exception ignored){}
    }

    void attachListeners(){
        applyDefaultTextListener(maxDepthInput, maxDepthDefaultText);
        applyDefaultTextListener(columnInput, columnDefaultText);

        columnInput.addActionListener(e -> {
            if (utilities.isPositiveInt(e.getActionCommand())){
                guiController.applyMove(e.getActionCommand());
            }
            columnInput.setText(columnDefaultText);
            columnInput.setFocusable(false);
            columnInput.setFocusable(true);
        });

        startButton.addActionListener(e -> {
            maxSearchDepth = maxDepthInput.getText();
            if (!utilities.isPositiveInt(maxSearchDepth)){
                maxDepthInput.setText(maxDepthDefaultText);
                return;
            }
            withPruning = withPruningRadio.isSelected();
            southPanel.remove(optionsPanel);
            southPanel.add(columnInput);
            frame.setVisible(true);
        });
    }

    void applyDefaultTextListener(JTextField jTextField, String defaultTest){
        jTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (jTextField.getText().equals(defaultTest)) {
                    jTextField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (jTextField.getText().isEmpty()) {
                    jTextField.setText(defaultTest);
                }
            }
        });
    }

    public static void main(String[] args){
        GUIView guiView = new GUIView();
        guiView.prepareGUI();
    }

}
