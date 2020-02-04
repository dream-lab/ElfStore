/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2020-02-04")
public class MetadataResponse implements org.apache.thrift.TBase<MetadataResponse, MetadataResponse._Fields>, java.io.Serializable, Cloneable, Comparable<MetadataResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("MetadataResponse");

  private static final org.apache.thrift.protocol.TField RESULT_FIELD_DESC = new org.apache.thrift.protocol.TField("result", org.apache.thrift.protocol.TType.MAP, (short)1);
  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.BYTE, (short)2);
  private static final org.apache.thrift.protocol.TField ERROR_RESPONSE_FIELD_DESC = new org.apache.thrift.protocol.TField("errorResponse", org.apache.thrift.protocol.TType.STRING, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new MetadataResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new MetadataResponseTupleSchemeFactory();

  public java.util.Map<java.lang.String,java.util.List<java.lang.String>> result; // required
  public byte status; // required
  public java.lang.String errorResponse; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    RESULT((short)1, "result"),
    STATUS((short)2, "status"),
    ERROR_RESPONSE((short)3, "errorResponse");

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
        case 1: // RESULT
          return RESULT;
        case 2: // STATUS
          return STATUS;
        case 3: // ERROR_RESPONSE
          return ERROR_RESPONSE;
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
  private static final _Fields optionals[] = {_Fields.ERROR_RESPONSE};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.RESULT, new org.apache.thrift.meta_data.FieldMetaData("result", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
                new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)))));
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BYTE)));
    tmpMap.put(_Fields.ERROR_RESPONSE, new org.apache.thrift.meta_data.FieldMetaData("errorResponse", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(MetadataResponse.class, metaDataMap);
  }

  public MetadataResponse() {
  }

  public MetadataResponse(
    java.util.Map<java.lang.String,java.util.List<java.lang.String>> result,
    byte status)
  {
    this();
    this.result = result;
    this.status = status;
    setStatusIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MetadataResponse(MetadataResponse other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetResult()) {
      java.util.Map<java.lang.String,java.util.List<java.lang.String>> __this__result = new java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>(other.result.size());
      for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> other_element : other.result.entrySet()) {

        java.lang.String other_element_key = other_element.getKey();
        java.util.List<java.lang.String> other_element_value = other_element.getValue();

        java.lang.String __this__result_copy_key = other_element_key;

        java.util.List<java.lang.String> __this__result_copy_value = new java.util.ArrayList<java.lang.String>(other_element_value);

        __this__result.put(__this__result_copy_key, __this__result_copy_value);
      }
      this.result = __this__result;
    }
    this.status = other.status;
    if (other.isSetErrorResponse()) {
      this.errorResponse = other.errorResponse;
    }
  }

  public MetadataResponse deepCopy() {
    return new MetadataResponse(this);
  }

  @Override
  public void clear() {
    this.result = null;
    setStatusIsSet(false);
    this.status = 0;
    this.errorResponse = null;
  }

  public int getResultSize() {
    return (this.result == null) ? 0 : this.result.size();
  }

  public void putToResult(java.lang.String key, java.util.List<java.lang.String> val) {
    if (this.result == null) {
      this.result = new java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>();
    }
    this.result.put(key, val);
  }

  public java.util.Map<java.lang.String,java.util.List<java.lang.String>> getResult() {
    return this.result;
  }

  public MetadataResponse setResult(java.util.Map<java.lang.String,java.util.List<java.lang.String>> result) {
    this.result = result;
    return this;
  }

  public void unsetResult() {
    this.result = null;
  }

  /** Returns true if field result is set (has been assigned a value) and false otherwise */
  public boolean isSetResult() {
    return this.result != null;
  }

  public void setResultIsSet(boolean value) {
    if (!value) {
      this.result = null;
    }
  }

  public byte getStatus() {
    return this.status;
  }

  public MetadataResponse setStatus(byte status) {
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

  public java.lang.String getErrorResponse() {
    return this.errorResponse;
  }

  public MetadataResponse setErrorResponse(java.lang.String errorResponse) {
    this.errorResponse = errorResponse;
    return this;
  }

  public void unsetErrorResponse() {
    this.errorResponse = null;
  }

  /** Returns true if field errorResponse is set (has been assigned a value) and false otherwise */
  public boolean isSetErrorResponse() {
    return this.errorResponse != null;
  }

  public void setErrorResponseIsSet(boolean value) {
    if (!value) {
      this.errorResponse = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case RESULT:
      if (value == null) {
        unsetResult();
      } else {
        setResult((java.util.Map<java.lang.String,java.util.List<java.lang.String>>)value);
      }
      break;

    case STATUS:
      if (value == null) {
        unsetStatus();
      } else {
        setStatus((java.lang.Byte)value);
      }
      break;

    case ERROR_RESPONSE:
      if (value == null) {
        unsetErrorResponse();
      } else {
        setErrorResponse((java.lang.String)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case RESULT:
      return getResult();

    case STATUS:
      return getStatus();

    case ERROR_RESPONSE:
      return getErrorResponse();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case RESULT:
      return isSetResult();
    case STATUS:
      return isSetStatus();
    case ERROR_RESPONSE:
      return isSetErrorResponse();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof MetadataResponse)
      return this.equals((MetadataResponse)that);
    return false;
  }

  public boolean equals(MetadataResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_result = true && this.isSetResult();
    boolean that_present_result = true && that.isSetResult();
    if (this_present_result || that_present_result) {
      if (!(this_present_result && that_present_result))
        return false;
      if (!this.result.equals(that.result))
        return false;
    }

    boolean this_present_status = true;
    boolean that_present_status = true;
    if (this_present_status || that_present_status) {
      if (!(this_present_status && that_present_status))
        return false;
      if (this.status != that.status)
        return false;
    }

    boolean this_present_errorResponse = true && this.isSetErrorResponse();
    boolean that_present_errorResponse = true && that.isSetErrorResponse();
    if (this_present_errorResponse || that_present_errorResponse) {
      if (!(this_present_errorResponse && that_present_errorResponse))
        return false;
      if (!this.errorResponse.equals(that.errorResponse))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetResult()) ? 131071 : 524287);
    if (isSetResult())
      hashCode = hashCode * 8191 + result.hashCode();

    hashCode = hashCode * 8191 + (int) (status);

    hashCode = hashCode * 8191 + ((isSetErrorResponse()) ? 131071 : 524287);
    if (isSetErrorResponse())
      hashCode = hashCode * 8191 + errorResponse.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(MetadataResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetResult()).compareTo(other.isSetResult());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResult()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.result, other.result);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
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
    lastComparison = java.lang.Boolean.valueOf(isSetErrorResponse()).compareTo(other.isSetErrorResponse());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetErrorResponse()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.errorResponse, other.errorResponse);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("MetadataResponse(");
    boolean first = true;

    sb.append("result:");
    if (this.result == null) {
      sb.append("null");
    } else {
      sb.append(this.result);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("status:");
    sb.append(this.status);
    first = false;
    if (isSetErrorResponse()) {
      if (!first) sb.append(", ");
      sb.append("errorResponse:");
      if (this.errorResponse == null) {
        sb.append("null");
      } else {
        sb.append(this.errorResponse);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (result == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'result' was not present! Struct: " + toString());
    }
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

  private static class MetadataResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public MetadataResponseStandardScheme getScheme() {
      return new MetadataResponseStandardScheme();
    }
  }

  private static class MetadataResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<MetadataResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, MetadataResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // RESULT
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map110 = iprot.readMapBegin();
                struct.result = new java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>(2*_map110.size);
                java.lang.String _key111;
                java.util.List<java.lang.String> _val112;
                for (int _i113 = 0; _i113 < _map110.size; ++_i113)
                {
                  _key111 = iprot.readString();
                  {
                    org.apache.thrift.protocol.TList _list114 = iprot.readListBegin();
                    _val112 = new java.util.ArrayList<java.lang.String>(_list114.size);
                    java.lang.String _elem115;
                    for (int _i116 = 0; _i116 < _list114.size; ++_i116)
                    {
                      _elem115 = iprot.readString();
                      _val112.add(_elem115);
                    }
                    iprot.readListEnd();
                  }
                  struct.result.put(_key111, _val112);
                }
                iprot.readMapEnd();
              }
              struct.setResultIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.BYTE) {
              struct.status = iprot.readByte();
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ERROR_RESPONSE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.errorResponse = iprot.readString();
              struct.setErrorResponseIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, MetadataResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.result != null) {
        oprot.writeFieldBegin(RESULT_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.LIST, struct.result.size()));
          for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> _iter117 : struct.result.entrySet())
          {
            oprot.writeString(_iter117.getKey());
            {
              oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, _iter117.getValue().size()));
              for (java.lang.String _iter118 : _iter117.getValue())
              {
                oprot.writeString(_iter118);
              }
              oprot.writeListEnd();
            }
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(STATUS_FIELD_DESC);
      oprot.writeByte(struct.status);
      oprot.writeFieldEnd();
      if (struct.errorResponse != null) {
        if (struct.isSetErrorResponse()) {
          oprot.writeFieldBegin(ERROR_RESPONSE_FIELD_DESC);
          oprot.writeString(struct.errorResponse);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class MetadataResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public MetadataResponseTupleScheme getScheme() {
      return new MetadataResponseTupleScheme();
    }
  }

  private static class MetadataResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<MetadataResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, MetadataResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.result.size());
        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> _iter119 : struct.result.entrySet())
        {
          oprot.writeString(_iter119.getKey());
          {
            oprot.writeI32(_iter119.getValue().size());
            for (java.lang.String _iter120 : _iter119.getValue())
            {
              oprot.writeString(_iter120);
            }
          }
        }
      }
      oprot.writeByte(struct.status);
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetErrorResponse()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetErrorResponse()) {
        oprot.writeString(struct.errorResponse);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, MetadataResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TMap _map121 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.LIST, iprot.readI32());
        struct.result = new java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>(2*_map121.size);
        java.lang.String _key122;
        java.util.List<java.lang.String> _val123;
        for (int _i124 = 0; _i124 < _map121.size; ++_i124)
        {
          _key122 = iprot.readString();
          {
            org.apache.thrift.protocol.TList _list125 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
            _val123 = new java.util.ArrayList<java.lang.String>(_list125.size);
            java.lang.String _elem126;
            for (int _i127 = 0; _i127 < _list125.size; ++_i127)
            {
              _elem126 = iprot.readString();
              _val123.add(_elem126);
            }
          }
          struct.result.put(_key122, _val123);
        }
      }
      struct.setResultIsSet(true);
      struct.status = iprot.readByte();
      struct.setStatusIsSet(true);
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.errorResponse = iprot.readString();
        struct.setErrorResponseIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

