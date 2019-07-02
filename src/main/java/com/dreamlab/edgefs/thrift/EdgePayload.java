/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-07-02")
public class EdgePayload implements org.apache.thrift.TBase<EdgePayload, EdgePayload._Fields>, java.io.Serializable, Cloneable, Comparable<EdgePayload> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("EdgePayload");

  private static final org.apache.thrift.protocol.TField EDGE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("edgeId", org.apache.thrift.protocol.TType.I16, (short)1);
  private static final org.apache.thrift.protocol.TField ENCODED_STORAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("encodedStorage", org.apache.thrift.protocol.TType.BYTE, (short)2);
  private static final org.apache.thrift.protocol.TField RELIABILITY_FIELD_DESC = new org.apache.thrift.protocol.TField("reliability", org.apache.thrift.protocol.TType.BYTE, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new EdgePayloadStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new EdgePayloadTupleSchemeFactory();

  public short edgeId; // required
  public byte encodedStorage; // optional
  public byte reliability; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    EDGE_ID((short)1, "edgeId"),
    ENCODED_STORAGE((short)2, "encodedStorage"),
    RELIABILITY((short)3, "reliability");

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
        case 1: // EDGE_ID
          return EDGE_ID;
        case 2: // ENCODED_STORAGE
          return ENCODED_STORAGE;
        case 3: // RELIABILITY
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
  private static final int __EDGEID_ISSET_ID = 0;
  private static final int __ENCODEDSTORAGE_ISSET_ID = 1;
  private static final int __RELIABILITY_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.ENCODED_STORAGE,_Fields.RELIABILITY};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.EDGE_ID, new org.apache.thrift.meta_data.FieldMetaData("edgeId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    tmpMap.put(_Fields.ENCODED_STORAGE, new org.apache.thrift.meta_data.FieldMetaData("encodedStorage", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    tmpMap.put(_Fields.RELIABILITY, new org.apache.thrift.meta_data.FieldMetaData("reliability", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(EdgePayload.class, metaDataMap);
  }

  public EdgePayload() {
  }

  public EdgePayload(
    short edgeId)
  {
    this();
    this.edgeId = edgeId;
    setEdgeIdIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public EdgePayload(EdgePayload other) {
    __isset_bitfield = other.__isset_bitfield;
    this.edgeId = other.edgeId;
    this.encodedStorage = other.encodedStorage;
    this.reliability = other.reliability;
  }

  public EdgePayload deepCopy() {
    return new EdgePayload(this);
  }

  @Override
  public void clear() {
    setEdgeIdIsSet(false);
    this.edgeId = 0;
    setEncodedStorageIsSet(false);
    this.encodedStorage = 0;
    setReliabilityIsSet(false);
    this.reliability = 0;
  }

  public short getEdgeId() {
    return this.edgeId;
  }

  public EdgePayload setEdgeId(short edgeId) {
    this.edgeId = edgeId;
    setEdgeIdIsSet(true);
    return this;
  }

  public void unsetEdgeId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __EDGEID_ISSET_ID);
  }

  /** Returns true if field edgeId is set (has been assigned a value) and false otherwise */
  public boolean isSetEdgeId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __EDGEID_ISSET_ID);
  }

  public void setEdgeIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __EDGEID_ISSET_ID, value);
  }

  public byte getEncodedStorage() {
    return this.encodedStorage;
  }

  public EdgePayload setEncodedStorage(byte encodedStorage) {
    this.encodedStorage = encodedStorage;
    setEncodedStorageIsSet(true);
    return this;
  }

  public void unsetEncodedStorage() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ENCODEDSTORAGE_ISSET_ID);
  }

  /** Returns true if field encodedStorage is set (has been assigned a value) and false otherwise */
  public boolean isSetEncodedStorage() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ENCODEDSTORAGE_ISSET_ID);
  }

  public void setEncodedStorageIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ENCODEDSTORAGE_ISSET_ID, value);
  }

  public byte getReliability() {
    return this.reliability;
  }

  public EdgePayload setReliability(byte reliability) {
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
    case EDGE_ID:
      if (value == null) {
        unsetEdgeId();
      } else {
        setEdgeId((java.lang.Short)value);
      }
      break;

    case ENCODED_STORAGE:
      if (value == null) {
        unsetEncodedStorage();
      } else {
        setEncodedStorage((java.lang.Byte)value);
      }
      break;

    case RELIABILITY:
      if (value == null) {
        unsetReliability();
      } else {
        setReliability((java.lang.Byte)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case EDGE_ID:
      return getEdgeId();

    case ENCODED_STORAGE:
      return getEncodedStorage();

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
    case EDGE_ID:
      return isSetEdgeId();
    case ENCODED_STORAGE:
      return isSetEncodedStorage();
    case RELIABILITY:
      return isSetReliability();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof EdgePayload)
      return this.equals((EdgePayload)that);
    return false;
  }

  public boolean equals(EdgePayload that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_edgeId = true;
    boolean that_present_edgeId = true;
    if (this_present_edgeId || that_present_edgeId) {
      if (!(this_present_edgeId && that_present_edgeId))
        return false;
      if (this.edgeId != that.edgeId)
        return false;
    }

    boolean this_present_encodedStorage = true && this.isSetEncodedStorage();
    boolean that_present_encodedStorage = true && that.isSetEncodedStorage();
    if (this_present_encodedStorage || that_present_encodedStorage) {
      if (!(this_present_encodedStorage && that_present_encodedStorage))
        return false;
      if (this.encodedStorage != that.encodedStorage)
        return false;
    }

    boolean this_present_reliability = true && this.isSetReliability();
    boolean that_present_reliability = true && that.isSetReliability();
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

    hashCode = hashCode * 8191 + edgeId;

    hashCode = hashCode * 8191 + ((isSetEncodedStorage()) ? 131071 : 524287);
    if (isSetEncodedStorage())
      hashCode = hashCode * 8191 + (int) (encodedStorage);

    hashCode = hashCode * 8191 + ((isSetReliability()) ? 131071 : 524287);
    if (isSetReliability())
      hashCode = hashCode * 8191 + (int) (reliability);

    return hashCode;
  }

  @Override
  public int compareTo(EdgePayload other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetEdgeId()).compareTo(other.isSetEdgeId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEdgeId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.edgeId, other.edgeId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetEncodedStorage()).compareTo(other.isSetEncodedStorage());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEncodedStorage()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.encodedStorage, other.encodedStorage);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("EdgePayload(");
    boolean first = true;

    sb.append("edgeId:");
    sb.append(this.edgeId);
    first = false;
    if (isSetEncodedStorage()) {
      if (!first) sb.append(", ");
      sb.append("encodedStorage:");
      sb.append(this.encodedStorage);
      first = false;
    }
    if (isSetReliability()) {
      if (!first) sb.append(", ");
      sb.append("reliability:");
      sb.append(this.reliability);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'edgeId' because it's a primitive and you chose the non-beans generator.
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

  private static class EdgePayloadStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public EdgePayloadStandardScheme getScheme() {
      return new EdgePayloadStandardScheme();
    }
  }

  private static class EdgePayloadStandardScheme extends org.apache.thrift.scheme.StandardScheme<EdgePayload> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, EdgePayload struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // EDGE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.edgeId = iprot.readI16();
              struct.setEdgeIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ENCODED_STORAGE
            if (schemeField.type == org.apache.thrift.protocol.TType.BYTE) {
              struct.encodedStorage = iprot.readByte();
              struct.setEncodedStorageIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // RELIABILITY
            if (schemeField.type == org.apache.thrift.protocol.TType.BYTE) {
              struct.reliability = iprot.readByte();
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
      if (!struct.isSetEdgeId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'edgeId' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, EdgePayload struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(EDGE_ID_FIELD_DESC);
      oprot.writeI16(struct.edgeId);
      oprot.writeFieldEnd();
      if (struct.isSetEncodedStorage()) {
        oprot.writeFieldBegin(ENCODED_STORAGE_FIELD_DESC);
        oprot.writeByte(struct.encodedStorage);
        oprot.writeFieldEnd();
      }
      if (struct.isSetReliability()) {
        oprot.writeFieldBegin(RELIABILITY_FIELD_DESC);
        oprot.writeByte(struct.reliability);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class EdgePayloadTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public EdgePayloadTupleScheme getScheme() {
      return new EdgePayloadTupleScheme();
    }
  }

  private static class EdgePayloadTupleScheme extends org.apache.thrift.scheme.TupleScheme<EdgePayload> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, EdgePayload struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI16(struct.edgeId);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetEncodedStorage()) {
        optionals.set(0);
      }
      if (struct.isSetReliability()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetEncodedStorage()) {
        oprot.writeByte(struct.encodedStorage);
      }
      if (struct.isSetReliability()) {
        oprot.writeByte(struct.reliability);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, EdgePayload struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.edgeId = iprot.readI16();
      struct.setEdgeIdIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.encodedStorage = iprot.readByte();
        struct.setEncodedStorageIsSet(true);
      }
      if (incoming.get(1)) {
        struct.reliability = iprot.readByte();
        struct.setReliabilityIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

