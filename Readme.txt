This is a peer to peer video streaming.

Command line execution is:
java -jar SimpleStreamer.jar [-sport X] [-remote hostname1,hostname2,...    [-rport Y1,Y2,...]] [-width W] [-height H]

Where sport is the server port to use, defaulting to 6262 if no sport is given, remote specifies a comma separate list of hostname to connect to, rport specifies a comma separated list of port numbers for the remote servers, width and height are the desired image parameters

Example: 
Step 1: Start a Server: java -jar SimpleStreamer.jar
Step 2: Start a Client: java -jar SimpleStreamer.jar -remote localhost -rport 6262