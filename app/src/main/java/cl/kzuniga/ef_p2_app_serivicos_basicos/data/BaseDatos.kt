package cl.kzuniga.ef_p2_app_serivicos_basicos.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Registros::class], version = 1)
abstract class BaseDatos: RoomDatabase() {
    abstract fun registrosDAO():RegistrosDAO
}