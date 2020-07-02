
import java.util.Queue;

public interface MyQueue<T> {
    void enqueue(T value);
    T dequeue();
    T peek();
    boolean empty();
    int size();
}