package model;

import java.io.File;
import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ManipuladorXML {

    private static final HashMap<String, String> arquivo = new HashMap<>();

    //listar aqui todos os arquivos existentes e seus caminhos
    static {
        arquivo.put("ListUser", "src/xml/users.xml");
    }

    public static Object select(String arquivo) throws JAXBException {
        File arqExistente = new File(getArquivo(arquivo));
        JAXBContext jaxbContext = JAXBContext.newInstance(ListUser.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(arqExistente);
    }

    public static void insert(String arquivo, Object values) throws JAXBException {
        File arqNovo = new File(getArquivo(arquivo));
        JAXBContext jaxbContext = JAXBContext.newInstance(ListUser.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(values, arqNovo);
    }

    public static boolean isEmpty(String arquivo) {
        File arq = new File(getArquivo(arquivo));
        return arq.length() == 0;
    }

    private static String getArquivo(String arquivos) {
        return arquivo.get(arquivos);
    }
}
