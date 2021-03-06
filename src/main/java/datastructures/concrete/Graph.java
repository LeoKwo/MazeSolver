package datastructures.concrete;

// import com.sun.tools.javadoc.DocImpl;
// import com.sun.xml.internal.bind.v2.model.core.ID;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
// import datastructures.concrete.ArrayDisjointSet;
import datastructures.interfaces.IEdge;
import datastructures.interfaces.IList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IPriorityQueue;
import misc.Sorter;
import misc.exceptions.NoPathExistsException;
// import misc.exceptions.NotYetImplementedException;
//
// import javax.xml.crypto.dom.DOMCryptoContext;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends IEdge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated than usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've constrained Graph
    //   so that E *must* always be an instance of IEdge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the IEdge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    //
    // private IDictionary<V, ISet<V>> graph;
    private IDictionary<V, ISet<E>> graph;
    private int numVertices;
    private int numEdges;
    // private IDictionary<E, Double> edgeWeights;
    private IList<E> edgeWeights;
    private ISet<E> edgeSet; // ???
    private ISet<V> vertSet;
    //

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.graph = new ChainedHashDictionary<>();
        //this.edgeWeights = new ChainedHashDictionary<>();
        edgeWeights = new DoubleLinkedList<>();
        edgeSet = new ChainedHashSet<>();
        vertSet = new ChainedHashSet<>();
        if (vertices != null && !vertices.contains(null) && edges != null && !edges.contains(null)) {
            for (V vertex : vertices) {
                vertSet.add(vertex);
            }
            for (E edge : edges) {
                if (vertices.contains(edge.getVertex1()) && vertices.contains(edge.getVertex2())) {
                    // ISet<V> destinations1 = graph.getOrDefault(edge.getVertex1(), null);
                    ISet<E> destinations1 = graph.getOrDefault(edge.getVertex1(), null);
                    // ISet<V> destinations2 = graph.getOrDefault(edge.getVertex2(), null);
                    ISet<E> destinations2 = graph.getOrDefault(edge.getVertex2(), null);
                    if (destinations1 == null) {
                        graph.put(edge.getVertex1(), new ChainedHashSet<>());
                    }
                    if (destinations2 == null) {
                        graph.put(edge.getVertex2(), new ChainedHashSet<>());
                    }
                    // graph.get(edge.getVertex1()).add(edge.getVertex2());
                    graph.get(edge.getVertex1()).add(edge);
                    graph.get(edge.getVertex2()).add(edge);
                    if (edge.getWeight() >= 0) {
                        //edgeWeights.put(edge, edge.getWeight());
                        edgeWeights.add(edge);
                        edgeSet.add(edge);
                    } else {
                        throw new IllegalArgumentException();
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
            this.numEdges = edges.size();
            this.numVertices = vertices.size();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        if (set == null) {
            throw new IllegalArgumentException();
        }
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return numVertices;
        // throw new NotYetImplementedException();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return numEdges;
        // throw new NotYetImplementedException();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        // throw new NotYetImplementedException();
        IDisjointSet<V> disjoint = new ArrayDisjointSet<>();
        ISet<E> mst = new ChainedHashSet<>();
        for (V vertex : vertSet) {
            disjoint.makeSet(vertex);
        }
        edgeWeights = Sorter.topKSort(edgeWeights.size(), edgeWeights);
        //edgeSet = Sorter
        for (E edge : edgeWeights) {
            //if u and v are in different components
            if (disjoint.findSet(edge.getVertex1()) != disjoint.findSet(edge.getVertex2())) {
                mst.add(edge);
                // update u and v to be in the same component
                disjoint.union(edge.getVertex1(), edge.getVertex2());
            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     * @throws IllegalArgumentException if start or end is null
     */
    // public IList<E> findShortestPathBetween(V start, V end) {
    //     IList<E> result = new DoubleLinkedList<>();
    //     IDictionary<V, Double> distances = new ChainedHashDictionary<>();
    //     ISet<V> unvisited = new ChainedHashSet<>();
    //     IPriorityQueue<Vertex> minHeap = new ArrayHeap<>();
    //     IDictionary<V, E> predecessors = new ArrayDictionary<>();
    //     for (V vertex : vertSet) {
    //         if (vertex.equals(start)) {
    //             distances.put(start, 0.0);
    //         } else {
    //             distances.put(vertex, Double.POSITIVE_INFINITY);
    //         }
    //         unvisited.add(vertex);
    //     }
    //     minHeap.insert(new Vertex(start, distances.get(start)));
    //     predecessors.put(start, null);
    //     while (!minHeap.isEmpty()) {
    //         Vertex current = minHeap.removeMin();
    //         if (unvisited.contains(current.getVertex())) {
    //             V currentVertex = current.getVertex();
    //             for (E outgoingEdge : graph.get(currentVertex)) {
    //                 V nextVertex = outgoingEdge.getOtherVertex(currentVertex);
    //                 double newDis = distances.get(currentVertex) + outgoingEdge.getWeight();
    //                 if (newDis < distances.get(nextVertex)) {
    //                     minHeap.insert(new Vertex(nextVertex, newDis));
    //                     distances.put(nextVertex, newDis);
    //                     predecessors.put(nextVertex, outgoingEdge);
    //                 }
    //             }
    //             unvisited.remove(currentVertex);
    //         }
    //     }
    //     if (!predecessors.containsKey(end)) {
    //         throw new NoPathExistsException();
    //     }
    //     while (!start.equals(end)) {
    //         E edge = predecessors.get(end);
    //         result.insert(0, edge);
    //         end = edge.getOtherVertex(end);
    //     }
    //     return result;
    // }
    //
    // private class Vertex implements Comparable<Vertex> {
    //     private V vert;
    //     private double distance;
    //
    //     public Vertex(V vert, double distance) {
    //         this.vert = vert;
    //         this.distance = distance;
    //     }
    //
    //     public V getVertex() {
    //         return this.vert;
    //     }
    //
    //     public int compareTo(Vertex other) {
    //         return Double.compare(this.distance, other.distance);
    //     }
    // }
    public IList<E> findShortestPathBetween(V start, V end) {
        IList<E> result = new DoubleLinkedList<>();
        IDictionary<V, Double> distances = new ChainedHashDictionary<>();
        ISet<V> unvisited = new ChainedHashSet<>();
        IPriorityQueue<Vertex> minHeap = new ArrayHeap<>();
        IDictionary<V, E> predecessors = new ArrayDictionary<>();

        for (V vertex : vertSet) {
            if (vertex.equals(start)) {
                distances.put(start, 0.0);
            } else {
                distances.put(vertex, Double.POSITIVE_INFINITY);
            }
            unvisited.add(vertex);
        }
        minHeap.insert(new Vertex(start, distances.get(start)));
        predecessors.put(start, null);
        while (!minHeap.isEmpty()) {
            Vertex current = minHeap.removeMin();
            if (unvisited.contains(current.getVertex())) {
                V currentVertex = current.getVertex();
                for (E outgoingEdge : graph.get(currentVertex)) {
                    V nextVertex = outgoingEdge.getOtherVertex(currentVertex);
                    double newDis = distances.get(currentVertex) + outgoingEdge.getWeight();
                    if (newDis < distances.get(nextVertex)) {
                        minHeap.insert(new Vertex(nextVertex, newDis));
                        distances.put(nextVertex, newDis);
                        predecessors.put(nextVertex, outgoingEdge);
                    }
                }
                unvisited.remove(currentVertex);
                if (currentVertex.equals(end)) {
                    break;
                }
            }
        }
        if (!predecessors.containsKey(end)) {
            throw new NoPathExistsException();
        }
        while (!start.equals(end)) {
            E edge = predecessors.get(end);
            result.insert(0, edge);
            end = edge.getOtherVertex(end);
        }
        return result;
    }

    private class Vertex implements Comparable<Vertex> {
        private V vert;
        private double distance;

        public Vertex(V vert, double distance) {
            this.vert = vert;
            this.distance = distance;
        }

        public V getVertex() {
            return this.vert;
        }

        public int compareTo(Vertex other) {
            return Double.compare(this.distance, other.distance);
        }
    }
}
