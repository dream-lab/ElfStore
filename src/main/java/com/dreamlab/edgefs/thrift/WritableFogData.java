/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-07-22")
public class WritableFogData implements org.apache.thrift.TBase<WritableFogData, WritableFogData._Fields>, java.io.Serializable, Cloneable, Comparable<WritableFogData> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("WritableFogData");

  private static final org.apache.thrift.protocol.TField NODE_FIELD_DESC = new org.apache.thrift.protocol.TField("node", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField PREFERENCE_FIELD_DESC = new org.apache.thrift.protocol.TField("preference", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField RELIABILITY_FIELD_DESC = new org.apache.thrift.protocol.TField("reliability", org.apache.thrift.protocol.TType.DOUBLE, (short)3);
  private static final org.apache.thrift.protocol.TField EDGE_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("edgeInfo", org.apache.thrift.protocol.TType.STRUCT, (short)4);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new WritableFogDataStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new WritableFogDataTupleSchemeFactory();

  public NodeInfoData node; // required
  /**
   * 
   * @see WritePreference
   */
  public WritePreference preference; // required
  public double reliability; // required
  public EdgeInfoData edgeInfo; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NODE((short)1, "node"),
    /**
     * 
     * @see WritePreference
     */
    PREFERENCE((short)2, "preference"),
    RELIABILITY((short)3, "reliability"),
    EDGE_INFO((short)4, "edgeInfo");

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
        case 1: // NODE
          return NODE;
        case 2: // PREFERENCE
          return PREFERENCE;
        case 3: // RELIABILITY
          return RELIABILITY;
        case 4: // EDGE_INFO
          return EDGE_INFO;
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
  private static final _Fields optionals[] = {_Fields.EDGE_INFO};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NODE, new org.apache.thrift.meta_data.FieldMetaData("node", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, NodeInfoData.class)));
    tmpMap.put(_Fields.PREFERENCE, new org.apache.thrift.meta_data.FieldMetaData("preference", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, WritePreference.class)));
    tmpMap.put(_Fields.RELIABILITY, new org.apache.thrift.meta_data.FieldMetaData("reliability", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    tmpMap.put(_Fields.EDGE_INFO, new org.apache.thrift.meta_data.FieldMetaData("edgeInfo", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, EdgeInfoData.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(WritableFogData.class, metaDataMap);
  }

  public WritableFogData() {
  }

  public WritableFogData(
    NodeInfoData node,
    WritePreference preference,
    double reliability)
  {
    this();
    this.node = node;
    this.preference = preference;
    this.reliability = reliability;
    setReliabilityIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public WritableFogData(WritableFogData other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetNode()) {
      this.node = new NodeInfoData(other.node);
    }
    if (other.isSetPreference()) {
      this.preference = other.preference;
    }
    this.reliability = other.reliability;
    if (other.isSetEdgeInfo()) {
      this.edgeInfo = new EdgeInfoData(other.edgeInfo);
    }
  }

  public WritableFogData deepCopy() {
    return new WritableFogData(this);
  }

  @Override
  public void clear() {
    this.node = null;
    this.preference = null;
    setReliabilityIsSet(false);
    this.reliability = 0.0;
    this.edgeInfo = null;
  }

  public NodeInfoData getNode() {
    return this.node;
  }

  public WritableFogData setNode(NodeInfoData node) {
    this.node = node;
    return this;
  }

  public void unsetNode() {
    this.node = null;
  }

  /** Returns true if field node is set (has been assigned a value) and false otherwise */
  public boolean isSetNode() {
    return this.node != null;
  }

  public void setNodeIsSet(boolean value) {
    if (!value) {
      this.node = null;
    }
  }

  /**
   * 
   * @see WritePreference
   */
  public WritePreference getPreference() {
    return this.preference;
  }

  /**
   * 
   * @see WritePreference
   */
  public WritableFogData setPreference(WritePreference preference) {
    this.preference = preference;
    return this;
  }

  public void unsetPreference() {
    this.preference = null;
  }

  /** Returns true if field preference is set (has been assigned a value) and false otherwise */
  public boolean isSetPreference() {
    return this.preference != null;
  }

  public void setPreferenceIsSet(boolean value) {
    if (!value) {
      this.preference = null;
    }
  }

  public double getReliability() {
    return this.reliability;
  }

  public WritableFogData setReliability(double reliability) {
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

  public EdgeInfoData getEdgeInfo() {
    return this.edgeInfo;
  }

  public WritableFogData setEdgeInfo(EdgeInfoData edgeInfo) {
    this.edgeInfo = edgeInfo;
    return this;
  }

  public void unsetEdgeInfo() {
    this.edgeInfo = null;
  }

  /** Returns true if field edgeInfo is set (has been assigned a value) and false otherwise */
  public boolean isSetEdgeInfo() {
    return this.edgeInfo != null;
  }

  public void setEdgeInfoIsSet(boolean value) {
    if (!value) {
      this.edgeInfo = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case NODE:
      if (value == null) {
        unsetNode();
      } else {
        setNode((NodeInfoData)value);
      }
      break;

    case PREFERENCE:
      if (value == null) {
        unsetPreference();
      } else {
        setPreference((WritePreference)value);
      }
      break;

    case RELIABILITY:
      if (value == null) {
        unsetReliability();
      } else {
        setReliability((java.lang.Double)value);
      }
      break;

    case EDGE_INFO:
      if (value == null) {
        unsetEdgeInfo();
      } else {
        setEdgeInfo((EdgeInfoData)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NODE:
      return getNode();

    case PREFERENCE:
      return getPreference();

    case RELIABILITY:
      return getReliability();

    case EDGE_INFO:
      return getEdgeInfo();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NODE:
      return isSetNode();
    case PREFERENCE:
      return isSetPreference();
    case RELIABILITY:
      return isSetReliability();
    case EDGE_INFO:
      return isSetEdgeInfo();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof WritableFogData)
      return this.equals((WritableFogData)that);
    return false;
  }

  public boolean equals(WritableFogData that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_node = true && this.isSetNode();
    boolean that_present_node = true && that.isSetNode();
    if (this_present_node || that_present_node) {
      if (!(this_present_node && that_present_node))
        return false;
      if (!this.node.equals(that.node))
        return false;
    }

    boolean this_present_preference = true && this.isSetPreference();
    boolean that_present_preference = true && that.isSetPreference();
    if (this_present_preference || that_present_preference) {
      if (!(this_present_preference && that_present_preference))
        return false;
      if (!this.preference.equals(that.preference))
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

    boolean this_present_edgeInfo = true && this.isSetEdgeInfo();
    boolean that_present_edgeInfo = true && that.isSetEdgeInfo();
    if (this_present_edgeInfo || that_present_edgeInfo) {
      if (!(this_present_edgeInfo && that_present_edgeInfo))
        return false;
      if (!this.edgeInfo.equals(that.edgeInfo))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetNode()) ? 131071 : 524287);
    if (isSetNode())
      hashCode = hashCode * 8191 + node.hashCode();

    hashCode = hashCode * 8191 + ((isSetPreference()) ? 131071 : 524287);
    if (isSetPreference())
      hashCode = hashCode * 8191 + preference.getValue();

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(reliability);

    hashCode = hashCode * 8191 + ((isSetEdgeInfo()) ? 131071 : 524287);
    if (isSetEdgeInfo())
      hashCode = hashCode * 8191 + edgeInfo.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(WritableFogData other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetNode()).compareTo(other.isSetNode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNode()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.node, other.node);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetPreference()).compareTo(other.isSetPreference());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPreference()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.preference, other.preference);
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
    lastComparison = java.lang.Boolean.valueOf(isSetEdgeInfo()).compareTo(other.isSetEdgeInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEdgeInfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.edgeInfo, other.edgeInfo);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("WritableFogData(");
    boolean first = true;

    sb.append("node:");
    if (this.node == null) {
      sb.append("null");
    } else {
      sb.append(this.node);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("preference:");
    if (this.preference == null) {
      sb.append("null");
    } else {
      sb.append(this.preference);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("reliability:");
    sb.append(this.reliability);
    first = false;
    if (isSetEdgeInfo()) {
      if (!first) sb.append(", ");
      sb.append("edgeInfo:");
      if (this.edgeInfo == null) {
        sb.append("null");
      } else {
        sb.append(this.edgeInfo);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (node == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'node' was not present! Struct: " + toString());
    }
    if (preference == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'preference' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'reliability' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
    if (node != null) {
      node.validate();
    }
    if (edgeInfo != null) {
      edgeInfo.validate();
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

  private static class WritableFogDataStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public WritableFogDataStandardScheme getScheme() {
      return new WritableFogDataStandardScheme();
    }
  }

  private static class WritableFogDataStandardScheme extends org.apache.thrift.scheme.StandardScheme<WritableFogData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, WritableFogData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NODE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.node = new NodeInfoData();
              struct.node.read(iprot);
              struct.setNodeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PREFERENCE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.preference = com.dreamlab.edgefs.thrift.WritePreference.findByValue(iprot.readI32());
              struct.setPreferenceIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // RELIABILITY
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.reliability = iprot.readDouble();
              struct.setReliabilityIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // EDGE_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.edgeInfo = new EdgeInfoData();
              struct.edgeInfo.read(iprot);
              struct.setEdgeInfoIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, WritableFogData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.node != null) {
        oprot.writeFieldBegin(NODE_FIELD_DESC);
        struct.node.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.preference != null) {
        oprot.writeFieldBegin(PREFERENCE_FIELD_DESC);
        oprot.writeI32(struct.preference.getValue());
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(RELIABILITY_FIELD_DESC);
      oprot.writeDouble(struct.reliability);
      oprot.writeFieldEnd();
      if (struct.edgeInfo != null) {
        if (struct.isSetEdgeInfo()) {
          oprot.writeFieldBegin(EDGE_INFO_FIELD_DESC);
          struct.edgeInfo.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class WritableFogDataTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public WritableFogDataTupleScheme getScheme() {
      return new WritableFogDataTupleScheme();
    }
  }

  private static class WritableFogDataTupleScheme extends org.apache.thrift.scheme.TupleScheme<WritableFogData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, WritableFogData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.node.write(oprot);
      oprot.writeI32(struct.preference.getValue());
      oprot.writeDouble(struct.reliability);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetEdgeInfo()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetEdgeInfo()) {
        struct.edgeInfo.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, WritableFogData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.node = new NodeInfoData();
      struct.node.read(iprot);
      struct.setNodeIsSet(true);
      struct.preference = com.dreamlab.edgefs.thrift.WritePreference.findByValue(iprot.readI32());
      struct.setPreferenceIsSet(true);
      struct.reliability = iprot.readDouble();
      struct.setReliabilityIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.edgeInfo = new EdgeInfoData();
        struct.edgeInfo.read(iprot);
        struct.setEdgeInfoIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

