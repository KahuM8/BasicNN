
package code;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Util {
    public static List<String> getLines(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (Exception e) {
            return null;
        }
    }

    public static double[][] getData(List<String> rows) {
        double[][] data = new double[2][rows.size() - 2];

        for (int i = 2; i < rows.size(); i++) {
            Scanner sc = new Scanner(rows.get(i));
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            data[0][i - 2] = x;
            data[1][i - 2] = y;
        }
        return data;

    }

}