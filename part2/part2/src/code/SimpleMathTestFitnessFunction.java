package code;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.terminal.Variable;

public class SimpleMathTestFitnessFunction extends GPFitnessFunction {

    private static final long serialVersionUID = 1L;

    private double[] xPoint;
    private double[] yPoint;
    private Variable xVariable;

    public SimpleMathTestFitnessFunction(double[] xPoint, double[] yPoint, Variable xVariable) {
        this.xPoint = xPoint;
        this.yPoint = yPoint;
        this.xVariable = xVariable;
    }

    @Override
    protected double evaluate(IGPProgram arg0) {
        double result = 0.0f;

        for (int i = 0; i < xPoint.length; i++) {
            xVariable.set(xPoint[i]);
            result += Math.abs(arg0.execute_double(0, new Object[0]) - yPoint[i]);
        }
        return result;
    }

}
