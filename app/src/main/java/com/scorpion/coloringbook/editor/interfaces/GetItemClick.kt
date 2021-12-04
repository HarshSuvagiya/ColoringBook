package com.scorpion.coloringbook.editor.interfaces

import com.scorpion.coloringbook.editor.model.EditorData

interface GetItemClick {
    fun onItemClick(item : EditorData,pos : Int)
}