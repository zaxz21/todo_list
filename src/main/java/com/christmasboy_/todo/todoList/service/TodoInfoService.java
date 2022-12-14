package com.christmasboy_.todo.todoList.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.christmasboy_.todo.member.entity.MemberInfoEntity;
import com.christmasboy_.todo.todoList.entity.TodoInfoEntity;
import com.christmasboy_.todo.todoList.repository.TodoInfoRepository;

@Service
public class TodoInfoService {
    @Autowired TodoInfoRepository tRepo;
    public Map<String, Object> addTodoInfo(TodoInfoEntity data, HttpSession session) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if(loginUser == null) {
            resultMap.put("status", false);
            resultMap.put("message", "로그인이 필요한 서비스입니다.");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        } else {
            data.setMiSeq(loginUser.getSeq());
            tRepo.save(data);
            resultMap.put("status", true);
            resultMap.put("message", "일정이 등록되었습니다.");
            resultMap.put("code", HttpStatus.CREATED);
        }
        return resultMap;
    }
    public Map<String, Object> getTodoList(HttpSession session) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if(loginUser == null) {
            resultMap.put("status", false);
            resultMap.put("message", "로그인이 필요한 서비스입니다.");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        } else {
            resultMap.put("status", true);
            resultMap.put("message", "조회되었습니다.");
            resultMap.put("code", HttpStatus.OK);
            resultMap.put("list", tRepo.findAllByMiSeq(loginUser.getSeq()));
        }
        return resultMap;
    }

    public Map<String, Object> updateTodoStatus(Integer status, Long seq) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        TodoInfoEntity todo = tRepo.findBySeq(seq);
        if(todo == null) {
            resultMap.put("status", false);
            resultMap.put("message", "잘못된 Todo 번호입니다.");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        } else {
            todo.setStatus(status);
            tRepo.save(todo);
            resultMap.put("status", true);
            resultMap.put("message", "Todo 상태가 변경되었습니다.");
            resultMap.put("code", HttpStatus.OK);
        }
        return resultMap;
    }
    
    public Map<String, Object> updateTodoContent(String content, Long seq) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        TodoInfoEntity todo = tRepo.findBySeq(seq);
        if(todo == null) {
            resultMap.put("status", false);
            resultMap.put("message", "잘못된 Todo 번호입니다.");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        } else {
            todo.setContent(content);
            tRepo.save(todo);
            resultMap.put("status", true);
            resultMap.put("message", "Todo 내용이 변경되었습니다.");
            resultMap.put("code", HttpStatus.OK);
        }
        return resultMap;
    }

    @Transactional
    public Map<String, Object> deleteTodo(Long seq, HttpSession session) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if(loginUser == null) {
            resultMap.put("status", false);
            resultMap.put("message", "로그인이 필요한 서비스입니다.");
            resultMap.put("code", HttpStatus.FORBIDDEN);
            return resultMap;
        }
        TodoInfoEntity todo = tRepo.findBySeqAndMiSeq(seq, loginUser.getSeq());
        if(todo == null) {
            resultMap.put("status", false);
            resultMap.put("message", "잘못된 Todo 번호입니다.");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        }else {
            tRepo.deleteBySeqAndMiSeq(seq, loginUser.getSeq());
            resultMap.put("status", true);
            resultMap.put("message", "삭제되었습니다.");
            resultMap.put("code", HttpStatus.OK);
        }
        return resultMap;
    }

    public Map<String, Object> selectTodoListByTerm(
        HttpSession session, String start, String end
    ) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if(loginUser == null) {
            resultMap.put("status", false);
            resultMap.put("message", "로그인이 필요한 서비스입니다.");
            resultMap.put("code", HttpStatus.FORBIDDEN);
            return resultMap;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date startDt = null;
		Date endDt = null;
        try {
            startDt = formatter.parse(start);
            endDt = formatter.parse(end);
        } catch (Exception e) {
            resultMap.put("status", false);
            resultMap.put("message", "날짜 형식을 확인해 주세요. (yyyyMMdd ex:20221214)");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
            return resultMap;
        }
        resultMap.put("status", true);
        resultMap.put("message", "조회 완료");
        resultMap.put("list", tRepo.findByEndDtBetweenAndMiSeq(startDt, endDt, loginUser.getSeq()));
        resultMap.put("code", HttpStatus.OK);
        return resultMap;
		
    }
}
