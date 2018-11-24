import Entities.OutputTask
import Entities.Tasks
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


fun tasksToXml() = transaction {
    val outputTasks = Tasks.selectAll().map { toOutPutTask(it) }
    return@transaction outputTasks
}

data class Convertion(
        @JacksonXmlElementWrapper(useWrapping = false)
        val list: List<OutputTask>)

fun toOutPutTask(row: ResultRow): OutputTask = OutputTask(
        row[Tasks.uniqId].toString(),
        row[Tasks.id].toString(),
        row[Tasks.description],
        row[Tasks.nameTask])


fun writeXmlFile() {

// or
    // Important: create XmlMapper; it will use proper factories, workarounds
    val xmlMapper = XmlMapper()
// or
    xmlMapper.setDefaultUseWrapper(false)
    xmlMapper.writeValue(File("./qwerewq.xml"),

            tasksToXml().toTypedArray())
}

fun parseXmlTasks() {
    tryReadLast()
//    val xmlMapper = XmlMapper()
//// or
//    xmlMapper.setDefaultUseWrapper(false)
//    print(File("qwerewq.xml").readText())
//
//    print(xmlMapper.readValue(File("./qwerewq.xml"), Convertion::class.java).toString() + "12345567")

}

fun tryReadLast() {
    val inputFile = File("./qwerewq.xml")
    val dbFactory = DocumentBuilderFactory.newInstance()
    val dBuilder = dbFactory.newDocumentBuilder()
    val doc = dBuilder.parse(inputFile)
    doc.documentElement.normalize()
    System.out.println("Root element :" + doc.documentElement.nodeName)
    val nList = doc.getElementsByTagName("item")
    println("----------------------------")

    for (temp in 0 until nList.length) {
        val nNode = nList.item(temp)
        System.out.println("\nCurrent Element :" + nNode.nodeName)
        if (nNode.nodeType == Node.ELEMENT_NODE) {
            val eElement = nNode as Element
            System.out.println("id : " + eElement
                    .getElementsByTagName("id").item(0).textContent)
        }
    }
}

