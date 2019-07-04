/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-07-04")
public class NodeInfoPrimary implements org.apache.thrift.TBase<NodeInfoPrimary, NodeInfoPrimary._Fields>, java.io.Serializable, Cloneable, Comparable<NodeInfoPrimary> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("NodeInfoPrimary");

  private static final org.apache.thrift.protocol.TField NODE_IP_FIELD_DESC = new org.apache.thrift.protocol.TField("NodeIP", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField PORT_FIELD_DESC = new org.apache.thrift.protocol.TField("port", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new NodeInfoPrimaryStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new NodeInfoPrimaryTupleSchemeFactory();

  public java.lang.String NodeIP; // required
  public int port; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NODE_IP((short)1, "NodeIP"),
    PORT((short)2, "port");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // NODE_IP
          return NODE_IP;
        case 2: // PORT
          return PORT;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __PORT_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NODE_IP, new org.apache.thrift.meta_data.FieldMetaData("NodeIP", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PORT, new org.apache.thrift.meta_data.FieldMetaData("port", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(NodeInfoPrimary.class, metaDataMap);
  }

  public NodeInfoPrimary() {
  }

  public NodeInfoPrimary(
    java.lang.String NodeIP,
    int port)
  {
    this();
    this.NodeIP = NodeIP;
    this.port = port;
    setPortIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public NodeInfoPrimary(NodeInfoPrimary other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetNodeIP()) {
      this.NodeIP = other.NodeIP;
    }
    this.port = other.port;
  }

  public NodeInfoPrimary deepCopy() {
    return new NodeInfoPrimary(this);
  }

  @Override
  public void clear() {
    this.NodeIP = null;
    setPortIsSet(false);
    this.port = 0;
  }

  public java.lang.String getNodeIP() {
    return this.NodeIP;
  }

  public NodeInfoPrimary setNodeIP(java.lang.String NodeIP) {
    this.NodeIP = NodeIP;
    return this;
  }

  public void unsetNodeIP() {
    this.NodeIP = null;
  }

  /** Returns true if field NodeIP is set (has been assigned a value) and false otherwise */
  public boolean isSetNodeIP() {
    return this.NodeIP != null;
  }

  public void setNodeIPIsSet(boolean value) {
    if (!value) {
      this.NodeIP = null;
    }
  }

  public int getPort() {
    return this.port;
  }

  public NodeInfoPrimary setPort(int port) {
    this.port = port;
    setPortIsSet(true);
    return this;
  }

  public void unsetPort() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __PORT_ISSET_ID);
  }

  /** Returns true if field port is set (has been assigned a value) and false otherwise */
  public boolean isSetPort() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __PORT_ISSET_ID);
  }

  public void setPortIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __PORT_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case NODE_IP:
      if (value == null) {
        unsetNodeIP();
      } else {
        setNodeIP((java.lang.String)value);
      }
      break;

    case PORT:
      if (value == null) {
        unsetPort();
      } else {
        setPort((java.lang.Integer)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NODE_IP:
      return getNodeIP();

    case PORT:
      return getPort();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NODE_IP:
      return isSetNodeIP();
    case PORT:
      return isSetPort();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof NodeInfoPrimary)
      return this.equals((NodeInfoPrimary)that);
    return false;
  }

  public boolean equals(NodeInfoPrimary that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_NodeIP = true && this.isSetNodeIP();
    boolean that_present_NodeIP = true && that.isSetNodeIP();
    if (this_present_NodeIP || that_present_NodeIP) {
      if (!(this_present_NodeIP && that_present_NodeIP))
        return false;
      if (!this.NodeIP.equals(that.NodeIP))
        return false;
    }

    boolean this_present_port = true;
    boolean that_present_port = true;
    if (this_present_port || that_present_port) {
      if (!(this_present_port && that_present_port))
        return false;
      if (this.port != that.port)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetNodeIP()) ? 131071 : 524287);
    if (isSetNodeIP())
      hashCode = hashCode * 8191 + NodeIP.hashCode();

    hashCode = hashCode * 8191 + port;

    return hashCode;
  }

  @Override
  public int compareTo(NodeInfoPrimary other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetNodeIP()).compareTo(other.isSetNodeIP());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNodeIP()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.NodeIP, other.NodeIP);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetPort()).compareTo(other.isSetPort());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPort()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.port, other.port);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("NodeInfoPrimary(");
    boolean first = true;

    sb.append("NodeIP:");
    if (this.NodeIP == null) {
      sb.append("null");
    } else {
      sb.append(this.NodeIP);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("port:");
    sb.append(this.port);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (NodeIP == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'NodeIP' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'port' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class NodeInfoPrimaryStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public NodeInfoPrimaryStandardScheme getScheme() {
      return new NodeInfoPrimaryStandardScheme();
    }
  }

  private static class NodeInfoPrimaryStandardScheme extends org.apache.thrift.scheme.StandardScheme<NodeInfoPrimary> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, NodeInfoPrimary struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NODE_IP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.NodeIP = iprot.readString();
              struct.setNodeIPIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PORT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.port = iprot.readI32();
              struct.setPortIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      if (!struct.isSetPort()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'port' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, NodeInfoPrimary struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.NodeIP != null) {
        oprot.writeFieldBegin(NODE_IP_FIELD_DESC);
        oprot.writeString(struct.NodeIP);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(PORT_FIELD_DESC);
      oprot.writeI32(struct.port);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class NodeInfoPrimaryTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public NodeInfoPrimaryTupleScheme getScheme() {
      return new NodeInfoPrimaryTupleScheme();
    }
  }

  private static class NodeInfoPrimaryTupleScheme extends org.apache.thrift.scheme.TupleScheme<NodeInfoPrimary> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, NodeInfoPrimary struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.NodeIP);
      oprot.writeI32(struct.port);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, NodeInfoPrimary struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.NodeIP = iprot.readString();
      struct.setNodeIPIsSet(true);
      struct.port = iprot.readI32();
      struct.setPortIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

