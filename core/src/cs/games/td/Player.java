package cs.games.td;

public class Player {

	private static int money;
	
	public Player() {
		money = 1000;
	}
	
	public int getMoney() {
		return money;
	}
	
	public static void earnMoney(int m) {
		money += m;
	}
	
	public static boolean spendMoney(int m) {
		boolean spent = false;
		if (m <= money) {
			money -= m;
			spent = true;
		}
		return spent;
	}
	
}
