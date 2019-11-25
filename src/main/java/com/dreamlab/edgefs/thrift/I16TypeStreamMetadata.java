/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-11-25")
public class I16TypeStreamMetadata implements org.apache.thrift.TBase<I16TypeStreamMetadata, I16TypeStreamMetadata._Fields>, java.io.Serializable, Cloneable, Comparable<I16TypeStreamMetadata> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("I16TypeStreamMetadata");

  private static final org.apache.thrift.protocol.TField VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("value", org.apache.thrift.protocol.TType.I16, (short)1);
  private static final org.apache.thrift.protocol.TField UPDATABLE_FIELD_DESC = new org.apache.thrift.protocol.TField("updatable", org.apache.thrift.protocol.TType.BOOL, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new I16TypeStreamMetadataStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new I16TypeStreamMetadataTupleSchemeFactory();

  public short value; // required
  public boolean updatable; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VALUE((short)1, "value"),
    UPDATABLE((short)2, "updatable");

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
        case 1: // VALUE
          return VALUE;
        case 2: // UPDATABLE
          return UPDATABLE;
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
  private static final int __VALUE_ISSET_ID = 0;
  private static final int __UPDATABLE_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VALUE, new org.apache.thrift.meta_data.FieldMetaData("value", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    tmpMap.put(_Fields.UPDATABLE, new org.apache.thrift.meta_data.FieldMetaData("updatable", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(I16TypeStreamMetadata.class, metaDataMap);
  }

  public I16TypeStreamMetadata() {
  }

  public I16TypeStreamMetadata(
    short value,
    boolean updatable)
  {
    this();
    this.value = value;
    setValueIsSet(true);
    this.updatable = updatable;
    setUpdatableIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public I16TypeStreamMetadata(I16TypeStreamMetadata other) {
    __isset_bitfield = other.__isset_bitfield;
    this.value = other.value;
    this.updatable = other.updatable;
  }

  public I16TypeStreamMetadata deepCopy() {
    return new I16TypeStreamMetadata(this);
  }

  @Override
  public void clear() {
    setValueIsSet(false);
    this.value = 0;
    setUpdatableIsSet(false);
    this.updatable = false;
  }

  public short getValue() {
    return this.value;
  }

  public I16TypeStreamMetadata setValue(short value) {
    this.value = value;
    setValueIsSet(true);
    return this;
  }

  public void unsetValue() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __VALUE_ISSET_ID);
  }

  /** Returns true if field value is set (has been assigned a value) and false otherwise */
  public boolean isSetValue() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __VALUE_ISSET_ID);
  }

  public void setValueIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __VALUE_ISSET_ID, value);
  }

  public boolean isUpdatable() {
    return this.updatable;
  }

  public I16TypeStreamMetadata setUpdatable(boolean updatable) {
    this.updatable = updatable;
    setUpdatableIsSet(true);
    return this;
  }

  public void unsetUpdatable() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __UPDATABLE_ISSET_ID);
  }

  /** Returns true if field updatable is set (has been assigned a value) and false otherwise */
  public boolean isSetUpdatable() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __UPDATABLE_ISSET_ID);
  }

  public void setUpdatableIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __UPDATABLE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case VALUE:
      if (value == null) {
        unsetValue();
      } else {
        setValue((java.lang.Short)value);
      }
      break;

    case UPDATABLE:
      if (value == null) {
        unsetUpdatable();
      } else {
        setUpdatable((java.lang.Boolean)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case VALUE:
      return getValue();

    case UPDATABLE:
      return isUpdatable();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case VALUE:
      return isSetValue();
    case UPDATABLE:
      return isSetUpdatable();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof I16TypeStreamMetadata)
      return this.equals((I16TypeStreamMetadata)that);
    return false;
  }

  public boolean equals(I16TypeStreamMetadata that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_value = true;
    boolean that_present_value = true;
    if (this_present_value || that_present_value) {
      if (!(this_present_value && that_present_value))
        return false;
      if (this.value != that.value)
        return false;
    }

    boolean this_present_updatable = true;
    boolean that_present_updatable = true;
    if (this_present_updatable || that_present_updatable) {
      if (!(this_present_updatable && that_present_updatable))
        return false;
      if (this.updatable != that.updatable)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + value;

    hashCode = hashCode * 8191 + ((updatable) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(I16TypeStreamMetadata other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetValue()).compareTo(other.isSetValue());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetValue()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.value, other.value);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetUpdatable()).compareTo(other.isSetUpdatable());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUpdatable()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.updatable, other.updatable);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("I16TypeStreamMetadata(");
    boolean first = true;

    sb.append("value:");
    sb.append(this.value);
    first = false;
    if (!first) sb.append(", ");
    sb.append("updatable:");
    sb.append(this.updatable);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'value' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'updatable' because it's a primitive and you chose the non-beans generator.
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

  private static class I16TypeStreamMetadataStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public I16TypeStreamMetadataStandardScheme getScheme() {
      return new I16TypeStreamMetadataStandardScheme();
    }
  }

  private static class I16TypeStreamMetadataStandardScheme extends org.apache.thrift.scheme.StandardScheme<I16TypeStreamMetadata> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, I16TypeStreamMetadata struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VALUE
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.value = iprot.readI16();
              struct.setValueIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // UPDATABLE
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.updatable = iprot.readBool();
              struct.setUpdatableIsSet(true);
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
      if (!struct.isSetValue()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'value' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetUpdatable()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'updatable' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, I16TypeStreamMetadata struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(VALUE_FIELD_DESC);
      oprot.writeI16(struct.value);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(UPDATABLE_FIELD_DESC);
      oprot.writeBool(struct.updatable);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class I16TypeStreamMetadataTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public I16TypeStreamMetadataTupleScheme getScheme() {
      return new I16TypeStreamMetadataTupleScheme();
    }
  }

  private static class I16TypeStreamMetadataTupleScheme extends org.apache.thrift.scheme.TupleScheme<I16TypeStreamMetadata> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, I16TypeStreamMetadata struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeI16(struct.value);
      oprot.writeBool(struct.updatable);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, I16TypeStreamMetadata struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.value = iprot.readI16();
      struct.setValueIsSet(true);
      struct.updatable = iprot.readBool();
      struct.setUpdatableIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

