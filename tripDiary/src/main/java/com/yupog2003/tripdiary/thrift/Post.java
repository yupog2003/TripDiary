package com.yupog2003.tripdiary.thrift;

import org.apache.thrift.EncodingUtils;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;
import org.apache.thrift.scheme.TupleScheme;

import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class Post implements org.apache.thrift.TBase<Post, Post._Fields>, java.io.Serializable, Cloneable, Comparable<Post> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Post");

  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField TRIP_FIELD_DESC = new org.apache.thrift.protocol.TField("trip", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField POSTER_FIELD_DESC = new org.apache.thrift.protocol.TField("poster", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField CONTENT_FIELD_DESC = new org.apache.thrift.protocol.TField("content", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("time", org.apache.thrift.protocol.TType.STRING, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PostStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PostTupleSchemeFactory());
  }

  public int id; // required
  public String trip; // required
  public String poster; // required
  public String content; // required
  public String time; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ID((short)1, "id"),
    TRIP((short)2, "trip"),
    POSTER((short)3, "poster"),
    CONTENT((short)4, "content"),
    TIME((short)5, "time");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ID
          return ID;
        case 2: // TRIP
          return TRIP;
        case 3: // POSTER
          return POSTER;
        case 4: // CONTENT
          return CONTENT;
        case 5: // TIME
          return TIME;
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
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __ID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.TRIP, new org.apache.thrift.meta_data.FieldMetaData("trip", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.POSTER, new org.apache.thrift.meta_data.FieldMetaData("poster", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CONTENT, new org.apache.thrift.meta_data.FieldMetaData("content", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TIME, new org.apache.thrift.meta_data.FieldMetaData("time", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Post.class, metaDataMap);
  }

  public Post() {
  }

  public Post(
    int id,
    String trip,
    String poster,
    String content,
    String time)
  {
    this();
    this.id = id;
    setIdIsSet(true);
    this.trip = trip;
    this.poster = poster;
    this.content = content;
    this.time = time;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Post(Post other) {
    __isset_bitfield = other.__isset_bitfield;
    this.id = other.id;
    if (other.isSetTrip()) {
      this.trip = other.trip;
    }
    if (other.isSetPoster()) {
      this.poster = other.poster;
    }
    if (other.isSetContent()) {
      this.content = other.content;
    }
    if (other.isSetTime()) {
      this.time = other.time;
    }
  }

  public Post deepCopy() {
    return new Post(this);
  }

  @Override
  public void clear() {
    setIdIsSet(false);
    this.id = 0;
    this.trip = null;
    this.poster = null;
    this.content = null;
    this.time = null;
  }

  public int getId() {
    return this.id;
  }

  public Post setId(int id) {
    this.id = id;
    setIdIsSet(true);
    return this;
  }

  public void unsetId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ID_ISSET_ID);
  }

  /** Returns true if field id is set (has been assigned a value) and false otherwise */
  public boolean isSetId() {
    return EncodingUtils.testBit(__isset_bitfield, __ID_ISSET_ID);
  }

  public void setIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ID_ISSET_ID, value);
  }

  public String getTrip() {
    return this.trip;
  }

  public Post setTrip(String trip) {
    this.trip = trip;
    return this;
  }

  public void unsetTrip() {
    this.trip = null;
  }

  /** Returns true if field trip is set (has been assigned a value) and false otherwise */
  public boolean isSetTrip() {
    return this.trip != null;
  }

  public void setTripIsSet(boolean value) {
    if (!value) {
      this.trip = null;
    }
  }

  public String getPoster() {
    return this.poster;
  }

  public Post setPoster(String poster) {
    this.poster = poster;
    return this;
  }

  public void unsetPoster() {
    this.poster = null;
  }

  /** Returns true if field poster is set (has been assigned a value) and false otherwise */
  public boolean isSetPoster() {
    return this.poster != null;
  }

  public void setPosterIsSet(boolean value) {
    if (!value) {
      this.poster = null;
    }
  }

  public String getContent() {
    return this.content;
  }

  public Post setContent(String content) {
    this.content = content;
    return this;
  }

  public void unsetContent() {
    this.content = null;
  }

  /** Returns true if field content is set (has been assigned a value) and false otherwise */
  public boolean isSetContent() {
    return this.content != null;
  }

  public void setContentIsSet(boolean value) {
    if (!value) {
      this.content = null;
    }
  }

  public String getTime() {
    return this.time;
  }

  public Post setTime(String time) {
    this.time = time;
    return this;
  }

  public void unsetTime() {
    this.time = null;
  }

  /** Returns true if field time is set (has been assigned a value) and false otherwise */
  public boolean isSetTime() {
    return this.time != null;
  }

  public void setTimeIsSet(boolean value) {
    if (!value) {
      this.time = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case ID:
      if (value == null) {
        unsetId();
      } else {
        setId((Integer)value);
      }
      break;

    case TRIP:
      if (value == null) {
        unsetTrip();
      } else {
        setTrip((String)value);
      }
      break;

    case POSTER:
      if (value == null) {
        unsetPoster();
      } else {
        setPoster((String)value);
      }
      break;

    case CONTENT:
      if (value == null) {
        unsetContent();
      } else {
        setContent((String)value);
      }
      break;

    case TIME:
      if (value == null) {
        unsetTime();
      } else {
        setTime((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ID:
      return Integer.valueOf(getId());

    case TRIP:
      return getTrip();

    case POSTER:
      return getPoster();

    case CONTENT:
      return getContent();

    case TIME:
      return getTime();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case ID:
      return isSetId();
    case TRIP:
      return isSetTrip();
    case POSTER:
      return isSetPoster();
    case CONTENT:
      return isSetContent();
    case TIME:
      return isSetTime();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Post)
      return this.equals((Post)that);
    return false;
  }

  public boolean equals(Post that) {
    if (that == null)
      return false;

    boolean this_present_id = true;
    boolean that_present_id = true;
    if (this_present_id || that_present_id) {
      if (!(this_present_id && that_present_id))
        return false;
      if (this.id != that.id)
        return false;
    }

    boolean this_present_trip = true && this.isSetTrip();
    boolean that_present_trip = true && that.isSetTrip();
    if (this_present_trip || that_present_trip) {
      if (!(this_present_trip && that_present_trip))
        return false;
      if (!this.trip.equals(that.trip))
        return false;
    }

    boolean this_present_poster = true && this.isSetPoster();
    boolean that_present_poster = true && that.isSetPoster();
    if (this_present_poster || that_present_poster) {
      if (!(this_present_poster && that_present_poster))
        return false;
      if (!this.poster.equals(that.poster))
        return false;
    }

    boolean this_present_content = true && this.isSetContent();
    boolean that_present_content = true && that.isSetContent();
    if (this_present_content || that_present_content) {
      if (!(this_present_content && that_present_content))
        return false;
      if (!this.content.equals(that.content))
        return false;
    }

    boolean this_present_time = true && this.isSetTime();
    boolean that_present_time = true && that.isSetTime();
    if (this_present_time || that_present_time) {
      if (!(this_present_time && that_present_time))
        return false;
      if (!this.time.equals(that.time))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(Post other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetId()).compareTo(other.isSetId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.id, other.id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTrip()).compareTo(other.isSetTrip());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTrip()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.trip, other.trip);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPoster()).compareTo(other.isSetPoster());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPoster()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.poster, other.poster);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetContent()).compareTo(other.isSetContent());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetContent()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.content, other.content);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTime()).compareTo(other.isSetTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.time, other.time);
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
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Post(");
    boolean first = true;

    sb.append("id:");
    sb.append(this.id);
    first = false;
    if (!first) sb.append(", ");
    sb.append("trip:");
    if (this.trip == null) {
      sb.append("null");
    } else {
      sb.append(this.trip);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("poster:");
    if (this.poster == null) {
      sb.append("null");
    } else {
      sb.append(this.poster);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("content:");
    if (this.content == null) {
      sb.append("null");
    } else {
      sb.append(this.content);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("time:");
    if (this.time == null) {
      sb.append("null");
    } else {
      sb.append(this.time);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class PostStandardSchemeFactory implements SchemeFactory {
    public PostStandardScheme getScheme() {
      return new PostStandardScheme();
    }
  }

  private static class PostStandardScheme extends StandardScheme<Post> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Post struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.id = iprot.readI32();
              struct.setIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TRIP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.trip = iprot.readString();
              struct.setTripIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // POSTER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.poster = iprot.readString();
              struct.setPosterIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // CONTENT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.content = iprot.readString();
              struct.setContentIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.time = iprot.readString();
              struct.setTimeIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Post struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(ID_FIELD_DESC);
      oprot.writeI32(struct.id);
      oprot.writeFieldEnd();
      if (struct.trip != null) {
        oprot.writeFieldBegin(TRIP_FIELD_DESC);
        oprot.writeString(struct.trip);
        oprot.writeFieldEnd();
      }
      if (struct.poster != null) {
        oprot.writeFieldBegin(POSTER_FIELD_DESC);
        oprot.writeString(struct.poster);
        oprot.writeFieldEnd();
      }
      if (struct.content != null) {
        oprot.writeFieldBegin(CONTENT_FIELD_DESC);
        oprot.writeString(struct.content);
        oprot.writeFieldEnd();
      }
      if (struct.time != null) {
        oprot.writeFieldBegin(TIME_FIELD_DESC);
        oprot.writeString(struct.time);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PostTupleSchemeFactory implements SchemeFactory {
    public PostTupleScheme getScheme() {
      return new PostTupleScheme();
    }
  }

  private static class PostTupleScheme extends TupleScheme<Post> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Post struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetId()) {
        optionals.set(0);
      }
      if (struct.isSetTrip()) {
        optionals.set(1);
      }
      if (struct.isSetPoster()) {
        optionals.set(2);
      }
      if (struct.isSetContent()) {
        optionals.set(3);
      }
      if (struct.isSetTime()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetId()) {
        oprot.writeI32(struct.id);
      }
      if (struct.isSetTrip()) {
        oprot.writeString(struct.trip);
      }
      if (struct.isSetPoster()) {
        oprot.writeString(struct.poster);
      }
      if (struct.isSetContent()) {
        oprot.writeString(struct.content);
      }
      if (struct.isSetTime()) {
        oprot.writeString(struct.time);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Post struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.id = iprot.readI32();
        struct.setIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.trip = iprot.readString();
        struct.setTripIsSet(true);
      }
      if (incoming.get(2)) {
        struct.poster = iprot.readString();
        struct.setPosterIsSet(true);
      }
      if (incoming.get(3)) {
        struct.content = iprot.readString();
        struct.setContentIsSet(true);
      }
      if (incoming.get(4)) {
        struct.time = iprot.readString();
        struct.setTimeIsSet(true);
      }
    }
  }

}

