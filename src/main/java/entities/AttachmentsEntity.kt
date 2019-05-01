package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object Attachments : IntIdTable() {
    val title = varchar("attachmentTitle", 55)
    val type = enumeration("type", AttachmentType::class)
    val link = varchar("link", 255)
}

enum class AttachmentType {
    PHOTO,
    VIDEO,
    SOUND,
}

class Attachment(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Attachment>(Attachments)

    var title by Attachments.title
    var type: AttachmentType by Attachments.type
    val link by Attachments.link
}

object ActivityAttachemts : IntIdTable() {
    val attachmentId = reference("attachmentId", Attachments)
    val activityId = reference("activityId", Activities)
}
