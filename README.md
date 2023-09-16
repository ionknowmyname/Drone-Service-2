
# **DRONE SERVICE - JAVA 17 - APACHE MAVEN - POSTGRES DATABASE**

// the relationship between medication and drone is many-to-one, because once a
medication is loaded on a drone, its locked to that drone and cannot be on two drones
at the same time; so not many-to-many

// when creating a medication, industry standards is to save the to a 3rd party and save
the url to the DB so frontend can take it up with CDN to display image via its url. I used
cloudinary, you can decide to use s3 bucket or whichever.

// another less efficient way is saving the image as base64 directly into the DB.


// make uploading in bulk possible, in case they wanna upload in batches


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
