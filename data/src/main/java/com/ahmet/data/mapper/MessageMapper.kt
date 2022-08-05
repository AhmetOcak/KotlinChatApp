package com.ahmet.data.mapper

import com.ahmet.data.model.MessageEntity
import com.ahmet.domain.interfaces.IEntityMapper
import com.ahmet.domain.model.Message
import javax.inject.Inject

class MessageMapper @Inject constructor() : IEntityMapper<MessageEntity, Message> {
    override fun mapFromEntity(entity: MessageEntity): Message {
        return Message(
            entity.userMessage,
            entity.friendMessage
        )
    }

    override fun entityFromModel(model: Message): MessageEntity {
        return MessageEntity(
            model.userMessage,
            model.friendMessage
        )
    }
}