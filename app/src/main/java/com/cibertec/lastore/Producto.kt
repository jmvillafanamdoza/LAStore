package com.cibertec.lastore

data class Producto(
    var idProducto: Int,
    var imagen: String,
    var nombre: String,
    var precio: Double,
    var stock: Int,
    var categoria: String
)