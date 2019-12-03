/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-12-15")
public class TwoPhasePreCommitResponse implements org.apache.thrift.TBase<TwoPhasePreCommitResponse, TwoPhasePreCommitResponse._Fields>, java.io.Serializable, Cloneable, Comparable<TwoPhasePreCommitResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TwoPhasePreCommitResponse");

  private static final org.apache.thrift.protocol.TField RESPONSE_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("responseType", org.apache.thrift.protocol.TType.STRING, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TwoPhasePreCommitResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TwoPhasePreCommitResponseTupleSchemeFactory();

  public java.lang.String responseType; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    RESPONSE_TYPE((short)1, "responseType");

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
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.RESPONSE_TYPE, new org.apache.thrift.meta_data.FieldMetaData("responseType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TwoPhasePreCommitResponse.class, metaDataMap);
  }

  public TwoPhasePreCommitResponse() {
  }

  public TwoPhasePreCommitResponse(
    java.lang.String responseType)
  {
    this();
    this.responseType = responseType;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TwoPhasePreCommitResponse(TwoPhasePreCommitResponse other) {
    if (other.isSetResponseType()) {
      this.responseType = other.responseType;
    }
  }

  public TwoPhasePreCommitResponse deepCopy() {
    return new TwoPhasePreCommitResponse(this);
  }

  @Override
  public void clear() {
    this.responseType = null;
  }

  public java.lang.String getResponseType() {
    return this.responseType;
  }

  public TwoPhasePreCommitResponse setResponseType(java.lang.String responseType) {
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

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case RESPONSE_TYPE:
      if (value == null) {
        unsetResponseType();
      } else {
        setResponseType((java.lang.String)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case RESPONSE_TYPE:
      return getResponseType();

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
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof TwoPhasePreCommitResponse)
      return this.equals((TwoPhasePreCommitResponse)that);
    return false;
  }

  public boolean equals(TwoPhasePreCommitResponse that) {
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

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetResponseType()) ? 131071 : 524287);
    if (isSetResponseType())
      hashCode = hashCode * 8191 + responseType.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(TwoPhasePreCommitResponse other) {
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TwoPhasePreCommitResponse(");
    boolean first = true;

    sb.append("responseType:");
    if (this.responseType == null) {
      sb.append("null");
    } else {
      sb.append(this.responseType);
    }
    first = false;
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TwoPhasePreCommitResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TwoPhasePreCommitResponseStandardScheme getScheme() {
      return new TwoPhasePreCommitResponseStandardScheme();
    }
  }

  private static class TwoPhasePreCommitResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<TwoPhasePreCommitResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TwoPhasePreCommitResponse struct) throws org.apache.thrift.TException {
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
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TwoPhasePreCommitResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.responseType != null) {
        oprot.writeFieldBegin(RESPONSE_TYPE_FIELD_DESC);
        oprot.writeString(struct.responseType);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TwoPhasePreCommitResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TwoPhasePreCommitResponseTupleScheme getScheme() {
      return new TwoPhasePreCommitResponseTupleScheme();
    }
  }

  private static class TwoPhasePreCommitResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<TwoPhasePreCommitResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TwoPhasePreCommitResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.responseType);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TwoPhasePreCommitResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.responseType = iprot.readString();
      struct.setResponseTypeIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

