package com.chatApplication.repository;

import com.chatApplication.model.Message;
import com.chatApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

//    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestamp(User sender, User receiver1, User receiver2, User sender1, Pageable pageable);

    @Query("SELECT m FROM Message m " +
            "WHERE ( m.receiver = :receiver AND m.seen = :seen) " +
            "ORDER BY m.timestamp DESC")
    List<Message> findMessagesByReceiverOrderByTimestampDesc(@Param("receiver") User receiver,
                                                             @Param("seen") boolean seen);




    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender = :sender AND m.receiver = :receiver) " +
            "   OR (m.sender = :receiver AND m.receiver = :sender) " +
            "ORDER BY m.timestamp DESC")
    List<Message> findMessagesBySenderAndReceiverOrderByTimestampDesc(
            @Param("sender") User sender,
            @Param("receiver") User receiver);

}

