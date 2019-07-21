/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-07-21")
public class TwoPhaseCommitRequest implements org.apache.thrift.TBase<TwoPhaseCommitRequest, TwoPhaseCommitRequest._Fields>, java.io.Serializable, Cloneable, Comparable<TwoPhaseCommitRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TwoPhaseCommitRequest");

  private static final org.apache.thrift.protocol.TField REQUEST_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("requestType", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField NODE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("nodeId", org.apache.thrift.protocol.TType.I16, (short)2);
  private static final org.apache.thrift.protocol.TField COORDINATOR_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("coordinatorId", org.apache.thrift.protocol.TType.I16, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TwoPhaseCommitRequestStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TwoPhaseCommitRequestTupleSchemeFactory();

  public java.lang.String requestType; // required
  public short nodeId; // required
  public short coordinatorId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    REQUEST_TYPE((short)1, "requestType"),
    NODE_ID((short)2, "nodeId"),
    COORDINATOR_ID((short)3, "coordinatorId");

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
        case 1: // REQUEST_TYPE
          return REQUEST_TYPE;
        case 2: // NODE_ID
          return NODE_ID;
        case 3: // COORDINATOR_ID
          return COORDINATOR_ID;
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
  private static final int __NODEID_ISSET_ID = 0;
  private static final int __COORDINATORID_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.REQUEST_TYPE, new org.apache.thrift.meta_data.FieldMetaData("requestType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.NODE_ID, new org.apache.thrift.meta_data.FieldMetaData("nodeId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    tmpMap.put(_Fields.COORDINATOR_ID, new org.apache.thrift.meta_data.FieldMetaData("coordinatorId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TwoPhaseCommitRequest.class, metaDataMap);
  }

  public TwoPhaseCommitRequest() {
  }

  public TwoPhaseCommitRequest(
    java.lang.String requestType,
    short nodeId,
    short coordinatorId)
  {
    this();
    this.requestType = requestType;
    this.nodeId = nodeId;
    setNodeIdIsSet(true);
    this.coordinatorId = coordinatorId;
    setCoordinatorIdIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TwoPhaseCommitRequest(TwoPhaseCommitRequest other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetRequestType()) {
      this.requestType = other.requestType;
    }
    this.nodeId = other.nodeId;
    this.coordinatorId = other.coordinatorId;
  }

  public TwoPhaseCommitRequest deepCopy() {
    return new TwoPhaseCommitRequest(this);
  }

  @Override
  public void clear() {
    this.requestType = null;
    setNodeIdIsSet(false);
    this.nodeId = 0;
    setCoordinatorIdIsSet(false);
    this.coordinatorId = 0;
  }

  public java.lang.String getRequestType() {
    return this.requestType;
  }

  public TwoPhaseCommitRequest setRequestType(java.lang.String requestType) {
    this.requestType = requestType;
    return this;
  }

  public void unsetRequestType() {
    this.requestType = null;
  }

  /** Returns true if field requestType is set (has been assigned a value) and false otherwise */
  public boolean isSetRequestType() {
    return this.requestType != null;
  }

  public void setRequestTypeIsSet(boolean value) {
    if (!value) {
      this.requestType = null;
    }
  }

  public short getNodeId() {
    return this.nodeId;
  }

  public TwoPhaseCommitRequest setNodeId(short nodeId) {
    this.nodeId = nodeId;
    setNodeIdIsSet(true);
    return this;
  }

  public void unsetNodeId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __NODEID_ISSET_ID);
  }

  /** Returns true if field nodeId is set (has been assigned a value) and false otherwise */
  public boolean isSetNodeId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __NODEID_ISSET_ID);
  }

  public void setNodeIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __NODEID_ISSET_ID, value);
  }

  public short getCoordinatorId() {
    return this.coordinatorId;
  }

  public TwoPhaseCommitRequest setCoordinatorId(short coordinatorId) {
    this.coordinatorId = coordinatorId;
    setCoordinatorIdIsSet(true);
    return this;
  }

  public void unsetCoordinatorId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __COORDINATORID_ISSET_ID);
  }

  /** Returns true if field coordinatorId is set (has been assigned a value) and false otherwise */
  public boolean isSetCoordinatorId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __COORDINATORID_ISSET_ID);
  }

  public void setCoordinatorIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __COORDINATORID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case REQUEST_TYPE:
      if (value == null) {
        unsetRequestType();
      } else {
        setRequestType((java.lang.String)value);
      }
      break;

    case NODE_ID:
      if (value == null) {
        unsetNodeId();
      } else {
        setNodeId((java.lang.Short)value);
      }
      break;

    case COORDINATOR_ID:
      if (value == null) {
        unsetCoordinatorId();
      } else {
        setCoordinatorId((java.lang.Short)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case REQUEST_TYPE:
      return getRequestType();

    case NODE_ID:
      return getNodeId();

    case COORDINATOR_ID:
      return getCoordinatorId();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case REQUEST_TYPE:
      return isSetRequestType();
    case NODE_ID:
      return isSetNodeId();
    case COORDINATOR_ID:
      return isSetCoordinatorId();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof TwoPhaseCommitRequest)
      return this.equals((TwoPhaseCommitRequest)that);
    return false;
  }

  public boolean equals(TwoPhaseCommitRequest that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_requestType = true && this.isSetRequestType();
    boolean that_present_requestType = true && that.isSetRequestType();
    if (this_present_requestType || that_present_requestType) {
      if (!(this_present_requestType && that_present_requestType))
        return false;
      if (!this.requestType.equals(that.requestType))
        return false;
    }

    boolean this_present_nodeId = true;
    boolean that_present_nodeId = true;
    if (this_present_nodeId || that_present_nodeId) {
      if (!(this_present_nodeId && that_present_nodeId))
        return false;
      if (this.nodeId != that.nodeId)
        return false;
    }

    boolean this_present_coordinatorId = true;
    boolean that_present_coordinatorId = true;
    if (this_present_coordinatorId || that_present_coordinatorId) {
      if (!(this_present_coordinatorId && that_present_coordinatorId))
        return false;
      if (this.coordinatorId != that.coordinatorId)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetRequestType()) ? 131071 : 524287);
    if (isSetRequestType())
      hashCode = hashCode * 8191 + requestType.hashCode();

    hashCode = hashCode * 8191 + nodeId;

    hashCode = hashCode * 8191 + coordinatorId;

    return hashCode;
  }

  @Override
  public int compareTo(TwoPhaseCommitRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetRequestType()).compareTo(other.isSetRequestType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRequestType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.requestType, other.requestType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetNodeId()).compareTo(other.isSetNodeId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNodeId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.nodeId, other.nodeId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetCoordinatorId()).compareTo(other.isSetCoordinatorId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCoordinatorId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.coordinatorId, other.coordinatorId);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TwoPhaseCommitRequest(");
    boolean first = true;

    sb.append("requestType:");
    if (this.requestType == null) {
      sb.append("null");
    } else {
      sb.append(this.requestType);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("nodeId:");
    sb.append(this.nodeId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("coordinatorId:");
    sb.append(this.coordinatorId);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (requestType == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'requestType' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'nodeId' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'coordinatorId' because it's a primitive and you chose the non-beans generator.
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

  private static class TwoPhaseCommitRequestStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TwoPhaseCommitRequestStandardScheme getScheme() {
      return new TwoPhaseCommitRequestStandardScheme();
    }
  }

  private static class TwoPhaseCommitRequestStandardScheme extends org.apache.thrift.scheme.StandardScheme<TwoPhaseCommitRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TwoPhaseCommitRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // REQUEST_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.requestType = iprot.readString();
              struct.setRequestTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // NODE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.nodeId = iprot.readI16();
              struct.setNodeIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // COORDINATOR_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.coordinatorId = iprot.readI16();
              struct.setCoordinatorIdIsSet(true);
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
      if (!struct.isSetNodeId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'nodeId' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetCoordinatorId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'coordinatorId' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TwoPhaseCommitRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.requestType != null) {
        oprot.writeFieldBegin(REQUEST_TYPE_FIELD_DESC);
        oprot.writeString(struct.requestType);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(NODE_ID_FIELD_DESC);
      oprot.writeI16(struct.nodeId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(COORDINATOR_ID_FIELD_DESC);
      oprot.writeI16(struct.coordinatorId);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TwoPhaseCommitRequestTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TwoPhaseCommitRequestTupleScheme getScheme() {
      return new TwoPhaseCommitRequestTupleScheme();
    }
  }

  private static class TwoPhaseCommitRequestTupleScheme extends org.apache.thrift.scheme.TupleScheme<TwoPhaseCommitRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TwoPhaseCommitRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.requestType);
      oprot.writeI16(struct.nodeId);
      oprot.writeI16(struct.coordinatorId);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TwoPhaseCommitRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.requestType = iprot.readString();
      struct.setRequestTypeIsSet(true);
      struct.nodeId = iprot.readI16();
      struct.setNodeIdIsSet(true);
      struct.coordinatorId = iprot.readI16();
      struct.setCoordinatorIdIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

