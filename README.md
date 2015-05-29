# determinator for Android
An Android app to take fast decisions!

Link to Trello where User Stories exists: https://trello.com/b/ldcY3Bwb/pinomg

The full developer documenation is found in docs/DeveloperDocumentation.pdf

##Getting Started
To get the code for the project, use git:

```
$ git clone https://github.com/PINOMG/determinator.git
```

After the code is copied to the local filesystem, simply open the project in Android Studio and you are good to go. If you use your own server, remember to switch the BASE_URL variable in ApiHandler.java to match your server's URL. Right now the application uses the PINOMG organization’s server at http://79.99.3.112/pinomg/.

##Dependencies
In order to get started, some prerequisites are required. These are amongst others:
- Java SE Development Kit 7
- Android SDK, version 22
- A (virtual) Android phone
- Android studio

And if you want to use your own server:

- PHP and MySQL server for the APIand the instructions to install this is found in separate repository, see API further down.

####SDK
- Target SDK: **22**
- Minimum SDK: **21**

##Building and release
This project uses Gradle to build the application. We recommend that you use Android Studio and the built in Gradle functionality. Building can be done by running (in the root directory of the app): 

```	
$ ./gradlew assemble
```

##Tests
The text based tests available are found in docs/tests.pdf.

The automated unit tests are located in app/src/test directory. To run the unit tests just run the test task (in the root directory of the app):

```
$ ./gradlew test --continue
```

For more info on how the unit tests are set up, see: *http://tools.android.com/tech-docs/unit-testing-support*

##License
The application is released under the MIT license and the full license is found in the LICENSE file.

##Team
- Björn Agaton
- Ebba Mannheimer
- Martin Lindehammar
- Olle Lindeman
- Patrik Olsson
- Philip Xu Cederhill

Built at Chalmers, 2015.