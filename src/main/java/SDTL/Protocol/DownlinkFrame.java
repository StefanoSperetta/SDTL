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
public class DownlinkFrame extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 4536358997181961296L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"DownlinkFrame\",\"namespace\":\"SDTL.Protocol\",\"fields\":[{\"name\":\"timestamp\",\"type\":\"long\"},{\"name\":\"data\",\"type\":\"bytes\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<DownlinkFrame> ENCODER =
      new BinaryMessageEncoder<DownlinkFrame>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<DownlinkFrame> DECODER =
      new BinaryMessageDecoder<DownlinkFrame>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<DownlinkFrame> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<DownlinkFrame> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<DownlinkFrame>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this DownlinkFrame to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a DownlinkFrame from a ByteBuffer. */
  public static DownlinkFrame fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public long timestamp;
  @Deprecated public java.nio.ByteBuffer data;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public DownlinkFrame() {}

  /**
   * All-args constructor.
   * @param timestamp The new value for timestamp
   * @param data The new value for data
   */
  public DownlinkFrame(java.lang.Long timestamp, java.nio.ByteBuffer data) {
    this.timestamp = timestamp;
    this.data = data;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return timestamp;
    case 1: return data;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: timestamp = (java.lang.Long)value$; break;
    case 1: data = (java.nio.ByteBuffer)value$; break;
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
   * Gets the value of the 'data' field.
   * @return The value of the 'data' field.
   */
  public java.nio.ByteBuffer getData() {
    return data;
  }

  /**
   * Sets the value of the 'data' field.
   * @param value the value to set.
   */
  public void setData(java.nio.ByteBuffer value) {
    this.data = value;
  }

  /**
   * Creates a new DownlinkFrame RecordBuilder.
   * @return A new DownlinkFrame RecordBuilder
   */
  public static SDTL.Protocol.DownlinkFrame.Builder newBuilder() {
    return new SDTL.Protocol.DownlinkFrame.Builder();
  }

  /**
   * Creates a new DownlinkFrame RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new DownlinkFrame RecordBuilder
   */
  public static SDTL.Protocol.DownlinkFrame.Builder newBuilder(SDTL.Protocol.DownlinkFrame.Builder other) {
    return new SDTL.Protocol.DownlinkFrame.Builder(other);
  }

  /**
   * Creates a new DownlinkFrame RecordBuilder by copying an existing DownlinkFrame instance.
   * @param other The existing instance to copy.
   * @return A new DownlinkFrame RecordBuilder
   */
  public static SDTL.Protocol.DownlinkFrame.Builder newBuilder(SDTL.Protocol.DownlinkFrame other) {
    return new SDTL.Protocol.DownlinkFrame.Builder(other);
  }

  /**
   * RecordBuilder for DownlinkFrame instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<DownlinkFrame>
    implements org.apache.avro.data.RecordBuilder<DownlinkFrame> {

    private long timestamp;
    private java.nio.ByteBuffer data;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(SDTL.Protocol.DownlinkFrame.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[0].schema(), other.timestamp);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.data)) {
        this.data = data().deepCopy(fields()[1].schema(), other.data);
        fieldSetFlags()[1] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing DownlinkFrame instance
     * @param other The existing instance to copy.
     */
    private Builder(SDTL.Protocol.DownlinkFrame other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[0].schema(), other.timestamp);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.data)) {
        this.data = data().deepCopy(fields()[1].schema(), other.data);
        fieldSetFlags()[1] = true;
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
    public SDTL.Protocol.DownlinkFrame.Builder setTimestamp(long value) {
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
    public SDTL.Protocol.DownlinkFrame.Builder clearTimestamp() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'data' field.
      * @return The value.
      */
    public java.nio.ByteBuffer getData() {
      return data;
    }

    /**
      * Sets the value of the 'data' field.
      * @param value The value of 'data'.
      * @return This builder.
      */
    public SDTL.Protocol.DownlinkFrame.Builder setData(java.nio.ByteBuffer value) {
      validate(fields()[1], value);
      this.data = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'data' field has been set.
      * @return True if the 'data' field has been set, false otherwise.
      */
    public boolean hasData() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'data' field.
      * @return This builder.
      */
    public SDTL.Protocol.DownlinkFrame.Builder clearData() {
      data = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DownlinkFrame build() {
      try {
        DownlinkFrame record = new DownlinkFrame();
        record.timestamp = fieldSetFlags()[0] ? this.timestamp : (java.lang.Long) defaultValue(fields()[0]);
        record.data = fieldSetFlags()[1] ? this.data : (java.nio.ByteBuffer) defaultValue(fields()[1]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<DownlinkFrame>
    WRITER$ = (org.apache.avro.io.DatumWriter<DownlinkFrame>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<DownlinkFrame>
    READER$ = (org.apache.avro.io.DatumReader<DownlinkFrame>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
