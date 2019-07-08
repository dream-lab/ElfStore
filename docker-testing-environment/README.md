### NOTE
* The IP addresses have been changed for the fogs and edges based on the network created using `docker network`.
* The above changes are made in  edge-config-files/ and cluster.conf
* The BASE_LOG value has also been changed in the respective cli module files.
* Put the edgefilesystem-0.1-jar-with-dependencies.jar file, after compilation, in cli/target/ folder.
* This testing environment is currently under development.

### Setup
```
docker network create --driver bridge --subnet 174.10.0.0/16 --gateway 174.10.0.1 elfstore-network
```

```
docker build -t elfstore .
```

* First start the fogs.
```
cd fogs
docker-compose up
```

* Then, in another terminal start the edges.
```
cd edges
docker-compose up
```

* Finally, in  another terminal, login into an edge and start the cli.
```
docker exec -it <edge-container-id> /bin/bash
cd edges/cli
python3.6 elfs_cli.py edge-config-files/edge1_config.json
```
