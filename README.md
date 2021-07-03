# T-Manager
# Development environment deployment

## Frontend

#### Step 1: Clone the project from GitHub

```bash
git clone https://github.com/T-manager/T-Manager.git
```

or you can download the zip file from

https://github.com/T-manager/T-Manager/archive/refs/heads/dev.zip

rename the folder to `T-Manager`

#### Step 2: install the project

go to the frontend part (make sure you're in the project folder `T-Manager`)

```bash
cd tm-frontend
npm install
```

test if it works

```bash
npm run dev
```



## Backend

**Note: You should install: intellij IDEA, java1.8 or openjdk8, MySQL**

#### Step 1: Import pom.xml

Idea will download maven plugin automatically. If not, please install it manually.

#### Step 2: Configure MySQL

login as root

create new MySQL user

```mysql
CREATE USER '[your SQL username]'@"localhost" IDENTIFIED BY '[your password]';
```

create a database named `t-manager` in MySQL

```mysql
CREATE DATABASE `T-Manager`;
```

give necessary privileges

```mysql
GRANT ALL PRIVILEGES ON `T-Manager` . * TO '[your SQL username]'@'localhost';
```

#### Step 3: Configure related configuration files

go to the project folder `T-Manager`

go to backend folder

```bash
cd T-Manager
```

modify folder path for images in `src/main/java/cpt202/groupwork/controller/ResourceController.java`

find the following method and modify the String `path`

```java
public String showPhotos(@PathVariable String filename) {
    try {
      String path = "[The absolute path of the folder where the image files are stored]";
      return "file:" + path + filename;
    } catch (Exception e) {
      return "fail";
    }
  }
```

modify some basic settings in `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/T-Manager?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username = [your database username]
spring.datasource.password = [your database user password]
spring.web.resources.static-locations= file:[The absolute path of the folder where the image files are stored]
```

If you want to deploy your own mailbox service, you need to modify the configuration of `spring.mail` in the `application.properties` file

```properties
spring.mail.host=[SMTP service provider host]
spring.mail.username=[your Email address]
spring.mail.password=[password given by your SMTP service provider]
# port normally 465
spring.mail.port=[service port] 
```




# Production environment deployment

**Note: Server OS: Ubuntu 18.04 TLS**



## Frontend

### on personal computer:

#### Step 1: Configure stored host

go to the project folder `T-Manager`

go to frontend folder

```bash
cd tm-frontend
```

change the host in `src/store/index.js`

find the following part 

```javascript
state: {
    token: null,
    host: "http://localhost:6767/api/",
    show: {
      showModifyTodo: false
    }
  },
```

change the `host` like this:

```bash
host: "http://[your server ip]/api/"
```

#### Step 2: Package the frontend project 

```bash
npm run build
```

upload `dist` folder in the `backend folder` to the server 

```bash
scp -r dist [username]@[yourserver]:/home/[username]
```

### on server:

#### Step 1: install and configure Nginx

```bash
sudo apt install nginx
```

move the  frontend content (an `index.html` and a folder `static`) to the website root

```bash
sudo mv ~/dist/* /var/www/html
```

modify Nginx config

```bash
sudo vi /etc/nginx/sites-enabled/default
```

find the following content

```nginx
location / {
	# First attempt to serve request as file, then
	# as directory, then fall back to displaying a 404.
	try_files $uri $uri/ =404;
}
```

change the content like this

```nginx
location / {
	try_files $uri $uri/ /index.html;
}
location ^~ /api {
    proxy_pass http://localhost:6767;
}
```

save the file and then restart Nginx service

```bash
sudo systemctl restart nginx
```



## Backend

### on personal computer:

#### Step 1: Configure related configuration files

go to the project folder `T-Manager`

go to backend folder

```bash
cd T-Manager
```

modify folder path for images in `src/main/java/cpt202/groupwork/controller/ResourceController.java`

find the following method and modify the String `path`

```java
public String showPhotos(@PathVariable String filename) {
    try {
      String path = "[The absolute path of the folder where the image files are stored]";
      return "file:" + path + filename;
    } catch (Exception e) {
      return "fail";
    }
  }
```

change some basic settings in `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://[your ip address or host name]:3306/T-Manager?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username = [your database username]
spring.datasource.password = [your database user password]
spring.web.resources.static-locations= file:[The absolute path of the folder where the image files are stored]
```

If you want to deploy your own mailbox service, you need to modify the configuration of `spring.mail` in the `application.properties` file

```properties
spring.mail.host=[SMTP service provider host]
spring.mail.username=[your Email address]
spring.mail.password=[password given by your SMTP service provider]
# port normally 465
spring.mail.port=[service port] 
```

#### Step 2: package the backend project

```bash
mvn package # if you are using Intellij IDEA, you do not need this command. Just use IDEA to package
```

after build success, upload the jar package to your server

```bash
cd target
scp demo-0.0.1-SNAPSHOT.jar [username]@[yourserver]:/home/[username]
```



### on server:

#### Step 1: install and configure MySQL

```bash
sudo apt install mysql-server
```

```
mysql --defaults-file=/etc/mysql/debian.cnf
```

create new MySQL user

```mysql
CREATE USER '[your SQL username]'@'%' IDENTIFIED BY '[your password]';
```

create a database named `t-manager` in MySQL

```mysql
CREATE DATABASE `T-Manager`;
```

give necessary privileges

```mysql
GRANT ALL PRIVILEGES ON `T-Manager` . * TO '[your SQL username]'@'%';
```

allow remote login

```
sudo vi /etc/mysql/debian.cnf
```

commit the line `bind-address=127.0.0.1`

save debian.cnf

#### Step 2: install java

```bash
sudo apt install openjdk-8-jdk
```

#### Step 3: Start T-Manager service

Install `screen` to ensure that the jar can run in the background after exiting the terminal

```bash
sudo apt install screen
```

create a new `screen` called `backend`

```bash
screen -S backend
```

run the backend program in the new `screen`

```bash
java -jar demo-0.0.1-SNAPSHOT.jar
```

