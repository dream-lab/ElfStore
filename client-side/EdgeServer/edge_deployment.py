import json
import os
import numpy
import paramiko

deployment_output = json.load(open("sample.json"))

copy_done_vm = {}
key_path = "/home/centos/aep_rsa"
copy_file = "cluster.conf"


fog_to_privateip = {}
privateip_to_fog = {}
edgeip_to_vm = {}


for key in deployment_output :

        if "Fog" in key:
                vm_ip = deployment_output[key]["host_vm_ip"]
                fog_to_privateip[key] = deployment_output[key]["private_networks"]


        elif "Edge" in key:
                edge_to_privateip = deployment_output[key]["private_networks"]
                vm_ip = deployment_output[key]["host_vm_ip"]

                for key2 in edge_to_privateip:
                        edgeip_to_vm[key]=(vm_ip,key2,edge_to_privateip[key2])


#to get fogid to privteip and private network
for key in fog_to_privateip:
        violet_private = fog_to_privateip


        for key2 in violet_private:

                for key3 in violet_private[key2]:
                        privateip_to_fog[key3] = (key2,violet_private[key2][key3])


print "here",privateip_to_fog
print edgeip_to_vm

# python EdgeServer.py 1 127.0.0.1 8000 8 127.0.0.1 9090 /home/swamiji/

reliability = numpy.random.normal(90,3,len(edgeip_to_vm.keys()))
edgeDocker_name = []
edgeId_list = []
edgeIP_list = []
edgePort_list = []
edge_vm = []
fogIp_list = []
fogPort_list = []
DATA_PATH = "/edgefs/"
BASE_LOG = "/logs/"

i = 0
for key in edgeip_to_vm:
	# print "edge vm is ",edgeip_to_vm[key][2]
	edgeIP_list.append(edgeip_to_vm[key][2])
	edgeDocker_name.append(key)
	edgePort_list.append(5000) #hard coded port
	pvt_ip = edgeip_to_vm[key][1]
	fogIp_list.append( privateip_to_fog[pvt_ip][1] )
	fogPort_list.append(6000)
	vm_ip = edgeip_to_vm[key][0]
	edge_vm.append(edgeip_to_vm[key][0])
	edgeId_list.append(i+1)


	# print "python EdgeServer.py ",edgeId_list[i],edgeIP_list[i], edgePort_list[i],int(reliability[i]),fogIp_list[i],fogPort_list[i],DATA_PATH+str(i+1)
	# k = paramiko.RSAKey.from_private_key_file(key_path)
 #    c = paramiko.SSHClient()
 #    c.set_missing_host_key_policy(paramiko.AutoAddPolicy())
 #    c.connect( hostname = vm_ip, username = "centos", pkey = k)

        start_edge_server = "sudo docker exec -i "+ key+ " python /edgefs/EdgeServer/EdgeServer.py "+str(edgeId_list[i])+" "+str(edgeIP_list[i])+" "+ str(edgePort_list[i])+" "+str(int(reliability[i]))+" "+str(fogIp_list[i])+" "+str(fogPort_list[i])+" "+DATA_PATH+str(i+1)+" "+BASE_LOG+str(i+1)
        print str(start_edge_server)

    # c.exec_command('nohup ' + start_server + ' >/dev/null 2>&1 &')
    # c.close()

	i = i + 1
	

print edgeIP_list, edgePort_list, fogIp_list, fogPort_list,edgeId_list
print "edge devices deployed succesfully"

