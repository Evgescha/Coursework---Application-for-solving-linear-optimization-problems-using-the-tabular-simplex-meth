package main.java;

enum Constraint {
    lessThan,
    equal,
    greatherThan;

    public static Constraint find(String operator) {
        if (operator.equalsIgnoreCase("<")
                || operator.equalsIgnoreCase("<=")) {
            return lessThan;
        } else if (operator.equalsIgnoreCase("=")) {
            return equal;
        } else if (operator.equalsIgnoreCase(">")
                || operator.equalsIgnoreCase(">=")) {
            return greatherThan;
        } else {
            throw new RuntimeException("invalid input");
        }
    }

}
