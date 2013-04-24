package ee.smkv.calc.loan;

/**
 * @author samko
 */
public class FieldNumberFormatException extends NumberFormatException {
  final int id;

  public FieldNumberFormatException(int id , String message) {
    super(message);
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
