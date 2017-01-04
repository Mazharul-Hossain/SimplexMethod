package Simplex;

import java.util.ArrayList;

public class Normalization {
    private String objectiveFunction;
    private int numOfDecisionVar;
    private ArrayList<Double> coeffOfDecisionVar = new ArrayList<Double>();
    private ArrayList<String> constraints = new ArrayList<String>();
    private ArrayList<ArrayList<Double>> coeffOfMatrix = new ArrayList<ArrayList<Double>>();
    private ArrayList<String> constants = new ArrayList<String>();
    private ArrayList<Double> constantTermList = new ArrayList<Double>();
    private boolean maxProblem;

    public Normalization(String objectiveFunction, ArrayList<String> constraints, ArrayList<String> constants) {
        this.objectiveFunction = objectiveFunction;
        this.constraints = constraints;
        this.constants = constants;
    }

    public void readObjectiveFunc(String objectiveFunc) {
        objectiveFunction = objectiveFunc;

    }

    public void readConstraints(ArrayList<String> constraintsList) {
        constraints = constraintsList;
    }

    public ArrayList<ArrayList<Double>> getCoeffOfMatrix() {

        //canonicalFormPrint();

        return coeffOfMatrix;
    }

    public ArrayList<Double> getConstantTermList() {

        //constantTermPrint();

        return constantTermList;
    }

    public ArrayList<Double> getCoeffOfDecisionVar() {

        //objectiveFuncPrint();

        return coeffOfDecisionVar;
    }

    public int getNumOfDecisionVar() {
        //System.out.println("numOfDecisionVar = " + numOfDecisionVar);
        return numOfDecisionVar;
    }

    public boolean getMaxProblemState() {

        //System.out.println("maxProblem : " + maxProblem);
        return maxProblem;
    }

    public void transferToCanonicalForm() {

        maxProblem = true;

        String[] temp = objectiveFunction.split("\\s+");
        for (String str : temp) {

            Double d = Double.parseDouble(str.trim());
            coeffOfDecisionVar.add(d);
        }
        numOfDecisionVar = coeffOfDecisionVar.size();

        //step3
        int numOfRow = constraints.size();
        for (String value : constraints) {

            ArrayList<Double> row = new ArrayList<Double>();

            String[] coeffList = value.split("\\s+");
            for (String coeff : coeffList) {
                row.add(Double.parseDouble(coeff.trim()));
            }
            for (int i = numOfDecisionVar; i < numOfDecisionVar + numOfRow; i++) {
                row.add(0.0);
            }
            coeffOfMatrix.add(row);
        }
        for (String value : constants) {
            constantTermList.add(Double.parseDouble(value.trim()));
        }

        //step4
        int j = numOfDecisionVar;
        for (int i = 0; i < numOfRow; i++) {
            coeffOfMatrix.get(i).set(j++, 1.0);
        }
        for (int i = numOfDecisionVar; i < numOfDecisionVar + numOfRow; i++) {
            coeffOfDecisionVar.add(0.0);
        }
    }

    public void canonicalFormPrint() {
        System.out.println("2 coeffOfMatrix : ");

        for (int i = 0; i < coeffOfMatrix.size(); i++) {
            for (int j = 0; j < coeffOfMatrix.get(i).size(); j++) {
                System.out.print(coeffOfMatrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    public void constantTermPrint() {
        System.out.println("4 constantTermList : ");
        for (int i = 0; i < constantTermList.size(); i++) {
            System.out.print(constantTermList.get(i) + " ");
        }
        System.out.println();
    }

    public void objectiveFuncPrint() {

        System.out.println("3 coeffOfDecisionVar : ");
        for (int i = 0; i < coeffOfDecisionVar.size(); i++) {
            System.out.print(coeffOfDecisionVar.get(i) + " ");
        }
        System.out.println();
    }
}
