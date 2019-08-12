package scraping;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Attr;

public class ScrapingLaLigaEspPublic {

	public static final String xmlFilePath = "xmlfile.xml";

	public static void main(String[] args) throws ParserConfigurationException, TransformerConfigurationException {
		// TODO Auto-generated method stub

		String file = "jugadores2.html";

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// elemento raiz
		org.w3c.dom.Document doc = docBuilder.newDocument();
		org.w3c.dom.Element rootElement = doc.createElement("jugadores");
		doc.appendChild(rootElement);

		if (getStatusFile(file) == 1) {
			Document documento = getHtmlFileToDocument(file);
			//Elements elementos = documento.select("a[href]");
			//Elements elementos = documento.select("table.mlb-scores"); //pizarra
			//Elements elementos = documento.select("div.pure-u-1 > p > a"); //fecha
			Elements elementos = documento.select("tr.alt-row"); //fecha
			
			System.out.println(elementos.size());
			for (Element elem : elementos) {
				// jugador
				org.w3c.dom.Element jugador = doc.createElement("jugador");
				rootElement.appendChild(jugador);
				
				Elements elemntTD = elem.select("td");
				
				String urlJugador = elemntTD.get(0).select("div.info").get(0).select("h3").get(0).select("a").attr("href");
				Attr attrJugadorUrl = doc.createAttribute("url");
				attrJugadorUrl.setValue(urlJugador);
				jugador.setAttributeNode(attrJugadorUrl);
				
				String urlJugadorTeam = elemntTD.get(0).select("a").get(0).attr("href");
				Attr attrJugadorTeamUrl = doc.createAttribute("urlteam");
				attrJugadorTeamUrl.setValue(urlJugadorTeam);
				jugador.setAttributeNode(attrJugadorTeamUrl);
				
				String nombreJugador = elemntTD.get(0).select("div.info").get(0).select("h3").get(0).select("a").get(0).text();
				Attr attrNombre = doc.createAttribute("nombre");
				attrNombre.setValue(nombreJugador);
				jugador.setAttributeNode(attrNombre);
				
				String puntosTotal = elemntTD.get(1).text();
				Attr attrPuntosTotal = doc.createAttribute("puntos");
				attrPuntosTotal.setValue(puntosTotal);
				jugador.setAttributeNode(attrPuntosTotal);
				
				String puntosTotalMedia = elemntTD.get(2).text();
				Attr attrPuntosTotalMedia = doc.createAttribute("media");
				attrPuntosTotalMedia.setValue(puntosTotalMedia);
				jugador.setAttributeNode(attrPuntosTotalMedia);
				
				String puntosLocal = elemntTD.get(3).text();
				Attr attrPuntosLocal = doc.createAttribute("local");
				attrPuntosLocal.setValue(puntosLocal);
				jugador.setAttributeNode(attrPuntosLocal);
				
				String puntosLocalMedia = elemntTD.get(4).text();
				Attr attrPuntosLocalMedia = doc.createAttribute("medialocal");
				attrPuntosLocalMedia.setValue(puntosLocalMedia);
				jugador.setAttributeNode(attrPuntosLocalMedia);
				
				String puntosVisita = elemntTD.get(5).text();
				Attr attrPuntosVisita = doc.createAttribute("visita");
				attrPuntosVisita.setValue(puntosVisita);
				jugador.setAttributeNode(attrPuntosVisita);
				
				String puntosVisitaMedia = elemntTD.get(6).text();
				Attr attrPuntosVisitaMedia = doc.createAttribute("mediavisita");
				attrPuntosVisitaMedia.setValue(puntosVisitaMedia);
				jugador.setAttributeNode(attrPuntosVisitaMedia);
				
				String puntosRacha = elemntTD.get(7).text();
				Attr attrPuntosRacha = doc.createAttribute("racha");
				attrPuntosRacha.setValue(puntosRacha);
				jugador.setAttributeNode(attrPuntosRacha);
				
				String precio = elemntTD.get(8).attr("data-value");
				Attr attrPrecio = doc.createAttribute("precio");
				attrPrecio.setValue(precio);
				jugador.setAttributeNode(attrPrecio);
				
				String posicion = elemntTD.get(9).text();
				Attr attrPosicion = doc.createAttribute("posicion");
				attrPosicion.setValue(posicion);
				jugador.setAttributeNode(attrPosicion);
				
//				String autor = elem.getElementsByClass("currency-name-container").text();
//				String price = elem.getElementsByClass("price").text();
//				System.out.println(titulo + ":" + autor + ":" + price);

				
				// atributo del elemento empleado
				
				
				
				
				

				// nombre
//				org.w3c.dom.Element nombre = doc.createElement("nombre");
//				nombre.appendChild(doc.createTextNode(autor));
//				empleado.appendChild(nombre);

				// valor
//				org.w3c.dom.Element valor = doc.createElement("valor");
//				valor.appendChild(doc.createTextNode(price));
//				empleado.appendChild(valor);
			}
		}

		
		//nombre del fichero
		Date  fecha = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss"); 
		System.out.println("Hora y fecha: "+hourdateFormat.format(fecha));
		String nombreFichero = hourdateFormat.format(fecha);
		
		// escribimos el contenido en un archivo .xml
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		String ruta = "dataXML\\";
		StreamResult result = new StreamResult(new File(ruta,nombreFichero + ".xml"));

		// StreamResult result = new StreamResult(new File("archivo.xml"));
		// Si se quiere mostrar por la consola...
		// StreamResult result = new StreamResult(System.out);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("File saved!");

	}

	/**
	 * Con esta método compruebo el Status code de la respuesta que recibo al hacer
	 * la petición EJM: 
	 * 200 OK 
	 * 300 Multiple Choices 
	 * 301 Moved Permanently 
	 * 305 Use Proxy 
	 * 400 Bad Request 
	 * 403 Forbidden 
	 * 404 Not Found 
	 * 500 Internal Server Error
	 * 502 Bad Gateway 
	 * 503 Service Unavailable
	 * 
	 * @param url
	 * @return Status Code
	 */
	public static int getStatusConnectionCode(String url) {

		Response response = null;

		try {
			response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
		}
		return response.statusCode();
	}

	/**
	 * Con este método devuelvo un objeto de la clase Document con el contenido del
	 * HTML de la web que me permitirá parsearlo con los métodos de la librelia
	 * JSoup
	 * 
	 * @param url
	 * @return Documento con el HTML
	 */
	public static Document getHtmlDocument(String url) {

		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		}
		return doc;
	}
	
	
	public static int getStatusFile(String file) {
		return 1;
	}
	
	
	public static Document getHtmlFileToDocument(String file) {

		File input = new File("data/"+file);
		Document doc = null;
		try {
			doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		}
		return doc;
	}
}
