package com.example.assignmentverse.fragments

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.assignmentverse.R
import com.example.assignmentverse.model.ContactInfo
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class WelcomeFragment : Fragment() {


    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity

    private lateinit var permTxt : TextView

    private lateinit var dbRef : DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        mActivity  = requireActivity()

        dbRef = Firebase.database.getReference("Contacts")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permTxt = view.findViewById(R.id.perm_txt)

        // Permission check
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            readContacts()
                            permTxt.apply {
                                setTextColor(ContextCompat.getColor(mContext, R.color.primary))
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
            else {
                permTxt.apply {
                    text = mActivity.getString(R.string.perm_mess_not_granted)
                    setTextColor(ContextCompat.getColor(mContext, R.color.error))
                }
                Toast.makeText(mContext, mActivity.getString(R.string.perm_denied), Toast.LENGTH_SHORT).show()
            }
        }
        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    private val conProj: Array<out String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
    )

    private val phoneProj: Array<out String> = arrayOf(
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    private fun readContacts() {
        // Making contact cursor
        val contactCur = mContext.contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
            conProj, null,
            null, ContactsContract.Contacts.DISPLAY_NAME)

        // Extracting contact details, we can directly use rawcontact query as well but it will make duplicates if contact is save at two places
        while(contactCur?.moveToNext() == true) {
            val id = contactCur.getColumnIndex(ContactsContract.Contacts._ID)
            val name = contactCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)


            // For phone number we have to make a query
            val phoneCur = mContext.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                        phoneProj,
                                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                                        arrayOf(contactCur.getString(id)), null)
            if (phoneCur?.moveToNext() == true) {
                val number = phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val newCont = ContactInfo(contactCur.getString(name), phoneCur.getString(number))
                // Inserting into db
                val contactId = dbRef.push().key ?: ""
                dbRef.child(contactId).setValue(newCont)
                    .addOnFailureListener {
                        Toast.makeText(mContext, "Error occurred ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            phoneCur?.close()
         }
        contactCur?.close()
    }

}