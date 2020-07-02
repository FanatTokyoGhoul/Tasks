public class UtilsQueue {
    public static <T> void addMyQueueLast(MyQueue<T> queueFirst, MyQueue<T> queueSecond){
        MyLinkedList<T> listFirst = new MyLinkedList<>();
        MyLinkedList<T> listSecond = new MyLinkedList<>();

        while (!queueFirst.empty()){
            listFirst.addLast(queueFirst.dequeue());
        }

        while (!queueSecond.empty()){
            listSecond.addLast(queueSecond.dequeue());
        }

        for(T value: listFirst){
            queueSecond.enqueue(value);
        }

        for(T value: listSecond){
            queueSecond.enqueue(value);
            queueFirst.enqueue(value);
        }

        for(T value: listFirst){
            queueFirst.enqueue(value);
        }
    }
}
