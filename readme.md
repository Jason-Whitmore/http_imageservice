# HTTP Image Service

This toy project was created to learn the following technologies:

Maven, Spring Boot, Java HTTPClient 

and the following skills:

Server and client development, HTTP


## Running the project

To run the project, the server should be started before running the client.

### Server

To run the server, use the command:

`make run_server`

This will run the server locally with port 8080. As the client interacts with the server, logs will be printed on the server's standard out.


### Client

#### Building the client

To build the client, use the command:

`make build_client`

This will create a jar file (`ServerRunner.jar`) in this root folder.

#### Running the client

To run the client, use the command:

`java -jar ClientRunner.jar [command]`

where `[command]` is the command for the program.

## Commands



### Help

The simplest way to find out what commands are available is to use the `help` command:

`java -jar ClientRunner.jar help`

This will print information to standard out about what commands are available.


### Stats
Usage: ClientRunner stats [image path] [address]
Example: ClientRunner stats image.png http://localhost:8080/
Description: Server will respond with statistics about the image that was sent, including image size,
mean color, and most commonly occurring color.

### Gray
Usage: ClientRunner gray [image path] [address] [output image path]
Example: ClientRunner gray image.png http://localhost:8080/ gray_image.png
Description: Server will respond with the request image converted into grayscale. This output image will be
saved to the specified location.

### Red
Usage: ClientRunner red [image path] [address] [output image path]
Example: ClientRunner red image.png http://localhost:8080/ output.png
Description: Server will respond with the request image's red channel isolated. This output image will be
saved to the specified location. The output image will be in grayscale, with white areas representing strong
red signals, and black areas with weak red signals.

### Green
Usage: ClientRunner green [image path] [address] [output image path]
Example: ClientRunner green image.png http://localhost:8080/ output.png
Description: Server will respond with the request image's green channel isolated. This output image will be
saved to the specified location. The output image will be in grayscale, with white areas representing strong
green signals, and black areas with weak green signals.

### Blue
Usage: ClientRunner blue [image path] [address] [output image path]
Example: ClientRunner blue image.png http://localhost:8080/ output.png
Description: Server will respond with the request image's blue channel isolated. This output image will be
saved to the specified location. The output image will be in grayscale, with white areas representing strong
blue signals, and black areas with weak blue signals.


## Example use

With this example image:

![Example input image](example_input.jpg)


Running `java -jar stats example_input.jpg http://localhost:8080/` will output:

>>Image statistics:

>>Number of rows: 750

>>Number of columns: 1000

>>Number of channels: 3

>>Most common color:

>>Channel 0: 10

>>Channel 1: 18

>>Channel 2: 21

>>Occurrences: 1639

>>Mean color:

>>Channel 0: 15

>>Channel 1: 31

>>Channel 2: 19

Running `java -jar blue example_input.jpg http://localhost:8080/ blue_output.jpg` will output:

![Blue output image](blue_output.jpg)

The blue areas of the image (mainly the sky and water) are are shown as brighter than other parts of the image,
indicating high blue signals.