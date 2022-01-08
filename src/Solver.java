import java.util.PriorityQueue;
import java.util.Stack;

public class Solver {
    static private int m_a;
    static private int m_b;
    private final PriorityQueue<SolverState> m_pq;
    private FieldState m_is;
    private final FieldState m_ts;

    private class SolverState implements Comparable<SolverState>
    {
        private final FieldState m_board;
        private final int m_moves;
        private final int m_priority;
        private final SolverState m_prev_state;

        public SolverState(FieldState board, int moves, SolverState previousState)
        {
            m_board = board;
            m_moves = moves;
            m_priority = m_a * m_board.Distance() + m_b * moves;
            m_prev_state = previousState;
        }

        public int compareTo(SolverState other)
        {
            return (m_priority - other.m_priority);
        }
    }

    public Solver(int size, int a, int b)
    {
        m_a = a;
        m_b = b;
        m_pq = new PriorityQueue<>();
        m_ts = FieldState.Target(size);
    }

    public boolean Solve(FieldState initial)
    {
        m_is = initial;
        m_pq.clear();
        m_pq.add(new SolverState(m_is, 0, null));
        while (!m_pq.isEmpty() && !m_pq.peek().m_board.Equals(m_ts))
        {
            SolverState min_state = m_pq.poll();
            var neighbors = min_state.m_board.Neighbors();
            for (FieldState neighbor : neighbors)
            {
                if (min_state.m_moves == 0)
                {
                    m_pq.add(new SolverState(neighbor, min_state.m_moves + 1, min_state));
                }
                else if (!neighbor.Equals(min_state.m_prev_state.m_board))
                {
                    m_pq.add(new SolverState(neighbor, min_state.m_moves + 1, min_state));
                }
            }
        }
        return IsSolved();
    }

    public boolean IsSolved()
    {
        return !m_pq.isEmpty() && m_pq.peek().m_board.Equals(m_ts);
    }

    public int MovesCount()
    {
        if (!IsSolved ())
            return -1;
        assert m_pq.peek() != null;
        return m_pq.peek().m_moves;
    }

    public void Print()
    {
        if (IsSolved())
        {
            Stack<FieldState> solutionStack = PathToSolution();
            while (!solutionStack.empty())
            {
                solutionStack.pop().Print();
            }
            System.out.println("Number of steps: " + MovesCount());
        }
        else
        {
            System.out.println("No solution!");
        }
    }

    public Stack<FieldState> PathToSolution()
    {
        if (!IsSolved() || m_pq.isEmpty())
            return null;

        Stack<FieldState> stackSolution = new Stack<>();
        SolverState current = m_pq.peek();
        while (current.m_prev_state != null)
        {
            stackSolution.push(current.m_board);
            current = current.m_prev_state;
        }
        stackSolution.push(m_is);
        return stackSolution;
    }
}