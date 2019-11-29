/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-11-29")
public class SQueryResponse implements org.apache.thrift.TBase<SQueryResponse, SQueryResponse._Fields>, java.io.Serializable, Cloneable, Comparable<SQueryResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SQueryResponse");

  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.BYTE, (short)1);
  private static final org.apache.thrift.protocol.TField STREAM_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("streamList", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new SQueryResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new SQueryResponseTupleSchemeFactory();

  public byte status; // required
  public java.util.List<java.lang.String> streamList; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STATUS((short)1, "status"),
    STREAM_LIST((short)2, "streamList");

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
        case 2: // STREAM_LIST
          return STREAM_LIST;
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
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    tmpMap.put(_Fields.STREAM_LIST, new org.apache.thrift.meta_data.FieldMetaData("streamList", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SQueryResponse.class, metaDataMap);
  }

  public SQueryResponse() {
  }

  public SQueryResponse(
    byte status,
    java.util.List<java.lang.String> streamList)
  {
    this();
    this.status = status;
    setStatusIsSet(true);
    this.streamList = streamList;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SQueryResponse(SQueryResponse other) {
    __isset_bitfield = other.__isset_bitfield;
    this.status = other.status;
    if (other.isSetStreamList()) {
      java.util.List<java.lang.String> __this__streamList = new java.util.ArrayList<java.lang.String>(other.streamList);
      this.streamList = __this__streamList;
    }
  }

  public SQueryResponse deepCopy() {
    return new SQueryResponse(this);
  }

  @Override
  public void clear() {
    setStatusIsSet(false);
    this.status = 0;
    this.streamList = null;
  }

  public byte getStatus() {
    return this.status;
  }

  public SQueryResponse setStatus(byte status) {
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

  public int getStreamListSize() {
    return (this.streamList == null) ? 0 : this.streamList.size();
  }

  public java.util.Iterator<java.lang.String> getStreamListIterator() {
    return (this.streamList == null) ? null : this.streamList.iterator();
  }

  public void addToStreamList(java.lang.String elem) {
    if (this.streamList == null) {
      this.streamList = new java.util.ArrayList<java.lang.String>();
    }
    this.streamList.add(elem);
  }

  public java.util.List<java.lang.String> getStreamList() {
    return this.streamList;
  }

  public SQueryResponse setStreamList(java.util.List<java.lang.String> streamList) {
    this.streamList = streamList;
    return this;
  }

  public void unsetStreamList() {
    this.streamList = null;
  }

  /** Returns true if field streamList is set (has been assigned a value) and false otherwise */
  public boolean isSetStreamList() {
    return this.streamList != null;
  }

  public void setStreamListIsSet(boolean value) {
    if (!value) {
      this.streamList = null;
    }
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

    case STREAM_LIST:
      if (value == null) {
        unsetStreamList();
      } else {
        setStreamList((java.util.List<java.lang.String>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case STATUS:
      return getStatus();

    case STREAM_LIST:
      return getStreamList();

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
    case STREAM_LIST:
      return isSetStreamList();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof SQueryResponse)
      return this.equals((SQueryResponse)that);
    return false;
  }

  public boolean equals(SQueryResponse that) {
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

    boolean this_present_streamList = true && this.isSetStreamList();
    boolean that_present_streamList = true && that.isSetStreamList();
    if (this_present_streamList || that_present_streamList) {
      if (!(this_present_streamList && that_present_streamList))
        return false;
      if (!this.streamList.equals(that.streamList))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + (int) (status);

    hashCode = hashCode * 8191 + ((isSetStreamList()) ? 131071 : 524287);
    if (isSetStreamList())
      hashCode = hashCode * 8191 + streamList.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(SQueryResponse other) {
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
    lastComparison = java.lang.Boolean.valueOf(isSetStreamList()).compareTo(other.isSetStreamList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStreamList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.streamList, other.streamList);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("SQueryResponse(");
    boolean first = true;

    sb.append("status:");
    sb.append(this.status);
    first = false;
    if (!first) sb.append(", ");
    sb.append("streamList:");
    if (this.streamList == null) {
      sb.append("null");
    } else {
      sb.append(this.streamList);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'status' because it's a primitive and you chose the non-beans generator.
    if (streamList == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'streamList' was not present! Struct: " + toString());
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

  private static class SQueryResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SQueryResponseStandardScheme getScheme() {
      return new SQueryResponseStandardScheme();
    }
  }

  private static class SQueryResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<SQueryResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SQueryResponse struct) throws org.apache.thrift.TException {
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
          case 2: // STREAM_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list58 = iprot.readListBegin();
                struct.streamList = new java.util.ArrayList<java.lang.String>(_list58.size);
                java.lang.String _elem59;
                for (int _i60 = 0; _i60 < _list58.size; ++_i60)
                {
                  _elem59 = iprot.readString();
                  struct.streamList.add(_elem59);
                }
                iprot.readListEnd();
              }
              struct.setStreamListIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, SQueryResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(STATUS_FIELD_DESC);
      oprot.writeByte(struct.status);
      oprot.writeFieldEnd();
      if (struct.streamList != null) {
        oprot.writeFieldBegin(STREAM_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.streamList.size()));
          for (java.lang.String _iter61 : struct.streamList)
          {
            oprot.writeString(_iter61);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SQueryResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SQueryResponseTupleScheme getScheme() {
      return new SQueryResponseTupleScheme();
    }
  }

  private static class SQueryResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<SQueryResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SQueryResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeByte(struct.status);
      {
        oprot.writeI32(struct.streamList.size());
        for (java.lang.String _iter62 : struct.streamList)
        {
          oprot.writeString(_iter62);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SQueryResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.status = iprot.readByte();
      struct.setStatusIsSet(true);
      {
        org.apache.thrift.protocol.TList _list63 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
        struct.streamList = new java.util.ArrayList<java.lang.String>(_list63.size);
        java.lang.String _elem64;
        for (int _i65 = 0; _i65 < _list63.size; ++_i65)
        {
          _elem64 = iprot.readString();
          struct.streamList.add(_elem64);
        }
      }
      struct.setStreamListIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

