public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Graph graph = GraphFactory.create(4000, 10);
        System.out.println("Generated graph with " + graph.getVertexes().size() + " vertexes and " + graph.getEdges().size() + " edges.");
    }
}
