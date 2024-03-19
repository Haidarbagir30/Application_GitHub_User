package com.learning.aplikasigithubuser.data.model

data class RensponseUserGithub(
    val incomplete_results: Boolean,
    val items: MutableList<Item>,
    val total_count: Int
)