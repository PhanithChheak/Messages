package com.phanith.messages

// we create a data class to store data but since we use fire base we just need an empty constructor
class User {
    var name: String? = null
    var email: String?=null
    var uid: String?=null
    var image: Int?= null

    constructor(){}

    constructor(name: String?,email: String?,uid: String?)
    {
        this.image =  R.drawable.ic_baseline_account_circle_24
        this.name = name
        this.email = email
        this.uid = uid
    }


}