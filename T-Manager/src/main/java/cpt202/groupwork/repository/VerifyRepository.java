package cpt202.groupwork.repository;

import cpt202.groupwork.entity.VerificationCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VerifyRepository extends JpaRepository<VerificationCode, Integer> {

  /**
   * Using user name, check whether verificationCode is exist
   * @param username
   * @return boolean
   */
  boolean existsByUserName(String username);

  /**
   *Using email, check whether verificationCode is exist
   * @param userEmail
   * @return boolean
   */
  boolean existsByUserEmail(String userEmail);

  /**
   * Using username, find verificationCode in verification database
   * @param username
   * @return Optional<VerificationCode>
   */
  Optional<VerificationCode> findByUserName(String username);

  /**
   * Using e-mail, find verificationCode in verification database
   * @param userEmail
   * @return Optional<VerificationCode>
   */
  Optional<VerificationCode> findByUserEmail(String userEmail);

}