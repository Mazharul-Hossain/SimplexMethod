package Simplex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class MyFrame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = -6417002867509503344L;
    private ImageIcon image = new ImageIcon("image\\0018.jpg");
    private JLabel objFuncLabel = new JLabel("Ŀ�꺯����");
    private JTextField objFunction = new JTextField();
    private JLabel constraintsLabel = new JLabel("Լ��������");
    private JTextArea constraintsArea = new JTextArea();
    private JButton button = new JButton("���");
    private JLabel solutionLabel = new JLabel("���Ž⣺");
    private JTextArea solutionShow = new JTextArea();

    public MyFrame() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setIconImage(image.getImage());
        setTitle("Simplex Method");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        initComponents();

    }

    public static void main(String[] args) {
        new MyFrame();
    }

    private void initComponents() {
        Container container = getContentPane();
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().addComponent(objFuncLabel).addComponent(objFunction).addComponent(constraintsLabel).addComponent(constraintsArea).addComponent(button));
        hGroup.addGroup(layout.createParallelGroup().addComponent(solutionLabel).addComponent(solutionShow));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup().addComponent(objFuncLabel));
        vGroup.addGroup(layout.createParallelGroup().addComponent(objFunction));
        vGroup.addGroup(layout.createParallelGroup().addComponent(constraintsLabel).addComponent(solutionLabel));
        vGroup.addGroup(layout.createParallelGroup().addComponent(constraintsArea).addComponent(solutionShow));
        vGroup.addGroup(layout.createParallelGroup().addComponent(button));
        layout.setVerticalGroup(vGroup);

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String objectiveFunc = objFunction.getText().trim();

                String constraints = constraintsArea.getText().trim();

                if (objectiveFunc.equals("") || constraints.equals("")) {
                    return;
                }
                String[] subConstraint = constraints.split("\n");
                ArrayList<String> constraintList = new ArrayList<String>();
                for (int i = 0; i < subConstraint.length; i++) {
                    constraintList.add(subConstraint[i]);
                }
                process(objectiveFunc, constraintList);
            }
        });
    }

    private void process(final String objectiveFunc, final ArrayList<String> constraints) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Normalization normalization = new Normalization(objectiveFunc, constraints);

                normalization.transferToCanonicalForm();
                normalization.objectiveFuncPrint();
                normalization.canonicalFormPrint();
                normalization.constantTermPrint();

                SimplexTable st = new SimplexTable(normalization.getMaxProblemState());
                st.initSimplexTable(normalization.getNumOfDecisionVar(), normalization.getCoeffOfMatrix(),
                        normalization.getCoeffOfDecisionVar(), normalization.getConstantTermList());
                st.execIteration();
                solutionShow.setText(st.getStringBuffer().toString());
            }
        }).start();
    }
}
