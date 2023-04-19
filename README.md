# Spring Boot Ecommerce Project

This project is to add the following security and DevOps functions on an existing eCommerce backend application:
* Handling of user authorization with proper security using JWT with Spring Security.
* Write tests case to meet an acceptable code coverage level of at least 60%
* Add logging using log4j
* Implement automation of CI/CD pipeline:
    * a Tomcat server and a Jenkins server is hosted in a docker container in AWS cloud VM. 
    * Github repository is integrated with the Jenkins server to automate build updated source code pushed and deploy on the Tomcat server as a .war file.
      
      **Architecture of the DevOps**
      
      ![image](https://user-images.githubusercontent.com/23166741/233114652-17ed3e0f-d35f-42f3-afc9-206398f33fc7.png)
      
      **Tomcat and Jenkins Server are hosted in Docker Container on Amazon EC2**
      
      ![image](https://user-images.githubusercontent.com/23166741/233115676-ba060a02-21a3-4506-be87-c7c866b4edc5.png)
      
      **Auto compilation and deployment of the project changes**
      
      ![image](https://user-images.githubusercontent.com/23166741/233116165-bc74eef0-acef-4ee8-b110-728b854fa645.png)
