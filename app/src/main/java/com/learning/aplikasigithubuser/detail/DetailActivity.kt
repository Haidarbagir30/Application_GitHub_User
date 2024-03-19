package com.learning.aplikasigithubuser.detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.learning.aplikasigithubuser.R
import com.learning.aplikasigithubuser.data.local.DbModule
import com.learning.aplikasigithubuser.data.model.Item
import com.learning.aplikasigithubuser.data.model.ResponseDetailUser
import com.learning.aplikasigithubuser.databinding.ActivityDetailBinding
import com.learning.aplikasigithubuser.detail.follow.FollowsFragment
import com.learning.aplikasigithubuser.utils.Result

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        DetailViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val item = intent.getParcelableExtra<Item>("item")
        val username = item?.login ?: ""

        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as ResponseDetailUser
                    binding.imageUser.load(user.avatar_url) {
                        transformations(CircleCropTransformation())
                    }
                    binding.username.text = user.name
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getDetailUser(username)

        val fragments = mutableListOf<Fragment>(
            FollowsFragment.newInstance(FollowsFragment.FOLLOWERS),
            FollowsFragment.newInstance(FollowsFragment.FOLLOWINGS)
        )

        val tittleFragment = mutableListOf(
            getString(R.string.followers),
            getString(R.string.following)
        )
//
////Detail Followers & Following
//        viewModel.resultFollowerslUser.observe(this) {
//            when (it) {
//                is Result.Success<*> -> {
//                    val followers = (it.data as List<*>).size
//                    binding.tvJmlFollowers.text = followers.toString()
//                }
//
//                is Result.Error -> {
//                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
//                }
//
//                is Result.Loading -> {
//                    // Tidak perlu melakukan apa-apa ketika loading
//                }
//            }
//        }

        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as? ResponseDetailUser
                    user?.let {
                        binding.imageUser.load(it.avatar_url) {
                            transformations(CircleCropTransformation())
                        }
                        binding.username.text = it.name
                        binding.tvJmlFollowers.text = it.followers.toString()
                        binding.tvJmlFollowing.text = it.following.toString()
                    }
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }



        viewModel.getFollowing(username)
        viewModel.getFollowers(username)

        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as ResponseDetailUser
                    binding.imageUser.load(user.avatar_url) {
                        transformations(CircleCropTransformation())
                    }
                    binding.username.text = user.login // Menampilkan username
                    binding.tvName.text = user.name // Menampilkan name atau string kosong jika null
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }


        val adapter = DetailAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
            tab.text = tittleFragment[position]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Tidak perlu melakukan apa-apa ketika tab di-reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Tidak perlu melakukan apa-apa ketika tab tidak dipilih
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

        })

        // Panggil metode getFollowers dan getFollowing setelah inisialisasi
        if (username.isNotEmpty()) {
            viewModel.getFollowers(username)

            viewModel.resultSuksesFavorite.observe(this){
                binding.btnFavorite.changeIconColor(R.color.red)
            }
            viewModel.resultDeleteFavorite.observe(this){
                binding.btnFavorite.changeIconColor(R.color.white)
            }

            binding.btnFavorite.setOnClickListener {
                viewModel.setFavorite(item)
            }
            viewModel.findFavorite(item?.id ?: 0) {
                binding.btnFavorite.changeIconColor(R.color.red)
            }

        }
    }
}

fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}
