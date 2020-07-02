public interface MyStack<T> {
    void push(T value);
    T peek();
    T pop();
    boolean empty();
    int size();
}