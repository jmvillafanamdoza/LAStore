package com.cibertec.lastore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdapter(private val mlist:List<Usuario>) : RecyclerView.Adapter<UsuarioAdapter.ViewHolder
        >(){
    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        val txtUsuario : TextView = ItemView.findViewById(R.id.textViewUsuario)
        val txtNombre : TextView = ItemView.findViewById(R.id.textViewNombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_usuario, parent, false)
        return UsuarioAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Usuario = mlist[position]
        holder.txtUsuario.text = "Usuario${position+1}:"
        holder.txtNombre.text = Usuario.userData
    }
}