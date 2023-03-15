package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.DashboardScreenBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.InfoScreenBinding

class DashboardActivity: AppCompatActivity() {
    private lateinit var binding: DashboardScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DashboardScreenBinding.inflate(layoutInflater)
        setContentView(this.binding.root)


    }
}