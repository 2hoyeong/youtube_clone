package me.hoyoung.youtube.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryTest {

    @Autowired
   private  UserRepository userRepository;

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void userSignUpTest() throws Exception {
        //given
        String id = "testId";
        String password = "testPassword";
        String name = "테스트용 계정";

        userRepository.save(User.builder()
                .id(id)
                .name(name)
                .password(password)
                .build());

        //when
        List<User> userList = userRepository.findAll();

        //then
        User user = userList.get(0);
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
    }
}
