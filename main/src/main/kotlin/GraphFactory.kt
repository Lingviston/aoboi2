import java.util.*

object GraphFactory {

    @JvmStatic
    fun create(n: Int, m: Int): Graph {
        if (m < 2) {
            throw IllegalArgumentException("Parameter 'm' must be >= 2.")
        }

        val distribution = Random()
        val vertexes = emptyList<Vertex>().toMutableList()
        val edges = emptyList<Edge>().toMutableList()

        val vertexCount = n * m
        for (vertexName: Int in 1..vertexCount) {
            val vertex = Vertex(vertexName)
            val edge = createEdge(vertex, vertexes, distribution)

            vertexes.add(vertex)
            edges.add(edge)
        }

        return Graph(vertexes, edges)
    }

    private fun createEdge(newVertex: Vertex, oldVertexes: List<Vertex>, distribution: Random): Edge {
        val randomNumber = distribution.nextFloat()

        val loopProbability = 1.0 / (2 * newVertex.name - 1)
        if (randomNumber <= loopProbability) {
            val edge = Edge(newVertex, newVertex)
            newVertex.edgesFactor += 2
            return edge
        }

        var toProbability = loopProbability
        for (vertex: Vertex in oldVertexes) {
            toProbability += vertex.edgesFactor.toFloat() / (2 * newVertex.name - 1)
            if (randomNumber <= toProbability) {
                val edge = Edge(newVertex, vertex)

                vertex.edgesFactor++
                newVertex.edgesFactor++

                return edge
            }
        }

        throw RuntimeException("Couldn't generate edge with required parameters. Something is wrong with an algorithm.")
    }
}