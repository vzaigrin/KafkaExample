/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package ru.vzaigrin.examples.kafka.streams;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class PageView extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 2602767775470242259L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PageView\",\"namespace\":\"ru.vzaigrin.examples.kafka.streams\",\"fields\":[{\"name\":\"user\",\"type\":\"string\"},{\"name\":\"page\",\"type\":\"string\"},{\"name\":\"industry\",\"type\":\"string\"},{\"name\":\"flags\",\"type\":[\"null\",\"string\"],\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<PageView> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<PageView> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<PageView> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<PageView> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<PageView> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this PageView to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a PageView from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a PageView instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static PageView fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.CharSequence user;
  private java.lang.CharSequence page;
  private java.lang.CharSequence industry;
  private java.lang.CharSequence flags;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public PageView() {}

  /**
   * All-args constructor.
   * @param user The new value for user
   * @param page The new value for page
   * @param industry The new value for industry
   * @param flags The new value for flags
   */
  public PageView(java.lang.CharSequence user, java.lang.CharSequence page, java.lang.CharSequence industry, java.lang.CharSequence flags) {
    this.user = user;
    this.page = page;
    this.industry = industry;
    this.flags = flags;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return user;
    case 1: return page;
    case 2: return industry;
    case 3: return flags;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: user = (java.lang.CharSequence)value$; break;
    case 1: page = (java.lang.CharSequence)value$; break;
    case 2: industry = (java.lang.CharSequence)value$; break;
    case 3: flags = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'user' field.
   * @return The value of the 'user' field.
   */
  public java.lang.CharSequence getUser() {
    return user;
  }


  /**
   * Sets the value of the 'user' field.
   * @param value the value to set.
   */
  public void setUser(java.lang.CharSequence value) {
    this.user = value;
  }

  /**
   * Gets the value of the 'page' field.
   * @return The value of the 'page' field.
   */
  public java.lang.CharSequence getPage() {
    return page;
  }


  /**
   * Sets the value of the 'page' field.
   * @param value the value to set.
   */
  public void setPage(java.lang.CharSequence value) {
    this.page = value;
  }

  /**
   * Gets the value of the 'industry' field.
   * @return The value of the 'industry' field.
   */
  public java.lang.CharSequence getIndustry() {
    return industry;
  }


  /**
   * Sets the value of the 'industry' field.
   * @param value the value to set.
   */
  public void setIndustry(java.lang.CharSequence value) {
    this.industry = value;
  }

  /**
   * Gets the value of the 'flags' field.
   * @return The value of the 'flags' field.
   */
  public java.lang.CharSequence getFlags() {
    return flags;
  }


  /**
   * Sets the value of the 'flags' field.
   * @param value the value to set.
   */
  public void setFlags(java.lang.CharSequence value) {
    this.flags = value;
  }

  /**
   * Creates a new PageView RecordBuilder.
   * @return A new PageView RecordBuilder
   */
  public static ru.vzaigrin.examples.kafka.streams.PageView.Builder newBuilder() {
    return new ru.vzaigrin.examples.kafka.streams.PageView.Builder();
  }

  /**
   * Creates a new PageView RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new PageView RecordBuilder
   */
  public static ru.vzaigrin.examples.kafka.streams.PageView.Builder newBuilder(ru.vzaigrin.examples.kafka.streams.PageView.Builder other) {
    if (other == null) {
      return new ru.vzaigrin.examples.kafka.streams.PageView.Builder();
    } else {
      return new ru.vzaigrin.examples.kafka.streams.PageView.Builder(other);
    }
  }

  /**
   * Creates a new PageView RecordBuilder by copying an existing PageView instance.
   * @param other The existing instance to copy.
   * @return A new PageView RecordBuilder
   */
  public static ru.vzaigrin.examples.kafka.streams.PageView.Builder newBuilder(ru.vzaigrin.examples.kafka.streams.PageView other) {
    if (other == null) {
      return new ru.vzaigrin.examples.kafka.streams.PageView.Builder();
    } else {
      return new ru.vzaigrin.examples.kafka.streams.PageView.Builder(other);
    }
  }

  /**
   * RecordBuilder for PageView instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PageView>
    implements org.apache.avro.data.RecordBuilder<PageView> {

    private java.lang.CharSequence user;
    private java.lang.CharSequence page;
    private java.lang.CharSequence industry;
    private java.lang.CharSequence flags;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(ru.vzaigrin.examples.kafka.streams.PageView.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.user)) {
        this.user = data().deepCopy(fields()[0].schema(), other.user);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.page)) {
        this.page = data().deepCopy(fields()[1].schema(), other.page);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.industry)) {
        this.industry = data().deepCopy(fields()[2].schema(), other.industry);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.flags)) {
        this.flags = data().deepCopy(fields()[3].schema(), other.flags);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing PageView instance
     * @param other The existing instance to copy.
     */
    private Builder(ru.vzaigrin.examples.kafka.streams.PageView other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.user)) {
        this.user = data().deepCopy(fields()[0].schema(), other.user);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.page)) {
        this.page = data().deepCopy(fields()[1].schema(), other.page);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.industry)) {
        this.industry = data().deepCopy(fields()[2].schema(), other.industry);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.flags)) {
        this.flags = data().deepCopy(fields()[3].schema(), other.flags);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'user' field.
      * @return The value.
      */
    public java.lang.CharSequence getUser() {
      return user;
    }


    /**
      * Sets the value of the 'user' field.
      * @param value The value of 'user'.
      * @return This builder.
      */
    public ru.vzaigrin.examples.kafka.streams.PageView.Builder setUser(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.user = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'user' field has been set.
      * @return True if the 'user' field has been set, false otherwise.
      */
    public boolean hasUser() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'user' field.
      * @return This builder.
      */
    public ru.vzaigrin.examples.kafka.streams.PageView.Builder clearUser() {
      user = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'page' field.
      * @return The value.
      */
    public java.lang.CharSequence getPage() {
      return page;
    }


    /**
      * Sets the value of the 'page' field.
      * @param value The value of 'page'.
      * @return This builder.
      */
    public ru.vzaigrin.examples.kafka.streams.PageView.Builder setPage(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.page = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'page' field has been set.
      * @return True if the 'page' field has been set, false otherwise.
      */
    public boolean hasPage() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'page' field.
      * @return This builder.
      */
    public ru.vzaigrin.examples.kafka.streams.PageView.Builder clearPage() {
      page = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'industry' field.
      * @return The value.
      */
    public java.lang.CharSequence getIndustry() {
      return industry;
    }


    /**
      * Sets the value of the 'industry' field.
      * @param value The value of 'industry'.
      * @return This builder.
      */
    public ru.vzaigrin.examples.kafka.streams.PageView.Builder setIndustry(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.industry = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'industry' field has been set.
      * @return True if the 'industry' field has been set, false otherwise.
      */
    public boolean hasIndustry() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'industry' field.
      * @return This builder.
      */
    public ru.vzaigrin.examples.kafka.streams.PageView.Builder clearIndustry() {
      industry = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'flags' field.
      * @return The value.
      */
    public java.lang.CharSequence getFlags() {
      return flags;
    }


    /**
      * Sets the value of the 'flags' field.
      * @param value The value of 'flags'.
      * @return This builder.
      */
    public ru.vzaigrin.examples.kafka.streams.PageView.Builder setFlags(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.flags = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'flags' field has been set.
      * @return True if the 'flags' field has been set, false otherwise.
      */
    public boolean hasFlags() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'flags' field.
      * @return This builder.
      */
    public ru.vzaigrin.examples.kafka.streams.PageView.Builder clearFlags() {
      flags = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageView build() {
      try {
        PageView record = new PageView();
        record.user = fieldSetFlags()[0] ? this.user : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.page = fieldSetFlags()[1] ? this.page : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.industry = fieldSetFlags()[2] ? this.industry : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.flags = fieldSetFlags()[3] ? this.flags : (java.lang.CharSequence) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<PageView>
    WRITER$ = (org.apache.avro.io.DatumWriter<PageView>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<PageView>
    READER$ = (org.apache.avro.io.DatumReader<PageView>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.user);

    out.writeString(this.page);

    out.writeString(this.industry);

    if (this.flags == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeString(this.flags);
    }

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.user = in.readString(this.user instanceof Utf8 ? (Utf8)this.user : null);

      this.page = in.readString(this.page instanceof Utf8 ? (Utf8)this.page : null);

      this.industry = in.readString(this.industry instanceof Utf8 ? (Utf8)this.industry : null);

      if (in.readIndex() != 1) {
        in.readNull();
        this.flags = null;
      } else {
        this.flags = in.readString(this.flags instanceof Utf8 ? (Utf8)this.flags : null);
      }

    } else {
      for (int i = 0; i < 4; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.user = in.readString(this.user instanceof Utf8 ? (Utf8)this.user : null);
          break;

        case 1:
          this.page = in.readString(this.page instanceof Utf8 ? (Utf8)this.page : null);
          break;

        case 2:
          this.industry = in.readString(this.industry instanceof Utf8 ? (Utf8)this.industry : null);
          break;

        case 3:
          if (in.readIndex() != 1) {
            in.readNull();
            this.flags = null;
          } else {
            this.flags = in.readString(this.flags instanceof Utf8 ? (Utf8)this.flags : null);
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










