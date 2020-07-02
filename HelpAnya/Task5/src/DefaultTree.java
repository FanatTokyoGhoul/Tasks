import java.util.*;

public interface DefaultTree<T> extends Iterable<T> {

    interface TreeNode<T> {

        T getValue();


        default TreeNode<T> getChildIndex(int index) throws Exception {
            return null;
        }
        default List<TreeNode<T>> getChild(){return null;}

    }

    TreeNode<T> getRoot();

    @FunctionalInterface
    interface Visitor<T> {

        public void visit(T value, int level);
    }

    default void preOrderVisit(Visitor<T> visitor) {
        class Inner {
            <T> void preOrderVisit(TreeNode<T> node, Visitor<T> visitor, int level) {
                if (node == null) {
                    return;
                }
                visitor.visit(node.getValue(), level);
                for(TreeNode<T> nextNode: node.getChild()){
                    preOrderVisit(nextNode, visitor, level + 1);
                }
            }
        }
        new Inner().preOrderVisit(getRoot(), visitor, 0);
    }

    default Iterable<T> preOrderValues() {
        return () -> {
            Stack<TreeNode<T>> stack = new Stack<>();
            if (getRoot() != null) {
                stack.push(getRoot());
            }

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return stack.size() > 0;
                }

                @Override
                public T next() {
                    TreeNode<T> node = stack.pop();

                    for(TreeNode<T> nextNode: node.getChild()){
                        if(nextNode != null) {
                            stack.push(nextNode);
                        }
                    }

                    return node.getValue();
                }

            };
        };
    }

    default Iterable<T> inOrderValues() {
        return () -> {
            Queue<TreeNode<T>> stack = new LinkedList<>();
            if (getRoot() != null) {
                stack.add(getRoot());
            }

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return stack.size() > 0;
                }

                @Override
                public T next() {
                    TreeNode<T> node = stack.poll();

                    for(TreeNode<T> nextNode: node.getChild()){
                        if(nextNode != null) {
                            stack.add(nextNode);
                        }
                    }

                    return node.getValue();
                }

            };
        };
    }

    default void byLevelVisit(Visitor<T> visitor) {
        class QueueItem {
            public DefaultTree.TreeNode<T> node;
            public int level;

            public QueueItem(DefaultTree.TreeNode<T> node, int level) {
                this.node = node;
                this.level = level;
            }
        }

        if (getRoot() == null) {
            return;
        }
        Queue<QueueItem> queue = new LinkedList<>();
        queue.add(new QueueItem(getRoot(), 0));
        while (queue.peek() != null) {
            QueueItem item = queue.poll();
           for(TreeNode<T> nextNode: item.node.getChild()){
               queue.add(new QueueItem(nextNode, item.level + 1));
           }
            visitor.visit(item.node.getValue(), item.level);
        }
    }

    @Override
    default Iterator<T> iterator() {
        return inOrderValues().iterator();
    }
}