package com.phanith.messages

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val Item_recieve = 1
    val Item_sent = 2


    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.send_text_view)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.receive_text_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            // inflate receive
            val view: View =
                LayoutInflater.from(context)
                    .inflate(R.layout.recieve_box_layout, parent, false)
            return ReceiveViewHolder(view)
        } else {
            //inflate sent
            val view: View =
                LayoutInflater.from(context)
                    .inflate(R.layout.sent_box_layout, parent, false)
            return SentViewHolder(view)

        }
    }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            // get the current message by position
            val currentMessage = messageList[position]

            if (holder.javaClass == SentViewHolder::class.java) {
                val viewHolder = holder as SentViewHolder
                holder.sentMessage.text = currentMessage.message

            } else {
                val viewHolder = holder as ReceiveViewHolder
                holder.receiveMessage.text = currentMessage.message
            }
        }

        override fun getItemViewType(position: Int): Int {
            val currentMessage = messageList[position]
            if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
                return Item_sent
            } else {
                return Item_recieve
            }
        }

        override fun getItemCount(): Int = messageList.size

    }