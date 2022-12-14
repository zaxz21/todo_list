package com.christmasboy_.todo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.christmasboy_.todo.todoList.entity.TodoInfoEntity;
import com.christmasboy_.todo.todoList.repository.TodoInfoRepository;
import com.christmasboy_.todo.todoList.service.TodoInfoService;

@SpringBootTest
class TodoApplicationTests {
	// @Autowired TodoInfoService tService;
	@Autowired TodoInfoRepository tRepo;
	@Test
	void loadTodo() throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date start = formatter.parse("2022-12-14");
		Date end = formatter.parse("2022-12-23");
		List<TodoInfoEntity> list = tRepo.findByEndDtBetweenAndMiSeq(start, end, 2L);
		for(TodoInfoEntity t : list) {
			System.out.println(t);
		}
	}

}
