package com.college.anwesha2k23.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.college.anwesha2k23.MyDialog
import com.college.anwesha2k23.R
import com.college.anwesha2k23.checkValue
import com.college.anwesha2k23.databinding.FragmentSignupBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Signup : Fragment() {
    private lateinit var binding: FragmentSignupBinding
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentSignupBinding.inflate(inflater, container, false)
         val view = binding.root

         binding.signinbutton.setOnClickListener {
             loadFragment(SignIn())
         }

         val myDialog = MyDialog(requireContext())

         binding.SignupButton.setOnClickListener {

             val email = checkValue(binding.EmailId) ?: return@setOnClickListener
             val phone = checkValue(binding.Contactnumber) ?: return@setOnClickListener
             val password = checkValue(binding.AnweshaPassword) ?: return@setOnClickListener
             val confirmPassword = checkValue(binding.ConfirmPassword) ?: return@setOnClickListener
             val name = checkValue(binding.anweshaFullName) ?: return@setOnClickListener

             if(password != confirmPassword) {
                 binding.ConfirmPassword.error = "Password does not match!"
                 return@setOnClickListener
             }
             binding.ConfirmPassword.error = null


             myDialog.showProgressDialog(this@Signup)

             CoroutineScope(Dispatchers.Main).launch {
                 val registerUser = UserRegisterInfo(email, password, name, phone)
                 try {
                     val response = UserAuthApi.userAuthApi.userRegister(registerUser)

                     if(response.isSuccessful) {
                         Snackbar.make(view, "You have successfully Registered!", Snackbar.LENGTH_LONG)
                             .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show()
                         loadFragment(SignIn())
                     }

                     else {
                         Snackbar.make(view, "Could not register!", Snackbar.LENGTH_LONG)
                             .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show()
                     }
                 }
                 catch(e: Exception) {
                     myDialog.showErrorAlertDialog("Oops! It seems like an error... ${e.message}")
                 }
             }
         }

        return view
    }

    private fun loadFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.login_container, fragment)
        fragmentTransaction.commit()
    }
}