package byog;

public class ArrayList<Item> {
    private Item[] items;
    private int size;

    /** Creates an empty list. */
    public ArrayList() {
        items = (Item[]) new Object[8];
        size = 0;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, Math.min(capacity, size));
        items = a;
    }

    /** Inserts X into the back of the list. */
    public void addLast(Item x) {
        if (size == items.length) {
            resize(size + 1);
        }
        items[size] = x;
        size = size + 1;
    }

    /** Returns the item from the back of the list. */
    public Item getLast() {
        return items[size - 1];
    }
    /** Gets the ith item in the list (0 is the front). */
    public Item get(int i) {
        return items[i];
    }

    public Item remove(int i) {
        Item stuff = items[i];
        for (int j = i; j < size - 1; j += 1) {
            items[j] = items[j + 1];
        }
        resize(size - 1);
        size -= 1;
        return stuff;
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }

    /** Deletes item from back of the list and
      * returns deleted item. */
    public Item removeLast() {
        Item x = getLast();
        items[size - 1] = null;
        size = size - 1;
        return x;
    }

    public void print() {
        for (int i = 0; i < size; i += 1) {
            System.out.print(items[i] + " ");
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
