package code;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPProblem;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

public class mathProblem extends GPProblem {
    // Create a GP Configuration Object
    Variable _xVariable;

    public mathProblem(double[] xs, double[] ys) throws InvalidConfigurationException {
        super(new GPConfiguration());

        GPConfiguration a_conf = getGPConfiguration();

        _xVariable = Variable.create(a_conf, "x", CommandGene.DoubleClass);

        a_conf.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
        a_conf.setMaxInitDepth(10);
        a_conf.setPopulationSize(1000);
        a_conf.setMaxCrossoverDepth(8);
        a_conf.setFitnessFunction(new SimpleMathTestFitnessFunction(xs, ys, _xVariable));
        a_conf.setStrictProgramCreation(true);

    }

    @Override
    public GPGenotype create() throws InvalidConfigurationException {
        GPConfiguration config = getGPConfiguration();

        // The return type of the GP program.
        Class<?>[] types = { CommandGene.DoubleClass };

        // Arguments of result-producing chromosome: none
        Class<?>[][] argTypes = { {} };

        // Next, we define the set of available GP commands and terminals to
        // use.
        CommandGene[][] nodeSets = {
                {
                        _xVariable,
                        new Add(config, CommandGene.DoubleClass),
                        new Multiply(config, CommandGene.DoubleClass),
                        new Divide(config, CommandGene.DoubleClass),
                        new Subtract(config, CommandGene.DoubleClass),
                        new Terminal(config, CommandGene.DoubleClass, -1, 1, true)
                }
        };

        GPGenotype result = GPGenotype.randomInitialGenotype(config, types, argTypes,
                nodeSets, 20, true);

        return result;
    }
}
