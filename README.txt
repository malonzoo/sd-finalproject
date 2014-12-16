PURPOSE:

This application was done as the final project for CS 215: Software Design.
The goal of the project was to research a particular design element in programming,
write a paper on that design element, and also create a program that uses that
element in action. For my project, I chose to research and implement the publish
and subscribe pattern, after being inspired to learn more about distributed systems
and how social messaging services like Twittter and Facebook Messenger work.

IMPLEMENTATION:
This application uses a client-server architecture to enable an end user (client)
to: 1. sign up as a user to the message service, 2. post messages to its feed,
3. subscribe to the feeds of other users also connected to the server, and 4.
get the messages from the users a person is subscribed to and display those messages
on its individual GUI. On the server side, whomever is running the server on their
computer can see a live feed of all the messages being posted by all the users
connected to the server as they come as well as a list of Users and the other Users
that are subscribed to them.

PUBLISH/SUBSCRIBE ASPECT:
Clients are both publishers and subscribers and each post that is published is sent
to a queue (ArrayDeQueue implementation) of messages, handled by a Topic object,
that is then pushed to all of the user that are subscribed to that Topic.

RUNNING:
1. Compile in parent directory that contains bin and src
   directories by entering this command into the command line:
      javac -d bin src/*.java
1. First start the server.
      java -cp bin MessageServer
2. Run the clients (can open as many as you'd wish)
      java -cp bin MessageClient
