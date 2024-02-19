## Source code Repositories
- https://github.com/Vaishnavi-Dontaraju/springboot-demo-ms-one
- https://github.com/Vaishnavi-Dontaraju/springboot-demo-ms-two

## Infrastructure Setup 
### Description
- Create an EC2 Instance and security group configs on AWS using Terraform by providing details such as region, ami, instance_type, volume_size and a shell script for user_data attribute in `main.tf`.
- The shell script that is provided in user_data attribute (DevOpsAppSetup.sh) contains installation commands for docker engine, and ensures the containers of jenkins, nexus, sonarqube and nginx are running whenever the EC2 instance is started.
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
   - Store the token to configure it later as part of Sonarqube tool setup in Jenkins.
   <add screenshot>
- Create a webhook by navigating to the webhooks page **Administration > Configurations > webhooks**
   - Click on create new webhook, provide name and webhook URL in below format:
     `** https://${jenkins_domain}/sonarqube-webhook/ **`
   
   <add screenshot>

## Jenkins Setup
- Login to jenkins url http://ec2-public-ip/app/jenkins with initial jenkins admin user's password by running below command in EC2 instance 
    ```
    sudo docker exec jenkins bash -c "cat /var/jenkins_home/secrets/initialAdminPassword"
    ```
### Install Required Plugins
- Maven Integration Plugin
- Pipeline Maven Integration Plugin
- Pipeline Utility Steps
- Nexus Artifact Uploader Plugin
- Sonar Scanner Plugin

### JDK Setup
   - Run the below commands in the EC2 instance to install AWS Corretto JDK in jenkins container
        ```
        curl -Lo /tmp/corretto-21.tar.gz https://corretto.aws/downloads/latest/amazon-corretto-21-x64-linux-jdk.tar.gz
        tar -xzvf /tmp/corretto-21.tar.gz -C $(docker volume inspect --format '{{ .Mountpoint }}' jenkins)
        rm -f /tmp/corretto-21.tar.gz
        ```
- ### Tools Configuration
   - Navigate to Manage Jenkins > Tools 
      - Maven setup
      - jdK setup
      - sonarqube setup
<add screenshots*3>

- ### Maven Setup
   - Navigate to Manage Jenkins > Managed Files
   -
<add screenshots*3>

- ### Shared Library Setup
   - Navigate to Manage Jenkins > System
   -
<add screenshots*3>

- ### Create a Pipeline 
   -
   -
<add screenshots*3>

- ### Email notification setup
   -
   -
<add screenshots*3>








