/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-11-25")
public class FindBlockQueryValue implements org.apache.thrift.TBase<FindBlockQueryValue, FindBlockQueryValue._Fields>, java.io.Serializable, Cloneable, Comparable<FindBlockQueryValue> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("FindBlockQueryValue");

  private static final org.apache.thrift.protocol.TField STREAM_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("streamId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField LOCATIONS_FIELD_DESC = new org.apache.thrift.protocol.TField("locations", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new FindBlockQueryValueStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new FindBlockQueryValueTupleSchemeFactory();

  public java.lang.String streamId; // required
  public java.util.List<FindReplica> locations; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STREAM_ID((short)1, "streamId"),
    LOCATIONS((short)2, "locations");

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
        case 1: // STREAM_ID
          return STREAM_ID;
        case 2: // LOCATIONS
          return LOCATIONS;
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
  private static final _Fields optionals[] = {_Fields.LOCATIONS};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STREAM_ID, new org.apache.thrift.meta_data.FieldMetaData("streamId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.LOCATIONS, new org.apache.thrift.meta_data.FieldMetaData("locations", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, FindReplica.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(FindBlockQueryValue.class, metaDataMap);
  }

  public FindBlockQueryValue() {
  }

  public FindBlockQueryValue(
    java.lang.String streamId)
  {
    this();
    this.streamId = streamId;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public FindBlockQueryValue(FindBlockQueryValue other) {
    if (other.isSetStreamId()) {
      this.streamId = other.streamId;
    }
    if (other.isSetLocations()) {
      java.util.List<FindReplica> __this__locations = new java.util.ArrayList<FindReplica>(other.locations.size());
      for (FindReplica other_element : other.locations) {
        __this__locations.add(new FindReplica(other_element));
      }
      this.locations = __this__locations;
    }
  }

  public FindBlockQueryValue deepCopy() {
    return new FindBlockQueryValue(this);
  }

  @Override
  public void clear() {
    this.streamId = null;
    this.locations = null;
  }

  public java.lang.String getStreamId() {
    return this.streamId;
  }

  public FindBlockQueryValue setStreamId(java.lang.String streamId) {
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

  public int getLocationsSize() {
    return (this.locations == null) ? 0 : this.locations.size();
  }

  public java.util.Iterator<FindReplica> getLocationsIterator() {
    return (this.locations == null) ? null : this.locations.iterator();
  }

  public void addToLocations(FindReplica elem) {
    if (this.locations == null) {
      this.locations = new java.util.ArrayList<FindReplica>();
    }
    this.locations.add(elem);
  }

  public java.util.List<FindReplica> getLocations() {
    return this.locations;
  }

  public FindBlockQueryValue setLocations(java.util.List<FindReplica> locations) {
    this.locations = locations;
    return this;
  }

  public void unsetLocations() {
    this.locations = null;
  }

  /** Returns true if field locations is set (has been assigned a value) and false otherwise */
  public boolean isSetLocations() {
    return this.locations != null;
  }

  public void setLocationsIsSet(boolean value) {
    if (!value) {
      this.locations = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case STREAM_ID:
      if (value == null) {
        unsetStreamId();
      } else {
        setStreamId((java.lang.String)value);
      }
      break;

    case LOCATIONS:
      if (value == null) {
        unsetLocations();
      } else {
        setLocations((java.util.List<FindReplica>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case STREAM_ID:
      return getStreamId();

    case LOCATIONS:
      return getLocations();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case STREAM_ID:
      return isSetStreamId();
    case LOCATIONS:
      return isSetLocations();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof FindBlockQueryValue)
      return this.equals((FindBlockQueryValue)that);
    return false;
  }

  public boolean equals(FindBlockQueryValue that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_streamId = true && this.isSetStreamId();
    boolean that_present_streamId = true && that.isSetStreamId();
    if (this_present_streamId || that_present_streamId) {
      if (!(this_present_streamId && that_present_streamId))
        return false;
      if (!this.streamId.equals(that.streamId))
        return false;
    }

    boolean this_present_locations = true && this.isSetLocations();
    boolean that_present_locations = true && that.isSetLocations();
    if (this_present_locations || that_present_locations) {
      if (!(this_present_locations && that_present_locations))
        return false;
      if (!this.locations.equals(that.locations))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetStreamId()) ? 131071 : 524287);
    if (isSetStreamId())
      hashCode = hashCode * 8191 + streamId.hashCode();

    hashCode = hashCode * 8191 + ((isSetLocations()) ? 131071 : 524287);
    if (isSetLocations())
      hashCode = hashCode * 8191 + locations.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(FindBlockQueryValue other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

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
    lastComparison = java.lang.Boolean.valueOf(isSetLocations()).compareTo(other.isSetLocations());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLocations()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.locations, other.locations);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("FindBlockQueryValue(");
    boolean first = true;

    sb.append("streamId:");
    if (this.streamId == null) {
      sb.append("null");
    } else {
      sb.append(this.streamId);
    }
    first = false;
    if (isSetLocations()) {
      if (!first) sb.append(", ");
      sb.append("locations:");
      if (this.locations == null) {
        sb.append("null");
      } else {
        sb.append(this.locations);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (streamId == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'streamId' was not present! Struct: " + toString());
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

  private static class FindBlockQueryValueStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public FindBlockQueryValueStandardScheme getScheme() {
      return new FindBlockQueryValueStandardScheme();
    }
  }

  private static class FindBlockQueryValueStandardScheme extends org.apache.thrift.scheme.StandardScheme<FindBlockQueryValue> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, FindBlockQueryValue struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STREAM_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.streamId = iprot.readString();
              struct.setStreamIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // LOCATIONS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list112 = iprot.readListBegin();
                struct.locations = new java.util.ArrayList<FindReplica>(_list112.size);
                FindReplica _elem113;
                for (int _i114 = 0; _i114 < _list112.size; ++_i114)
                {
                  _elem113 = new FindReplica();
                  _elem113.read(iprot);
                  struct.locations.add(_elem113);
                }
                iprot.readListEnd();
              }
              struct.setLocationsIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, FindBlockQueryValue struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.streamId != null) {
        oprot.writeFieldBegin(STREAM_ID_FIELD_DESC);
        oprot.writeString(struct.streamId);
        oprot.writeFieldEnd();
      }
      if (struct.locations != null) {
        if (struct.isSetLocations()) {
          oprot.writeFieldBegin(LOCATIONS_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.locations.size()));
            for (FindReplica _iter115 : struct.locations)
            {
              _iter115.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class FindBlockQueryValueTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public FindBlockQueryValueTupleScheme getScheme() {
      return new FindBlockQueryValueTupleScheme();
    }
  }

  private static class FindBlockQueryValueTupleScheme extends org.apache.thrift.scheme.TupleScheme<FindBlockQueryValue> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, FindBlockQueryValue struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.streamId);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetLocations()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetLocations()) {
        {
          oprot.writeI32(struct.locations.size());
          for (FindReplica _iter116 : struct.locations)
          {
            _iter116.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, FindBlockQueryValue struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.streamId = iprot.readString();
      struct.setStreamIdIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list117 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.locations = new java.util.ArrayList<FindReplica>(_list117.size);
          FindReplica _elem118;
          for (int _i119 = 0; _i119 < _list117.size; ++_i119)
          {
            _elem118 = new FindReplica();
            _elem118.read(iprot);
            struct.locations.add(_elem118);
          }
        }
        struct.setLocationsIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

