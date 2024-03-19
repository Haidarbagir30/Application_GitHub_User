package com.learning.aplikasigithubuser.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.learning.aplikasigithubuser.data.local.DbModule
import com.learning.aplikasigithubuser.data.model.Item
import com.learning.aplikasigithubuser.data.remoteuser.ApiClient
import com.learning.aplikasigithubuser.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val db: DbModule) : ViewModel() {
    val resultDetailUser = MutableLiveData<Result>()
    val resultFollowerslUser = MutableLiveData<Result>()
    val resultFollowinglUser = MutableLiveData<Result>()
    val resultSuksesFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    private var isFavorite = false

    fun setFavorite(item: Item?) {
        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    db.userDao.delete(item)
                    resultDeleteFavorite.value = true
                } else {
                    db.userDao.insert(item)
                    resultSuksesFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }

    }

    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findByid(id)
            if (user != null) {
                listenFavorite()
                isFavorite = true
            }
        }

    }

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getDetailUserGithub(username)

                emit(response)
            }
                .onStart {//dijalankan ketika mulai
                    resultDetailUser.value = Result.Loading(true)
                }
                .onCompletion {//dijalankan ketika selesai
                    resultDetailUser.value = Result.Loading(false)
                }
                .catch {//dijalankan ketika error
                    it.printStackTrace()
                    resultDetailUser.value = Result.Error(it)
                }
                .collect {
                    resultDetailUser.value = Result.Success(it)
                }

        }
    }

    fun getFollowing(username: String) {

        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getFollowingUserGithub(username)

                emit(response)
            }
                .onStart {//dijalankan ketika mulai
                    resultFollowinglUser.value = Result.Loading(true)
                }
                .onCompletion {//dijalankan ketika selesai
                    resultFollowinglUser.value = Result.Loading(false)
                }
                .catch {//dijalankan ketika error
                    it.printStackTrace()
                    resultFollowinglUser.value = Result.Error(it)
                }
                .collect {
                    resultFollowinglUser.value = Result.Success(it)
                }

        }
    }

    fun getFollowers(username: String) {

        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getFollowersUserGithub(username)

                emit(response)
            }
                .onStart {//dijalankan ketika mulai
                    resultFollowerslUser.value = Result.Loading(true)
                }
                .onCompletion {//dijalankan ketika selesai
                    resultFollowerslUser.value = Result.Loading(false)
                }
                .catch {//dijalankan ketika error
                    it.printStackTrace()
                    resultFollowerslUser.value = Result.Error(it)
                }
                .collect {
                    resultFollowerslUser.value = Result.Success(it)
                }

        }
    }

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}