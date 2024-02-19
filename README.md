## Source code Repositories
- https://github.com/Vaishnavi-Dontaraju/springboot-demo-ms-one
- https://github.com/Vaishnavi-Dontaraju/springboot-demo-ms-two

## Infrastructure Setup 
- Create an EC2 Instance and security group on AWS using Terraform by providing region,ami, instance_type, volume_size in main.tf configuration file. Assign a filepath to user_data attribute in main.tf file which contains installation commands of docker containers and docker network.
- The file DevOpsAppSetup.sh installs jenkins, nexus, sonarqube and nginx containers while launching the EC2 instance.
- Run `terraform init` command to initialize a working directory containing the .tf file and download the necessary provider plugins and modules.
- Run `terraform plan` to view the execution plan carried out by terraform to create the resources. Next, run `terraform apply` to execute the actions proposed in a Terraform plan to create, update, or destroy infrastructure.
- After EC2 instance is created, run below command to login to instance.
    `$ ssh -i <PEM_FILE_PATH> ec2-user@ec2-ip-address-dns-name-here ` 
- Next, run `sudo docker ps -a` and check the list of docker containers which are running currently.

## Setup Nginx as Reverse Proxy 
write about config and docker file

## Nexus Setup 
- Use a URL like http://ec2-ip-address/app/nexus to access Nexus via nginx reverse proxy.
- Get initial Nexus admin user password by running below command in EC2 instance cli and change the password accordingly.
    ` docker exec nexus bash -c "cat /nexus-data/admin.password" `
- Create two hosted repositories:
   - **snapshot**
   - **release**
- Create one proxy repository:
   - **maven-central**
- Create one group repository which includes both hosted and proxy repositories:
   - **maven-public**

## Sonarqube Setup
- Use a URL like http://ec2-ip-address/app/sonarqube to access sonarqube and use default credentials to login.
    ```
    Default User: admin
    Default Password: admin
    ```
- Generate a new token at **User > My Account > Security**
   - 
   -
   - Copy the token in a separate file.
- Create a webhook by navigating to the webhooks page *Administration > Configurations > webhooks*
   - Click on create new webhook, provide name and webhook URL in below format:
     `** https://${jenkins_domain}/sonarqube-webhook/ **`
   -

## Jenkins Setup
- Login to jenkins url http://ec2-ip-address/app/jenkins with initial jenkins admin user's password by running below command in EC2 instance machine 
    `docker exec jenkins bash -c "cat /var/jenkins_home/secrets/initialAdminPassword"`
- ### Plugins to install
        - Maven Integration Plugin
        - Pipeline Maven Integration Plugin
        - Pipeline Utility Steps
        - Nexus Artifact Uploader Plugin
        - Sonar Scanner Plugin
- ### JDK Setup
   - Run below commands to install JDK AWS Corretto in jenkins container
     ```
      curl -Lo /tmp/corretto-21.tar.gz https://corretto.aws/downloads/latest/amazon-corretto-21-x64-linux-jdk.tar.gz
      tar -xzvf /tmp/corretto-21.tar.gz -C /var/jenkins_home
      rm /tmp/corretto-21.tar.gz
    ```
- ### Tools Configuration
   - Navigate to Manage Jenkins > Tools 
      - Maven setup
      - jdK setup
      - sonarqube setup

- ### Maven Setup
   - Navigate to Manage Jenkins > Managed Files
   -
   -

- ### Shared Library Setup
   - Navigate to Manage Jenkins > System
   -
   -

- ### Create a Pipeline 
   -
   -
   -

- ### Email notification setup
   -
   -
   -








