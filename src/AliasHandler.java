import java.io.IOException;
import java.util.ArrayList;
import java.io.*;

public class AliasHandler {
    int a;
    String userAlias;
    File aliases = new File(".chomsky/aliases.txt");

    // -a / -A : Add Alias
    void addAlias(String args, ArrayList<Alias> list) throws IOException {
        FileOutputStream oAliases = new FileOutputStream(aliases);
        DataOutputStream oAlias =  new DataOutputStream(oAliases);
        oAlias.writeUTF(userAlias);
        oAlias.close();

        System.out.println(String.format("Add: %s", userAlias));
    }

    // -r / -R : Remove Alias
    void removeAlias(String args, ArrayList<Alias> list) throws IOException{
        File temp = File.createTempFile("temp", "");

        BufferedReader reader = new BufferedReader(new FileReader(aliases));
        BufferedWriter writer = new BufferedWriter(new FileWriter(temp));

        String aliasRemove = userAlias;
        String currentLine;
        String trimmedLine;

        while((currentLine = reader.readLine()) != null) {
            trimmedLine = currentLine.trim();
            if(trimmedLine.equals(aliasRemove)) {
                writer.write(currentLine + System.getProperty("line.separator"));
            }
        }

        writer.close();
        reader.close();
        temp.renameTo(aliases);

        System.out.println(String.format("Remove: %s", args));
    }

    // -m / -M : Modify Alias
    void modifyAlias(String args, ArrayList<Alias> list) throws IOException{
        File temp = File.createTempFile("temp", "");

        BufferedReader reader = new BufferedReader(new FileReader(aliases));
        StringBuffer inputBuffer = new StringBuffer();
        String newAlias;

        while((newAlias = reader.readLine()) != null) {
            newAlias = userAlias;
            inputBuffer.append(newAlias);
        }

        reader.close();
        temp.renameTo(aliases);

        System.out.println(String.format("Modify: %s", args));
    }

    // -l / -L : List Alias
    String listAliases(String args, ArrayList<Alias> list) throws IOException{
        FileInputStream listAliases = new FileInputStream (aliases);

        a = listAliases.read();
        while(a != -1)
        {
            System.out.print((char)a);
            a = listAliases.read();
        }
        listAliases.close();

        return String.format("List: %s", args);
    }
}
