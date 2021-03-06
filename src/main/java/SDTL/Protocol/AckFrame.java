/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package SDTL.Protocol;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class AckFrame extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 2597451114033863811L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AckFrame\",\"namespace\":\"SDTL.Protocol\",\"fields\":[{\"name\":\"timestamp\",\"type\":[\"null\",\"long\"],\"default\":\"null\"},{\"name\":\"ack\",\"type\":\"boolean\"},{\"name\":\"hash\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<AckFrame> ENCODER =
      new BinaryMessageEncoder<AckFrame>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<AckFrame> DECODER =
      new BinaryMessageDecoder<AckFrame>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<AckFrame> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<AckFrame> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<AckFrame>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this AckFrame to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a AckFrame from a ByteBuffer. */
  public static AckFrame fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.Long timestamp;
  @Deprecated public boolean ack;
  @Deprecated public int hash;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public AckFrame() {}

  /**
   * All-args constructor.
   * @param timestamp The new value for timestamp
   * @param ack The new value for ack
   * @param hash The new value for hash
   */
  public AckFrame(java.lang.Long timestamp, java.lang.Boolean ack, java.lang.Integer hash) {
    this.timestamp = timestamp;
    this.ack = ack;
    this.hash = hash;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return timestamp;
    case 1: return ack;
    case 2: return hash;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: timestamp = (java.lang.Long)value$; break;
    case 1: ack = (java.lang.Boolean)value$; break;
    case 2: hash = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'timestamp' field.
   * @return The value of the 'timestamp' field.
   */
  public java.lang.Long getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the 'timestamp' field.
   * @param value the value to set.
   */
  public void setTimestamp(java.lang.Long value) {
    this.timestamp = value;
  }

  /**
   * Gets the value of the 'ack' field.
   * @return The value of the 'ack' field.
   */
  public java.lang.Boolean getAck() {
    return ack;
  }

  /**
   * Sets the value of the 'ack' field.
   * @param value the value to set.
   */
  public void setAck(java.lang.Boolean value) {
    this.ack = value;
  }

  /**
   * Gets the value of the 'hash' field.
   * @return The value of the 'hash' field.
   */
  public java.lang.Integer getHash() {
    return hash;
  }

  /**
   * Sets the value of the 'hash' field.
   * @param value the value to set.
   */
  public void setHash(java.lang.Integer value) {
    this.hash = value;
  }

  /**
   * Creates a new AckFrame RecordBuilder.
   * @return A new AckFrame RecordBuilder
   */
  public static SDTL.Protocol.AckFrame.Builder newBuilder() {
    return new SDTL.Protocol.AckFrame.Builder();
  }

  /**
   * Creates a new AckFrame RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new AckFrame RecordBuilder
   */
  public static SDTL.Protocol.AckFrame.Builder newBuilder(SDTL.Protocol.AckFrame.Builder other) {
    return new SDTL.Protocol.AckFrame.Builder(other);
  }

  /**
   * Creates a new AckFrame RecordBuilder by copying an existing AckFrame instance.
   * @param other The existing instance to copy.
   * @return A new AckFrame RecordBuilder
   */
  public static SDTL.Protocol.AckFrame.Builder newBuilder(SDTL.Protocol.AckFrame other) {
    return new SDTL.Protocol.AckFrame.Builder(other);
  }

  /**
   * RecordBuilder for AckFrame instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<AckFrame>
    implements org.apache.avro.data.RecordBuilder<AckFrame> {

    private java.lang.Long timestamp;
    private boolean ack;
    private int hash;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(SDTL.Protocol.AckFrame.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[0].schema(), other.timestamp);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.ack)) {
        this.ack = data().deepCopy(fields()[1].schema(), other.ack);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.hash)) {
        this.hash = data().deepCopy(fields()[2].schema(), other.hash);
        fieldSetFlags()[2] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing AckFrame instance
     * @param other The existing instance to copy.
     */
    private Builder(SDTL.Protocol.AckFrame other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[0].schema(), other.timestamp);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.ack)) {
        this.ack = data().deepCopy(fields()[1].schema(), other.ack);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.hash)) {
        this.hash = data().deepCopy(fields()[2].schema(), other.hash);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'timestamp' field.
      * @return The value.
      */
    public java.lang.Long getTimestamp() {
      return timestamp;
    }

    /**
      * Sets the value of the 'timestamp' field.
      * @param value The value of 'timestamp'.
      * @return This builder.
      */
    public SDTL.Protocol.AckFrame.Builder setTimestamp(java.lang.Long value) {
      validate(fields()[0], value);
      this.timestamp = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'timestamp' field has been set.
      * @return True if the 'timestamp' field has been set, false otherwise.
      */
    public boolean hasTimestamp() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'timestamp' field.
      * @return This builder.
      */
    public SDTL.Protocol.AckFrame.Builder clearTimestamp() {
      timestamp = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'ack' field.
      * @return The value.
      */
    public java.lang.Boolean getAck() {
      return ack;
    }

    /**
      * Sets the value of the 'ack' field.
      * @param value The value of 'ack'.
      * @return This builder.
      */
    public SDTL.Protocol.AckFrame.Builder setAck(boolean value) {
      validate(fields()[1], value);
      this.ack = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'ack' field has been set.
      * @return True if the 'ack' field has been set, false otherwise.
      */
    public boolean hasAck() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'ack' field.
      * @return This builder.
      */
    public SDTL.Protocol.AckFrame.Builder clearAck() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'hash' field.
      * @return The value.
      */
    public java.lang.Integer getHash() {
      return hash;
    }

    /**
      * Sets the value of the 'hash' field.
      * @param value The value of 'hash'.
      * @return This builder.
      */
    public SDTL.Protocol.AckFrame.Builder setHash(int value) {
      validate(fields()[2], value);
      this.hash = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'hash' field has been set.
      * @return True if the 'hash' field has been set, false otherwise.
      */
    public boolean hasHash() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'hash' field.
      * @return This builder.
      */
    public SDTL.Protocol.AckFrame.Builder clearHash() {
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AckFrame build() {
      try {
        AckFrame record = new AckFrame();
        record.timestamp = fieldSetFlags()[0] ? this.timestamp : (java.lang.Long) defaultValue(fields()[0]);
        record.ack = fieldSetFlags()[1] ? this.ack : (java.lang.Boolean) defaultValue(fields()[1]);
        record.hash = fieldSetFlags()[2] ? this.hash : (java.lang.Integer) defaultValue(fields()[2]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<AckFrame>
    WRITER$ = (org.apache.avro.io.DatumWriter<AckFrame>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<AckFrame>
    READER$ = (org.apache.avro.io.DatumReader<AckFrame>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
