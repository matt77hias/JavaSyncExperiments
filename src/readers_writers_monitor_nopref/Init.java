package readers_writers_monitor_nopref;

public class Init {

	public static void main(String[] args) {
		Database database = new Database();
		for (int i=0; i<Database.NB_OF_READERS; i++) {
			new Thread(new Reader(database, i)).start();
		}
		for (int i=0; i<Database.NB_OF_WRITERS; i++) {
			new Thread(new Writer(database, i)).start();
		}
	}
}
