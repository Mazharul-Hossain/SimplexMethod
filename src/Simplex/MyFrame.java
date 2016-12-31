package Simplex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class MyFrame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = -6417002867509503344L;
    private ImageIcon image = new ImageIcon("image\\0018.jpg");
    private JLabel objFuncLabel = new JLabel("Objective function");
    private JTextField objFunction = new JTextField();
    private JLabel constantsLabel = new JLabel("Constants");
    private JTextArea constantsArea = new JTextArea();
    private JLabel constraintsLabel = new JLabel("Restrictions");
    private JTextArea constraintsArea = new JTextArea();
    private JButton button = new JButton("Solve");
    private JLabel solutionLabel = new JLabel("Optimal solution");
    private JTextArea solutionShow = new JTextArea();

    public MyFrame() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setIconImage(image.getImage());
        setTitle("Simplex Method");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        setVisible(true);

    }

    public static void main(String[] args) {
        new MyFrame();
    }

    static String readFile(String path) {

        Charset encoding = StandardCharsets.UTF_8;//Charset.defaultCharset();
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, encoding);
    }

    private void initComponents() {

        Container container = getContentPane();
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // horizontal group
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup().addComponent(objFuncLabel).addComponent(objFunction).addComponent(constraintsLabel).addComponent(constraintsArea).addComponent(button));

        hGroup.addGroup(layout.createParallelGroup().addComponent(constantsLabel).addComponent(constantsArea).addComponent(solutionLabel).addComponent(solutionShow));

        layout.setHorizontalGroup(hGroup);

        // vertical group
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup().addComponent(objFuncLabel).addComponent(constantsLabel));
        vGroup.addGroup(layout.createParallelGroup().addComponent(objFunction).addComponent(constantsArea));
        vGroup.addGroup(layout.createParallelGroup().addComponent(constraintsLabel).addComponent(solutionLabel));
        vGroup.addGroup(layout.createParallelGroup().addComponent(constraintsArea).addComponent(solutionShow));
        vGroup.addGroup(layout.createParallelGroup().addComponent(button));

        layout.setVerticalGroup(vGroup);

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                /*String objectiveFunc = objFunction.getText().trim();

                String constraints.txt = constraintsArea.getText().trim();

                String constants.txt = constantsArea.getText().trim();

                if (objectiveFunc.equals("") || constraints.txt.equals("")|| constants.txt.equals("")) {
                    return;
                }*/

                String objectiveFunc = readFile("input\\optimum.txt");
                String constraints = readFile("input\\constraints.txt");
                String constants = readFile("input\\constants.txt");

                String[] subConstraint = constraints.split("\n");
                ArrayList<String> constraintList = new ArrayList<String>();
                for (int i = 0; i < subConstraint.length; i++) {
                    constraintList.add(subConstraint[i]);
                }

                String[] subConstant = constants.split("\n");
                ArrayList<String> constantList = new ArrayList<String>();
                for (int i = 0; i < subConstant.length; i++) {
                    constantList.add(subConstant[i]);
                }

                process(objectiveFunc, constraintList, constantList);
            }
        });
    }

    private void process(final String objectiveFunc, final ArrayList<String> constraints, final ArrayList<String> constants) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Normalization normalization = new Normalization(objectiveFunc, constraints, constants);

                normalization.transferToCanonicalForm();
                //normalization.objectiveFuncPrint();
                //normalization.canonicalFormPrint();
                //normalization.constantTermPrint();

                SimplexTable st = new SimplexTable(normalization.getMaxProblemState());
                st.initSimplexTable(normalization.getNumOfDecisionVar(), normalization.getCoeffOfMatrix(),
                        normalization.getCoeffOfDecisionVar(), normalization.getConstantTermList());
                st.execIteration();
                solutionShow.setText(st.getStringBuffer().toString());
            }
        }).start();
    }
}
