package gr.uom.skillabjava

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager

object MyNotificationGroup {
    val INSTANCE: NotificationGroup = NotificationGroupManager.getInstance()
        .getNotificationGroup("Skillab Notification Group")
}