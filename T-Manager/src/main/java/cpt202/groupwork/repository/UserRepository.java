package cpt202.groupwork.repository;

import cpt202.groupwork.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Check whether userName exists in user database
     * @param username
     * @return boolean
     */
    boolean existsByUserName(String username);

    /**
     * Check whether userName exists in user database
     * @param userEmail
     * @return boolean
     */
    boolean existsByUserEmail(String userEmail);

    /**
     * Using user name, find user
     * @param username
     * @return Optional<User>
     */
    Optional<User> findByUserName(String username);

    /**
     * Using Email, find user in user database
     * @param email
     * @return Optional<User>
     */
    User findByUserEmail(String email);

    /**
     * Using userId, find user in user database
     * @param id
     * @return Optional<User>
     */
    Optional<User> findByUserId(Integer id);

}