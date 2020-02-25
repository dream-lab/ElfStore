import sys
sys.path.append('./gen-py')
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from fogclient import FogService
from fogclient.ttypes import *
from EdgeServices import EdgeService
from EdgeServices.ttypes import *
import time
import os
import contextlib

if os.path.isdir("./DataAndLogs") == False:
    os.mkdir("./DataAndLogs")

## the file logs.txt will be created later
BASE_LOG = "./DataAndLogs/"
FOG_SERVICE = 0
SESSION_SECRET = 'test'

START = int()
END = int()
EDGE_ID = int()
EDGE_IP = str()
EDGE_PORT = int()
EDGE_RELIABILITY = int()
FOG_IP = str()
FOG_PORT = int()
JSON_RESPONSE = dict()
VERSION = -1

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

class EdgeClient:
    def __init__(self):
        self.log = {}

    def init_consistency_model(self, consistency_type):
        self.consistency_type = consistency_type
        if self.consistency_type == 1:
            print("Consistency : RYW")
        elif self.consistency_type == 2:
            print("Consistency : MR")
        elif self.consistency_type == 3:
            print("Consistency : Causal")
        else:
            print("Consistency : Vanilla ElfStore")

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
        return client,transport

    #Close connection
    def closeSocket(self,transport):

        print("closing connection")
        transport.close()

    def findStreamIdAndHomeFog(self, microbatchID):

        print("findStreamIdAndHomeFog() method called ")
        print(": The Fog ip ", FOG_IP," The Fog port is ",FOG_PORT)
        client,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
        response = client.findStreamByBlockId(microbatchID, str(EDGE_ID), True, True)
        self.closeSocket(transport)

        # if response.homefog == None:
        #     homefog = NodeInfoData()
        #     homefog.nodeId = 1
        #     homefog.NodeIP = FOG_IP
        #     homefog.port = FOG_PORT
        #
        #     streamId = "str"
        #     response.homefog = homefog
        #     response.streamid = streamId

        return response

    #find and read replicas for microbatches
    def findAndUpdate(self,microbatchID,filePath, data, fogReplicaMap, sizeChoice,setLease,metaKeyValueMap, sessionMap, timeout, pushbased, read_session_map, write_session_map):

        global VERSION

        log_record = ""
        edgeInfoData = EdgeInfoData()
        edgeInfoData.nodeId = EDGE_ID
        edgeInfoData.nodeIp = EDGE_IP
        edgeInfoData.port = EDGE_PORT
        edgeInfoData.reliability = EDGE_RELIABILITY
        edgeInfoData.storage = 12

        # streamHomeFogResponse = self.findStreamIdAndHomeFog(microbatchID)

        ''' 
        Create a session log that needs to be passed in the updateMetadata for causal consistency
        '''
        mySessionLog = SessionLog()
        writeList = []
        parentFogList = []
        versionList = []
        version = 0

        for microbatchID in sessionMap:
            writeList.append(microbatchID)
            versionList.append(sessionMap[microbatchID][0])
            parentFogList.append(sessionMap[microbatchID][1])

        mySessionLog.updatedMicrobatchList = writeList
        mySessionLog.parentFogList = parentFogList
        mySessionLog.versionList = versionList

        # A crucial change
        microbatchID = START
                
        print("DEBUG : The Fog ip ", FOG_IP," The Fog port is ",FOG_PORT)
        myClient,transport = self.openSocketConnection(FOG_IP,FOG_PORT,FOG_SERVICE)
        timestamp_record = "\n"+ str(microbatchID) +",find, local,"+",starttime = "+repr(time.time())+","
        startTime = time.time()
        # print("Update api The pushbased flag is ",pushbased)
        # if pushbased is False:
        #     print("Normal find op pushbased flag => ", pushbased)
        #     response = client.find(microbatchID,0,None,True,True,edgeInfoData,str(EDGE_ID))
        # else :
        #     response = client.findPushModel(microbatchID, version, None, True, True, edgeInfoData, timeout, str(EDGE_ID))

        endTime = time.time()
        log_record = log_record + ",findTime="+str(endTime-startTime)
        timestamp_record = timestamp_record +"endtime = " + repr(time.time()) + '\n'

        myLogs = open(BASE_LOG+ 'oldlogs.txt','a')
        myLogs.write(timestamp_record+"\n")
        myLogs.close()

        metaData = Metadata()
        metaData.clientId = CLIENT_ID
        metaData.sessionSecret = SESSION_SECRET
        metaData.mbId = microbatchID
        metaData.streamId = STREAM_ID
        metaData.timestamp = int(time.time() * 1000)
        print("Going to update the ",metaData)

        '''
        Acquire lock
        '''
        # print("Acquiring data for updateBlock")
        # myClient,transport = self.openSocketConnection(streamHomeFogResponse.homefog.NodeIP,streamHomeFogResponse.homefog.port,FOG_SERVICE)
        # lock_status = myClient.acquireLockForUpdateBlock(microbatchID, streamHomeFogResponse.streamid, str(EDGE_ID))
        # while(lock_status == False):
        #     '''
        #     Acquiring lock on the streamid obtained from the response
        #     '''
        #     lock_status = myClient.acquireLockForUpdateBlock(microbatchID, streamHomeFogResponse.streamid, str(EDGE_ID))
        #     print("The lock status here is ",lock_status)
        #     time.sleep(random.randint(1,5))

        startTime = time.time()

        updateResponse = None
        parentFog = None
        if microbatchID in sessionMap:
            parentFog = sessionMap[microbatchID][1]
        if self.consistency_type != 3:
            updateResponse = myClient.updateBlockQuorum(microbatchID, metaData, data, str(EDGE_ID), PutPreference.ALL,
                                                        edgeInfoData, parentFog)
        else:
            updateResponse = myClient.updateBlockCausalQuorum(microbatchID, metaData, data, mySessionLog, str(EDGE_ID),
                                                              PutPreference.ALL, edgeInfoData, parentFog)

        endTime = time.time()

        log_record = log_record + ",update=" + str(endTime - startTime)
        print("The updateResponse is ",updateResponse)

        if self.consistency_type == 1 or self.consistency_type == 3:
            print("RYW/Causal consistency model (update operation) ")
            sessionList = []
            sessionList.append(updateResponse.version)
            sessionList.append(updateResponse.fogHint)

            if microbatchID in sessionMap:
                if updateResponse.version > sessionMap[microbatchID][0] :
                    sessionMap[microbatchID] = sessionList
                    write_session_map[microbatchID] = sessionList
            else:
                sessionMap[microbatchID] = sessionList
                write_session_map[microbatchID] = sessionList

        # lock_status = myClient.releaseLockForUpdateBlock(microbatchID, streamHomeFogResponse.streamid, str(EDGE_ID))
        # print("Released Lock status is => ",lock_status)
        #print("The sessionmap is ",sessionMap)

        retVal = 1
        return retVal, sessionMap, log_record, read_session_map, write_session_map


def update(path,streamId,start,metaKeyValueMap,fogIp,fogPort,edgeId,edgeIP,edgePort, edgeReliability,clientId,splitChoice,setLease,leaseDuration,compFormat,sessionMap, consistency_type, pushbased, timeout, read_session_map, write_session_map, verbose = False):
    if(metaKeyValueMap == None):
        metaKeyValueMap = {}
        metaKeyValueMap["cds"] = "dream"
    log_record = "LOG,update" +",clientid="+str(clientId)+ ",streamId="+ str(streamId)+",mbId="+str(start)+",sizeofmetadata="+str(len(str(metaKeyValueMap)))+",sizeofsessionlog="+str(len(sessionMap)) +",sizeofreadsessionlog="+str(len(read_session_map)) + ",sizeofwritesessionlog="+str(len(write_session_map)) + ",fog="+fogIp+":"+str(fogPort) + ",pushbased="+str(pushbased)
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
    global EDGE_IP
    EDGE_IP = edgeIP
    global EDGE_PORT
    EDGE_PORT = int(edgePort)
    global EDGE_RELIABITLITY
    EDGE_RELIABILITY = int(edgeReliability)
    global CLIENT_ID
    CLIENT_ID = clientId
    global SPLIT_CHOICE
    SPLIT_CHOICE = int(splitChoice)
    global STREAM_RELIABILITY
    # STREAM_RELIABILITY = myEdge.getStreamMetadataReliability(STREAM_ID)
    global EXPECTED_LEASE
    EXPECTED_LEASE = int(leaseDuration)
    global COMP_FORMAT
    COMP_FORMAT = compFormat

    myEdge.init_consistency_model(consistency_type)

    print("The metaKeyValueMap is ",metaKeyValueMap)

    if setLease == "1":
        setLease = True
    elif setLease == "0":
        setLease = False

    global SET_LEASE
    SET_LEASE = setLease

    fogReplicaMap = {}
    yetAnotherMap = {}

    ## Check if the entity specified by the PATH is a file or a directory
    if(os.path.isfile(PATH)):
        if SPLIT_CHOICE == 1:
            ##i.e write the whole file as a single block. SINGLE BLOCK, no split
            # print("The stream reliability needed is : ", str(STREAM_RELIABILITY))

            filePath = PATH
            byteArray = []

            file = open(PATH,'rb')

            ## Get the file size
            fileInfo = os.stat(PATH)
            fileSizeMB = fileInfo.st_size / 10000000 ## in MB
            print("File size in MB is ",fileSizeMB)
            byteArray.append(file.read())
            file.close()
            newFilePath = ""

            if verbose == True :
                response, sessionMap, fau_record, read_session_map, write_session_map= myEdge.findAndUpdate(START, newFilePath, byteArray[0], fogReplicaMap, fileSizeMB,setLease,metaKeyValueMap, sessionMap, timeout, pushbased, read_session_map, write_session_map)
                log_record = log_record +fau_record
            else:
                with nostdout():
                    response, sessionMap, fau_record, read_session_map, write_session_map = myEdge.findAndUpdate(START, newFilePath, byteArray[0], fogReplicaMap, fileSizeMB,setLease,metaKeyValueMap, sessionMap, timeout, pushbased, read_session_map, write_session_map)
                    log_record = log_record +fau_record
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
            file.close()
            print("number of binary files",len(byteArray))
            newFilePath = ""
            for i in range(0,len(byteArray)):
                fileSizeMB = len(byteArray[i]) / 10000000 ## in MB

                if verbose == True :
                    response, sessionMap, fau_record, read_session_map, write_session_map = myEdge.findAndUpdate(START, newFilePath, byteArray[i], fogReplicaMap, fileSizeMB,setLease,metaKeyValueMap, sessionMap, timeout, pushbased, read_session_map, write_session_map)
                    log_record = log_record + fau_record
                    START = START + 1
                else:
                    with nostdout():
                        response, sessionMap, fau_record, read_session_map, write_session_map = myEdge.findAndUpdate(START, newFilePath, byteArray[i], fogReplicaMap, fileSizeMB,setLease,metaKeyValueMap, sessionMap, timeout, pushbased, read_session_map, write_session_map)
                        log_record = log_record + fau_record
                        START = START + 1
                    sys.stdout = sys.__stdout__
            if response == 1:
                print("All writes successfull")
            else:
                print(response)
                print("not successfull")

    #print("DEBUG : (update) The sessionMap being returned is ",sessionMap)

    log_record = repr(time.time())+"," + log_record + ",version="+str(VERSION)
    with open(BASE_LOG+"logs.txt",'a') as logFile:
        logFile.write(log_record+"\n")

    return sessionMap, read_session_map, write_session_map
