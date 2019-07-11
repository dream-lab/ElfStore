import sys
sys.path.append('./edgefs/gen-py')
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

######################################################IGNORE ALL THIS ######################################################
MAX_REPRESENTABLE_DISK_SPACE = 32
DIVISION_FACTOR = 32
DISK_UTIL = 2
BLOCK_SIZE = 10*1024*1024
FOG_SERVICE = 0
EDGE_SERVICE = 1
ENCODED_SPACE = 200
BASE_LOG = "/logs/"
FOG_IP = "127.0.0.1"
FOG_PORT = 9092

EDGE_ID = 0
EDGE_IP = 0
EDGE_PORT = 0
EDGE_RELIABILITY = 0
STREAM_RELIABILITY = 0.99
FILE_SIZE = 1000000
sizeChoice = 1
STREAM_ID = 'test'

STREAM_OWNER_FOG_IP = "127.0.0.1"
STREAM_OWNER_FOG_PORT = 9090

#this is assigned when the open() api succeeds
SESSION_SECRET = 'test'

CLIENT_ID = 'test'


#Before every write & incrementBlockCount operation, we issue a renew lease call
#which should succeed always in case left lease time is more than the time taken
#to complete an operation. For this we maintain a conservative estimate of time
#taken for the completion of operations. Also in case if the left lease time is
#less than the time taken to complete an operation, the renew lease call may fail
#i.e. the lease may not be renewed in which case the stream should be reopened

#estimating the putNext() call to succeed in 10 seconds
ESTIMATE_PUT_NEXT = 20*1000

#estimating the incrementBlockCount() call to succeed in 5 seconds
ESTIMATE_INCR_BLOCK_COUNT = 10*1000

#local time when lock is acquired either by opening stream or renew lease
TIME_LOCK_ACQUIRED = 0

#local time when the lease expires
TIME_LEASE_EXPIRE = 0

#whether the client currently holds the lock
IS_LOCK_HELD = False

#now we have lease mechanism in place wherein only a single client can write to a
#stream and other clients will wait to acquire the lease. Since we want the blockIds
#in a stream to be continuous i.e. no gaps between blockIds, use the open() call and
#get the last blockId written to the stream and write from the next id and not the
#current way of writing where each client has a fixed range where it will be writing
#although each stream has a definite starting point.NOTE::For the different stages of
#the experiment, we will give different start points which is fine.
LAST_BLOCK_ID = 0

######################################################IGNORE ALL THIS ######################################################

logs_list = []

class EdgeClient:
    def __init__(self):
        self.log = {}

    #Write to one of the local edge devices
    # def writeToEdge(self,IP, port, data):

    #     print "IP is ",IP
    #     print "the length of data is ",len(data)
    #     # Make socket
    #     transport = TSocket.TSocket('localhost', EDGE_PORT)

    #     # Buffering is critical. Raw sockets are very slow
    #     transport = TTransport.TBufferedTransport(transport)

    #     # Wrap in a protocol
    #     protocol = TBinaryProtocol.TBinaryProtocol(transport)

    #     # Create a client to use the protocol encoder
    #     client = EdgeService.Client(protocol)

    #     # Connect!
    #     transport.open()

    #     # client.write("Somethiong",None,data)
    #     client.pong()

    #     metaObj = Metadata("1","2","3")

    #     result = client.write("md1",metaObj, data)
    #     print result

    #     transport.close()


        # self.closeSocket(transport)

    # Return the fog client instance to talk to the fog
    def openSocketConnection(self,ip,port, choice):

        print "ip ",ip," port",port," choice ", "open socket connection"
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
            print "In the edge service call "
            client = EdgeService.Client(protocol)

        # Connect!
        transport.open()

        return client,transport

    #Close connection
    def closeSocket(self,transport):

        print "closing connection"
        transport.close()

    #utility function to return the systems's utilizaion and free space
    def returnDiskSpace(self):

        if( hasattr(os,'statvfs')):
            #st = os.statvfs("/") #The root part
            st = os.statvfs("/")
            free = st.f_bavail * st.f_frsize
            total = st.f_blocks * st.f_frsize
            used = (st.f_blocks - st.f_bfree) * st.f_frsize
            print "Disk ",free," : ",total," : ",used

            util = used/float(total)*100
            # print "Disk util is " ,util
            # return GB_part,(MB_part*1024),int(KB_part*1024),int(util)
            disk_space_in_MB = float(free /(1024*1024.0))
            return disk_space_in_MB,util

        return 0 #default return value

    #Number to bit string
    def returnBinaryString(self,number):

        var = ""
        while (number!=0):
            bit = number%2
            var = var + str(bit)
            number = number/2

        while(len(var)<5):
            var = var + '0'

        var = var[::-1]

        return var

    #Encode free space available in disk, (7-bit) 1st bit for GB/MB order, 2nd bit for encoding , 5 bits for units
    def encodeFreeSpace(self, space):

        disk_space_in_MB,util = self.returnDiskSpace()
	print "space requested ",space


        disk_space_in_MB = space/(1024*1024) #remove this before using

        # print "Free space in GB ",int(free_space_GB)," Free Space in MB ",int(free_space_MB), " Free Space in KB ",int(free_space_KB)
        print "Requested space in MB ",int(disk_space_in_MB)

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

        print "The encoded disk_space is ",int(disk_space)

        return int(disk_space),util

    #Test Function to check decode logic
    def decodeLogic(self,diskSpace):

        if (diskSpace>=-128 and diskSpace<-27):
            diskSpace = (diskSpace + 128)*10

        elif(diskSpace>=-27 and diskSpace<=72):
            diskSpace = 1000 + (diskSpace + 28)*100

        else:
            diskSpace = 11000 + (diskSpace - 72 )*1000

        print diskSpace;

    #collect disk stats
    def prepareDiskStatPayload(self,edgePayload, prevDiskSpace):

        freeDiskSpace,util = self.encodeFreeSpace(ENCODED_SPACE)

        if(util>DISK_UTIL):
            print "Watermark level",DISK_UTIL,"%", " breached!! disk utilization now at ",util, "%"

        if(prevDiskSpace!=freeDiskSpace):
            edgePayload.encodedStorage = freeDiskSpace
            prevDiskSpace = freeDiskSpace

        return edgePayload,prevDiskSpace


    # Periodically keep sending disk reports
    def heartBeat(self,client):

        prevDiskSpace = 0
        storagePayloadInterval = 5
        counter = 0

        while True:

            edgePayload = EdgePayload(EDGE_ID) #only the edge ID

            if(counter%storagePayloadInterval==0): #include payload from storage
                edgePayload, prevDiskSpace = self.prepareDiskStatPayload(edgePayload, prevDiskSpace)


            result = client.edgeHeartBeats(edgePayload)

            print "Hearbeat sent"
            counter = counter + 1
            time.sleep(10)

    #Register the fog device to start sending heartbeats
    def registerEdgeDevice(self,nodeId,nodeIp,port,reliability,storage_sent):

        timestamp_record = "register edgeJoin,starttime = "+repr(time.time())+","
        print "Register Edge device method was called.."
        print "NodeId ",nodeId," Node IP ",nodeIp," port ",port, " reliability ",reliability


        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT, FOG_SERVICE)

        #register Edge device
        storage, util = self.encodeFreeSpace(storage_sent)
        edgeDevice = EdgeInfoData(nodeId,nodeIp,port,reliability,storage)
        client.edgeJoin(edgeDevice)

        self.closeSocket(transport)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.close()

    #Send periodic updates
    def sendPeriodicUpdates(self):

        print "The fog IP ",FOG_IP," fog port ",FOG_PORT

        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT, FOG_SERVICE)

        #keep sending disk reports
        self.heartBeat(client)

        # Close!
        self.closeSocket(transport)

    def writeRequestToFog(self,microbatchID,streamId,filePath, data, fogReplicaMap, yetAnotherMap,sizeChoice):

        #/home/swamiji/eclipse-workspace/edgefs_Europar/EdgeServer/Audio_02_06_2019_20_57_02.mp3

        #Read data and send it
        # path = '/home/swamiji/phd/myFile.txt'
        #path = filePath
        #file = open(path,'r')
        #data = file.read() # add try catch here
        print "read the file ",len(data)

        #statinfo = os.stat(path)
        #dataLength = statinfo.st_size
        encodedSpace,util = self.encodeFreeSpace(len(data)) #this is the length of the file

        #lets see if there is an actual need to renew lease though we will be calling renew method anyways


        print "fog ip, fog port , talking to fog for replica locations ",FOG_IP,FOG_PORT
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
        metaData = Metadata()
        metaData.clientId = CLIENT_ID
        metaData.sessionSecret = SESSION_SECRET
        metaData.mbId = microbatchID
        metaData.streamId = streamId
        metaData.timestamp = time.time() * 1000
        additional_prop = {}
        additional_prop["Name"] = "Sheshadri"
        metaData.properties = json.dumps(additional_prop)


        #print EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,encodedSpace
        #edgeInfo = EdgeInfoData(EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,encodedSpace)
        #print "here also ",edgeInfo
        print "encodedSpace ",encodedSpace


        timestamp_record_getWrite = str(microbatchID)  +   ","  +   str(-100) +  ", local, "  + "getWriteLocations ,starttime = " + repr(time.time())  + ","
        #result = myClient.getWriteLocations(encodedSpace,metaData,blackListedFogs,edgeInfo) #datalength,
        result = myClient.getWriteLocations(encodedSpace,metaData,blackListedFogs,True)
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

        print "the write locations are ",result

	timestamp_record = str(microbatchID) +  ",-1, local ,write req,starttime = " +   repr(time.time()) +    ","

        #lets renew the lease. The behaviour should adhere with the policy of the lease time
        #left in comparison to the time taken to complete the operation
        print "Lets first issue request to renew the lease for putNext()"
        self.renew_lease(metaData.streamId, metaData.clientId, metaData.sessionSecret, 0, ESTIMATE_PUT_NEXT, metaData.mbId)

        #ISSUE ALERT:: Since the metaData object is prepared above, it might happen that the clientId and sessionSecret
        #were set to dummy global values since before issuing the first write, we do a renew lease which is last code line
        #but we set the clientId and secret many lines above. So once renew_lease() returns the proper sessionSecret will
        #be with the client and it can properly perform the first write operation. The same fields above cannot be commented
        #since both clientId and sessionSecret are required fields (thrift)
        metaData.clientId = CLIENT_ID
        metaData.sessionSecret = SESSION_SECRET

        index = 1
        processes = []
        # loop is for different fogs(and edges) returned WRITING STARTS HERE : ISHAN
        for writable in result:
            writeProcess = multiprocessing.Process(target=self.writeToEdge,args=(writable,microbatchID,streamId,data,EDGE_ID,index,sizeChoice, metaData))
	    processes.append(writeProcess)
            writeProcess.start()
	    index = index + 1

	for p in processes:
	    p.join()

	print "all writes to replicas finished "
        self.closeSocket(transport)

        timestamp_record = timestamp_record +"endtime = " + repr(time.time())+" , " + str(sizeChoice) + '\n'
        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
	myLogs.write(timestamp_record_getWrite)#write timestamp for getWrite Locations
        myLogs.close()

        #the response type is BlockMetadataUpdateResponse
        response = self.increment_block_count(metaData)
        if response.code == -1:
            #this blockId is already written
            #In our designed experiment, different clients are writing to different regions so this
            #issue will not occur. However this is kept to indicate that such a scenario can occur
            #with concurrent clients
            print "BlockId : " + str(microbatchID) + " is already written, failing the write"
            return -1
        elif response.code == -2:
            #lease expired
            print "Lease expired, should renew the lease before trying again"
            return -1
        elif response.code == -3:
            #client does not hold the lock
            print "Client does not hold the lock, should open the stream before writing"
            return -1
        else:
            return 1



    # Write to either fog or edge depending on the result
    def writeToEdge(self, writable, microbatchID, streamId, data, EDGE_ID, index, sizeChoice, metaData):

	device = ""
	if(writable.edgeInfo!=None):
		device = "local"
	else:
		device = str(writable.node.nodeId)

	localTime = repr(time.time())
        timestamp_record = str(microbatchID)  + ","  +  str(index) +  "," +  device   + "," +    "write req,starttime = "+localTime+","

        print "got the writable ",writable," the microbatchID is ",microbatchID

        nodeInfo = writable.node #NodeInfoData
        writePreference = writable.preference
        reliability = writable.reliability
        edgeInfoData = writable.edgeInfo # this is an optional field

        #Write to the edge
        if edgeInfoData != None :

            print "ip ",edgeInfoData.nodeIp," port",edgeInfoData.port
            # Make socket
            transport = TSocket.TSocket(edgeInfoData.nodeIp, edgeInfoData.port)

            # Buffering is critical. Raw sockets are very slow
            transport = TTransport.TFramedTransport(transport)

            # Wrap in a protocol
            protocol = TBinaryProtocol.TBinaryProtocol(transport)

            # Create a client to use the protocol encoder
            client = EdgeService.Client(protocol)

            # Connect!
            transport.open()

            #byte write(1:string mbId, 2:Metadata mbMetadata, 3:binary mbData)

	    #local write -150
	    timestamp_record_local = str(microbatchID) +","+str(-150)+",local,write req,starttime = "+localTime+","
            #the response is not a byte anymore, its a WriteResponse
            response = client.write(microbatchID,metaData,data)
	    timestamp_record_local = timestamp_record_local +"endtime = " + repr(time.time())+" , " + str(sizeChoice) + '\n'

            print "response from the edge ",response.status

            transport.close()

            #update the metadata structures for the Fog
            client,transport = self.openSocketConnection(nodeInfo.NodeIP,nodeInfo.port,FOG_SERVICE)

            #byte insertMetadata(1: Metadata mbMetadata, 2: EdgeInfoData edgeInfoData);

            #this was valid as per previous implementation in which we assumed that a local
            #write means that the client will be writing to itself. However as per the new
            #implementation, it is not necessary and a local write means a write that is written
            #to itself or any other edge managed by the same Fog i.e. a neighbor edge of the client
            #edgeInfoData = EdgeInfoData()
            #edgeInfoData.nodeId = EDGE_ID
            #edgeInfoData.nodeIp = EDGE_IP
            #edgeInfoData.port = EDGE_PORT
            #edgeInfoData.reliability = EDGE_RELIABILITY
            #edgeInfoData.storage = 12 #This value is not useful for computation

            #update the metadata with the checksum
            #hash_md5 = hashlib.md5()
            #hash_md5.update(data)
            #metaData.checksum = hash_md5.hexdigest()

	    #metadata insert to fog -50
            my_dict = {}
	    timeMetadata = str(microbatchID) +","+str(-50)+",local ,metadata req,starttime = "+repr(time.time())+","
            response = client.insertMetadata(metaData, edgeInfoData, my_dict)
	    timeMetadata = timeMetadata + " endTime = "+repr(time.time())+" , " + str(sizeChoice)+'\n'


	    myLogs = open(BASE_LOG+ 'logs.txt','a')
	    myLogs.write(timestamp_record_local)
	    myLogs.write(timeMetadata)
            myLogs.close()
            print "The response from the fog is ", response

            self.closeSocket(transport)

        else :

            print "Have to talk to a fog with preference "
            # byte write(1:Metadata mbMetadata, 2:binary data, 3:WritePreference preference);
            client,transport = self.openSocketConnection(nodeInfo.NodeIP,nodeInfo.port,FOG_SERVICE)

            #response is now a WriteResponse and not a byte
            my_dict = {}
            response = client.putNext(metaData, data, writable.preference, my_dict)


            print "the response from the fog for write ",response.status
            self.closeSocket(transport)

        timestamp_record = timestamp_record +"endtime = " + repr(time.time())+" , " + str(sizeChoice) + '\n'
        print "the time stamp for write request is ",timestamp_record

        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.close()

    # call to the edge
    def readFromEdgeOrFog(self,microbatchID):

        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)

        # ReadResponse read(1: string microbatchId, 2:bool checkLocal, 3:bool checkNeighbors, 4:bool checkBuddies, 5:EdgeInfoData selfInfo);
        edgeInfoData = EdgeInfoData()
        edgeInfoData.nodeId = EDGE_ID
        edgeInfoData.nodeIp = EDGE_IP
        edgeInfoData.port = EDGE_PORT
        edgeInfoData.reliability = EDGE_RELIABILITY
        edgeInfoData.storage = 12

        timestamp_record = str(microbatchID)+",read req,starttime = "+repr(time.time())+","
        response = client.read(microbatchID,True,True,True,edgeInfoData,True)#last bit is for recovery
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'

        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.close()

        if(response.status == 0):
            print "File not found : cannot read file"
        else:

            edgeInfoRecv = response.edgeInfo

            if(edgeInfoRecv!=None):

                print "edgeInfoRecv from fog ",edgeInfoRecv
                #have to read data from edge

                transport = TSocket.TSocket(edgeInfoRecv.nodeIp,edgeInfoRecv.port)

                # Buffering is critical. Raw sockets are very slow
                transport = TTransport.TFramedTransport(transport)

                # Wrap in a protocol
                protocol = TBinaryProtocol.TBinaryProtocol(transport)

                # Create a client to use the protocol encoder
                client = EdgeService.Client(protocol)

                # Connect!
                transport.open()

                response = client.read(microbatchID,1) #this is for recovery

                print "Read status is ",response.status

                if response.status==0 :
                    print "File not found : cannot read file"

                else:
                    print "Local Read ",len(response.data)," number of bytes"
                    print "Metadata also read ",response.metadata

                transport.close()
            else:

                if response.status == 0:
                    print "File not found : cannot find file"

                else:
                    print "Remote Read ",len(response.data), " number of bytes"
                    print "Metadata also read ",response.metadata


            self.closeSocket(transport)


    #find api, from client to a Fog, meta
    def find(self, metadataKey, metadataValue, checkNeighbors, checkBuddies):

        # FindResponse find(1: string metadataKey, 2:string metadataValue, 3:bool checkNeighbors, 4:bool checkBuddies);
        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
        response = client.find(metadataKey, metadataValue, True,True)
        print "metadataKey ",metadataKey," metadataValue ", metadataValue ,"checkNeighbors ", checkNeighbors,"checkBuddies",checkBuddies

        if(response.status==0):
            print "Cannot find micro batches of that metadata"

        else :
            print "the number of micro batches returned are ",len(response.data)

        self.closeSocket(transport)



    #registers stream for the edge
    def registerStream(self, streamId, startBlockNum):

        print "fog ip, fog port ",FOG_IP,FOG_PORT
        # streamId = "serc1"

        #streamPayload = StreamMetadata()
        #streamPayload.startTime = time.time()*1000
        #streamPayload.reliability = float(STREAM_RELIABILITY)
        #streamPayload.minReplica = 2
        #streamPayload.maxReplica = 5

        streamMD = StreamMetadata()
        streamMD.streamId = streamId
        streamMD.startTime = I64TypeStreamMetadata(time.time()*1000, False)
        streamMD.reliability = DoubleTypeStreamMetadata(float(0.50), True) # STREAM_RELIABILITY
        streamMD.minReplica = ByteTypeStreamMetadata(2, True)
        streamMD.maxReplica = ByteTypeStreamMetadata(5, True)
        streamMD.version = I32TypeStreamMetadata(0, True)
        streamMD.otherProperties = dict()
        streamMD.otherProperties["update_prop"] = DynamicTypeStreamMetadata("0", "Integer", True)

        print "The reliability is set to ",streamMD.reliability.value
        print "The min replica is ",streamMD.minReplica.value , "\n The max replica is ",streamMD.maxReplica.value

        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
        timestamp_record = "register stream req,starttime = "+repr(time.time())+","

        response = client.registerStream(streamId, streamMD, startBlockNum)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.close()

        print "the response is ",response
        self.closeSocket(transport)

    #Test code for testing read
    def testRecovery(self):


        # print "edgeInfoRecv from fog ",edgeInfoRecv
        #have to read data from edge

        transport = TSocket.TSocket("127.0.0.1",5005)

        # Buffering is critical. Raw sockets are very slow
        transport = TTransport.TFramedTransport(transport)

        # Wrap in a protocol
        protocol = TBinaryProtocol.TBinaryProtocol(transport)

        # Create a client to use the protocol encoder
        client = EdgeService.Client(protocol)

        # Connect!
        transport.open()

        response = client.read("jsr.java",1) #this is for recovery

        print "Read status is ",response.status

        if response.status==0 :
            print "File not found : cannot read file"

        else:
            print "Local Read ",len(response.data)," number of bytes"
            print "Metadata also read ",response.metadata

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
        print "the time stamp for find request is ",timestamp_record

        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.close()

        self.closeSocket(transport)
        print "Sent replicas ",response


        for findReplica in response :

             edgeInfoData = findReplica.edgeInfo

             if(edgeInfoData!=None):

                 print "edgeInfoRecv from fog ",edgeInfoData
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
                 response = client.read(microbatchId,0) #this is for recovery
                 timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
                 myLogs = open(BASE_LOG+ "logs.txt",'a')
                 myLogs.write(timestamp_record)
                 myLogs.close()

                 print "Read status is ",response.status

                 if response.status==0 :
                     print "File not found : cannot read file"

                 else:
                     print "Local Read ",len(response.data)," number of bytes"
                     print "Metadata also read ",response.metadata
                     return 1 #successful read

                 transport.close()
             elif(findReplica.node!=None) :

                 fogNode = findReplica.node

                 client,transport = self.openSocketConnection(fogNode.NodeIP,fogNode.port,FOG_SERVICE)

                 timestamp_record = str(microbatchId)+", 27 ,"+str(findReplica.node.nodeId)  + ",write req,starttime = "+repr(time.time())+","
                 response = client.read(microbatchId,0)
                 timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
                 myLogs = open(BASE_LOG+ "logs.txt",'a')
                 myLogs.write(timestamp_record)
                 myLogs.close()
                 if(response.status == 1):
                     print "Fog Amount of bytes read ",len(response.data)
                     return 1 #successful read
                 else:
                     print "The queried fog does not have data"

                 self.closeSocket(transport)
             else:

                 print "The queried fog does not have data"

    def writeUsingProcess(self, start, streamId, byteArray, process_index, return_dict):
        print "Inside a write process"
        #as each process will do 100 writes, we need to create the two maps and send
        #them to the main method and then merge all maps to create single picture there
        fogReplicaMap = {}
        yetAnotherMap = {}

        newFilePath = ""
        numWrites = 0
	recycle = 0
        #lets note the time taken by each process as well
        #this is not for a microbatchId so using -1 for it, -1000 indicates the time for a process completing 100 writes
        # and third is the number of process where this ranges from 0 to 9 during stress testing
        timestamp_process = "-1, -1000, " + str(process_index) + ",stress test write process req,starttime = "+repr(time.time())+","
        while(numWrites<38):# original value is 100
            myEdge.writeRequestToFog(str(start),streamId,newFilePath,byteArray[recycle],fogReplicaMap, yetAnotherMap)
	    numWrites = numWrites + 1
	    start = start + 1
	    recycle = (recycle + 1)%10
        timestamp_process = timestamp_process +"endtime = " + repr(time.time()) + '\n'
        #writing of logs should happen here
	myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_process)
        myLogs.close()

        return_dict[process_index] = (fogReplicaMap, yetAnotherMap)

    #fetching stream metadata has an id of 250
    def getStreamMetadata(self, sid):
        print "Going to fetch stream metadata"
        timestamp_record = "-1, 250,"+ str(-1)  + ",stream md fetch,starttime = "+repr(time.time())+","
        client,transport = self.openSocketConnection(FOG_IP, FOG_PORT, FOG_SERVICE)
        streamMetadataInfo = client.getStreamMetadata(sid, True, True, True)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
        self.closeSocket(transport)

        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.close()

        return streamMetadataInfo.streamMetadata


    #stream metadata update has an id of 300
    def updateStreamMD(self):
        print "Going to update stream metadata, first get the latest stream metadata"

        timestamp_record = "-1, 300,"+ str(-1)  + ",stream md update,starttime = "+repr(time.time())+","
        global STREAM_ID
        metadata = self.getStreamMetadata(STREAM_ID)
        #type of ownerFog is NodeInfoPrimary
        ownerFog = metadata.owner.value
        ownerFog_ip = ownerFog.NodeIP
        ownerFog_port = ownerFog.port
        print "Owner of ", STREAM_ID, " is ", ownerFog_ip, " : ", ownerFog_port

        print "============ VERSION IS ============" , metadata.version.value
        current_value = int(metadata.otherProperties["update_prop"].value)
        next_value = current_value + 1
        metadata.otherProperties["update_prop"] = DynamicTypeStreamMetadata(str(next_value), "Integer", True)

        client,transport = self.openSocketConnection(ownerFog_ip, ownerFog_port, FOG_SERVICE)

        #result type is StreamMetadataUpdateResponse
        result = client.updateStreamMetadata(metadata)
        #lets add the status as well as the last field in the log
        if result.code > 0:
            #success case
            timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + ",1" + '\n'
        else:
            #failure case
            timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + ",0" + '\n'

        self.closeSocket(transport)

        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.close()

    #open stream has an id of 350
    def openStream(self, stream_id, client_id, expected_lease, blockId):
        print "Going to open stream for writing"
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

        client,transport = self.openSocketConnection(STREAM_OWNER_FOG_IP, STREAM_OWNER_FOG_PORT, FOG_SERVICE)

        global TIME_LOCK_ACQUIRED
        global TIME_LEASE_EXPIRE
        global IS_LOCK_HELD

        while True:
            #type of response is OpenStreamResponse
            # sheshadri setlease
            response = client.open(stream_id, client_id, expected_lease, True)
            print "The response of open stream is ",response.status
            if response.status == 1:
                # if the set lease is false , then only IS_LOCK_HELD is needed
                TIME_LOCK_ACQUIRED = int(time.time() * 1000)
                TIME_LEASE_EXPIRE = TIME_LOCK_ACQUIRED + response.leaseTime
                print "The lease expire is ",TIME_LEASE_EXPIRE
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

    #renewLease() has an id of 400 and the overall time to complete the lock acquiring has an id of 450
    #if the left lease time is more than the time for the operation e.g. putNext or increment block count
    #then the renewLease() should return with a status of 1 and if that doesn't happen, it should be treated
    #as an exception. Similarly if the left lease time is less than the time for the operation, renewLease()
    #might return with a status of 0 and a negative code. In that case, some other client would have acquired
    #the lock on the stream and we will then use the openStream to acquire the lock. To capture whether an
    #exception occurs during this process, we use a code of 475
    def renew_lease(self, stream_id, client_id, session_secret, expected_lease, expected_completion_time, blockId):
        print "Lets check if there is a need to renew lease"

        #lets divide the renewal result in two parts based on whether the left lease time is more or less than the
        #time taken to complete the operation. In case we have more lease time left than the operation, we will
        #not make call to the server and proceed with the operation
        global TIME_LEASE_EXPIRE
        global TIME_LOCK_ACQUIRED
        current_time = int((time.time() * 1000))
        if TIME_LEASE_EXPIRE - current_time > expected_completion_time:
            temp = TIME_LEASE_EXPIRE - current_time
            #no need to call renewLease now, lets return and perform the operation
            print "There is some problem with renew lease ",temp," expected completed time ",expected_completion_time
            return

        #the behaviour of renewal of lease is as follows: the renewal can only succeed when the client holding
        #the lock is the previous holder as well i.e. no one has acquired the lock in between the lease expiration
        #and this renewal attempt. In case it fails, then go back to the open() api to get a fresh lock as some
        #other client got the lock in between
        global STREAM_OWNER_FOG_IP
        global STREAM_OWNER_FOG_PORT
        global IS_LOCK_HELD

        #this log is to capture the total time for acquiring the lock
        timestamp_full = str(blockId) + ", 450,"+ STREAM_OWNER_FOG_IP  + ",lock reacquire,starttime = "+repr(time.time())+","
        timestamp_record = ''

        fallback = True
        print "The lock status is ",IS_LOCK_HELD
        if IS_LOCK_HELD == True:

            print "The lock is held"
            #for lease renewal, after endtime, adding status flag as well indicating whether it was successful or not
            timestamp_record = str(blockId) + ", 400,"+ STREAM_OWNER_FOG_IP  + ",renew stream lease,starttime = "+repr(time.time())+","

            client,transport = self.openSocketConnection(STREAM_OWNER_FOG_IP, STREAM_OWNER_FOG_PORT, FOG_SERVICE)

            #the response type is StreamLeaseRenewalResponse
            # sheshadri setlease
            response = client.renewLease(stream_id, client_id, session_secret, expected_lease, True)
            if response.status == 1:
                print "renewLease() successful"
                timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + ",1" + '\n'
                timestamp_full = timestamp_full +"endtime = " + repr(time.time()) + '\n'
                fallback = False
                #set the time of new lease start and lease expire time here
                TIME_LOCK_ACQUIRED = int(time.time() * 1000)
                TIME_LEASE_EXPIRE = TIME_LOCK_ACQUIRED + response.leaseTime
            else:
                print "renewLease() failed, falling back to open() api"
                timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + ",0" + '\n'
                IS_LOCK_HELD = False

            self.closeSocket(transport)

        if fallback == True:
            self.openStream(stream_id, client_id, expected_lease, blockId)
            timestamp_full = timestamp_full +"endtime = " + repr(time.time()) + '\n'
        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.write(timestamp_full)
        myLogs.close()

    #incrementBlockCount() has an id of 500
    def increment_block_count(self, metadata):
        print "Going to increment the last blockId for the stream"
        800
        timestamp_record = str(metadata.mbId) + ", 500,"+ STREAM_OWNER_FOG_IP  + ",increment last blockId,starttime = "+repr(time.time())+","

        print "Lets first issue request to renew the lease for incrementBlockCount()"
        self.renew_lease(metadata.streamId, metadata.clientId, metadata.sessionSecret, 0, ESTIMATE_INCR_BLOCK_COUNT, metadata.mbId)

        global STREAM_OWNER_FOG_IP
        global STREAM_OWNER_FOG_PORT
        client,transport = self.openSocketConnection(STREAM_OWNER_FOG_IP, STREAM_OWNER_FOG_PORT, FOG_SERVICE)
        #the response type is BlockMetadataUpdateResponse
        # sheshadri setlease
        response = client.incrementBlockCount(metadata, True)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
        self.closeSocket(transport)

        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.close()

        return response

    def getLargestBlockId(self, stream_id):
        metadata = self.getStreamMetadata(stream_id)
        ownerFog = metadata.owner.value
        global STREAM_OWNER_FOG_IP
        STREAM_OWNER_FOG_IP = ownerFog.NodeIP
        global STREAM_OWNER_FOG_PORT
        STREAM_OWNER_FOG_PORT = ownerFog.port
        client,transport = self.openSocketConnection(STREAM_OWNER_FOG_IP, STREAM_OWNER_FOG_PORT, FOG_SERVICE)
        largest_id = client.getLargestBlockId(stream_id)
        self.closeSocket(transport)
        return largest_id

    def findStream(self):

        print "The find stream method is called"
        myQuery = SQueryRequest()
        myQuery.reliability = DoubleTypeStreamMetadata(float(0.99), True)
        #myQuery.minReplica = ByteTypeStreamMetadata(2, True)
        # myQuery.maxReplica = ByteTypeStreamMetadata(5, True)

        global STREAM_OWNER_FOG_IP
        global STREAM_OWNER_FOG_PORT

        print "The fog ip, fog port is ",STREAM_OWNER_FOG_IP, STREAM_OWNER_FOG_PORT

        client,transport = self.openSocketConnection(STREAM_OWNER_FOG_IP, STREAM_OWNER_FOG_PORT, FOG_SERVICE)

        print "The query is ",myQuery

        myQueryResponse = client.findStream(myQuery)
        print "The findstream response status ",myQueryResponse.status
        print "The list of streamid matching the findStream ",myQueryResponse.streamList

    def updateBlock(self):

        edgeInfoData = EdgeInfoData()
        edgeInfoData.nodeId = EDGE_ID
        edgeInfoData.nodeIp = EDGE_IP
        edgeInfoData.port = EDGE_PORT
        edgeInfoData.reliability = EDGE_RELIABILITY
        edgeInfoData.storage = 12

        global STREAM_ID
        global CLIENT_ID
        global SESSION_SECRET
        metaData = Metadata()
        metaData.clientId = CLIENT_ID
        metaData.sessionSecret = SESSION_SECRET
        metaData.mbId = 700
        metaData.streamId = streamId
        metaData.timestamp = time.time() * 1000
        additional_prop = {}
        additional_prop["Name"] = "Sheshadri"
        metaData.properties = json.dumps(additional_prop)

        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
        response = client.find(700,True,True,edgeInfoData)
        self.closeSocket(transport)
        print "Sent replicas ",response

        file = open("/home/swamiji/phd/EdgeFS/ElfStore/Commands.txt",'r')
        data = file.read()
        print "appended ",len(data),"number of bytes"
        file.close()

        hash_md5 = hashlib.md5()
        hash_md5.update(data)
        metaData.checksum = hash_md5.hexdigest()

        print "METADATA IS ", metaData


        for findReplica in response :

             edgeInfoData = findReplica.edgeInfo

             if(edgeInfoData!=None):

                 print "edgeInfoRecv from fog ",edgeInfoData
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

                 response = client.update(700,metaData, data) #this is for recovery

                 # print "Read status is ",response.status

                 # if response.status==0 :
                 #     print "File not found : cannot read file"

                 # else:
                 #     print "Local Read ",len(response.data)," number of bytes"
                 #     print "Metadata also read ",response.metadata
                 #     return 1 #successful read

                 transport.close()
             elif(findReplica.node!=None) :

                 fogNode = findReplica.node

                 client,transport = self.openSocketConnection(fogNode.NodeIP,fogNode.port,FOG_SERVICE)

                 response = client.updateBlock(700,metaData, data)
                 # if(response.status == 1):
                 #     print "Fog Amount of bytes read ",len(response.data)
                 #     return 1 #successful read
                 # else:
                 #     print "The queried fog does not have data"

                 self.closeSocket(transport)
             else:
                 print "The queried fog does not have data"

        exit(0)


#MAIN MEthod
if __name__ == '__main__':

    myEdge = EdgeClient()

    if(len(sys.argv)!=16):
        print "Usage: python EdgeClient.py 1 127.0.0.1 5000 85 127.0.0.1 9090 "
        print "python EdgeClient.py edge_conf.txt",len(sys.argv)
        with open('edge_conf.txt','r') as f:
            print f.read()
        exit(0)

    # with open('edge_conf.txt') as data_file:
    #     config = json.load(data_file)

    # EDGE_ID = int(config["edgeid"])
    # EDGE_IP = (config["edgeip"])
    # EDGE_PORT = int(config["edgeport"])
    # EDGE_RELIABILITY = int(config["reliability"])
    # choice = int(config["choice"])
    # mbId = config["microbatchid"]
    # streamId = config["streamid"]
    # FOG_IP = config["fogip"]
    # FOG_PORT = int(config["fogport"])

    # python2 EdgeClient_updated.py 2 127.0.0.1 8001 82 127.0.0.1 9090 test_2 1 5 /home/skmonga/ElfStore/logs_2/ 700 1 1 client_2 100 &

    EDGE_ID = int(sys.argv[1])
    EDGE_IP = sys.argv[2]
    EDGE_PORT = int(sys.argv[3])
    EDGE_RELIABILITY = int(sys.argv[4])
    FOG_IP = sys.argv[5]
    FOG_PORT = int(sys.argv[6])
    streamId = sys.argv[7]
    microbatchId = sys.argv[8]
    choice = int(sys.argv[9])
    BASE_LOG = sys.argv[10]
    START = int(sys.argv[11])
    STREAM_RELIABILITY = int(sys.argv[12])
    FILE_SIZE = int(sys.argv[13])
    client_id = sys.argv[14]
    NUM_WRITES = int(sys.argv[15])

    #just to place the streamId in the constant STREAM_ID
    global STREAM_ID
    global CLIENT_ID
    STREAM_ID = streamId
    CLIENT_ID = client_id

    sizeChoice = 1

    if(STREAM_RELIABILITY==0):
        STREAM_RELIABILITY = 0.90

    elif(STREAM_RELIABILITY==1):
        STREAM_RELIABILITY=0.99

    elif(STREAM_RELIABILITY==2):
        STREAM_RELIABILITY=0.999

    else:
        STREAM_RELIABILITY=0.9999


    if(FILE_SIZE==0):
        FILE_SIZE = 1000000
        sizeChoice = 1
    else:
        FILE_SIZE = 10000000
        sizeChoice = 10


    filePath = microbatchId
    fogReplicaMap = {} #here is the dictionary
    yetAnotherMap = {}

    print "Starting to client/write ...",choice
    #this is for stream metadata updates
    if (choice == 24):
        myEdge.findStream()
        exit(0)

    if (choice == 25):
        myEdge.updateBlock()

    if(choice == 20):
        num_updates = 100
        for i in range(num_updates):
            myEdge.updateStreamMD()
    #this is for testing the open() api
    if(choice == 21):
        #third argument is expected lease time which is not used currently
        myEdge.openStream(STREAM_ID, CLIENT_ID, 0, 0)
    #this is for testing the renewLease() api
    if(choice == 22):
        #fourth argument is expected lease time which is not used currently
        #fifth argument is the expected completion time for an operation
        #last argument is the blockId for which we are trying to renew
        #the lease. It can be a putNext() or incrementBlockCount()
        myEdge.renew_lease(STREAM_ID, CLIENT_ID, SESSION_SECRET, 0, 0, 0)
    #this is to get the
    if(choice == 23):
        largest_id = myEdge.getLargestBlockId(streamId)
        print "The largest blockId is : ", str(largest_id)
    if(choice==0):
        myEdge.readFromEdge(microbatchid)
    elif(choice==1):
        print "Register Stream "
        myEdge.registerStream(STREAM_ID, START)
    elif(choice==2):
        print "Write data to the stream mbId =>",microbatchId
        myEdge.writeRequestToFog(microbatchId,streamId)
        # ports=[9090,9091,9092,9093,9094,9095]
        # for port in ports:
        #     FOG_PORT = port
        #     print "the port chosen is ",FOG_PORT
        #     myEdge.readFromEdgeOrFog(mbId)

    elif(choice==3):
        print "Read data from fog "
        # ports=[9090,9091,9092,9093]
        # for port in ports:
        #     FOG_PORT = port
        #     print "the port chosen is ",FOG_PORT
        #     myEdge.readFromEdgeOrFog(mbId)

        myEdge.readFromEdgeOrFog(microbatchId)
    elif(choice==4):
        print "The find api.."
        # myEdge.find("Name","Sheshadri",True,True)
        myEdge.findAndRead(microbatchId)
    elif(choice==5):
        print "here"
        print "The stream reliability needed is : ", str(STREAM_RELIABILITY)

	filePath = "./edgefs/microbatch_data/microbatch"

	byteArray = []
	readFiles = 0

	while(readFiles<10):

            print "The file being read is ",filePath+str(readFiles)+".txt"
	    file = open(filePath+str(readFiles)+".txt",'r')
	    byteArray.append(file.read(FILE_SIZE))
	    print "appended ",len(byteArray[0]),"number of bytes"
	    readFiles = readFiles + 1
	    file.close()

	#End of while loop
	print "number of 10mB binary files",len(byteArray)

	newFilePath = ""
	numWrites = 0
	recycle = 0
        print "NUmber of writes needed are ",NUM_WRITES
        while(numWrites < NUM_WRITES):
            response = myEdge.writeRequestToFog(START, streamId, newFilePath, byteArray[recycle], fogReplicaMap, yetAnotherMap, sizeChoice)
            print "Writing file ",numWrites
            if response > 0:
	        numWrites = numWrites + 1
	        START = START + 1
	        recycle = (recycle + 1)%10
    elif(choice==6):

        print "Reading block ... ",microbatchId
        myEdge.findAndRead(int(microbatchId))
        sys.exit(0)

	numReads = 0
	while(numReads<100):

	    sample = random.randint(0,1599)
	    print "find and get with the id =>",sample
	    myEdge.findAndRead(sample)
	    numReads = numReads + 1
    elif(choice==7):
        print "Stress Testing"

	filePath = "./edgefs/microbatch_data/microbatch"

	byteArray = []
	readFiles = 0

	while(readFiles<10):

            print "The file being read is ",filePath+str(readFiles)+".txt"
	    file = open(filePath+str(readFiles)+".txt",'r')
	    byteArray.append(file.read())
	    print "appended ",len(byteArray[0]),"number of bytes"
	    readFiles = readFiles + 1
	    file.close()


        #reading is done, lets start 10 processes
        #keeping the same range as we usually pass around in the shell script
        #just multiplying here by 10 so that no changes are required elsewhere
        START = START * 4
        END = END * 4
        write_processes = []
        process_index = 0

        #each process populates a dictionary at the index of the process itself and we can then use the total
        #result once join is called on every process. We can then use the result here
        manager = multiprocessing.Manager()
        return_dict = manager.dict()

        # -1 for microbatchId as this is not actually for a microbatch, -10000 for total time taken writing 1000 microbatches
        # by a single client and edgeId is used as the nodeId in this case while usually its a fog nodeId
        timestamp_stress = "-1, -10000, " +  ",stress test write req,starttime = "+repr(time.time())+","
        while(process_index < 4):
            w_process = multiprocessing.Process(target=myEdge.writeUsingProcess, args=(START, streamId, byteArray, process_index, return_dict))
            write_processes.append(w_process)
            w_process.start()
            process_index = process_index + 1
            START = START + 100

        for p in write_processes:
            p.join()
        timestamp_stress = timestamp_stress +"endtime = " + repr(time.time()) + '\n'
	#write this to the log
	myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_stress)
        myLogs.close()

        #now lets collect all the values and create the total dictionary
        return_values = return_dict.values()
        for value in return_values:
            fReplicationMap = value[0]
            microbatchIdMap = value[1]
            for key in fReplicationMap:
                if key not in fogReplicaMap:
                    fogReplicaMap[key] = fReplicationMap[key]
                else:
                    fogReplicaMap[key] = fogReplicaMap[key] + fReplicationMap[key]
            for key in microbatchIdMap:
                if key not in yetAnotherMap:
                    yetAnotherMap[key] = microbatchIdMap[key]

    else:
        print "Wrong choice 1: register, 2: write 3: read 4: find"

    replicaDistr = str(fogReplicaMap)
    file = open("./edgefs/distribution.txt",'w')
    file.write(replicaDistr)
    file.close()
    print "the distribution file is written at /edgefs/distribution.txt",replicaDistr

    microbatchDist = str(yetAnotherMap)
    file = open("./edgefs/MB_dist.txt",'w')
    file.write(microbatchDist)
    file.close()

    print "the distribution file is written at /edgefs/MB_dist.txt",microbatchDist





#call the main
# main()

#######################################ADDITIONAL FOR DEBUG THINGS ##############################################
