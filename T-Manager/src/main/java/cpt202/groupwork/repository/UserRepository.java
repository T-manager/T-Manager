package cpt202.groupwork.repository;

import cpt202.groupwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 是否存在 user.userName
     *
     * @param userName
     * @return
     */
    boolean existsByUserName(String userName);

}