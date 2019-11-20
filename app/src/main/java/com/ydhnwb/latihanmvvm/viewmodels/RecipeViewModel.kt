package com.ydhnwb.latihanmvvm.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ydhnwb.latihanmvvm.models.Recipe
import com.ydhnwb.latihanmvvm.utils.Constants
import com.ydhnwb.latihanmvvm.utils.SingleLiveEvent
import com.ydhnwb.latihanmvvm.utils.WrappedListResponse
import com.ydhnwb.latihanmvvm.utils.WrappedResponse
import com.ydhnwb.latihanmvvm.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeViewModel : ViewModel() {
    private var recipes = MutableLiveData<List<Recipe>>()
    private var recipe = MutableLiveData<Recipe>()
    private var state : SingleLiveEvent<RecipeState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun fetchAllPost(token : String){
        state.value = RecipeState.IsLoading(true)
        api.allRecipe(token).enqueue(object : Callback<WrappedListResponse<Recipe>>{
            override fun onFailure(call: Call<WrappedListResponse<Recipe>>, t: Throwable) {
                state.value = RecipeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Recipe>>, response: Response<WrappedListResponse<Recipe>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedListResponse<Recipe>
                    if(body.status.equals("1")){
                        val r = body.data
                        recipes.postValue(r)
                    }
                }else{
                    state.value = RecipeState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = RecipeState.IsLoading(false)
            }
        })
    }

    fun fetchOnePost(token: String, id : String){
        state.value = RecipeState.IsLoading(true)
        api.getOneRecipe(token, id).enqueue(object : Callback<WrappedResponse<Recipe>>{
            override fun onFailure(call: Call<WrappedResponse<Recipe>>, t: Throwable) {
                println(t.message)
                state.value = RecipeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<Recipe>>, response: Response<WrappedResponse<Recipe>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedResponse<Recipe>
                    if(body.status.equals("1")){
                        val r = body.data
                        recipe.postValue(r)
                    }
                }else{
                    state.value = RecipeState.Error("Gagal mendapatkan response dari server")
                }
                state.value = RecipeState.IsLoading(false)
            }
        })
    }

    fun create(token: String, title : String, content: String){
        state.value = RecipeState.IsLoading(true)
        api.createRecipe(token, title, content).enqueue(object : Callback<WrappedResponse<Recipe>>{
            override fun onFailure(call: Call<WrappedResponse<Recipe>>, t: Throwable) {
                state.value = RecipeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<Recipe>>, response: Response<WrappedResponse<Recipe>>) {
                if (response.isSuccessful){
                    val body = response.body() as WrappedResponse<Recipe>
                    if(body.status.equals("1")){
                        //0 is success create
                        //1 success update
                        //2 success delete
                        state.value = RecipeState.IsSuccess(0)
                    }else{
                        println(body.message)
                        state.value = RecipeState.Error("Gagal saat membuat recipe. :(")
                    }
                }else{
                    state.value = RecipeState.Error("Kesalahan saat membuat recipe")
                }
                state.value = RecipeState.IsLoading(false)
            }
        })
    }

    fun update(token: String, id: String, title: String, content: String){
        state.value = RecipeState.IsLoading(true)
        api.updateRecipe(token, id, title, content).enqueue(object : Callback<WrappedResponse<Recipe>>{
            override fun onFailure(call: Call<WrappedResponse<Recipe>>, t: Throwable) {
                state.value = RecipeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<Recipe>>, response: Response<WrappedResponse<Recipe>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedResponse<Recipe>
                    if(body.status.equals("1")){
                        state.value = RecipeState.IsSuccess(1)
                    }else{
                        state.value = RecipeState.Error("Gagal saat mengupdate recipe. :(")

                    }
                }else{
                    state.value = RecipeState.Error("Kesalahan saat mengupdate recipe")
                }
                state.value = RecipeState.IsLoading(false)
            }
        })
    }

    fun deleteRecipe(token: String, id : String){
        state.value = RecipeState.IsLoading(true)
        api.deleteRecipe(token, id).enqueue(object : Callback<WrappedResponse<Recipe>>{
            override fun onFailure(call: Call<WrappedResponse<Recipe>>, t: Throwable) {
                state.value = RecipeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<Recipe>>, response: Response<WrappedResponse<Recipe>>) {
                if (response.isSuccessful){
                    val b = response.body() as WrappedResponse<Recipe>
                    if(b.status.equals("1")){
                        state.value = RecipeState.IsSuccess(2)
                    }else{
                        state.value = RecipeState.Error("Tidak dapat menghapus")
                    }
                }else{
                    state.value = RecipeState.Error("Terjadi kesalahan saat menghapus")
                }
                state.value = RecipeState.IsLoading(false)
            }
        })
    }

    fun validate(title : String, content: String) : Boolean{
        state.value = RecipeState.Reset
        if(title.isEmpty() || content.isEmpty()){
            state.value = RecipeState.ShowToast("Mohon isi semua form")
            return false
        }

        if(title.length < 10){
            state.value = RecipeState.RecipeValidation(title = "Judul setidaknya 10 char")
            return false
        }

        if(content.length < 20){
            state.value = RecipeState.RecipeValidation(content = "Konten setidaknya 20 char")
            return false
        }
        return true
     }

    fun getRecipes() = recipes
    fun getOneRecipe() = recipe
    fun getState()  = state
}

sealed class RecipeState {
    data class ShowToast(var message : String) : RecipeState()
    data class IsLoading(var state : Boolean = false) : RecipeState()
    data class RecipeValidation(var title : String? = null, var content : String? = null) : RecipeState()
    data class Error(var err : String?) : RecipeState()
    data class IsSuccess(var what : Int? = null) : RecipeState()
    object Reset : RecipeState()
}
