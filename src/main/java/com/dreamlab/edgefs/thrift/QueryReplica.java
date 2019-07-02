/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.dreamlab.edgefs.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2019-07-02")
public class QueryReplica implements org.apache.thrift.TBase<QueryReplica, QueryReplica._Fields>, java.io.Serializable, Cloneable, Comparable<QueryReplica> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("QueryReplica");

  private static final org.apache.thrift.protocol.TField MATCHING_NODES_FIELD_DESC = new org.apache.thrift.protocol.TField("matchingNodes", org.apache.thrift.protocol.TType.MAP, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new QueryReplicaStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new QueryReplicaTupleSchemeFactory();

  public java.util.Map<java.lang.Long,java.util.List<NodeInfoData>> matchingNodes; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    MATCHING_NODES((short)1, "matchingNodes");

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
        case 1: // MATCHING_NODES
          return MATCHING_NODES;
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
    tmpMap.put(_Fields.MATCHING_NODES, new org.apache.thrift.meta_data.FieldMetaData("matchingNodes", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64), 
            new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
                new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, NodeInfoData.class)))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(QueryReplica.class, metaDataMap);
  }

  public QueryReplica() {
  }

  public QueryReplica(
    java.util.Map<java.lang.Long,java.util.List<NodeInfoData>> matchingNodes)
  {
    this();
    this.matchingNodes = matchingNodes;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public QueryReplica(QueryReplica other) {
    if (other.isSetMatchingNodes()) {
      java.util.Map<java.lang.Long,java.util.List<NodeInfoData>> __this__matchingNodes = new java.util.HashMap<java.lang.Long,java.util.List<NodeInfoData>>(other.matchingNodes.size());
      for (java.util.Map.Entry<java.lang.Long, java.util.List<NodeInfoData>> other_element : other.matchingNodes.entrySet()) {

        java.lang.Long other_element_key = other_element.getKey();
        java.util.List<NodeInfoData> other_element_value = other_element.getValue();

        java.lang.Long __this__matchingNodes_copy_key = other_element_key;

        java.util.List<NodeInfoData> __this__matchingNodes_copy_value = new java.util.ArrayList<NodeInfoData>(other_element_value.size());
        for (NodeInfoData other_element_value_element : other_element_value) {
          __this__matchingNodes_copy_value.add(new NodeInfoData(other_element_value_element));
        }

        __this__matchingNodes.put(__this__matchingNodes_copy_key, __this__matchingNodes_copy_value);
      }
      this.matchingNodes = __this__matchingNodes;
    }
  }

  public QueryReplica deepCopy() {
    return new QueryReplica(this);
  }

  @Override
  public void clear() {
    this.matchingNodes = null;
  }

  public int getMatchingNodesSize() {
    return (this.matchingNodes == null) ? 0 : this.matchingNodes.size();
  }

  public void putToMatchingNodes(long key, java.util.List<NodeInfoData> val) {
    if (this.matchingNodes == null) {
      this.matchingNodes = new java.util.HashMap<java.lang.Long,java.util.List<NodeInfoData>>();
    }
    this.matchingNodes.put(key, val);
  }

  public java.util.Map<java.lang.Long,java.util.List<NodeInfoData>> getMatchingNodes() {
    return this.matchingNodes;
  }

  public QueryReplica setMatchingNodes(java.util.Map<java.lang.Long,java.util.List<NodeInfoData>> matchingNodes) {
    this.matchingNodes = matchingNodes;
    return this;
  }

  public void unsetMatchingNodes() {
    this.matchingNodes = null;
  }

  /** Returns true if field matchingNodes is set (has been assigned a value) and false otherwise */
  public boolean isSetMatchingNodes() {
    return this.matchingNodes != null;
  }

  public void setMatchingNodesIsSet(boolean value) {
    if (!value) {
      this.matchingNodes = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case MATCHING_NODES:
      if (value == null) {
        unsetMatchingNodes();
      } else {
        setMatchingNodes((java.util.Map<java.lang.Long,java.util.List<NodeInfoData>>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case MATCHING_NODES:
      return getMatchingNodes();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case MATCHING_NODES:
      return isSetMatchingNodes();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof QueryReplica)
      return this.equals((QueryReplica)that);
    return false;
  }

  public boolean equals(QueryReplica that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_matchingNodes = true && this.isSetMatchingNodes();
    boolean that_present_matchingNodes = true && that.isSetMatchingNodes();
    if (this_present_matchingNodes || that_present_matchingNodes) {
      if (!(this_present_matchingNodes && that_present_matchingNodes))
        return false;
      if (!this.matchingNodes.equals(that.matchingNodes))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetMatchingNodes()) ? 131071 : 524287);
    if (isSetMatchingNodes())
      hashCode = hashCode * 8191 + matchingNodes.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(QueryReplica other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetMatchingNodes()).compareTo(other.isSetMatchingNodes());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMatchingNodes()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.matchingNodes, other.matchingNodes);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("QueryReplica(");
    boolean first = true;

    sb.append("matchingNodes:");
    if (this.matchingNodes == null) {
      sb.append("null");
    } else {
      sb.append(this.matchingNodes);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (matchingNodes == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'matchingNodes' was not present! Struct: " + toString());
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

  private static class QueryReplicaStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public QueryReplicaStandardScheme getScheme() {
      return new QueryReplicaStandardScheme();
    }
  }

  private static class QueryReplicaStandardScheme extends org.apache.thrift.scheme.StandardScheme<QueryReplica> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, QueryReplica struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // MATCHING_NODES
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map54 = iprot.readMapBegin();
                struct.matchingNodes = new java.util.HashMap<java.lang.Long,java.util.List<NodeInfoData>>(2*_map54.size);
                long _key55;
                java.util.List<NodeInfoData> _val56;
                for (int _i57 = 0; _i57 < _map54.size; ++_i57)
                {
                  _key55 = iprot.readI64();
                  {
                    org.apache.thrift.protocol.TList _list58 = iprot.readListBegin();
                    _val56 = new java.util.ArrayList<NodeInfoData>(_list58.size);
                    NodeInfoData _elem59;
                    for (int _i60 = 0; _i60 < _list58.size; ++_i60)
                    {
                      _elem59 = new NodeInfoData();
                      _elem59.read(iprot);
                      _val56.add(_elem59);
                    }
                    iprot.readListEnd();
                  }
                  struct.matchingNodes.put(_key55, _val56);
                }
                iprot.readMapEnd();
              }
              struct.setMatchingNodesIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, QueryReplica struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.matchingNodes != null) {
        oprot.writeFieldBegin(MATCHING_NODES_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I64, org.apache.thrift.protocol.TType.LIST, struct.matchingNodes.size()));
          for (java.util.Map.Entry<java.lang.Long, java.util.List<NodeInfoData>> _iter61 : struct.matchingNodes.entrySet())
          {
            oprot.writeI64(_iter61.getKey());
            {
              oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, _iter61.getValue().size()));
              for (NodeInfoData _iter62 : _iter61.getValue())
              {
                _iter62.write(oprot);
              }
              oprot.writeListEnd();
            }
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class QueryReplicaTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public QueryReplicaTupleScheme getScheme() {
      return new QueryReplicaTupleScheme();
    }
  }

  private static class QueryReplicaTupleScheme extends org.apache.thrift.scheme.TupleScheme<QueryReplica> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, QueryReplica struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.matchingNodes.size());
        for (java.util.Map.Entry<java.lang.Long, java.util.List<NodeInfoData>> _iter63 : struct.matchingNodes.entrySet())
        {
          oprot.writeI64(_iter63.getKey());
          {
            oprot.writeI32(_iter63.getValue().size());
            for (NodeInfoData _iter64 : _iter63.getValue())
            {
              _iter64.write(oprot);
            }
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, QueryReplica struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TMap _map65 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.I64, org.apache.thrift.protocol.TType.LIST, iprot.readI32());
        struct.matchingNodes = new java.util.HashMap<java.lang.Long,java.util.List<NodeInfoData>>(2*_map65.size);
        long _key66;
        java.util.List<NodeInfoData> _val67;
        for (int _i68 = 0; _i68 < _map65.size; ++_i68)
        {
          _key66 = iprot.readI64();
          {
            org.apache.thrift.protocol.TList _list69 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
            _val67 = new java.util.ArrayList<NodeInfoData>(_list69.size);
            NodeInfoData _elem70;
            for (int _i71 = 0; _i71 < _list69.size; ++_i71)
            {
              _elem70 = new NodeInfoData();
              _elem70.read(iprot);
              _val67.add(_elem70);
            }
          }
          struct.matchingNodes.put(_key66, _val67);
        }
      }
      struct.setMatchingNodesIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

