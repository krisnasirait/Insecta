package com.insecta.edu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.insecta.edu.adapter.LessonAdapter
import com.insecta.edu.data.LessonsItem
import com.insecta.edu.databinding.FragmentHomeBinding


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
        val lessons = createDummyData()
        val adapter = LessonAdapter(this)
        binding.rvMateri.layoutManager = LinearLayoutManager(context)
        binding.rvMateri.adapter = adapter
        adapter.setData(lessons)
    }

    fun createDummyData(): List<LessonsItem> {
        return listOf(
            LessonsItem(
                lessonId = "Lesson1",
                lessonTitle = "Serangga? Hewan apakah itu?",
                lessonDescription = "This is a lesson about eating.",
                lessonImage = R.drawable.serangga
            ),
            LessonsItem(
                lessonId = "Lesson2",
                lessonTitle = "Kelompok Serangga",
                lessonDescription = "This is a lesson about studying.",
                lessonImage = R.drawable.kelompok
            ),
            LessonsItem(
                lessonId = "Lesson3",
                lessonTitle = "Bagian Tubuh Serangga",
                lessonDescription = "This is a lesson about drinking.",
                lessonImage = R.drawable.bagian
            ),
            LessonsItem(
                lessonId = "Lesson4",
                lessonTitle = "Metamorfosis Serangga",
                lessonDescription = "This is a lesson about seeing.",
                lessonImage = R.drawable.metamorph
            )
        )
    }

    override fun onItemUserListClicked(lessonId: String) {
        TODO("Not yet implemented")
    }
}