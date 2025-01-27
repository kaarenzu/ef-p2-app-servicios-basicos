package cl.kzuniga.ef_p2_app_serivicos_basicos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RegistrosDAO {

    @Insert
    suspend fun insertar(registros: Registros)

    @Update
    suspend fun actualizar(registros: Registros)

    @Delete
    suspend fun eliminar(registros: Registros)

    @Query("SELECT * FROM RegistrosCuentasBasicas ORDER BY fecha DESC")
    suspend fun obtenerRegistros(): List<Registros>

}