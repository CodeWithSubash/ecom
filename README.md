# Development Environment Setup

## Cloning the project
- git clone git@github.com:CodeWithSubash/ecom.git  

## Setup Server (JAVA)

### Pre-requisitie/Tools
- Installl IntelliJ IDEA community edition  
- Jdk 1.8+ is installed
- mysql database (laragon to start the server)
- mysql workbench (to view the db)
- 
### Building server for first time
- Open the project folder \ecom\ecommerce from Intellij
- Verify the mysql db credentials in application-dev.yml
- Set the active profile as dev from application.yml
- Add the configuration 
-- Create new configuration
-- Give name "ecom"
-- Choose main class "EcommerceApplication" from com.softwebdevelopers.ecommerce.EcommerceApplication
-- Select the JRE (the default one from Intellij i.e. jdk-12.0.2)
- Build the project
- Run the project (It may ask for firewall acccess. Select Allow)
-- The success message will come as 'ecommerce' is running! 
-- Access URL: http://localhost:8081
-- Profile : [dev]

### Building client for first time
- Change dir to /client
- Install npm packages as 'npm install' (packages defined in package.json)
- Create .env file and update the server ip address from above step
- Create .env.development file and update the first line as 
-- SERVER_API_URL= https://reqres.in/api
- Run the client app 'npm run dev'
- Ignore the warning 'The dns.promises API is experimental'
- You should observe the
-- Listening: http://localhost:3000/
-- Tailwind Viewer: http://localhost:3000/_tailwind/  (css framework, similar to bootstrap)
