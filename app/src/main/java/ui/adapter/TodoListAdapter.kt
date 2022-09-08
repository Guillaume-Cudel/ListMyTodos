package ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.listmytodos.R
import model.Todo

class TodoListAdapter: RecyclerView.Adapter<TodoListViewHolder>() {

    var todoList = listOf<Todo>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        return TodoListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        val item = todoList[position]

        val state = item.state
        if(state) {
            holder.checkBox.isChecked = true
            //todo when a todo is done, it's placed at the bottom of the list et should be crossed out
        } else {holder.checkBox.isChecked = false}

        holder.title.text = item.title

        changeUrgentColor(item.urgent, holder.urgentColor)

    }

    override fun getItemCount() = todoList.size


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