package com.ahmet.domain.interfaces

interface IEntityMapper<Entity, Model> {

    fun mapFromEntity(entity: Entity) : Model

    fun entityFromModel(model: Model) : Entity
}