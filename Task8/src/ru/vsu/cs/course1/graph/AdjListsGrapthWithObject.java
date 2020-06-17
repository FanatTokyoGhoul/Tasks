package ru.vsu.cs.course1.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdjListsGrapthWithObject<T> extends AdjListsGraph {
    private List<T> listObjects = new ArrayList<>();

    public void add(T object, int v){
        for (; vCount <= v; vCount++) {
            vEdjLists.add(null);
            listObjects.add(null);
        }

        listObjects.set(v, object);
    }
    public T get(int v){
        return listObjects.get(v);
    }

    @Override
    public void addAdge(int v1, int v2) {
        int maxV = Math.max(v1, v2);
        // добавляем вершин в список списков связности
        for (; vCount <= maxV; vCount++) {
            vEdjLists.add(null);
            listObjects.add(null);
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
}
