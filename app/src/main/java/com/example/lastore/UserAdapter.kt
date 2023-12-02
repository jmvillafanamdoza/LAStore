package com.example.lastore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class UserAdapter(private val userList : ArrayList<UserData>): RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item,parent, false)
        return  MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = userList[position]

        holder.location.text = currentitem.location ?:"Default Location"
        holder.name.text = currentitem.name ?: "Default Name"
        holder.operator.text = currentitem.operator ?: "Default Operator"
        holder.phone.text = currentitem.phone ?: "Default Phone"
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val location : TextView = itemView.findViewById(R.id.tvlocation)
        val name : TextView = itemView.findViewById(R.id.tvname)
        val operator : TextView = itemView.findViewById(R.id.tvoperator)
        val phone : TextView = itemView.findViewById(R.id.tvphone)


    }
}