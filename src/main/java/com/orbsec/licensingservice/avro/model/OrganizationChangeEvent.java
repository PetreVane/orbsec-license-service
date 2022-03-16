/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.orbsec.licensingservice.avro.model;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class OrganizationChangeEvent extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -1316861420480532617L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"OrganizationChangeEvent\",\"namespace\":\"com.orbsec.licensingservice.avro.model\",\"fields\":[{\"name\":\"organizationId\",\"type\":\"string\"},{\"name\":\"changeType\",\"type\":{\"type\":\"enum\",\"name\":\"ChangeType\",\"symbols\":[\"CREATION\",\"UPDATE\",\"DELETION\"]}},{\"name\":\"description\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<OrganizationChangeEvent> ENCODER =
      new BinaryMessageEncoder<OrganizationChangeEvent>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<OrganizationChangeEvent> DECODER =
      new BinaryMessageDecoder<OrganizationChangeEvent>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<OrganizationChangeEvent> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<OrganizationChangeEvent> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<OrganizationChangeEvent> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<OrganizationChangeEvent>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this OrganizationChangeEvent to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a OrganizationChangeEvent from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a OrganizationChangeEvent instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static OrganizationChangeEvent fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.CharSequence organizationId;
  private com.orbsec.licensingservice.avro.model.ChangeType changeType;
  private java.lang.CharSequence description;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public OrganizationChangeEvent() {}

  /**
   * All-args constructor.
   * @param organizationId The new value for organizationId
   * @param changeType The new value for changeType
   * @param description The new value for description
   */
  public OrganizationChangeEvent(java.lang.CharSequence organizationId, com.orbsec.licensingservice.avro.model.ChangeType changeType, java.lang.CharSequence description) {
    this.organizationId = organizationId;
    this.changeType = changeType;
    this.description = description;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return organizationId;
    case 1: return changeType;
    case 2: return description;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: organizationId = (java.lang.CharSequence)value$; break;
    case 1: changeType = (com.orbsec.licensingservice.avro.model.ChangeType)value$; break;
    case 2: description = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'organizationId' field.
   * @return The value of the 'organizationId' field.
   */
  public java.lang.CharSequence getOrganizationId() {
    return organizationId;
  }


  /**
   * Sets the value of the 'organizationId' field.
   * @param value the value to set.
   */
  public void setOrganizationId(java.lang.CharSequence value) {
    this.organizationId = value;
  }

  /**
   * Gets the value of the 'changeType' field.
   * @return The value of the 'changeType' field.
   */
  public com.orbsec.licensingservice.avro.model.ChangeType getChangeType() {
    return changeType;
  }


  /**
   * Sets the value of the 'changeType' field.
   * @param value the value to set.
   */
  public void setChangeType(com.orbsec.licensingservice.avro.model.ChangeType value) {
    this.changeType = value;
  }

  /**
   * Gets the value of the 'description' field.
   * @return The value of the 'description' field.
   */
  public java.lang.CharSequence getDescription() {
    return description;
  }


  /**
   * Sets the value of the 'description' field.
   * @param value the value to set.
   */
  public void setDescription(java.lang.CharSequence value) {
    this.description = value;
  }

  /**
   * Creates a new OrganizationChangeEvent RecordBuilder.
   * @return A new OrganizationChangeEvent RecordBuilder
   */
  public static com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder newBuilder() {
    return new com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder();
  }

  /**
   * Creates a new OrganizationChangeEvent RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new OrganizationChangeEvent RecordBuilder
   */
  public static com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder newBuilder(com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder other) {
    if (other == null) {
      return new com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder();
    } else {
      return new com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder(other);
    }
  }

  /**
   * Creates a new OrganizationChangeEvent RecordBuilder by copying an existing OrganizationChangeEvent instance.
   * @param other The existing instance to copy.
   * @return A new OrganizationChangeEvent RecordBuilder
   */
  public static com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder newBuilder(com.orbsec.licensingservice.avro.model.OrganizationChangeEvent other) {
    if (other == null) {
      return new com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder();
    } else {
      return new com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder(other);
    }
  }

  /**
   * RecordBuilder for OrganizationChangeEvent instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<OrganizationChangeEvent>
    implements org.apache.avro.data.RecordBuilder<OrganizationChangeEvent> {

    private java.lang.CharSequence organizationId;
    private com.orbsec.licensingservice.avro.model.ChangeType changeType;
    private java.lang.CharSequence description;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.organizationId)) {
        this.organizationId = data().deepCopy(fields()[0].schema(), other.organizationId);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.changeType)) {
        this.changeType = data().deepCopy(fields()[1].schema(), other.changeType);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.description)) {
        this.description = data().deepCopy(fields()[2].schema(), other.description);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing OrganizationChangeEvent instance
     * @param other The existing instance to copy.
     */
    private Builder(com.orbsec.licensingservice.avro.model.OrganizationChangeEvent other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.organizationId)) {
        this.organizationId = data().deepCopy(fields()[0].schema(), other.organizationId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.changeType)) {
        this.changeType = data().deepCopy(fields()[1].schema(), other.changeType);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.description)) {
        this.description = data().deepCopy(fields()[2].schema(), other.description);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'organizationId' field.
      * @return The value.
      */
    public java.lang.CharSequence getOrganizationId() {
      return organizationId;
    }


    /**
      * Sets the value of the 'organizationId' field.
      * @param value The value of 'organizationId'.
      * @return This builder.
      */
    public com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder setOrganizationId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.organizationId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'organizationId' field has been set.
      * @return True if the 'organizationId' field has been set, false otherwise.
      */
    public boolean hasOrganizationId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'organizationId' field.
      * @return This builder.
      */
    public com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder clearOrganizationId() {
      organizationId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'changeType' field.
      * @return The value.
      */
    public com.orbsec.licensingservice.avro.model.ChangeType getChangeType() {
      return changeType;
    }


    /**
      * Sets the value of the 'changeType' field.
      * @param value The value of 'changeType'.
      * @return This builder.
      */
    public com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder setChangeType(com.orbsec.licensingservice.avro.model.ChangeType value) {
      validate(fields()[1], value);
      this.changeType = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'changeType' field has been set.
      * @return True if the 'changeType' field has been set, false otherwise.
      */
    public boolean hasChangeType() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'changeType' field.
      * @return This builder.
      */
    public com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder clearChangeType() {
      changeType = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'description' field.
      * @return The value.
      */
    public java.lang.CharSequence getDescription() {
      return description;
    }


    /**
      * Sets the value of the 'description' field.
      * @param value The value of 'description'.
      * @return This builder.
      */
    public com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder setDescription(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.description = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'description' field has been set.
      * @return True if the 'description' field has been set, false otherwise.
      */
    public boolean hasDescription() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'description' field.
      * @return This builder.
      */
    public com.orbsec.licensingservice.avro.model.OrganizationChangeEvent.Builder clearDescription() {
      description = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public OrganizationChangeEvent build() {
      try {
        OrganizationChangeEvent record = new OrganizationChangeEvent();
        record.organizationId = fieldSetFlags()[0] ? this.organizationId : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.changeType = fieldSetFlags()[1] ? this.changeType : (com.orbsec.licensingservice.avro.model.ChangeType) defaultValue(fields()[1]);
        record.description = fieldSetFlags()[2] ? this.description : (java.lang.CharSequence) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<OrganizationChangeEvent>
    WRITER$ = (org.apache.avro.io.DatumWriter<OrganizationChangeEvent>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<OrganizationChangeEvent>
    READER$ = (org.apache.avro.io.DatumReader<OrganizationChangeEvent>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.organizationId);

    out.writeEnum(this.changeType.ordinal());

    out.writeString(this.description);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.organizationId = in.readString(this.organizationId instanceof Utf8 ? (Utf8)this.organizationId : null);

      this.changeType = com.orbsec.licensingservice.avro.model.ChangeType.values()[in.readEnum()];

      this.description = in.readString(this.description instanceof Utf8 ? (Utf8)this.description : null);

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.organizationId = in.readString(this.organizationId instanceof Utf8 ? (Utf8)this.organizationId : null);
          break;

        case 1:
          this.changeType = com.orbsec.licensingservice.avro.model.ChangeType.values()[in.readEnum()];
          break;

        case 2:
          this.description = in.readString(this.description instanceof Utf8 ? (Utf8)this.description : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}









