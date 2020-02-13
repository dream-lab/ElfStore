/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2020-02-13")
public class SQueryRequest implements org.apache.thrift.TBase<SQueryRequest, SQueryRequest._Fields>, java.io.Serializable, Cloneable, Comparable<SQueryRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SQueryRequest");

  private static final org.apache.thrift.protocol.TField START_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("startTime", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField RELIABILITY_FIELD_DESC = new org.apache.thrift.protocol.TField("reliability", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField MIN_REPLICA_FIELD_DESC = new org.apache.thrift.protocol.TField("minReplica", org.apache.thrift.protocol.TType.STRUCT, (short)3);
  private static final org.apache.thrift.protocol.TField MAX_REPLICA_FIELD_DESC = new org.apache.thrift.protocol.TField("maxReplica", org.apache.thrift.protocol.TType.STRUCT, (short)4);
  private static final org.apache.thrift.protocol.TField VERSION_FIELD_DESC = new org.apache.thrift.protocol.TField("version", org.apache.thrift.protocol.TType.STRUCT, (short)5);
  private static final org.apache.thrift.protocol.TField OWNER_FIELD_DESC = new org.apache.thrift.protocol.TField("owner", org.apache.thrift.protocol.TType.STRUCT, (short)6);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new SQueryRequestStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new SQueryRequestTupleSchemeFactory();

  public I64TypeStreamMetadata startTime; // optional
  public DoubleTypeStreamMetadata reliability; // optional
  public ByteTypeStreamMetadata minReplica; // optional
  public ByteTypeStreamMetadata maxReplica; // optional
  public I32TypeStreamMetadata version; // optional
  public NodeInfoPrimaryTypeStreamMetadata owner; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    START_TIME((short)1, "startTime"),
    RELIABILITY((short)2, "reliability"),
    MIN_REPLICA((short)3, "minReplica"),
    MAX_REPLICA((short)4, "maxReplica"),
    VERSION((short)5, "version"),
    OWNER((short)6, "owner");

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
        case 1: // START_TIME
          return START_TIME;
        case 2: // RELIABILITY
          return RELIABILITY;
        case 3: // MIN_REPLICA
          return MIN_REPLICA;
        case 4: // MAX_REPLICA
          return MAX_REPLICA;
        case 5: // VERSION
          return VERSION;
        case 6: // OWNER
          return OWNER;
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
  private static final _Fields optionals[] = {_Fields.START_TIME,_Fields.RELIABILITY,_Fields.MIN_REPLICA,_Fields.MAX_REPLICA,_Fields.VERSION,_Fields.OWNER};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.START_TIME, new org.apache.thrift.meta_data.FieldMetaData("startTime", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, I64TypeStreamMetadata.class)));
    tmpMap.put(_Fields.RELIABILITY, new org.apache.thrift.meta_data.FieldMetaData("reliability", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, DoubleTypeStreamMetadata.class)));
    tmpMap.put(_Fields.MIN_REPLICA, new org.apache.thrift.meta_data.FieldMetaData("minReplica", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ByteTypeStreamMetadata.class)));
    tmpMap.put(_Fields.MAX_REPLICA, new org.apache.thrift.meta_data.FieldMetaData("maxReplica", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ByteTypeStreamMetadata.class)));
    tmpMap.put(_Fields.VERSION, new org.apache.thrift.meta_data.FieldMetaData("version", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, I32TypeStreamMetadata.class)));
    tmpMap.put(_Fields.OWNER, new org.apache.thrift.meta_data.FieldMetaData("owner", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, NodeInfoPrimaryTypeStreamMetadata.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SQueryRequest.class, metaDataMap);
  }

  public SQueryRequest() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SQueryRequest(SQueryRequest other) {
    if (other.isSetStartTime()) {
      this.startTime = new I64TypeStreamMetadata(other.startTime);
    }
    if (other.isSetReliability()) {
      this.reliability = new DoubleTypeStreamMetadata(other.reliability);
    }
    if (other.isSetMinReplica()) {
      this.minReplica = new ByteTypeStreamMetadata(other.minReplica);
    }
    if (other.isSetMaxReplica()) {
      this.maxReplica = new ByteTypeStreamMetadata(other.maxReplica);
    }
    if (other.isSetVersion()) {
      this.version = new I32TypeStreamMetadata(other.version);
    }
    if (other.isSetOwner()) {
      this.owner = new NodeInfoPrimaryTypeStreamMetadata(other.owner);
    }
  }

  public SQueryRequest deepCopy() {
    return new SQueryRequest(this);
  }

  @Override
  public void clear() {
    this.startTime = null;
    this.reliability = null;
    this.minReplica = null;
    this.maxReplica = null;
    this.version = null;
    this.owner = null;
  }

  public I64TypeStreamMetadata getStartTime() {
    return this.startTime;
  }

  public SQueryRequest setStartTime(I64TypeStreamMetadata startTime) {
    this.startTime = startTime;
    return this;
  }

  public void unsetStartTime() {
    this.startTime = null;
  }

  /** Returns true if field startTime is set (has been assigned a value) and false otherwise */
  public boolean isSetStartTime() {
    return this.startTime != null;
  }

  public void setStartTimeIsSet(boolean value) {
    if (!value) {
      this.startTime = null;
    }
  }

  public DoubleTypeStreamMetadata getReliability() {
    return this.reliability;
  }

  public SQueryRequest setReliability(DoubleTypeStreamMetadata reliability) {
    this.reliability = reliability;
    return this;
  }

  public void unsetReliability() {
    this.reliability = null;
  }

  /** Returns true if field reliability is set (has been assigned a value) and false otherwise */
  public boolean isSetReliability() {
    return this.reliability != null;
  }

  public void setReliabilityIsSet(boolean value) {
    if (!value) {
      this.reliability = null;
    }
  }

  public ByteTypeStreamMetadata getMinReplica() {
    return this.minReplica;
  }

  public SQueryRequest setMinReplica(ByteTypeStreamMetadata minReplica) {
    this.minReplica = minReplica;
    return this;
  }

  public void unsetMinReplica() {
    this.minReplica = null;
  }

  /** Returns true if field minReplica is set (has been assigned a value) and false otherwise */
  public boolean isSetMinReplica() {
    return this.minReplica != null;
  }

  public void setMinReplicaIsSet(boolean value) {
    if (!value) {
      this.minReplica = null;
    }
  }

  public ByteTypeStreamMetadata getMaxReplica() {
    return this.maxReplica;
  }

  public SQueryRequest setMaxReplica(ByteTypeStreamMetadata maxReplica) {
    this.maxReplica = maxReplica;
    return this;
  }

  public void unsetMaxReplica() {
    this.maxReplica = null;
  }

  /** Returns true if field maxReplica is set (has been assigned a value) and false otherwise */
  public boolean isSetMaxReplica() {
    return this.maxReplica != null;
  }

  public void setMaxReplicaIsSet(boolean value) {
    if (!value) {
      this.maxReplica = null;
    }
  }

  public I32TypeStreamMetadata getVersion() {
    return this.version;
  }

  public SQueryRequest setVersion(I32TypeStreamMetadata version) {
    this.version = version;
    return this;
  }

  public void unsetVersion() {
    this.version = null;
  }

  /** Returns true if field version is set (has been assigned a value) and false otherwise */
  public boolean isSetVersion() {
    return this.version != null;
  }

  public void setVersionIsSet(boolean value) {
    if (!value) {
      this.version = null;
    }
  }

  public NodeInfoPrimaryTypeStreamMetadata getOwner() {
    return this.owner;
  }

  public SQueryRequest setOwner(NodeInfoPrimaryTypeStreamMetadata owner) {
    this.owner = owner;
    return this;
  }

  public void unsetOwner() {
    this.owner = null;
  }

  /** Returns true if field owner is set (has been assigned a value) and false otherwise */
  public boolean isSetOwner() {
    return this.owner != null;
  }

  public void setOwnerIsSet(boolean value) {
    if (!value) {
      this.owner = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case START_TIME:
      if (value == null) {
        unsetStartTime();
      } else {
        setStartTime((I64TypeStreamMetadata)value);
      }
      break;

    case RELIABILITY:
      if (value == null) {
        unsetReliability();
      } else {
        setReliability((DoubleTypeStreamMetadata)value);
      }
      break;

    case MIN_REPLICA:
      if (value == null) {
        unsetMinReplica();
      } else {
        setMinReplica((ByteTypeStreamMetadata)value);
      }
      break;

    case MAX_REPLICA:
      if (value == null) {
        unsetMaxReplica();
      } else {
        setMaxReplica((ByteTypeStreamMetadata)value);
      }
      break;

    case VERSION:
      if (value == null) {
        unsetVersion();
      } else {
        setVersion((I32TypeStreamMetadata)value);
      }
      break;

    case OWNER:
      if (value == null) {
        unsetOwner();
      } else {
        setOwner((NodeInfoPrimaryTypeStreamMetadata)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case START_TIME:
      return getStartTime();

    case RELIABILITY:
      return getReliability();

    case MIN_REPLICA:
      return getMinReplica();

    case MAX_REPLICA:
      return getMaxReplica();

    case VERSION:
      return getVersion();

    case OWNER:
      return getOwner();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case START_TIME:
      return isSetStartTime();
    case RELIABILITY:
      return isSetReliability();
    case MIN_REPLICA:
      return isSetMinReplica();
    case MAX_REPLICA:
      return isSetMaxReplica();
    case VERSION:
      return isSetVersion();
    case OWNER:
      return isSetOwner();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof SQueryRequest)
      return this.equals((SQueryRequest)that);
    return false;
  }

  public boolean equals(SQueryRequest that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_startTime = true && this.isSetStartTime();
    boolean that_present_startTime = true && that.isSetStartTime();
    if (this_present_startTime || that_present_startTime) {
      if (!(this_present_startTime && that_present_startTime))
        return false;
      if (!this.startTime.equals(that.startTime))
        return false;
    }

    boolean this_present_reliability = true && this.isSetReliability();
    boolean that_present_reliability = true && that.isSetReliability();
    if (this_present_reliability || that_present_reliability) {
      if (!(this_present_reliability && that_present_reliability))
        return false;
      if (!this.reliability.equals(that.reliability))
        return false;
    }

    boolean this_present_minReplica = true && this.isSetMinReplica();
    boolean that_present_minReplica = true && that.isSetMinReplica();
    if (this_present_minReplica || that_present_minReplica) {
      if (!(this_present_minReplica && that_present_minReplica))
        return false;
      if (!this.minReplica.equals(that.minReplica))
        return false;
    }

    boolean this_present_maxReplica = true && this.isSetMaxReplica();
    boolean that_present_maxReplica = true && that.isSetMaxReplica();
    if (this_present_maxReplica || that_present_maxReplica) {
      if (!(this_present_maxReplica && that_present_maxReplica))
        return false;
      if (!this.maxReplica.equals(that.maxReplica))
        return false;
    }

    boolean this_present_version = true && this.isSetVersion();
    boolean that_present_version = true && that.isSetVersion();
    if (this_present_version || that_present_version) {
      if (!(this_present_version && that_present_version))
        return false;
      if (!this.version.equals(that.version))
        return false;
    }

    boolean this_present_owner = true && this.isSetOwner();
    boolean that_present_owner = true && that.isSetOwner();
    if (this_present_owner || that_present_owner) {
      if (!(this_present_owner && that_present_owner))
        return false;
      if (!this.owner.equals(that.owner))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetStartTime()) ? 131071 : 524287);
    if (isSetStartTime())
      hashCode = hashCode * 8191 + startTime.hashCode();

    hashCode = hashCode * 8191 + ((isSetReliability()) ? 131071 : 524287);
    if (isSetReliability())
      hashCode = hashCode * 8191 + reliability.hashCode();

    hashCode = hashCode * 8191 + ((isSetMinReplica()) ? 131071 : 524287);
    if (isSetMinReplica())
      hashCode = hashCode * 8191 + minReplica.hashCode();

    hashCode = hashCode * 8191 + ((isSetMaxReplica()) ? 131071 : 524287);
    if (isSetMaxReplica())
      hashCode = hashCode * 8191 + maxReplica.hashCode();

    hashCode = hashCode * 8191 + ((isSetVersion()) ? 131071 : 524287);
    if (isSetVersion())
      hashCode = hashCode * 8191 + version.hashCode();

    hashCode = hashCode * 8191 + ((isSetOwner()) ? 131071 : 524287);
    if (isSetOwner())
      hashCode = hashCode * 8191 + owner.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(SQueryRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetStartTime()).compareTo(other.isSetStartTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStartTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.startTime, other.startTime);
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
    lastComparison = java.lang.Boolean.valueOf(isSetMinReplica()).compareTo(other.isSetMinReplica());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMinReplica()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.minReplica, other.minReplica);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetMaxReplica()).compareTo(other.isSetMaxReplica());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMaxReplica()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.maxReplica, other.maxReplica);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetVersion()).compareTo(other.isSetVersion());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVersion()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.version, other.version);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetOwner()).compareTo(other.isSetOwner());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOwner()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.owner, other.owner);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("SQueryRequest(");
    boolean first = true;

    if (isSetStartTime()) {
      sb.append("startTime:");
      if (this.startTime == null) {
        sb.append("null");
      } else {
        sb.append(this.startTime);
      }
      first = false;
    }
    if (isSetReliability()) {
      if (!first) sb.append(", ");
      sb.append("reliability:");
      if (this.reliability == null) {
        sb.append("null");
      } else {
        sb.append(this.reliability);
      }
      first = false;
    }
    if (isSetMinReplica()) {
      if (!first) sb.append(", ");
      sb.append("minReplica:");
      if (this.minReplica == null) {
        sb.append("null");
      } else {
        sb.append(this.minReplica);
      }
      first = false;
    }
    if (isSetMaxReplica()) {
      if (!first) sb.append(", ");
      sb.append("maxReplica:");
      if (this.maxReplica == null) {
        sb.append("null");
      } else {
        sb.append(this.maxReplica);
      }
      first = false;
    }
    if (isSetVersion()) {
      if (!first) sb.append(", ");
      sb.append("version:");
      if (this.version == null) {
        sb.append("null");
      } else {
        sb.append(this.version);
      }
      first = false;
    }
    if (isSetOwner()) {
      if (!first) sb.append(", ");
      sb.append("owner:");
      if (this.owner == null) {
        sb.append("null");
      } else {
        sb.append(this.owner);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (startTime != null) {
      startTime.validate();
    }
    if (reliability != null) {
      reliability.validate();
    }
    if (minReplica != null) {
      minReplica.validate();
    }
    if (maxReplica != null) {
      maxReplica.validate();
    }
    if (version != null) {
      version.validate();
    }
    if (owner != null) {
      owner.validate();
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class SQueryRequestStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SQueryRequestStandardScheme getScheme() {
      return new SQueryRequestStandardScheme();
    }
  }

  private static class SQueryRequestStandardScheme extends org.apache.thrift.scheme.StandardScheme<SQueryRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SQueryRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // START_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.startTime = new I64TypeStreamMetadata();
              struct.startTime.read(iprot);
              struct.setStartTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // RELIABILITY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.reliability = new DoubleTypeStreamMetadata();
              struct.reliability.read(iprot);
              struct.setReliabilityIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // MIN_REPLICA
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.minReplica = new ByteTypeStreamMetadata();
              struct.minReplica.read(iprot);
              struct.setMinReplicaIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // MAX_REPLICA
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.maxReplica = new ByteTypeStreamMetadata();
              struct.maxReplica.read(iprot);
              struct.setMaxReplicaIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // VERSION
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.version = new I32TypeStreamMetadata();
              struct.version.read(iprot);
              struct.setVersionIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // OWNER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.owner = new NodeInfoPrimaryTypeStreamMetadata();
              struct.owner.read(iprot);
              struct.setOwnerIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, SQueryRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.startTime != null) {
        if (struct.isSetStartTime()) {
          oprot.writeFieldBegin(START_TIME_FIELD_DESC);
          struct.startTime.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.reliability != null) {
        if (struct.isSetReliability()) {
          oprot.writeFieldBegin(RELIABILITY_FIELD_DESC);
          struct.reliability.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.minReplica != null) {
        if (struct.isSetMinReplica()) {
          oprot.writeFieldBegin(MIN_REPLICA_FIELD_DESC);
          struct.minReplica.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.maxReplica != null) {
        if (struct.isSetMaxReplica()) {
          oprot.writeFieldBegin(MAX_REPLICA_FIELD_DESC);
          struct.maxReplica.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.version != null) {
        if (struct.isSetVersion()) {
          oprot.writeFieldBegin(VERSION_FIELD_DESC);
          struct.version.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.owner != null) {
        if (struct.isSetOwner()) {
          oprot.writeFieldBegin(OWNER_FIELD_DESC);
          struct.owner.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SQueryRequestTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SQueryRequestTupleScheme getScheme() {
      return new SQueryRequestTupleScheme();
    }
  }

  private static class SQueryRequestTupleScheme extends org.apache.thrift.scheme.TupleScheme<SQueryRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SQueryRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetStartTime()) {
        optionals.set(0);
      }
      if (struct.isSetReliability()) {
        optionals.set(1);
      }
      if (struct.isSetMinReplica()) {
        optionals.set(2);
      }
      if (struct.isSetMaxReplica()) {
        optionals.set(3);
      }
      if (struct.isSetVersion()) {
        optionals.set(4);
      }
      if (struct.isSetOwner()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetStartTime()) {
        struct.startTime.write(oprot);
      }
      if (struct.isSetReliability()) {
        struct.reliability.write(oprot);
      }
      if (struct.isSetMinReplica()) {
        struct.minReplica.write(oprot);
      }
      if (struct.isSetMaxReplica()) {
        struct.maxReplica.write(oprot);
      }
      if (struct.isSetVersion()) {
        struct.version.write(oprot);
      }
      if (struct.isSetOwner()) {
        struct.owner.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SQueryRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.startTime = new I64TypeStreamMetadata();
        struct.startTime.read(iprot);
        struct.setStartTimeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.reliability = new DoubleTypeStreamMetadata();
        struct.reliability.read(iprot);
        struct.setReliabilityIsSet(true);
      }
      if (incoming.get(2)) {
        struct.minReplica = new ByteTypeStreamMetadata();
        struct.minReplica.read(iprot);
        struct.setMinReplicaIsSet(true);
      }
      if (incoming.get(3)) {
        struct.maxReplica = new ByteTypeStreamMetadata();
        struct.maxReplica.read(iprot);
        struct.setMaxReplicaIsSet(true);
      }
      if (incoming.get(4)) {
        struct.version = new I32TypeStreamMetadata();
        struct.version.read(iprot);
        struct.setVersionIsSet(true);
      }
      if (incoming.get(5)) {
        struct.owner = new NodeInfoPrimaryTypeStreamMetadata();
        struct.owner.read(iprot);
        struct.setOwnerIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

