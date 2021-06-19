package controller;

public class test {

	public static void main(String[] args){

		Jeu game = new Jeu();
		


		int[][] possibilites = game.get_possibilities(game.get_plateau()[5][2],5,2);
		game.display();
		System.out.println("");
		game.move(4,2);
		game.display();
		possibilites = game.get_possibilities(game.get_plateau()[4][2],4,2);
		game.move(3,2);
		possibilites = game.get_possibilities(game.get_plateau()[3][2],3,2);
		game.move(2,2);
		possibilites = game.get_possibilities(game.get_plateau()[2][2],2,2);
		game.move(1,2);
		game.display();

	}
}