#
# Autogenerated by Thrift Compiler (0.11.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TFrozenDict, TException, TApplicationException
from thrift.protocol.TProtocol import TProtocolException
from thrift.TRecursive import fix_spec

import sys

from thrift.transport import TTransport
all_structs = []


class ParentFog(object):
    """
    Attributes:
     - nodeId
     - nodeIp
     - port
    """


    def __init__(self, nodeId=None, nodeIp=None, port=None,):
        self.nodeId = nodeId
        self.nodeIp = nodeIp
        self.port = port

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.I16:
                    self.nodeId = iprot.readI16()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.nodeIp = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.I32:
                    self.port = iprot.readI32()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('ParentFog')
        if self.nodeId is not None:
            oprot.writeFieldBegin('nodeId', TType.I16, 1)
            oprot.writeI16(self.nodeId)
            oprot.writeFieldEnd()
        if self.nodeIp is not None:
            oprot.writeFieldBegin('nodeIp', TType.STRING, 2)
            oprot.writeString(self.nodeIp.encode('utf-8') if sys.version_info[0] == 2 else self.nodeIp)
            oprot.writeFieldEnd()
        if self.port is not None:
            oprot.writeFieldBegin('port', TType.I32, 3)
            oprot.writeI32(self.port)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        if self.nodeId is None:
            raise TProtocolException(message='Required field nodeId is unset!')
        if self.nodeIp is None:
            raise TProtocolException(message='Required field nodeIp is unset!')
        if self.port is None:
            raise TProtocolException(message='Required field port is unset!')
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class Metadata(object):
    """
    Attributes:
     - clientId
     - sessionSecret
     - streamId
     - mbId
     - timestamp
     - checksum
     - properties
    """


    def __init__(self, clientId=None, sessionSecret=None, streamId=None, mbId=None, timestamp=None, checksum=None, properties=None,):
        self.clientId = clientId
        self.sessionSecret = sessionSecret
        self.streamId = streamId
        self.mbId = mbId
        self.timestamp = timestamp
        self.checksum = checksum
        self.properties = properties

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRING:
                    self.clientId = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.sessionSecret = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.STRING:
                    self.streamId = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 4:
                if ftype == TType.I64:
                    self.mbId = iprot.readI64()
                else:
                    iprot.skip(ftype)
            elif fid == 5:
                if ftype == TType.I64:
                    self.timestamp = iprot.readI64()
                else:
                    iprot.skip(ftype)
            elif fid == 6:
                if ftype == TType.STRING:
                    self.checksum = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 7:
                if ftype == TType.STRING:
                    self.properties = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('Metadata')
        if self.clientId is not None:
            oprot.writeFieldBegin('clientId', TType.STRING, 1)
            oprot.writeString(self.clientId.encode('utf-8') if sys.version_info[0] == 2 else self.clientId)
            oprot.writeFieldEnd()
        if self.sessionSecret is not None:
            oprot.writeFieldBegin('sessionSecret', TType.STRING, 2)
            oprot.writeString(self.sessionSecret.encode('utf-8') if sys.version_info[0] == 2 else self.sessionSecret)
            oprot.writeFieldEnd()
        if self.streamId is not None:
            oprot.writeFieldBegin('streamId', TType.STRING, 3)
            oprot.writeString(self.streamId.encode('utf-8') if sys.version_info[0] == 2 else self.streamId)
            oprot.writeFieldEnd()
        if self.mbId is not None:
            oprot.writeFieldBegin('mbId', TType.I64, 4)
            oprot.writeI64(self.mbId)
            oprot.writeFieldEnd()
        if self.timestamp is not None:
            oprot.writeFieldBegin('timestamp', TType.I64, 5)
            oprot.writeI64(self.timestamp)
            oprot.writeFieldEnd()
        if self.checksum is not None:
            oprot.writeFieldBegin('checksum', TType.STRING, 6)
            oprot.writeString(self.checksum.encode('utf-8') if sys.version_info[0] == 2 else self.checksum)
            oprot.writeFieldEnd()
        if self.properties is not None:
            oprot.writeFieldBegin('properties', TType.STRING, 7)
            oprot.writeString(self.properties.encode('utf-8') if sys.version_info[0] == 2 else self.properties)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        if self.clientId is None:
            raise TProtocolException(message='Required field clientId is unset!')
        if self.sessionSecret is None:
            raise TProtocolException(message='Required field sessionSecret is unset!')
        if self.streamId is None:
            raise TProtocolException(message='Required field streamId is unset!')
        if self.mbId is None:
            raise TProtocolException(message='Required field mbId is unset!')
        if self.timestamp is None:
            raise TProtocolException(message='Required field timestamp is unset!')
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class EdgeInfoData(object):
    """
    Attributes:
     - nodeId
     - nodeIp
     - port
     - reliability
     - storage
    """


    def __init__(self, nodeId=None, nodeIp=None, port=None, reliability=None, storage=None,):
        self.nodeId = nodeId
        self.nodeIp = nodeIp
        self.port = port
        self.reliability = reliability
        self.storage = storage

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.I16:
                    self.nodeId = iprot.readI16()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.nodeIp = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.I32:
                    self.port = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 4:
                if ftype == TType.BYTE:
                    self.reliability = iprot.readByte()
                else:
                    iprot.skip(ftype)
            elif fid == 5:
                if ftype == TType.BYTE:
                    self.storage = iprot.readByte()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('EdgeInfoData')
        if self.nodeId is not None:
            oprot.writeFieldBegin('nodeId', TType.I16, 1)
            oprot.writeI16(self.nodeId)
            oprot.writeFieldEnd()
        if self.nodeIp is not None:
            oprot.writeFieldBegin('nodeIp', TType.STRING, 2)
            oprot.writeString(self.nodeIp.encode('utf-8') if sys.version_info[0] == 2 else self.nodeIp)
            oprot.writeFieldEnd()
        if self.port is not None:
            oprot.writeFieldBegin('port', TType.I32, 3)
            oprot.writeI32(self.port)
            oprot.writeFieldEnd()
        if self.reliability is not None:
            oprot.writeFieldBegin('reliability', TType.BYTE, 4)
            oprot.writeByte(self.reliability)
            oprot.writeFieldEnd()
        if self.storage is not None:
            oprot.writeFieldBegin('storage', TType.BYTE, 5)
            oprot.writeByte(self.storage)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        if self.nodeId is None:
            raise TProtocolException(message='Required field nodeId is unset!')
        if self.nodeIp is None:
            raise TProtocolException(message='Required field nodeIp is unset!')
        if self.port is None:
            raise TProtocolException(message='Required field port is unset!')
        if self.reliability is None:
            raise TProtocolException(message='Required field reliability is unset!')
        if self.storage is None:
            raise TProtocolException(message='Required field storage is unset!')
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class ReadResponse(object):
    """
    Attributes:
     - status
     - data
     - edgeInfo
     - metadata
    """


    def __init__(self, status=None, data=None, edgeInfo=None, metadata=None,):
        self.status = status
        self.data = data
        self.edgeInfo = edgeInfo
        self.metadata = metadata

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.BYTE:
                    self.status = iprot.readByte()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.data = iprot.readBinary()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.STRUCT:
                    self.edgeInfo = EdgeInfoData()
                    self.edgeInfo.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 4:
                if ftype == TType.STRUCT:
                    self.metadata = Metadata()
                    self.metadata.read(iprot)
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('ReadResponse')
        if self.status is not None:
            oprot.writeFieldBegin('status', TType.BYTE, 1)
            oprot.writeByte(self.status)
            oprot.writeFieldEnd()
        if self.data is not None:
            oprot.writeFieldBegin('data', TType.STRING, 2)
            oprot.writeBinary(self.data)
            oprot.writeFieldEnd()
        if self.edgeInfo is not None:
            oprot.writeFieldBegin('edgeInfo', TType.STRUCT, 3)
            self.edgeInfo.write(oprot)
            oprot.writeFieldEnd()
        if self.metadata is not None:
            oprot.writeFieldBegin('metadata', TType.STRUCT, 4)
            self.metadata.write(oprot)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        if self.status is None:
            raise TProtocolException(message='Required field status is unset!')
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class ReadReplica(object):
    """
    Attributes:
     - status
     - data
     - metadata
    """


    def __init__(self, status=None, data=None, metadata=None,):
        self.status = status
        self.data = data
        self.metadata = metadata

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.BYTE:
                    self.status = iprot.readByte()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.data = iprot.readBinary()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.STRUCT:
                    self.metadata = Metadata()
                    self.metadata.read(iprot)
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('ReadReplica')
        if self.status is not None:
            oprot.writeFieldBegin('status', TType.BYTE, 1)
            oprot.writeByte(self.status)
            oprot.writeFieldEnd()
        if self.data is not None:
            oprot.writeFieldBegin('data', TType.STRING, 2)
            oprot.writeBinary(self.data)
            oprot.writeFieldEnd()
        if self.metadata is not None:
            oprot.writeFieldBegin('metadata', TType.STRUCT, 3)
            self.metadata.write(oprot)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        if self.status is None:
            raise TProtocolException(message='Required field status is unset!')
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class WriteResponse(object):
    """
    Attributes:
     - status
     - reliability
    """


    def __init__(self, status=None, reliability=None,):
        self.status = status
        self.reliability = reliability

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.BYTE:
                    self.status = iprot.readByte()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.BYTE:
                    self.reliability = iprot.readByte()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('WriteResponse')
        if self.status is not None:
            oprot.writeFieldBegin('status', TType.BYTE, 1)
            oprot.writeByte(self.status)
            oprot.writeFieldEnd()
        if self.reliability is not None:
            oprot.writeFieldBegin('reliability', TType.BYTE, 2)
            oprot.writeByte(self.reliability)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        if self.status is None:
            raise TProtocolException(message='Required field status is unset!')
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(ParentFog)
ParentFog.thrift_spec = (
    None,  # 0
    (1, TType.I16, 'nodeId', None, None, ),  # 1
    (2, TType.STRING, 'nodeIp', 'UTF8', None, ),  # 2
    (3, TType.I32, 'port', None, None, ),  # 3
)
all_structs.append(Metadata)
Metadata.thrift_spec = (
    None,  # 0
    (1, TType.STRING, 'clientId', 'UTF8', None, ),  # 1
    (2, TType.STRING, 'sessionSecret', 'UTF8', None, ),  # 2
    (3, TType.STRING, 'streamId', 'UTF8', None, ),  # 3
    (4, TType.I64, 'mbId', None, None, ),  # 4
    (5, TType.I64, 'timestamp', None, None, ),  # 5
    (6, TType.STRING, 'checksum', 'UTF8', None, ),  # 6
    (7, TType.STRING, 'properties', 'UTF8', None, ),  # 7
)
all_structs.append(EdgeInfoData)
EdgeInfoData.thrift_spec = (
    None,  # 0
    (1, TType.I16, 'nodeId', None, None, ),  # 1
    (2, TType.STRING, 'nodeIp', 'UTF8', None, ),  # 2
    (3, TType.I32, 'port', None, None, ),  # 3
    (4, TType.BYTE, 'reliability', None, None, ),  # 4
    (5, TType.BYTE, 'storage', None, None, ),  # 5
)
all_structs.append(ReadResponse)
ReadResponse.thrift_spec = (
    None,  # 0
    (1, TType.BYTE, 'status', None, None, ),  # 1
    (2, TType.STRING, 'data', 'BINARY', None, ),  # 2
    (3, TType.STRUCT, 'edgeInfo', [EdgeInfoData, None], None, ),  # 3
    (4, TType.STRUCT, 'metadata', [Metadata, None], None, ),  # 4
)
all_structs.append(ReadReplica)
ReadReplica.thrift_spec = (
    None,  # 0
    (1, TType.BYTE, 'status', None, None, ),  # 1
    (2, TType.STRING, 'data', 'BINARY', None, ),  # 2
    (3, TType.STRUCT, 'metadata', [Metadata, None], None, ),  # 3
)
all_structs.append(WriteResponse)
WriteResponse.thrift_spec = (
    None,  # 0
    (1, TType.BYTE, 'status', None, None, ),  # 1
    (2, TType.BYTE, 'reliability', None, None, ),  # 2
)
fix_spec(all_structs)
del all_structs
