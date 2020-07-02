import java.util.Collection;

public class MyImplementationStack<T> implements MyStack<T> {

    private MyLinkedList<T> list;
    private int size;

    public MyImplementationStack() {
        list = new MyLinkedList<>();
    }

    public MyImplementationStack(Collection<T> collection) {
        this.list = new MyLinkedList<>(collection);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void push(T value) {
        list.addFirst(value);
        size++;
    }

    @Override
    public T peek() {
        return list.getFirstElement();
    }

    @Override
    public T pop() {
        T value = list.getFirstElement();
        list.removeFirst();
        size--;
        return value;
    }

    @Override
    public boolean empty() {
        return list.getQuantityElement() == 0;
    }
}
