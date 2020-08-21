package com.ajiranet.connections.constants;

import java.util.regex.Pattern;

public class Constants {
	public static final String DEVICE_NAME_ALREADY_EXISTS_MSG = "ERROR: Device name %s already exists";
	public static final String SUCCESSFULLY_ADDED_MSG = "Successfully added ";
	public static final String ERROR_ADDING_DEVICE_MSG = "Error while adding device %s :%s";
	public static final String INVALID_COMMAND_SYNTAX_MSG = "Error: Invalid command syntax ";
	public static final String DEVICE_NOT_FOUND_MSG = "Error: Device Not found: %s";
	public static final String CONNECTED_WITH_MSG = "%s connected with %s";
	public static final String CONNECT_ERROR_MSG = "ERROR: Following devices were not connected due to : \n Connection already exists for devices: %s \n Same Devices: %s \n Device does not exist:  %s";
	public static final String SIGNAL_STRENGTH_DEVICE_NOT_FOUND = "ERROR: Signal Strength updated failed. Device %s not found";
	public static final String SIGNAL_STRENGTH_UPDATED = "ERROR: Signal Strength updated for Device %s";
	public static final String SIGNAL_STRENGTH_DEVICE_UNSUPPORTED = "ERROR: Signal Strength updated failed %s unsupported device";
	public static final String QUIT = "QUIT";
	public static final String WHITE_SPACE_STRING = " ";
	public static final String CHOOSE_VALID_OPTION_MESSAGE = "Please choose a valid option";
	public static final String CHOOSE_OPTION_MESSAGE = "Please choose an option below";
	public static final String WELCOME_MESSAGE = "Welcome to Ajira Net. Manage your connections";
	public static final Pattern MENU_OPTION_PATTERN = Pattern.compile("(ADD|CONNECT|INFO_ROUTE|SET_DEVICE_STRENGTH|QUIT)");
	public static final Pattern ADD_DEVICE_PATTERN = Pattern.compile("(ADD) (COMPUTER|REPEATER) (\\w{1,5}$)");
	public static final Pattern CONNECT_DEVICE_PATTERN = Pattern.compile("(CONNECT)(\\s\\w{1,5})((\\s\\w{1,5}){1,10})");
	public static final Pattern INFO_ROUTE_PATTERN = Pattern.compile("(INFO_ROUTE) (\\w{1,5}) (\\w{1,5})");
	public static final Pattern SIGNAL_STRENGTH_PATTERN = Pattern.compile("(SET_DEVICE_STRENGTH) (\\w{1,5}) ([0-9][0]?)");
	public static final String ALPHABET_DETECTOR_PATTERN = "\\w*[a-zA-Z]\\w*";
	public static final String QUIT_SYSTEM_OPTION = QUIT;
	public static final String SET_DEVICE_STRENGTH_OPTION = "SET_DEVICE_STRENGTH <DEVICE_NAME> <#STRENGTH>";
	public static final String ROUTE_INFORMATION_OPTION = "INFO_ROUTE <SRC_DEVICE_NAME> <DEST_DEVICE_NAME>";
	public static final String CONNECT_DEVICES_OPTION = "CONNECT <DEVICE_NAME> <DEVICE_LIST>";
	public static final String ADD_A_DEVICE_OPTION = "ADD <DEVICE_TYPE> <DEVICE_NAME>";
	
}
