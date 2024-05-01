package com.git.alquilalotodo.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.git.alquilalotodo.adapter.ChatAdapter
import com.git.alquilalotodo.databinding.ActivityChatBinding
import com.git.alquilalotodo.objects.Contacto
import com.git.alquilalotodo.objects.ContactoAgregado
import com.git.alquilalotodo.objects.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var dbRealTime : DatabaseReference
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var userMobile :String
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageList : ArrayList<Message>
    private lateinit var chatAdapter:ChatAdapter

    private lateinit var receiverUid : String
    private var posicion : Int = 0

    var receiverRoom:String? =null
    var senderRoom:String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRealTime = FirebaseDatabase.getInstance().getReference()
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val nombre = intent.getStringExtra("nombre").toString()
        receiverUid = intent.getStringExtra("userId").toString()
        posicion = intent.getIntExtra("position",0)


        val senderUid = FirebaseAuth.getInstance().currentUser!!.uid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        val profilePic = binding.profilePic
        val nameTV = binding.chatUsername

        val messageEditText = binding.messageEditText
        recyclerView = binding.chatRecycleView
        messageList = ArrayList()
        chatAdapter = ChatAdapter(this,messageList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter

        recyclerView.post {
            recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        }


        //Añadir los datos para el recycle view
        dbRealTime.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapShot in snapshot.children){
                        val message = postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }

                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {


                }

            })

        //Añadir el mensaje enviado a la BD realtime
        binding.sendBtn.setOnClickListener {

            val getTextMessage = messageEditText.text.toString()

            if (!getTextMessage.isNullOrBlank()) {
                val messageObject = Message(getTextMessage, senderUid)

                dbRealTime.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        dbRealTime.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }

                messageEditText.setText("")
            }
        }


        //Obtener datos del MessagesAdapter
        val imagePerfil = intent.getStringExtra("imagenPerfil").toString()
        val getMobile = intent.getStringExtra("mobile").toString()

        //Obtener el mobil del usuario
        userMobile = ""

        nameTV.setText(nombre)
        if (!imagePerfil.isNullOrBlank()){
            Picasso.get().load(imagePerfil).into(profilePic)
        }


        binding.backBtn.setOnClickListener {
            finish()
        /*
            if (messageList.isNotEmpty()){
                saveLastMsg(receiverUid,posicion)
            }else{
                finish()
            }
             */
        }
    }

    /*
    override fun onBackPressed() {
        super.onBackPressed()
        if (messageList.isNotEmpty()){
            saveLastMsg(receiverUid,posicion)
        }else{
            finish()
        }
    }
     */

    private fun saveLastMsg(receiverUid : String,position:Int){
        val lastMsg = messageList.last().mensaje.toString()

        val myDoc = db.collection("usuarios").document(auth.currentUser!!.uid)
        myDoc.get().addOnSuccessListener {

            val contactosAgregadosMap = it.get("listaChatUsuarios") as ArrayList<ContactoAgregado>
            contactosAgregadosMap[position] = ContactoAgregado(receiverUid,lastMsg)
            val contactosAgregadosMapUpdated = hashMapOf<String,Any>(
                "listaChatUsuarios" to contactosAgregadosMap
            )

            myDoc.update(contactosAgregadosMapUpdated).addOnSuccessListener {
                finish()
                val otherDoc = db.collection("usuarios").document(receiverUid)
                otherDoc.get().addOnSuccessListener {

                    val otherContactosAgregadosMap = it.get("listaChatUsuarios") as ArrayList<ContactoAgregado>

                    for (i in 0 until otherContactosAgregadosMap.size) {

                        //Recorrer la lista de contactos para obtener el uid
                        val contactoMap = otherContactosAgregadosMap[i] as HashMap<String, Any>
                        val contactoUid = contactoMap["contactoUid"] as String

                        if (contactoUid.equals(auth.currentUser!!.uid)) {


                            otherContactosAgregadosMap[i] = ContactoAgregado(auth.currentUser!!.uid,lastMsg)
                            val otherContactosAgregadosMapUpdated = hashMapOf<String,Any>(
                                "listaChatUsuarios" to otherContactosAgregadosMap
                            )

                            otherDoc.update(otherContactosAgregadosMapUpdated).addOnSuccessListener {

                            }

                        }
                    }

                    }
                }
            }
        }
    }