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
import psutil
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
#this is assigned when the open() api succeeds
SESSION_SECRET = 'test'
#estimating the putNext() call to succeed in 10 seconds
ESTIMATE_PUT_NEXT = 20*1000
#local time when the lease expires
TIME_LEASE_EXPIRE = 0
STREAM_OWNER_FOG_IP = "127.0.0.1"
STREAM_OWNER_FOG_PORT = 9090
#whether the client currently holds the lock
IS_LOCK_HELD = False
#estimating the incrementBlockCount() call to succeed in 5 seconds
ESTIMATE_INCR_BLOCK_COUNT = 10*1000



PATH = str()
STREAM_ID = str()
START = int()
FOG_IP = str()
FOG_PORT = int()
EDGE_ID = int()
CLIENT_ID = str()
STREAM_RELIABILITY = float()
## Write file(s) as one block
SPLIT_CHOICE = int()
## default block size is 10MB
DEFAULT_BLOCK_SIZE = 10000000
SET_LEASE = bool()
#duration for expected lease; specified by user; used during a put
#The default value is set to 0 here; if it is found to be <=0 at the server side
#then it is set to 90 seconds in FogServiceHandler
EXPECTED_LEASE = int()
# format for compression of blocks before storing them on edges
COMP_FORMAT = str()
UNCOMP_SIZE = int()

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

    def getStreamMetadataReliability(self, sid):
        client,transport = self.openSocketConnection(FOG_IP, FOG_PORT, FOG_SERVICE)
        streamMetadataInfo = client.getStreamMetadata(sid, True, True, True)
        self.closeSocket(transport)
        return streamMetadataInfo.streamMetadata.reliability.value

    #fetching stream metadata has an id of 250
    def getStreamMetadata(self, sid):
        print("Going to fetch stream metadata")
        timestamp_record = "-1, 250,"+ str(-1)  + ",stream md fetch,starttime = "+repr(time.time())+","
        client,transport = self.openSocketConnection(FOG_IP, FOG_PORT, FOG_SERVICE)
        streamMetadataInfo = client.getStreamMetadata(sid, True, True, True)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
        self.closeSocket(transport)

        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.close()

        return streamMetadataInfo.streamMetadata

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

    #open stream has an id of 350
    def openStream(self, stream_id, client_id, expected_lease, blockId,setLease, streamOwnerFog):
        print("Going to open stream for writing")
        #stream can only be opened by calling open() on the stream owner
        #for writes, initially we will use getStreamMetadata() to get the owner
        #which can be cached so that the after successful completion of a write,
        #we can invoke incrementBlockCount on the owner directly and further writes
        #can also use the cached information till the lease is valid

        timestamp_record = str(blockId) + ", 350,"+ str(-1)  + ",open stream,starttime = "+repr(time.time())+","
        metadata = self.getStreamMetadata(stream_id)
        ownerFog = metadata.owner.value
        global STREAM_OWNER_FOG_IP
        STREAM_OWNER_FOG_IP = ownerFog.NodeIP
        global STREAM_OWNER_FOG_PORT
        STREAM_OWNER_FOG_PORT = ownerFog.port

        client,transport = self.openSocketConnection(streamOwnerFog.NodeIP, streamOwnerFog.port, FOG_SERVICE)

        global TIME_LOCK_ACQUIRED
        global TIME_LEASE_EXPIRE
        global IS_LOCK_HELD

        while True:
            #type of response is OpenStreamResponse
            response = client.openLease(stream_id, client_id, expected_lease,setLease)
            if response.status == 1:
                TIME_LOCK_ACQUIRED = int(time.time() * 1000)
                TIME_LEASE_EXPIRE = TIME_LOCK_ACQUIRED + response.leaseTime
                IS_LOCK_HELD = True
                break
            else:
                time.sleep(1)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'

        self.closeSocket(transport)

        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.close()

        global SESSION_SECRET
        SESSION_SECRET = response.sessionSecret
        return response

    '''
    give away the acquired lock for the stream
    '''
    def closeStream(self, stream_id, client_id, blockId, streamOwnerFog):
        print("Relinquishing stream lock")

        timestamp_record = str(blockId) + ", 350,"+ str(-1)  + ",open stream,starttime = "+repr(time.time())+","
        metadata = self.getStreamMetadata(stream_id)
        ownerFog = metadata.owner.value
        global STREAM_OWNER_FOG_IP
        STREAM_OWNER_FOG_IP = ownerFog.NodeIP
        global STREAM_OWNER_FOG_PORT
        STREAM_OWNER_FOG_PORT = ownerFog.port

        client,transport = self.openSocketConnection(streamOwnerFog.NodeIP, streamOwnerFog.port, FOG_SERVICE)

        global TIME_LOCK_ACQUIRED
        global TIME_LEASE_EXPIRE
        global IS_LOCK_HELD

        while True:
            #type of response is OpenStreamResponse
            response = client.closeLease(stream_id, client_id)
            if response.status == 1:                
                IS_LOCK_HELD = False
                break
            else:
                time.sleep(1)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'

        self.closeSocket(transport)

        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.close()

        global SESSION_SECRET
        SESSION_SECRET = response.sessionSecret
        return response

    '''
    Insert the homeFog for the block that is being inserted
    Useful during the update() api
    '''
    def insert_home_fog_for_block(self, microbatchID, streamID, homeFog):
        my_client, transport = self.openSocketConnection(FOG_IP, FOG_PORT, FOG_SERVICE)
        response = my_client.insertHomeFog(microbatchID, streamID, homeFog)
        print("The response is ", response)
        self.closeSocket(transport)

    '''
    Performs a remote api call to a Fog, to retrieve the streamId Owner
    returns : NodeInfoData which is the owner Fog of the stream
    '''
    def find_stream_owner(self, streamId):
        my_client, transport = self.openSocketConnection(FOG_IP, FOG_PORT, FOG_SERVICE)
        streamOwnerFog = my_client.findStreamOwner(streamId, True, True)
        print("Stream owner Fog is ",streamOwnerFog)
        self.closeSocket(transport)
        return streamOwnerFog

    #utility function to return the systems's utilizaion and free space
    def returnDiskSpace(self):
        ## psutil (process and system utilities) is a cross-platform library for
        ## retrieving information on storage running processes.
        if( hasattr(psutil,'disk_usage')):
            total = psutil.disk_usage('/').total
            free = psutil.disk_usage('/').free
            used = psutil.disk_usage('/').used
            print("Disk ",free," : ",total," : ",used)

            util = used/float(total)*100
            disk_space_in_MB = float(free/(1024*1024.0))
            return disk_space_in_MB,util

        return 0 #default return value

    #Encode free space available in disk, (7-bit) 1st bit for GB/MB order, 2nd bit for encoding , 5 bits for units
    def encodeFreeSpace(self, space):

        disk_space_in_MB,util = self.returnDiskSpace() #127,.20
        print("space requested ",space)


        disk_space_in_MB = space/(1024*1024) #remove this before using

        # print "Free space in GB ",int(free_space_GB)," Free Space in MB ",int(free_space_MB), " Free Space in KB ",int(free_space_KB)
        print("Requested space in MB ",int(disk_space_in_MB))

        disk_space = 0
        free_space_GB = disk_space_in_MB/1000

        if(int(free_space_GB)>66):
            disk_space = 127

        #this is for 11GB to 64GB
        if(free_space_GB >=11.0 and free_space_GB<=66.0):
            disk_space = free_space_GB + 61

        #this is for 1100MB to 10900MB
        if(free_space_GB>=1.0 and free_space_GB < 11.0):
            disk_space_in_100_MB = (disk_space_in_MB/100.0)
            disk_space = disk_space_in_100_MB - 38

        #this is for 0MB to 1000MB
        if(free_space_GB>= 0.0 and free_space_GB<1.0):
            disk_space_in_10_MB  = disk_space_in_MB/10.0
            disk_space  =  disk_space_in_10_MB - 128

        print("The encoded disk_space is ",int(disk_space))
        return int(disk_space),util

    '''
    Create the metadata object for the microbatchID
    '''
    def createMetadataObj(self, microbatchID, streamID, blockSize):

        metaData = Metadata()
        metaData.clientId = CLIENT_ID
        metaData.sessionSecret = SESSION_SECRET
        metaData.mbId = microbatchID
        metaData.streamId = streamID
        metaData.timestamp = int(time.time() * 1000)
        metaData.compFormat = COMP_FORMAT
        metaData.uncompSize = blockSize
        metaData.sizeofblock = blockSize

        additional_prop = {}
        additional_prop["Name"] = "Sheshadri"
        metaData.properties = json.dumps(additional_prop)

        keyValDict = {}

        keyValDict["cds"] = ["iisc", "has", "serc"]
        keyValDict["dream"] = ["cds", "has", "dream"]
        keyValDict["iot"] = ["dream", "has" ,"iot", "subgroup"]
        metaData.metakeyvaluepairs = keyValDict

        return metaData

    def writeRequestToFog(self,microbatchID,streamId,filePath, data, fogReplicaMap, yetAnotherMap,sizeChoice,setLease,metaKeyValueMap):

        #/home/swamiji/eclipse-workspace/edgefs_Europar/EdgeServer/Audio_02_06_2019_20_57_02.mp3
        print("read the file ",len(data))

        #statinfo = os.stat(path)
        #dataLength = statinfo.st_size
        encodedSpace,util = self.encodeFreeSpace(len(data)) #this is the length of the file

        #lets see if there is an actual need to renew lease though we will be calling renew method anyways


        print("fog ip, fog port , talking to fog for replica locations ",FOG_IP,FOG_PORT)
        #write request going to a fog
        # client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE) #127.0.0.1 9091
        transport = TSocket.TSocket(FOG_IP, FOG_PORT)

        # Buffering is critical. Raw sockets are very slow
        transport = TTransport.TFramedTransport(transport)

        # Wrap in a protocol
        protocol = TBinaryProtocol.TBinaryProtocol(transport)

        # Create a client to use the protocol encoder
        myClient = FogService.Client(protocol)

        # Connect!
        transport.open()

        #
        blackListedFogs = []
        #EdgeInfoData(nodeId,nodeIp,port,reliability,storage)getWriteLocations
        # list<WritableFogData> getWriteLocations(1: byte dataLength, 2: Metadata metadata,
        #                                     3: list<i16> blackListedFogs, 4:EdgeInfoData selfInfo)
        global STREAM_ID
        global CLIENT_ID
        global SESSION_SECRET
        metaData = self.createMetadataObj(microbatchID, streamId, len(data))

        #print EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,encodedSpace
        #edgeInfo = EdgeInfoData(EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,encodedSpace)
        #print "here also ",edgeInfo
        print("encodedSpace ",encodedSpace)


        timestamp_record_getWrite = str(microbatchID)  +   ","  +   str(-100) +  ", local, "  + "getWriteLocations ,starttime = " + repr(time.time())  + ","
        #result = myClient.getWriteLocations(encodedSpace,metaData,blackListedFogs,edgeInfo) #datalength,
        print("sizeofblock => ",metaData.sizeofblock)

        timestamp_record_getWrite = timestamp_record_getWrite +"endtime = " + repr(time.time()) +" , " + str(sizeChoice) + '\n'

        #we are calculating replicas using getWriteLocations()
        yetAnotherMap[microbatchID] = {}

        insideDict = {}

        for w in result:
            edgeInfoData = w.edgeInfo
            if(edgeInfoData != None ):
                if("local" in fogReplicaMap):
                     fogReplicaMap["local"] =  fogReplicaMap["local"] + 1

                else:
                     fogReplicaMap["local"] = 1

                if("local" in insideDict):
                             insideDict["local"] = insideDict["local"] + 1

                else:
                     insideDict["local"] = 1

            else:
                if(str(w.node.nodeId) in fogReplicaMap):
                    fogReplicaMap[str(w.node.nodeId)] =  fogReplicaMap[str(w.node.nodeId)] + 1
                else:
                    fogReplicaMap[str(w.node.nodeId)] = 1

                if(str(w.node.nodeId) in insideDict):
                      insideDict[str(w.node.nodeId)] = insideDict[str(w.node.nodeId)] + 1
                else:
                      insideDict[str(w.node.nodeId)] =  1

        #util map
        yetAnotherMap[microbatchID] = insideDict

        #before sending the actual writes, lets add the checksum now as there is no point
        #in sending the checksum while identifying replicas
        hash_md5 = hashlib.md5()
        hash_md5.update(data)
        metaData.checksum = hash_md5.hexdigest()

        print("the write locations are ",result)

        timestamp_record = str(microbatchID) +  ",-1, local ,write req,starttime = " +   repr(time.time()) +    ","

        #lets renew the lease. The behaviour should adhere with the policy of the lease time
        #left in comparison to the time taken to complete the operation
        print("Lets first issue request to renew the lease for putNext()")

        '''
        Find the stream owner fog of the given stream
        '''
        homeFog = self.find_stream_owner(streamId)

        '''
        Acquire the lease for the given stream
        '''
        self.openStream(metaData.streamId, metaData.clientId, EXPECTED_LEASE, metaData.mbId,setLease, homeFog)
        #ISSUE ALERT:: Since the metaData object is prepared above, it might happen that the clientId and sessionSecret
        #were set to dummy global values since before issuing the first write, we do a renew lease which is last code line
        #but we set the clientId and secret many lines above. So once renew_lease() returns the proper sessionSecret will
        #be with the client and it can properly perform the first write operation. The same fields above cannot be commented
        #since both clientId and sessionSecret are required fields (thrift)
        metaData.clientId = CLIENT_ID
        metaData.sessionSecret = SESSION_SECRET

        index = 1
        processes = []
        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)

        '''
        call to Fog on put api, 2 types of put can happen
            1. Normal put
            2. Causal put
        '''
        response = client.putDataQuorum(metaData, 0, data,metaKeyValueMap, CLIENT_ID)

        '''
        Insert a homefog of the stream for the block that is being inserted
        '''
        self.insert_home_fog_for_block(microbatchID, streamId, homeFog)
        print("Wrote data at ",time.time())

        '''
        Release the lease for the given stream
        '''
        response = self.closeStream(metaData.streamId, metaData.clientId, microbatchID,homeFog)

        print("response status ",response.status)
        return response.status
    
def put(path,streamId,start,metadataLocation,fogIp,fogPort,edgeId,clientId,splitChoice,setLease,leaseDuration,compFormat,verbose = False):
    myEdge = EdgeClient()

    global PATH
    PATH = path
    global STREAM_ID
    STREAM_ID = streamId
    global START
    START = int(start)
    global FOG_IP
    FOG_IP = fogIp
    global FOG_PORT
    FOG_PORT = int(fogPort)
    global EDGE_ID
    EDGE_ID = int(edgeId)
    global CLIENT_ID
    CLIENT_ID = clientId
    global SPLIT_CHOICE
    SPLIT_CHOICE = int(splitChoice)
    global STREAM_RELIABILITY
    STREAM_RELIABILITY = myEdge.getStreamMetadataReliability(STREAM_ID)
    global EXPECTED_LEASE
    EXPECTED_LEASE = int(leaseDuration)
    global COMP_FORMAT
    COMP_FORMAT = compFormat

    ## Initialize the metaKeyValueMap dict. This dicionary/map comtains the optional metadata
    ## properties that can be specified by the end user during runtime.
    ## The metataLocation points to a json file that contains the additional metadata information.
    metaKeyValueMap = dict()
    if metadataLocation != None:
        metaKeyValueMap = json.load(open(metadataLocation,'r'))

    metaKeyValueMap = {}
    metaKeyValueMap["1"] = ["dream"]
    metaKeyValueMap["2"] = ["lab"]
    metaKeyValueMap["3"] = ["iisc"]
    metaKeyValueMap["4"] = ["bgl"]

    if setLease == "1":
        setLease = True
    elif setLease == "0":
        setLease = False

    global SET_LEASE
    SET_LEASE = setLease

    fogReplicaMap = {} #here is the dictionary
    yetAnotherMap = {}

    ## Check if the entity specified by the PATH is a file or a directory
    if(os.path.isfile(PATH)):
        if SPLIT_CHOICE == 1:
            ##i.e write the whole file as a single block.
            print("The stream reliability needed is : ", str(STREAM_RELIABILITY))

            filePath = PATH
            byteArray = []

            file = open(PATH,'rb')

            ## Get the file size
            fileInfo = os.stat(PATH)
            fileSizeMB = fileInfo.st_size / 10000000 ## in MB
            byteArray.append(file.read())
            file.close()
            #print "appended ",len(byteArray[len(byteArray)-1]),"number of bytes" + str(SPLIT_CHOICE)
            newFilePath = ""

            if verbose == True :
                response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[0], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease,metaKeyValueMap)
            else:
                with nostdout():
                    response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[0], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease,metaKeyValueMap)
                sys.stdout = sys.__stdout__

            if response == 1:
                print("...")
                print("All writes successfull")
            else:
                print(response)
                print("not successfull")


        else:
            file = open(PATH,'rb')
            ## Get the file size
            fileInfo = os.stat(PATH)
            fileSize = fileInfo.st_size
            byteArray = []

            while file.tell() != fileSize:
                byteArray.append(file.read(DEFAULT_BLOCK_SIZE))
                #print "appended ",len(byteArray[len(byteArray)-1]),"number of bytes"
            file.close()
            print("number of binary files",len(byteArray))
            newFilePath = ""
            for i in range(0,len(byteArray)):
                fileSizeMB = len(byteArray[i]) / 10000000 ## in MB

                if verbose == True :
                    response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease,metaKeyValueMap)
                    START = START + 1
                else:
                    with nostdout():
                        response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease,metaKeyValueMap)
                        START = START + 1
                    sys.stdout = sys.__stdout__
            if response == 1:
                print("...")
                print("All writes successfull")
            else:
                print(response)
                print("not successfull")

    else:
        ## This means that the PATH represents a directory
        ## Hance, perform write on all the files in the directory.
        ## Writing all files from the directory
        files = os.listdir(PATH)

        if SPLIT_CHOICE == 1:
            byteArray = []
            for fileName in files:
                print("Reading file ",PATH+"/"+fileName)
                #print "The file being read is ",PATH+"/"+fileName
                file = open(PATH+"/"+fileName,'rb')
                byteArray.append(file.read())
                #print "appended ",len(byteArray[len(byteArray)-1]),"number of bytes"
                file.close()

            #End of while loop
            print("number of binary files",len(byteArray))
            print(str(setLease))
            newFilePath = ""
            for i in range(0,len(byteArray)):
                fileSizeMB = len(byteArray[i]) / 10000000 ## in MB

                if verbose == True :
                    response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease,metaKeyValueMap)
                    START = START + 1
                else:
                    with nostdout():
                        response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease,metaKeyValueMap)
                        START = START + 1
                    sys.stdout = sys.__stdout__
            if response == 1:
                print("...")
                print("All writes successfull")
            else:
                print(response)
                print("not successfull")

        else:
            byteArray = []
            for fileName in files:
                print("Reading file ",PATH+"/"+fileName)
                file = open(PATH+"/"+fileName,'rb')
                ## Get the file size
                fileInfo = os.stat(PATH+"/"+fileName)
                fileSize = fileInfo.st_size

                while file.tell() != fileSize:
                    byteArray.append(file.read(DEFAULT_BLOCK_SIZE))
                    #print "appended ",len(byteArray[len(byteArray)-1]),"number of bytes"

                file.close() ## Reading of the files done. NOTE : The files have ben splitted simultaneously
            print("number of binary files",len(byteArray))
            print(str(setLease))
            newFilePath = ""
            for i in range(0,len(byteArray)):
                fileSizeMB = len(byteArray[i]) / 10000000 ## in MB

                if verbose == True :
                    response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease,metaKeyValueMap)
                    START = START + 1
                else:
                    with nostdout():
                        response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease,metaKeyValueMap)
                        START = START + 1
                    sys.stdout = sys.__stdout__
            if response == 1:
                print("...")
                print("All writes successfull")
            else:
                print(response)
                print("not successfull")
