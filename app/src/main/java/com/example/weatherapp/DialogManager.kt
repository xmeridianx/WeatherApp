package com.example.weatherapp

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object DialogManager {
    fun locationDialog(contenxt: Context, listener: Listener){
        val builder = AlertDialog.Builder(contenxt)
        val dialog = builder.create()
        dialog.setTitle("Местоположение включено?")
        dialog.setMessage("Хотите включить местоположение?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Да"){_,_ ->
            listener.onClick(null)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Нет"){_,_ ->
            dialog.dismiss()
        }
        dialog.show()
    }
    fun searchByCity(contenxt: Context, listener: Listener){
        val builder = AlertDialog.Builder(contenxt)
        val editText = EditText(contenxt)
        builder.setView(editText)
        val dialog = builder.create()
        dialog.setTitle("Название города")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Да"){_,_ ->
            listener.onClick(editText.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Нет"){_,_ ->
            dialog.dismiss()
        }
        dialog.show()
    }
    interface Listener{
        fun onClick(name: String?)
    }
}