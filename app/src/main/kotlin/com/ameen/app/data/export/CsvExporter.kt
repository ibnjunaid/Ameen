package com.ameen.app.data.export

import com.ameen.app.data.local.PathPointEntity
import java.io.OutputStream

object CsvExporter {
    fun export(points: List<PathPointEntity>, outputStream: OutputStream) {
        val csv = StringBuilder().apply {
            append("timestamp,latitude,longitude,isCorrected\n")
            points.forEach {
                append("${it.timestamp},${it.latitude},${it.longitude},${it.isCorrected}\n")
            }
        }.toString()

        outputStream.write(csv.toByteArray())
    }
}
