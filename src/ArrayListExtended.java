import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ArrayListExtended<E> extends ArrayList<E> {

    public <E> ArrayListExtended()
    {
        super();
    }

    public <E> ArrayListExtended(Collection c)
    {
        super(c);
    }

    public ArrayListExtended<E> tail()
    {
        if (this.size() <= 1)
        {
            return new ArrayListExtended<E>();
        }
        else
        {
            return new ArrayListExtended<E>(this.subList(1, this.size()));
        }
    }

    public ArrayListExtended<E> body()
    {
        if (this.size() <= 1)
        {
            return new ArrayListExtended<E>();
        }
        else
        {
            return new ArrayListExtended<E>(this.subList(0, this.size() - 1));
        }
    }

    public E head()
    {
        if (this.size() == 0)
        {
            return null;
        }
        else
        {
            return super.get(0);
        }
    }

    public E last()
    {
        if (this.size() == 0)
        {
            return null;
        }
        else
        {
            return super.get(this.size()-1);
        }
    }

    public ArrayListExtended<E> append(ArrayListExtended<E> c)
    {
        this.addAll(c);
        return this;
    }

    public E getMaybe(int index)
    {
        if (this.size() == 0 || this == null)
        {
            return null;
        }
        else
        {
            return super.get(index);
        }
    }
}
