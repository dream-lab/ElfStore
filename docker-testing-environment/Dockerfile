FROM centos:7
ENV container docker

RUN mkdir /edgefs
COPY preload/ /edgefs/
COPY preload/cli/cluster.conf /
RUN mkdir /edgefs/data
RUN mkdir /edgefs/logs

RUN yum repolist
RUN yum -y update

RUN yum install -y iproute
RUN yum install -y traceroute
RUN yum install -y iptables-services
RUN yum install -y iperf3
RUN yum install -y nmap
RUN yum install -y net-tools

RUN yum install -y java-1.8.0-openjdk
RUN yum install -y java-1.8.0-openjdk-devel
RUN yum install -y openssh-clients
RUN yum install -y epel-release
RUN yum install -y fping
RUN yum install -y htop
RUN yum install -y vim
RUN yum install -y wget

RUN yum install -y https://centos7.iuscommunity.org/ius-release.rpm
RUN yum install -y python36u python36u-libs python36u-devel python36u-pip
RUN yum install -y which gcc
RUN yum install -y openldap-devel

RUN pip3.6 install --upgrade pip
RUN pip3.6 install psutil==5.6.3
RUN pip3.6 install six==1.12.0
RUN pip3.6 install termcolor==1.1.0
RUN pip3.6 install thrift==0.11.0
