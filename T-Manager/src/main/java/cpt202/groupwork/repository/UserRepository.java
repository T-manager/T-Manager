package cpt202.groupwork.repository;

import cpt202.groupwork.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 是否存在 user.userName
     *
     * @param username
     * @return
     */
    boolean existsByUserName(String username);
    boolean existsByUserEmail(String userEmail);


    Optional<User> findByUserName(String username);
    User findByUserEmail(String email);
    Optional<User> findByUserId(Integer id);

}