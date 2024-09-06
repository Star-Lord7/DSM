package sv.edu.udb.guia08app.datos

class Persona {
    fun key(key: String?) {
    }
    var dui: String? = null
    var nombre: String? = null
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()
    constructor() {}
    constructor(dui: String?, nombre: String?) {
        this.dui = dui
        this.nombre = nombre
    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "dui" to dui,
            "nombre" to nombre,
            "key" to key,
            "per" to per
        )
    }
}