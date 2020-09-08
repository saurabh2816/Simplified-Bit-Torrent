
Computer Networks CNT5106C -Project 

--------------------------Single-Student Project ------------------------------------------------------------

Name        : Simplified Bit-torrent
IDE            : INtelliJ
Language : JAVA -(Socket API)
------------------------------------------------------------------------------------------------------------------------------

Structure :
To make Project more comprehendable for the user I have implemented different packages and embedded java files for  clients and Mainserver(Fileowner).Hence there are 5 clients packages and one Main server package and one splitter .

--------------------------------------------------------------------------------------------------------------------------------------

Flow:
This project works on a ring topology in which first the Mainserver sends all the clients unique files dynamically .Then all the clients makes a peer to peer connection amongst themselves and send the required files accordingly for each client.Every client has two threads 1)uploadtopeer 2) downloadpeer . Uploadtopeer uploads the chunks to next client selectively by keeping a check on the chunklist on each  client which is maintained in a String array of size (size=no of chunks made by the server while splitting ).While one client acts as upoader ,next client in the ring topolgy starts its downloadpeer and receive the required files from the previous client .This goes on till all the clients receive all the required files.After that merging is done to get the final output file.Finally each client gets original file splitted by the fileowner or Mainserver.

Config file(Ring topology):
-------------------------------------------------------------------------------------------------------------------------------------------------
(Ports)                    Server         client 1     client 2    client 3    client 4       client 5

client 1                   4450           ----            ----             ----                ----              5002
  
client 2                   4450             5000           ----             ----            ----              ----

client 3                   4450              ----            5001           ----            ----              ----

client 4                   4450              ----             ----           11000         ----              ----

client 5                   4450              ----             ----            ----            12000          ----


---------------------------------------------------------------------------------------------------------------------------------------------------------------------

Run :

First put your file in splitfile package and name your file in main server ,counter and the corresponding Merge files for every client with the proper extension . 
Then first run the Mainserver and then run all the clients one after another .
For testing , a Test.zip file is already set in all the required files ....Hit Run straight away ..(Mainserver-->Client1-->Client2-->Client3-->Client4--->Client5)..

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

                                                             
                                             
                                               *******************************END OF README FILE*************************



 




