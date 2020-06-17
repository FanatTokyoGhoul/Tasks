package ru.vsu.cs.course1.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Реализация графа на основе списков смежности
 */
public class AdjListsGraph implements Graph {
    protected List<List<Integer>> vEdjLists = new ArrayList<>();
    protected int vCount = 0;
    protected int eCount = 0;

    private static Iterable<Integer> nullIterable = new Iterable<Integer>() {
        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Integer next() {
                    return null;
                }
            };
        }
    };

    @Override
    public String toDotWithPath(List<Integer> list){
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = this instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : this.adjacencies(v1)) {
                sb.append(String.format("  %d %s %d", v1, (isDigraph ? "->" : "--"), v2)).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        for(int i = 0; i < list.size(); i++){
            sb.append(list.get(i));
            if(i != list.size()-1){
                sb.append(isDigraph ? "->" : "--");
            }
        }
        sb.append("[color=\"#ff0000\"]").append(nl);
        sb.append("}").append(nl);

        return sb.toString();
    }

    @Override
    public List<List<Integer>> findPaths(int startV, int endV){
        List<List<Integer>> paths = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        path.add(startV);
        findNewPath(paths, path, endV);
        return paths;
    }

    private void findNewPath(List<List<Integer>> paths, List<Integer> path, int endV){
        for(Integer edge: adjacencies(path.get(path.size() - 1))){
            if(edge == endV){
                List<Integer> newPath = new ArrayList<>(path);
                path.add(edge);
                paths.add(path);
                path = newPath;
            }else{
                if(!path.contains(edge)) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(edge);
                    findNewPath(paths, newPath, endV);
                }
            }
        }
    }

    @Override
    public int vertexCount() {
        return vCount;
    }

    @Override
    public int edgeCount() {
        return eCount;
    }

    @Override
    public void addAdge(int v1, int v2) {
        int maxV = Math.max(v1, v2);
        // добавляем вершин в список списков связности
        for (; vCount <= maxV; vCount++) {
            vEdjLists.add(null);
        }
        if (!isAdj(v1, v2)) {
            if (vEdjLists.get(v1) == null) {
                vEdjLists.set(v1, new LinkedList<>());
            }
            vEdjLists.get(v1).add(v2);
            eCount++;
            // для наследников
            if (!(this instanceof Digraph)) {
                if (vEdjLists.get(v2) == null) {
                    vEdjLists.set(v2, new LinkedList<>());
                }
                vEdjLists.get(v2).add(v1);
            }
        }
    }

    private int countingRemove(List<Integer> list, int v) {
        int count = 0;
        if (list != null) {
            for (Iterator<Integer> it = list.iterator(); it.hasNext(); ) {
                if (it.next().equals(v)) {
                    it.remove();
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void removeAdge(int v1, int v2) {
        eCount -= countingRemove(vEdjLists.get(v1), v2);
        if (!(this instanceof Digraph)) {
            eCount -= countingRemove(vEdjLists.get(v2), v1);
        }
    }

    @Override
    public Iterable<Integer> adjacencies(int v) {
        return vEdjLists.get(v) == null ? nullIterable : vEdjLists.get(v);
    }
}
