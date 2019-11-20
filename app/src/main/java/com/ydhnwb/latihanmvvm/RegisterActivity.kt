package com.ydhnwb.latihanmvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ydhnwb.latihanmvvm.utils.Constants
import com.ydhnwb.latihanmvvm.viewmodels.UserState
import com.ydhnwb.latihanmvvm.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
        doRegister()
    }

    private fun doRegister(){
        btn_register.setOnClickListener {
            val name = et_name.text.toString().trim()
            val email = et_email.text.toString().trim()
            val passw = et_password.text.toString().trim()
            if(userViewModel.validate(name, email, passw)){
                userViewModel.register(name, email, passw)
            }
        }
    }

    private fun handleUIState(it : UserState){
        when(it){
            is UserState.Reset -> {
                setNameError(null)
                setEmailError(null)
                setPasswordError(null)
            }
            is UserState.Error -> {
                isLoading(false)
                toast(it.err)
            }

            is UserState.ShowToast -> toast(it.message)
            is UserState.Failed -> {
                isLoading(false)
                toast(it.message)
            }
            is UserState.Validate -> {
                it.name?.let{
                    setNameError(it)
                }
                it.email?.let {
                    setEmailError(it)
                }
                it.password?.let {
                    setPasswordError(it)
                }
            }
            is UserState.Success -> {
                Constants.setToken(this@RegisterActivity, it.token)
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }).also {
                    finish()
                }
            }
            is UserState.IsLoading -> isLoading(it.state)
        }
    }



    private fun setNameError(err : String?) { in_name.error = err }
    private fun setEmailError(err : String?){ in_email.error = err }
    private fun setPasswordError(err : String?){ in_password.error = err }
    private fun isLoading(state : Boolean){
        if(state){
            btn_register.isEnabled = false
            loading.isIndeterminate = true
        }else{
            loading.apply {
                isIndeterminate = false
                progress = 0
            }
            btn_register.isEnabled = true
        }
    }

    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()


}
