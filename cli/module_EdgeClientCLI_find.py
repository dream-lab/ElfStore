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

import module_EdgeClientCLI_ls

if os.path.isdir("./DataAndLogs") == False:
    os.mkdir("./DataAndLogs")

## the file logs.txt will be created later
BASE_LOG = "./DataAndLogs/"
FOG_SERVICE = 0
FOG_IP = str()
FOG_PORT = int()

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

    # get list of mbids and corresponding streamids for blocks that match given metadata properties(specified in a json file).
    def getFindBlockUsingQuery(self,metaKeyValueMap):
        timestamp_record = "-1, 700,"+ str(-1)  + ",find block using q,starttime = "+repr(time.time())+","

        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
        mbIdStreamIdMap = client.findBlockUsingQuery(metaKeyValueMap,True,True);

        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + ",1" + '\n'
        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.close()

        return mbIdStreamIdMap

    # get list of mbids and corresponding streamids for blocks that match given metadata properties(specified in a json file).
    def getFindStreamUsingQuery(self,metaKeyValueMap):
        timestamp_record = "-1, 750,"+ str(-1)  + ",find stream using q,starttime = "+repr(time.time())+","

        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
        streamIdSet = client.findStreamUsingQuery(metaKeyValueMap,True,True);

        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + ",1" + '\n'
        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.close()

        return streamIdSet

    # Return the fog client instance to talk to the fog
    def openSocketConnection(self,ip,port, choice):

        #print "ip ",ip," port",port," choice ", "open socket connection"
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
            #print "In the edge service call "
            client = EdgeService.Client(protocol)

        # Connect!
        transport.open()

        #print "came here"

        return client,transport

    #Close connection
    def closeSocket(self,transport):

        #print "closing connection"
        transport.close()



def find(mbid,blockMetaLocation,streamMetaLocation,fogIp,fogPort,verbose = False):
    global FOG_IP
    FOG_IP = fogIp
    global FOG_PORT
    FOG_PORT = int(fogPort)
    ## Trace of this function:
    ## 1. Call lsMbIdSystem from module_EdgeClientCLI_ls with groupBy = 2 (i.e group by mbid)
    ## 2. Get the edge list for the desired mbid, convert it to a set and display.

    ## 1. Call lsMbIdSystem from module_EdgeClientCLI_ls with groupBy = 2 (i.e group by mbid)
    choice = 20
    groupBy=2
    if mbid != None and blockMetaLocation == None and streamMetaLocation == None:
        try:
            mbids = module_EdgeClientCLI_ls.ls(fogIp,fogPort,choice,groupBy,verbose)
            #str(list(set(mbids[mbid])))
            print("Edges: "+str(list(set(mbids[int(mbid)]))))
        except KeyError:
            print("Microbatch not in in the system.")
    elif blockMetaLocation != None and mbid == None and streamMetaLocation == None:
        myEdge = EdgeClient()
        metaKeyValueMap = json.load(open(blockMetaLocation,'r'))
        response = myEdge.getFindBlockUsingQuery(metaKeyValueMap)
        print(response)

    elif streamMetaLocation != None and mbid == None and blockMetaLocation == None:
        myEdge = EdgeClient()
        metaKeyValueMap = json.load(open(streamMetaLocation,'r'))
        response = myEdge.getFindStreamUsingQuery(metaKeyValueMap)
        print(response)
