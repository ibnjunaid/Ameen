package com.ameen.app.data.export

import com.ameen.app.data.local.PathPointEntity
import com.ameen.app.data.local.SessionEntity
import java.io.OutputStream

object KmlExporter {
    fun export(session: SessionEntity, points: List<PathPointEntity>, outputStream: OutputStream) {
        val kml = StringBuilder().apply {
            append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n")
            append("<Document>\n")
            append("  <name>${session.name}</name>\n")

            // Start and End Points
            if (session.startLat != null && session.startLon != null) {
                append("  <Placemark><name>Point A</name><Point><coordinates>${session.startLon},${session.startLat}</coordinates></Point></Placemark>\n")
            }
            if (session.endLat != null && session.endLon != null) {
                append("  <Placemark><name>Point B</name><Point><coordinates>${session.endLon},${session.endLat}</coordinates></Point></Placemark>\n")
            }

            // Path
            append("  <Placemark>\n")
            append("    <name>Walked Path</name>\n")
            append("    <LineString><coordinates>\n")
            points.forEach {
                append("      ${it.longitude},${it.latitude}\n")
            }
            append("    </coordinates></LineString>\n")
            append("  </Placemark>\n")

            append("</Document>\n")
            append("</kml>")
        }.toString()

        outputStream.write(kml.toByteArray())
    }
}
