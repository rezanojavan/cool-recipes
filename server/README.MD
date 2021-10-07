# How to build
To build this application you only need to run below command in root of project

`gradlew build`

# How to run
To run application you can use 2 ways. First open the source code in your IDE and run application there 
second use gradle to run it. To use gradle to run application please execute below command in root of project

`gradlew bootRun`

to exit the application just `Ctrl+C`

# Access to swagger
After you run application, you can access swagger by below url
`http://localhost/swagger-ui.html`

# Build docker image 
This application also support docker, and you can build a docker image for this application .
to create a docker image please execute below command in root folder

`docker build -t cool-recipe:latest .`

# Push to docker
Now you can push your image to docker repository. run command:

`docker push cool-recipe:latest`

# Run docker image
After you successfully build docker image, you can now run docker image through below command

`docker run -p80:80 -i -t cool-recipe:latest`

# Test APIs
After you got the server running, you can start testing APIs by a tool like postman. In the project root you will find a 
postman configs that you can easily import and start testing.
1. postman collection file : cool-recipe.postman_collection.json
2. postman environment file : cool-recipe.postman_environment.json

# Deploy project to k8s
There is a helm chart(v3) next to the cool recipe project in the same repo that you can deploy the cool recipe to 
the kubernetes cluster. To deploy to your k8s cluster, go to the chart directory and use below command.

`helm -n [your namespace] --context=[your conetxt] install cool-recipe .`

before you run this command, you need to go over the value file and change some config to your env like image and ingress 
