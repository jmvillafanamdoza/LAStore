package com.cibertec.lastore

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject

class ProductoAdapter(private val mlist:List<Producto>) : RecyclerView.Adapter<ProductoAdapter.ViewHolder
        >(){
    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        val txtNombre : TextView = ItemView.findViewById(R.id.textViewNombre)
        val txtPrecio : TextView = ItemView.findViewById(R.id.textViewPrecio)
        val imagen : ImageView = ItemView.findViewById(R.id.imageViewProducto)
        val btnEditar: Button = ItemView.findViewById(R.id.buttonEditar)

        init {
            btnEditar.setOnClickListener {
                showDialogEditar(itemView.context, adapterPosition);
            }
        }

        private fun showDialogEditar(context: Context, position: Int) {
            val ipServicio = context.getString(R.string.ipServicio)
            Fuel.get("https://${ipServicio}/getProductByOrder/${position}")
                .header("Content-Type", "application/json;charset=utf-8")
                .responseJson { _, _, result ->
                    result.fold(
                        success = { json ->
                            val dialogConfirm = Dialog(context)
                            dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialogConfirm.setCancelable(false)
                            dialogConfirm.setContentView(R.layout.card_edit_producto)

                            dialogConfirm.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            //Obtener valores
                            val success = json.content
                            val producto: Producto = Gson().fromJson(success, Producto::class.java)
                            //Input idProd Defecto
                            val textViewNroidProd: TextView = dialogConfirm.findViewById(R.id.textViewNroidProd)
                            textViewNroidProd.text = producto.idProducto.toString()
                            Log.e("RESPOOOONSE",json.content)
                            //Input Nombre Defecto
                            val textInputNombre: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldNombre)
                            textInputNombre.setText(producto.nombre)
                            //Input LinkImg Defecto
                            val textInputLinkImg: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldLinkImg)
                            textInputLinkImg.setText(producto.imagen)
                            //Input Precio Defecto
                            val textInputPrecio: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldPrecio)
                            textInputPrecio.setText(producto.precio.toString())
                            //Input Stock Defecto
                            val textInputStock: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldStock)
                            textInputStock.setText(producto.stock.toString())
                            //Input Categoria Defecto
                            val textInputCategoria: TextInputEditText = dialogConfirm.findViewById(R.id.txtFieldCategoria)
                            textInputCategoria.setText(producto.categoria)

                            val btnDialogGuardar: Button = dialogConfirm.findViewById(R.id.btnDialogGuardar)
                            val btnDialogBorrar: Button = dialogConfirm.findViewById(R.id.btnDialogBorrar)


                            btnDialogGuardar.setOnClickListener {
                                //Obtener datos de textField
                                val idProducto = producto.idProducto.toString()
                                val nombre = textInputNombre.text.toString()
                                val linkImg = textInputLinkImg.text.toString()
                                val precioString = textInputPrecio.text.toString()
                                val stockString = textInputStock.text.toString()
                                val categoria = textInputCategoria.text.toString()
                                // Verificar si algun campo esta en blanco
                                if (idProducto.isNotEmpty() && nombre.isNotEmpty() && linkImg.isNotEmpty() && precioString.isNotBlank() && stockString.isNotEmpty() && categoria.isNotEmpty()) {
                                    val precio: Double? = precioString.toDoubleOrNull()
                                    if (precio != null) {
                                        // Verificar si stock es un numero entero
                                        val stock: Int? = stockString.toIntOrNull()
                                        if (stock != null) {
                                            //Convertir a json
                                            val jsonProducto = JSONObject()
                                            jsonProducto.put("idProducto", idProducto)
                                            jsonProducto.put("nombre", nombre)
                                            jsonProducto.put("imagen", linkImg)
                                            jsonProducto.put("precio", precio)
                                            jsonProducto.put("stock", stock)
                                            jsonProducto.put("categoria", categoria)
                                            val ipServicio = context.getString(R.string.ipServicio)
                                            Fuel.put("https://${ipServicio}/UpdateProduct/${producto.idProducto}")
                                                .header("Content-Type", "application/json;charset=utf-8")
                                                .body(jsonProducto.toString())
                                                .response { _, _, result ->
                                                    result.fold(
                                                        success = {
                                                            showDialog("Producto actualizado", context)
                                                            val intent = Intent(itemView.context, ListadoProductos::class.java)
                                                            itemView.context.startActivity(intent)
                                                        },
                                                        failure = { error ->
                                                            showDialog("Error al actualizar", context)
                                                        }
                                                    )
                                                }
                                            dialogConfirm.dismiss()
                                        }else{
                                            showDialog("Por favor, ingrese un numero entero para stock",context)
                                        }
                                    }else{
                                        showDialog("Por favor, ingresar un numero para precio",context)
                                    }
                                }else{
                                    showDialog("Por favor, complete todos los campos",context)
                                }
                            }
                            btnDialogBorrar.setOnClickListener {
                                val ipServicio = context.getString(R.string.ipServicio)

                                Fuel.delete("https://${ipServicio}/DeleteProduct?id=${producto.idProducto}")
                                    .header("Content-Type", "application/json;charset=utf-8")
                                    .response { _, _, result ->
                                        result.fold(
                                            success = {
                                                showDialog("Producto eliminado", context)
                                                val intent = Intent(itemView.context, ListadoProductos::class.java)
                                                itemView.context.startActivity(intent)
                                            },
                                            failure = { error ->
                                                showDialog("Error al eliminar", context)
                                            }
                                        )
                                    }
                                dialogConfirm.dismiss()
                            }

                            dialogConfirm.show()
                        },
                        failure = { error ->
                            showDialog("Error al buscar producto",itemView.context)

                        }
                    )
                }
        }
        private fun showDialog(mensaje: String,context: Context){
            val dialogConfirm = Dialog(context)
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_producto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    override fun onBindViewHolder(holder: ProductoAdapter.ViewHolder, position: Int) {
        val Producto = mlist[position]
        holder.txtNombre.text = Producto.nombre
        holder.txtPrecio.text = Producto.precio.toString()
        val imageUrl = Producto.imagen
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.imagen)
    }

}