#
# Autogenerated by Thrift Compiler (0.9.1)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TException, TApplicationException

from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol, TProtocol
try:
  from thrift.protocol import fastbinary
except:
  fastbinary = None



class ParentFog:
  """
  Attributes:
   - nodeId
   - nodeIp
   - port
  """

  thrift_spec = (
    None, # 0
    (1, TType.I16, 'nodeId', None, None, ), # 1
    (2, TType.STRING, 'nodeIp', None, None, ), # 2
    (3, TType.I32, 'port', None, None, ), # 3
  )

  def __init__(self, nodeId=None, nodeIp=None, port=None,):
    self.nodeId = nodeId
    self.nodeIp = nodeIp
    self.port = port

  def read(self, iprot):
    if iprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None and fastbinary is not None:
      fastbinary.decode_binary(self, iprot.trans, (self.__class__, self.thrift_spec))
      return
    iprot.readStructBegin()
    while True:
      (fname, ftype, fid) = iprot.readFieldBegin()
      if ftype == TType.STOP:
        break
      if fid == 1:
        if ftype == TType.I16:
          self.nodeId = iprot.readI16();
        else:
          iprot.skip(ftype)
      elif fid == 2:
        if ftype == TType.STRING:
          self.nodeIp = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 3:
        if ftype == TType.I32:
          self.port = iprot.readI32();
        else:
          iprot.skip(ftype)
      else:
        iprot.skip(ftype)
      iprot.readFieldEnd()
    iprot.readStructEnd()

  def write(self, oprot):
    if oprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and self.thrift_spec is not None and fastbinary is not None:
      oprot.trans.write(fastbinary.encode_binary(self, (self.__class__, self.thrift_spec)))
      return
    oprot.writeStructBegin('ParentFog')
    if self.nodeId is not None:
      oprot.writeFieldBegin('nodeId', TType.I16, 1)
      oprot.writeI16(self.nodeId)
      oprot.writeFieldEnd()
    if self.nodeIp is not None:
      oprot.writeFieldBegin('nodeIp', TType.STRING, 2)
      oprot.writeString(self.nodeIp)
      oprot.writeFieldEnd()
    if self.port is not None:
      oprot.writeFieldBegin('port', TType.I32, 3)
      oprot.writeI32(self.port)
      oprot.writeFieldEnd()
    oprot.writeFieldStop()
    oprot.writeStructEnd()

  def validate(self):
    if self.nodeId is None:
      raise TProtocol.TProtocolException(message='Required field nodeId is unset!')
    if self.nodeIp is None:
      raise TProtocol.TProtocolException(message='Required field nodeIp is unset!')
    if self.port is None:
      raise TProtocol.TProtocolException(message='Required field port is unset!')
    return


  def __repr__(self):
    L = ['%s=%r' % (key, value)
      for key, value in self.__dict__.iteritems()]
    return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

  def __eq__(self, other):
    return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

  def __ne__(self, other):
    return not (self == other)

class Metadata:
  """
  Attributes:
   - mbId
   - streamId
   - timestamp
  """

  thrift_spec = (
    None, # 0
    (1, TType.STRING, 'mbId', None, None, ), # 1
    (2, TType.STRING, 'streamId', None, None, ), # 2
    (3, TType.STRING, 'timestamp', None, None, ), # 3
  )

  def __init__(self, mbId=None, streamId=None, timestamp=None,):
    self.mbId = mbId
    self.streamId = streamId
    self.timestamp = timestamp

  def read(self, iprot):
    if iprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None and fastbinary is not None:
      fastbinary.decode_binary(self, iprot.trans, (self.__class__, self.thrift_spec))
      return
    iprot.readStructBegin()
    while True:
      (fname, ftype, fid) = iprot.readFieldBegin()
      if ftype == TType.STOP:
        break
      if fid == 1:
        if ftype == TType.STRING:
          self.mbId = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 2:
        if ftype == TType.STRING:
          self.streamId = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 3:
        if ftype == TType.STRING:
          self.timestamp = iprot.readString();
        else:
          iprot.skip(ftype)
      else:
        iprot.skip(ftype)
      iprot.readFieldEnd()
    iprot.readStructEnd()

  def write(self, oprot):
    if oprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and self.thrift_spec is not None and fastbinary is not None:
      oprot.trans.write(fastbinary.encode_binary(self, (self.__class__, self.thrift_spec)))
      return
    oprot.writeStructBegin('Metadata')
    if self.mbId is not None:
      oprot.writeFieldBegin('mbId', TType.STRING, 1)
      oprot.writeString(self.mbId)
      oprot.writeFieldEnd()
    if self.streamId is not None:
      oprot.writeFieldBegin('streamId', TType.STRING, 2)
      oprot.writeString(self.streamId)
      oprot.writeFieldEnd()
    if self.timestamp is not None:
      oprot.writeFieldBegin('timestamp', TType.STRING, 3)
      oprot.writeString(self.timestamp)
      oprot.writeFieldEnd()
    oprot.writeFieldStop()
    oprot.writeStructEnd()

  def validate(self):
    if self.mbId is None:
      raise TProtocol.TProtocolException(message='Required field mbId is unset!')
    if self.streamId is None:
      raise TProtocol.TProtocolException(message='Required field streamId is unset!')
    if self.timestamp is None:
      raise TProtocol.TProtocolException(message='Required field timestamp is unset!')
    return


  def __repr__(self):
    L = ['%s=%r' % (key, value)
      for key, value in self.__dict__.iteritems()]
    return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

  def __eq__(self, other):
    return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

  def __ne__(self, other):
    return not (self == other)