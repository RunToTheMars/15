import java.lang.reflect.Field;
import java.util.Arrays;

public class Main {
    static private int m_field_size = 3;
    static private int m_a = 3;
    static private int m_b = 1;

    static private int m_shuffle_num = 1000;

    static private int m_test_size = 20;
    static private int m_test_a_max = 10;
    static private int m_test_b_max = 10;

    public static void main(String[] args)
    {
//        simple_run ();
        test (false);
        test (true);
    }

    private static void simple_run ()
    {
        Solver solver = new Solver(m_field_size, m_a, m_b);

        System.out.println("Initial field: ");
        FieldState field = GenerateField(m_field_size);
        field.Print ();

        if (solver.Solve(field))
        {
            System.out.println("Solution: ");
            solver.Print();
        }
        else
        {
            System.out.println("Can't find solution: ");
        }
    }

    private static void test (Boolean print_time)
    {
        System.out.println("Field size:" + m_field_size + "x" + m_field_size);
        if (print_time)
            System.out.println("Average time for solving the puzzle depending on 'a' and 'b' coeffs:");
        else
            System.out.println("Average moves for solving the puzzle depending on 'a' and 'b' coeffs:");
        System.out.print("a\\b| ");

        for (int b = 1; b <= m_test_b_max; b++)
        {
            System.out.printf("   %2d  ", b);
        }

        System.out.println();

        for (int a = 1; a <= m_test_a_max; a++)
        {
            System.out.printf("%2d | ", a);
            for (int b = 1; b <= m_test_b_max; b++)
            {
                boolean testPassed = test(m_field_size, a, b, m_test_size, print_time);
                if (!testPassed)
                    System.out.println("FAIL");
            }
            System.out.println();
        }
    }

    private static boolean test (int puzzleSize, int a, int b, int test_size, boolean printTime)
    {
        Solver solver = new Solver(puzzleSize, a, b);
        double[] times = new double[test_size];
        int[] moves = new int[test_size];

        for (int test_i = 0; test_i < test_size; test_i++)
        {
            long start_time = System.currentTimeMillis();
            solver.Solve(GenerateField(puzzleSize));
            long finish_time = System.currentTimeMillis();
            times[test_i] = (finish_time - start_time) / 1000.0;
            if (!solver.IsSolved())
                return false;
            moves[test_i] = solver.MovesCount();
        }
        if (printTime)
            System.out.printf("%.3e  ", Arrays.stream(times).average().getAsDouble());
        else
            System.out.printf("%3.2f  ", Arrays.stream(moves).average().getAsDouble());
        return true;
    }

    private static FieldState GenerateField(int size)
    {
        FieldState field = FieldState.Target (size);
        for (int i = 0; i < m_shuffle_num; i++)
        {
            field = field.RandomMove();
        }
        return field;
    }
}