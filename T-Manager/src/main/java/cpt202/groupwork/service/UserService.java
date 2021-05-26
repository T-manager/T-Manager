package cpt202.groupwork.service;

import cpt202.groupwork.Response;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.entity.VerificationCode;

public interface UserService {

  Response<?> userIdExists(final Integer userId);

  Response<?> userGetInfoByName(final String username);

  Response<?> userCreate(final User user);

  Response<?> userDelete(final Integer userId);

  Response<?> userDelete(final String username);

  Response<?> userModify(final Integer userId,  final User userMod);

  Response<?> userModify(final String username,  final User userMod);

  Response<?> userLogin(final User postUser);

  Response<?> userNameExists(final String username);

  Response<?> userEmailExists(final String email);

  Response<?> userInfoCheck(final User postUser);

  Response<?> verificationEmailSend(final User user);

  Response<?> verifyCode(final VerificationCode verificationCode);
}
