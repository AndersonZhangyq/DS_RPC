/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package chat.struct;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum messageStatus implements org.apache.thrift.TEnum {
  UNREAD(1),
  SENT_TO_USER(2);

  private final int value;

  private messageStatus(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static messageStatus findByValue(int value) { 
    switch (value) {
      case 1:
        return UNREAD;
      case 2:
        return SENT_TO_USER;
      default:
        return null;
    }
  }
}
