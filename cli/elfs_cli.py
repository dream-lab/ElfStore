from cmd import Cmd
import argparse
import shlex
import sys
import io
import os
from termcolor import colored
import json
import hashlib
import subprocess
import contextlib

import module_EdgeClientCLI_regstream
import module_EdgeClientCLI_get
import module_EdgeClientCLI_put
import module_EdgeClientCLI_ls
import module_EdgeClientCLI_find

## Global parameters
EDGE_ID = int()
EDGE_IP = str()
EDGE_PORT = int()
EDGE_RELI = float()
FOG_IP = str()
FOG_PORT = int()
LOGS_FOLDER = str()
BASE_LOG = str()
CLIENT_ID = str()
COMP_FORMAT = str()

## Used when the --v (i.e verbose) is set to false
## The output of the python file of the correspoding command is written here
## so that it does not show up in the CLI
class DummyFile(object):
    def write(self, x): pass

@contextlib.contextmanager
def nostdout():
    #save_stdout = sys.stdout
    sys.stdout = DummyFile()
    yield
## end of --v context


class elfsCLI(Cmd):
    prompt = colored('elfs>','yellow')
    intro = "\nElfStore: A Resilient Data Storage Service for Federated Edge and Fog Resources\n"

    def do_regstream(self,args):
        ## here args includes everyting after the invokation command
        ## split the args starting using shlex into tokens
        line = shlex.split(args)
        ## parse the tokens using the previously defined #global parser
        tokens = regstream_parser.parse_args(line)
        if tokens.v == True:
            module_EdgeClientCLI_regstream.regStream(tokens.id,tokens.reli,tokens.fogIp,tokens.fogPort,tokens.minReplica,tokens.maxReplica,True)
            #del jsonResponse
        else:
            module_EdgeClientCLI_regstream.regStream(tokens.id,tokens.reli,tokens.fogIp,tokens.fogPort,tokens.minReplica,tokens.maxReplica)
            #del jsonResponse

    def do_put(self,args):

        ## here args includes everyting after the invokation command
        ## split the args starting using shlex into tokens
        line = shlex.split(args)
        ## parse the tokens using the previously defined #global parser
        tokens = put_parser.parse_args(line)

        splitChoice = str(0)
        setLease = str(0)

        if tokens.singleBlock == True:
            splitChoice = str(1);
        if tokens.setLease == True:
            setLease = str(1)

        if tokens.v == True:
            module_EdgeClientCLI_put.put(tokens.path,tokens.streamId,tokens.start,tokens.metadata, tokens.fogIp,tokens.fogPort,tokens.edgeId,tokens.clientId,splitChoice,setLease,tokens.duration,tokens.comp,True)
        else:
            module_EdgeClientCLI_put.put(tokens.path,tokens.streamId,tokens.start,tokens.metadata, tokens.fogIp,tokens.fogPort,tokens.edgeId,tokens.clientId,splitChoice,setLease,tokens.duration,tokens.comp)


    def do_get(self,args):
        ## here args includes everyting after the invokation command
        ## split the args starting using shlex into tokens
        line = shlex.split(args)
        ## parse the tokens using the previously defined #global parser
        tokens = get_parser.parse_args(line)
        if tokens.v == True:
            module_EdgeClientCLI_get.get(tokens.start, tokens.end, tokens.edgeId, tokens.edgeIp, tokens.edgePort, tokens.edgeReli, tokens.fogIp, tokens.fogPort,True)
            #del jsonResponse
        else:
            module_EdgeClientCLI_get.get(tokens.start, tokens.end, tokens.edgeId, tokens.edgeIp, tokens.edgePort, tokens.edgeReli, tokens.fogIp, tokens.fogPort)
            #del jsonResponse

    def do_ls(self,args):
        ## here args includes everyting after the invokation command
        ## split the args starting using shlex into tokens
        line = shlex.split(args)
        ## parse the tokens using the previously defined #global parser
        tokens = ls_parser.parse_args(line)

        choice = int()
        groupBy = int()

        if tokens.groupBy == "edge":    groupBy = 1
        else :  groupBy = 2

        if tokens.all == True:
            choice = 19
        elif tokens.neighbors == True and tokens.buddies == False:
            choice = 15
        elif tokens.neighbors == False and tokens.buddies == True:
            choice = 16
        elif tokens.neighbors == True and tokens.buddies == True:
            choice = 14
        if tokens.v == True:
            ## slight memory overhead; module is required to return a response since the same module is used by the find module
            response = module_EdgeClientCLI_ls.ls(tokens.fogIp,tokens.fogPort,choice,groupBy,True)
            del response
        else:
            ## slight memory overhead; module is required to return a response since the same module is used by the find module
            response = module_EdgeClientCLI_ls.ls(tokens.fogIp,tokens.fogPort,choice,groupBy)
            del response

    def do_find(self, args):
        ## here args includes everyting after the invokation command
        ## split the args starting using shlex into tokens
        line = shlex.split(args)
        ## parse the tokens using the previously defined #global parser
        tokens = find_parser.parse_args(line)

        if tokens.v == True:
            module_EdgeClientCLI_find.find(tokens.mbid,tokens.blockMeta,tokens.streamMeta,tokens.fogIp,tokens.fogPort,True)
        else:
            module_EdgeClientCLI_find.find(tokens.mbid,tokens.blockMeta,tokens.streamMeta,tokens.fogIp,tokens.fogPort)


    def do_join(self,args):
        ## here args includes everyting after the invokation command
        ## split the args starting using shlex into tokens
        line = shlex.split(args)
        ## parse the tokens using the previously defined #global parser
        tokens = join_parser.parse_args(line)
        ## the following forms the bash command that executes a python file based on the corresponding command
        command = "python module_EdgeClientCLI_join.py "+tokens.configFiles
        ## execute the command
        os.system(command)

    def do_exit(self,inp):
        print("Bye")
        sys.exit(0)

    ## The following functions are used for documentation that can be refered by the user using the 'help' command
    def help_regstream(self):
        print("Register a stream.")
        print("-> Values of all parameters marked with * have to be specified during exection of the command.")
        print("** Usage :  regstream *--streamId=(string) *--streamReli=(short) --v --start=(int) --fogIp=x.x.x.x --fogPort=(int) ")
    def help_put(self):
        print("Put a file in ElfStore.")
        print("-> Perform a put with lease enabled on the stream using the --setLease flag.")
        print("-> Values of all parameters marked with * have to be specified during exection of the command.")
        print("** Usage : put *--path=(string) *--streamId=(string) *--start=(int) --comp --metadata=(jsonFilePath) --duration=(int) --singleBlock --setLease --v --fogIp=x.x.x.x --fogPort=(int) --edgeId=(int) --clientId=(string)")
        print("-> If --singleBlock flag is specified then the file(s) is written as a single block of same size as as that of the file.")
        print("-> The default value is NA, specified in edge confie file. Supported formats\n1.Snappy\n2.Gzip")
        print("-> The duration paramater used to set the lease duration during a put. Default is 90 seconds")
    def help_get(self):
        print("Get file(s) using the microbatchId. If end is not specified then only the first block will be retreived (specified using start).")
        print("-> Values of all parameters marked with * have to be specified during exection of the command.")
        print("** Usage :  get *--start=(int) --end=(int) --v --edgeId=(int) --edgeIp=x.x.x.x --edgePort=(int) --edgeReli=(int) --fogIp=x.x.x.x --fogPort=(int) ")
    def help_ls(self):
        print("List blocks in neighbors, buddy pool or in the whole system. Can by grouped by Edge or MbId")
        print("-> The --neighbors argument is used to indicate listing of blocks present in the neighbors of the default/specified fog.")
        print("-> The --buddies argument is used to indicate listing of blocks present in the buddy pool of the default/specified fog.")
        print("-> The --all argument is used to indicate listing of blocks in the whole system.")
        print("-> Grouping can be done using --groupBy argument either by 'edge' or by 'mbid'.")
        print("-> Values of all parameters marked with * have to be specified during exection of the command.")
        print("** Usage (to list blocks in neighbors):  ls *--neighbors --groupBy=(str) --v --fogIp=x.x.x.x --fogPort=(int)")
        print("** Usage (to list blocks in buddy pool): ls *--buddies --groupBy=(str) --v --fogIp=x.x.x.x --fogPort=(int)")
        print("** Usage (to list blocks in neighbors and buddy pool): ls *--neighbors *--buddies --groupBy=(str) --v --fogIp=x.x.x.x --fogPort=(int)")
        print("** Usage (to list blocks in the entire system): ls *--all --groupBy=(str) --v --fogIp=x.x.x.x --fogPort=(int)")
        print("-> The default value for --groupBy is 'edge'.")
    def help_join(self):
        print("Add new edges to the system, whose properties are defined in the config file.")
        print("-> Values of all parameters marked with * have to be specified during exection of the command.")
        print("-> Usage : join *--configFiles=(pathToConfigFilesFolder)")
    def help_find(self):
        print("Find locations of blocks or find mbIds of blocks that have specific metadata properties (specified in a json file)")
        print("** Usage (Find edges containing specified block): find --mbid=(int) --v")
        print("** Usage (Find blocks with specific metadata properties): find --metadata=(jsonFilePath) --v")
    def help_exit(self):
        print("Exit elfs.")

    ## Called when an empty line is entered in response to the prompt.
    ## If this method is not overridden, it repeats the last nonempty
    ## command entered.(A bug in the cmd module)
    def emptyline(self):
        if self.lastcmd:
            self.lastcmd = ""
            return self.onecmd('\n')


if __name__ == '__main__':
    ## Reading the config file
    CONFIG_FILE = sys.argv[1]
    edgeConfig = json.load(open(CONFIG_FILE,'r'))
    ## Initializing the default #global properties, based on the edge config file.
    #global EDGE_ID
    EDGE_ID = edgeConfig['edgeId']
    #global EDGE_IP
    EDGE_IP = edgeConfig['edgeIp']
    #global EDGE_PORT
    EDGE_PORT = edgeConfig['edgePort']
    #global EDGE_RELI
    EDGE_RELI = edgeConfig['reliability']
    #global FOG_IP
    FOG_IP = edgeConfig['fogIp']
    #global FOG_PORT
    FOG_PORT = edgeConfig['fogPort']
    #global LOGS_FOLDER
    LOGS_FOLDER = edgeConfig['logsFolder']
    #global BASE_LOG
    BASE_LOG = edgeConfig['baseLog']

    ## Hashed based on the edge id, since it is unique.
    #global CLIENT_ID
    CLIENT_ID = hashlib.md5(str(EDGE_ID).encode('utf-8')).hexdigest()

    ## The default compression format is NA
    COMP_FORMAT = edgeConfig['compFormat']

    ##Printing the session information.
    print("Session Information")
    print("Edge ID : " + str(EDGE_ID))
    print("Edge IP : " + EDGE_IP)
    print("Edge Port : " + str(EDGE_PORT))
    print("Edge Reliability : " + str(EDGE_RELI))
    print("Fog IP : " + FOG_IP)
    print("Fog Port : " + str(FOG_PORT))
    print("Storage Location : " + "./DataAndLogs/edge"+str(EDGE_ID)+"_data")
    print("Client ID : " + CLIENT_ID)


    ## NOTE : The user can type the arguments in any order, the only condition is that all the required arguments should be present.
    ## Start be defining #global parser
    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers()

    ## Parser for regstream Command
    ## Arguments :
    ## 1. --streamId
    ## 2. --streamReli
    ## 3. --start
    ## 4. --fogIp (default, based on config file)
    ## 5. --fogPort (default, based on config file)
    ## 6. --minReplica (default = 2)
    ## 7. --maxReplica (default = 5)
    regstream_parser = subparsers.add_parser("regstream")
    regstream_parser.add_argument("--id")
    regstream_parser.add_argument("--reli")
    regstream_parser.add_argument("--fogIp",default = FOG_IP)
    regstream_parser.add_argument("--fogPort", default = str(FOG_PORT))
    regstream_parser.add_argument("--minReplica", default = str(2))
    regstream_parser.add_argument("--maxReplica", default = str(5))
    regstream_parser.add_argument("--v","--verbose", action = "store_true")

    ## Parser for put command
    ## Arguments :
    ## 1. --path
    ## 2. --streamId
    ## 3. --start
    ## 4. --metadata (default as None)
    ## 5. --fogIp (default, based on config file)
    ## 6. --fogPort (default, based on config file)
    ## 7. --edgeId (default, based on config file)
    ## 8. --clientId (default, hashed based on the edge id)
    ## 9. --setLease (flag)
    ## 10. --duration (same as expected lease implemented in server; default value as 0)
    ## 11. --comp (has default, based on config file)
    put_parser = subparsers.add_parser("put")
    put_parser.add_argument("--path")
    put_parser.add_argument("--streamId")
    put_parser.add_argument("--start")
    put_parser.add_argument("--metadata", default = None)
    put_parser.add_argument("--fogIp", default = FOG_IP)
    put_parser.add_argument("--fogPort", default = str(FOG_PORT))
    put_parser.add_argument("--edgeId", default = str(EDGE_ID))
    put_parser.add_argument("--clientId", default = CLIENT_ID)
    put_parser.add_argument("--singleBlock", action ="store_true")
    put_parser.add_argument("--setLease", action ="store_true")
    put_parser.add_argument("--duration",default=str(0))
    put_parser.add_argument("--comp",default=COMP_FORMAT)
    put_parser.add_argument("--v","--verbose", action ="store_true")

    ## Parser for get command
    ## Arguments :
    ## 1. --mbIdS
    ## 2. --edgeId (default, based on config file)
    ## 3. --edgeIp (default, based on config file)
    ## 4. --edgePort (default, based on config file)
    ## 5. --edgeReli (default, from stream metadata)
    ## 6. --fogIp (default, based on config file)
    ## 7. --fogPort (default, based on config file)
    get_parser = subparsers.add_parser("get")
    get_parser.add_argument("--start")
    get_parser.add_argument("--end", default = str(-1))
    get_parser.add_argument("--edgeId", default = str(EDGE_ID))
    get_parser.add_argument("--edgeIp", default = EDGE_IP)
    get_parser.add_argument("--edgePort", default = str(EDGE_PORT))
    get_parser.add_argument("--edgeReli", default = str(EDGE_RELI))
    get_parser.add_argument("--fogIp", default = FOG_IP)
    get_parser.add_argument("--fogPort", default = str(FOG_PORT))
    get_parser.add_argument("--v","--verbose", action = "store_true")


    ## Parser for ls command
    ## Arguments :
    ## 1. --fogIp (default, based on config file)
    ## 2. --fogPort (default, based on config file)
    ## 3. --all (flag)
    ## 4. --neighbours (flag)
    ## 5. --buddies (flag)
    ## 6. --groupBy (default, as edge)
    ## The above argument is to list the mbids in the local
    ## partition only, specified by the fogIp and fogPort.
    ls_parser = subparsers.add_parser("ls")
    ls_parser.add_argument("--fogIp", default = FOG_IP)
    ls_parser.add_argument("--fogPort", default = FOG_PORT)
    ls_parser.add_argument("--all",action='store_true')
    ls_parser.add_argument("--buddies",action='store_true')
    ls_parser.add_argument("--neighbors",action='store_true')
    ls_parser.add_argument("--groupBy", default = "edge")
    ls_parser.add_argument("--v","--verbose", action = "store_true")

    ## Parser for find command
    ## Arguments :
    ## 1. --fogIp (default, based on config file)
    ## 2. --fogPort (default, based on config file)
    ## 3. --mbid    (specify any one from 3,4 or 5)
    ## 4. --blockMeta (specify any one from 3,4 or 5)
    ## 5. --streamMeta (specify any one from 3,4 or 5)
    find_parser = subparsers.add_parser("find")
    find_parser.add_argument("--fogIp", default = FOG_IP)
    find_parser.add_argument("--fogPort", default = FOG_PORT)
    find_parser.add_argument("--mbid", default = None)
    find_parser.add_argument("--blockMeta",default = None)
    find_parser.add_argument("--streamMeta",default = None)
    find_parser.add_argument("--v","--verbose", action = "store_true")

    ## Parser for join command
    ## Arguments :
    ## 1. --configFile
    join_parser = subparsers.add_parser("join")
    join_parser.add_argument("--configFiles")

    obj = elfsCLI()
    obj.cmdloop()
