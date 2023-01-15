package com.example.relay.running

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.relay.databinding.FragmentRunningBinding
import com.example.relay.db.RunDao
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RunningFragment: Fragment() {

    @Inject
    lateinit var runDao: RunDao

    private lateinit var binding: FragmentRunningBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("runDao","RUNDAO: ${runDao.hashCode()}")
        return FragmentRunningBinding.inflate(layoutInflater).root
    }
}