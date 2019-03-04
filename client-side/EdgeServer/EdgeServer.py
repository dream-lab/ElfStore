import sys

sys.path.append('/edgefs/gen-py')

import math
import random
import time
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TNonblockingServer
from fogclient import FogService
from fogclient.ttypes import *
from EdgeServices import EdgeService
from EdgeServices.ttypes import *
from thrift.server import TServer
from threading import Thread

import time
import os
import collections
import json
from pprint import pprint

import time
import os
import collections
import multiprocessing
import pickle

from os import listdir
from os.path import isfile, join

FOG_IP = ""
FOG_PORT = 0

EDGE_ID = 0
EDGE_IP = 0
EDGE_PORT = 0
EDGE_RELIABILITY = 0
ENCODED_SPACE = 2000
DISK_UTIL = 90
BASE_LOG = "/logs/"
DATA_PATH = "/edgefs/"

def returnDiskSpace():

    if( hasattr(os,'statvfs')):
        st = os.statvfs("/") #The root part
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

#Encode free space available in disk, (7-bit) 1st bit for GB/MB order, 2nd bit for encoding , 5 bits for units
def encodeFreeSpace(space):

    disk_space_in_MB,util = returnDiskSpace()
    # disk_space_in_MB = space #remove this before using
    # print "Free space in GB ",int(free_space_GB)," Free Space in MB ",int(free_space_MB), " Free Space in KB ",int(free_space_KB)    
    print "Free space in MB ",int(disk_space_in_MB)

    disk_space = 0
    free_space_GB = disk_space_in_MB/1000.0

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

    print "The disk_space is ",int(disk_space)," checking with decoder ",decodeLogic(disk_space)

    return int(disk_space),util 

#Test Function to check decode logic
def decodeLogic(diskSpace):        

    if (diskSpace>=-128 and diskSpace<-27): 
        diskSpace = (diskSpace + 128)*10
    
    elif(diskSpace>=-27 and diskSpace<=72):
        diskSpace = 1000 + (diskSpace + 28)*100 
    
    else:
        diskSpace = 11000 + (diskSpace - 72 )*1000

    print diskSpace;

# Return the fog client instance to talk to the fog
def openSocketConnection(ip,port):
    # Make socket
    transport = TSocket.TSocket(FOG_IP, FOG_PORT)

    # Buffering is critical. Raw sockets are very slow
    transport = TTransport.TFramedTransport(transport)

    # Wrap in a protocol
    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    # Create a client to use the protocol encoder
    client = FogService.Client(protocol)

    # Connect!
    transport.open()

    return client,transport

#Close connection
def closeSocket(transport):

    print "closing connection"
    transport.close()

#collect disk stats
def prepareDiskStatPayload(edgePayload, prevDiskSpace):

    freeDiskSpace,util = encodeFreeSpace(ENCODED_SPACE)
    
    if(util>DISK_UTIL):
        print "Watermark level",DISK_UTIL,"%", " breached!! disk utilization now at ",util, "%"        

    if(prevDiskSpace!=freeDiskSpace):
        edgePayload.encodedStorage = freeDiskSpace
        prevDiskSpace = freeDiskSpace

    return edgePayload,prevDiskSpace


# Periodically keep sending disk reports
def heartBeat():

    prevDiskSpace = 0
    storagePayloadInterval = 20
    counter = 0

    while True:

        edgePayload = EdgePayload(EDGE_ID) #only the edge ID

        if(counter%storagePayloadInterval==0): #include payload from storage
            edgePayload, prevDiskSpace = prepareDiskStatPayload(edgePayload, prevDiskSpace)
		

	try :
	
		client,transport = openSocketConnection(FOG_IP,FOG_PORT)
		print "About to send heartbeat"
	        result = client.edgeHeartBeats(edgePayload)
		print "heartbeat response recived ",result
		closeSocket(transport)
	except Exception as e:

		print "The exception is caught ",e
		print "Socket Error "
	
        print "Hearbeat sent"
        counter = counter + 1
        time.sleep(10)

#Send periodic updates 
def sendPeriodicUpdates():

    print "The fog IP ",FOG_IP," fog port ",FOG_PORT

    #client,transport = openSocketConnection(FOG_IP,FOG_PORT)

    #keep sending disk reports
    heartBeat()
   
    # Close!
    #closeSocket(transport)

#Register the fog device to start sending heartbeats
def registerEdgeDevice(nodeId,nodeIp,port,reliability,storage_sent):
    print "Register Edge device method was called.."
    print "NodeId ",nodeId," Node IP ",nodeIp," port ",port, " reliability ",reliability

    
    client,transport = openSocketConnection(FOG_IP,FOG_PORT)

    #register Edge device
    storage, util = encodeFreeSpace(storage_sent)
    edgeDevice = EdgeInfoData(nodeId,nodeIp,port,reliability,storage)
    client.edgeJoin(edgeDevice)
   
    closeSocket(transport)    


class EdgeServiceHandler:
    def __init__(self):
        self.log = {}

    def pong(self):
        print('ping()')
        time.sleep(10)

    def add(self, n1, n2):
        print('add(%d,%d)' % (n1, n2))
        return n1 + n2

    def write(self,mbId,mbMetadata,mbData):
	
    	print "Got the write request",len(mbData)
	
	#write two files, one data , one metadata
        timestamp_record = ",write req edge server,starttime = "+str(time.time())+","
      	with open(DATA_PATH + mbId + ".data",'w') as file:
            file.write(mbData)

        timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'

        #yLogs = open(BASE_LOG+ str(EDGE_ID)+'_logs.txt','a')
        #yLogs.write(timestamp_record)
        #yLogs.close()

        timestamp_record = ",write req,starttime = "+str(time.time())+","
	with open(DATA_PATH + mbId + ".meta",'w') as file:
            pickle.dump(mbMetadata, file, pickle.HIGHEST_PROTOCOL)

        timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'
        #yLogs = open(BASE_LOG+ str(EDGE_ID)+'_logs.txt','a')
        #yLogs.write(timestamp_record)
        #yLogs.close()
	
	#if reached this point
    	return 1

    def insert(self,data):
        print "Got the data ",len(data)
        with open(DATA_PATH+mbId+".data",'w') as file:
            file.write(data)

        print "Write completed"        
        return 1

    def read(self, mbId, fetchMetadata):

    	print "Read request came",mbId, fetchMetadata
        response = ReadReplica()  
        response.status = 0

        files = [f for f in listdir(DATA_PATH) if isfile(join(DATA_PATH, f))]

        if mbId+".data" not in files:
            print "cannot find the requested microbatch "                               

            return response

        
        with open(DATA_PATH+mbId+".data",'r') as file:
            print "Found the request micro batch "
            response.status = 1

            timestamp_record = ",Edge Server read data,starttime = "+str(time.time())+","
	    print "Entered the file read area "
            response.data = file.read()
	    print "Leaving the file read region"
            timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'

            #myLogs = open(BASE_LOG+ str(EDGE_ID)+'_logs.txt','a')
            #myLogs.write(timestamp_record)
            #myLogs.close()
	    print "Exiting file read "

        if(fetchMetadata == 1):
            print "For recovery need to read the metadata "

            timestamp_record = ",Edge Server Read metadata req,starttime = "+str(time.time())+","
            print "entered the metadata read region "
            with open(DATA_PATH+mbId+".meta",'r') as metafile:
                response.metadata = pickle.load(metafile)
	    print "Leaving the metadta read region"
            timestamp_record = timestamp_record +"endtime = " + str(time.time()) + '\n'

            #myLogs = open(BASE_LOG+ str(EDGE_ID)+'_logs.txt','a')
            #myLogs.write(timestamp_record)
            #myLogs.close()
	    print "leaving the log write area "

            if(response.metadata == None) :
                print "pickle file not loaded "
            else:
                print response.metadata

            response.status = 1

        return response    	

    def zip(self):
        print('zip()')


if __name__ == '__main__':

    # encodeFreeSpace(5000)
    # if(len(sys.argv)!=2):
    #     print "Usage: python EdgeServer.py edge_server_conf.txt"
    #     print "Edit values in the file before using"
    #     with open('edge_server_conf.txt','r') as f:
    #         print f.read()
    #     exit(0)


    if(len(sys.argv)!=9):
        print "Usage: python EdgeServer.py 1 127.0.0.1 8000 90 127.0.0.1 9000 path"
        print " the parameters stand for EdgeId, EdgeIp, Port, Reliability, FogIp, Fog port, Path"
        print "length is ",len(sys.argv)
        exit(0)

	# #First register the Edge 
    # with open('edge_server_conf.txt') as data_file:    
    #     config = json.load(data_file)

    # EDGE_ID = int(config["edgeid"])
    # EDGE_IP = config["edgeip"]
    # EDGE_PORT = int(config["edgeport"])
    # EDGE_RELIABILITY = int(config["reliability"])
    # FOG_IP = config["fogip"]
    # FOG_PORT = config["fogport"]
    # DATA_PATH = config["basepath"]
    EDGE_ID = int(sys.argv[1])
    EDGE_IP = sys.argv[2]
    EDGE_PORT = int(sys.argv[3])
    EDGE_RELIABILITY = int(sys.argv[4])
    FOG_IP = sys.argv[5]
    FOG_PORT = int(sys.argv[6])    
    DATA_PATH = sys.argv[7] #where the files will be stored
    BASE_LOG = sys.argv[8]


    print "EDGE ID ",EDGE_ID, " port is ",EDGE_IP," FOG IP ",FOG_IP," FOG PORT ",FOG_PORT

    if not os.path.exists(DATA_PATH):
        os.makedirs(DATA_PATH)

    if not os.path.exists(BASE_LOG):
        os.makedirs(BASE_LOG)        



    registerEdgeDevice(EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,ENCODED_SPACE)
    heartbeatProcess = multiprocessing.Process(target=sendPeriodicUpdates)
    heartbeatProcess.start()

 #    if not os.path.exists(BASE_LOG):
 #        os.makedirs(BASE_LOG)        



 #    registerEdgeDevice(EDGE_ID,EDGE_IP,EDGE_PORT,EDGE_RELIABILITY,ENCODED_SPACE)
 #    heartbeatProcess = multiprocessing.Process(target=sendPeriodicUpdates)
 #    heartbeatProcess.start()

    handler = EdgeServiceHandler()
    processor = EdgeService.Processor(handler)
    transport = TSocket.TServerSocket(host="127.0.0.1", port=8080)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()
    # server =TNonblockingServer.TNonblockingServer(processor, transport)
    #server =TServer.TThreadPoolServer(processor, transport, tfactory, pfactory)
    #newServer = TServer.TForkingServer(processor, transport)
    print('Starting the server...')
    server = TServer.TThreadedServer(processor, transport, tfactory, pfactory)
    server.serve()

    # # You could do one of these for a multithreaded server
    # # server = TServer.TThreadedServer(
    # #     processor, transport, tfactory, pfactory)
    # # server = TServer.TThreadPoolServer(
    # #     processor, transport, tfactory, pfactory)

    # print('Starting the server...')
    # server.serve()
    # print('done.')