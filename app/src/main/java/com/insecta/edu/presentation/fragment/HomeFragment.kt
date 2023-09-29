package com.insecta.edu.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.insecta.edu.R
import com.insecta.edu.adapter.LessonAdapter
import com.insecta.edu.data.Util
import com.insecta.edu.data.model.DetailLessonItem
import com.insecta.edu.data.model.LessonItem
import com.insecta.edu.databinding.FragmentHomeBinding
import com.insecta.edu.presentation.activity.DetailLessonActivity


class HomeFragment : Fragment(), LessonAdapter.OnItemClickListener  {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = LessonAdapter(this)
        binding.rvMateri.layoutManager = LinearLayoutManager(context)
        binding.rvMateri.adapter = adapter
        adapter.setData(Util.dummyLessonsData)
    }

    override fun onItemClicked(lessonId: String) {
        val intent = Intent(requireContext(), DetailLessonActivity::class.java)
        intent.putExtra("LESSON_ID", lessonId)
        startActivity(intent)
    }
}