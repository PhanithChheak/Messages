package com.phanith.messages

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter require list of data to bind to the view holder
class UserAdapter(private val exampleList: List<User>,
                  private val listener: MainActivity
): RecyclerView.Adapter<UserAdapter.ExampleViewHolder>() {


    // the function will call to create and object for the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        //xml layout(itemView) are blueprint, to use the layout we have to create view object
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_cardview,parent,false)
        return ExampleViewHolder(itemView)
    }

    // function will be call many time as we scroll, due to binding the data to the view holder
    //holder are the recycle data that need to put into view and position is to identify each data
    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]

        currentItem.image?.let { holder.imageView.setImageResource(it) }
        holder.textView1.text = currentItem.name
        holder.textView2.text = currentItem.email


    }
    // this function need to know how many data are in the list
    override fun getItemCount() = exampleList.size

    inner class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnClickListener{
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textView1: TextView = itemView.findViewById(R.id.test_view_1)
        val textView2: TextView = itemView.findViewById(R.id.test_view_2)


        init {
            itemView.setOnClickListener(this)
        }

        // we need to override the on click listener since the it is not the responsibility
        // of the adapter to know which function to run
        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.OnItemClick(position)
            }
        }

    }
    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }


}