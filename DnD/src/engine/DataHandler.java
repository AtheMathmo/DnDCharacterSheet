package engine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataHandler {
	
	// Will handle reading and writing of the character sheet

	public Boolean CheckSavedData() {
		return false;
	}

	public void WriteData(Character character)
	{
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream("saveData.bin");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(character);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in saveData.bin");
		}catch(IOException i)
		{
			i.printStackTrace();
		}
	}

	public Character ReadData()
	{
		Character character;
		System.out.println("Reading data.");

		try {
			FileInputStream fileIn = new FileInputStream("saveData.bin");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			character = (Character) in.readObject();
			in.close();
			fileIn.close();
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		} catch(ClassNotFoundException c) {
			System.out.println("Character class could not be found.");
			c.printStackTrace();
			return null;
		}

        System.out.println(character.getCharacterName());
		return character;
	}

	public Character ImportData(File file)
	{
		Character character = null;
		System.out.println("Reading data.");

		try {
            character = new Character();
            character.setPlayerName("Imported");
			Path filePath = file.toPath();
			BufferedReader reader = Files.newBufferedReader(filePath);

			String line;

			while ((line = reader.readLine()) != null) {
				if (!(line.startsWith("_") || line.isEmpty() || line.startsWith("("))) {
					String[] splitString = line.split(":");
					String validString = splitString[splitString.length-1];
					System.out.println(validString);
				}
			}

		} catch(Exception ex) {
            ex.printStackTrace();
        }

		return character;
	}
	
}
