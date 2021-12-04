package com.scorpion.coloringbook.category.interfaces

import com.scorpion.coloringbook.category.model.CategoryModel

interface GetItemClick {
    fun onItemClick(model : CategoryModel)
}