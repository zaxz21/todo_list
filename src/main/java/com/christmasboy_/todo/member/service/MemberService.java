package com.christmasboy_.todo.member.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.christmasboy_.todo.member.data.LoginVO;
import com.christmasboy_.todo.member.entity.MemberImageEntity;
import com.christmasboy_.todo.member.entity.MemberInfoEntity;
import com.christmasboy_.todo.member.repository.MemberImageRepository;
import com.christmasboy_.todo.member.repository.MemberRepository;
import com.christmasboy_.todo.utils.AESAlgorithm;

@Service
public class MemberService {
    @Autowired MemberRepository m_repo;
    @Autowired MemberImageRepository mi_repo;
    public Map<String, Object> addMemberImage(MemberImageEntity data, Long miSeq) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        data.setMiSeq(miSeq);
        mi_repo.save(data);
        resultMap.put("status", true);
        resultMap.put("message", "이미지가 저장되었습니다. ");
        return resultMap;
    }
    public String getFilenameByUri(String uri) {
        List<MemberImageEntity> data = mi_repo.findTop1ByUriOrderBySeqDesc(uri);
        return data.get(0).getFileName();
    }
    public Map<String, Object> addMember(MemberInfoEntity data) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if(m_repo.countByEmail(data.getEmail()) == 1) {
            resultMap.put("status", false);
            resultMap.put("message", data.getEmail()+"은/는 이미 등록된 사용자 입니다.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else {
            try{
                String encPwd = AESAlgorithm.Encrypt(data.getPwd());
                data.setPwd(encPwd);
            } catch(Exception e) {e.printStackTrace();}
            m_repo.save(data);
            resultMap.put("status", true);
            resultMap.put("message", "회원이 등록되었습니다.");
            resultMap.put("code", HttpStatus.CREATED);
        }
        return resultMap;
    }
    public Map<String, Object> loginMember(LoginVO data) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = null;
        try {
            loginUser = m_repo.findByEmailAndPwd(data.getEmail(), AESAlgorithm.Encrypt(data.getPwd()));
        } catch(Exception e) {e.printStackTrace();}
        if(loginUser == null) {
            resultMap.put("status", false);
            resultMap.put("message", "아이디 혹은 비밀번호 오류입니다.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        } else {
            resultMap.put("status", true);
            resultMap.put("message", "로그인 되었습니다");
            resultMap.put("code", HttpStatus.ACCEPTED);
            resultMap.put("loginUser", loginUser);
        }
        return resultMap;
    }
}
