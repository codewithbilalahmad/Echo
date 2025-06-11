package com.muhammad.echo.core.presentation.designsystem.dropdown

data class Selectable<T>(
    val item : T,val selected : Boolean
){
    companion object{
        fun <T> List<T>.asUnselectedItems() : List<Selectable<T>>{
            return map {item ->
                Selectable(item = item, selected = false)
            }
        }
    }
}