# Spotify-Project

## Introduction
- link to project doc
This is the final project of our [**Advanced Programming**](https://github.com/Advanced-Programming-1401) course in Shahid Beheshti University. It's a clone of the famous music-streaming application **Spotify** designed using **Java** programming language.<br>
From [here](), you can acces the project documentation as well.









## Pre Requirements
- Java
- SQLITE
- JavaFX
- Netbeans / IntelliJ as an IDE
- Ant / Gradle as a build system for the project
  
## UML Design 
![SpotifyClient](https://github.com/Shyshfa/Spotify-Project/assets/118434072/c68d5c71-412c-4045-bb03-18573f161575)
![SpotifyServer](https://github.com/Shyshfa/Spotify-Project/assets/118434072/69f36772-b8c0-422e-8cb2-dcb1d85f1836)










## Implementation
The project has 2 main part:<br>
1. `SpotifyClient` <br>
2. `SpotifyServer` package which handles the requests sent from the client and responds properly. It's worth of mention that dealing with database is on the server-side of the project as well.
- `SpotifyClient`
This is the package that is considered to be the interface/app layer of the project. It's repsonsible for getting input from a client, sending it through a socket to the server and waiting for a proper response. 
- `SpotifyServer`
The server is responsible for reading the requests from each clinet, either respoend directly or acces the database using `DBManager` class methods and then, responding back to the client. It's worth noting that the server is multithreaded, which means it can handle dealing with multiple reqeusets as well. Also a server log a printed to the terminal to show what the server is doint at the moment.
- git issues
- hashing passwords
- the `hash` method in `SpotifyServer` class
- using sockets to transfer data

## Creating GUI
The main tool for designing graphical user interface for this project is [JavaFX](https://en.wikipedia.org/wiki/JavaFX).

## server 
- server log
- socket programming

## database structures



## Presentation 
[This](https://docs.google.com/presentation/d/15Pqmx_Ug9YLieOzJ7dPYiVqc6WX6ZsMPkJ6riUVkjj8/edit?usp=sharing) is the link to the slides used for project presentation.


## How to run the code?



## Contributers
- Course instructor: [Dr.Saeed Reza Kheradpisheh](https://www.linkedin.com/in/saeed-reza-kheradpisheh-7a0b18155/)
- Project mentor: [Mobin Nesari](https://www.linkedin.com/in/mobin-nesari/)
- Project judge: [Arsham Gholamzadeh](https://www.linkedin.com/in/arsham-khoee/)
- Design:<br>
[Rana Rokni](https://www.linkedin.com/in/rana-r-a5542420a/)
[Farid Karimi](https://www.linkedin.com/in/farid-karimi-30456a257/)
[Shayan Shahrabi Farahani](https://www.linkedin.com/in/shayanshahrabi/)

## date/time
Spring of 2023 (Version 1.0)

## Resources
- Managing database tables with [Extends Class](https://extendsclass.com/)
- [JavaFX tutorial](https://youtube.com/playlist?list=PLxaMIx7eqffLc9mkqFoBFANcZmJVBtzvp)
- [Another JavaFX tutorial](https://youtube.com/playlist?list=PLZPZq0r_RZOM-8vJA3NQFZB7JroDcMwev)
