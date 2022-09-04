package com.example.assignmentverse.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentverse.R
import com.example.assignmentverse.model.ContactInfo

class ContactAdapter(private val contactList: List<ContactInfo>, private val context: Context): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun onBind(item: ContactInfo, position: Int) {
            val sno = itemView.findViewById<TextView>(R.id.con_sno)
            val name = itemView.findViewById<TextView>(R.id.con_name)
            val num = itemView.findViewById<TextView>(R.id.con_number)

            sno.text = context.getString(R.string.sno, position)
            name.text =  item.name
            num.text = item.number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.contact_item,
        parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = contactList[position]
        holder.onBind(item, position)
    }

    override fun getItemCount(): Int = contactList.size
}