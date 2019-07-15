### Overview
* The following environment consists of a D20 configuration. Which consists of 4 fog containers and 16 edge containers.
* This setup is purely based on docker and is meant for testing and getting familiarized with ElfStore and its command line interface.
* For an more realistic IoT testing environment please refer to [VIoLET](https://github.com/dream-lab/VIoLET).
* Docker Compose 3 is used for this setup.
* The configuration for fogs and edges is present in their respective yaml files. It recommended to start them in separate terminals, without detaching them, as it would give real-time logs of fog and edge servers respectively.
* Since this setup uses docker compose 3 for deploying containers, the storage limit of edge containers is enforced using a tmpfs mount. Because of which, before compiling the project some additional steps have to be performed manually (mentioned in Setup below).
* The fog and edge containers are connected to an external subnetted network and are assigned static IP addresses.
* For testing purposes, a new CLI command, `putlarge`, is added. Using which you can perform perform 'n' number of writes on some test data.

### NOTE
* The IP addresses have been changed for the fogs and edges based on the network created using `docker network`.
* The above changes are made in  edge-config-files/ and cluster.conf
* The BASE_LOG value has also been changed in the respective cli module files.
* This testing environment is currently under development.

### Setup
1.  Update the data path to the following in com.dreamlab.edgefs.edge.model.Edge.java.
```
...
File myFile = new File("/edgedatatemp");
...
```

2. Compile the project using maven.
```
mvn clean compile assembly:single
```

3. Copy the target/edgefilesystem-0.1-jar-with-dependencies.jar to docker-testing-environment/preload/cli/target/

4. Create a network with subnet and gateway specified. (The containers will communicate via this network)

```
docker network create --driver bridge --subnet 174.10.0.0/16 --gateway 174.10.0.1 elfstore-network
```

5. Build the docker image.
```
docker build -t elfstore .
```

6. First start the fogs.
```
cd fogs
docker-compose up
```

7. Then, in a new terminal start the edges.
```
cd edges
docker-compose up
```

8. Finally, in  a new terminal terminal, login into an edge (say edge1) and start the cli.
```
docker exec -it edges_edge1_1 /bin/bash
cd edgefs/cli/
python3.6 elfs_cli.py edge-config-files/edge1_config.json
```
