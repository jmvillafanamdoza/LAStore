package com.cibertec.lastore

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class ListadoProductos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_productos)

        val btnAtras : Button = findViewById(R.id.btnAtras)

        val btnAgregarProducto : Button = findViewById(R.id.btnAgregarProducto)

        btnAtras.setOnClickListener {
            val MenuPrincipalWindow = Intent(this, MenuPrincipal::class.java)
            startActivity(MenuPrincipalWindow)
            finish()
        }


        btnAgregarProducto.setOnClickListener {
            showDialogCrear()
        }

        val recycleViewProductos : RecyclerView = findViewById(R.id.lstProductos)
        recycleViewProductos.layoutManager = LinearLayoutManager(this)

        // RecyclerView con 2 columnas
        val layoutManager = GridLayoutManager(this, 2)
        recycleViewProductos.layoutManager = layoutManager

        val data = ArrayList<Producto>();

        val adapter = ProductoAdapter(data)
        recycleViewProductos.adapter = adapter

        val ipServicio = getString(R.string.ipServicio)
        Fuel.get("https://${ipServicio}/GetProduct")
            .header("Content-Type", "application/json;charset=utf-8")
            .responseJson { _, _, result ->
                result.fold(
                    success = { json ->
                        try {
                            val gson = Gson()
                            val tipoListaProductos = object : TypeToken<List<Producto>>() {}.type
                            val listaProductos: List<Producto> = gson.fromJson(json.content, tipoListaProductos)
                            listaProductos?.let {
                                data.clear()
                                data.addAll(it)
                                adapter.notifyDataSetChanged()
                            }
                        } catch (e: Exception) {
                            showDialog("Error de JSON parse")
                        }
                    },
                    failure = { error ->
                        showDialog("Error de conexion")
                    }
                )
            }

    }
    private fun showDialog(mensaje: String){
        val dialogConfirm = Dialog(this)
        dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogConfirm.setCancelable(false)
        dialogConfirm.setContentView(R.layout.dialog_mensaje)

        dialogConfirm.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val modalMessageTextView: TextView = dialogConfirm.findViewById(R.id.textViewMensaje)
        modalMessageTextView.text = mensaje

        val btnDialogAceptar : Button = dialogConfirm.findViewById(R.id.btnDialogAceptar)

        btnDialogAceptar.setOnClickListener {
            dialogConfirm.dismiss()
        }

        dialogConfirm.show()
    }
    fun showDialogCrear(){
        val dialogConfirm = Dialog(this)
        dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogConfirm.setCancelable(false)
        dialogConfirm.setContentView(R.layout.card_create_producto)

        dialogConfirm.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnDialogCrear: Button = dialogConfirm.findViewById(R.id.btnDialogCrear)
        btnDialogCrear.setOnClickListener {
            //Declaracion de textField
            val textInputNombre: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldNombre)
            val textInputLinkImg: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldLinkImg)
            val textInputPrecio: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldPrecio)
            val textInputStock: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldStock)
            val textInputCategoria: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldCategoria)
            //Obtener datos de textField
            val nombre = textInputNombre.text.toString()
            val linkImg = textInputLinkImg.text.toString()
            val precioString = textInputPrecio.text.toString()
            val stockString = textInputStock.text.toString()
            val categoria = textInputCategoria.text.toString()
            // Verificar si algun campo esta en blanco
            if (nombre.isNotEmpty() && linkImg.isNotEmpty() && precioString.isNotEmpty() && stockString.isNotEmpty() && categoria.isNotEmpty()) {
                // Verificar si precio es un numero decimal
                val precio: Double? = precioString.toDoubleOrNull()
                if (precio != null) {
                    // Verificar si stock es un numero entero
                    val stock: Int? = stockString.toIntOrNull()
                    if (stock != null) {
                        //Convertir a json
                        val jsonProducto = JSONObject()
                        jsonProducto.put("nombre", nombre)
                        jsonProducto.put("imagen", linkImg)
                        jsonProducto.put("precio", precio)
                        jsonProducto.put("stock", stock)
                        jsonProducto.put("categoria", categoria)
                        val ipServicio = getString(R.string.ipServicio)
                        Fuel.post("https://${ipServicio}/CreateProduct")
                            .header("Content-Type", "application/json;charset=utf-8")
                            .body(jsonProducto.toString())
                            .response { _, _, result ->
                                result.fold(
                                    success = {
                                        showDialog("Producto creado")
                                        val intent = Intent(this, ListadoProductos::class.java)
                                        startActivity(intent)
                                    },
                                    failure = { error ->
                                        showDialog("Error al crear")
                                    }
                                )
                            }
                        dialogConfirm.dismiss()
                    }else{
                        showDialog("Por favor, ingrese un numero entero para stock")
                    }
                } else {
                    showDialog("Por favor, ingresar un numero para precio")
                }
            }else{
                showDialog("Por favor, complete todos los campos")
            }

        }
        val btnDialogVolver: Button = dialogConfirm.findViewById(R.id.btnDialogVolver)
        btnDialogVolver.setOnClickListener {
            dialogConfirm.dismiss()
        }

        dialogConfirm.show()
    }
}