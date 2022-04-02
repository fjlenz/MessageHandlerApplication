package com.fj.messagehandlerapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fj.messagehandlerapplication.databinding.FragmentSecondBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    var singleMessageRetrieved = MessageModel()

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        // Get Message ID that was handed over from ListView on Fragment First
        var messageIdReceived = arguments?.getString("messageId")
        Log.v("The ID that I got:", messageIdReceived.toString())

        // retrieve single message from API
        retrieveMessage(messageIdReceived.toString())

        return binding.root
    }

    private fun retrieveMessage(messageId:String) {
        binding.textMessageIdView.visibility = GONE
        binding.textMessageTextView.visibility  = GONE
        binding.textMessageRatingView.visibility = GONE

        binding.progressMessageLoader.visibility = VISIBLE

        Log.v("Starting /message GET","")
        // Retrofit for API Call
        val service = MessageAPIClient.getClient().create(MessageService::class.java)

        val call = service.getMessage(messageId)

        call.enqueue(object : Callback<MessageModel> {
            override fun onResponse(call: Call<MessageModel>, response: Response<MessageModel>) {
                Log.v("On Response",response.toString())
                if (response.code() == 200) {
                    //val messagesResponse = response.body()!!
                    singleMessageRetrieved = response.body()!!
                    //MessagesModel.messageList = retrievedMessages
                    Log.v("SingleMessage: ", singleMessageRetrieved.toString())

                    populateSingleMessage()
                    binding.progressMessageLoader.visibility = View.INVISIBLE
                }
            }
            override fun onFailure(call: Call<MessageModel>, t: Throwable) {
                Log.v("Error in Retrofit",t.toString())
            }
        })
    }

    private fun populateSingleMessage() {

        binding.textMessageIdView.text = singleMessageRetrieved.id
        binding.textMessageTextView.setText(singleMessageRetrieved.messageText)
        binding.textMessageRatingView.setText(singleMessageRetrieved.rating.toString())

        binding.textMessageIdView.visibility = VISIBLE
        binding.textMessageTextView.visibility  = VISIBLE
        binding.textMessageRatingView.visibility = VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonUpdate.setOnClickListener {
            updateMessage()
        }
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun updateMessage() {
        binding.progressMessageLoader.visibility = VISIBLE

        Log.v("Starting /message PUT","")
        // Retrofit for API Call
        val service = MessageAPIClient.getClient().create(MessageService::class.java)
        var messageForUpdate = MessageModel()

        messageForUpdate.id = binding.textMessageIdView.text.toString()
        messageForUpdate.messageText = binding.textMessageTextView.text.toString()
        messageForUpdate.rating = Integer.parseInt(binding.textMessageRatingView.text.toString())

        Log.v("Model4Update: ",messageForUpdate.toString())

        val call = service.updateMessage(singleMessageRetrieved.id, messageForUpdate)

        call.enqueue(object : Callback<MessageModel> {
            override fun onResponse(call: Call<MessageModel>, response: Response<MessageModel>) {
                Log.v("On Response",response.toString())
                if (response.code() == 200) {
                    singleMessageRetrieved = response.body()!!
                    Log.v("SingleMessage: ", singleMessageRetrieved.toString())

                    populateSingleMessage()
                    binding.progressMessageLoader.visibility = View.INVISIBLE
                }
            }
            override fun onFailure(call: Call<MessageModel>, t: Throwable) {
                Log.v("Error in Retrofit",t.toString())
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}