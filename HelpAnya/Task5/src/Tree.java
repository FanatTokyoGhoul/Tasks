import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Tree<T> implements DefaultTree<T> {

    protected class Node implements TreeNode<T> {

        public T value;
        public List<TreeNode<T>> child;

        public Node(T value, List<TreeNode<T>> child) {
            this.value = value;
            this.child = child;
        }

        public Node(T value) {
            this.value = value;
            this.child = null;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public TreeNode<T> getChildIndex(int index) throws Exception {
            return child.get(index);
        }

        @Override
        public List<TreeNode<T>> getChild() {
            return child;
        }
    }

    public Tree(Function<String, T> fromStrFunc) {
        this.fromStrFunc = fromStrFunc;
        this.stringFromT = T::toString;
    }

    protected Node root = null;
    protected Function<T, String> stringFromT;
    protected Function<String, T> fromStrFunc;

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    private T readValue(StringBuilder bracketNotation) throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bracketNotation.length(); i++) {
            if (bracketNotation.charAt(i) == ' ') {
                continue;
            }
            if (bracketNotation.charAt(i) == ';' || bracketNotation.charAt(i) == ',' || bracketNotation.charAt(i) == '(') {
                bracketNotation.delete(0, i);
                bracketNotation.deleteCharAt(0);
                bracketNotation.deleteCharAt(bracketNotation.length() - 1);
                break;
            }
            builder.append(bracketNotation.charAt(i));
        }

        return fromStr(builder.toString());
    }

    protected Node fromBracketStr(String bracketNotation) throws Exception {
        int counterBracket = 0;
        StringBuilder builder = new StringBuilder();
        StringBuilder line = new StringBuilder(bracketNotation);
        T parentValue = readValue(line);
        Node node = new Node(parentValue, new LinkedList<>());
        for (int i = 0; i < line.length(); i++) {
            if (counterBracket == 0) {
                if (line.charAt(i) == ' ') {
                    continue;
                } else if (line.charAt(i) == ';' || line.charAt(i) == ',') {
                    if (builder.length() > 0) {
                        node.child.add(new Node(fromStr(builder.toString()), new LinkedList<>()));
                        builder.delete(0, builder.length());
                    }
                } else if (line.charAt(i) == '(') {
                    builder.append(line.charAt(i));
                    counterBracket++;
                } else if (i == line.length() - 1) {
                    builder.append(line.charAt(i));
                    node.child.add(new Node(fromStr(builder.toString()), new LinkedList<>()));
                    builder.delete(0, builder.length());
                } else {
                    builder.append(line.charAt(i));
                }
            }else {
                builder.append(line.charAt(i));
                if (line.charAt(i) == ')') {
                    counterBracket--;
                    if (counterBracket == 0) {
                        node.child.add(fromBracketStr(builder.toString()));
                        builder.delete(0, builder.length());
                    }
                }
            }
        }
        if(counterBracket != 0){
            throw new Exception("BracketException");
        }
        return node;
    }

    public void fromBracketNotation(String bracketNotation) throws Exception {
        this.root = fromBracketStr(bracketNotation);
    }

    private T fromStr(String s) throws Exception {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        if (fromStrFunc == null) {
            throw new Exception("Не определена функция конвертации строки в T");
        }
        return fromStrFunc.apply(s);
    }

    public void reverseTree(){
        reverseNode(root);
    }

    private void reverseNode(TreeNode<T> node){
        if(node == null){
            return;
        }

        Collections.reverse(node.getChild());

        for(TreeNode<T> nodeList: node.getChild()){
            reverseNode(nodeList);
        }
    }
}
