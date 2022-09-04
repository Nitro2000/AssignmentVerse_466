package com.example.assignmentverse.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentverse.R
import com.example.assignmentverse.adapters.ContactAdapter
import com.example.assignmentverse.model.ContactInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {

    private lateinit var contactList: MutableList<ContactInfo>

    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        mActivity  = requireActivity()
        contactList = mutableListOf()
        dbRef = Firebase.database.getReference("Contacts")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val conRecView = view.findViewById<RecyclerView>(R.id.con_rec_view)
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val contact = data.getValue(ContactInfo::class.java)
                        if (contact != null) contactList.add(contact)
                    }

                    val contactAdapter = ContactAdapter(contactList, mContext)
                    conRecView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                    conRecView.adapter = contactAdapter

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}