package com.scorpioninfosoft.coloringbook.category.model

import java.io.Serializable

data class CategoryModel(
    var catId : String,
    var catName : String,
    var key : String,
    var thumbUrl : String
) : Serializable
