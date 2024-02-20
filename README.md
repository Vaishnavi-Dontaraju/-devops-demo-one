## Source code Repositories
- https://github.com/Vaishnavi-Dontaraju/springboot-demo-ms-one
- https://github.com/Vaishnavi-Dontaraju/springboot-demo-ms-two

## Infrastructure Setup 
### Description
- Create an EC2 Instance and security group configs on AWS using Terraform by providing details such as region, ami, instance_type, volume_size and a shell script for user_data attribute in `main.tf`.
- The shell script that is provided in user_data attribute (DevOpsAppSetup.sh) contains installation commands for docker engine, and ensures the containers of jenkins, nexus, sonarqube and nginx are running whenever the EC2 instance is started.
<DevopsAppSetup file link>
### Execution steps
- Run `terraform init` command to initialize a working directory to download the necessary provider plugins and modules.
- Run `terraform plan` to view the execution plan carried out by Terraform to create the resources. 
- Run `terraform apply` to execute the actions proposed in a Terraform plan to create or update infrastructure.
- Run `terraform show` to verify the details of the created EC2 instance.
<add screenshot * 2>
- After the public ip address of the created EC2 instance is noted, run below command to login to instance.
   ```
      $ ssh -i <PEM_FILE_PATH> ec2-user@ec2-ip-address-dns-name-here 
   ```
- Finally, run `sudo docker ps -a` to verify the list of docker containers running currently.
<add screenshot>

## Setup Nginx as Reverse Proxy 
write about config and docker file

## Nexus Setup 
- Use a URL like http://ec2-public-ip/app/nexus to access Nexus via nginx reverse proxy.
- Get initial Nexus admin user's password by running below command in EC2 instance.
    ```
    docker exec nexus bash -c "cat /nexus-data/admin.password"
    ```
- Create the following hosted repositories to store:
   - **snapshot** artifacts
   - **release** artifacts
- Create the following proxy repository to download dependencies from:
    - **maven-central**
- Create the following group repository which includes both hosted and proxy repositories:
   - **maven-public**
<add screenshot>

## Sonarqube Setup
- Use a URL like http://ec2-public-ip/app/sonarqube to access Sonarqube with below default credentials to login.
    ```
    Default User: admin
    Default Password: admin
    ```
- Generate a new token at **User > My Account > Security**
   - Under **Generate Tokens**, enter Name, select Type as **Global Analysis Token** and set Expires in to **no expiration**. Click on Generate Token.
   - Store the token to configure it later as part of Sonarqube tool setup in Jenkins.
   <add screenshot>
- Create a webhook by navigating to the home page **Administration > Configurations > Webhooks**
   - 
   - Click on **Create** and provide name, URL in below format and provide secret text. Store the secret text to use it in Jenkins and click on **Create**
     `** https://${jenkins_domain}/sonarqube-webhook/ **`
   
   <add screenshot>

## Jenkins Setup
- Login to jenkins url http://ec2-public-ip/app/jenkins with initial jenkins admin user's password by running below command in EC2 instance 
    ```
    sudo docker exec jenkins bash -c "cat /var/jenkins_home/secrets/initialAdminPassword"
    ```
### Install Required Plugins
- Maven Integration 
- Pipeline Maven Integration 
- Pipeline Utility Steps
- Nexus Artifact Uploader 
- SonarQube Scanner for Jenkins 

### Sonarqube and Shared Library Setup 
   - Navigate to **Manage Jenkins > Select System under System Configuration**
   - In SonarQube servers section, click on Add SonarQube and provide Name, Server URL
       - For server authentication token, select on +Add drop down and Select Kind as Secret text and provide secret text in Secret field. Enter id and description. Click on Add to submit the details.
       - Now select the created id from drop down as Server authentication token.
       - Next, click on Advanced and click on +Add under Webhook Secret to add webhook details created earlier in sonarqube. Select Kind as Secret text and provide secret text in Secret field. Enter webhook id and description. Click on Add to submit the details.
       - Now select the created webhook id under in Webhook Secret.
   - In Global Pipeline Libraries section, click on Add and provide Name.
       - Select Modern SCM as Retrieval Method and Git as Source Code Management.
       - Paste the git repository url in Project Repository field and click on +Add to add the credentials of git.Select Kind as username and password and enter the details. Provide id, description and click on Add. Now select the created credentials from drop down.
   - Finally, Click on Apply to save the configuration.
        
<add screenshots*3>

### Maven Setup
   - Navigate to **Manage Jenkins > Select Managed Files under System Configuration**
   - Click on Add a new Config, select Maven settings.xml and click Next.
   - Provide Name and click on Add below Server Credentials to provide nexus server id and add nexus credentials.
   - Write maven settings.xml as follows so that the project can pull its dependencies from private nexus artifactory.
   <settings.xml file link>
<add screenshots>

### Tools Configuration
   - Navigate to **Manage Jenkins > Select Tools under System Configuration** 
      - Under Maven Configuration section, select Provided settings.xml from Default settings provider drop down menu. Under Provided Settings drop down menu select the managed file name created in Maven Setup step..
      - Go to JDK installations section and click on Add. Provide the name and the file path of installed jdk. 
      - In SonarQube Scanner installations section, click on Add SonarQube Scanner and provide name and check Install automatically. The sonarqube version is selected by default from drop down menu.
      - Next, go to Maven installations section and click on Add Maven. Provide name and check Install automatically. The maven version is selected by default from drop down menu.
      - Finally, click on Apply to submit the details.
<add screenshots*3>

### Create a Pipeline 
   - Click on + New Item on jenkins home page, select Pipeline, provide pipeline name and click on OK.
   - In configuration page, check on Poll SCM and schedule it as ` * * * * * `
   - In Pipeline section, select Pipeline script from SCM from Definition drop down and provide git repository url in which maven build is present and select git credentials. Click on Apply.
   - Build the pipeline script and check the build status.
<add screenshots*3>

- ### Email notification setup
   - To configure email notifications post build, navigate to **Manage Jenkins > Select System under System Configuration**  
   - Under Extended E-mail Notification section, provide SMTP server, SMTP port and click on Advanced. Click on Add and enter your gmail credentials. Select the credentials and check Use SSL.
   - Similarly, update the same details in Email Notification section and check Use SMTP Authentication and provide gmail credentials.
   - Click on Apply to save the configuration.
<add screenshots*3>








