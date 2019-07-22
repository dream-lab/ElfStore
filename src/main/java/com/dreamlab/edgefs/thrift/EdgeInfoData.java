/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-07-22")
public class EdgeInfoData implements org.apache.thrift.TBase<EdgeInfoData, EdgeInfoData._Fields>, java.io.Serializable, Cloneable, Comparable<EdgeInfoData> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("EdgeInfoData");

  private static final org.apache.thrift.protocol.TField NODE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("nodeId", org.apache.thrift.protocol.TType.I16, (short)1);
  private static final org.apache.thrift.protocol.TField NODE_IP_FIELD_DESC = new org.apache.thrift.protocol.TField("nodeIp", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PORT_FIELD_DESC = new org.apache.thrift.protocol.TField("port", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField RELIABILITY_FIELD_DESC = new org.apache.thrift.protocol.TField("reliability", org.apache.thrift.protocol.TType.BYTE, (short)4);
  private static final org.apache.thrift.protocol.TField STORAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("storage", org.apache.thrift.protocol.TType.BYTE, (short)5);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new EdgeInfoDataStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new EdgeInfoDataTupleSchemeFactory();

  public short nodeId; // required
  public java.lang.String nodeIp; // required
  public int port; // required
  public byte reliability; // required
  public byte storage; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NODE_ID((short)1, "nodeId"),
    NODE_IP((short)2, "nodeIp"),
    PORT((short)3, "port"),
    RELIABILITY((short)4, "reliability"),
    STORAGE((short)5, "storage");

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
        case 1: // NODE_ID
          return NODE_ID;
        case 2: // NODE_IP
          return NODE_IP;
        case 3: // PORT
          return PORT;
        case 4: // RELIABILITY
          return RELIABILITY;
        case 5: // STORAGE
          return STORAGE;
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
  private static final int __NODEID_ISSET_ID = 0;
  private static final int __PORT_ISSET_ID = 1;
  private static final int __RELIABILITY_ISSET_ID = 2;
  private static final int __STORAGE_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NODE_ID, new org.apache.thrift.meta_data.FieldMetaData("nodeId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    tmpMap.put(_Fields.NODE_IP, new org.apache.thrift.meta_data.FieldMetaData("nodeIp", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PORT, new org.apache.thrift.meta_data.FieldMetaData("port", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.RELIABILITY, new org.apache.thrift.meta_data.FieldMetaData("reliability", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    tmpMap.put(_Fields.STORAGE, new org.apache.thrift.meta_data.FieldMetaData("storage", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(EdgeInfoData.class, metaDataMap);
  }

  public EdgeInfoData() {
  }

  public EdgeInfoData(
    short nodeId,
    java.lang.String nodeIp,
    int port,
    byte reliability,
    byte storage)
  {
    this();
    this.nodeId = nodeId;
    setNodeIdIsSet(true);
    this.nodeIp = nodeIp;
    this.port = port;
    setPortIsSet(true);
    this.reliability = reliability;
    setReliabilityIsSet(true);
    this.storage = storage;
    setStorageIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public EdgeInfoData(EdgeInfoData other) {
    __isset_bitfield = other.__isset_bitfield;
    this.nodeId = other.nodeId;
    if (other.isSetNodeIp()) {
      this.nodeIp = other.nodeIp;
    }
    this.port = other.port;
    this.reliability = other.reliability;
    this.storage = other.storage;
  }

  public EdgeInfoData deepCopy() {
    return new EdgeInfoData(this);
  }

  @Override
  public void clear() {
    setNodeIdIsSet(false);
    this.nodeId = 0;
    this.nodeIp = null;
    setPortIsSet(false);
    this.port = 0;
    setReliabilityIsSet(false);
    this.reliability = 0;
    setStorageIsSet(false);
    this.storage = 0;
  }

  public short getNodeId() {
    return this.nodeId;
  }

  public EdgeInfoData setNodeId(short nodeId) {
    this.nodeId = nodeId;
    setNodeIdIsSet(true);
    return this;
  }

  public void unsetNodeId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __NODEID_ISSET_ID);
  }

  /** Returns true if field nodeId is set (has been assigned a value) and false otherwise */
  public boolean isSetNodeId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __NODEID_ISSET_ID);
  }

  public void setNodeIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __NODEID_ISSET_ID, value);
  }

  public java.lang.String getNodeIp() {
    return this.nodeIp;
  }

  public EdgeInfoData setNodeIp(java.lang.String nodeIp) {
    this.nodeIp = nodeIp;
    return this;
  }

  public void unsetNodeIp() {
    this.nodeIp = null;
  }

  /** Returns true if field nodeIp is set (has been assigned a value) and false otherwise */
  public boolean isSetNodeIp() {
    return this.nodeIp != null;
  }

  public void setNodeIpIsSet(boolean value) {
    if (!value) {
      this.nodeIp = null;
    }
  }

  public int getPort() {
    return this.port;
  }

  public EdgeInfoData setPort(int port) {
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

  public byte getReliability() {
    return this.reliability;
  }

  public EdgeInfoData setReliability(byte reliability) {
    this.reliability = reliability;
    setReliabilityIsSet(true);
    return this;
  }

  public void unsetReliability() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __RELIABILITY_ISSET_ID);
  }

  /** Returns true if field reliability is set (has been assigned a value) and false otherwise */
  public boolean isSetReliability() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __RELIABILITY_ISSET_ID);
  }

  public void setReliabilityIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __RELIABILITY_ISSET_ID, value);
  }

  public byte getStorage() {
    return this.storage;
  }

  public EdgeInfoData setStorage(byte storage) {
    this.storage = storage;
    setStorageIsSet(true);
    return this;
  }

  public void unsetStorage() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __STORAGE_ISSET_ID);
  }

  /** Returns true if field storage is set (has been assigned a value) and false otherwise */
  public boolean isSetStorage() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __STORAGE_ISSET_ID);
  }

  public void setStorageIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __STORAGE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case NODE_ID:
      if (value == null) {
        unsetNodeId();
      } else {
        setNodeId((java.lang.Short)value);
      }
      break;

    case NODE_IP:
      if (value == null) {
        unsetNodeIp();
      } else {
        setNodeIp((java.lang.String)value);
      }
      break;

    case PORT:
      if (value == null) {
        unsetPort();
      } else {
        setPort((java.lang.Integer)value);
      }
      break;

    case RELIABILITY:
      if (value == null) {
        unsetReliability();
      } else {
        setReliability((java.lang.Byte)value);
      }
      break;

    case STORAGE:
      if (value == null) {
        unsetStorage();
      } else {
        setStorage((java.lang.Byte)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NODE_ID:
      return getNodeId();

    case NODE_IP:
      return getNodeIp();

    case PORT:
      return getPort();

    case RELIABILITY:
      return getReliability();

    case STORAGE:
      return getStorage();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NODE_ID:
      return isSetNodeId();
    case NODE_IP:
      return isSetNodeIp();
    case PORT:
      return isSetPort();
    case RELIABILITY:
      return isSetReliability();
    case STORAGE:
      return isSetStorage();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof EdgeInfoData)
      return this.equals((EdgeInfoData)that);
    return false;
  }

  public boolean equals(EdgeInfoData that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_nodeId = true;
    boolean that_present_nodeId = true;
    if (this_present_nodeId || that_present_nodeId) {
      if (!(this_present_nodeId && that_present_nodeId))
        return false;
      if (this.nodeId != that.nodeId)
        return false;
    }

    boolean this_present_nodeIp = true && this.isSetNodeIp();
    boolean that_present_nodeIp = true && that.isSetNodeIp();
    if (this_present_nodeIp || that_present_nodeIp) {
      if (!(this_present_nodeIp && that_present_nodeIp))
        return false;
      if (!this.nodeIp.equals(that.nodeIp))
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

    boolean this_present_reliability = true;
    boolean that_present_reliability = true;
    if (this_present_reliability || that_present_reliability) {
      if (!(this_present_reliability && that_present_reliability))
        return false;
      if (this.reliability != that.reliability)
        return false;
    }

    boolean this_present_storage = true;
    boolean that_present_storage = true;
    if (this_present_storage || that_present_storage) {
      if (!(this_present_storage && that_present_storage))
        return false;
      if (this.storage != that.storage)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + nodeId;

    hashCode = hashCode * 8191 + ((isSetNodeIp()) ? 131071 : 524287);
    if (isSetNodeIp())
      hashCode = hashCode * 8191 + nodeIp.hashCode();

    hashCode = hashCode * 8191 + port;

    hashCode = hashCode * 8191 + (int) (reliability);

    hashCode = hashCode * 8191 + (int) (storage);

    return hashCode;
  }

  @Override
  public int compareTo(EdgeInfoData other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetNodeId()).compareTo(other.isSetNodeId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNodeId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.nodeId, other.nodeId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetNodeIp()).compareTo(other.isSetNodeIp());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNodeIp()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.nodeIp, other.nodeIp);
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
    lastComparison = java.lang.Boolean.valueOf(isSetReliability()).compareTo(other.isSetReliability());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetReliability()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.reliability, other.reliability);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetStorage()).compareTo(other.isSetStorage());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStorage()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.storage, other.storage);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("EdgeInfoData(");
    boolean first = true;

    sb.append("nodeId:");
    sb.append(this.nodeId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("nodeIp:");
    if (this.nodeIp == null) {
      sb.append("null");
    } else {
      sb.append(this.nodeIp);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("port:");
    sb.append(this.port);
    first = false;
    if (!first) sb.append(", ");
    sb.append("reliability:");
    sb.append(this.reliability);
    first = false;
    if (!first) sb.append(", ");
    sb.append("storage:");
    sb.append(this.storage);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'nodeId' because it's a primitive and you chose the non-beans generator.
    if (nodeIp == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'nodeIp' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'port' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'reliability' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'storage' because it's a primitive and you chose the non-beans generator.
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

  private static class EdgeInfoDataStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public EdgeInfoDataStandardScheme getScheme() {
      return new EdgeInfoDataStandardScheme();
    }
  }

  private static class EdgeInfoDataStandardScheme extends org.apache.thrift.scheme.StandardScheme<EdgeInfoData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, EdgeInfoData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NODE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.nodeId = iprot.readI16();
              struct.setNodeIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // NODE_IP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.nodeIp = iprot.readString();
              struct.setNodeIpIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PORT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.port = iprot.readI32();
              struct.setPortIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // RELIABILITY
            if (schemeField.type == org.apache.thrift.protocol.TType.BYTE) {
              struct.reliability = iprot.readByte();
              struct.setReliabilityIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // STORAGE
            if (schemeField.type == org.apache.thrift.protocol.TType.BYTE) {
              struct.storage = iprot.readByte();
              struct.setStorageIsSet(true);
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
      if (!struct.isSetNodeId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'nodeId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetPort()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'port' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetReliability()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'reliability' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetStorage()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'storage' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, EdgeInfoData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(NODE_ID_FIELD_DESC);
      oprot.writeI16(struct.nodeId);
      oprot.writeFieldEnd();
      if (struct.nodeIp != null) {
        oprot.writeFieldBegin(NODE_IP_FIELD_DESC);
        oprot.writeString(struct.nodeIp);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(PORT_FIELD_DESC);
      oprot.writeI32(struct.port);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(RELIABILITY_FIELD_DESC);
      oprot.writeByte(struct.reliability);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(STORAGE_FIELD_DESC);
      oprot.writeByte(struct.storage);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class EdgeInfoDataTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public EdgeInfoDataTupleScheme getScheme() {
      return new EdgeInfoDataTupleScheme();
    }
  }

  private static class EdgeInfoDataTupleScheme extends org.apache.thrift.scheme.TupleScheme<EdgeInfoData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, EdgeInfoData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI16(struct.nodeId);
      oprot.writeString(struct.nodeIp);
      oprot.writeI32(struct.port);
      oprot.writeByte(struct.reliability);
      oprot.writeByte(struct.storage);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, EdgeInfoData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.nodeId = iprot.readI16();
      struct.setNodeIdIsSet(true);
      struct.nodeIp = iprot.readString();
      struct.setNodeIpIsSet(true);
      struct.port = iprot.readI32();
      struct.setPortIsSet(true);
      struct.reliability = iprot.readByte();
      struct.setReliabilityIsSet(true);
      struct.storage = iprot.readByte();
      struct.setStorageIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

