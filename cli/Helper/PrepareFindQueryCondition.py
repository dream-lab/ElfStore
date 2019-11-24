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
def getFindQueryCondition():
    
    findQueryConditionList = []

    condition1 = FindQueryCondition()
    condition2 = FindQueryCondition()
    condition3 = FindQueryCondition()
    condition4 = FindQueryCondition()
    condition5 = FindQueryCondition()
    condition6 = FindQueryCondition()

    condition1.key, condition1.value = "1","cds"
    condition2.key, condition2.value = "1","cds"
    condition3.key, condition3.value = "2","dream"
    condition4.key, condition4.value = "1","cds"
    condition5.key, condition5.value = "2","dream"
    condition6.key, condition6.value = "3","iisc"
 
    orList1,orList2,orList3 = [],[],[]

    orList1.append(condition1)
    orList2.append(condition2)
    orList2.append(condition3)
    orList3.append(condition4)
    orList3.append(condition5)
    orList3.append(condition6)

    findQueryConditionList.append(orList1)
    findQueryConditionList.append(orList2)
    findQueryConditionList.append(orList3)

    return  findQueryConditionList
