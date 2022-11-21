package com.example.tubespbp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubespbp.api.UserAPI
import com.example.tubespbp.room.UserDB
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class FragmentProfile : Fragment() {
    val db by lazy{activity?.let { UserDB(it)}  }

    private var queue: RequestQueue? = null
    var sharedPreferences: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val nameTxt :TextView =  view.findViewById(R.id.etUsername)
        val emailTxt :TextView =  view.findViewById(R.id.etEmail)
        val tanggalLahirTxt :TextView =  view.findViewById(R.id.etTglLahir)
        val noHpTxt :TextView =  view.findViewById(R.id.etNoHp)
        val btnEdit : Button = view.findViewById(R.id.btnEdit)
        val imageButton : ImageView = view.findViewById(R.id.user)
        val id = sharedPreferences?.getString("id", "-1")
        val token = sharedPreferences!!.getString("token", "")
        queue = Volley.newRequestQueue(requireContext())
        getUserById(id!!.toLong(), token!!, nameTxt, emailTxt)

        val user = db?.UserDao()?.getUser(id!!.toInt())
        nameTxt.setText(user!!.username)
        emailTxt.setText(user.email)
        tanggalLahirTxt.setText(user.tanggallahir)
        noHpTxt.setText(user.noHp)

        btnEdit.setOnClickListener(){
            (activity as HomeActivity).setActivity(EditProfileActivity())
        }

        imageButton.setOnClickListener(){
            println("hello")
            val intent = Intent(requireActivity(), CameraActivity::class.java)

            startActivity(intent)
        }

    }

    private fun getUserById(id: Long, token:String, name:TextView, email :TextView){
        sharedPreferences = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        println("id" + id)

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, UserAPI.GET_BY_ID_URL + id,
                { response ->
                    val jsonObject = JSONObject(response)
                    val Username = jsonObject.getJSONObject("data").getString("username")
                    val Email = jsonObject.getJSONObject("data").getString("email")
                    name.setText(Username)
                    email.setText(Email)
                },
                Response.ErrorListener{ error ->

                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun logout(token : String) {
        val stringRequest: StringRequest =
            object :
                StringRequest(Method.POST, UserAPI.GET_ALL_URL, Response.Listener { response ->
                    val gson = Gson()
                    val jsonObject = JSONObject(response)
                    var status = jsonObject.getString("message")

                    if (status != "Logout Succes")
                        Toast.makeText(requireContext(), "Logout Successfully", Toast.LENGTH_SHORT)
                            .show()

                }, Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            requireContext(),
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }

            }
        queue!!.add(stringRequest)
    }
}