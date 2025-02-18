package com.icia.fproject.chat.dao;


import com.icia.fproject.chat.model.MessageEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findBySenderNameOrReceiverNameOrderByDateAsc(String senderName, String receiverName);
    List<MessageEntity> findBySenderNameAndReceiverNameOrReceiverNameAndSenderNameOrderByDateAsc(String senderName, String receiverName, String receiverName1, String senderName1);
}





