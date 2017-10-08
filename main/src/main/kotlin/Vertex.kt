data class Vertex(val name: Int) {

    private val edgesTo: MutableList<Int> = emptyList<Int>().toMutableList()

    val edges: List<Int>
        get() = edgesTo.toList()

    val edgesFactor: Int
        get() = edgesTo.size

    fun addEdgeTo(vertexName: Int) {
        edgesTo.add(vertexName)
    }
}