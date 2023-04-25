import java.util.Arrays;
import java.util.List;

public class a2Part1 {

        public static void main(String[] _ignored) {
                // base cases
                runTests(false, 100, 0.2, false);
                runTests(true, 100, 0.2, false);

                // sensitivity testing
                // epoch testing
                System.out.println("Epoch testing");
                runTests(false, 1, 0.2, true);
                runTests(false, 101, 0.2, true);
                runTests(false, 201, 0.2, true);
                runTests(false, 301, 0.2, true);
                runTests(false, 401, 0.2, true);
                runTests(false, 501, 0.2, true);
                // learning rate testing
                System.out.println("Learning rate testing");
                runTests(false, 100, 0.2, true);
                runTests(false, 100, 1, true);
                runTests(false, 100, 1.8, true);
                runTests(false, 100, 2.6, true);
                runTests(false, 100, 3.4, true);
                runTests(false, 100, 4.2, true);
                runTests(false, 100, 5, true);
                runTests(false, 100, 5.8, true);
                runTests(false, 100, 6.6, true);
                runTests(false, 100, 7.4, true);
                runTests(false, 100, 8.2, true);
                runTests(false, 100, 9, true);


                // talk about if the changes are linear
        }

        public static void runTests(boolean biases, int epoch, double learning, boolean sTesting) {
                List<String[]> lines = Util.getLines("penguins307-train.csv");
                String[] header = lines.remove(0);
                String[] labels = Util.getLabels(lines);
                double[][] instances = Util.getData(lines, biases);

                // scale features to [0,1] to improve training
                Rescaler rescaler = new Rescaler(instances);
                rescaler.rescaleData(instances, biases);

                if (!sTesting)
                        System.out.println(Arrays.deepToString(instances));
                System.out.println(
                                "--------------------------------------------------------------------------------------------");

                if (biases) {
                        System.out.println("***Biases included***\n");
                }

                // We can"t use strings as labels directly in the network, so need to do some
                // transformations
                LabelEncoder label_encoder = new LabelEncoder(labels);
                // encode "Adelie" as 1, "Chinstrap" as 2, "Gentoo" as 3
                int[] integer_encoded = label_encoder.intEncode(labels);

                // encode 1 as [1, 0, 0], 2 as [0, 1, 0], and 3 as [0, 0, 1] (to fit with our
                // network
                // outputs!)
                int[][] onehot_encoded = label_encoder.oneHotEncode(labels);

                // Parameters. As per the handout.
                int n_in = 4, n_hidden = 2, n_out = 3;
                if (biases) {
                        n_in++;
                        n_hidden++;
                }


                double[][] initial_hidden_layer_weights = biases
                                ? new double[][] {{-0.28, -0.22, 0}, {0.08, 0.20, 0},
                                                {-0.30, 0.32, 0}, {0.10, 0.01, 0}, {0.02, -0.20, 0}}
                                : new double[][] {{-0.28, -0.22}, {0.08, 0.20}, {-0.30, 0.32},
                                                {0.10, 0.01}};

                double[][] initial_output_layer_weights = biases
                                ? new double[][] {{-0.29, 0.03, 0.21}, {0.08, 0.13, -0.36},
                                                {-0.33, 0.26, 0.06}}
                                : new double[][] {{-0.29, 0.03, 0.21}, {0.08, 0.13, -0.36}};

                NeuralNetwork nn = new NeuralNetwork(n_in, n_hidden, n_out,
                                initial_hidden_layer_weights, initial_output_layer_weights,
                                learning, biases, sTesting);

                if (!sTesting)
                        System.out.printf(
                                        "First instance has label %s, which is %d as an integer, and %s as a list of outputs.\n",
                                        labels[0], integer_encoded[0],
                                        Arrays.toString(onehot_encoded[0]));

                // need to wrap it into a 2D array
                int[] instance1_prediction = nn.predict(new double[][] {instances[0]}, true);
                String instance1_predicted_label;
                if (instance1_prediction[0] == -1) {
                        // This should never happen once you have implemented the feedforward.
                        instance1_predicted_label = "???";
                } else {
                        instance1_predicted_label =
                                        label_encoder.inverse_transform(instance1_prediction[0]);
                }
                if (!sTesting)
                        System.out.println("Predicted label for the first instance is: "
                                        + instance1_predicted_label);

                if (!sTesting)
                        System.out.println();

                if (!sTesting)
                        System.out.println("Hidden initial weights: "
                                        + Arrays.deepToString(initial_hidden_layer_weights));
                if (!sTesting)
                        System.out.println("Output initial weights: "
                                        + Arrays.deepToString(initial_output_layer_weights));

                double[][] outputs = nn.forward_pass(instances[0]);
                double[][][] error = nn.backward_propagate_error(instances[0], outputs[0],
                                outputs[1], onehot_encoded[0], true);
                nn.update_weights(error[0], error[1]);

                if (!sTesting)
                        System.out.println("Weights after performing BP for first instance only:");
                if (!sTesting)
                        System.out.println("Hidden layer weights:\n"
                                        + Arrays.deepToString(nn.hidden_layer_weights));
                if (!sTesting)
                        System.out.println("Output layer weights:\n"
                                        + Arrays.deepToString(nn.output_layer_weights));

                nn.train(instances, onehot_encoded, epoch);
                if (!sTesting)
                        System.out.println("\nAfter training:");
                if (!sTesting)
                        System.out.println("Hidden layer weights:\n"
                                        + Arrays.deepToString(nn.hidden_layer_weights) + "\n");
                if (!sTesting)
                        System.out.println("Output layer weights:\n"
                                        + Arrays.deepToString(nn.output_layer_weights) + "\n");



                List<String[]> lines_test = Util.getLines("penguins307-test.csv");
                String[] header_test = lines_test.remove(0);
                String[] labels_test = Util.getLabels(lines_test);
                double[][] instances_test = Util.getData(lines_test, biases);

                // scale the test according to our training data.
                rescaler.rescaleData(instances_test, biases);

                ;
                int[] predicted_labels = nn.predict(instances_test, false);
                int[] desiredLablesTest = label_encoder.intEncode(labels_test);
                int num_right = 0;
                for (int i = 0; i < desiredLablesTest.length; i++) {
                        if (desiredLablesTest[i] == predicted_labels[i]) {
                                num_right++;
                        }
                }
                double accuracy = (1.0 * num_right) / desiredLablesTest.length;
                if (!sTesting) {
                        System.out.println("test predicted labels: "
                                        + Arrays.toString(predicted_labels));
                        System.out.println("test hidden layer weights:\n"
                                        + Arrays.deepToString(nn.hidden_layer_weights) + "\n");
                        System.out.println("test output layer weights:\n"
                                        + Arrays.deepToString(nn.output_layer_weights) + "\n");
                }
                System.out.println("Test set accuracy: " + accuracy + "  epochs: " + epoch
                                + "    learning rate: " + learning + "\n");
                if (!sTesting) {
                        System.out.println("biases included? " + biases + "\n");
                        System.out.println("Finished!" + "\n");
                }

                System.out.println();
        }

}
