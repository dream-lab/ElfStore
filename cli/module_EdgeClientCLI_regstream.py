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


STREAM_ID = str()
STREAM_RELIABILITY = float()
FOG_IP = str()
FOG_PORT = int()
START = int()
KMIN = int()
KMAX = int()

## Used when the --v (i.e verbose) is set to false
## The output of the python file of the correspoding command is written here
## so that it does not show up in the CLI
class DummyFile(object):
    def write(self, x):
        pass
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

    def formulateJsonResponse(self,STREAM_ID):
        metadata = self.getStreamMetadata(STREAM_ID)
        endTimeValue = int(); endTimeUpdatable = int();
        if metadata.endTime == None:
            endTimeValue = None; endTimeUpdatable= None;
        else:
            endTimeValue = metadata.endTime.value; endTimeUpdatable = metadata.endTime.updatable;

        jsonResponse = dict()
        jsonResponse.update({
        "streamId" : metadata.streamId,
        "startTime" : {"value":metadata.startTime.value,"updatable":metadata.startTime.updatable},
        "endTime" : {"value": endTimeValue, "updatable" : endTimeUpdatable},
        "reliablility" : {"value":metadata.reliability.value, "updatable":metadata.reliability.updatable},
        "minReplica" : {"value":metadata.minReplica.value, "updatable": metadata.minReplica.updatable},
        "maxReplica" : {"value":metadata.maxReplica.value, "updatable": metadata.maxReplica.updatable},
        "version" : {"value":metadata.version.value, "updatable": metadata.version.updatable},
        "owner" :  {"value":{"NodeIP" : metadata.owner.value.NodeIP , "port" : metadata.owner.value.port}, "updatable": metadata.owner.updatable},
        "otherProperties" : {"update_prop" :  {"updatable" :metadata.otherProperties['update_prop'].updatable , "value" :metadata.otherProperties['update_prop'].value ,"clazz":metadata.otherProperties['update_prop'].clazz}}
        })

        jsonResponse= json.dumps(jsonResponse)
        return jsonResponse


    def getStreamMetadata(self, sid):
        client,transport = self.openSocketConnection(FOG_IP, FOG_PORT, FOG_SERVICE)
        streamMetadataInfo = client.getStreamMetadata(sid, True, True, True)
        self.closeSocket(transport)
        return streamMetadataInfo.streamMetadata

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


    #registers stream for the edge
    def registerStream(self, streamId, startBlockNum):
        print("fog ip, fog port ",FOG_IP,FOG_PORT)
        # streamId = "serc1"

        #streamPayload = StreamMetadata()
        #streamPayload.startTime = time.time()*1000
        #streamPayload.reliability = float(STREAM_RELIABILITY)
        #streamPayload.minReplica = 2
        #streamPayload.maxReplica = 5

        streamMD = StreamMetadata()
        streamMD.streamId = streamId
        streamMD.startTime = I64TypeStreamMetadata(int(time.time()*1000), False)
        streamMD.reliability = DoubleTypeStreamMetadata(float(STREAM_RELIABILITY), True)
        streamMD.minReplica = ByteTypeStreamMetadata(KMIN, True)
        streamMD.maxReplica = ByteTypeStreamMetadata(KMAX, True)
        streamMD.version = I32TypeStreamMetadata(0, True)
        streamMD.otherProperties = dict()
        streamMD.otherProperties["update_prop"] = DynamicTypeStreamMetadata("0", "Integer", True)

        print("The reliability is set to ",streamMD.reliability.value)
        print("The min replica is ",streamMD.minReplica.value , "\nThe max replica is ",streamMD.maxReplica.value)

        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
        timestamp_record = "register stream req,starttime = "+repr(time.time())+","

        response = client.registerStream(streamId, streamMD, startBlockNum)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.close()

        print("the response is ",response)
        self.closeSocket(transport)
        return response


def regStream(streamId,streamReli,fogIp,fogPort,kmin,kmax,verbose = False):
    global STREAM_ID
    STREAM_ID = streamId
    global STREAM_RELIABILITY
    STREAM_RELIABILITY = float(streamReli)
    global FOG_IP
    FOG_IP = fogIp
    global FOG_PORT
    FOG_PORT = int(fogPort)
    global KMIN
    KMIN = int(kmin)
    global KMAX
    KMAX = int(kmax)
    global START
    START = 700
    myEdge = EdgeClient()
    if verbose == True:
        response = myEdge.registerStream(STREAM_ID,START)
        #return myEdge.formulateJsonResponse(STREAM_ID)
    else:
        responseCode = 0
        with nostdout():
            responseCode = myEdge.registerStream(STREAM_ID,START)
        #jsonResponse = myEdge.formulateJsonResponse(STREAM_ID)
        sys.stdout = sys.__stdout__
        if responseCode==1:
            print("success")
        else:
            print("failed")
            print("Response code: "+str(responseCode))
        #return jsonResponse
