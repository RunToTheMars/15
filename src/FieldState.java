import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FieldState
{
    private final Integer m_size;
    private final Integer[] m_positions;
    private Integer m_empty_pos;

    public FieldState(Integer[] positions)
    {
        m_positions = positions;
        m_size = (int) Math.sqrt(positions.length);
        for (int i = 0; i < m_positions.length; i++)
        {
            if (m_positions[i] == 0)
            {
                m_empty_pos = i;
                break;
            }
        }
    }

    public boolean Equals(Object other)
    {
        if (other == this)
        {
            return true;
        }

        if (other == null)
        {
            return false;
        }

        if (other.getClass() != this.getClass())
        {
            return false;
        }

        FieldState other_field = (FieldState) other;
        return Arrays.equals(this.m_positions, other_field.m_positions);
    }

    public Integer At(int row, int col)
    {
        return m_positions[row * m_size + col];
    }

    public int Distance()
    {
        int res = 0;
        for (int i = 0; i < m_size; i++)
        {
            for (int j = 0; j < m_size; j++)
            {
                Integer value = At(i, j);
                if (value == 0)
                    continue;

                int diff_row = Math.abs(TargetRowFor(value) - i);
                int diff_col = Math.abs(TargetColumnFor(value) - j);
                res += diff_row + diff_col;
            }
        }
        return res;
    }

    private int TargetRowFor(int value)
    {
        if (value == 0)
            return m_size - 1;
        return (value - 1) / m_size;
    }

    private int TargetColumnFor(int value)
    {
        if (value % m_size == 0)
            return m_size - 1;
        return (value % m_size) - 1;
    }

    public ArrayList<FieldState> Neighbors()
    {
        ArrayList<FieldState> list = new ArrayList<>();

        FieldState up = MoveUp();
        if (up != this)
            list.add(up);

        FieldState down = MoveDown();
        if (down != this)
            list.add(down);

        FieldState left = MoveLeft();
        if (left != this)
            list.add(left);

        FieldState right = MoveRight();
        if (right != this)
            list.add(right);

        return list;
    }

    public FieldState MoveLeft()
    {
        if (m_empty_pos % m_size == 0)
            return this;
        Integer[] new_positions = m_positions.clone();
        new_positions[m_empty_pos] = new_positions[m_empty_pos - 1];
        new_positions[m_empty_pos - 1] = 0;
        return new FieldState(new_positions);
    }

    public FieldState MoveRight()
    {
        if (m_empty_pos % m_size == m_size - 1)
            return this;
        Integer[] new_positions = m_positions.clone();
        new_positions[m_empty_pos] = new_positions[m_empty_pos + 1];
        new_positions[m_empty_pos + 1] = 0;
        return new FieldState(new_positions);
    }

    public FieldState MoveUp()
    {
        if (m_empty_pos < m_size)
            return this;
        Integer[] new_positions = m_positions.clone();
        new_positions[m_empty_pos] = new_positions[m_empty_pos - m_size];
        new_positions[m_empty_pos - m_size] = 0;
        return new FieldState(new_positions);
    }

    public FieldState MoveDown()
    {
        if (m_empty_pos > m_size * (m_size - 1) - 1)
            return this;
        Integer[] new_positions = m_positions.clone();
        new_positions[m_empty_pos] = new_positions[m_empty_pos + m_size];
        new_positions[m_empty_pos + m_size] = 0;
        return new FieldState(new_positions);
    }

    public FieldState RandomMove()
    {
        Random rnd = new Random();
        int direction = rnd.nextInt(4);
        return switch (direction)
        {
            case 0 -> this.MoveLeft();
            case 1 -> this.MoveRight();
            case 2 -> this.MoveUp();
            case 3 -> this.MoveDown();
            default -> this;
        };
    }

    public void Print()
    {
        for (int i = 0; i < m_size; i++)
        {
            System.out.print("|");
            for (int j = 0; j < m_size; j++)
            {
                if (At(i, j) == 0)
                    System.out.print("   ");
                else
                    System.out.printf("%2d ", At(i, j));
            }
            System.out.println("|");
        }
        System.out.println();
    }

    public static FieldState Target(int size)
    {
        Integer[] positions = new Integer[size * size];
        for (int i = 0; i < size * size - 1; i++)
            positions[i] = i + 1;
        positions[size * size - 1] = 0;
        return new FieldState(positions);
    }
}