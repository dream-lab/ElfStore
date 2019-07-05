import subprocess
import os
import sys
import json

PATH = sys.argv[1]

devnull = open(os.devnull,'w')

## Check for data and logs folder
if os.path.isdir("./DataAndLogs") == False : os.mkdir("./DataAndLogs")

edgeConfigFiles = os.listdir(PATH)

print("Starting the edges...")

for configFile in edgeConfigFiles:
    edge = json.load(open(PATH+"/"+configFile,'r'))
    ## Check for dataAndLogs folder for the corresponding edge
    dataFolder = "./DataAndLogs/edge"+str(edge['edgeId'])+"_data"
    if os.path.isdir(dataFolder) == False : os.mkdir(dataFolder)
    print("Starting Edge "+str(edge['edgeId']))
    ## the command is platform specific
    if sys.platform[0:3] == "win":
        command = "start /B java -cp target/edgefilesystem-0.1-jar-with-dependencies.jar com.dreamlab.edgefs.edge.server.EdgeServer "+str(edge["edgeId"])+" "+edge["edgeIp"]+" "+str(edge["edgePort"])+" "+str(edge["reliability"])+" "+edge["fogIp"]+" "+str(edge["fogPort"])+" "+dataFolder+" "+edge["baseLog"]
    else:
        command = "start java -cp target/edgefilesystem-0.1-jar-with-dependencies.jar com.dreamlab.edgefs.edge.server.EdgeServer "+str(edge["edgeId"])+" "+edge["edgeIp"]+" "+str(edge["edgePort"])+" "+str(edge["reliability"])+" "+edge["fogIp"]+" "+str(edge["fogPort"])+" "+dataFolder+" "+edge["baseLog"]+" &"
    subprocess.call(command,shell=True,stdout = devnull,stderr = devnull)

print("Done.")
