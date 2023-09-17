
# **DRONE SERVICE - JAVA 17 - APACHE MAVEN - POSTGRES DATABASE**

---
## PROJECT IMPLEMENTATION SUMMARY

There are two branches: **main** and **develop**:

- Main branch is implemented to use Drone & Medication entities. On loading and unloading a drone, the medications are
added to the medications list for that drone, and the medications themselves are tied to that drone using the drone
serial
- Develop branch uses Drone, Medication & DroneMedication entities. On loading and unloading a drone, a DroneMedication
is either created or deleted. No relationship between the 3 entities, and the only thing linking a medication that's 
loaded to a drone is the drone serial and medication id in DroneMedication


---
## SETUP

### 1. LOCAL
- Start Postgres DB via Docker with command below:
```
docker run -d \
  --name my_postgres_container \
  -e POSTGRES_DB=drone-service-db-2 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -v /Users/faithfulolaleru/Documents/Volumes:/var/lib/postgresql/data:rw \
  -p 5433:5432 \
  postgres:latest
```
Don't forget to change faithfulolaleru to your username. I put it in your user documents so its easier to track 
your data on your local machine

- Run `mvn spring-boot:run` in terminal. Service starts on port 8080

### 2. DOCKER
- cd into this directory and run command below to build docker image:
 ```
docker build -t drone-service:1.0 .
```
- Run docker-compose.yaml with the command below:
```
docker-compose up -d
```

### NOTE:

- For Cloudinary, I pushed my personal account details with project. Once it hits Gitlab, Cloudinary might change my 
details. Feel free to replace with your details in the .env file 

---
## BUILD PROCESS

- The relationship between medication and drone is many-to-one, because once a
medication is loaded on a drone, its locked to that drone and cannot be on two drones
at the same time; so not many-to-many

- When creating a medication, industry standards is to save the file to a 3rd party and save
the url to the DB so frontend can take it up with CDN to display image via its url. I used
cloudinary, you can decide to use s3 bucket or whichever.

- Another less efficient way is saving the image as base64 directly into the DB.

- make uploading in bulk possible, in case they want to upload in batches

- Industry practices prefer that you put the file upload into its own service i.e CloudinaryService,
and just call while creating a medication; but this works fine as well.

---

## EXTRAS

- I added my postman collection, you can use it to bootstrap yours
