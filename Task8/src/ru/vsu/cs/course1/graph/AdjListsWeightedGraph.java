package ru.vsu.cs.course1.graph;

import java.util.*;

public class AdjListsWeightedGraph implements WeightedGraph {
    class Edge implements WeightedEdgeTo {

        int to;
        double weight;

        public Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int to() {
            return to;
        }

        @Override
        public double weight() {
            return weight;
        }
    }

    static class MatrixWeightAndHistory {
        public double[][] matrixWeight;
        public int[][] matrixHistory;
    }

    static class Vertex {

    }

    private static final int INFINITY = Integer.MAX_VALUE/2;

    protected int vertexCount = 0;
    protected int edgeCount = 0;
    protected List<List<WeightedEdgeTo>> vEdjLists = new ArrayList<>();

    private static Iterable<WeightedEdgeTo> nullIterable = new Iterable<WeightedEdgeTo>() {
        @Override
        public Iterator<WeightedEdgeTo> iterator() {
            return new Iterator<WeightedEdgeTo>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public WeightedEdgeTo next() {
                    return null;
                }
            };
        }
    };

    @Override
    public boolean isAdj(int v1, int v2) {
        for (WeightedEdgeTo adj : adjacenciesWithWeights(v1)) {
            if (adj.to() == v2) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addAdge(int v1, int v2, double weight) {
        int maxV = Math.max(v1, v2);
        // добавляем вершин в список списков связности
        for (; vertexCount <= maxV; vertexCount++) {
            vEdjLists.add(null);
        }
        if (!isAdj(v1, v2)) {
            if (vEdjLists.get(v1) == null) {
                vEdjLists.set(v1, new LinkedList<>());
            }
            vEdjLists.get(v1).add(new Edge(v2, weight));
            edgeCount++;
            // для наследников
            if (!(this instanceof Digraph)) {
                if (vEdjLists.get(v2) == null) {
                    vEdjLists.set(v2, new LinkedList<>());
                }
                vEdjLists.get(v2).add(new Edge(v1, weight));
            }
        }
    }

    private List<Integer> restoringThePath(int start, int end, int[][] matrixHistory) {
        List<Integer> listPath = new ArrayList<>();
        listPath.add(start);
        while (start != end) {
            start = matrixHistory[start][end];
            listPath.add(start);
        }
        return listPath;
    }

    public List<Integer> theChinesePostman() {
        Map<Integer, Integer> map = new TreeMap<>();
        List<Integer> list = findOddVertexes(map);

        if (list != null) {
            MatrixWeightAndHistory matrixWeightAndHistory = new MatrixWeightAndHistory();
            matrixWeightAndHistory.matrixWeight = crateWeightMatrix();
            matrixWeightAndHistory.matrixHistory = crateAdjacencyMatrix();
            algorithmFloydWarshall(matrixWeightAndHistory);

            double[][] oddMatrix = createWeightOddMatrix(matrixWeightAndHistory.matrixWeight, map);
            int[] minMatches = minMatches(oddMatrix);
            int[][] adjacencyMatrix = crateMatrix();
            List<Integer> path;

            for (int i = 0; i < minMatches.length; i += 2) {
                path = restoringThePath(map.get(minMatches[i]), map.get(minMatches[i + 1]), matrixWeightAndHistory.matrixHistory);
                for (int j = 0; j < path.size() - 1; j++) {
                    adjacencyMatrix[path.get(j)][path.get(j + 1)]++;
                    adjacencyMatrix[path.get(j + 1)][path.get(j)]++;
                }
            }
            return   eilerPath(adjacencyMatrix);
        } else {
            return eilerPath(crateMatrix());
        }
    }

    private double[][] createWeightOddMatrix(double[][] matrixWeight, Map<Integer, Integer> odds){
        double[][] oddMatrix = new double[odds.size()][odds.size()];
        for(double[] array: oddMatrix) {
            Arrays.fill(array, INFINITY);
        }

        for(int i = 0; i < odds.size(); i++){

                for (int j = 0; j < odds.size(); j++) {
                    oddMatrix[i][j] = matrixWeight[odds.get(i)][odds.get(j)];
                }

        }

        return oddMatrix;
    }

    private double[][] crateWeightMatrix() {
        double[][] matrix = new double[vertexCount][vertexCount];
        for(double[] array: matrix) {
            Arrays.fill(array, INFINITY);
        }
        for (int i = 0; i < vEdjLists.size(); i++) {
            for (WeightedEdgeTo edge : vEdjLists.get(i)) {
                matrix[i][edge.to()] = edge.weight();
                matrix[edge.to()][i] = edge.weight();
            }
        }
        return matrix;
    }

    private int[][] crateMatrix() {
        int[][] matrix = new int[vertexCount][vertexCount];
        for (int i = 0; i < vEdjLists.size(); i++) {
            for (WeightedEdgeTo edge : vEdjLists.get(i)) {
                matrix[i][edge.to()] = 1;
                matrix[edge.to()][i] = 1;
            }
        }
        return matrix;
    }

    private int[][] crateAdjacencyMatrix() {
        int[][] matrix = new int[vertexCount][vertexCount];
        for(int[] array: matrix) {
            Arrays.fill(array, -1);
        }
        for (int i = 0; i < vEdjLists.size(); i++) {
            for (WeightedEdgeTo edge : vEdjLists.get(i)) {
                matrix[i][edge.to()] = edge.to();
                matrix[edge.to()][i] = i;
            }
        }
        return matrix;
    }

    private List<Integer> eilerPath(int[][] matrixAdjacency) {
        Stack<Integer> stack = new Stack<>();
        List<Integer> list = new ArrayList<>();
        int v = 0;
        int u;
        int edge;
        stack.push(v);
        while (!stack.empty()) {
            edge = findAdjacencyVertex(matrixAdjacency, stack.peek());
            if (edge == -1) {
                list.add(stack.pop());
            } else {
                u = edge;
                matrixAdjacency[stack.peek()][u]--;
                matrixAdjacency[u][stack.peek()]--;
                stack.push(u);
            }
        }
        return list;
    }

    private int findAdjacencyVertex(int[][] matrixAdjacency, int edge) {
        for (int i = 0; i < matrixAdjacency.length; i++) {
            if (matrixAdjacency[edge][i] > 0) {
                return i;
            }
        }
        return -1;
    }

    private List<Integer> findOddVertexes(Map<Integer, Integer> map) {
        List<Integer> list = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < vEdjLists.size(); i++) {
            if (vEdjLists.get(i).size() % 2 != 0) {
                list.add(i);
                map.put(counter, i);
                counter++;
            }
        }

        if (list.size() == 0) {
            return null;
        }
        return list;
    }

    private MatrixWeightAndHistory algorithmFloydWarshall(MatrixWeightAndHistory matrixWeightAndHistory) {
        double[][] matrixWeight = matrixWeightAndHistory.matrixWeight;
        int[][] matrixHistory = matrixWeightAndHistory.matrixHistory;

        int size = matrixWeight.length;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrixWeight[i][j] > -1) {
                    for (int w = 0; w < size; w++) {
                        if (matrixWeight[i][w] > matrixWeight[i][j] + matrixWeight[j][w] && i != w) {
                            matrixWeight[i][w] = matrixWeight[i][j] + matrixWeight[j][w];
                            matrixHistory[i][w] = matrixHistory[i][j];
                        }
                    }

                }
            }
        }
        return matrixWeightAndHistory;
    }

    private int[] minMatches(double[][] matrix) {
        double minWeight = Double.MAX_VALUE;
        int index = -1;
        double weight = 0;

        List<int[]> list = findPath(matrix.length);

        for (int[] array : list) {
            for (int i = 0; i < matrix.length; i += 2) {
                weight += matrix[array[i]][array[i + 1]];
            }
            if (weight < minWeight) {
                minWeight = weight;
                index = list.indexOf(array);
            }
        }

        return list.get(index);
    }

    private List<int[]> findPath(int n) {
        int count = fuctorial(n);
        int max = n - 1;
        int shift = max;

        List<int[]> list = new ArrayList<>();

        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i;
        }

        int buffer = 0;
        int[] bufferMatrix;

        while (count > 0) {
            buffer = array[shift];
            array[shift] = array[shift - 1];
            array[shift - 1] = buffer;
            bufferMatrix = Arrays.copyOf(array, array.length);
            list.add(bufferMatrix);
            count--;
            if (shift < 2) {
                shift = max;
            } else {
                shift--;
            }
        }

        return list;
    }

    int fuctorial(int n) {
        return (n > 0) ? n * fuctorial(n - 1) : 1;
    }

    @Override
    public Iterable<WeightedEdgeTo> adjacenciesWithWeights(int v) {
        return vEdjLists.get(v) == null ? nullIterable : vEdjLists.get(v);
    }

    @Override
    public int vertexCount() {
        return vertexCount;
    }

    @Override
    public int edgeCount() {
        return edgeCount;
    }

    @Override
    public void addAdge(int v1, int v2) {

    }

    private int countingRemove(List<WeightedEdgeTo> list, int v) {
        int count = 0;
        if (list != null) {
            for (Iterator<WeightedEdgeTo> it = list.iterator(); it.hasNext(); ) {
                if (it.next().to() == v) {
                    it.remove();
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void removeAdge(int v1, int v2) {
        edgeCount -= countingRemove(vEdjLists.get(v1), v2);
        if (!(this instanceof Digraph)) {
            edgeCount -= countingRemove(vEdjLists.get(v2), v1);
        }
    }

    @Override
    public Iterable<Integer> adjacencies(int v) {
        return null;
    }

    @Override
    public String toDot() {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = this instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < vertexCount(); v1++) {
            int count = 0;
            for (WeightedEdgeTo v2 : this.adjacenciesWithWeights(v1)) {
                sb.append(String.format("  %d %s %d", v1, (isDigraph ? "->" : "--"), v2.to())).append("[label=\"").append(v2.weight()).append("\"]").append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
}
