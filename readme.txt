
Computer Networks CNT5106C Fall 2015 -Project 

--------------------------Single-Student Project ----------------------------------------------------------------------------

Name     : Simplified Bit-torrent
IDE      : INtelliJ/Eclipse
Language : JAVA 
------------------------------------------------------------------------------------------------------------------------------

Structure :
To make Project more comprehendable for the user we have implemented different packages and embedded java files
for clients and Mainserver(Server.java). Hence there are:
1).5 clients packages which contain their respective java files and their client handler.java file for thread managment.
2).one server file with its client connection.java file.
3).one splitter package, and
4).one merging package.

-------------------------------------------------------------------------------------------------------------------------------

Flow:
This project works on a ring topology in which first the Mainserver sends all the clients unique files dynamically i.e. at a gap of 5.
Then all the clients makes a peer to peer connection amongst themselves and send the required files accordingly for each client.
Every client has two threads 1)ActUploader()-works on seperate thread 2) Actdownloader()-works on main thread. 
actUploader() uploads the chunks to next client selectively by first sending its chunks available in form of arraylist to its neighbour.

The downloading client in the ring topolgy starts its downloadpeer and compare the list sent by the uploader and request for 
the chunks acccordingly. This goes on till all the clients receive all the required files.
After that merging is done to get the final file. Finally each client gets original file splitted by the fileowner or server.

Config file:
--------------------------------------------------------------------------------------------------------------------------------
		port-no.	chunks Recieved From Server	Client Downloading From 

Server		4444    		----------			--------
Client 1	4001			1-6-11....			client 5    
Client 2	4002			2-7-12....   			client 1
Client 3	4003			3-8-13....   			client 2
Client 4	4004			4-9-14....            		client 3
Client 5	4005			5-10-15...   			client 4

----------------------------------------------------------------------------------------------------------------------------------
Run :

First put your file to be sent in project folder.
Then first run the Mainserver and then run all the clients one after another .
For testing , a file.mp3,file.pdf,file.txt is already place in project folder ....Hit Run straight away ....

----------------------------------------------------------------------------------------------------------------------------------
