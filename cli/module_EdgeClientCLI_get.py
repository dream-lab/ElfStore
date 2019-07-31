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

        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
        print("the time stamp for find request is ",timestamp_record)

        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.close()

        compFormat = str()
        uncompSize = int()
        ## if the response contains empty list then the search is terminated here
        if len(response) == 0:
            print("Length of response = 0. Replica not found, terminating here")
            return 0,0
        else:
            ## the microbatch is present in the system.
            ## for obtaining compression format and uncompressed block size. addresses the issues 1. what is the file extension?, 2. how many bytes to read from the stream.
            ## NOTE: this will only return a result if the block metadata is present in the fog of the current partition (i.e the read is a local read)
            ## This operation has a little overhead since it is only performed once. Another reason is in case it is a local read
            ## then an connection to an edge is directly made. But since the edge does not maintain bock metadata map, an explicit connection to
            ## the parent fog would have to be made once again in order to retreive the required metadata info.
            ## Therefore since a connection to the parent fog is already being made here it is better to make a call and  retreive the indormation.
            ## This call is just a wild guess, it may return null. If it is supposed to be a fog read, then, anyways a connection will be made to another fog,
            ## we will fetch the format and size at that point of time.
            ## Also the fog to which the connection will be made (for a fog read) it will definitely have the corresponding block metadata
            compFormatSize = client.requestCompFormatSize(microbatchId);
            print(compFormatSize)
            if len(compFormatSize) !=0:
                ## i.e format and uncompressed size present
                compFormat = list(compFormatSize.keys())[0];
                uncompSize = compFormatSize[compFormat];

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

                 timestamp_record = str(microbatchId)+", "+compFormat+", 25 , "+ str(findReplica.node.nodeId) + " , Read req,starttime = "+repr(time.time())+","
                 response = client.read(microbatchId,0,compFormat,uncompSize) #this is for recovery
                 timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
                 myLogs = open(BASE_LOG+ "logs.txt",'a')
                 myLogs.write(timestamp_record)
                 myLogs.close()
                 #print response
                 print("Read status is ",response.status)
                 if response.status==0 :
                     print("File not found : cannot read file")
                     return 0,0

                 elif response.status==1:
                     #self.formulateJsonResponse(microbatchId,response)
                     bytesRead = len(response.data)
                     print("Local Read ",len(response.data)," number of bytes")
                     print("metadata also read ",response.metadata)
                     return 1,bytesRead #successful read
                 else:
                     return response.code,0

                 transport.close()
             elif(findReplica.node!=None) :

                 fogNode = findReplica.node

                 client,transport = self.openSocketConnection(fogNode.NodeIP,fogNode.port,FOG_SERVICE)
                 timestamp_record = str(microbatchId)+", "+compFormat+", 27 ,"+str(findReplica.node.nodeId)  + ",write req,starttime = "+repr(time.time())+","

                 ## retreiving the compression format and the uncompressed block size for read operation from
                 ## If you have reached here it means that the block is present in another partition and the previous
                 ## 'client.requestCompFormatSize()' would definitely have returned null.
                 ## (Since no block in a partition => no metadata of that block maintained by fog of that particular partition)
                 ## Therefore fetch the format and size with the following call. This call will definitely return an entry.
                 compFormat = str()
                 uncompSize = int()
                 compFormatSize = client.requestCompFormatSize(microbatchId);
                 if len(compFormatSize) !=0:
                     ## i.e format and uncompressed size present
                     compFormat = list(compFormatSize.keys())[0];
                     uncompSize = compFormatSize[compFormat];

                 response = client.read(microbatchId,0,compFormat,uncompSize)
                 timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
                 myLogs = open(BASE_LOG+ "logs.txt",'a')
                 myLogs.write(timestamp_record)
                 myLogs.close()
                 if(response.status == 1):
                     #self.formulateJsonResponse(microbatchId,response)
                     bytesRead = len(response.data)
                     print("Fog Amount of bytes read ",len(response.data))
                     return 1,bytesRead #successful read
                 else:
                     print("The queried fog does not have data")
                     return response.status,0

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
            responseCode,bytesRead = myEdge.findAndRead(i)
            i = i + 1
    else:

        i = START
        while i<=END:
            bytesRead = 0
            responseCode = 0;
            with nostdout():
                responseCode,bytesRead = myEdge.findAndRead(i)
            sys.stdout = sys.__stdout__
            #print "Read response for microbatch "+str(i)+" is : \nResponse = "+str(JSON_RESPONSE[i]['status'])+" \nNo. of bytes read = "+str(len(JSON_RESPONSE[i]['data']))+"\n"
            if responseCode!=1 : print("Read response for microbatch "+str(i)+" is : \nfailure  \nNo. of bytes read = 0\n"+"Response Code: "+str(responseCode))
            else :  print("Read response for microbatch "+str(i)+" is : \nsuccess \nNo. of bytes read = "+str(bytesRead)+"\n")
            i = i + 1

    #jsonResponse = JSON_RESPONSE

    #JSON_RESPONSE = dict()
    #return json.dumps(JSON_RESPONSE)
