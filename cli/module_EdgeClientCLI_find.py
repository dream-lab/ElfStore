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

import module_EdgeClientCLI_ls

if os.path.isdir("./DataAndLogs") == False:
    os.mkdir("./DataAndLogs")

## the file logs.txt will be created later
BASE_LOG = "./DataAndLogs/"
FOG_SERVICE = 0

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



def find(mbid,fogIp,fogPort,verbose = False):
    ## Trace of this function:
    ## 1. Call lsMbIdSystem from module_EdgeClientCLI_ls with groupBy = 2 (i.e group by mbid)
    ## 2. Get the edge list for the desired mbid, convert it to a set and display.

    ## 1. Call lsMbIdSystem from module_EdgeClientCLI_ls with groupBy = 2 (i.e group by mbid)
    choice = 20
    groupBy=2

    try:
        mbids = module_EdgeClientCLI_ls.ls(fogIp,fogPort,choice,groupBy,verbose)
        #str(list(set(mbids[mbid])))
        print("Edges: "+str(list(set(mbids[int(mbid)]))))
    except KeyError:
        print("Microbatch not in in the system.")
