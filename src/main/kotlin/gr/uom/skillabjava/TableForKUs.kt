package gr.uom.skillabjava

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.swing.JTable
import javax.swing.table.DefaultTableModel


fun createResultsTable(results: List<PythonResult>): JTable {
    // Define column names: "Filename" and K1 to K27
    val columnNames = arrayOf("Filename") + (1..27).map { "K$it" }.toTypedArray()

    // Create a table model
    val model = DefaultTableModel(columnNames, 0)

    // Populate the table model with results
    results.forEach { result ->
        // Create a row with the filename and K counts
        val row = Array<Any?>(columnNames.size) { 0 } // Change to Array<Any?>
        row[0] = result.filename // Set filename in the first column

        // Set K counts in their respective columns
        result.detected_kus.forEach { (key, value) ->
            val index = key.substring(1).toInt() // Get the number from K1, K2, ..., K27
            row[index] = value // Set the count in the corresponding column
        }

        model.addRow(row)
    }

    // Create the table
    return JTable(model).apply {
        autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
    }
}


data class PythonResult(
    val filename: String,
    val sha: String,
    val detected_kus: Map<String, Int>,
    val elapsed_time: Double
)


// Function to parse the output into a list of PythonResult objects
fun parsePythonResults(output: String): List<PythonResult> {
    val gson = Gson()
    // Split the output into separate JSON objects
    val results = output.trim().split("\n").map { it.trim() }
    val type = object : TypeToken<PythonResult>() {}.type

    return results.map { gson.fromJson(it, type) }
}
