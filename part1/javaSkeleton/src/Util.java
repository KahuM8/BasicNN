import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<String[]> getLines(String path) {
        List<String[]> rowList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineItems = line.split(",");
                rowList.add(lineItems);
            }
        } catch (Exception e) {
            // Handle any I/O problems
            throw new Error(e);
        }

        return rowList;
    }

    public static double[][] getData(List<String[]> rows, boolean biases)  {
        double[][] data = new double[rows.size()][rows.get(0).length - (biases?0:1)];
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            //exclude the label row!
            for (int j = 0; j < row.length - 1; j++) {
                data[i][j] = Double.parseDouble(row[j]);
            }
            if(biases){
                data[i][data[i].length-1] = 1.0;
            }
        }
        return data;
    }

    public static String[] getLabels(List<String[]> rows) {
        String[] labels = new String[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            labels[i] = row[row.length - 1];
        }
        return labels;
    }
}
