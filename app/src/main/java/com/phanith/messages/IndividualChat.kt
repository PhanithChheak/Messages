package com.phanith.messages

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class IndividualChat : AppCompatActivity() {
    private lateinit var chatRecycleView: RecyclerView
    private lateinit var message: EditText
    private lateinit var sendButton: ImageView

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>


    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String


    private lateinit var chatRoom: String

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_chat)

        chatRecycleView = findViewById(R.id.chat_recycler_view)
        message = findViewById(R.id.messagetext)
        sendButton = findViewById(R.id.sendbutton)

        // Get intent from the main activity
        val name = intent.getStringExtra("name")
        val receiveUserUid = intent.getStringExtra("uid")
        // set the menu  bar to Reciever Name
        supportActionBar?.title = name

        dbRef = FirebaseDatabase.getInstance().getReference()

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecycleView.layoutManager = LinearLayoutManager(this)
        chatRecycleView.adapter = messageAdapter


        // Create UID
        senderRoom = receiveUserUid + currentUserUid
        receiverRoom = currentUserUid + receiveUserUid

        // get data from the Firebase database
        dbRef.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener{


                override fun onDataChange(snapshot: DataSnapshot) {
                    // clear previous value
                    messageList.clear()

                    for( postSnapshot: DataSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })






        sendButton.setOnClickListener {
            val currentmessage = message.text.toString()
            val messageObject = Message(currentmessage, currentUserUid)


            // send data to Firebase with Chatroom UID
            dbRef.child("chats").child(senderRoom).child("message").push().
            setValue(messageObject).addOnSuccessListener {
                dbRef.child("chats").child(receiverRoom).child("message").push().
                setValue(messageObject)
            }
            message.setText("")
        }


    }
}