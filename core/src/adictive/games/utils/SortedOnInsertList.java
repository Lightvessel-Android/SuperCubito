package adictive.games.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortedOnInsertList<E> extends ArrayList<E>
{
    private final Comparator<E> comparator;

    public SortedOnInsertList(int initialAllocation, final Comparator<E> comparator) {
        super(initialAllocation);
        this.comparator = comparator;
    }

    @Override
    public void add(int index, E element) {
        throw new RuntimeException("Do not insert by index on a SortedOnInsertList");
    }

    @Override
    public boolean add(final E e) {
        final boolean result = super.add(e);
        Collections.sort(this, this.comparator);
        return result;
    }
}