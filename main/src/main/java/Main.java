import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

public class Main {
    private static final String GRAPH_PARAMETERS_PATTERN = "Built graph #%d. Coherent = %s. Diameter = %d";
    private static final String AVERAGE_VALUES_PATTERN = "Built %d graphs with n = %d, m = %d. Average distance between " +
            "vertexes in a single coherency component is %f. Coherency probability is %.2f.";

    private static final int GRAPHS_COUNT = 1000;

    private static final int VERTEX_COUNT = 4000;
    private static final int GRAPH_SCALE = 10;

    public static void main(String[] args) {
        final Map<Integer, Float> degreesMap = new TreeMap<>();

        float coherentCount = 0;
        float distancesSum = 0;
        int distancesCount = 0;
        for(int i = 0; i < GRAPHS_COUNT; ++i) {
            final Graph graph = GraphFactory.create(VERTEX_COUNT, GRAPH_SCALE);
            final List<Graph.CoherentComponent> coherentComponents = graph.getCoherentComponents();

            final boolean isCoherent = coherentComponents.size() == 1;
            coherentCount += isCoherent ? 1 : 0;

            for (Vertex vertex : graph.getVertexes()) {
                degreesMap.compute(vertex.getEdgesFactor(), (key, oldValue) -> oldValue == null ? 1 : ++oldValue);
            }

            int diameter = 0;
            for (Graph.CoherentComponent coherentComponent : coherentComponents) {
                final List<Integer> distances = coherentComponent.getAllDistances();
                distancesSum += distances.stream().mapToInt(Integer::intValue).sum();
                distancesCount += distances.size();
                diameter = Collections.max(distances);
            }

            if (!isCoherent) {
                diameter = graph.getVertexCount();
            }

            final String graphParametersOutput = String.format(Locale.ENGLISH,
                    GRAPH_PARAMETERS_PATTERN,
                    i + 1, String.valueOf(isCoherent), diameter);
            System.out.println(graphParametersOutput);
        }

        final float coherencyProbability = coherentCount / GRAPHS_COUNT;
        final float averageDistanceInCoherentComponent = distancesSum / distancesCount;
        final String averageValuesOutput = String.format(Locale.ENGLISH, AVERAGE_VALUES_PATTERN, GRAPHS_COUNT,
                VERTEX_COUNT, GRAPH_SCALE, averageDistanceInCoherentComponent, coherencyProbability);
        System.out.println(averageValuesOutput);

        try (final FileWriter fileWriter = new FileWriter("vertex_factor_probabilities.csv")) {
            final int totalVertexCount = VERTEX_COUNT * GRAPHS_COUNT;
            for (Map.Entry<Integer, Float> entry : degreesMap.entrySet()) {
                final float probability = entry.getValue() / totalVertexCount;
                fileWriter.write(entry.getKey() + "," + probability + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
