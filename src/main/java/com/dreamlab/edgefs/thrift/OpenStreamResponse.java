/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-11-29")
public class OpenStreamResponse implements org.apache.thrift.TBase<OpenStreamResponse, OpenStreamResponse._Fields>, java.io.Serializable, Cloneable, Comparable<OpenStreamResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("OpenStreamResponse");

  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.BYTE, (short)1);
  private static final org.apache.thrift.protocol.TField MESSAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("message", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField LEASE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("leaseTime", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField SESSION_SECRET_FIELD_DESC = new org.apache.thrift.protocol.TField("sessionSecret", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField LAST_BLOCK_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("lastBlockId", org.apache.thrift.protocol.TType.I64, (short)5);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new OpenStreamResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new OpenStreamResponseTupleSchemeFactory();

  public byte status; // required
  public java.lang.String message; // optional
  public int leaseTime; // optional
  public java.lang.String sessionSecret; // optional
  public long lastBlockId; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STATUS((short)1, "status"),
    MESSAGE((short)2, "message"),
    LEASE_TIME((short)3, "leaseTime"),
    SESSION_SECRET((short)4, "sessionSecret"),
    LAST_BLOCK_ID((short)5, "lastBlockId");

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
        case 1: // STATUS
          return STATUS;
        case 2: // MESSAGE
          return MESSAGE;
        case 3: // LEASE_TIME
          return LEASE_TIME;
        case 4: // SESSION_SECRET
          return SESSION_SECRET;
        case 5: // LAST_BLOCK_ID
          return LAST_BLOCK_ID;
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
  private static final int __STATUS_ISSET_ID = 0;
  private static final int __LEASETIME_ISSET_ID = 1;
  private static final int __LASTBLOCKID_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.MESSAGE,_Fields.LEASE_TIME,_Fields.SESSION_SECRET,_Fields.LAST_BLOCK_ID};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    tmpMap.put(_Fields.MESSAGE, new org.apache.thrift.meta_data.FieldMetaData("message", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.LEASE_TIME, new org.apache.thrift.meta_data.FieldMetaData("leaseTime", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.SESSION_SECRET, new org.apache.thrift.meta_data.FieldMetaData("sessionSecret", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.LAST_BLOCK_ID, new org.apache.thrift.meta_data.FieldMetaData("lastBlockId", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(OpenStreamResponse.class, metaDataMap);
  }

  public OpenStreamResponse() {
  }

  public OpenStreamResponse(
    byte status)
  {
    this();
    this.status = status;
    setStatusIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public OpenStreamResponse(OpenStreamResponse other) {
    __isset_bitfield = other.__isset_bitfield;
    this.status = other.status;
    if (other.isSetMessage()) {
      this.message = other.message;
    }
    this.leaseTime = other.leaseTime;
    if (other.isSetSessionSecret()) {
      this.sessionSecret = other.sessionSecret;
    }
    this.lastBlockId = other.lastBlockId;
  }

  public OpenStreamResponse deepCopy() {
    return new OpenStreamResponse(this);
  }

  @Override
  public void clear() {
    setStatusIsSet(false);
    this.status = 0;
    this.message = null;
    setLeaseTimeIsSet(false);
    this.leaseTime = 0;
    this.sessionSecret = null;
    setLastBlockIdIsSet(false);
    this.lastBlockId = 0;
  }

  public byte getStatus() {
    return this.status;
  }

  public OpenStreamResponse setStatus(byte status) {
    this.status = status;
    setStatusIsSet(true);
    return this;
  }

  public void unsetStatus() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __STATUS_ISSET_ID);
  }

  /** Returns true if field status is set (has been assigned a value) and false otherwise */
  public boolean isSetStatus() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __STATUS_ISSET_ID);
  }

  public void setStatusIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __STATUS_ISSET_ID, value);
  }

  public java.lang.String getMessage() {
    return this.message;
  }

  public OpenStreamResponse setMessage(java.lang.String message) {
    this.message = message;
    return this;
  }

  public void unsetMessage() {
    this.message = null;
  }

  /** Returns true if field message is set (has been assigned a value) and false otherwise */
  public boolean isSetMessage() {
    return this.message != null;
  }

  public void setMessageIsSet(boolean value) {
    if (!value) {
      this.message = null;
    }
  }

  public int getLeaseTime() {
    return this.leaseTime;
  }

  public OpenStreamResponse setLeaseTime(int leaseTime) {
    this.leaseTime = leaseTime;
    setLeaseTimeIsSet(true);
    return this;
  }

  public void unsetLeaseTime() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __LEASETIME_ISSET_ID);
  }

  /** Returns true if field leaseTime is set (has been assigned a value) and false otherwise */
  public boolean isSetLeaseTime() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __LEASETIME_ISSET_ID);
  }

  public void setLeaseTimeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __LEASETIME_ISSET_ID, value);
  }

  public java.lang.String getSessionSecret() {
    return this.sessionSecret;
  }

  public OpenStreamResponse setSessionSecret(java.lang.String sessionSecret) {
    this.sessionSecret = sessionSecret;
    return this;
  }

  public void unsetSessionSecret() {
    this.sessionSecret = null;
  }

  /** Returns true if field sessionSecret is set (has been assigned a value) and false otherwise */
  public boolean isSetSessionSecret() {
    return this.sessionSecret != null;
  }

  public void setSessionSecretIsSet(boolean value) {
    if (!value) {
      this.sessionSecret = null;
    }
  }

  public long getLastBlockId() {
    return this.lastBlockId;
  }

  public OpenStreamResponse setLastBlockId(long lastBlockId) {
    this.lastBlockId = lastBlockId;
    setLastBlockIdIsSet(true);
    return this;
  }

  public void unsetLastBlockId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __LASTBLOCKID_ISSET_ID);
  }

  /** Returns true if field lastBlockId is set (has been assigned a value) and false otherwise */
  public boolean isSetLastBlockId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __LASTBLOCKID_ISSET_ID);
  }

  public void setLastBlockIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __LASTBLOCKID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case STATUS:
      if (value == null) {
        unsetStatus();
      } else {
        setStatus((java.lang.Byte)value);
      }
      break;

    case MESSAGE:
      if (value == null) {
        unsetMessage();
      } else {
        setMessage((java.lang.String)value);
      }
      break;

    case LEASE_TIME:
      if (value == null) {
        unsetLeaseTime();
      } else {
        setLeaseTime((java.lang.Integer)value);
      }
      break;

    case SESSION_SECRET:
      if (value == null) {
        unsetSessionSecret();
      } else {
        setSessionSecret((java.lang.String)value);
      }
      break;

    case LAST_BLOCK_ID:
      if (value == null) {
        unsetLastBlockId();
      } else {
        setLastBlockId((java.lang.Long)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case STATUS:
      return getStatus();

    case MESSAGE:
      return getMessage();

    case LEASE_TIME:
      return getLeaseTime();

    case SESSION_SECRET:
      return getSessionSecret();

    case LAST_BLOCK_ID:
      return getLastBlockId();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case STATUS:
      return isSetStatus();
    case MESSAGE:
      return isSetMessage();
    case LEASE_TIME:
      return isSetLeaseTime();
    case SESSION_SECRET:
      return isSetSessionSecret();
    case LAST_BLOCK_ID:
      return isSetLastBlockId();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof OpenStreamResponse)
      return this.equals((OpenStreamResponse)that);
    return false;
  }

  public boolean equals(OpenStreamResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_status = true;
    boolean that_present_status = true;
    if (this_present_status || that_present_status) {
      if (!(this_present_status && that_present_status))
        return false;
      if (this.status != that.status)
        return false;
    }

    boolean this_present_message = true && this.isSetMessage();
    boolean that_present_message = true && that.isSetMessage();
    if (this_present_message || that_present_message) {
      if (!(this_present_message && that_present_message))
        return false;
      if (!this.message.equals(that.message))
        return false;
    }

    boolean this_present_leaseTime = true && this.isSetLeaseTime();
    boolean that_present_leaseTime = true && that.isSetLeaseTime();
    if (this_present_leaseTime || that_present_leaseTime) {
      if (!(this_present_leaseTime && that_present_leaseTime))
        return false;
      if (this.leaseTime != that.leaseTime)
        return false;
    }

    boolean this_present_sessionSecret = true && this.isSetSessionSecret();
    boolean that_present_sessionSecret = true && that.isSetSessionSecret();
    if (this_present_sessionSecret || that_present_sessionSecret) {
      if (!(this_present_sessionSecret && that_present_sessionSecret))
        return false;
      if (!this.sessionSecret.equals(that.sessionSecret))
        return false;
    }

    boolean this_present_lastBlockId = true && this.isSetLastBlockId();
    boolean that_present_lastBlockId = true && that.isSetLastBlockId();
    if (this_present_lastBlockId || that_present_lastBlockId) {
      if (!(this_present_lastBlockId && that_present_lastBlockId))
        return false;
      if (this.lastBlockId != that.lastBlockId)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + (int) (status);

    hashCode = hashCode * 8191 + ((isSetMessage()) ? 131071 : 524287);
    if (isSetMessage())
      hashCode = hashCode * 8191 + message.hashCode();

    hashCode = hashCode * 8191 + ((isSetLeaseTime()) ? 131071 : 524287);
    if (isSetLeaseTime())
      hashCode = hashCode * 8191 + leaseTime;

    hashCode = hashCode * 8191 + ((isSetSessionSecret()) ? 131071 : 524287);
    if (isSetSessionSecret())
      hashCode = hashCode * 8191 + sessionSecret.hashCode();

    hashCode = hashCode * 8191 + ((isSetLastBlockId()) ? 131071 : 524287);
    if (isSetLastBlockId())
      hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(lastBlockId);

    return hashCode;
  }

  @Override
  public int compareTo(OpenStreamResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetStatus()).compareTo(other.isSetStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.status, other.status);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetMessage()).compareTo(other.isSetMessage());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMessage()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.message, other.message);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetLeaseTime()).compareTo(other.isSetLeaseTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLeaseTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.leaseTime, other.leaseTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetSessionSecret()).compareTo(other.isSetSessionSecret());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSessionSecret()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.sessionSecret, other.sessionSecret);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetLastBlockId()).compareTo(other.isSetLastBlockId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLastBlockId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.lastBlockId, other.lastBlockId);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("OpenStreamResponse(");
    boolean first = true;

    sb.append("status:");
    sb.append(this.status);
    first = false;
    if (isSetMessage()) {
      if (!first) sb.append(", ");
      sb.append("message:");
      if (this.message == null) {
        sb.append("null");
      } else {
        sb.append(this.message);
      }
      first = false;
    }
    if (isSetLeaseTime()) {
      if (!first) sb.append(", ");
      sb.append("leaseTime:");
      sb.append(this.leaseTime);
      first = false;
    }
    if (isSetSessionSecret()) {
      if (!first) sb.append(", ");
      sb.append("sessionSecret:");
      if (this.sessionSecret == null) {
        sb.append("null");
      } else {
        sb.append(this.sessionSecret);
      }
      first = false;
    }
    if (isSetLastBlockId()) {
      if (!first) sb.append(", ");
      sb.append("lastBlockId:");
      sb.append(this.lastBlockId);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'status' because it's a primitive and you chose the non-beans generator.
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

  private static class OpenStreamResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public OpenStreamResponseStandardScheme getScheme() {
      return new OpenStreamResponseStandardScheme();
    }
  }

  private static class OpenStreamResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<OpenStreamResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, OpenStreamResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.BYTE) {
              struct.status = iprot.readByte();
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // MESSAGE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.message = iprot.readString();
              struct.setMessageIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // LEASE_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.leaseTime = iprot.readI32();
              struct.setLeaseTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // SESSION_SECRET
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.sessionSecret = iprot.readString();
              struct.setSessionSecretIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // LAST_BLOCK_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.lastBlockId = iprot.readI64();
              struct.setLastBlockIdIsSet(true);
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
      if (!struct.isSetStatus()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'status' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, OpenStreamResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(STATUS_FIELD_DESC);
      oprot.writeByte(struct.status);
      oprot.writeFieldEnd();
      if (struct.message != null) {
        if (struct.isSetMessage()) {
          oprot.writeFieldBegin(MESSAGE_FIELD_DESC);
          oprot.writeString(struct.message);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetLeaseTime()) {
        oprot.writeFieldBegin(LEASE_TIME_FIELD_DESC);
        oprot.writeI32(struct.leaseTime);
        oprot.writeFieldEnd();
      }
      if (struct.sessionSecret != null) {
        if (struct.isSetSessionSecret()) {
          oprot.writeFieldBegin(SESSION_SECRET_FIELD_DESC);
          oprot.writeString(struct.sessionSecret);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetLastBlockId()) {
        oprot.writeFieldBegin(LAST_BLOCK_ID_FIELD_DESC);
        oprot.writeI64(struct.lastBlockId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class OpenStreamResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public OpenStreamResponseTupleScheme getScheme() {
      return new OpenStreamResponseTupleScheme();
    }
  }

  private static class OpenStreamResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<OpenStreamResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, OpenStreamResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeByte(struct.status);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetMessage()) {
        optionals.set(0);
      }
      if (struct.isSetLeaseTime()) {
        optionals.set(1);
      }
      if (struct.isSetSessionSecret()) {
        optionals.set(2);
      }
      if (struct.isSetLastBlockId()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetMessage()) {
        oprot.writeString(struct.message);
      }
      if (struct.isSetLeaseTime()) {
        oprot.writeI32(struct.leaseTime);
      }
      if (struct.isSetSessionSecret()) {
        oprot.writeString(struct.sessionSecret);
      }
      if (struct.isSetLastBlockId()) {
        oprot.writeI64(struct.lastBlockId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, OpenStreamResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.status = iprot.readByte();
      struct.setStatusIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.message = iprot.readString();
        struct.setMessageIsSet(true);
      }
      if (incoming.get(1)) {
        struct.leaseTime = iprot.readI32();
        struct.setLeaseTimeIsSet(true);
      }
      if (incoming.get(2)) {
        struct.sessionSecret = iprot.readString();
        struct.setSessionSecretIsSet(true);
      }
      if (incoming.get(3)) {
        struct.lastBlockId = iprot.readI64();
        struct.setLastBlockIdIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

