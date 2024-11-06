package gr.uom.skillabjava

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.notification.NotificationType
import com.intellij.ui.JBColor
import com.intellij.ui.util.maximumHeight
import com.intellij.ui.util.minimumHeight
import com.intellij.ui.util.preferredHeight
import java.awt.Component
import java.awt.Dimension
import java.io.File
import java.util.jar.JarFile
import javax.swing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Color
import java.awt.Font

class MyToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // Define the .idea folder path and the file inside it
        val projectPath = project.basePath ?: return
        val gitFolderPath = File(projectPath, ".git")
        val ideaFolderPath = File(projectPath, ".idea")
        val generalResultsFile = File(ideaFolderPath, "skillab_general_analysis_results.txt")
//        val shaResultsFile = File(ideaFolderPath, "commit_analysis_results.txt")


        val contentPanel = JBPanel<JBPanel<*>>()
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        val allDataScrollPanel = JScrollPane()
        val allDataPanel = JPanel()
        allDataPanel.layout = BoxLayout(allDataPanel, BoxLayout.Y_AXIS)
        allDataScrollPanel.setViewportView(allDataPanel)


        // Create "General Project Analysis" button and results area
        val generalAnalysisButton = JButton("General Project Analysis")
        generalAnalysisButton.alignmentX = Component.CENTER_ALIGNMENT
        allDataPanel.add(generalAnalysisButton)

        val chartPanel = JPanel()
        chartPanel.minimumHeight = 300
        chartPanel.maximumHeight = 302
        chartPanel.preferredHeight = 300
        chartPanel.isVisible = false
        allDataPanel.add(chartPanel)

        val generalAnalysisMoreButton = JButton("More...")
        generalAnalysisMoreButton.alignmentX = Component.CENTER_ALIGNMENT
        generalAnalysisMoreButton.font = Font("Arial", Font.ITALIC, 10)
        generalAnalysisMoreButton.isVisible = false
        allDataPanel.add(generalAnalysisMoreButton)


        val scrollPanelTable = JScrollPane()
        scrollPanelTable.maximumHeight = 130
        scrollPanelTable.minimumHeight = 128
        scrollPanelTable.preferredHeight = 130
        scrollPanelTable.isVisible = false
        allDataPanel.add(scrollPanelTable)


        generalAnalysisMoreButton.addActionListener {
            if (generalAnalysisMoreButton.text.equals("More...")) {
                val savedResults = generalResultsFile.readText()
                val resultsParsed = parsePythonResults(savedResults)

                scrollPanelTable.isVisible = true
                val table = createResultsTable(resultsParsed)
                scrollPanelTable.setViewportView(table)

                generalAnalysisMoreButton.text = "Less"
            }
            else {
                scrollPanelTable.isVisible = false
                generalAnalysisMoreButton.text = "More..."
            }
        }


        // Add a separator section
        allDataPanel.add(Box.createRigidArea(Dimension(0, 10)))
        val generalSeparator = JSeparator(SwingConstants.HORIZONTAL)
        generalSeparator.maximumSize = Dimension(Int.MAX_VALUE, 10)
        allDataPanel.add(generalSeparator)



        // Load existing analysis results if available
        if (generalResultsFile.exists()) {
            val savedResults = generalResultsFile.readText()
            val resultsParsed= parsePythonResults(savedResults)

            chartPanel.isVisible = true
            val chart = createBarChart(resultsParsed)
            chartPanel.add(chart)

            generalAnalysisMoreButton.isVisible = true
        }

        // Add action listener to the general button
        generalAnalysisButton.addActionListener {
            // Show notification
            val notification = MyNotificationGroup.INSTANCE.createNotification(
                "Skillab Analysis",
                "The analysis started, it might take some time...",
                NotificationType.INFORMATION
            )
            notification.notify(null)

            SwingUtilities.invokeLater {
                val projectPath = project.basePath ?: "Unknown Project Path"

                val tempPythonDir = extractPythonDirectory("/python")
                if (tempPythonDir != null) {
                    val mainScriptProject = File(File(tempPythonDir, "2"),"main.py").absolutePath
                    val modelPath = File(tempPythonDir, "codebert").absolutePath

                    // Run Python Script
                    GlobalScope.launch {
                        val results = runPythonModel(
                            ProcessBuilder(
                                "python", mainScriptProject,
                                "--repoPath", projectPath,
                                "--codebertPath", modelPath
                            )
                        )


                        withContext(Dispatchers.Main) {
                            val resultsParsed= parsePythonResults(results)

                            chartPanel.isVisible = true
                            val chart = createBarChart(resultsParsed)
                            chartPanel.add(chart)

                            generalAnalysisMoreButton.isVisible = true

                            // Save results to file in the .idea folder
                            if (!ideaFolderPath.exists()) {
                                ideaFolderPath.mkdirs()
                            }
                            generalResultsFile.writeText(results)
                        }
                    }
                }
            }
        }



        //if git project commit analysis is possible
        if (gitFolderPath.exists()) {
            // Create "Commit Analysis" button and results area
            // Create a panel for the commit SHA input and the button
            val commitPanel = JPanel()
            commitPanel.layout = BoxLayout(commitPanel, BoxLayout.X_AXIS)

            // Create the input field for commit SHA
            val commitShaField = JTextField(20)
            commitShaField.maximumHeight = 25
            commitShaField.toolTipText = "The SHA of the commit"
            commitPanel.add(commitShaField) // Add the input field to the panel

            // Create "Commit Analysis" button
            val commitAnalysisButton = JButton("Commit Analysis")
            commitPanel.add(commitAnalysisButton) // Add the button to the same panel

            // Add the commitPanel (which contains the input field and button) to the contentPanel
            allDataPanel.add(commitPanel)

            val scrollPanelSHATable = JScrollPane()
            scrollPanelSHATable.maximumHeight = 130
            scrollPanelSHATable.minimumHeight = 128
            scrollPanelSHATable.preferredHeight = 130
            scrollPanelSHATable.isVisible = false
            allDataPanel.add(scrollPanelSHATable)

            commitAnalysisButton.addActionListener {
                val commitSha = commitShaField.text.trim() // Get the entered Git SHA
                if (commitSha.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        null, "Please enter a valid Git Commit SHA", "Error", JOptionPane.ERROR_MESSAGE
                    )
                    return@addActionListener
                }

                // Show notification
                val notification = MyNotificationGroup.INSTANCE.createNotification(
                    "Skillab Analysis",
                    "The analysis started, it might take some time...",
                    NotificationType.INFORMATION
                )
                notification.notify(null)

                SwingUtilities.invokeLater {
                    val projectPath = project.basePath ?: "Unknown Project Path"

                    val tempPythonDir = extractPythonDirectory("/python")
                    if (tempPythonDir != null) {
                        val mainScriptWithSHAPath = File(File(tempPythonDir, "1"),"main.py").absolutePath
                        val modelPath = File(tempPythonDir, "codebert").absolutePath

                        // Run Python Script
                        GlobalScope.launch {
                            val results = runPythonModel(
                                ProcessBuilder(
                                    "python", mainScriptWithSHAPath,
                                    "--repoPath", projectPath,
                                    "--sha", commitSha,
                                    "--codebertPath", modelPath
                                )
                            )

                            withContext(Dispatchers.Main) {
                                scrollPanelSHATable.isVisible = true
                                val resultsParsed= parsePythonResults(results)
                                val table= createResultsTable(resultsParsed)
                                scrollPanelSHATable.setViewportView(table)
                            }
                        }
                    }
                }
            }
        }


        contentPanel.add(allDataScrollPanel)

        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(contentPanel, "", false)
        contentManager.addContent(content)

        toolWindow.setTitleActions(listOf(ShowInfoAction()))
    }

    suspend fun runPythonModel(processBuilder: ProcessBuilder): String {
        return try {
            withContext(Dispatchers.IO) {
                val process = processBuilder.start()

                // Capture the output of the Python script
                val output = process.inputStream.bufferedReader().readText()
                process.waitFor()
                println(output)
                output
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Failed to run analysis: ${e.message}"
        }
    }


    private fun extractPythonDirectory(resourceDirectory: String): File? {
        return try {
            // Define a base directory for temporary files
            val baseTempDir = File(System.getProperty("java.io.tmpdir"))
            val tempDirName = "skillab_python_intelij_temp_v01"
            val tempDir = File(baseTempDir, tempDirName)

            // Check if the directory already exists
            if (tempDir.exists()) {
                return tempDir
            }

            // Create a new temporary directory if it doesn't exist
            tempDir.mkdirs()

            // Get the resource URL and ensure it is in the JAR file format
            val resourceUrl = javaClass.getResource(resourceDirectory) ?: return null
            val resourceUri = resourceUrl.toURI()
            val resourcePath = resourceUri.toString()

            if (resourcePath.startsWith("jar:")) {
                // Extract files from the JAR
                val jarPath = resourcePath.substringAfter("jar:file:").substringBefore("!")
                val jarFile = JarFile(File(jarPath))

                // Iterate through all entries in the JAR file
                val entries = jarFile.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    // Check if the entry is part of the specified directory
                    if (entry.name.startsWith(resourceDirectory.removePrefix("/"))) {
                        // Create a corresponding file in the temporary directory
                        val tempFile = File(tempDir, entry.name.removePrefix(resourceDirectory.removePrefix("/")))

                        // If the entry is a directory, create the directory
                        if (entry.isDirectory) {
                            tempFile.mkdirs()
                        } else {
                            // If the entry is a file, copy its content to the temp file
                            jarFile.getInputStream(entry).use { inputStream ->
                                tempFile.outputStream().use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                        }
                    }
                }
                jarFile.close()
            } else {
                return null
            }
            tempDir
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }





}
