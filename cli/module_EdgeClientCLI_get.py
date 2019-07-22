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


    def formulateJsonResponse(self,mbid,replica):
        metadataClientId = None;
        metadataSessionSecret = None;
        metadataStreamId = None;
        metadataMbId  = None;
        metadataTimeStamp = None;
        metadataChecksum = None;
        metadataProperties = None;
        metadataCompFormat = None;
        metadataUncompSize = None;

        if replica.metadata != None:
            metadataClientId = replica.metadata.clientId
            metadataSessionSecret = replica.metadata.sessionSecret
            metadataStreamId = replica.metadata.streamId
            metadataMbId = replica.metadata.mbId
            metadataTimeStamp = replica.metadata.timestamp
            metadataChecksum = replica.metadata.checksum
            metadataProperties = replica.metadata.properties
            metadataCompFormat  = replica.metadata.compFormat
            metadataUncompSize = replica.metadata,uncompSize;
        mbData  = str(replica.data)
        jsonResponse = {mbid :
         {
        "status": replica.status,
         "data": mbData,
         "metadata":{
            "clientId" : metadataClientId,
            "sessionSecret" : metadataSessionSecret,
            "streamId" : metadataStreamId,
            "mbId" : metadataMbId,
            "timestamp" : metadataTimeStamp,
            "checksum" : metadataChecksum,
            "properties" : metadataProperties,
            "compFormat":metadataCompFormat,
            "uncompSize": metadataUncompSize}
            }
        }
        global JSON_RESPONSE
        JSON_RESPONSE.update(jsonResponse)

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

    #find and read replicas for microbatches
    def findAndRead(self, microbatchId):

        edgeInfoData = EdgeInfoData()
        edgeInfoData.nodeId = EDGE_ID
        edgeInfoData.nodeIp = EDGE_IP
        edgeInfoData.port = EDGE_PORT
        edgeInfoData.reliability = EDGE_RELIABILITY
        edgeInfoData.storage = 12

        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)

        timestamp_record = str(microbatchId)+ ",23, local ,find req,starttime = "+repr(time.time())+","

        response = client.find(microbatchId,True,True,edgeInfoData)
        ## for obtaining compression format. required for performing read as filePath has to be formulated.
        compFormatSize = client.requestCompFormatSize(microbatchId);
        compFormat = list(compFormatSize.keys())[0];
        uncompSize = compFormatSize[compFormat];

        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
        print("the time stamp for find request is ",timestamp_record)

        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.close()

        self.closeSocket(transport)
        print("Sent replicas ",response)


        for findReplica in response :

             edgeInfoData = findReplica.edgeInfo

             if(edgeInfoData!=None):

                 print("edgeInfoRecv from fog ",edgeInfoData)
                 #have to read data from edge

                 transport = TSocket.TSocket(edgeInfoData.nodeIp,edgeInfoData.port)

                 # Buffering is critical. Raw sockets are very slow
                 transport = TTransport.TFramedTransport(transport)

                 # Wrap in a protocol
                 protocol = TBinaryProtocol.TBinaryProtocol(transport)

                 # Create a client to use the protocol encoder
                 client = EdgeService.Client(protocol)

                 # Connect!
                 transport.open()

                 timestamp_record = str(microbatchId)+", 25 , "+ str(findReplica.node.nodeId) + " , Read req,starttime = "+repr(time.time())+","
                 response = client.read(microbatchId,0,compFormat,uncompSize) #this is for recovery
                 timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
                 myLogs = open(BASE_LOG+ "logs.txt",'a')
                 myLogs.write(timestamp_record)
                 myLogs.close()
                 #print response
                 print("Read status is ",response.status)
                 if response.status==0 :
                     print("File not found : cannot read file")

                 else:
                     self.formulateJsonResponse(microbatchId,response)
                     print("Local Read ",len(response.data)," number of bytes")
                     print("metadata also read ",response.metadata)
                     return 1 #successful read

                 transport.close()
             elif(findReplica.node!=None) :

                 fogNode = findReplica.node

                 client,transport = self.openSocketConnection(fogNode.NodeIP,fogNode.port,FOG_SERVICE)

                 timestamp_record = str(microbatchId)+", 27 ,"+str(findReplica.node.nodeId)  + ",write req,starttime = "+repr(time.time())+","
                 response = client.read(microbatchId,0,compFormat,uncompSize)
                 timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
                 myLogs = open(BASE_LOG+ "logs.txt",'a')
                 myLogs.write(timestamp_record)
                 myLogs.close()
                 if(response.status == 1):
                     self.formulateJsonResponse(microbatchId,response)
                     print("Fog Amount of bytes read ",len(response.data))
                     return 1 #successful read
                 else:
                     print("The queried fog does not have data")

                 self.closeSocket(transport)
             else:

                 print("The queried fog does not have data")


def get(start,end,edgeId,edgeIp,edgePort,edgeReliability,fogIp,fogPort, verbose = False):
    if int(end) == -1 : end = start
    global START
    START = int(start)
    global END
    END = int(end)
    global EDGE_ID
    EDGE_ID = int(edgeId)
    global EDGE_IP
    EDGE_IP = edgeIp
    global EDGE_PORT
    EDGE_PORT = int(edgePort)
    global EDGE_RELIABITLITY
    EDGE_RELIABILITY = int(edgeReliability)
    global FOG_IP
    FOG_IP = fogIp
    global FOG_PORT
    FOG_PORT = int(fogPort)
    global JSON_RESPONSE
    myEdge = EdgeClient()

    if verbose == True:
        i = START
        while i<=END:
            myEdge.findAndRead(i)
            i = i + 1
    else:
        i = START
        while i<=END:
            with nostdout():
                myEdge.findAndRead(i)
            sys.stdout = sys.__stdout__
            #print "Read response for microbatch "+str(i)+" is : \nResponse = "+str(JSON_RESPONSE[i]['status'])+" \nNo. of bytes read = "+str(len(JSON_RESPONSE[i]['data']))+"\n"
            if i not in list(JSON_RESPONSE.keys()) : print("Read response for microbatch "+str(i)+" is : \nfailure  \nNo. of bytes read = 0\n")
            else :  print("Read response for microbatch "+str(i)+" is : \nsuccess \nNo. of bytes read = "+str(len(JSON_RESPONSE[i]['data']))+"\n")
            i = i + 1

    jsonResponse = JSON_RESPONSE

    JSON_RESPONSE = dict()
    return json.dumps(jsonResponse)
