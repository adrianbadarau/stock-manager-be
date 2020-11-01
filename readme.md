## Inventory management app

This is a demo app to show the performance of ktor for a fictitious warehouse.

The goal if this app is to keep memory under 50M and have fast startup times

### Running the app
You can run this application using `gradlew run` or build the fat jar `gradlew shadowJar` then go to the `build/libs` directory and run  `ikea_homework-0.0.1-all.jar`

The containerized version of the app was tested in a container with 60MB and it seems to run stable, just run `docker build -t my-application .` and then `docker run -m60M --cpus 2 -it -p 8080:8080 --rm my-application` ,but for best performance it would be safe to give it 80MB+    
