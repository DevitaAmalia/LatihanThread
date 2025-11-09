public class testConn {
    public static void main(String[] args) throws Exception {
    try (java.sql.Connection c = DB.getConnection()) {
      System.out.println("OK: " + !c.isClosed());
    }
  }
}
