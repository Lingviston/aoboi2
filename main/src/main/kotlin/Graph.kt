
import java.util.Comparator
import java.util.PriorityQueue

data class Graph(private val vertexes: List<Vertex>) {

    val edgesCount: Int
        get() = vertexes.sumBy {
            it.edgesFactor
        }.div(2)

    fun isCoherent(): Boolean {
        val checkedVertexes = emptySet<Int>().toMutableSet()

        val firstVertex = vertexes[0]
        addAllNewConnectedVertexes(firstVertex, checkedVertexes)

        return vertexes.all {
            checkedVertexes.contains(it.name)
        }
    }

    fun getCoherentComponents(): List<CoherentComponent> {
        val vertexNames = emptySet<Int>().toMutableSet()
        vertexNames += 1..vertexes.size

        val coherentComponents = emptyList<CoherentComponent>().toMutableList()
        while (vertexNames.isNotEmpty()) {
            val firstVertexName = vertexNames.elementAt(0)
            val firstVertex = vertexes[firstVertexName - 1]

            val connectedVertexNames = emptySet<Int>().toMutableSet()
            addAllNewConnectedVertexes(firstVertex, connectedVertexNames)

            val coherentComponent = CoherentComponent(connectedVertexNames)
            coherentComponents.add(coherentComponent)

            vertexNames.removeAll(connectedVertexNames)
        }

        return coherentComponents.toList()
    }

    private fun addAllNewConnectedVertexes(vertex: Vertex, checkedVertexes: MutableSet<Int>) {
        checkedVertexes.add(vertex.name)

        for (connectedVertexName: Int in vertex.edges) {
            if (checkedVertexes.contains(connectedVertexName)) {
                continue
            } else {
                val connectedVertex = vertexes[connectedVertexName - 1]
                addAllNewConnectedVertexes(connectedVertex, checkedVertexes)
            }
        }
    }

    inner class CoherentComponent(private val vertexNames: Set<Int>) {

        fun getDistances(): MutableCollection<Int> {

            val distances = emptyMap<Int, Int>().toMutableMap()
            val marks = emptyMap<Int, Boolean>().toMutableMap()

            val priorityQueue = PriorityQueue<Vertex>(Comparator.comparingInt({ distances.getOrDefault(it.name, Int.MAX_VALUE / 2) }))
            val startingVertex = vertexes[vertexNames.elementAt(0) - 1]
            distances[startingVertex.name] = 0
            priorityQueue.add(startingVertex)
            while (priorityQueue.size > 0) {
                val viewed = priorityQueue.poll()

                marks[viewed.name] = true
                val viewedDistance = distances.getOrDefault(viewed.name, Int.MAX_VALUE / 2)
                for (vertexName in viewed.edges) {
                    if (!(marks.getOrDefault(vertexName, false)) && distances.getOrDefault(vertexName, Int.MAX_VALUE / 2) > viewedDistance + 1) {
                        distances[vertexName] = viewedDistance + 1
                        priorityQueue.add(vertexes[vertexName - 1])
                    }
                }
            }
            return distances.values
        }
    }
}