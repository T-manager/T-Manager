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
    boolean existsByUsername(String username);


    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findById(Integer id);

}