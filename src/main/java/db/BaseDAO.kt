package db

interface BaseDAO<Entity : Any> {

    fun insert(data: Entity): Entity

    fun update(data: Entity): Entity

    fun delete(id: Int): Boolean

    fun getAll(): List<Entity>

    fun getData(id: Int): Entity
}