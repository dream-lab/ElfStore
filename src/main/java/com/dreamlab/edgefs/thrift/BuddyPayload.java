/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-07-22")
public class BuddyPayload implements org.apache.thrift.TBase<BuddyPayload, BuddyPayload._Fields>, java.io.Serializable, Cloneable, Comparable<BuddyPayload> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("BuddyPayload");

  private static final org.apache.thrift.protocol.TField PAYLOAD_FIELD_DESC = new org.apache.thrift.protocol.TField("payload", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField MB_ID_TO_STREAM_ID_MAP_FIELD_DESC = new org.apache.thrift.protocol.TField("mbIdToStreamIdMap", org.apache.thrift.protocol.TType.MAP, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new BuddyPayloadStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new BuddyPayloadTupleSchemeFactory();

  public java.nio.ByteBuffer payload; // required
  public java.util.Map<java.lang.Long,java.lang.String> mbIdToStreamIdMap; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PAYLOAD((short)1, "payload"),
    MB_ID_TO_STREAM_ID_MAP((short)2, "mbIdToStreamIdMap");

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
        case 1: // PAYLOAD
          return PAYLOAD;
        case 2: // MB_ID_TO_STREAM_ID_MAP
          return MB_ID_TO_STREAM_ID_MAP;
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
  private static final _Fields optionals[] = {_Fields.MB_ID_TO_STREAM_ID_MAP};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PAYLOAD, new org.apache.thrift.meta_data.FieldMetaData("payload", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.MB_ID_TO_STREAM_ID_MAP, new org.apache.thrift.meta_data.FieldMetaData("mbIdToStreamIdMap", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(BuddyPayload.class, metaDataMap);
  }

  public BuddyPayload() {
  }

  public BuddyPayload(
    java.nio.ByteBuffer payload)
  {
    this();
    this.payload = org.apache.thrift.TBaseHelper.copyBinary(payload);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public BuddyPayload(BuddyPayload other) {
    if (other.isSetPayload()) {
      this.payload = org.apache.thrift.TBaseHelper.copyBinary(other.payload);
    }
    if (other.isSetMbIdToStreamIdMap()) {
      java.util.Map<java.lang.Long,java.lang.String> __this__mbIdToStreamIdMap = new java.util.HashMap<java.lang.Long,java.lang.String>(other.mbIdToStreamIdMap);
      this.mbIdToStreamIdMap = __this__mbIdToStreamIdMap;
    }
  }

  public BuddyPayload deepCopy() {
    return new BuddyPayload(this);
  }

  @Override
  public void clear() {
    this.payload = null;
    this.mbIdToStreamIdMap = null;
  }

  public byte[] getPayload() {
    setPayload(org.apache.thrift.TBaseHelper.rightSize(payload));
    return payload == null ? null : payload.array();
  }

  public java.nio.ByteBuffer bufferForPayload() {
    return org.apache.thrift.TBaseHelper.copyBinary(payload);
  }

  public BuddyPayload setPayload(byte[] payload) {
    this.payload = payload == null ? (java.nio.ByteBuffer)null : java.nio.ByteBuffer.wrap(payload.clone());
    return this;
  }

  public BuddyPayload setPayload(java.nio.ByteBuffer payload) {
    this.payload = org.apache.thrift.TBaseHelper.copyBinary(payload);
    return this;
  }

  public void unsetPayload() {
    this.payload = null;
  }

  /** Returns true if field payload is set (has been assigned a value) and false otherwise */
  public boolean isSetPayload() {
    return this.payload != null;
  }

  public void setPayloadIsSet(boolean value) {
    if (!value) {
      this.payload = null;
    }
  }

  public int getMbIdToStreamIdMapSize() {
    return (this.mbIdToStreamIdMap == null) ? 0 : this.mbIdToStreamIdMap.size();
  }

  public void putToMbIdToStreamIdMap(long key, java.lang.String val) {
    if (this.mbIdToStreamIdMap == null) {
      this.mbIdToStreamIdMap = new java.util.HashMap<java.lang.Long,java.lang.String>();
    }
    this.mbIdToStreamIdMap.put(key, val);
  }

  public java.util.Map<java.lang.Long,java.lang.String> getMbIdToStreamIdMap() {
    return this.mbIdToStreamIdMap;
  }

  public BuddyPayload setMbIdToStreamIdMap(java.util.Map<java.lang.Long,java.lang.String> mbIdToStreamIdMap) {
    this.mbIdToStreamIdMap = mbIdToStreamIdMap;
    return this;
  }

  public void unsetMbIdToStreamIdMap() {
    this.mbIdToStreamIdMap = null;
  }

  /** Returns true if field mbIdToStreamIdMap is set (has been assigned a value) and false otherwise */
  public boolean isSetMbIdToStreamIdMap() {
    return this.mbIdToStreamIdMap != null;
  }

  public void setMbIdToStreamIdMapIsSet(boolean value) {
    if (!value) {
      this.mbIdToStreamIdMap = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case PAYLOAD:
      if (value == null) {
        unsetPayload();
      } else {
        if (value instanceof byte[]) {
          setPayload((byte[])value);
        } else {
          setPayload((java.nio.ByteBuffer)value);
        }
      }
      break;

    case MB_ID_TO_STREAM_ID_MAP:
      if (value == null) {
        unsetMbIdToStreamIdMap();
      } else {
        setMbIdToStreamIdMap((java.util.Map<java.lang.Long,java.lang.String>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case PAYLOAD:
      return getPayload();

    case MB_ID_TO_STREAM_ID_MAP:
      return getMbIdToStreamIdMap();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case PAYLOAD:
      return isSetPayload();
    case MB_ID_TO_STREAM_ID_MAP:
      return isSetMbIdToStreamIdMap();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof BuddyPayload)
      return this.equals((BuddyPayload)that);
    return false;
  }

  public boolean equals(BuddyPayload that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_payload = true && this.isSetPayload();
    boolean that_present_payload = true && that.isSetPayload();
    if (this_present_payload || that_present_payload) {
      if (!(this_present_payload && that_present_payload))
        return false;
      if (!this.payload.equals(that.payload))
        return false;
    }

    boolean this_present_mbIdToStreamIdMap = true && this.isSetMbIdToStreamIdMap();
    boolean that_present_mbIdToStreamIdMap = true && that.isSetMbIdToStreamIdMap();
    if (this_present_mbIdToStreamIdMap || that_present_mbIdToStreamIdMap) {
      if (!(this_present_mbIdToStreamIdMap && that_present_mbIdToStreamIdMap))
        return false;
      if (!this.mbIdToStreamIdMap.equals(that.mbIdToStreamIdMap))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetPayload()) ? 131071 : 524287);
    if (isSetPayload())
      hashCode = hashCode * 8191 + payload.hashCode();

    hashCode = hashCode * 8191 + ((isSetMbIdToStreamIdMap()) ? 131071 : 524287);
    if (isSetMbIdToStreamIdMap())
      hashCode = hashCode * 8191 + mbIdToStreamIdMap.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(BuddyPayload other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetPayload()).compareTo(other.isSetPayload());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPayload()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.payload, other.payload);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetMbIdToStreamIdMap()).compareTo(other.isSetMbIdToStreamIdMap());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMbIdToStreamIdMap()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mbIdToStreamIdMap, other.mbIdToStreamIdMap);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("BuddyPayload(");
    boolean first = true;

    sb.append("payload:");
    if (this.payload == null) {
      sb.append("null");
    } else {
      org.apache.thrift.TBaseHelper.toString(this.payload, sb);
    }
    first = false;
    if (isSetMbIdToStreamIdMap()) {
      if (!first) sb.append(", ");
      sb.append("mbIdToStreamIdMap:");
      if (this.mbIdToStreamIdMap == null) {
        sb.append("null");
      } else {
        sb.append(this.mbIdToStreamIdMap);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (payload == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'payload' was not present! Struct: " + toString());
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class BuddyPayloadStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public BuddyPayloadStandardScheme getScheme() {
      return new BuddyPayloadStandardScheme();
    }
  }

  private static class BuddyPayloadStandardScheme extends org.apache.thrift.scheme.StandardScheme<BuddyPayload> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, BuddyPayload struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PAYLOAD
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.payload = iprot.readBinary();
              struct.setPayloadIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // MB_ID_TO_STREAM_ID_MAP
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map18 = iprot.readMapBegin();
                struct.mbIdToStreamIdMap = new java.util.HashMap<java.lang.Long,java.lang.String>(2*_map18.size);
                long _key19;
                java.lang.String _val20;
                for (int _i21 = 0; _i21 < _map18.size; ++_i21)
                {
                  _key19 = iprot.readI64();
                  _val20 = iprot.readString();
                  struct.mbIdToStreamIdMap.put(_key19, _val20);
                }
                iprot.readMapEnd();
              }
              struct.setMbIdToStreamIdMapIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, BuddyPayload struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.payload != null) {
        oprot.writeFieldBegin(PAYLOAD_FIELD_DESC);
        oprot.writeBinary(struct.payload);
        oprot.writeFieldEnd();
      }
      if (struct.mbIdToStreamIdMap != null) {
        if (struct.isSetMbIdToStreamIdMap()) {
          oprot.writeFieldBegin(MB_ID_TO_STREAM_ID_MAP_FIELD_DESC);
          {
            oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I64, org.apache.thrift.protocol.TType.STRING, struct.mbIdToStreamIdMap.size()));
            for (java.util.Map.Entry<java.lang.Long, java.lang.String> _iter22 : struct.mbIdToStreamIdMap.entrySet())
            {
              oprot.writeI64(_iter22.getKey());
              oprot.writeString(_iter22.getValue());
            }
            oprot.writeMapEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class BuddyPayloadTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public BuddyPayloadTupleScheme getScheme() {
      return new BuddyPayloadTupleScheme();
    }
  }

  private static class BuddyPayloadTupleScheme extends org.apache.thrift.scheme.TupleScheme<BuddyPayload> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, BuddyPayload struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeBinary(struct.payload);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetMbIdToStreamIdMap()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetMbIdToStreamIdMap()) {
        {
          oprot.writeI32(struct.mbIdToStreamIdMap.size());
          for (java.util.Map.Entry<java.lang.Long, java.lang.String> _iter23 : struct.mbIdToStreamIdMap.entrySet())
          {
            oprot.writeI64(_iter23.getKey());
            oprot.writeString(_iter23.getValue());
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, BuddyPayload struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.payload = iprot.readBinary();
      struct.setPayloadIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TMap _map24 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I64, org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.mbIdToStreamIdMap = new java.util.HashMap<java.lang.Long,java.lang.String>(2*_map24.size);
          long _key25;
          java.lang.String _val26;
          for (int _i27 = 0; _i27 < _map24.size; ++_i27)
          {
            _key25 = iprot.readI64();
            _val26 = iprot.readString();
            struct.mbIdToStreamIdMap.put(_key25, _val26);
          }
        }
        struct.setMbIdToStreamIdMapIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

