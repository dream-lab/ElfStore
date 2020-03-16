/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2020-03-13")
public class StreamMetadataInfo implements org.apache.thrift.TBase<StreamMetadataInfo, StreamMetadataInfo._Fields>, java.io.Serializable, Cloneable, Comparable<StreamMetadataInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("StreamMetadataInfo");

  private static final org.apache.thrift.protocol.TField STREAM_METADATA_FIELD_DESC = new org.apache.thrift.protocol.TField("streamMetadata", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField CACHED_FIELD_DESC = new org.apache.thrift.protocol.TField("cached", org.apache.thrift.protocol.TType.BOOL, (short)2);
  private static final org.apache.thrift.protocol.TField CACHE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("cacheTime", org.apache.thrift.protocol.TType.I64, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new StreamMetadataInfoStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new StreamMetadataInfoTupleSchemeFactory();

  public StreamMetadata streamMetadata; // required
  public boolean cached; // required
  public long cacheTime; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STREAM_METADATA((short)1, "streamMetadata"),
    CACHED((short)2, "cached"),
    CACHE_TIME((short)3, "cacheTime");

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
        case 1: // STREAM_METADATA
          return STREAM_METADATA;
        case 2: // CACHED
          return CACHED;
        case 3: // CACHE_TIME
          return CACHE_TIME;
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
  private static final int __CACHED_ISSET_ID = 0;
  private static final int __CACHETIME_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.CACHE_TIME};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STREAM_METADATA, new org.apache.thrift.meta_data.FieldMetaData("streamMetadata", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, StreamMetadata.class)));
    tmpMap.put(_Fields.CACHED, new org.apache.thrift.meta_data.FieldMetaData("cached", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.CACHE_TIME, new org.apache.thrift.meta_data.FieldMetaData("cacheTime", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(StreamMetadataInfo.class, metaDataMap);
  }

  public StreamMetadataInfo() {
  }

  public StreamMetadataInfo(
    StreamMetadata streamMetadata,
    boolean cached)
  {
    this();
    this.streamMetadata = streamMetadata;
    this.cached = cached;
    setCachedIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public StreamMetadataInfo(StreamMetadataInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetStreamMetadata()) {
      this.streamMetadata = new StreamMetadata(other.streamMetadata);
    }
    this.cached = other.cached;
    this.cacheTime = other.cacheTime;
  }

  public StreamMetadataInfo deepCopy() {
    return new StreamMetadataInfo(this);
  }

  @Override
  public void clear() {
    this.streamMetadata = null;
    setCachedIsSet(false);
    this.cached = false;
    setCacheTimeIsSet(false);
    this.cacheTime = 0;
  }

  public StreamMetadata getStreamMetadata() {
    return this.streamMetadata;
  }

  public StreamMetadataInfo setStreamMetadata(StreamMetadata streamMetadata) {
    this.streamMetadata = streamMetadata;
    return this;
  }

  public void unsetStreamMetadata() {
    this.streamMetadata = null;
  }

  /** Returns true if field streamMetadata is set (has been assigned a value) and false otherwise */
  public boolean isSetStreamMetadata() {
    return this.streamMetadata != null;
  }

  public void setStreamMetadataIsSet(boolean value) {
    if (!value) {
      this.streamMetadata = null;
    }
  }

  public boolean isCached() {
    return this.cached;
  }

  public StreamMetadataInfo setCached(boolean cached) {
    this.cached = cached;
    setCachedIsSet(true);
    return this;
  }

  public void unsetCached() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __CACHED_ISSET_ID);
  }

  /** Returns true if field cached is set (has been assigned a value) and false otherwise */
  public boolean isSetCached() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __CACHED_ISSET_ID);
  }

  public void setCachedIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __CACHED_ISSET_ID, value);
  }

  public long getCacheTime() {
    return this.cacheTime;
  }

  public StreamMetadataInfo setCacheTime(long cacheTime) {
    this.cacheTime = cacheTime;
    setCacheTimeIsSet(true);
    return this;
  }

  public void unsetCacheTime() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __CACHETIME_ISSET_ID);
  }

  /** Returns true if field cacheTime is set (has been assigned a value) and false otherwise */
  public boolean isSetCacheTime() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __CACHETIME_ISSET_ID);
  }

  public void setCacheTimeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __CACHETIME_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case STREAM_METADATA:
      if (value == null) {
        unsetStreamMetadata();
      } else {
        setStreamMetadata((StreamMetadata)value);
      }
      break;

    case CACHED:
      if (value == null) {
        unsetCached();
      } else {
        setCached((java.lang.Boolean)value);
      }
      break;

    case CACHE_TIME:
      if (value == null) {
        unsetCacheTime();
      } else {
        setCacheTime((java.lang.Long)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case STREAM_METADATA:
      return getStreamMetadata();

    case CACHED:
      return isCached();

    case CACHE_TIME:
      return getCacheTime();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case STREAM_METADATA:
      return isSetStreamMetadata();
    case CACHED:
      return isSetCached();
    case CACHE_TIME:
      return isSetCacheTime();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof StreamMetadataInfo)
      return this.equals((StreamMetadataInfo)that);
    return false;
  }

  public boolean equals(StreamMetadataInfo that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_streamMetadata = true && this.isSetStreamMetadata();
    boolean that_present_streamMetadata = true && that.isSetStreamMetadata();
    if (this_present_streamMetadata || that_present_streamMetadata) {
      if (!(this_present_streamMetadata && that_present_streamMetadata))
        return false;
      if (!this.streamMetadata.equals(that.streamMetadata))
        return false;
    }

    boolean this_present_cached = true;
    boolean that_present_cached = true;
    if (this_present_cached || that_present_cached) {
      if (!(this_present_cached && that_present_cached))
        return false;
      if (this.cached != that.cached)
        return false;
    }

    boolean this_present_cacheTime = true && this.isSetCacheTime();
    boolean that_present_cacheTime = true && that.isSetCacheTime();
    if (this_present_cacheTime || that_present_cacheTime) {
      if (!(this_present_cacheTime && that_present_cacheTime))
        return false;
      if (this.cacheTime != that.cacheTime)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetStreamMetadata()) ? 131071 : 524287);
    if (isSetStreamMetadata())
      hashCode = hashCode * 8191 + streamMetadata.hashCode();

    hashCode = hashCode * 8191 + ((cached) ? 131071 : 524287);

    hashCode = hashCode * 8191 + ((isSetCacheTime()) ? 131071 : 524287);
    if (isSetCacheTime())
      hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(cacheTime);

    return hashCode;
  }

  @Override
  public int compareTo(StreamMetadataInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetStreamMetadata()).compareTo(other.isSetStreamMetadata());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStreamMetadata()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.streamMetadata, other.streamMetadata);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetCached()).compareTo(other.isSetCached());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCached()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cached, other.cached);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetCacheTime()).compareTo(other.isSetCacheTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCacheTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cacheTime, other.cacheTime);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("StreamMetadataInfo(");
    boolean first = true;

    sb.append("streamMetadata:");
    if (this.streamMetadata == null) {
      sb.append("null");
    } else {
      sb.append(this.streamMetadata);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("cached:");
    sb.append(this.cached);
    first = false;
    if (isSetCacheTime()) {
      if (!first) sb.append(", ");
      sb.append("cacheTime:");
      sb.append(this.cacheTime);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (streamMetadata == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'streamMetadata' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'cached' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
    if (streamMetadata != null) {
      streamMetadata.validate();
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

  private static class StreamMetadataInfoStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public StreamMetadataInfoStandardScheme getScheme() {
      return new StreamMetadataInfoStandardScheme();
    }
  }

  private static class StreamMetadataInfoStandardScheme extends org.apache.thrift.scheme.StandardScheme<StreamMetadataInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, StreamMetadataInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STREAM_METADATA
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.streamMetadata = new StreamMetadata();
              struct.streamMetadata.read(iprot);
              struct.setStreamMetadataIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CACHED
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.cached = iprot.readBool();
              struct.setCachedIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // CACHE_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.cacheTime = iprot.readI64();
              struct.setCacheTimeIsSet(true);
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
      if (!struct.isSetCached()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'cached' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, StreamMetadataInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.streamMetadata != null) {
        oprot.writeFieldBegin(STREAM_METADATA_FIELD_DESC);
        struct.streamMetadata.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(CACHED_FIELD_DESC);
      oprot.writeBool(struct.cached);
      oprot.writeFieldEnd();
      if (struct.isSetCacheTime()) {
        oprot.writeFieldBegin(CACHE_TIME_FIELD_DESC);
        oprot.writeI64(struct.cacheTime);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class StreamMetadataInfoTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public StreamMetadataInfoTupleScheme getScheme() {
      return new StreamMetadataInfoTupleScheme();
    }
  }

  private static class StreamMetadataInfoTupleScheme extends org.apache.thrift.scheme.TupleScheme<StreamMetadataInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, StreamMetadataInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.streamMetadata.write(oprot);
      oprot.writeBool(struct.cached);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetCacheTime()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetCacheTime()) {
        oprot.writeI64(struct.cacheTime);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, StreamMetadataInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.streamMetadata = new StreamMetadata();
      struct.streamMetadata.read(iprot);
      struct.setStreamMetadataIsSet(true);
      struct.cached = iprot.readBool();
      struct.setCachedIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.cacheTime = iprot.readI64();
        struct.setCacheTimeIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

