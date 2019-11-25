/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-11-25")
public class FindReplica implements org.apache.thrift.TBase<FindReplica, FindReplica._Fields>, java.io.Serializable, Cloneable, Comparable<FindReplica> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("FindReplica");

  private static final org.apache.thrift.protocol.TField NODE_FIELD_DESC = new org.apache.thrift.protocol.TField("node", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField EDGE_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("edgeInfo", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new FindReplicaStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new FindReplicaTupleSchemeFactory();

  public NodeInfoData node; // optional
  public EdgeInfoData edgeInfo; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NODE((short)1, "node"),
    EDGE_INFO((short)2, "edgeInfo");

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
        case 1: // NODE
          return NODE;
        case 2: // EDGE_INFO
          return EDGE_INFO;
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
  private static final _Fields optionals[] = {_Fields.NODE,_Fields.EDGE_INFO};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NODE, new org.apache.thrift.meta_data.FieldMetaData("node", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, NodeInfoData.class)));
    tmpMap.put(_Fields.EDGE_INFO, new org.apache.thrift.meta_data.FieldMetaData("edgeInfo", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, EdgeInfoData.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(FindReplica.class, metaDataMap);
  }

  public FindReplica() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public FindReplica(FindReplica other) {
    if (other.isSetNode()) {
      this.node = new NodeInfoData(other.node);
    }
    if (other.isSetEdgeInfo()) {
      this.edgeInfo = new EdgeInfoData(other.edgeInfo);
    }
  }

  public FindReplica deepCopy() {
    return new FindReplica(this);
  }

  @Override
  public void clear() {
    this.node = null;
    this.edgeInfo = null;
  }

  public NodeInfoData getNode() {
    return this.node;
  }

  public FindReplica setNode(NodeInfoData node) {
    this.node = node;
    return this;
  }

  public void unsetNode() {
    this.node = null;
  }

  /** Returns true if field node is set (has been assigned a value) and false otherwise */
  public boolean isSetNode() {
    return this.node != null;
  }

  public void setNodeIsSet(boolean value) {
    if (!value) {
      this.node = null;
    }
  }

  public EdgeInfoData getEdgeInfo() {
    return this.edgeInfo;
  }

  public FindReplica setEdgeInfo(EdgeInfoData edgeInfo) {
    this.edgeInfo = edgeInfo;
    return this;
  }

  public void unsetEdgeInfo() {
    this.edgeInfo = null;
  }

  /** Returns true if field edgeInfo is set (has been assigned a value) and false otherwise */
  public boolean isSetEdgeInfo() {
    return this.edgeInfo != null;
  }

  public void setEdgeInfoIsSet(boolean value) {
    if (!value) {
      this.edgeInfo = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case NODE:
      if (value == null) {
        unsetNode();
      } else {
        setNode((NodeInfoData)value);
      }
      break;

    case EDGE_INFO:
      if (value == null) {
        unsetEdgeInfo();
      } else {
        setEdgeInfo((EdgeInfoData)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NODE:
      return getNode();

    case EDGE_INFO:
      return getEdgeInfo();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NODE:
      return isSetNode();
    case EDGE_INFO:
      return isSetEdgeInfo();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof FindReplica)
      return this.equals((FindReplica)that);
    return false;
  }

  public boolean equals(FindReplica that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_node = true && this.isSetNode();
    boolean that_present_node = true && that.isSetNode();
    if (this_present_node || that_present_node) {
      if (!(this_present_node && that_present_node))
        return false;
      if (!this.node.equals(that.node))
        return false;
    }

    boolean this_present_edgeInfo = true && this.isSetEdgeInfo();
    boolean that_present_edgeInfo = true && that.isSetEdgeInfo();
    if (this_present_edgeInfo || that_present_edgeInfo) {
      if (!(this_present_edgeInfo && that_present_edgeInfo))
        return false;
      if (!this.edgeInfo.equals(that.edgeInfo))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetNode()) ? 131071 : 524287);
    if (isSetNode())
      hashCode = hashCode * 8191 + node.hashCode();

    hashCode = hashCode * 8191 + ((isSetEdgeInfo()) ? 131071 : 524287);
    if (isSetEdgeInfo())
      hashCode = hashCode * 8191 + edgeInfo.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(FindReplica other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetNode()).compareTo(other.isSetNode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNode()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.node, other.node);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetEdgeInfo()).compareTo(other.isSetEdgeInfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEdgeInfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.edgeInfo, other.edgeInfo);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("FindReplica(");
    boolean first = true;

    if (isSetNode()) {
      sb.append("node:");
      if (this.node == null) {
        sb.append("null");
      } else {
        sb.append(this.node);
      }
      first = false;
    }
    if (isSetEdgeInfo()) {
      if (!first) sb.append(", ");
      sb.append("edgeInfo:");
      if (this.edgeInfo == null) {
        sb.append("null");
      } else {
        sb.append(this.edgeInfo);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (node != null) {
      node.validate();
    }
    if (edgeInfo != null) {
      edgeInfo.validate();
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

  private static class FindReplicaStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public FindReplicaStandardScheme getScheme() {
      return new FindReplicaStandardScheme();
    }
  }

  private static class FindReplicaStandardScheme extends org.apache.thrift.scheme.StandardScheme<FindReplica> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, FindReplica struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NODE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.node = new NodeInfoData();
              struct.node.read(iprot);
              struct.setNodeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // EDGE_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.edgeInfo = new EdgeInfoData();
              struct.edgeInfo.read(iprot);
              struct.setEdgeInfoIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, FindReplica struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.node != null) {
        if (struct.isSetNode()) {
          oprot.writeFieldBegin(NODE_FIELD_DESC);
          struct.node.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.edgeInfo != null) {
        if (struct.isSetEdgeInfo()) {
          oprot.writeFieldBegin(EDGE_INFO_FIELD_DESC);
          struct.edgeInfo.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class FindReplicaTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public FindReplicaTupleScheme getScheme() {
      return new FindReplicaTupleScheme();
    }
  }

  private static class FindReplicaTupleScheme extends org.apache.thrift.scheme.TupleScheme<FindReplica> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, FindReplica struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetNode()) {
        optionals.set(0);
      }
      if (struct.isSetEdgeInfo()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetNode()) {
        struct.node.write(oprot);
      }
      if (struct.isSetEdgeInfo()) {
        struct.edgeInfo.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, FindReplica struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.node = new NodeInfoData();
        struct.node.read(iprot);
        struct.setNodeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.edgeInfo = new EdgeInfoData();
        struct.edgeInfo.read(iprot);
        struct.setEdgeInfoIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

