package com.ydhnwb.latihanmvvm.viewmodels

import androidx.lifecycle.ViewModel
import com.ydhnwb.latihanmvvm.models.User
import com.ydhnwb.latihanmvvm.utils.Constants
import com.ydhnwb.latihanmvvm.utils.SingleLiveEvent
import com.ydhnwb.latihanmvvm.utils.WrappedResponse
import com.ydhnwb.latihanmvvm.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel(){
    private var state : SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun login(email: String, password: String){
        state.value = UserState.IsLoading(true)
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                state.value = UserState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedResponse<User>
                    if(body.status.equals("1")){
                        state.value = UserState.Success("Bearer ${body.data!!.api_token}")
                    }else{
                        state.value = UserState.Failed("Login gagal")
                    }
                }else{
                    state.value = UserState.Error("Kesalahan terjadi saat login")
                }
                state.value = UserState.IsLoading(false)
            }
        })
    }


    fun register(name : String, email: String, password: String){
        state.value = UserState.IsLoading(true)
        api.register(name, email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                state.value = UserState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedResponse<User>
                    if(body.status.equals("1")){
                        state.value = UserState.Success("Bearer ${body.data!!.api_token}")
                    }else{
                        state.value = UserState.Failed("Register gagal")
                    }
                }else{
                    state.value = UserState.Error("Kesalahan terjadi saat register")
                }
                state.value = UserState.IsLoading(false)
            }
        })
    }


    fun validate(name: String?, email: String, password: String) : Boolean{
        state.value = UserState.Reset
        if(name != null){
            if(name.isEmpty()){
                state.value = UserState.ShowToast("Nama tidak boleh kosong")
                return false
            }
            if(name.length < 5){
                state.value = UserState.Validate(name = "Nama setidaknya 5 chars")
                return false
            }
        }
        if (email.isEmpty() || password.isEmpty()){
            state.value =UserState.ShowToast("Mohon isi semua form")
            return false
        }
        if(!Constants.isValidEmail(email)){
            state.value = UserState.Validate(email = "Email tidak valid")
            return false
        }
        if(!Constants.isValidPassword(password)){
            state.value = UserState.Validate(password = "Password tidak valid")
            return false
        }
        return true
    }

    fun getState() = state

}


sealed class UserState{
    data class Error(var err : String?) : UserState()
    data class ShowToast(var message : String) : UserState()
    data class Validate(var name : String? = null, var email : String? = null, var password : String? = null) : UserState()
    data class IsLoading(var state :Boolean = false) : UserState()
    data class Success(var token :String) : UserState()
    data class Failed(var message :String) : UserState()
    object Reset : UserState()
}