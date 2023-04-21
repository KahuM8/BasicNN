import java.util.Arrays;

public class NeuralNetwork {
    public final double[][] hidden_layer_weights;
    public final double[][] output_layer_weights;
    private final int num_inputs;
    private final int num_hidden;
    private final int num_outputs;
    private final double learning_rate;

    public NeuralNetwork(int num_inputs, int num_hidden, int num_outputs,
            double[][] initial_hidden_layer_weights, double[][] initial_output_layer_weights,
            double learning_rate) {
        // Initialise the network
        this.num_inputs = num_inputs;
        this.num_hidden = num_hidden;
        this.num_outputs = num_outputs;

        this.hidden_layer_weights = initial_hidden_layer_weights;
        this.output_layer_weights = initial_output_layer_weights;

        this.learning_rate = learning_rate;
    }


    // Calculate neuron activation for an input
    public double sigmoid(double input) {
        double output = Double.NaN; // TODO!
        return output;
    }

    // Feed forward pass input to a network output
    public double[][] forward_pass(double[] inputs) {
        double[] hidden_layer_outputs = new double[num_hidden];
        for (int i = 0; i < num_hidden; i++) {
            double weighted_sum = 0;
            for (int j = 0; j < inputs.length; j++) {
                weighted_sum += inputs[j] * hidden_layer_weights[j][i];
            }
            double output = sigmoid(weighted_sum);
            hidden_layer_outputs[i] = output;
        }

        double[] output_layer_outputs = new double[num_outputs];
        for (int i = 0; i < num_outputs; i++) {
            double weighted_sum = 0;
            for (int j = 0; j < hidden_layer_outputs.length; j++) {
                weighted_sum += hidden_layer_outputs[j] * output_layer_weights[j][i];
            }
            double output = sigmoid(weighted_sum);
            output_layer_outputs[i] = output;
        }
        return new double[][] {hidden_layer_outputs, output_layer_outputs};
    }

    public double[][][] backward_propagate_error(double[] inputs, double[] hidden_layer_outputs,
            double[] output_layer_outputs, int desired_outputs) {

        double[] output_layer_betas = new double[num_outputs];
        for (int i = 0; i < num_outputs; i++) {
            output_layer_betas[i] = (desired_outputs - output_layer_outputs[i])
                    * output_layer_outputs[i] * (1 - output_layer_outputs[i]);
        }
        System.out.println("OL betas: " + Arrays.toString(output_layer_betas));

        double[] hidden_layer_betas = new double[num_hidden];
        for (int i = 0; i < num_hidden; i++) {
            double error_sum = 0;
            for (int j = 0; j < num_outputs; j++) {
                error_sum += output_layer_betas[j] * output_layer_weights[i][j];
            }
            hidden_layer_betas[i] =
                    error_sum * hidden_layer_outputs[i] * (1 - hidden_layer_outputs[i]);
        }
        System.out.println("HL betas: " + Arrays.toString(hidden_layer_betas));

        double[][] delta_output_layer_weights = new double[num_hidden][num_outputs];
        for (int i = 0; i < num_hidden; i++) {
            for (int j = 0; j < num_outputs; j++) {
                delta_output_layer_weights[i][j] =
                        learning_rate * output_layer_betas[j] * hidden_layer_outputs[i];
            }
        }

        double[][] delta_hidden_layer_weights = new double[num_inputs][num_hidden];
        for (int i = 0; i < num_inputs; i++) {
            for (int j = 0; j < num_hidden; j++) {
                delta_hidden_layer_weights[i][j] =
                        learning_rate * hidden_layer_betas[j] * inputs[i];
            }
        }

        return new double[][][] {delta_output_layer_weights, delta_hidden_layer_weights};
    }

    public void update_weights(double[][] delta_output_layer_weights,
            double[][] delta_hidden_layer_weights) {
        // Update the weights for the output layer
        for (int i = 0; i < output_layer_weights.length; i++) {
            for (int j = 0; j < output_layer_weights[i].length; j++) {
                output_layer_weights[i][j] += delta_output_layer_weights[i][j];
            }
        }

        // Update the weights for the hidden layer
        for (int i = 0; i < hidden_layer_weights.length; i++) {
            for (int j = 0; j < hidden_layer_weights[i].length; j++) {
                hidden_layer_weights[i][j] += delta_hidden_layer_weights[i][j];
            }
        }
    }

    public void train(double[][] instances, int[] desired_outputs, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            System.out.println("epoch = " + epoch);
            int[] predictions = new int[instances.length];
            for (int i = 0; i < instances.length; i++) {
                double[] instance = instances[i];
                double[][] outputs = forward_pass(instance);
                double[][][] delta_weights = backward_propagate_error(instance, outputs[0],
                        outputs[1], desired_outputs[i]);
                int predicted_class = -1; // TODO!
                predictions[i] = predicted_class;

                // We use online learning, i.e. update the weights after every instance.
                update_weights(delta_weights[0], delta_weights[1]);
            }

            // Print new weights
            System.out
                    .println("Hidden layer weights \n" + Arrays.deepToString(hidden_layer_weights));
            System.out.println(
                    "Output layer weights  \n" + Arrays.deepToString(output_layer_weights));

            // TODO: Print accuracy achieved over this epoch
            double acc = Double.NaN;
            System.out.println("acc = " + acc);
        }
    }

    public int[] predict(double[][] instances) {
        int[] predictions = new int[instances.length];
        for (int i = 0; i < instances.length; i++) {
            double[] instance = instances[i];
            double[][] outputs = forward_pass(instance);
            int predicted_class = -1; // TODO !Should be 0, 1, or 2.
            predictions[i] = predicted_class;
        }
        return predictions;
    }

}
