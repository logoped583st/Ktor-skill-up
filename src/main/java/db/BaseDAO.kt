package db

interface BaseDAO<Entity : Any, OutputEntity:Any?> {

    fun insert(data: Entity): OutputEntity

    fun update(data: Entity): OutputEntity

    fun delete(id: Int)

    fun getAll(): List<OutputEntity>

    fun getData(id: Int): OutputEntity
}