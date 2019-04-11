import sys
sys.path.append('/edgefs/gen-py')
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

        print "ip ",ip," port",port," choice ", "open scoket connection"
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

        print "came here"

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

        timestamp_record = "register edgeJoin,starttime = "+str(time.time())+","
        print "Register Edge device method was called.."
        print "NodeId ",nodeId," Node IP ",nodeIp," port ",port, " reliability ",reliability

        
        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT, FOG_SERVICE)

        #register Edge device
        storage, util = self.encodeFreeSpace(storage_sent)
        edgeDevice = EdgeInfoData(nodeId,nodeIp,port,reliability,storage)
        client.edgeJoin(edgeDevice)
       
        self.closeSocket(transport)
        timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'
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

        #/home/swamiji/eclipse-workspace/EdgeFS_Europar/EdgeServer/Audio_02_06_2019_20_57_02.mp3
         
        #Read data and send it     
        # path = '/home/swamiji/phd/myFile.txt'
        #path = filePath
        #file = open(path,'r')
        #data = file.read() # add try catch here
        print "read the file ",len(data)

        #statinfo = os.stat(path)
        #dataLength = statinfo.st_size              
        encodedSpace,util = self.encodeFreeSpace(len(data)) #this is the length of the file        

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
        metaData = Metadata()
        metaData.mbId = microbatchID
        metaData.streamId = streamId
        metaData.timestamp = time.time() * 1000
        additional_prop = {}
        additional_prop["Name"] = "Sheshadri"
        metaData.properties = json.dumps(additional_prop)


        print EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,encodedSpace
        edgeInfo = EdgeInfoData(EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,encodedSpace) 
        print "here also ",edgeInfo
        print "encodedSpace ",encodedSpace


        timestamp_record_getWrite=  microbatchID  +   ","  +   str(-100) +  ", local, "  + "getWriteLocations ,starttime = " + str(time.time())  + ","
        result = myClient.getWriteLocations(encodedSpace,metaData,blackListedFogs,edgeInfo) #datalength,
        timestamp_record_getWrite = timestamp_record_getWrite +"endtime = " + str(time.time()) +" , " + str(sizeChoice) + '\n'
	
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


        #myLogs = open(BASE_LOG+ +'logs.txt','a')
        # myLogs.write(timestamp_record1)
        #myLogs.close()

        print "the write locations are ",result

	index = 1
	timestamp_record = microbatchID+  ",-1, local ,write req,starttime = " +   str(time.time()) +    ","        
	processes = []
        #loop is for different fogs(and edges) returned 
        for writable in result:
            writeProcess = multiprocessing.Process(target=self.writeToEdge,args=(writable,microbatchID,streamId,data,EDGE_ID,index,sizeChoice))
	    processes.append(writeProcess)
            writeProcess.start()
	    index = index + 1
        
	for p in processes:
	    p.join()
	
	print "all writes to replicas finished "     
        self.closeSocket(transport)

        timestamp_record = timestamp_record +"endtime = " + str(time.time())+" , " + str(sizeChoice) + '\n'
        myLogs = open(BASE_LOG+ 'logs.txt','a')
        myLogs.write(timestamp_record)
	myLogs.write(timestamp_record_getWrite)#write timestamp for getWrite Locations
        myLogs.close()

  


    # Write to either fog or edge depending on the result
    def writeToEdge(self,writable,microbatchID,streamId,data,EDGE_ID,index,sizeChoice):

	device = ""
	if(writable.edgeInfo!=None):
		device = "local"
	else:
		device = str(writable.node.nodeId)

	localTime = str(time.time())
        timestamp_record = microbatchID  + ","  +  str(index) +  "," +  device   + "," +    "write req,starttime = "+localTime+","
	
        print "got the writable ",writable," the microbatchID is ",microbatchID

        nodeInfo = writable.node #NodeInfoData 
        writePreference = writable.preference
        reliability = writable.reliability
        edgeInfoData = writable.edgeInfo # this is an optional field

        metaData = Metadata()
        metaData.mbId = microbatchID
        metaData.streamId = streamId
        metaData.timestamp = time.time()
        additional_prop = {}
        additional_prop["Name"] = "Sheshadri"
        metaData.properties = json.dumps(additional_prop)


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

	    #local read -150
	    timestamp_record_local = microbatchID+","+str(-150)+",local,write req,starttime = "+localTime+","       
            response = client.write(microbatchID,metaData,data)
	    timestamp_record_local = timestamp_record_local +"endtime = " + str(time.time())+" , " + str(sizeChoice) + '\n'
	    
            print "response from the edge ",response

            transport.close()

            #update the metadata structures for the Fog
            client,transport = self.openSocketConnection(nodeInfo.NodeIP,nodeInfo.port,FOG_SERVICE)

            #byte insertMetadata(1: Metadata mbMetadata, 2: EdgeInfoData edgeInfoData);
            edgeInfoData = EdgeInfoData()
            edgeInfoData.nodeId = EDGE_ID
            edgeInfoData.nodeIp = EDGE_IP
            edgeInfoData.port = EDGE_PORT
            edgeInfoData.reliability = EDGE_RELIABILITY            
            edgeInfoData.storage = 12 #This value is not useful for computation


	    #metadata read -50
	    timeMetadata = microbatchID+","+str(-50)+",local ,metadata req,starttime = "+str(time.time())+","
            response = client.insertMetadata(metaData, edgeInfoData)
	    timeMetadata = timeMetadata + " endTime = "+str(time.time())+" , " + str(sizeChoice)+'\n'


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

            response = client.write(metaData, data, writable.preference)
	
	
            print "the response from the fog for write ",response
            self.closeSocket(transport)

        timestamp_record = timestamp_record +"endtime = " + str(time.time())+" , " + str(sizeChoice) + '\n'
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

        timestamp_record = microbatchID+",read req,starttime = "+str(time.time())+","
        response = client.read(microbatchID,True,True,True,edgeInfoData,True)#last bit is for recovery
        timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'

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
    def registerStream(self, streamId):

        print "fog ip, fog port ",FOG_IP,FOG_PORT
        # streamId = "serc1"

        streamPayload = StreamMetadata()
        streamPayload.startTime = time.time()*1000
        streamPayload.reliability = float(STREAM_RELIABILITY)
        streamPayload.minReplica = 2
        streamPayload.maxReplica = 5
        print "The reliability is set to ",streamPayload.reliability
        print "The min replica is ",streamPayload.minReplica , "\n The max replica is ",streamPayload.maxReplica 

        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
        timestamp_record = ",register stream req,starttime = "+str(time.time())+","

        response = client.registerStream(streamId,streamPayload)
        timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'
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

        timestamp_record = microbatchId+ ",23, local ,find req,starttime = "+str(time.time())+","

        response = client.find(microbatchId,True,True,edgeInfoData)

        timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'
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
                
                 timestamp_record = microbatchId+", 25 , "+ str(findReplica.node.nodeId) + " , Read req,starttime = "+str(time.time())+","
                 response = client.read(microbatchId,0) #this is for recovery
                 timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'
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

                 timestamp_record = microbatchId+", 27 ,"+str(findReplica.node.nodeId)  + ",write req,starttime = "+str(time.time())+","
                 response = client.read(microbatchId,0)
                 timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'
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
        timestamp_process = "-1, -1000, " + str(process_index) + ",stress test write process req,starttime = "+str(time.time())+","
        while(numWrites<38):# original value is 100
            myEdge.writeRequestToFog(str(start),streamId,newFilePath,byteArray[recycle],fogReplicaMap, yetAnotherMap)
	    numWrites = numWrites + 1
	    start = start + 1
	    recycle = (recycle + 1)%10
        timestamp_process = timestamp_process +"endtime = " + str(time.time()) + '\n'
        #writing of logs should happen here
	myLogs = open(BASE_LOG+ "logs.txt",'a')
        myLogs.write(timestamp_process)
        myLogs.close()

        return_dict[process_index] = (fogReplicaMap, yetAnotherMap)


#MAIN MEthod
if __name__ == '__main__':

    myEdge = EdgeClient()
       
    if(len(sys.argv)!=14):
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
    if(choice==0):        
        myEdge.readFromEdge(microbatchid)
    elif(choice==1):
        print "Register Stream "
        myEdge.registerStream(streamId)        
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
	
	filePath = "/edgefs/microbatch_data/microbatch"	

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
        while(numWrites<100):
            myEdge.writeRequestToFog(str(START),streamId,newFilePath,byteArray[recycle],fogReplicaMap, yetAnotherMap,sizeChoice)
	    numWrites = numWrites + 1
	    START = START + 1
	    recycle = (recycle + 1)%10
    elif(choice==6):

	numReads = 0
	while(numReads<100):

	    sample = random.randint(0,1599)
	    print "find and get with the id =>",sample
	    myEdge.findAndRead(str(sample))
	    numReads = numReads + 1
    elif(choice==7):
        print "Stress Testing"
	
	filePath = "/edgefs/microbatch_data/microbatch"

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
        timestamp_stress = "-1, -10000, " +  ",stress test write req,starttime = "+str(time.time())+","        
        while(process_index < 4):
            w_process = multiprocessing.Process(target=myEdge.writeUsingProcess, args=(START, streamId, byteArray, process_index, return_dict))
            write_processes.append(w_process)
            w_process.start()
            process_index = process_index + 1
            START = START + 100

        for p in write_processes:
            p.join()
        timestamp_stress = timestamp_stress +"endtime = " + str(time.time()) + '\n'
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
    file = open("/edgefs/distribution.txt",'w')
    file.write(replicaDistr)
    file.close()
    print "the distribution file is written at /edgefs/distribution.txt",replicaDistr

    microbatchDist = str(yetAnotherMap)
    file = open("/edgefs/MB_dist.txt",'w')
    file.write(microbatchDist)
    file.close()

    print "the distribution file is written at /edgefs/MB_dist.txt",microbatchDist



        
    
#call the main 
# main()  

#######################################ADDITIONAL FOR DEBUG THINGS ##############################################
