package cpt202.groupwork.controller;


import cpt202.groupwork.entity.User;
import cpt202.groupwork.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/resource")
public class ResourceController {

  @Autowired
  UserRepository userRepository;

  /**
   * 显示单张图片
   *
   * @return
   */
  @GetMapping("/show/{filename}")
  public String showPhotos(@PathVariable String filename) {
    try {
      String path = "///data/www/java01/images/";
      // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
      return "file:" + path + filename;
    } catch (Exception e) {
      return "fail";
    }
  }

  @PostMapping("/upload/{username}")
  @ResponseBody
  public String upload(@PathVariable String username, @RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      return "上传失败，请选择文件";
    }
    Optional<User> user = userRepository.findByUserName(username);

    String fileName = (file.getOriginalFilename());
    String fileType = fileName.split("\\.")[fileName.split("\\.").length - 1];
    String filePath = "../images";
    DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    Calendar calendar = Calendar.getInstance();
    String dateName = df.format(calendar.getTime());
    File path = new File(filePath);
    if (!path.exists()) {
      path.mkdirs();
    }
    File dest = new File(path.getAbsolutePath() + "/" + dateName + "." + fileType);
    try {
      file.transferTo(dest);
      user.get().setUserAvatar(dateName + "." + fileType);
      userRepository.save(user.get());
      return dateName + "." + fileType;
    } catch (IOException e) {
    }
    return "上传失败！";
  }

  @DeleteMapping("/delete/{fileName}")
  @ResponseBody
  public void delete(@PathVariable String fileName) {
    String filePath = "../images";
    File path = new File(filePath);
    File file = new File(path.getAbsolutePath() + "/" + fileName);
    if (file.exists()) {
      file.delete();
    }
  }
}
