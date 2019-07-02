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
import wmi
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

    #incrementBlockCount() has an id of 500
    def increment_block_count(self, metadata,setLease):
        global STREAM_OWNER_FOG_IP
        global STREAM_OWNER_FOG_PORT
        print("Going to increment the last blockId for the stream")

        timestamp_record = str(metadata.mbId) + ", 500,"+ STREAM_OWNER_FOG_IP  + ",increment last blockId,starttime = "+repr(time.time())+","

        print("Lets first issue request to renew the lease for incrementBlockCount()")
        self.renew_lease(metadata.streamId, metadata.clientId, metadata.sessionSecret, 0, ESTIMATE_INCR_BLOCK_COUNT, metadata.mbId,setLease)


        client,transport = self.openSocketConnection(STREAM_OWNER_FOG_IP, STREAM_OWNER_FOG_PORT, FOG_SERVICE)
        #the response type is BlockMetadataUpdateResponse
        response = client.incrementBlockCount(metadata,setLease)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'
        self.closeSocket(transport)

        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.close()

        return response

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
    def openStream(self, stream_id, client_id, expected_lease, blockId,setLease):
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

        client,transport = self.openSocketConnection(STREAM_OWNER_FOG_IP, STREAM_OWNER_FOG_PORT, FOG_SERVICE)

        global TIME_LOCK_ACQUIRED
        global TIME_LEASE_EXPIRE
        global IS_LOCK_HELD

        while True:
            #type of response is OpenStreamResponse
            response = client.open(stream_id, client_id, expected_lease,setLease)
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


    #renewLease() has an id of 400 and the overall time to complete the lock acquiring has an id of 450
    #if the left lease time is more than the time for the operation e.g. putNext or increment block count
    #then the renewLease() should return with a status of 1 and if that doesn't happen, it should be treated
    #as an exception. Similarly if the left lease time is less than the time for the operation, renewLease()
    #might return with a status of 0 and a negative code. In that case, some other client would have acquired
    #the lock on the stream and we will then use the openStream to acquire the lock. To capture whether an
    #exception occurs during this process, we use a code of 475
    def renew_lease(self, stream_id, client_id, session_secret, expected_lease, expected_completion_time, blockId,setLease):
        print("Lets check if there is a need to renew lease")

        #lets divide the renewal result in two parts based on whether the left lease time is more or less than the
        #time taken to complete the operation. In case we have more lease time left than the operation, we will
        #not make call to the server and proceed with the operation
        global TIME_LEASE_EXPIRE
        global TIME_LOCK_ACQUIRED
        current_time = int((time.time() * 1000))
        if TIME_LEASE_EXPIRE - current_time > expected_completion_time:
            #no need to call renewLease now, lets return and perform the operation
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
        if IS_LOCK_HELD == True:
            #for lease renewal, after endtime, adding status flag as well indicating whether it was successful or not
            timestamp_record = str(blockId) + ", 400,"+ STREAM_OWNER_FOG_IP  + ",renew stream lease,starttime = "+repr(time.time())+","

            client,transport = self.openSocketConnection(STREAM_OWNER_FOG_IP, STREAM_OWNER_FOG_PORT, FOG_SERVICE)

            #the response type is StreamLeaseRenewalResponse
            response = client.renewLease(stream_id, client_id, session_secret, expected_lease,setLease)
            if response.status == 1:
                print("renewLease() successful")
                timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + ",1" + '\n'
                timestamp_full = timestamp_full +"endtime = " + repr(time.time()) + '\n'
                fallback = False
                #set the time of new lease start and lease expire time here
                TIME_LOCK_ACQUIRED = int(time.time() * 1000)
                TIME_LEASE_EXPIRE = TIME_LOCK_ACQUIRED + response.leaseTime
            else:
                print("renewLease() failed, falling back to open() api")
                timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + ",0" + '\n'
                IS_LOCK_HELD = False

            self.closeSocket(transport)

        if fallback == True:
            self.openStream(stream_id, client_id, expected_lease, blockId,setLease)
            timestamp_full = timestamp_full +"endtime = " + repr(time.time()) + '\n'
        myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_record)
        myLogs.write(timestamp_full)
        myLogs.close()


    #utility function to return the systems's utilizaion and free space
    def returnDiskSpace(self):
        ## for windows os
        if sys.platform[0:3] == "win":
            wmiObject = wmi.WMI()
            for drive in wmiObject.Win32_LogicalDisk():
                ## get the properties of the corect drive
                if os.getcwd()[0:2] == drive.Name:
                    total = int(drive.Size)
                    free = int(drive.FreeSpace)
                    used = total - free
                    print("Disk ",free," : ",total," : ",used)

                    util = used/float(total)*100
                    disk_space_in_MB = float(free/(1024*1024.0))
                    return disk_space_in_MB,util
        ## for unix, linux or mac
        else:
            if( hasattr(os,'statvfs')):
                #st = os.statvfs("/") #The root part
                st = os.statvfs("/")
                free = st.f_bavail * st.f_frsize
                total = st.f_blocks * st.f_frsize
                used = (st.f_blocks - st.f_bfree) * st.f_frsize
                print("Disk ",free," : ",total," : ",used)

                util = used/float(total)*100
                # print "Disk util is " ,util
                # return GB_part,(MB_part*1024),int(KB_part*1024),int(util)
                disk_space_in_MB = float(free /(1024*1024.0))
                return disk_space_in_MB,util

                return 0 #default return value
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



    def writeRequestToFog(self,microbatchID,streamId,filePath, data, fogReplicaMap, yetAnotherMap,sizeChoice,setLease):

        #/home/swamiji/eclipse-workspace/edgefs_Europar/EdgeServer/Audio_02_06_2019_20_57_02.mp3

        #Read data and send it
        # path = '/home/swamiji/phd/myFile.txt'
        #path = filePath
        #file = open(path,'r')
        #data = file.read() # add try catch here
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
        metaData = Metadata()
        metaData.clientId = CLIENT_ID
        metaData.sessionSecret = SESSION_SECRET
        metaData.mbId = microbatchID
        metaData.streamId = streamId
        metaData.timestamp = int(time.time() * 1000)
        additional_prop = {}
        additional_prop["Name"] = "Sheshadri"
        metaData.properties = json.dumps(additional_prop)


        #print EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,encodedSpace
        #edgeInfo = EdgeInfoData(EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,encodedSpace)
        #print "here also ",edgeInfo
        print("encodedSpace ",encodedSpace)


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

        print("the write locations are ",result)

        timestamp_record = str(microbatchID) +  ",-1, local ,write req,starttime = " +   repr(time.time()) +    ","

        #lets renew the lease. The behaviour should adhere with the policy of the lease time
        #left in comparison to the time taken to complete the operation
        print("Lets first issue request to renew the lease for putNext()")
        self.renew_lease(metaData.streamId, metaData.clientId, metaData.sessionSecret, 0, ESTIMATE_PUT_NEXT, metaData.mbId,setLease)

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

        print("all writes to replicas finished ")
        self.closeSocket(transport)

        timestamp_record = timestamp_record +"endtime = " + repr(time.time())+" , " + str(sizeChoice) + '\n'
        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.write(timestamp_record_getWrite)#write timestamp for getWrite Locations
        myLogs.close()

        #the response type is BlockMetadataUpdateResponse
        response = self.increment_block_count(metaData,setLease)
        if response.code == -1:
            #this blockId is already written
            #In our designed experiment, different clients are writing to different regions so this
            #issue will not occur. However this is kept to indicate that such a scenario can occur
            #with concurrent clients
            print("BlockId : " + str(microbatchID) + " is already written, failing the write")
            return -1
        elif response.code == -2:
            #lease expired
            print("Lease expired, should renew the lease before trying again")
            return -2
        elif response.code == -3:
            #client does not hold the lock
            print("Client does not hold the lock, should open the stream before writing")
            return -3
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
        timestamp_record = str(microbatchID)  + ","  +  str(index) +  "," +  device   + "," +"write req,starttime = "+localTime+","
        #print "got the writable ",writable," the microbatchID is ",microbatchID

        nodeInfo = writable.node #NodeInfoData
        writePreference = writable.preference
        reliability = writable.reliability
        edgeInfoData = writable.edgeInfo # this is an optional field

        #Write to the edge
        if edgeInfoData != None :

            #print "ip ",edgeInfoData.nodeIp," port",edgeInfoData.port
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

            #print "response from the edge ",response.status

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
            timeMetadata = str(microbatchID) +","+str(-50)+",local ,metadata req,starttime = "+repr(time.time())+","
            response = client.insertMetadata(metaData, edgeInfoData)
            timeMetadata = timeMetadata + " endTime = "+repr(time.time())+" , " + str(sizeChoice)+'\n'


            myLogs = open(BASE_LOG+ 'logs.txt','a')
            myLogs.write(timestamp_record_local)
            myLogs.write(timeMetadata)
            myLogs.close()
            #print "The response from the fog is ", response

            self.closeSocket(transport)

        else :

            #print "Have to talk to a fog with preference "
            # byte write(1:Metadata mbMetadata, 2:binary data, 3:WritePreference preference);
            client,transport = self.openSocketConnection(nodeInfo.NodeIP,nodeInfo.port,FOG_SERVICE)

            #response is now a WriteResponse and not a byte
            response = client.putNext(metaData, data, writable.preference)


            #print "the response from the fog for write ",response.status
            self.closeSocket(transport)

        timestamp_record = timestamp_record +"endtime = " + repr(time.time())+" , " + str(sizeChoice) + '\n'
        #print "the time stamp for write request is ",timestamp_record

        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
        myLogs.close()

def put(path,streamId,start,fogIp,fogPort,edgeId,clientId,splitChoice,setLease,verbose = False):
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
                response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[0], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease)
            else:
                with nostdout():
                    response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[0], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease)
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
            print(str(setLease))
            newFilePath = ""
            for i in range(0,len(byteArray)):
                fileSizeMB = len(byteArray[i]) / 10000000 ## in MB

                if verbose == True :
                    response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease)
                    START = START + 1
                else:
                    with nostdout():
                        response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease)
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
                    response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease)
                    START = START + 1
                else:
                    with nostdout():
                        response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease)
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
                    response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease)
                    START = START + 1
                else:
                    with nostdout():
                        response = myEdge.writeRequestToFog(START, STREAM_ID, newFilePath, byteArray[i], fogReplicaMap, yetAnotherMap, fileSizeMB,setLease)
                        START = START + 1
                    sys.stdout = sys.__stdout__
            if response == 1:
                print("...")
                print("All writes successfull")
            else:
                print(response)
                print("not successfull")
