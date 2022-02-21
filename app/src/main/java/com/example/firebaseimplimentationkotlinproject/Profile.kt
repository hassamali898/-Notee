package com.example.firebaseimplimentationkotlinproject

import android.support.annotation.Keep
import java.util.*

@Keep
data class Profile(var uid:String?=null,var title:String?=null,var caption:String?=null,var photo:String?=null,var creationTime:Date?=null)
