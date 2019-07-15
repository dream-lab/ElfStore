import sys
sys.path.append('./gen-py')
import math
import random
import time
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from fogclient import FogService
from fogclient.ttypes import *
from EdgeServices import EdgeService
from EdgeServices.ttypes import *
import time
import os
import collections
import json
import multiprocessing
from pprint import pprint
import hashlib
import contextlib

if os.path.isdir("/edgefs/logs") == False:
    os.mkdir("/edgefs/logs")

## the file logs.txt will be created later
BASE_LOG = "/edgefs/logs/"
FOG_SERVICE = 0

FOG_IP = str()
FOG_PORT = int()
CHOICE = int()
GROUP_BY = int()

## Used when the --v (i.e verbose) is set to false
## The output of the python file of the correspoding command is written here
## so that it does not show up in the CLI
class DummyFile(object):
    def write(self, x): pass
    def flush(self):
        pass

@contextlib.contextmanager
def nostdout():
    #save_stdout = sys.stdout
    sys.stdout = DummyFile()
    yield
## end of --v context

class EdgeClient:
    def __init__(self):
        self.log = {}

    # Return the fog client instance to talk to the fog
    def openSocketConnection(self,ip,port, choice):

        print("ip ",ip," port",port," choice ", "open socket connection")
        # Make socket
        transport = TSocket.TSocket(ip, port)

        # Buffering is critical. Raw sockets are very slow
        transport = TTransport.TFramedTransport(transport)

        # Wrap in a protocol
        protocol = TBinaryProtocol.TBinaryProtocol(transport)

        # Create a client to use the protocol encoder
        if(choice == FOG_SERVICE):
            client = FogService.Client(protocol)
        else:
            print("In the edge service call ")
            client = EdgeService.Client(protocol)

        # Connect!
        transport.open()

        print("came here")

        return client,transport

    #Close connection
    def closeSocket(self,transport):

        print("closing connection")
        transport.close()

    #ISHAN:
    def getMbIDLocationMapByMbIdLocal(self, mbidMap):
        for mbid,edgeInfo in mbidMap.items():
            for edgeId in edgeInfo.keys():
                mbidMap[mbid] = [edgeId]
        return mbidMap

    def combineMaps(self,map1, map2):
        for id2 in list(map2.keys()):
            if id2 not in list(map1.keys()):
                map1.update({id2: map2[id2]})
            else:
                map1[id2] = map1[id2] + map2[id2]
        return map1

    ## The following function reverse maps the mbIDLocationMap
    def getMbIDLocationMapByEdgeLocal(self,mbidMap):
        mbidMapByEdge = dict()
        for mbid,edgeInfo in list(mbidMap.items()):
             for edgeId in list(edgeInfo.keys()):
                if edgeId not in list(mbidMapByEdge.keys()) : mbidMapByEdge.update({edgeId : []})

                mbidMapByEdge[edgeId].append(mbid)
        return mbidMapByEdge


    ## groupBy = 1 means that group by edgeId
    ## groupBy = 2 means that group by blockId
    ## this is used for testing only
    def lsLocal(self,groupBy):
        if groupBy == 1:
            client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
            mbidMapByEdge = self.getMbIDLocationMapByEdgeLocal(client.requestMbIDLocationMap())
            return mbidMapByEdge
            self.closeSocket(transport)
        if groupBy == 2:
            client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
            mbidMapByMbId = self.getMbIDLocationMapByMbIdLocal(client.requestMbIDLocationMap())
            return mbidMapByMbId

    ## list mbids for the neighbors of the called fog
    def lsNeighbors(self,groupBy):
        if groupBy == 1:
            client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
            neighbors = client.requestAllNeighbors();
            mbidMapByEdge = dict()
            for neighbor in neighbors:
                clientNeighbor,transportNeighbor = self.openSocketConnection(neighbor.nodeInstance.NodeIP,neighbor.nodeInstance.port,FOG_SERVICE)
                print("Reading  mbids from the neighbor ",neighbor.nodeInstance.NodeIP," ",neighbor.nodeInstance.port)
                neighborMbIdByEdge = self.getMbIDLocationMapByEdgeLocal(clientNeighbor.requestMbIDLocationMap())
                mbidMapByEdge = self.combineMaps(mbidMapByEdge, neighborMbIdByEdge)
                self.closeSocket(transportNeighbor)

            self.closeSocket(transport)
            return mbidMapByEdge
        if groupBy == 2:
            client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
            neighbors = client.requestAllNeighbors();
            mbidMapByMbId = dict()
            for neighbor in neighbors:
                clientNeighbor,transportNeighbor = self.openSocketConnection(neighbor.nodeInstance.NodeIP,neighbor.nodeInstance.port,FOG_SERVICE)
                print("Reading  mbids from the neighbor ",neighbor.nodeInstance.NodeIP," ",neighbor.nodeInstance.port)
                neighborMbIdByMbId = self.getMbIDLocationMapByMbIdLocal(clientNeighbor.requestMbIDLocationMap())
                mbidMapByMbId = self.combineMaps(mbidMapByMbId, neighborMbIdByMbId)
                self.closeSocket(transportNeighbor)

            self.closeSocket(transport)
            return mbidMapByMbId

    ## list mbids for the neighbors of the called fog
    def lsBuddies(self,groupBy):
        if groupBy == 1:
            client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
            buddies = client.getBuddyPoolMembers();
            mbidMapByEdge = dict()
            for buddy in buddies:
                clientBuddy,transportBuddy = self.openSocketConnection(buddy.nodeInstance.NodeIP, buddy.nodeInstance.port, FOG_SERVICE)
                print("Reading mbids from the buddy ",buddy.nodeInstance.NodeIP," ",buddy.nodeInstance.port)
                buddyMbIdByEdge = self.getMbIDLocationMapByEdgeLocal(clientBuddy.requestMbIDLocationMap())
                mbidMapByEdge = self.combineMaps(mbidMapByEdge, buddyMbIdByEdge)
                self.closeSocket(transportBuddy)

            return mbidMapByEdge

        if groupBy == 2:
            client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
            buddies = client.getBuddyPoolMembers();
            mbidMapByMbId = dict()
            for buddy in buddies:
                clientBuddy,transportBuddy = self.openSocketConnection(buddy.nodeInstance.NodeIP, buddy.nodeInstance.port, FOG_SERVICE)
                print("Reading mbids from the buddy ",buddy.nodeInstance.NodeIP," ",buddy.nodeInstance.port)
                buddyMbIdByMbId = self.getMbIDLocationMapByMbIdLocal(clientBuddy.requestMbIDLocationMap())
                mbidMapByMbId = self.combineMaps(mbidMapByMbId, buddyMbIdByMbId)
                self.closeSocket(transportBuddy)

            return mbidMapByMbId

    ## list the mbids of the whole system

    def lsMbIdSystem(self,groupBy):
    	## Trace overview:
    	## 1. Get mbids from the the current fog to which the client made a call
    	## 2. Get mbids from the current fogs' neighbors
    	## 3. Get mbids from the current fogs' buddies(i.e the buddy's local partition) and their corresponding neighbors.

        if groupBy == 1:
            mbidMapByEdgeSystem = dict()
            ## 1. Get mbids from the the current fog to which the client made a call
            client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
            print("Read mbids from the fog ",FOG_IP," ",FOG_PORT)
            mbidMapByEdgeLocal = self.getMbIDLocationMapByEdgeLocal(client.requestMbIDLocationMap())
            mbidMapByEdgeSystem = self.combineMaps(mbidMapByEdgeSystem,mbidMapByEdgeLocal)

            ## 2. Get mbids from the current fogs' neighbors
            neighbors = client.requestAllNeighbors();
            for neighbor in neighbors:
                clientNeighbor,transportNeighbor = self.openSocketConnection(neighbor.nodeInstance.NodeIP,neighbor.nodeInstance.port,FOG_SERVICE)
                print("Reading  mbids from the neighbor ",neighbor.nodeInstance.NodeIP," ",neighbor.nodeInstance.port)
                neighborMbIdByEdge = self.getMbIDLocationMapByEdgeLocal(clientNeighbor.requestMbIDLocationMap())
                mbidMapByEdgeSystem = self.combineMaps(mbidMapByEdgeSystem, neighborMbIdByEdge)
                self.closeSocket(transportNeighbor)

            ## 3. Get mbids from the current fogs' buddies and their corresponding neighbors.
            print("Reading mbids from the buddies and thier corresponding neighbors.")
            buddies = client.getBuddyPoolMembers();
            for buddy in buddies:
                mbidMapByEdgeSystem = self.combineMaps(mbidMapByEdgeSystem,self.lsMbIdBuddyNeighbor(buddy,1))
            print("The mbids in the whole system are : ")
            return mbidMapByEdgeSystem

        if groupBy == 2:
            mbidMapByMbIdSystem = dict()
            ## 1. Get mbids from the the current fog to which the client made a call
            client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
            print("Read mbids from the fog ",FOG_IP," ",FOG_PORT)
            mbidMapByMbIdLocal = self.getMbIDLocationMapByMbIdLocal(client.requestMbIDLocationMap())
            mbidMapByMbIdSystem = self.combineMaps(mbidMapByMbIdSystem,mbidMapByMbIdLocal)

            ## 2. Get mbids from the current fogs' neighbors
            neighbors = client.requestAllNeighbors();
            for neighbor in neighbors:
                clientNeighbor,transportNeighbor = self.openSocketConnection(neighbor.nodeInstance.NodeIP,neighbor.nodeInstance.port,FOG_SERVICE)
                print("Reading  mbids from the neighbor ",neighbor.nodeInstance.NodeIP," ",neighbor.nodeInstance.port)
                neighborMbIdByMbId = self.getMbIDLocationMapByMbIdLocal(clientNeighbor.requestMbIDLocationMap())
                mbidMapByMbIdSystem = self.combineMaps(mbidMapByMbIdSystem, neighborMbIdByMbId)
                self.closeSocket(transportNeighbor)

            ## 3. Get mbids from the current fogs' buddies(i.e the buddy's local partition) and their corresponding neighbors.
            print("Reading mbids from the buddies and thier corresponding neighbors.")
            buddies = client.getBuddyPoolMembers();
            for buddy in buddies:
                mbidMapByMbIdSystem = self.combineMaps(mbidMapByMbIdSystem,self.lsMbIdBuddyNeighbor(buddy,2))

            return mbidMapByMbIdSystem


    ## this function return the mbids of the buddies and its neighbours
    ## flag is a dummy variable

    def lsMbIdBuddyNeighbor(self,buddy,groupBy):
        ## Trace overview:
    	## 1. Get mbids from the the buddy
    	## 2. Get mbids from the buddy's neighbors
        if groupBy == 1:
            mbidMapByEdge = dict()

            ## 1. Get mbids from the the buddy
            print("Reading  mbids from the local partition of the buddy ",buddy.nodeInstance.NodeIP," ",buddy.nodeInstance.port)
            clientBuddy,transportBuddy = self.openSocketConnection(buddy.nodeInstance.NodeIP, buddy.nodeInstance.port, FOG_SERVICE)
            buddyMbIdByEdge = self.getMbIDLocationMapByEdgeLocal(clientBuddy.requestMbIDLocationMap())
            mbidMapByEdge = self.combineMaps(mbidMapByEdge, buddyMbIdByEdge)

            ## 2. Get mbids from the buddy's neighbors
            neighbors = clientBuddy.requestAllNeighbors();
            for neighbor in neighbors:
                clientNeighbor,transportNeighbor = self.openSocketConnection(neighbor.nodeInstance.NodeIP,neighbor.nodeInstance.port,FOG_SERVICE)
                print("Reading  mbids from the buddy's neighbor ",neighbor.nodeInstance.NodeIP," ",neighbor.nodeInstance.port)
                neighborMbIdByEdge = self.getMbIDLocationMapByEdgeLocal(clientNeighbor.requestMbIDLocationMap())
                mbidMapByEdge = self.combineMaps(mbidMapByEdge, neighborMbIdByEdge)
                self.closeSocket(transportNeighbor)

            self.closeSocket(transportBuddy)
            return mbidMapByEdge

        if groupBy == 2:
            mbidMapByMbId = dict()

            ## 1. Get mbids from the the buddy
            print("Reading  mbids from the local partition of the buddy ",buddy.nodeInstance.NodeIP," ",buddy.nodeInstance.port)
            clientBuddy,transportBuddy = self.openSocketConnection(buddy.nodeInstance.NodeIP, buddy.nodeInstance.port, FOG_SERVICE)
            buddyMbIdByMbId = self.getMbIDLocationMapByMbIdLocal(clientBuddy.requestMbIDLocationMap())
            mbidMapByMbId = self.combineMaps(mbidMapByMbId, buddyMbIdByMbId)

            ## 2. Get mbids from the buddy's neighbors
            neighbors = clientBuddy.requestAllNeighbors();
            for neighbor in neighbors:
                clientNeighbor,transportNeighbor = self.openSocketConnection(neighbor.nodeInstance.NodeIP,neighbor.nodeInstance.port,FOG_SERVICE)
                print("Reading  mbids from the buddy's neighbor ",neighbor.nodeInstance.NodeIP," ",neighbor.nodeInstance.port)
                neighborMbIdByMbId = self.getMbIDLocationMapByMbIdLocal(clientNeighbor.requestMbIDLocationMap())
                mbidMapByMbId = self.combineMaps(mbidMapByMbId, neighborMbIdByMbId)
                self.closeSocket(transportNeighbor)

            self.closeSocket(transportBuddy)
            return mbidMapByMbId

## This function prints the mbid map (wither grouped by edge or by mbid) in a
## slightly prettier format rather than just dumping the dictionary

def printMbidMap(mbids,groupBy):
    ## For groupBy = 1 (i.e group by edge), the format resembles as follows :
    ## Edge 1:
    ## mbId1 mbid2 mbid3
    ## Edge 2:
    ## mbid1 mbid2 mbid3
    ## ...
    if groupBy == 1:
        for edgeId in mbids.keys():
            print("Edge "+str(edgeId)+":")
            mbids[edgeId].sort()
            print(" ".join(str(x) for x in set(mbids[edgeId])) + "\n")

    ## For groupBy = 2 (i.e group by mbid), the format resembles as follows :
    ## Microbatch 1: []
    ## Microbatch 2: []
    ## ...
    else:
        ## The edges are in the form of a list ,it has to be converted to a set
        ## before printing. This is because there may be redundant edgeId values
        ## for each mbId key value.
        for mbId in mbids.keys():
            mbids[mbId].sort()
            print("Microbatch "+str(mbId)+": "+str(list(set(mbids[mbId]))) + "\n")


def ls(fogIp,fogPort,choice,groupBy,verbose = False):
    global FOG_IP
    FOG_IP = fogIp
    global FOG_PORT
    FOG_PORT = int(fogPort)
    global CHOICE
    CHOICE = int(choice)
    global GROUP_BY
    GROUP_BY = int(groupBy)

    myEdge = EdgeClient()

    ## List mbids only for the owner's buddies and owner's neighbors. (** This does not include the local partition)
    if(CHOICE == 14):
        if verbose ==True:
            mbids = myEdge.lsNeighbors(GROUP_BY)
            mbids = myEdge.combineMaps(mbids,myEdge.lsBuddies(GROUP_BY))
        else:
            with nostdout():
                mbids = myEdge.lsNeighbors(GROUP_BY)
                mbids = myEdge.combineMaps(mbids,myEdge.lsBuddies(GROUP_BY))
            sys.stdout = sys.__stdout__
        print("The mbids present in the neighbors and the buddy pool of the current fog "+FOG_IP+" "+str(FOG_PORT)+" are :")
        printMbidMap(mbids,groupBy)
        return json.dumps(mbids)

    ## list mbids for owner fog's neighbors
    if(CHOICE == 15):
        if verbose ==True: mbids = myEdge.lsNeighbors(GROUP_BY)
        else:
            with nostdout():
                mbids = myEdge.lsNeighbors(GROUP_BY)
            sys.stdout = sys.__stdout__
        print("The mbids present in the neighbors of the current fog "+FOG_IP+" "+str(FOG_PORT)+" are :")
        printMbidMap(mbids,groupBy)
        return json.dumps(mbids)

    ## list mbids for owner fog's buddies
    if(CHOICE == 16):
        if verbose ==True: mbids = myEdge.lsBuddies(GROUP_BY)
        else:
            with nostdout():
                mbids = myEdge.lsBuddies(GROUP_BY)
            sys.stdout = sys.__stdout__
        print("The mbids present in the buddy pool of the current fog "+FOG_IP+" "+str(FOG_PORT)+" are :")
        printMbidMap(mbids,groupBy)
        return json.dumps(mbids)

    ## list mbids for local partition
    if(CHOICE == 18):
        mbids = myEdge.lsLocal(GROUP_BY)
        return json.dumps(mbids)
    ## list mbids for the whole system
    if(CHOICE == 19):
        if verbose ==True: mbids = myEdge.lsMbIdSystem(GROUP_BY)
        else:
            with nostdout():
                mbids = myEdge.lsMbIdSystem(GROUP_BY)
            sys.stdout = sys.__stdout__
        print("The mbids in the whole system are : ")
        printMbidMap(mbids,groupBy)
        return json.dumps(mbids)

    ## this will only be used by module_EdgeClientCLI_find.find()
    if(CHOICE == 20):
        if verbose ==True: mbids = myEdge.lsMbIdSystem(GROUP_BY)
        else:
            with nostdout():
                ## This command uses the lsMbIdSystem function from the module_EdgeClientCLI_ls script.
                ## Since we require the edgeids where the desired mbid is present we need to group the
                ## map by mbids (i.e groupBy = 2)
                mbids = myEdge.lsMbIdSystem(2)
            sys.stdout = sys.__stdout__
        return mbids
