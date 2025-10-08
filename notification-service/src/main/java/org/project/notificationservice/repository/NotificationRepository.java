package org.project.notificationservice.repository;

import org.project.notificationservice.model.Notification;
import org.project.notificationservice.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByStatus(NotificationStatus status);

}
