package com.ark.inflearnback.domain.member.service;

import com.ark.inflearnback.domain.member.dto.SignRequestDto;
import com.ark.inflearnback.domain.member.exception.DuplicateEmailException;
import com.ark.inflearnback.domain.member.exception.RoleNotFoundException;
import com.ark.inflearnback.domain.security.model.Member;
import com.ark.inflearnback.domain.security.model.Role;
import com.ark.inflearnback.domain.security.repository.MemberRepository;
import com.ark.inflearnback.domain.security.repository.RoleRepository;
import com.ark.inflearnback.domain.security.type.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManagementService {
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(final SignRequestDto request) throws DuplicateEmailException {
        log.info(String.format("%s 회원 가입 진행", request.getEmail()));
        verifyEmail(request);
        signUpComplete(request);
        log.info(String.format("%s 회원 가입 완료", request.getEmail()));
    }

    private void verifyEmail(final SignRequestDto request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            log.warn("이미 사용중인 이메일이므로 가입 불가");
            throw new DuplicateEmailException("이미 사용중인 이메일입니다.");
        }
    }

    private void signUpComplete(final SignRequestDto request) {
        memberRepository.save(Member.of(request.encodePassword(passwordEncoder), findRoleMember()));
    }

    private Role findRoleMember() {
        try {
            return roleRepository.findByRoleType(RoleType.ROLE_MEMBER)
                    .orElseThrow(RoleNotFoundException::new);
        }
        catch (RoleNotFoundException e) {
            log.error(String.format("%s를 찾을 수 없거나 활성화 돼있지 않습니다. ROLE 테이블을 확인하세요.", RoleType.ROLE_MEMBER));
            throw new RoleNotFoundException("내부 서버 에러.");
        }
    }
}
