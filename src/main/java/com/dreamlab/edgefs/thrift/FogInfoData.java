/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-07-22")
public class FogInfoData implements org.apache.thrift.TBase<FogInfoData, FogInfoData._Fields>, java.io.Serializable, Cloneable, Comparable<FogInfoData> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("FogInfoData");

  private static final org.apache.thrift.protocol.TField NODE_INSTANCE_FIELD_DESC = new org.apache.thrift.protocol.TField("nodeInstance", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField RELIABILITY_FIELD_DESC = new org.apache.thrift.protocol.TField("reliability", org.apache.thrift.protocol.TType.DOUBLE, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new FogInfoDataStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new FogInfoDataTupleSchemeFactory();

  public NodeInfoData nodeInstance; // required
  public double reliability; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NODE_INSTANCE((short)1, "nodeInstance"),
    RELIABILITY((short)2, "reliability");

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
        case 1: // NODE_INSTANCE
          return NODE_INSTANCE;
        case 2: // RELIABILITY
          return RELIABILITY;
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
  private static final int __RELIABILITY_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NODE_INSTANCE, new org.apache.thrift.meta_data.FieldMetaData("nodeInstance", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, NodeInfoData.class)));
    tmpMap.put(_Fields.RELIABILITY, new org.apache.thrift.meta_data.FieldMetaData("reliability", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(FogInfoData.class, metaDataMap);
  }

  public FogInfoData() {
  }

  public FogInfoData(
    NodeInfoData nodeInstance,
    double reliability)
  {
    this();
    this.nodeInstance = nodeInstance;
    this.reliability = reliability;
    setReliabilityIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public FogInfoData(FogInfoData other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetNodeInstance()) {
      this.nodeInstance = new NodeInfoData(other.nodeInstance);
    }
    this.reliability = other.reliability;
  }

  public FogInfoData deepCopy() {
    return new FogInfoData(this);
  }

  @Override
  public void clear() {
    this.nodeInstance = null;
    setReliabilityIsSet(false);
    this.reliability = 0.0;
  }

  public NodeInfoData getNodeInstance() {
    return this.nodeInstance;
  }

  public FogInfoData setNodeInstance(NodeInfoData nodeInstance) {
    this.nodeInstance = nodeInstance;
    return this;
  }

  public void unsetNodeInstance() {
    this.nodeInstance = null;
  }

  /** Returns true if field nodeInstance is set (has been assigned a value) and false otherwise */
  public boolean isSetNodeInstance() {
    return this.nodeInstance != null;
  }

  public void setNodeInstanceIsSet(boolean value) {
    if (!value) {
      this.nodeInstance = null;
    }
  }

  public double getReliability() {
    return this.reliability;
  }

  public FogInfoData setReliability(double reliability) {
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

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case NODE_INSTANCE:
      if (value == null) {
        unsetNodeInstance();
      } else {
        setNodeInstance((NodeInfoData)value);
      }
      break;

    case RELIABILITY:
      if (value == null) {
        unsetReliability();
      } else {
        setReliability((java.lang.Double)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NODE_INSTANCE:
      return getNodeInstance();

    case RELIABILITY:
      return getReliability();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NODE_INSTANCE:
      return isSetNodeInstance();
    case RELIABILITY:
      return isSetReliability();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof FogInfoData)
      return this.equals((FogInfoData)that);
    return false;
  }

  public boolean equals(FogInfoData that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_nodeInstance = true && this.isSetNodeInstance();
    boolean that_present_nodeInstance = true && that.isSetNodeInstance();
    if (this_present_nodeInstance || that_present_nodeInstance) {
      if (!(this_present_nodeInstance && that_present_nodeInstance))
        return false;
      if (!this.nodeInstance.equals(that.nodeInstance))
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

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetNodeInstance()) ? 131071 : 524287);
    if (isSetNodeInstance())
      hashCode = hashCode * 8191 + nodeInstance.hashCode();

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(reliability);

    return hashCode;
  }

  @Override
  public int compareTo(FogInfoData other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetNodeInstance()).compareTo(other.isSetNodeInstance());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNodeInstance()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.nodeInstance, other.nodeInstance);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("FogInfoData(");
    boolean first = true;

    sb.append("nodeInstance:");
    if (this.nodeInstance == null) {
      sb.append("null");
    } else {
      sb.append(this.nodeInstance);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("reliability:");
    sb.append(this.reliability);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (nodeInstance == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'nodeInstance' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'reliability' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
    if (nodeInstance != null) {
      nodeInstance.validate();
    }
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

  private static class FogInfoDataStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public FogInfoDataStandardScheme getScheme() {
      return new FogInfoDataStandardScheme();
    }
  }

  private static class FogInfoDataStandardScheme extends org.apache.thrift.scheme.StandardScheme<FogInfoData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, FogInfoData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NODE_INSTANCE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.nodeInstance = new NodeInfoData();
              struct.nodeInstance.read(iprot);
              struct.setNodeInstanceIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // RELIABILITY
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.reliability = iprot.readDouble();
              struct.setReliabilityIsSet(true);
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
      if (!struct.isSetReliability()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'reliability' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, FogInfoData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.nodeInstance != null) {
        oprot.writeFieldBegin(NODE_INSTANCE_FIELD_DESC);
        struct.nodeInstance.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(RELIABILITY_FIELD_DESC);
      oprot.writeDouble(struct.reliability);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class FogInfoDataTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public FogInfoDataTupleScheme getScheme() {
      return new FogInfoDataTupleScheme();
    }
  }

  private static class FogInfoDataTupleScheme extends org.apache.thrift.scheme.TupleScheme<FogInfoData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, FogInfoData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.nodeInstance.write(oprot);
      oprot.writeDouble(struct.reliability);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, FogInfoData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.nodeInstance = new NodeInfoData();
      struct.nodeInstance.read(iprot);
      struct.setNodeInstanceIsSet(true);
      struct.reliability = iprot.readDouble();
      struct.setReliabilityIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

