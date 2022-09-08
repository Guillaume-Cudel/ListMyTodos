package ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.listmytodos.R
import model.Todo
import ui.Communicator

class TodoListAdapter(private val communicator: Communicator): RecyclerView.Adapter<TodoListViewHolder>() {

    var todoList = listOf<Todo>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        return TodoListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        val item = todoList[position]


        holder.checkBox.setOnClickListener {
            item.state = holder.checkBox.isChecked
            setState(item.state, holder.checkBox, holder.title)
            communicator.updateList(todoList)
        }
        setState(item.state, holder.checkBox, holder.title)
        holder.title.text = item.title

        changeUrgentColor(item.urgent, holder.urgentColor)

    }

    override fun getItemCount() = todoList.size

    fun setState(state: Boolean, box: CheckBox, text: TextView){
        if(state){
            box.isChecked = true
            text.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            box.isChecked = false
            text.paintFlags = 0
        }
    }

    fun changeUrgentColor(urgent: Int, image: ImageButton){
        when(urgent){
            1 -> image.setBackgroundResource(R.drawable.round_green)
            2 -> image.setBackgroundResource(R.drawable.round_orange)
            3 -> image.setBackgroundResource(R.drawable.round_red)
        }
    }


}

class TodoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val checkBox: CheckBox = itemView.findViewById(R.id.todo_checkbox)
    val title: TextView = itemView.findViewById(R.id.todo_title)
    val urgentColor: ImageButton = itemView.findViewById(R.id.todo_urgent_state)

    companion object{
        fun create(parent: ViewGroup): TodoListViewHolder{
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_item, parent, false)
            return TodoListViewHolder(view)
        }
    }
}