package main.java;

public class Solv {
    public static String solve(
            double[] objectiveFunc,
            double[][] constraintLeftSide,
            Constraint[] constraintOperator,
            double[] constraintRightSide,
            boolean function) {
        Modeler model = new Modeler(constraintLeftSide, constraintRightSide,
                constraintOperator, objectiveFunc);

        Simplex simplex = new Simplex(model.getTableaux(),
                model.getNumberOfConstraint(),
                model.getNumberOfOriginalVariable(), function);
        String ans = "";

        double[] x = simplex.primal();
        for (int i = 0; i < x.length; i++)
            ans += "x[" + i + "] = " + x[i] + "\n";
        ans += "\nSolution: " + simplex.value();

        return ans;
    }
}
