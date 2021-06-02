package cpt202.groupwork.repository;

import cpt202.groupwork.entity.VerificationCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: Zhonghao
 * @create:
 **/



@Repository
public interface VerifyRepository extends JpaRepository<VerificationCode, Integer> {

  boolean existsByUserName(String username);
  boolean existsByUserEmail(String userEmail);


  Optional<VerificationCode> findByUserName(String username);
  Optional<VerificationCode> findByUserEmail(String userEmail);

}