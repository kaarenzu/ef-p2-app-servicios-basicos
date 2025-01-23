package cl.kzuniga.ef_p2_app_serivicos_basicos.modelo

import java.io.Serializable

data class RegistroClass(
    val registro:String,
    val fecha:String,
    val tipoRegistro:String

) : Serializable