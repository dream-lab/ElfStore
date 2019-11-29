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


'''
We can read from file or do something else for metadata 
'''
def getMetaKeyVal():
    metaKeyValMap = {}
    metaKeyValMap["1"] = ["dream"]
    metaKeyValMap["2"] = ["lab"]
    metaKeyValMap["3"] = ["iisc"]
    # metaKeyValMap["4"] = "bgl"

    return  metaKeyValMap