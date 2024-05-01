package com.git.alquilalotodo.adapter

import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.git.alquilalotodo.R
import com.git.alquilalotodo.objects.Contacto
import com.git.alquilalotodo.objects.Message
import com.git.alquilalotodo.ui.activities.ChatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ContactosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val profileImage : CircleImageView =itemView.findViewById(R.id.profilePic)
    private val name : TextView = itemView.findViewById(R.id.name)
    private val lastMessage : TextView = itemView.findViewById(R.id.lastMessage)
    private val unseenMessages : TextView = itemView.findViewById(R.id.unseenMessages)
    private val rootLayout : LinearLayout = itemView.findViewById(R.id.rootLayout)

    fun renderMessage(contacto : Contacto, posicion:Int ,context: AppCompatActivity){

        if (!contacto.imagenPerfil.isNullOrBlank()){
            Picasso.get().load(contacto.imagenPerfil).into(profileImage)
        }

        name.text = contacto.nombre

        //lastMessage.text = contacto.lastMsg

        /*
if (conta.unseenMessages == 0){
    unseenMessages.visibility = View.GONE
} else {
    unseenMessages.visibility = View.VISIBLE
}
 */

        rootLayout.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId",contacto.contactoUid)
            intent.putExtra("nombre",contacto.nombre)
            intent.putExtra("imagenPerfil",contacto.imagenPerfil)
            intent.putExtra("position",posicion)
            context.startActivity(intent)
        }


    }

}