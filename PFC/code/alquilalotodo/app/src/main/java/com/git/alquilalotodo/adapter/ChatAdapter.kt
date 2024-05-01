package com.git.alquilalotodo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.git.alquilalotodo.R
import com.git.alquilalotodo.objects.Message
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(val context: Context,val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

        val ITEM_RECEIVE = 1
        val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == 1) {
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.other_chat_adapter_layout,parent,false)
            return ReceiveViewHolder(view)
        }else {
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.my_chat_adapter_layout,parent,false)
            return SentViewHolder(view)
        }

    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMsg = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){

            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMsg.mensaje

        } else {

            val viewHolder = holder as ReceiveViewHolder
            holder.reciveMessage.text = currentMsg.mensaje

        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMsg = messageList[position]

        if(FirebaseAuth.getInstance().currentUser!!.uid.equals(currentMsg.userId)){
            return ITEM_SENT
        }else {
            return ITEM_RECEIVE
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sentMessage = itemView.findViewById<TextView>(R.id.myMsg)

    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val reciveMessage = itemView.findViewById<TextView>(R.id.otherMsg)


    }


}