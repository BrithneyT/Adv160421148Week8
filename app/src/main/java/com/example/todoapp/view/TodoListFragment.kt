package com.example.todoapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.example.todoapp.viewmodel.ListTodoViewModel



/**
 * A simple [Fragment] subclass.
 * Use the [TodoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodoListFragment : Fragment() {

    private lateinit var viewModel: ListTodoViewModel
    private val todoListAdapter  = TodoListAdapter(arrayListOf(),
        { item -> viewModel.clearTask(item) })
    private lateinit var binding: FragmentTodoListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater,container,false)
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListTodoViewModel::class.java)
        viewModel.refresh()
        binding.recViewToDo.layoutManager = LinearLayoutManager(context)
        binding.recViewToDo.adapter = todoListAdapter

        binding.btnFab.setOnClickListener {
            val action = TodoListFragmentDirections.actionCreateTodo()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()

    }

    fun observeViewModel() {
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            todoListAdapter.updateTodoList(it)
            if(it.isEmpty()) {
                binding.recViewToDo?.visibility = View.GONE
                binding.txtError.setText("Your todo still empty.")
            } else {
                binding.recViewToDo?.visibility = View.VISIBLE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == false) {
                binding.progressLoad?.visibility = View.GONE
            } else {
                binding.progressLoad?.visibility = View.VISIBLE
            }
        })


        viewModel.todoLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it == false) {
                binding.txtError?.visibility = View.GONE
            } else {
                binding.txtError?.visibility = View.VISIBLE
            }
        })


    }




}