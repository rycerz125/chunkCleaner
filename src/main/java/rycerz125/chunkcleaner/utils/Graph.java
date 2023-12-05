package rycerz125.chunkcleaner.utils;

import java.util.ArrayList;
public class Graph {
    // A user define class to represent a graph.
    // A graph is an array of adjacency lists.
    // Size of array will be V (number of vertices
    // in graph)
    int V;
    ArrayList<ArrayList<Integer> > adjListArray;
    ArrayList<ArrayList<Integer> > finalList;

    // constructor
    public Graph(int V)
    {
        this.V = V;
        // define the size of array as
        // number of vertices
        adjListArray = new ArrayList<>();
        finalList = new ArrayList<>();
        // Create a new list for each vertex
        // such that adjacent nodes can be stored

        for (int i = 0; i < V; i++) {
            adjListArray.add(i, new ArrayList<>());
        }
    }

    // Adds an edge to an undirected graph
    public void addEdge(int src, int dest)
    {
        // Add an edge from src to dest.
        adjListArray.get(src).add(dest);

        // Since graph is undirected, add an edge from dest
        // to src also
        adjListArray.get(dest).add(src);
    }

    void DFSUtil(int v, boolean[] visited)
    {
        // Mark the current node as visited and print it
        visited[v] = true;
        finalList.get(finalList.size()-1).add(v);
        //System.out.print(v + " ");
        // Recur for all the vertices
        // adjacent to this vertex
        for (int x : adjListArray.get(v)) {
            if (!visited[x])
                DFSUtil(x, visited);
        }
    }
    public void connectedComponents()
    {
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[V];
        for (int v = 0; v < V; ++v) {
            if (!visited[v]) {
                // print all reachable vertices
                // from v
                finalList.add(new ArrayList<>());
                DFSUtil(v, visited);
                //System.out.println();

            }
        }
    }

    public ArrayList<ArrayList<Integer>> getFinalList() {
        connectedComponents();
        return finalList;
    }

    // Driver code
    public static void main(String[] args)
    {
        // Create a graph given in the above diagram
        Graph g = new Graph(20);

        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(7, 1);
        g.addEdge(7, 2);
        g.addEdge(8, 2);
        g.addEdge(9, 0);
        g.addEdge(10, 2);
        g.addEdge(11, 2);
        g.addEdge(12, 2);



        g.addEdge(3, 4);
        System.out.println(
                "Following are connected components");

        g.connectedComponents();
        for(ArrayList<Integer> list : g.finalList){
            for(Integer i : list){
                System.out.print(i+" - ");
            }
            System.out.println();
        }
    }
}