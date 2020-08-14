package com.hms.accountkit.utils

data class AccountDetails (
    var isLoggedIn:Boolean=false,
    var name:String,
    var email: String,
    var imageUrl: String,
    var errorMsg:String
){



}