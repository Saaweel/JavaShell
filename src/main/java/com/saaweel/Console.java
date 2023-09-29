package com.saaweel;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Console {
	public static Scanner readS = new Scanner(System.in);

	private void recursiveDelete(Path path) throws Exception {
		DirectoryStream<Path> stream = Files.newDirectoryStream(path);

		for (Path file : stream) {
			if (Files.isDirectory(file)) {
				recursiveDelete(file);
			} else {
				Files.delete(file);
			}
		}

		Files.delete(path);
	}

	private void createDirectoryRecursive(Path path) throws Exception {
		if (!Files.exists(path)) {
			createDirectoryRecursive(path.getParent());
			Files.createDirectory(path);
		}
	}

	public void ls(String path){
		if (path == null)
			path = System.getProperty("user.dir");

		Path dir = Paths.get(path);

		if (Files.exists(dir)) {
			if (Files.isDirectory(dir)) {
				System.out.println(Color.GREEN + "Directorio " + Color.ORANGE + path + Color.RESET);
				System.out.println("--------------------------------------------------");
				
				try {
					DirectoryStream<Path> stream = Files.newDirectoryStream(dir);

					for (Path file : stream) {
						if (Files.isDirectory(file)) {
							System.out.println(Color.BLUE + file.getFileName() + Color.RESET);
						} else {
							System.out.println(file.getFileName());
						}
					}
				} catch (Exception e) {
					System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET);
				}
			} else {
				System.out.println(Color.GREEN + "Archivo " + Color.ORANGE + path + Color.RESET);
				System.out.println("--------------------------------------------------");
				System.out.println("Nombre: " + dir.getFileName());
				System.out.println("Tamaño: " + dir.toFile().length() + " bytes");
				System.out.println("Ruta: " + dir.getParent());
			}
		} else {
			System.out.println(Color.RED + "Error: " + path + " no es un fichero ni directorio" + Color.RESET);
		}
	}

	public void cp(String fromFile, String toDirectory){
		Path from = Paths.get(fromFile);

		if (Files.exists(from)) {
			Path to = Paths.get(toDirectory);

			if (Files.isDirectory(to)) {
				try {
					Files.copy(from, to.resolve(from.getFileName()));
					System.out.println(Color.GREEN + "Archivo copiado" + Color.RESET);
				} catch (Exception e) {
					System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET);
				}
			} else {
				System.out.println(Color.RED + "Error: " + toDirectory + " no es un directorio" + Color.RESET);
			}
		} else {
			System.out.println(Color.RED + "Error: " + fromFile + " no existe" + Color.RESET);
		}
	}

	public void mv(String fromFile, String toDirectory){
		Path from = Paths.get(fromFile);

		if (Files.exists(from)) {
			Path to = Paths.get(toDirectory);

			if (Files.isDirectory(to)) {
				try {
					Files.move(from, to.resolve(from.getFileName()));
					System.out.println(Color.GREEN + "Archivo copiado" + Color.RESET);
				} catch (Exception e) {
					System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET);
				}
			} else {
				try {
					from.toFile().renameTo(new File(from.getParent().toString() + "/" + toDirectory));
					System.out.println(Color.GREEN + "Archivo renombrado" + Color.RESET);
				} catch (Exception e) {
					System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET);
				}
			}
		} else {
			System.out.println(Color.RED + "Error: " + fromFile + " no existe" + Color.RESET);
		}
	}

	public void rm(String path, String option){
		Path file = Paths.get(path);

		if (Files.exists(file)) {
			if (Files.isDirectory(file)) {
				if (option != null && option.equals("-r")) {
					try {
						recursiveDelete(file);
						System.out.println(Color.GREEN + "Directorio eliminado" + Color.RESET);
					} catch (Exception e) {
						System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET);
					}
				} else {
					System.out.println(Color.RED + "Error: " + path + " es un directorio, para borrarlo utilice " + Color.BLUE + "-r" + Color.RED + " como opción" + Color.RESET);
				}
			} else {
				try {
					Files.delete(file);
					System.out.println(Color.GREEN + "Archivo eliminado" + Color.RESET);
				} catch (Exception e) {
					System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET);
				}
			}
		} else {
			System.out.println(Color.RED + "Error: " + path + " no existe" + Color.RESET);
		}
	}

	public void mkdir(String name, String option){
		Path dir = Paths.get(name);
		
		if (Files.exists(dir)) {
			System.out.println(Color.RED + "Error: " + name + " ya existe" + Color.RESET);
		} else {
			try {
				Files.createDirectory(dir);
				System.out.println(Color.GREEN + "Directorio creado" + Color.RESET);
			} catch (java.nio.file.NoSuchFileException e) {
				if (option != null && option.equals("-p")) {
					try {
						createDirectoryRecursive(dir);
						System.out.println(Color.GREEN + "Directorio creado" + Color.RESET);
					} catch (java.lang.NullPointerException ex) {
						System.out.println(Color.RED + "Error: " + dir + " no es una ruta válida" + Color.RESET);
					} catch (Exception ex) {
						System.out.println(Color.RED + "Error: " + ex.getMessage() + Color.RESET);
					}
				} else {
					System.out.println(Color.RED + "Error: " + name + " no existe, para crearlo utilice " + Color.BLUE + "-p" + Color.RED + " como opción" + Color.RESET);
				}
			} catch (Exception e) {
				System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET);
			}
		}
	}

	public void touch(String name){
		Path file = Paths.get(name);

		if (Files.exists(file)) {
			System.out.println(Color.RED + "Error: " + name + " ya existe" + Color.RESET);
		} else {
			try {
				Files.createFile(file);
				System.out.println(Color.GREEN + "Archivo creado" + Color.RESET);
			} catch (Exception e) {
				System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET);
			}
		}
	}

	public void grep(String pattern, String path, String option){
		// Puedes usar -e al final del comando para hacer que la busqueda sea exacta (Mayusculas y Minisculas deben coincidir)
		if (path == null)
			path = System.getProperty("user.dir");
		else
			if (path.equals("-e")) {
				option = "-e";
				path = System.getProperty("user.dir");
			}
		

		Path dir = Paths.get(path);
			
		if (Files.exists(dir) && Files.isDirectory(dir)) {
			System.out.println(Color.GREEN + "Directorio " + Color.ORANGE + path + Color.RESET);
			System.out.println("--------------------------------------------------");
			
			try {
				DirectoryStream<Path> stream = Files.newDirectoryStream(dir);

				for (Path file : stream) {
					if ((option.equals("-e") && file.getFileName().toString().contains(pattern)) || (!option.equals("-e") && file.getFileName().toString().toLowerCase().contains(pattern.toLowerCase()))) {
						if (Files.isDirectory(file)) {
							System.out.println(Color.BLUE + file.getFileName() + Color.RESET);
						} else {
							System.out.println(file.getFileName());
						}
					}
				}
			} catch (Exception e) {
				System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET);
			}
		} else {
			System.out.println(Color.RED + "Error: " + path + " no es un directorio" + Color.RESET);
		}
	} 

	public static void main(String[] args) {
		Console console = new Console();

		String msg = "";

		System.out.println("");

		do {
			System.out.print(Color.GREEN + System.getProperty("user.name") + "@" + System.getProperty("user.name") +": " + Color.CYAN + System.getProperty("user.dir") + Color.RESET + "$ ");
			msg = readS.nextLine();
			args = msg.split(" ");

			System.out.println("");

			if (args.length > 0) {
				switch (args[0]) {
					case "ls":
						if (args.length == 1)
							console.ls(null);
						else
							if (args.length == 2)
								console.ls(args[1]);
							else
								System.out.println(Color.RED + "Error de formato: ls <ruta (opcional)>" + Color.RESET);
						break;
					case "cp":
					System.out.println(args.length);
						if (args.length == 3)
							console.cp(args[1], args[2]);
						else
							System.out.println(Color.RED + "Error de formato: cp <archivo> <directorio>" + Color.RESET);
						break;
					case "mv":
						if (args.length == 3)
							console.mv(args[1], args[2]);
						else
							System.out.println(Color.RED + "Error de formato: mv <archivo> <directorio/nombre>" + Color.RESET);
						break;
					case "rm":
						if (args.length == 2)
							console.rm(args[1], null);
						else
							if (args.length == 3)
								console.rm(args[1], args[2]);
							else
								System.out.println(Color.RED + "Error de formato: rm <ruta> <opcion (opcional)>" + Color.RESET);
						break;
					case "mkdir":
						if (args.length == 2)
							console.mkdir(args[1], null);
						else
							if (args.length == 3)
								console.mkdir(args[1], args[2]);
							else
								System.out.println(Color.RED + "Error de formato: mkdir <nombre> <opcion (opcional)>" + Color.RESET);
						break;
					case "touch":
						if (args.length == 2)
							console.touch(args[1]);
						else
							System.out.println(Color.RED + "Error de formato: touch <ruta/nombre>" + Color.RESET);
						break;
					case "grep":
						// Puedes usar -e al final del comando para hacer que la busqueda sea exacta (Mayusculas y Minisculas deben coincidir)
						switch (args.length) {
							case 2:
								console.grep(args[1], null, null);
								break;
							case 3:
								console.grep(args[1], args[2], null);
								break;
							case 4:
								console.grep(args[1], args[2], args[3]);
								break;
							default:
								System.out.println(Color.RED + "Error de formato: grep <patron> <ruta (opcional)> <opcion (opcional)>" + Color.RESET);
								break;
						}
						break;
				}
			}

			System.out.println("");
		} while (!msg.equals("exit"));
	}
}