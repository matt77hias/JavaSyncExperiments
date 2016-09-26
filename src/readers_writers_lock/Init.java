package readers_writers_lock;

public class Init {

	public static void main(String[] args) {
		Database database = new Database();
		for (int i=0; i<5; i++) {
			new Thread(new Reader(database, i)).start();
			new Thread(new Writer(database, i)).start();
		}
	}
}
