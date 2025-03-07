tạo file application.properties
  |--  src/
    |--  main/
      |--  resources/
        |--  application.properties

thêm mã dưới vô file application.properties, tạo csdl dùng largon, xampp 

spring.application.name=LTDD
server.port=8080

#đổi tên csld mới tạo vào 
spring.datasource.url=jdbc:mysql://localhost:3306/<tên csdl>
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update

spring.resources.static-locations=classpath:/static/

spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

spring.http.encoding.charset=UTF-8
spring.http.encoding.enabled=true
spring.http.encoding.force=true

spring.resources.cache.period=0
spring.web.resources.cache.cachecontrol.max-age=0

jwt.secret=M2Q1JEY3ZzktSGJKa0xwMFFyU3RAM1d4eVphQmNEZUZnSGlKa0xtTm9QcVJzVFQhdQ==

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=hethongvellrel@gmail.com
spring.mail.password=QEVA ZAMC FPPK QZZ H
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.debug=true
