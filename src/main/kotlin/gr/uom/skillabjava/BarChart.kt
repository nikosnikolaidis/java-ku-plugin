package gr.uom.skillabjava

import com.intellij.ui.util.maximumHeight
import com.intellij.ui.util.minimumHeight
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.data.category.DefaultCategoryDataset
import java.awt.Dimension
import javax.swing.JPanel

fun createBarChart(results: List<PythonResult>): JPanel {
    // Create dataset
    val dataset = DefaultCategoryDataset()

    // Sum K values across all results
    val kSums = IntArray(27) { 0 } // to hold sums for K1 to K27

    results.forEach { result ->
        result.detected_kus.forEach { (key, value) ->
            val index = key.substring(1).toInt() - 1 // Convert K1..K27 to 0..26
            kSums[index] += value
        }
    }

    // Add the sums to the dataset
    for (i in kSums.indices) {
        dataset.addValue(kSums[i], "K Counts", "K${i + 1}")
    }

    // Create the chart
    val chart: JFreeChart = ChartFactory.createBarChart(
        "Sum of K Counts",
        "K Values",
        "Count",
        dataset
    )

    // Create a ChartPanel to hold the chart
    return ChartPanel(chart).apply {
//        maximumHeight = 300
        preferredSize = Dimension(400, 300)
    }
}