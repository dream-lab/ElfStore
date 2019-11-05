import sys
sys.path.append('../gen-py')
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
get the matching preference based on the input passed
'''
def getMatchPref(matchPref):

    try:
        return MatchPreference._VALUES_TO_NAMES[int(matchPref)]
    except:
        return MatchPreference._VALUES_TO_NAMES[int(1)]

'''
get the replica count based on the input passed
'''
def getReplicaCount(replicaCount):

    try:
        return ReplicaCount._VALUES_TO_NAMES[int(replicaCount)]
    except:
        return ReplicaCount._VALUES_TO_NAMES[int(0)]

'''
Testing code, down below
'''
# print(getReplicaCount("1"))
# print(getReplicaCount("-11"))
#
# print(getMatchPref("2"))
# print(getMatchPref("-11"))