package main.java;

public class SimplexTest {
    private static final boolean MAXIMIZE = true;
    private static final boolean MINIMIZE = false;

    // test client
    public static void main(String[] args) {
//        example1();
//        example2();
        example3();
    }

    private static void example1() {
        double[] objectiveFunc = {1, 2};
        double[][] constraintLeftSide = {
                {-1, 2},
                {1, 1},
                {1, -1},
                {0, 1}
        };
        Constraint[] constraintOperator = {
                Constraint.greatherThan,
                Constraint.greatherThan,
                Constraint.lessThan,
                Constraint.lessThan};
        double[] constraintRightSide = {
                2,
                4,
                2,
                6};

        extracted(objectiveFunc, constraintLeftSide, constraintOperator, constraintRightSide, MAXIMIZE);
    }

    private static void example2() {
        double[] objectiveFunc = {6, 5};
        double[][] constraintLeftSide = {
                {-3, 5},
                {-2, 5},
                {1, 0},
                {3, -8}
        };
        Constraint[] constraintOperator = {
                Constraint.lessThan,
                Constraint.lessThan,
                Constraint.lessThan,
                Constraint.lessThan};
        double[] constraintRightSide = {
                25,
                30,
                10,
                6};

        extracted(objectiveFunc, constraintLeftSide, constraintOperator, constraintRightSide, MAXIMIZE);
    }

    private static void example3() {
        double[] objectiveFunc = {-1, 2};
        double[][] constraintLeftSide = {
                {-2, 1, 1},
                {1, 1, 1},
                {1, -2, 1}
        };
        Constraint[] constraintOperator = {
                Constraint.equal,
                Constraint.equal,
                Constraint.equal};
        double[] constraintRightSide = {
                2,
                5,
                12};

        extracted(objectiveFunc, constraintLeftSide, constraintOperator, constraintRightSide, MINIMIZE);
    }

    private static void extracted(
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
        double[] x = simplex.primal();
        for (int i = 0; i < x.length; i++)
            System.out.println("x[" + i + "] = " + x[i]);
        System.out.println("Solution: " + simplex.value());
    }


}
