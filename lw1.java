import java.util.Arrays;
import java.io.*;
import java.lang.Object;
import org.apache.commons.math3.special.Erf;

public class lw1 {
    public static void main(String[] args) throws Exception{
        int n = 1000;
        int a = 58900;
        int sigma = 8;
        System.out.println("a = " + a + "; sigma = " + sigma);
        double[] mu = new double[n];
        for (int j = 0; j < n; j++) {
            double[] eps = new double[12];
            for (int i = 0; i < 12; i++) {
                eps[i] = Math.random();
            }
            for (int i = 0; i < 12; i++) {
                mu[j] += eps[i];
            }
            mu[j] -= 6;
        }

        // випадкова величина x
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = sigma * mu[i] + a;
        }
        x = Arrays.stream(x).sorted().toArray();

        FileWriter file = new FileWriter("lw1.txt");

        // ширина та границі інтервалів
        double h = (x[n-1] - x[0]) / 20;
        double[] boundaries = new double[21];
        for (int i = 0; i < 21; i++) {
            boundaries[i] = x[0] + i * h;
            file.write(String.format("%.2f", boundaries[i])+"\n");
        }
        file.write("\n");

        // кількість влучень в інтервали
        int[] y = new int[20];
        x[0] += 0.0000001;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 20; j++) {
                if (boundaries[j] < x[i] && x[i] <= boundaries[j+1]) {
                    y[j] += 1;
                }
            }
        }

        for (int i = 0; i < 20; i++) {
            file.write(y[i]+"\n");
        }
        file.close();

        // середне значення
        double x_average = (Arrays.stream(x).sum()) / n;
        System.out.println("Середнє значення x = " + String.format("%.6f", x_average));

        // дисперсія
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Math.pow((x[i] - x_average), 2);
        }
        double dispersion = sum / (n - 1);
        System.out.println("Дисперсія = " + String.format("%.6f", dispersion));

        // середнє квадратичне відхилення
        double deviation = Math.pow(dispersion, 0.5);
        System.out.println("Середнє квадратичне відхилення = " + String.format("%.6f", deviation));

        // теоретичне значення частоти влучення випадкової величини в інтервали
        double[] p_teory = new double[20];
        for (int i = 0; i < 20; i++) {
            p_teory[i] = n * 0.5 * Math.abs
                    (Erf.erf((boundaries[i+1] - x_average)/(deviation * Math.pow(2, 0.5)),
                    (boundaries[i] - x_average)/(deviation * Math.pow(2, 0.5))));
        }

        // критерій хі квадрат
        double chi2 = 0;
        for (int i = 0; i < 20; i++) {
            chi2 += Math.pow(y[i] - p_teory[i], 2) / p_teory[i];
        }


        System.out.println("Табличне значення критерію хі квадрат: 30.14353");
        System.out.println("Розраховане значення критерію: " + String.format("%.5f", chi2));
        System.out.println("З довірчою ймовірністю 0,95 можна стверджувати, що:");
        if (chi2 <= 30.14353) {
            System.out.println("Випадкова величина розподілена за нормальним законом розподілу.");
        }
        else {
            System.out.println("Випадкова величина не розподілена за рівномірним законом розподілу.");
        }
    }
}
