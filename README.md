# KSnip

Koltin version of my snippets app.  The KSnip app manages a JSON file
collection of snippets, organized by topic. Add snipeets, save the file. Set the 
file as the default in the preferences.  Nothing fancy, just a useful app as a
byproduct of working with Kotlin.

# Build, Run, Create App

```bash
# build
./gradlew clean assemble
# Run
java -jar build/libs/KSnip-1.0-SNAPSHOT.jar
# Create OS X App
./gradlew osxApp
# Run App
open build/app/KSnip.app 
```