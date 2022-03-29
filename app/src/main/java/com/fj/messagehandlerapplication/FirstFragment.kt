package com.fj.messagehandlerapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fj.messagehandlerapplication.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment()  {

    var messageList = ArrayList<MessageModel>()

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveMessages()

        binding.buttonRefresh.setOnClickListener {
            retrieveMessages()
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveMessages() {
        binding.progressMessageLoader.visibility = VISIBLE;

        Log.v("Starting /messages call","")
        // Retrofit for API Call
        val service = MessageAPIClient.getClient().create(MessageService::class.java)

        val call = service.getMessageList()

        call.enqueue(object : Callback<ArrayList<MessageModel>> {
            override fun onResponse(call: Call<ArrayList<MessageModel>>, response: Response<ArrayList<MessageModel>>) {
                Log.v("On Response",response.toString())
                if (response.code() == 200) {
                    //val messagesResponse = response.body()!!
                    messageList = response.body()!!
                    //MessagesModel.messageList = retrievedMessages
                    Log.v("Number of Messages: ", messageList.size.toString())
                    for( message in messageList){
                        Log.v("MainActivity", message.messageText)
                    }

                    populateListView()
                    binding.progressMessageLoader.visibility = INVISIBLE;
                }
            }
            override fun onFailure(call: Call<ArrayList<MessageModel>>, t: Throwable) {
                Log.v("Error in Retrofit",t.toString())
            }
        })
    }


    private fun populateListView() {
        val context = context as MainActivity

        var resultAdapter = ArrayAdapter<MessageModel>(
            context,
            android.R.layout.simple_list_item_1,
            messageList
        )


        binding.listMessageView.setOnItemClickListener{parent, view, position, id ->
            Log.v("position", position.toString())
            Log.v("Someone clicked: ", messageList[position].toString())

            val bundle = bundleOf("messageId" to messageList[position].id.toString())

            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)

        }

        binding.listMessageView.setAdapter(resultAdapter)

    }
}