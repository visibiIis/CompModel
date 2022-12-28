import java.io.*;
import Jama.Matrix;

public class lw2 {
    public static void main(String[] args) throws Exception{
        // спостережувані дані
        double[] X_tabular = {1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 41, 45, 49, 53, 57, 61};
        double[] Y_tabular = {312.89, 1612, 4225, 8043, 12900, 18560, 24740, 31070,
                37160, 42510, 46600, 48820, 48510, 44960, 37370, 24910};

        // вибір найкращої моделі методом найменших квадратів
        int best_n = 0;
        double min_criterion = Math.pow(10, 100);

        for (int n = 1; n <= 100; n++) {
            Matrix B = find_b(X_tabular, Y_tabular, n);
            double criterion = 0;
            for (int i = 0; i < 16; i++) {
                criterion += Math.pow(function(X_tabular[i], B) - Y_tabular[i], 2);
            }

            if (criterion < min_criterion) {
                min_criterion = criterion;
                best_n = n;
            }
        }

        System.out.println("Критерій найменших квадратів = " + (float) min_criterion);

        Matrix desired_B = find_b(X_tabular, Y_tabular, best_n);
        System.out.println("\nНабір коефіцієнтів bі при найкращій моделі:");
        for (int i = 0; i < desired_B.getRowDimension(); i++) {
            System.out.println("b" + (i) + " = " + (float) desired_B.get(i, 0));
        }
        System.out.println();

        double[] Y_found = new double[16];
        for (int i = 0; i < 16; i++) {
            Y_found[i] = function(X_tabular[i], desired_B);
        }
        for (int i = 0; i < 16; i++) {
            System.out.println("Y" + (i+1) + ": tabular = " + Y_tabular[i] +
                    "; found = " + (float) Y_found[i]);
        }

        FileWriter file = new FileWriter("lw2.txt");
        for (int i = 0; i < 16; i++) {
            file.write(String.format("%.2f", Y_tabular[i])+"\n");
        }
        file.write("\n");
        for (int i = 0; i < 16; i++) {
            file.write(String.format("%.2f", Y_found[i])+"\n");
        }
        file.close();
    }

    // функція пошуку коефіцієнтів
    static Matrix find_b(double[] X_given, double[] Y_given, int n) {
        Matrix X = new Matrix(new double[16][n + 1]);
        for (int i = 1; i < n + 1; i++) {
            for (int j = 0; j < 16; j++) {
                X.set(j, 0, 1);
                X.set(j, i, Math.pow(X_given[j], i));
            }
        }

        Matrix Y = new Matrix(new double[16][1]);
        for (int i = 0; i < 16; i++) {
            Y.set(i, 0, Y_given[i]);
        }

        return X.transpose().times(X).inverse().times(X.transpose()).times(Y);
    }

    // функція залежності y від x і b
    static double function(double x, Matrix b) {
        double f = 0;
        for (int i = 0; i < b.getRowDimension(); i++) {
            f += b.get(i, 0) * (Math.pow(x, i));
        }
        return f;
    }
}


