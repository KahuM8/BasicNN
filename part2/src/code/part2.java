package code;

import org.jgap.*;
import org.jgap.gp.GPProblem;
import org.jgap.gp.impl.GPGenotype;

import java.util.List;

public class part2 {

    public static void main(String[] args) throws InvalidConfigurationException {

        // load data
        List<String> lines = Util.getLines("regression.txt");
        System.out.println(lines);
        double[][] data = Util.getData(lines);

        GPProblem myProblem = new mathProblem(data[0], data[1]);
        GPGenotype geno = myProblem.create();

        for (int evolutions = 0; geno.getFittestProgram().getFitnessValue() > 0.01; evolutions++) {
            System.out.println("Generation: " + evolutions + " Fitness: " + geno.getFittestProgram().getFitnessValue());
            geno.evolve(1);
        }

        System.out.println("Generation: " + geno.getGPConfiguration().getGenerationNr() + " Fitness: "
                + geno.getFittestProgram().getFitnessValue());

        System.out.println("Best solution: " + geno.getFittestProgram().toStringNorm(0));

    }
}
