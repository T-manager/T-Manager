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
   * Displays a single image
   *
   * @param filename, the full name of image file, including the name and file type
   * @return path and filename
   */
  @GetMapping("/show/{filename}")
  public String showPhotos(@PathVariable String filename) {
    try {
      String path = "///home/ubuntu/images/";
      // Since you are reading files from the native, file must be added, and path is the path in the application configuration file
      return "file:" + path + filename;
    } catch (Exception e) {
      return "fail";
    }
  }

  /**
   * Upload a single image
   *
   * @param username, the name of user who is current uploding the file
   * @return filename, including filetype
   */
  @PostMapping("/upload/{username}")
  @ResponseBody
  public String upload(@PathVariable String username, @RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      return "Upload failed, please select file";
    }
    Optional<User> user = userRepository.findByUserName(username);

    String fileName = (file.getOriginalFilename());
    String fileType = fileName.split("\\.")[fileName.split("\\.").length - 1];
    String filePath = "./images";
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
    return "Upload failed!";
  }

  /**
   * Delete a single image
   *
   * @param filename, including the name and file type
   */
  @DeleteMapping("/delete/{fileName}")
  @ResponseBody
  public void delete(@PathVariable String fileName) {
    String filePath = "./images";
    File path = new File(filePath);
    File file = new File(path.getAbsolutePath() + "/" + fileName);
    if (file.exists()) {
      file.delete();
    }
  }
}
