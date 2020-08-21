package com.ajiranet.connections;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.ajiranet.connections.constants.Constants;
import com.ajiranet.connections.model.Device;
import com.ajiranet.connections.model.Response;
import com.ajiranet.connections.network.Network;
import com.ajiranet.connections.network.NetworkFactory;

public class Main {


	public static void main(String[] args) {

		Map<String, Device> devices = new TreeMap<String, Device>();

		Scanner scanner = getScanner();

		System.out.println(Constants.WELCOME_MESSAGE);
		System.out.println(Constants.CHOOSE_OPTION_MESSAGE);
		
		NetworkFactory networkFactory = new NetworkFactory();

		printMenuoptions();

		String menuOption = null;

		while (scanner.hasNextLine()) {
			if (scanner.hasNext(Constants.MENU_OPTION_PATTERN)) {
				menuOption = scanner.nextLine();
				if ("".equals(menuOption.trim())) {
					scanner.nextLine();
				} else {
					menuOption = menuOption.toUpperCase().trim();
					int whitespaceIndex = menuOption.indexOf(Constants.WHITE_SPACE_STRING);
					String optionType = whitespaceIndex!=-1?menuOption.substring(0, whitespaceIndex).trim():menuOption;
					System.out.println(String.format("Command %s", optionType));
					if (Constants.QUIT.equals(optionType)) {
						scanner.close();
						System.out.println(String.format("Thanks for choosing AjiraNet %s", menuOption));
						break;
					}
					Network network = networkFactory.getNetwork(optionType);

					Response response = network.process(menuOption, devices);
					System.out.println(String.format("Message: %s", response.getMessage()));
					if (response.isError()) {
						System.out.println(response.getErrorMessage());
					}
				}

			} else {
				System.out.println("Error: Invalid command syntax.");
				scanner.nextLine();
			}
		}

	}


	private static Scanner getScanner() {
		return new Scanner(System.in);
	}

	private static void printMenuoptions() {
		System.out.println(Constants.ADD_A_DEVICE_OPTION);
		System.out.println(Constants.CONNECT_DEVICES_OPTION);
		System.out.println(Constants.ROUTE_INFORMATION_OPTION);
		System.out.println(Constants.SET_DEVICE_STRENGTH_OPTION);
		System.out.println(Constants.QUIT_SYSTEM_OPTION);
	}

	
	

}
