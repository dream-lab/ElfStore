import sys
sys.path.append('../gen-py')

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
from edgeclient import *

class EdgeServiceHandler:
    def __init__(self):
        pass
    
    def storageSpaceAtEdge(self):
        print "Yes"
        return "A"


if __name__ == '__main__':
    print sys.path
    handler =  EdgeServiceHandler()
    processor = EdgeService.Processor(handler)
    transport = TSocket.TServerSocket(host='127.0.0.1', port=9100)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()
    #the server is kept single single-threaded for now
    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    #for multi-threaded server , use the following
    #server = TServer.TThreadedServer(processor, transport, tfactory, pfactory)
    
    print "Starting the server"
    server.serve()
    print "Server Exiting"
       
        

        