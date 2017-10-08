import java.util.*

object GraphFactory {

    @JvmStatic
    fun create(n: Int, m: Int): Graph {
        val vertexCount = n * m
        val distribution = Random()
        val vertexes = emptyList<Vertex>().toMutableList()

        for (vertexName: Int in 1..vertexCount) {
            val vertex = Vertex(vertexName)

            createEdge(vertex, vertexes, distribution)

            vertexes.add(vertex)
        }

        if (m >= 2) {
            val collapsedVertexes = collapse(vertexes, m)
            return Graph(collapsedVertexes)
        }

        return Graph(vertexes)
    }

    private fun createEdge(newVertex: Vertex, oldVertexes: List<Vertex>, distribution: Random) {
        val randomNumber = distribution.nextFloat()

        val loopProbability = 1.0 / (2 * newVertex.name - 1)
        if (randomNumber <= loopProbability) {
            newVertex.addEdgeTo(newVertex.name)
            newVertex.addEdgeTo(newVertex.name)
            return
        }

        var toProbability = loopProbability
        for (vertex: Vertex in oldVertexes) {
            toProbability += vertex.edgesFactor.toFloat() / (2 * newVertex.name - 1)
            if (randomNumber <= toProbability) {
                newVertex.addEdgeTo(vertex.name)
                vertex.addEdgeTo(newVertex.name)
                return
            }
        }

        throw RuntimeException("Couldn't generate edge with required parameters. Something is wrong with an algorithm.")
    }

    private fun collapse(graph: MutableList<Vertex>, m: Int): List<Vertex> {
        val newGraph = Array (graph.size / m) {
            Vertex(it + 1)
        }


        for (vertex: Vertex in graph) {
            val newVertexName = getNewVertexName(vertex.name, m)
            val newVertex = newGraph[newVertexName - 1]

            vertex.edges
                    .map { getNewVertexName(it, m) }
                    .forEach { newVertex.addEdgeTo(it) }
        }

        return newGraph.toList()
    }

    private fun getNewVertexName(currentVertexName: Int, m: Int) = currentVertexName / m + if (currentVertexName % m != 0) 1 else 0
}