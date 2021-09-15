package com.ark.inflearnback.domain.security.repository;

import com.ark.inflearnback.annotation.EnableDockerContainers;
import com.ark.inflearnback.annotation.ExtensionJpaTest;
import com.ark.inflearnback.domain.security.model.Member;
import com.ark.inflearnback.domain.security.model.Role;
import com.ark.inflearnback.domain.security.type.RoleType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@ExtensionJpaTest
@EnableDockerContainers
@DisplayName("Docker로 Test용 MySQL Container가 적용되는지 테스트. 이 테스트를 실행할 때 반드시 Docker가 실행중이어야 함 !")
class MemberRepositoryTest {

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;

    public MemberRepositoryTest(
        final MemberRepository memberRepository, final RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
    }

    @Test
    void save() throws Exception {
        Role role = roleRepository.save(Role.of(RoleType.MEMBER, false));
        Member member = memberRepository.save(Member.of("siro@gmail.com", "password", role));
        Assertions.assertThat(member.getEmail()).isEqualTo("siro@gmail.com");
        Assertions.assertThat(member.getPassword()).isEqualTo("password");
        Assertions.assertThat(member.getRole()).isEqualTo(role);
    }

}
