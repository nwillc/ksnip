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

___


[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e9e773f58c024f868b7fdc52c2279635)](https://www.codacy.com/app/nwillc/ksnip?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=nwillc/ksnip&amp;utm_campaign=Badge_Grade)
