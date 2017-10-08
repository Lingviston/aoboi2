import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {

    private static final int GRAPHS_COUNT = 100;

    private static final int VERTEX_COUNT = 10;
    private static final int GRAPH_SCALE = 1;

    public static void main(String[] args) {
        float coherentCount = 0;
        for(int i = 0; i < GRAPHS_COUNT; ++i) {
            System.out.println("Processing graph # " + (i + 1));

            final Graph graph = GraphFactory.create(VERTEX_COUNT, GRAPH_SCALE);
            final List<Graph.CoherentComponent> coherentComponents = graph.getCoherentComponents();
            for (Graph.CoherentComponent coherentComponent : coherentComponents) {
                final Collection<Integer> distances = coherentComponent.getDistances();
                final int maxDistance = Collections.max(distances);
                System.out.println("Max distance in coherent component = " + maxDistance);
            }
            coherentCount += coherentComponents.size() == 1 ? 1 : 0;
        }

        System.out.println("Coherency probability = " + (coherentCount / GRAPHS_COUNT));
    }
}
