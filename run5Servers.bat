set PATH=%PATH%;C:\Program Files\Java\jdk1.8.0_121\bin
javac src/rmiinterface/RMIInterface.java src/rmiinterface/Part.java src/rmiserver/PartRepository.java src/rmiclient/Client.java
cd src
start rmiregistry
java rmiserver.PartRepository Server1
cd src
start rmiregistry
java rmiserver.PartRepository Server2
cd src
start rmiregistry
java rmiserver.PartRepository Server3
cd src
start rmiregistry
java rmiserver.PartRepository Server4
cd src
start rmiregistry
java rmiserver.PartRepository Server5
cd src
java rmiclient.Client