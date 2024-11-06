package gr.uom.skillabjava

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.intellij.icons.AllIcons
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.table.DefaultTableModel
import java.awt.BorderLayout

class ShowInfoAction : AnAction("Info", "Show information", AllIcons.General.Information) {
    override fun actionPerformed(e: AnActionEvent) {
        // Show a dialog with a table
        val dialog = InfoDialog()
        dialog.show()
    }

    class InfoDialog : DialogWrapper(true) {

        init {
            init()
            title = "Information About KU"
            setSize(450,450)
        }

        override fun createCenterPanel(): JComponent {
            val panel = JPanel(BorderLayout())

            // Create the table model with some sample data
            val columnNames = arrayOf("KU", "Name")
            val data = arrayOf(
                arrayOf<Any>("K1", "Data Types"),
                arrayOf<Any>("K2", "Operators and Decisions"),
                arrayOf<Any>("K3", "Arrays"),
                arrayOf<Any>("K4", "Loops"),
                arrayOf<Any>("K5", "Methods and Encapsulation"),
                arrayOf<Any>("K6", "Inheritance"),
                arrayOf<Any>("K7", "Advanced Class Design"),
                arrayOf<Any>("K8", "Generics and Collections"),
                arrayOf<Any>("K9", "Functional Interfaces"),
                arrayOf<Any>("K10", "Stream API"),
                arrayOf<Any>("K11", "Exceptions"),
                arrayOf<Any>("K12", "Date Time API"),
                arrayOf<Any>("K13", "IO"),
                arrayOf<Any>("K14", "NIO"),
                arrayOf<Any>("K15", "String Processing"),
                arrayOf<Any>("K16", "Concurrency"),
                arrayOf<Any>("K17", "Databases"),
                arrayOf<Any>("K18", "Localization"),
                arrayOf<Any>("K19", "Java Persistence API"),
                arrayOf<Any>("K20", "Enterprise Java Beans"),
                arrayOf<Any>("K21", "Java Message Service API"),
                arrayOf<Any>("K22", "SOAP Web Services"),
                arrayOf<Any>("K23", "Servlets"),
                arrayOf<Any>("K24", "Java REST API"),
                arrayOf<Any>("K25", "Websockets"),
                arrayOf<Any>("K26", "Java Server Faces"),
                arrayOf<Any>("K27", "Contexts and Dependency Injection"),
                arrayOf<Any>("K28", "Batch Processing"),
            )
            val tableModel = object : DefaultTableModel(data, columnNames) {
                override fun isCellEditable(row: Int, column: Int): Boolean {
                    return false
                }
            }

            val table = object : JBTable(tableModel) {
                override fun getToolTipText(event: java.awt.event.MouseEvent): String? {
                    val row = rowAtPoint(event.point)
                    val column = columnAtPoint(event.point)

                    // Only show tooltips for column 2 (index 1)
                    return if (column == 1) {
                        when (row) {
                            0 -> "<html>[C1] Declare and initialize different types of variables (e.g., primitive,\n" +
                                    "parameterized and array type), including the casting of primitive\n" +
                                    "data types</html>"
                            1 -> "<html>[C1] Use Java operators (e.g., assignment and postfix operators);\n" +
                                    "use parentheses to override operator precedence\n" +
                                    "<br>[C2] Test equality between strings and other objects using == and\n" +
                                    "equals()\n" +
                                    "<br>[C3] Create and use if, if-else, and ternary constructs\n" +
                                    "<br>[C4] Use the switch statement</html>"
                            2 -> "<html>[C1] Declare, instantiate, initialize and use a one-dimensional array\n" +
                                    "<br>[C2] Declare, instantiate, initialize and use a multi-dimensional\n" +
                                    "array</html>"
                            3 -> "<html>[C1] Create and use while loops\n" +
                                    "<br>[C2] Create and use for loops, including the enhanced for loop\n" +
                                    "<br>[C3] Create and use do-while loops\n" +
                                    "<br>[C4] Use the break statement\n" +
                                    "<br>[C5] Use the continue statement</html>"
                            4 -> "<html>[C1] Create methods with arguments and return values\n" +
                                    "<br>[C2] Apply the static keyword to methods, fields, and blocks\n" +
                                    "<br>[C3] Create an overloaded method and overloaded constructor\n" +
                                    "<br>[C4] Create a constructor chaining (use this() method to call one\n" +
                                    "constructor from another constructor)\n" +
                                    "<br>[C5] Use variable length arguments in methods\n" +
                                    "<br>[C6] Use different access modifiers (e.g., private and protected)\n" +
                                    "other than default\n" +
                                    "<br>[C7] Apply encapsulation: identify set and get methods to initialize\n" +
                                    "any private class variables\n" +
                                    "<br>[C8] Apply encapsulation: Immutable class generation-final class\n" +
                                    "and initialize private variables through the constructor</html>"
                            5 -> "<html>[C1] Use basic polymorphism (e.g., a superclass refers to a subclass)\n" +
                                    "<br>[C2] Use polymorphic parameters (e.g., pass instances of a subclass\n" +
                                    "or interface to a method)\n" +
                                    "<br>[C3] Create overridden methods\n" +
                                    "<br>[C4] Create abstract classes and abstract methods\n" +
                                    "<br>[C5] Use super() and the super keyword to access the members\n" +
                                    "(e.g., fields and methods) of a parent class\n" +
                                    "<br>[C6] Use casting in referring a subclass object to a superclass object</html>"
                            6 -> "<html>[C1] Create inner classes, including static inner classes, local\n" +
                                    "classes, nested classes, and anonymous inner classes\n" +
                                    "<br>[C2] Develop code that uses the final keyword\n" +
                                    "<br>[C3] Use enumerated types including methods and constructors in\n" +
                                    "an enum type\n" +
                                    "<br>[C4] Create singleton classes and immutable classes</html>"
                            7 -> "<html>[C1] Create and use a generic class\n" +
                                    "<br>[C2] Create and use ArrayList, TreeSet, TreeMap, and\n" +
                                    "ArrayDeque\n" +
                                    "<br>[C3] Use java.util.Comparator and\n" +
                                    "java.lang.Comparable interfaces\n" +
                                    "<br>[C4] Iterate using the forEach method of List</html>"
                            8 -> "<html>[C1] Use the built-in interfaces included in the\n" +
                                    "java.util.function packages such as Predicate,\n" +
                                    "Consumer, Function, and Supplier\n" +
                                    "<br>[C2] Develop code that uses primitive versions of functional interfaces\n" +
                                    "<br>[C3] Develop code that uses binary versions of functional interfaces\n" +
                                    "<br>[C4] Develop code that uses the UnaryOperator interface</html>"
                            9 -> "<html>[C1] Develop code to extract data from an object using the peek()\n" +
                                    "and map() methods, including primitive versions of the map()\n" +
                                    "method\n" +
                                    "<br>[C2] Search for data by using search methods of the Stream\n" +
                                    "classes, including findFirst, findAny, anyMatch, allMatch,\n" +
                                    "noneMatch\n" +
                                    "<br>[C3] Develop code that uses the Optional class\n" +
                                    "<br>[C4] Develop code that uses Stream data methods and calculation\n" +
                                    "methods\n" +
                                    "<br>[C5] Sort a collection using the Stream API\n" +
                                    "<br>[C6] Save results to a collection using the collect() method\n" +
                                    "<br>[C7] Use the flatMap() method in the Stream API</html>"
                            10 -> "<html>[C1] Create a try-catch block\n" +
                                    "<br>[C2] Use the catch, multi-catch, and finally clauses\n" +
                                    "<br>[C3] Auto-close resources with a try-with-resources statement\n" +
                                    "<br>[C4] Create custom exceptions and autocloseable resources\n" +
                                    "<br>[C5] Create and invoke a method that throws an exception\n" +
                                    "<br>[C6] Use common exception classes and categories (such\n" +
                                    " as NullPointerException, ArithmeticException,\n" +
                                    " ArrayIndexOutOfBoundsException, ClassCastException)\n" +
                                    "<br>[C7] Use assertions</html>"
                            11 -> "<html>[C1] Create and manage date-based and time-based events including\n" +
                                    "a combination of date and time into a single object using\n" +
                                    "LocalDate, LocalTime, LocalDateTime, Instant, Period,\n" +
                                    "and Duration\n" +
                                    "<br>[C2] Format date and time values when using different timezones\n" +
                                    "<br>[C3] Create and manage date-based and time-based events using\n" +
                                    "Instant, Period, Duration, and Temporal Unit\n" +
                                    "<br>[C4] Create and manipulate calendar data using\n" +
                                    "classes from java.time.*</html>"
                            12 -> "<html>[C1] Read and write data using the console\n" +
                                    "<br>[C2] Use BufferedReader, BufferedWriter, File,\n" +
                                    "FileReader, FileWriter, FileInputStream,\n" +
                                    "FileOutputStream, ObjectOutputStream,\n" +
                                    "ObjectInputStream, and PrintWriter from the java.io\n" +
                                    "package</html>"
                            13 -> "<html>[C1] Use the Path interface to operate on file and directory paths\n" +
                                    "<br>[C2] Use the Files class to check, read, delete, copy, move, and\n" +
                                    "manage metadata of a file or directory</html>"
                            14 -> "<html>[C1] Search, parse and build strings\n" +
                                    "<br>[C2] Manipulate data using the StringBuilder class and its\n" +
                                    "methods\n" +
                                    "<br>[C3] Use regular expressions using the Pattern and Matcher\n" +
                                    "classes\n" +
                                    "<br>[C4] Use string formatting</html>"
                            15 -> "<html>[C1] Create worker threads using Runnable, Callable and use\n" +
                                    "an ExecutorService to concurrently execute tasks\n" +
                                    "<br>[C2] Use the synchronized keyword and the\n" +
                                    "java.util.concurrent.atomic package to control the\n" +
                                    "order of thread execution\n" +
                                    "<br>[C3] Use java.util.concurrent collections and classes including\n" +
                                    "CyclicBarrier and CopyOnWriteArrayList\n" +
                                    "<br>[C4] Use the parallel Fork/Join Framework</html>"
                            16 -> "<html>[C1] Describe the interfaces that make up the core of the JDBC\n" +
                                    "API, including the Driver, Connection, Statement, and\n" +
                                    "ResultSet interfaces\n" +
                                    "<br>[C2] Submit queries and read results from the database, including\n" +
                                    "creating statements, returning result sets, iterating through the\n" +
                                    "results, and properly closing result sets, statements, and connections</html>"
                            17 -> "<html>[C1] Read and set the locale by using the Locale object\n" +
                                    "<br>[C2] Build a resource bundle for each locale and load a resource\n" +
                                    "bundle in an application</html>"
                            18 -> "<html>[C1] Create JPA Entities and Object-Relational Mappings (ORM)\n" +
                                    "<br>[C2] Use EntityManager to perform database operations, transactions,\n" +
                                    "and locking with JPA entities\n" +
                                    "<br>[C3] Create and execute JPQL statements</html>"
                            19 -> "<html>[C1] Create session EJB components containing synchronous and\n" +
                                    "asynchronous business methods, manage the life cycle container\n" +
                                    "callbacks, and use interceptors\n" +
                                    "<br>[C2] Create EJB timers</html>"
                            20 -> "<html>[C1] Implement Java EE message producers and consumers,\n" +
                                    "including message-driven beans\n" +
                                    "<br>[C2] Use transactions with the JMS API</html>"
                            21 -> "<html>[C1] Create SOAP Web Services and Clients using the JAX-WS API\n" +
                                    "<br>[C2] Create, marshall and unmarshall Java Objects by using the\n" +
                                    "JAXB API</html>"
                            22 -> "<html>[C1] Create Java Servlets and use HTTP methods\n" +
                                    "<br>[C2] Handle HTTP headers, parameters and cookies\n" +
                                    "<br>[C3] Manage servlet life cycle with container callback methods and\n" +
                                    "WebFilters</html>"
                            23 -> "<html>[C1] Apply REST service conventions\n" +
                                    "<br>[C2] Create REST Services and clients using the JAX-RS API</html>"
                            24 -> "<html>[C1] Create WebSocket Server and Client Endpoint Handlers\n" +
                                    "<br>[C2] Produce and consume, encode and decode WebSocket\n" +
                                    "messages</html>"
                            25 -> "<html>[C1] Use JSF syntax and JSF Tag Libraries\n" +
                                    "<br>[C2] Handle localization and produce messages\n" +
                                    "<br>[C3] Use Expression Language (EL) and interact with CDI beans</html>"
                            26 -> "<html>[C1] Create CDI Bean Qualifiers, Producers, Disposers, Interceptors,\n" +
                                    "Events, and Stereotypes</html>"
                            27 -> "<html>[C1] Implement batch jobs using the JSR 352 API</html>"
                            else -> null
                        }
                    } else {
                        null  // No tooltips for other columns
                    }
                }
            }

            // Create the JBTable and add it to a scroll pane
//            val table = JBTable(tableModel)
            val scrollPane = JBScrollPane(table)

            // Add the table to the panel
            panel.add(scrollPane, BorderLayout.CENTER)

            return panel
        }

    }
}