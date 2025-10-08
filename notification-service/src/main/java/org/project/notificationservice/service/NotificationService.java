package org.project.notificationservice.service;

import org.project.notificationservice.event.OrderPaidEvent;
import org.project.notificationservice.event.UserRegisteredEvent;

public interface NotificationService {

    void sendUserRegisteredNotification(UserRegisteredEvent event);

    void sendOrderPaidNotification(OrderPaidEvent event);
}
