package de.htwk.aop.risiko;

public class Application {

	public static void main(String[] args) {
		Menu menu = new MenuImpl();
		menu.startMenu();
		System.out.println("Programm wird beendet");
	}
}