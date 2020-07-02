import java.util.Collection;

public class MyImplementationQueue<T> implements MyQueue<T> {

    private MyLinkedList<T> list;
    private int size = 0;

    public MyImplementationQueue() {
        list = new MyLinkedList<>();
    }

    public MyImplementationQueue(Collection<T> collection) {
        for (T element : collection) {
            list.addLast(element);
            size++;
        }
    }

    @Override
    public void enqueue(T value) {
        list.addLast(value);
        size++;
    }

    @Override
    public T dequeue() {
        T value = list.getFirstElement();
        list.removeFirst();
        size--;
        return value;
    }

    @Override
    public T peek() {
        return list.getFirstElement();
    }

    @Override
    public boolean empty() {
        return list.getQuantityElement() == 0;
    }

    @Override
    public int size() {
        return size;
    }

}
