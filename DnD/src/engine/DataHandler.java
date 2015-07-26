package engine;

import jdk.internal.util.xml.impl.Input;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataHandler {
	
	// Will handle reading and writing of the character sheet

	public Boolean CheckSavedData() {
		return false;
	}

	public void WriteData(Character character, File file)
	{
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(character);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in ".concat(file.getCanonicalPath()));
		}catch(IOException i)
		{
            JOptionPane.showMessageDialog(null, i.getMessage());
		}
	}

	public Character ReadData(File file)
	{
		//TODO think about what happens when an invalid file is selected.
		Character character;
		System.out.println("Reading data.");

		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			character = (Character) in.readObject();
			in.close();
			fileIn.close();
		} catch(IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage().concat("\nUsing default character."));
			return new Character();
		} catch(ClassNotFoundException c) {
			JOptionPane.showMessageDialog(null, c.getMessage().concat("\nUsing default character."));
			c.printStackTrace();
			return new Character();
		} catch (ClassCastException ce) {
            JOptionPane.showMessageDialog(null, ce.getMessage().concat("\nUsing default character."));
            return new Character();
        }

		return character;
	}

	public Character ImportData(File file)
	{
		Character character = new Character();
		System.out.println("Reading data.");

        /*
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
        */

		return character;
	}

	public static String ReadInStream(InputStream inStream) {
		final char[] buffer = new char[1024];
		final StringBuilder out = new StringBuilder();
		try (Reader in = new InputStreamReader(inStream, "UTF-8")) {
			for (;;) {
				int rsz = in.read(buffer, 0, buffer.length);
				if (rsz < 0)
					break;
				out.append(buffer, 0, rsz);
			}
		}
		catch (UnsupportedEncodingException ex) {
			System.out.println(ex.getMessage());
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return out.toString();
	}
	
}
