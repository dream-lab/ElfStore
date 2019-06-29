/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-06-29")
public class DynamicTypeStreamMetadata implements org.apache.thrift.TBase<DynamicTypeStreamMetadata, DynamicTypeStreamMetadata._Fields>, java.io.Serializable, Cloneable, Comparable<DynamicTypeStreamMetadata> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DynamicTypeStreamMetadata");

  private static final org.apache.thrift.protocol.TField VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("value", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField CLAZZ_FIELD_DESC = new org.apache.thrift.protocol.TField("clazz", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField UPDATABLE_FIELD_DESC = new org.apache.thrift.protocol.TField("updatable", org.apache.thrift.protocol.TType.BOOL, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new DynamicTypeStreamMetadataStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new DynamicTypeStreamMetadataTupleSchemeFactory();

  public java.lang.String value; // required
  public java.lang.String clazz; // required
  public boolean updatable; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VALUE((short)1, "value"),
    CLAZZ((short)2, "clazz"),
    UPDATABLE((short)3, "updatable");

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
        case 2: // CLAZZ
          return CLAZZ;
        case 3: // UPDATABLE
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
  private static final int __UPDATABLE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VALUE, new org.apache.thrift.meta_data.FieldMetaData("value", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CLAZZ, new org.apache.thrift.meta_data.FieldMetaData("clazz", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.UPDATABLE, new org.apache.thrift.meta_data.FieldMetaData("updatable", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DynamicTypeStreamMetadata.class, metaDataMap);
  }

  public DynamicTypeStreamMetadata() {
  }

  public DynamicTypeStreamMetadata(
    java.lang.String value,
    java.lang.String clazz,
    boolean updatable)
  {
    this();
    this.value = value;
    this.clazz = clazz;
    this.updatable = updatable;
    setUpdatableIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DynamicTypeStreamMetadata(DynamicTypeStreamMetadata other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetValue()) {
      this.value = other.value;
    }
    if (other.isSetClazz()) {
      this.clazz = other.clazz;
    }
    this.updatable = other.updatable;
  }

  public DynamicTypeStreamMetadata deepCopy() {
    return new DynamicTypeStreamMetadata(this);
  }

  @Override
  public void clear() {
    this.value = null;
    this.clazz = null;
    setUpdatableIsSet(false);
    this.updatable = false;
  }

  public java.lang.String getValue() {
    return this.value;
  }

  public DynamicTypeStreamMetadata setValue(java.lang.String value) {
    this.value = value;
    return this;
  }

  public void unsetValue() {
    this.value = null;
  }

  /** Returns true if field value is set (has been assigned a value) and false otherwise */
  public boolean isSetValue() {
    return this.value != null;
  }

  public void setValueIsSet(boolean value) {
    if (!value) {
      this.value = null;
    }
  }

  public java.lang.String getClazz() {
    return this.clazz;
  }

  public DynamicTypeStreamMetadata setClazz(java.lang.String clazz) {
    this.clazz = clazz;
    return this;
  }

  public void unsetClazz() {
    this.clazz = null;
  }

  /** Returns true if field clazz is set (has been assigned a value) and false otherwise */
  public boolean isSetClazz() {
    return this.clazz != null;
  }

  public void setClazzIsSet(boolean value) {
    if (!value) {
      this.clazz = null;
    }
  }

  public boolean isUpdatable() {
    return this.updatable;
  }

  public DynamicTypeStreamMetadata setUpdatable(boolean updatable) {
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
        setValue((java.lang.String)value);
      }
      break;

    case CLAZZ:
      if (value == null) {
        unsetClazz();
      } else {
        setClazz((java.lang.String)value);
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

    case CLAZZ:
      return getClazz();

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
    case CLAZZ:
      return isSetClazz();
    case UPDATABLE:
      return isSetUpdatable();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof DynamicTypeStreamMetadata)
      return this.equals((DynamicTypeStreamMetadata)that);
    return false;
  }

  public boolean equals(DynamicTypeStreamMetadata that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_value = true && this.isSetValue();
    boolean that_present_value = true && that.isSetValue();
    if (this_present_value || that_present_value) {
      if (!(this_present_value && that_present_value))
        return false;
      if (!this.value.equals(that.value))
        return false;
    }

    boolean this_present_clazz = true && this.isSetClazz();
    boolean that_present_clazz = true && that.isSetClazz();
    if (this_present_clazz || that_present_clazz) {
      if (!(this_present_clazz && that_present_clazz))
        return false;
      if (!this.clazz.equals(that.clazz))
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

    hashCode = hashCode * 8191 + ((isSetValue()) ? 131071 : 524287);
    if (isSetValue())
      hashCode = hashCode * 8191 + value.hashCode();

    hashCode = hashCode * 8191 + ((isSetClazz()) ? 131071 : 524287);
    if (isSetClazz())
      hashCode = hashCode * 8191 + clazz.hashCode();

    hashCode = hashCode * 8191 + ((updatable) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(DynamicTypeStreamMetadata other) {
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
    lastComparison = java.lang.Boolean.valueOf(isSetClazz()).compareTo(other.isSetClazz());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetClazz()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.clazz, other.clazz);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("DynamicTypeStreamMetadata(");
    boolean first = true;

    sb.append("value:");
    if (this.value == null) {
      sb.append("null");
    } else {
      sb.append(this.value);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("clazz:");
    if (this.clazz == null) {
      sb.append("null");
    } else {
      sb.append(this.clazz);
    }
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
    if (value == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'value' was not present! Struct: " + toString());
    }
    if (clazz == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'clazz' was not present! Struct: " + toString());
    }
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

  private static class DynamicTypeStreamMetadataStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DynamicTypeStreamMetadataStandardScheme getScheme() {
      return new DynamicTypeStreamMetadataStandardScheme();
    }
  }

  private static class DynamicTypeStreamMetadataStandardScheme extends org.apache.thrift.scheme.StandardScheme<DynamicTypeStreamMetadata> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DynamicTypeStreamMetadata struct) throws org.apache.thrift.TException {
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
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.value = iprot.readString();
              struct.setValueIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CLAZZ
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.clazz = iprot.readString();
              struct.setClazzIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // UPDATABLE
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
      if (!struct.isSetUpdatable()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'updatable' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, DynamicTypeStreamMetadata struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.value != null) {
        oprot.writeFieldBegin(VALUE_FIELD_DESC);
        oprot.writeString(struct.value);
        oprot.writeFieldEnd();
      }
      if (struct.clazz != null) {
        oprot.writeFieldBegin(CLAZZ_FIELD_DESC);
        oprot.writeString(struct.clazz);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(UPDATABLE_FIELD_DESC);
      oprot.writeBool(struct.updatable);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DynamicTypeStreamMetadataTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DynamicTypeStreamMetadataTupleScheme getScheme() {
      return new DynamicTypeStreamMetadataTupleScheme();
    }
  }

  private static class DynamicTypeStreamMetadataTupleScheme extends org.apache.thrift.scheme.TupleScheme<DynamicTypeStreamMetadata> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DynamicTypeStreamMetadata struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.value);
      oprot.writeString(struct.clazz);
      oprot.writeBool(struct.updatable);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DynamicTypeStreamMetadata struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.value = iprot.readString();
      struct.setValueIsSet(true);
      struct.clazz = iprot.readString();
      struct.setClazzIsSet(true);
      struct.updatable = iprot.readBool();
      struct.setUpdatableIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

