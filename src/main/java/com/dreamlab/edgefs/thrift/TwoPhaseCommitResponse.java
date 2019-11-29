/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-11-29")
public class TwoPhaseCommitResponse implements org.apache.thrift.TBase<TwoPhaseCommitResponse, TwoPhaseCommitResponse._Fields>, java.io.Serializable, Cloneable, Comparable<TwoPhaseCommitResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TwoPhaseCommitResponse");

  private static final org.apache.thrift.protocol.TField RESPONSE_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("responseType", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField BUDDY_POOL_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("buddyPoolId", org.apache.thrift.protocol.TType.I16, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TwoPhaseCommitResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TwoPhaseCommitResponseTupleSchemeFactory();

  public java.lang.String responseType; // required
  public short buddyPoolId; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    RESPONSE_TYPE((short)1, "responseType"),
    BUDDY_POOL_ID((short)2, "buddyPoolId");

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
        case 1: // RESPONSE_TYPE
          return RESPONSE_TYPE;
        case 2: // BUDDY_POOL_ID
          return BUDDY_POOL_ID;
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
  private static final int __BUDDYPOOLID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.BUDDY_POOL_ID};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.RESPONSE_TYPE, new org.apache.thrift.meta_data.FieldMetaData("responseType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.BUDDY_POOL_ID, new org.apache.thrift.meta_data.FieldMetaData("buddyPoolId", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TwoPhaseCommitResponse.class, metaDataMap);
  }

  public TwoPhaseCommitResponse() {
  }

  public TwoPhaseCommitResponse(
    java.lang.String responseType)
  {
    this();
    this.responseType = responseType;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TwoPhaseCommitResponse(TwoPhaseCommitResponse other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetResponseType()) {
      this.responseType = other.responseType;
    }
    this.buddyPoolId = other.buddyPoolId;
  }

  public TwoPhaseCommitResponse deepCopy() {
    return new TwoPhaseCommitResponse(this);
  }

  @Override
  public void clear() {
    this.responseType = null;
    setBuddyPoolIdIsSet(false);
    this.buddyPoolId = 0;
  }

  public java.lang.String getResponseType() {
    return this.responseType;
  }

  public TwoPhaseCommitResponse setResponseType(java.lang.String responseType) {
    this.responseType = responseType;
    return this;
  }

  public void unsetResponseType() {
    this.responseType = null;
  }

  /** Returns true if field responseType is set (has been assigned a value) and false otherwise */
  public boolean isSetResponseType() {
    return this.responseType != null;
  }

  public void setResponseTypeIsSet(boolean value) {
    if (!value) {
      this.responseType = null;
    }
  }

  public short getBuddyPoolId() {
    return this.buddyPoolId;
  }

  public TwoPhaseCommitResponse setBuddyPoolId(short buddyPoolId) {
    this.buddyPoolId = buddyPoolId;
    setBuddyPoolIdIsSet(true);
    return this;
  }

  public void unsetBuddyPoolId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __BUDDYPOOLID_ISSET_ID);
  }

  /** Returns true if field buddyPoolId is set (has been assigned a value) and false otherwise */
  public boolean isSetBuddyPoolId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __BUDDYPOOLID_ISSET_ID);
  }

  public void setBuddyPoolIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __BUDDYPOOLID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case RESPONSE_TYPE:
      if (value == null) {
        unsetResponseType();
      } else {
        setResponseType((java.lang.String)value);
      }
      break;

    case BUDDY_POOL_ID:
      if (value == null) {
        unsetBuddyPoolId();
      } else {
        setBuddyPoolId((java.lang.Short)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case RESPONSE_TYPE:
      return getResponseType();

    case BUDDY_POOL_ID:
      return getBuddyPoolId();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case RESPONSE_TYPE:
      return isSetResponseType();
    case BUDDY_POOL_ID:
      return isSetBuddyPoolId();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof TwoPhaseCommitResponse)
      return this.equals((TwoPhaseCommitResponse)that);
    return false;
  }

  public boolean equals(TwoPhaseCommitResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_responseType = true && this.isSetResponseType();
    boolean that_present_responseType = true && that.isSetResponseType();
    if (this_present_responseType || that_present_responseType) {
      if (!(this_present_responseType && that_present_responseType))
        return false;
      if (!this.responseType.equals(that.responseType))
        return false;
    }

    boolean this_present_buddyPoolId = true && this.isSetBuddyPoolId();
    boolean that_present_buddyPoolId = true && that.isSetBuddyPoolId();
    if (this_present_buddyPoolId || that_present_buddyPoolId) {
      if (!(this_present_buddyPoolId && that_present_buddyPoolId))
        return false;
      if (this.buddyPoolId != that.buddyPoolId)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetResponseType()) ? 131071 : 524287);
    if (isSetResponseType())
      hashCode = hashCode * 8191 + responseType.hashCode();

    hashCode = hashCode * 8191 + ((isSetBuddyPoolId()) ? 131071 : 524287);
    if (isSetBuddyPoolId())
      hashCode = hashCode * 8191 + buddyPoolId;

    return hashCode;
  }

  @Override
  public int compareTo(TwoPhaseCommitResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetResponseType()).compareTo(other.isSetResponseType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResponseType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.responseType, other.responseType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetBuddyPoolId()).compareTo(other.isSetBuddyPoolId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBuddyPoolId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.buddyPoolId, other.buddyPoolId);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TwoPhaseCommitResponse(");
    boolean first = true;

    sb.append("responseType:");
    if (this.responseType == null) {
      sb.append("null");
    } else {
      sb.append(this.responseType);
    }
    first = false;
    if (isSetBuddyPoolId()) {
      if (!first) sb.append(", ");
      sb.append("buddyPoolId:");
      sb.append(this.buddyPoolId);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (responseType == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'responseType' was not present! Struct: " + toString());
    }
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

  private static class TwoPhaseCommitResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TwoPhaseCommitResponseStandardScheme getScheme() {
      return new TwoPhaseCommitResponseStandardScheme();
    }
  }

  private static class TwoPhaseCommitResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<TwoPhaseCommitResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TwoPhaseCommitResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // RESPONSE_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.responseType = iprot.readString();
              struct.setResponseTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // BUDDY_POOL_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.buddyPoolId = iprot.readI16();
              struct.setBuddyPoolIdIsSet(true);
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
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TwoPhaseCommitResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.responseType != null) {
        oprot.writeFieldBegin(RESPONSE_TYPE_FIELD_DESC);
        oprot.writeString(struct.responseType);
        oprot.writeFieldEnd();
      }
      if (struct.isSetBuddyPoolId()) {
        oprot.writeFieldBegin(BUDDY_POOL_ID_FIELD_DESC);
        oprot.writeI16(struct.buddyPoolId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TwoPhaseCommitResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TwoPhaseCommitResponseTupleScheme getScheme() {
      return new TwoPhaseCommitResponseTupleScheme();
    }
  }

  private static class TwoPhaseCommitResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<TwoPhaseCommitResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TwoPhaseCommitResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.responseType);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetBuddyPoolId()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetBuddyPoolId()) {
        oprot.writeI16(struct.buddyPoolId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TwoPhaseCommitResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.responseType = iprot.readString();
      struct.setResponseTypeIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.buddyPoolId = iprot.readI16();
        struct.setBuddyPoolIdIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

