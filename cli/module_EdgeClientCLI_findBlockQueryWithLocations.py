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

if os.path.isdir("./DataAndLogs") == False:
    os.mkdir("./DataAndLogs")

## the file logs.txt will be created later
BASE_LOG = "./DataAndLogs/"
FOG_SERVICE = 0


START = int()
END = int()
EDGE_ID = int()
EDGE_IP = str()
EDGE_PORT = int()
EDGE_RELIABILITY = int()
FOG_IP = str()
FOG_PORT = int()
JSON_RESPONSE = dict()

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


## here a global variable for json response is required since there will be
## multiple entries for each blockId
## contains instances for ReadReplica structure (defined in the EdgeServices.thrift file)


class EdgeClient:
    def __init__(self):
        self.log = {}

        # Return the fog client instance to talk to the fog
    def openSocketConnection(self, ip, port, choice):

        print("ip ", ip, " port", port, " choice ", "open socket connection")
        # Make socket
        transport = TSocket.TSocket(ip, port)

        # Buffering is critical. Raw sockets are very slow
        transport = TTransport.TFramedTransport(transport)

        # Wrap in a protocol
        protocol = TBinaryProtocol.TBinaryProtocol(transport)

        # Create a client to use the protocol encoder
        if (choice == FOG_SERVICE):
            client = FogService.Client(protocol)
        else:
            print("In the edge service call ")
            client = EdgeService.Client(protocol)

        # Connect!
        transport.open()
        return client, transport

    # Close connection
    def closeSocket(self, transport):

        print("closing connection")
        transport.close()

    def createEdgeInfoDataObject(self, edgeID, edgeIP, edgePort, reliability):

        edgeInfoData = EdgeInfoData()
        edgeInfoData.nodeId = int(edgeID)
        edgeInfoData.nodeIp = edgeIP
        edgeInfoData.port = int(edgePort)
        edgeInfoData.reliability = int(reliability)
        edgeInfoData.storage = 12

        return edgeInfoData

    def findBlockQueryWithLocation(self, edgeID,edgeIP,edgePort,edgeReliability,fogIP,fogPort, metakeyvalMap, findQueryCondition, replicaCount):

        client, transport = self.openSocketConnection(fogIP, fogPort, FOG_SERVICE)
        edgeInfoData = self.createEdgeInfoDataObject(edgeID, edgeIP, edgePort, edgeReliability)
        # print("matchPref ",MatchPreference._NAMES_TO_VALUES[matchPref])
        # print("replicaCount ", ReplicaCount._NAMES_TO_VALUES[replicaCount])
        replicaCount = ReplicaCount._NAMES_TO_VALUES[replicaCount]
        print("Metakeyvalue map ",metakeyvalMap)
        result = client.findBlocksAndLocationsWithQuery(metakeyvalMap, True, True, findQueryCondition, replicaCount,edgeInfoData)
        print("The result => ",result)

'''
getmetadata for a microbatch
'''
def findBlockQueryWithLocations(edgeID,edgeIP,edgePort,edgeReliability,fogIP,fogPort, metakeyvalMap, findQueryCondition, replicaCount):

    myEdge = EdgeClient()
    myEdge.findBlockQueryWithLocation(edgeID, edgeIP, edgePort, edgeReliability,fogIP,fogPort, metakeyvalMap, findQueryCondition, replicaCount)
