import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

public class GUIView {

    public final Border border = BorderFactory.createLineBorder(Color.BLACK);
    public final JFrame frame = new JFrame();
    public final JLabel playerScoreLabel = new JLabel("Player Score: 0", JLabel.CENTER);
    public final JLabel computerScoreLabel = new JLabel("Computer Score: 0", JLabel.CENTER);
    public final JPanel boardPanel = new JPanel(new GridLayout(6, 7));
    public final JLabel timeTakenLabel = new JLabel("Time Taken(ms): ");
    public final JLabel nodesExpandedLabel = new JLabel("Nodes Expanded: ");


    private GUIController guiController;
    private final String maxDepthDefaultText = "Enter Max Search Depth and Press Start";
    private final String columnDefaultText = "Enter Column Number and Press Enter";
    private final JPanel southPanel = new JPanel();
    private final JPanel optionsPanel = new JPanel();
    private final JRadioButton withPruningRadio = new JRadioButton("With Alpha Beta Pruning", false);
    private final JTextField maxDepthInput = new JTextField(maxDepthDefaultText);
    private final JTextField columnInput = new JTextField(columnDefaultText);
    private final JButton startButton = new JButton("Start");

    void prepareGUI(){
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 250, 5));
        JPanel columnsNumPanel = new JPanel(new GridLayout(1, 7));
        ButtonGroup pruningRadioGroup = new ButtonGroup();
        JRadioButton withoutPruningRadio = new JRadioButton("Without Pruning", true);

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
            ImageIcon whiteCircleImg = new ImageIcon(ImageIO.read(new File(".\\src\\main\\java\\WhiteCircleImg.png")));
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
            Utilities utilities = new Utilities();
            if (utilities.isPositiveInt(e.getActionCommand())){
                guiController.applyMove(Integer.parseInt(e.getActionCommand()));
            }
            columnInput.setText(columnDefaultText);
            columnInput.setFocusable(false);
            columnInput.setFocusable(true);
        });

        startButton.addActionListener(e -> {
            String maxSearchDepth = maxDepthInput.getText();
            Utilities utilities = new Utilities();
            if (!utilities.isPositiveInt(maxSearchDepth) || maxSearchDepth.charAt(0) == '0'){
                maxDepthInput.setText(maxDepthDefaultText);
                return;
            }
            boolean withPruning = withPruningRadio.isSelected();
            guiController = new GUIController(this, Integer.parseInt(maxSearchDepth), withPruning);
            southPanel.remove(optionsPanel);
            southPanel.add(columnInput);
            southPanel.add(timeTakenLabel);
            southPanel.add(nodesExpandedLabel);
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
