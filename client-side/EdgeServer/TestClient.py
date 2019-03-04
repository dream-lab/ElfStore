#!/usr/bin/env python

#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements. See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership. The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License. You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.
#

import sys
import glob
sys.path.append('../gen-py')

from EdgeServices import EdgeService
from EdgeServices.ttypes import *
import time
import logging
logging.basicConfig()

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol


def main():
    # Make socket
    transport = TSocket.TSocket('10.24.240.206', 8000)

    # Buffering is critical. Raw sockets are very slow
    transport = TTransport.TFramedTransport(transport)

    # Wrap in a protocol
    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    # Create a client to use the protocol encoder
    client = EdgeService.Client(protocol)

    # Connect!
    transport.open()

    # client.pong()
    # print('ping()')


    microbatchId = "sheshadri"
    metaData = Metadata()
    metaData.mbId = microbatchId
    metaData.streamId = "cds"
    metaData.timestamp = time.time()

    myFile = open("krishna.java",'r')
    data = myFile.read()

    # response  = client.write(microbatchId, metaData,data)
    # if(response==1):
    #     print "success",response
    # else:
    #     print "failure"

    readResponse = client.read(microbatchId, 0)
    if(readResponse.status==1):
        print "something to read"
        print "got ",len(readResponse.data), " bytes of data"

        if(readResponse.metadata != None):
            print "metadata requested ",readResponse.metadata

        else:
            print "no metadata requested "

    else:

        print "no read avaiblable"

  
    # Close!
    transport.close()


# def writeTest(microbatchId, metadata, data):



    



if __name__ == '__main__':
    try:
        main()
    except Thrift.TException as tx:
        print('%s' % tx.message)
