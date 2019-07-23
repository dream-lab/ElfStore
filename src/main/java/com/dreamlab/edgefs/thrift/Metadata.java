/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-07-23")
public class Metadata implements org.apache.thrift.TBase<Metadata, Metadata._Fields>, java.io.Serializable, Cloneable, Comparable<Metadata> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Metadata");

  private static final org.apache.thrift.protocol.TField CLIENT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("clientId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField SESSION_SECRET_FIELD_DESC = new org.apache.thrift.protocol.TField("sessionSecret", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField STREAM_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("streamId", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField MB_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("mbId", org.apache.thrift.protocol.TType.I64, (short)4);
  private static final org.apache.thrift.protocol.TField TIMESTAMP_FIELD_DESC = new org.apache.thrift.protocol.TField("timestamp", org.apache.thrift.protocol.TType.I64, (short)5);
  private static final org.apache.thrift.protocol.TField CHECKSUM_FIELD_DESC = new org.apache.thrift.protocol.TField("checksum", org.apache.thrift.protocol.TType.STRING, (short)6);
  private static final org.apache.thrift.protocol.TField PROPERTIES_FIELD_DESC = new org.apache.thrift.protocol.TField("properties", org.apache.thrift.protocol.TType.STRING, (short)7);
  private static final org.apache.thrift.protocol.TField COMP_FORMAT_FIELD_DESC = new org.apache.thrift.protocol.TField("compFormat", org.apache.thrift.protocol.TType.STRING, (short)8);
  private static final org.apache.thrift.protocol.TField UNCOMP_SIZE_FIELD_DESC = new org.apache.thrift.protocol.TField("uncompSize", org.apache.thrift.protocol.TType.I64, (short)9);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new MetadataStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new MetadataTupleSchemeFactory();

  public java.lang.String clientId; // required
  public java.lang.String sessionSecret; // required
  public java.lang.String streamId; // required
  public long mbId; // required
  public long timestamp; // required
  public java.lang.String checksum; // optional
  public java.lang.String properties; // optional
  public java.lang.String compFormat; // optional
  public long uncompSize; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CLIENT_ID((short)1, "clientId"),
    SESSION_SECRET((short)2, "sessionSecret"),
    STREAM_ID((short)3, "streamId"),
    MB_ID((short)4, "mbId"),
    TIMESTAMP((short)5, "timestamp"),
    CHECKSUM((short)6, "checksum"),
    PROPERTIES((short)7, "properties"),
    COMP_FORMAT((short)8, "compFormat"),
    UNCOMP_SIZE((short)9, "uncompSize");

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
        case 1: // CLIENT_ID
          return CLIENT_ID;
        case 2: // SESSION_SECRET
          return SESSION_SECRET;
        case 3: // STREAM_ID
          return STREAM_ID;
        case 4: // MB_ID
          return MB_ID;
        case 5: // TIMESTAMP
          return TIMESTAMP;
        case 6: // CHECKSUM
          return CHECKSUM;
        case 7: // PROPERTIES
          return PROPERTIES;
        case 8: // COMP_FORMAT
          return COMP_FORMAT;
        case 9: // UNCOMP_SIZE
          return UNCOMP_SIZE;
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
  private static final int __MBID_ISSET_ID = 0;
  private static final int __TIMESTAMP_ISSET_ID = 1;
  private static final int __UNCOMPSIZE_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.CHECKSUM,_Fields.PROPERTIES,_Fields.COMP_FORMAT,_Fields.UNCOMP_SIZE};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CLIENT_ID, new org.apache.thrift.meta_data.FieldMetaData("clientId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.SESSION_SECRET, new org.apache.thrift.meta_data.FieldMetaData("sessionSecret", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.STREAM_ID, new org.apache.thrift.meta_data.FieldMetaData("streamId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.MB_ID, new org.apache.thrift.meta_data.FieldMetaData("mbId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.TIMESTAMP, new org.apache.thrift.meta_data.FieldMetaData("timestamp", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.CHECKSUM, new org.apache.thrift.meta_data.FieldMetaData("checksum", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PROPERTIES, new org.apache.thrift.meta_data.FieldMetaData("properties", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.COMP_FORMAT, new org.apache.thrift.meta_data.FieldMetaData("compFormat", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.UNCOMP_SIZE, new org.apache.thrift.meta_data.FieldMetaData("uncompSize", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Metadata.class, metaDataMap);
  }

  public Metadata() {
  }

  public Metadata(
    java.lang.String clientId,
    java.lang.String sessionSecret,
    java.lang.String streamId,
    long mbId,
    long timestamp)
  {
    this();
    this.clientId = clientId;
    this.sessionSecret = sessionSecret;
    this.streamId = streamId;
    this.mbId = mbId;
    setMbIdIsSet(true);
    this.timestamp = timestamp;
    setTimestampIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Metadata(Metadata other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetClientId()) {
      this.clientId = other.clientId;
    }
    if (other.isSetSessionSecret()) {
      this.sessionSecret = other.sessionSecret;
    }
    if (other.isSetStreamId()) {
      this.streamId = other.streamId;
    }
    this.mbId = other.mbId;
    this.timestamp = other.timestamp;
    if (other.isSetChecksum()) {
      this.checksum = other.checksum;
    }
    if (other.isSetProperties()) {
      this.properties = other.properties;
    }
    if (other.isSetCompFormat()) {
      this.compFormat = other.compFormat;
    }
    this.uncompSize = other.uncompSize;
  }

  public Metadata deepCopy() {
    return new Metadata(this);
  }

  @Override
  public void clear() {
    this.clientId = null;
    this.sessionSecret = null;
    this.streamId = null;
    setMbIdIsSet(false);
    this.mbId = 0;
    setTimestampIsSet(false);
    this.timestamp = 0;
    this.checksum = null;
    this.properties = null;
    this.compFormat = null;
    setUncompSizeIsSet(false);
    this.uncompSize = 0;
  }

  public java.lang.String getClientId() {
    return this.clientId;
  }

  public Metadata setClientId(java.lang.String clientId) {
    this.clientId = clientId;
    return this;
  }

  public void unsetClientId() {
    this.clientId = null;
  }

  /** Returns true if field clientId is set (has been assigned a value) and false otherwise */
  public boolean isSetClientId() {
    return this.clientId != null;
  }

  public void setClientIdIsSet(boolean value) {
    if (!value) {
      this.clientId = null;
    }
  }

  public java.lang.String getSessionSecret() {
    return this.sessionSecret;
  }

  public Metadata setSessionSecret(java.lang.String sessionSecret) {
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

  public java.lang.String getStreamId() {
    return this.streamId;
  }

  public Metadata setStreamId(java.lang.String streamId) {
    this.streamId = streamId;
    return this;
  }

  public void unsetStreamId() {
    this.streamId = null;
  }

  /** Returns true if field streamId is set (has been assigned a value) and false otherwise */
  public boolean isSetStreamId() {
    return this.streamId != null;
  }

  public void setStreamIdIsSet(boolean value) {
    if (!value) {
      this.streamId = null;
    }
  }

  public long getMbId() {
    return this.mbId;
  }

  public Metadata setMbId(long mbId) {
    this.mbId = mbId;
    setMbIdIsSet(true);
    return this;
  }

  public void unsetMbId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __MBID_ISSET_ID);
  }

  /** Returns true if field mbId is set (has been assigned a value) and false otherwise */
  public boolean isSetMbId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __MBID_ISSET_ID);
  }

  public void setMbIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __MBID_ISSET_ID, value);
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public Metadata setTimestamp(long timestamp) {
    this.timestamp = timestamp;
    setTimestampIsSet(true);
    return this;
  }

  public void unsetTimestamp() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __TIMESTAMP_ISSET_ID);
  }

  /** Returns true if field timestamp is set (has been assigned a value) and false otherwise */
  public boolean isSetTimestamp() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __TIMESTAMP_ISSET_ID);
  }

  public void setTimestampIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __TIMESTAMP_ISSET_ID, value);
  }

  public java.lang.String getChecksum() {
    return this.checksum;
  }

  public Metadata setChecksum(java.lang.String checksum) {
    this.checksum = checksum;
    return this;
  }

  public void unsetChecksum() {
    this.checksum = null;
  }

  /** Returns true if field checksum is set (has been assigned a value) and false otherwise */
  public boolean isSetChecksum() {
    return this.checksum != null;
  }

  public void setChecksumIsSet(boolean value) {
    if (!value) {
      this.checksum = null;
    }
  }

  public java.lang.String getProperties() {
    return this.properties;
  }

  public Metadata setProperties(java.lang.String properties) {
    this.properties = properties;
    return this;
  }

  public void unsetProperties() {
    this.properties = null;
  }

  /** Returns true if field properties is set (has been assigned a value) and false otherwise */
  public boolean isSetProperties() {
    return this.properties != null;
  }

  public void setPropertiesIsSet(boolean value) {
    if (!value) {
      this.properties = null;
    }
  }

  public java.lang.String getCompFormat() {
    return this.compFormat;
  }

  public Metadata setCompFormat(java.lang.String compFormat) {
    this.compFormat = compFormat;
    return this;
  }

  public void unsetCompFormat() {
    this.compFormat = null;
  }

  /** Returns true if field compFormat is set (has been assigned a value) and false otherwise */
  public boolean isSetCompFormat() {
    return this.compFormat != null;
  }

  public void setCompFormatIsSet(boolean value) {
    if (!value) {
      this.compFormat = null;
    }
  }

  public long getUncompSize() {
    return this.uncompSize;
  }

  public Metadata setUncompSize(long uncompSize) {
    this.uncompSize = uncompSize;
    setUncompSizeIsSet(true);
    return this;
  }

  public void unsetUncompSize() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __UNCOMPSIZE_ISSET_ID);
  }

  /** Returns true if field uncompSize is set (has been assigned a value) and false otherwise */
  public boolean isSetUncompSize() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __UNCOMPSIZE_ISSET_ID);
  }

  public void setUncompSizeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __UNCOMPSIZE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case CLIENT_ID:
      if (value == null) {
        unsetClientId();
      } else {
        setClientId((java.lang.String)value);
      }
      break;

    case SESSION_SECRET:
      if (value == null) {
        unsetSessionSecret();
      } else {
        setSessionSecret((java.lang.String)value);
      }
      break;

    case STREAM_ID:
      if (value == null) {
        unsetStreamId();
      } else {
        setStreamId((java.lang.String)value);
      }
      break;

    case MB_ID:
      if (value == null) {
        unsetMbId();
      } else {
        setMbId((java.lang.Long)value);
      }
      break;

    case TIMESTAMP:
      if (value == null) {
        unsetTimestamp();
      } else {
        setTimestamp((java.lang.Long)value);
      }
      break;

    case CHECKSUM:
      if (value == null) {
        unsetChecksum();
      } else {
        setChecksum((java.lang.String)value);
      }
      break;

    case PROPERTIES:
      if (value == null) {
        unsetProperties();
      } else {
        setProperties((java.lang.String)value);
      }
      break;

    case COMP_FORMAT:
      if (value == null) {
        unsetCompFormat();
      } else {
        setCompFormat((java.lang.String)value);
      }
      break;

    case UNCOMP_SIZE:
      if (value == null) {
        unsetUncompSize();
      } else {
        setUncompSize((java.lang.Long)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case CLIENT_ID:
      return getClientId();

    case SESSION_SECRET:
      return getSessionSecret();

    case STREAM_ID:
      return getStreamId();

    case MB_ID:
      return getMbId();

    case TIMESTAMP:
      return getTimestamp();

    case CHECKSUM:
      return getChecksum();

    case PROPERTIES:
      return getProperties();

    case COMP_FORMAT:
      return getCompFormat();

    case UNCOMP_SIZE:
      return getUncompSize();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case CLIENT_ID:
      return isSetClientId();
    case SESSION_SECRET:
      return isSetSessionSecret();
    case STREAM_ID:
      return isSetStreamId();
    case MB_ID:
      return isSetMbId();
    case TIMESTAMP:
      return isSetTimestamp();
    case CHECKSUM:
      return isSetChecksum();
    case PROPERTIES:
      return isSetProperties();
    case COMP_FORMAT:
      return isSetCompFormat();
    case UNCOMP_SIZE:
      return isSetUncompSize();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof Metadata)
      return this.equals((Metadata)that);
    return false;
  }

  public boolean equals(Metadata that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_clientId = true && this.isSetClientId();
    boolean that_present_clientId = true && that.isSetClientId();
    if (this_present_clientId || that_present_clientId) {
      if (!(this_present_clientId && that_present_clientId))
        return false;
      if (!this.clientId.equals(that.clientId))
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

    boolean this_present_streamId = true && this.isSetStreamId();
    boolean that_present_streamId = true && that.isSetStreamId();
    if (this_present_streamId || that_present_streamId) {
      if (!(this_present_streamId && that_present_streamId))
        return false;
      if (!this.streamId.equals(that.streamId))
        return false;
    }

    boolean this_present_mbId = true;
    boolean that_present_mbId = true;
    if (this_present_mbId || that_present_mbId) {
      if (!(this_present_mbId && that_present_mbId))
        return false;
      if (this.mbId != that.mbId)
        return false;
    }

    boolean this_present_timestamp = true;
    boolean that_present_timestamp = true;
    if (this_present_timestamp || that_present_timestamp) {
      if (!(this_present_timestamp && that_present_timestamp))
        return false;
      if (this.timestamp != that.timestamp)
        return false;
    }

    boolean this_present_checksum = true && this.isSetChecksum();
    boolean that_present_checksum = true && that.isSetChecksum();
    if (this_present_checksum || that_present_checksum) {
      if (!(this_present_checksum && that_present_checksum))
        return false;
      if (!this.checksum.equals(that.checksum))
        return false;
    }

    boolean this_present_properties = true && this.isSetProperties();
    boolean that_present_properties = true && that.isSetProperties();
    if (this_present_properties || that_present_properties) {
      if (!(this_present_properties && that_present_properties))
        return false;
      if (!this.properties.equals(that.properties))
        return false;
    }

    boolean this_present_compFormat = true && this.isSetCompFormat();
    boolean that_present_compFormat = true && that.isSetCompFormat();
    if (this_present_compFormat || that_present_compFormat) {
      if (!(this_present_compFormat && that_present_compFormat))
        return false;
      if (!this.compFormat.equals(that.compFormat))
        return false;
    }

    boolean this_present_uncompSize = true && this.isSetUncompSize();
    boolean that_present_uncompSize = true && that.isSetUncompSize();
    if (this_present_uncompSize || that_present_uncompSize) {
      if (!(this_present_uncompSize && that_present_uncompSize))
        return false;
      if (this.uncompSize != that.uncompSize)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetClientId()) ? 131071 : 524287);
    if (isSetClientId())
      hashCode = hashCode * 8191 + clientId.hashCode();

    hashCode = hashCode * 8191 + ((isSetSessionSecret()) ? 131071 : 524287);
    if (isSetSessionSecret())
      hashCode = hashCode * 8191 + sessionSecret.hashCode();

    hashCode = hashCode * 8191 + ((isSetStreamId()) ? 131071 : 524287);
    if (isSetStreamId())
      hashCode = hashCode * 8191 + streamId.hashCode();

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(mbId);

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(timestamp);

    hashCode = hashCode * 8191 + ((isSetChecksum()) ? 131071 : 524287);
    if (isSetChecksum())
      hashCode = hashCode * 8191 + checksum.hashCode();

    hashCode = hashCode * 8191 + ((isSetProperties()) ? 131071 : 524287);
    if (isSetProperties())
      hashCode = hashCode * 8191 + properties.hashCode();

    hashCode = hashCode * 8191 + ((isSetCompFormat()) ? 131071 : 524287);
    if (isSetCompFormat())
      hashCode = hashCode * 8191 + compFormat.hashCode();

    hashCode = hashCode * 8191 + ((isSetUncompSize()) ? 131071 : 524287);
    if (isSetUncompSize())
      hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(uncompSize);

    return hashCode;
  }

  @Override
  public int compareTo(Metadata other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetClientId()).compareTo(other.isSetClientId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetClientId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.clientId, other.clientId);
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
    lastComparison = java.lang.Boolean.valueOf(isSetStreamId()).compareTo(other.isSetStreamId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStreamId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.streamId, other.streamId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetMbId()).compareTo(other.isSetMbId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMbId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mbId, other.mbId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetTimestamp()).compareTo(other.isSetTimestamp());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTimestamp()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.timestamp, other.timestamp);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetChecksum()).compareTo(other.isSetChecksum());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetChecksum()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.checksum, other.checksum);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetProperties()).compareTo(other.isSetProperties());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProperties()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.properties, other.properties);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetCompFormat()).compareTo(other.isSetCompFormat());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCompFormat()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.compFormat, other.compFormat);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetUncompSize()).compareTo(other.isSetUncompSize());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUncompSize()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.uncompSize, other.uncompSize);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("Metadata(");
    boolean first = true;

    sb.append("clientId:");
    if (this.clientId == null) {
      sb.append("null");
    } else {
      sb.append(this.clientId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("sessionSecret:");
    if (this.sessionSecret == null) {
      sb.append("null");
    } else {
      sb.append(this.sessionSecret);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("streamId:");
    if (this.streamId == null) {
      sb.append("null");
    } else {
      sb.append(this.streamId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("mbId:");
    sb.append(this.mbId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("timestamp:");
    sb.append(this.timestamp);
    first = false;
    if (isSetChecksum()) {
      if (!first) sb.append(", ");
      sb.append("checksum:");
      if (this.checksum == null) {
        sb.append("null");
      } else {
        sb.append(this.checksum);
      }
      first = false;
    }
    if (isSetProperties()) {
      if (!first) sb.append(", ");
      sb.append("properties:");
      if (this.properties == null) {
        sb.append("null");
      } else {
        sb.append(this.properties);
      }
      first = false;
    }
    if (isSetCompFormat()) {
      if (!first) sb.append(", ");
      sb.append("compFormat:");
      if (this.compFormat == null) {
        sb.append("null");
      } else {
        sb.append(this.compFormat);
      }
      first = false;
    }
    if (isSetUncompSize()) {
      if (!first) sb.append(", ");
      sb.append("uncompSize:");
      sb.append(this.uncompSize);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (clientId == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'clientId' was not present! Struct: " + toString());
    }
    if (sessionSecret == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'sessionSecret' was not present! Struct: " + toString());
    }
    if (streamId == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'streamId' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'mbId' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'timestamp' because it's a primitive and you chose the non-beans generator.
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

  private static class MetadataStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public MetadataStandardScheme getScheme() {
      return new MetadataStandardScheme();
    }
  }

  private static class MetadataStandardScheme extends org.apache.thrift.scheme.StandardScheme<Metadata> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Metadata struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CLIENT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.clientId = iprot.readString();
              struct.setClientIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SESSION_SECRET
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.sessionSecret = iprot.readString();
              struct.setSessionSecretIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // STREAM_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.streamId = iprot.readString();
              struct.setStreamIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // MB_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.mbId = iprot.readI64();
              struct.setMbIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // TIMESTAMP
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.timestamp = iprot.readI64();
              struct.setTimestampIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // CHECKSUM
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.checksum = iprot.readString();
              struct.setChecksumIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // PROPERTIES
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.properties = iprot.readString();
              struct.setPropertiesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 8: // COMP_FORMAT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.compFormat = iprot.readString();
              struct.setCompFormatIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 9: // UNCOMP_SIZE
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.uncompSize = iprot.readI64();
              struct.setUncompSizeIsSet(true);
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
      if (!struct.isSetMbId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'mbId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetTimestamp()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'timestamp' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Metadata struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.clientId != null) {
        oprot.writeFieldBegin(CLIENT_ID_FIELD_DESC);
        oprot.writeString(struct.clientId);
        oprot.writeFieldEnd();
      }
      if (struct.sessionSecret != null) {
        oprot.writeFieldBegin(SESSION_SECRET_FIELD_DESC);
        oprot.writeString(struct.sessionSecret);
        oprot.writeFieldEnd();
      }
      if (struct.streamId != null) {
        oprot.writeFieldBegin(STREAM_ID_FIELD_DESC);
        oprot.writeString(struct.streamId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(MB_ID_FIELD_DESC);
      oprot.writeI64(struct.mbId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(TIMESTAMP_FIELD_DESC);
      oprot.writeI64(struct.timestamp);
      oprot.writeFieldEnd();
      if (struct.checksum != null) {
        if (struct.isSetChecksum()) {
          oprot.writeFieldBegin(CHECKSUM_FIELD_DESC);
          oprot.writeString(struct.checksum);
          oprot.writeFieldEnd();
        }
      }
      if (struct.properties != null) {
        if (struct.isSetProperties()) {
          oprot.writeFieldBegin(PROPERTIES_FIELD_DESC);
          oprot.writeString(struct.properties);
          oprot.writeFieldEnd();
        }
      }
      if (struct.compFormat != null) {
        if (struct.isSetCompFormat()) {
          oprot.writeFieldBegin(COMP_FORMAT_FIELD_DESC);
          oprot.writeString(struct.compFormat);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetUncompSize()) {
        oprot.writeFieldBegin(UNCOMP_SIZE_FIELD_DESC);
        oprot.writeI64(struct.uncompSize);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class MetadataTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public MetadataTupleScheme getScheme() {
      return new MetadataTupleScheme();
    }
  }

  private static class MetadataTupleScheme extends org.apache.thrift.scheme.TupleScheme<Metadata> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Metadata struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.clientId);
      oprot.writeString(struct.sessionSecret);
      oprot.writeString(struct.streamId);
      oprot.writeI64(struct.mbId);
      oprot.writeI64(struct.timestamp);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetChecksum()) {
        optionals.set(0);
      }
      if (struct.isSetProperties()) {
        optionals.set(1);
      }
      if (struct.isSetCompFormat()) {
        optionals.set(2);
      }
      if (struct.isSetUncompSize()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetChecksum()) {
        oprot.writeString(struct.checksum);
      }
      if (struct.isSetProperties()) {
        oprot.writeString(struct.properties);
      }
      if (struct.isSetCompFormat()) {
        oprot.writeString(struct.compFormat);
      }
      if (struct.isSetUncompSize()) {
        oprot.writeI64(struct.uncompSize);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Metadata struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.clientId = iprot.readString();
      struct.setClientIdIsSet(true);
      struct.sessionSecret = iprot.readString();
      struct.setSessionSecretIsSet(true);
      struct.streamId = iprot.readString();
      struct.setStreamIdIsSet(true);
      struct.mbId = iprot.readI64();
      struct.setMbIdIsSet(true);
      struct.timestamp = iprot.readI64();
      struct.setTimestampIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.checksum = iprot.readString();
        struct.setChecksumIsSet(true);
      }
      if (incoming.get(1)) {
        struct.properties = iprot.readString();
        struct.setPropertiesIsSet(true);
      }
      if (incoming.get(2)) {
        struct.compFormat = iprot.readString();
        struct.setCompFormatIsSet(true);
      }
      if (incoming.get(3)) {
        struct.uncompSize = iprot.readI64();
        struct.setUncompSizeIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

